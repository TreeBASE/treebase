/*
 * Copyright 2007 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.domain.nexus.mesquite;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mesquite.categ.lib.CategoricalData;
import mesquite.categ.lib.CategoricalState;
import mesquite.lib.characters.CharacterData;

import org.apache.log4j.Logger;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.jdbc.CompoundElementJDBC;
import org.cipres.treebase.dao.jdbc.DiscreteMatrixElementJDBC;
import org.cipres.treebase.dao.jdbc.DiscreteMatrixJDBC;
import org.cipres.treebase.dao.jdbc.MatrixColumnJDBC;
import org.cipres.treebase.dao.jdbc.MatrixJDBC;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.CompoundMatrixElement;
import org.cipres.treebase.domain.matrix.DiscreteChar;
import org.cipres.treebase.domain.matrix.DiscreteCharState;
import org.cipres.treebase.domain.matrix.DiscreteMatrixElement;
import org.cipres.treebase.domain.matrix.MatrixColumn;
import org.cipres.treebase.domain.matrix.MatrixDataType;
import org.cipres.treebase.domain.matrix.MatrixElement;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.StandardMatrix;

/**
 * Convert Mesquite categoricalData matrix to treebase standard matrix.
 * 
 * Created on Feb 13, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class MesquiteStandardMatrixConverter extends MesquiteMatrixConverter {

	private static final Logger LOGGER = Logger.getLogger(MesquiteStandardMatrixConverter.class);

	/**
	 * Constructor.
	 */
	public MesquiteStandardMatrixConverter() {
		super();
	}

	/**
	 * create a standard matrix object for Mesquite Categorical data.
	 * 
	 * @see org.cipres.treebase.domain.nexus.mesquite.MesquiteMatrixConverter#createMatrix(mesquite.lib.characters.CharacterData)
	 */
	@Override
	protected MatrixJDBC createMatrix(CharacterData pMesqMatrix) {
		CategoricalData categoricalData = (CategoricalData) pMesqMatrix;

		CharacterMatrix m = new StandardMatrix();
		DiscreteMatrixJDBC matrixJDBC = new DiscreteMatrixJDBC(m, categoricalData, this);

		int numChars = categoricalData.getNumChars();
		DiscreteChar symbolChar = null; // shared phyloChar for symbols.

		// get symbols as one String. need to do conversion:
		buildSymbols(categoricalData, m);

		// first consider the default char based on matrix data type.
		// For standard matrix, we know it has to be discrete char:
		MatrixDataType dataType = getMatrixDataType();
		DiscreteChar defaultChar = (DiscreteChar) dataType.getDefaultCharacter();
		if (defaultChar != null) {
			matrixJDBC.setDefaultCharID(defaultChar.getId());
		}

		List<MatrixColumnJDBC> columnJDBCs = new ArrayList<MatrixColumnJDBC>();

		// add character/column:
		for (int i = 0; i < numChars; i++) {

			// MatrixColumn aColumn = new MatrixColumn();
			MatrixColumnJDBC aColumnJDBC = new MatrixColumnJDBC();

			DiscreteChar tbChar = defaultChar;

			if (tbChar == null) {
				// it is probably a Standard dataType,
				// need to create characters for each column.

				if (!categoricalData.characterHasName(i) && !categoricalData.hasStateNames(i)) {
					// 1. if there is no character name defined and no state names defined,
					// need to use the defined symbol character
					// which is shared for the entire matrix.
					if (symbolChar == null) {

						tbChar = createCharUseSymbols(categoricalData, i);

						symbolChar = tbChar;

					} else {
						tbChar = symbolChar;

//						if (LOGGER.isDebugEnabled()) {
//							LOGGER.debug(" symbolChar reused for column: " + i); //$NON-NLS-1$
//						}
					}

				} else if (!categoricalData.hasStateNames(i)) {
					// 2. if there is character name defined but still no state names,
					// create a new character but need to use symbols as state names.
					tbChar = createCharUseSymbols(categoricalData, i);

					String charName = null;
					if (categoricalData.characterHasName(i)) {
						charName = categoricalData.getCharacterName(i);
					}
					tbChar.setDescription(charName);

				} else {
					// 3. has stateNames defined,
					// create a new character and set defined state names and symbols.
					tbChar = new DiscreteChar();

					String charName = null;
					if (categoricalData.characterHasName(i)) {
						charName = categoricalData.getCharacterName(i);
					}
					tbChar.setDescription(charName);

					int lastState = categoricalData.maxStateWithName(i);

					for (int j = 0; j <= lastState; j++) {
						String stateName = categoricalData.getStateName(i, j);
						String stateNote = categoricalData.getStateNote(i, j);
						char stateSymbol = categoricalData.getSymbol(j);

						DiscreteCharState tbCharState = new DiscreteCharState();
						tbCharState.setDescription(stateName);
						tbCharState.setSymbol(stateSymbol);

						if (!TreebaseUtil.isEmpty(stateNote)) {
							tbCharState.setNotes(stateNote);
						}

						tbChar.addCharState(tbCharState);
					}
				}

				// ////////////////////////////
				// // Need to create a new char. Add states.
				// if (categoricalData.hasStateNames(i)) {
				//
				// String charName = null;
				// if (categoricalData.characterHasName(i)) {
				// charName = categoricalData.getCharacterName(i);
				// }
				// tbChar = new DiscreteChar();
				// tbChar.setDescription(charName);
				//
				// int lastState = categoricalData.maxStateWithName(i);
				//
				// for (int j = 0; j <= lastState; j++) {
				// String stateName = categoricalData.getStateName(i, j);
				// String stateNote = categoricalData.getStateNote(i, j);
				// char stateSymbol = categoricalData.getSymbol(j);
				//						
				// DiscreteCharState tbCharState = new DiscreteCharState();
				// tbCharState.setDescription(stateName);
				// tbCharState.setSymbol(stateSymbol);
				//						
				// if (!TreebaseUtil.isEmpty(stateNote)) {
				// tbCharState.setNotes(stateNote);
				// }
				//     
				// tbChar.addCharState(tbCharState);
				// }
				// } else {
				// // no state name defined. In this case
				// // 1. if there is no character name defined, need to use the defined symbol
				// character
				// // which is shared for the entire matrix.
				// // 2. if there is character name defined, create a new character but need to
				// // use symbol.
				//					
				// if (symbolChar == null) {
				//
				// tbChar = new DiscreteChar();
				// // do we need the symbol boolean property?
				// //tbChar.setSymbol(true);
				//						
				// int lastSymbol = categoricalData.getMaxSymbolDefined();
				//						
				// for (int j = 0; j <= lastSymbol; j++) {
				// //String stateSymbolStr = categoricalData.getStateSymbol(i, j);
				// String stateNote = categoricalData.getStateNote(i, j);
				// char stateSymbol = categoricalData.getSymbol(j);
				//							
				// DiscreteCharState tbCharState = new DiscreteCharState();
				// //tbCharState.setDescription(stateSymbolStr);
				// tbCharState.setSymbol(stateSymbol);
				//							
				// if (!TreebaseUtil.isEmpty(stateNote)) {
				// tbCharState.setNotes(stateNote);
				// }
				//
				// tbChar.addCharState(tbCharState);
				// }
				//
				// symbolChar = tbChar;
				//
				// if (LOGGER.isDebugEnabled()) {
				// LOGGER.debug(" symbolChar created for column: " + i); //$NON-NLS-1$
				// }
				// } else {
				// tbChar = symbolChar;
				//						
				// if (LOGGER.isDebugEnabled()) {
				// LOGGER.debug(" symbolChar reused for column: " + i); //$NON-NLS-1$
				// }
				// }
				// }
			}

			// aColumn.setCharacter(tbChar);
			// m.addColumn(aColumn);
			aColumnJDBC.setPhyloChar(tbChar);
			columnJDBCs.add(aColumnJDBC);
		}

		matrixJDBC.setMatrixColumnJDBCs(columnJDBCs);
		return matrixJDBC;
	}

	/**
	 * Create a phyloChar, its states are created from the symbols defined in the matrix.
	 * 
	 * @param pCategoricalData
	 * @param pColIndex
	 * @return
	 */
	private DiscreteChar createCharUseSymbols(CategoricalData pCategoricalData, int pColIndex) {
		DiscreteChar tbChar;
		tbChar = new DiscreteChar();
		// FIXME do we need the symbol boolean property?
		// tbChar.setSymbol(true);

		int lastSymbol = pCategoricalData.getMaxSymbolDefined();

		for (int j = 0; j <= lastSymbol; j++) {
			// String stateSymbolStr = categoricalData.getStateSymbol(i, j);
			String stateNote = pCategoricalData.getStateNote(pColIndex, j);
			char stateSymbol = pCategoricalData.getSymbol(j);

			DiscreteCharState tbCharState = new DiscreteCharState();
			// tbCharState.setDescription(stateSymbolStr);
			tbCharState.setSymbol(stateSymbol);

			if (!TreebaseUtil.isEmpty(stateNote)) {
				tbCharState.setNotes(stateNote);
			}

			tbChar.addCharState(tbCharState);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" symbolChar created for column: " + pColIndex); //$NON-NLS-1$
		}
		return tbChar;
	}

	/**
	 * Create the symbols string.
	 * 
	 * Note: lifted from Mesquite code: ManageCategoricalChars.writeCharactersBlock()
	 * 
	 * @param pCategoricalData
	 * @param pMatrix
	 */
	private void buildSymbols(CategoricalData pCategoricalData, CharacterMatrix pMatrix) {
		int maxSt = pCategoricalData.getMaxState();
		int maxS = pCategoricalData.getMaxSymbolDefined();
		if (maxS < 1) {
			maxS = 1;
		}

		if (maxS < maxSt) {
			maxS = maxSt;
		}

		StringBuffer symbolBuf = new StringBuffer();
		for (int i = 0; i <= maxS; i++) {
			//symbolBuf.append(" ").append(pCategoricalData.getSymbol(i));
			//Do not insert space:
			symbolBuf.append(pCategoricalData.getSymbol(i));
		}
		pMatrix.setSymbols(symbolBuf.toString());
	}

	/**
	 * Not used anymore. Replaced by createColJDBCElements()
	 */
	@Deprecated
	protected void addJDBCElements(MatrixRow pRow, int pRowIndex, CharacterData pMesqMatrix) {

		CategoricalData categoricalData = (CategoricalData) pMesqMatrix;
		int numChars = categoricalData.getNumChars();

		CharacterMatrix matrix = pRow.getMatrix();
		List<MatrixColumn> columns = matrix.getColumnsReadOnly();
		String gapSymbolStr = "" + matrix.getGapSymbol();
		String missingSymbolStr = "" + matrix.getMissingSymbol();

		// add matrix row elements
		for (int colIndex = 0; colIndex < numChars; colIndex++) {
			// first find mesquite char and treebase char:
			MatrixColumn matrixColumn = columns.get(colIndex);
			DiscreteChar columnChar = (DiscreteChar) matrixColumn.getCharacter();

			// convert the mesquite character state to treebase state:
			long statesLong = categoricalData.getState(colIndex, pRowIndex);

			MatrixElement element = null;
			if (categoricalData.isInapplicable(colIndex, pRowIndex)) {
				// gap:
				element = createElement(columnChar, gapSymbolStr, 0, matrix);

			} else if (categoricalData.isUnassigned(colIndex, pRowIndex)) {
				// missing
				element = createElement(columnChar, missingSymbolStr, 0, matrix);

			} else {

				int[] states = CategoricalState.expand(statesLong);

				if (states.length > 1) {
					// multiple states, use compound element:
					CompoundMatrixElement compoundElement = new CompoundMatrixElement();
					Set<DiscreteMatrixElement> elements = new HashSet<DiscreteMatrixElement>();

					// pre calculate the compound values and cache it in compoundElement.
					StringBuilder compoundValues = new StringBuilder();
					compoundValues.append('{');

					for (int i = 0; i < states.length; i++) {
						int aState = states[i];
						String stateName = categoricalData.getStateName(colIndex, aState);

						DiscreteMatrixElement anElement = createElement(
							columnChar,
							stateName,
							i,
							matrix);
						anElement.setColumn(matrixColumn);
						elements.add(anElement);

						anElement.appendValue(compoundValues);

						if (i < states.length - 1) {
							compoundValues.append(' ');
						}
					}

					compoundValues.append('}');
					compoundElement.setCompoundValue(compoundValues.toString());

					compoundElement.setElements(elements);
					element = compoundElement;

				} else {
					// single state, use single element:
					String stateName = categoricalData.getStateName(colIndex, states[0]);
					element = createElement(columnChar, stateName, 0, matrix);
				}
			}

			element.setColumn(matrixColumn);
			pRow.addElement(element);
		}
	}

	/**
	 * Find the discrete char state id and set it to the matrixElementJDBC.
	 * 
	 * @param pStateSymbolToIdMap
	 * @param pSymbol
	 * @param pGapSymbol
	 * @param pMissingSymbol
	 * @param pElementJDBC
	 * @return
	 */
	private void findDiscreteStateId(
		Map<String, Long> pStateSymbolToIdMap,
		char pSymbol,
		char pGapSymbol,
		char pMissingSymbol,
		DiscreteMatrixElementJDBC pElementJDBC) {

		long stateId = -1; // signal missing symbol or gap symbol.

		// short gap = 0;
		// element.setGap(gap); // false

		//if (pSymbol != null) {
			// first check the symbol match either "-" gap
			// or "?" missing, set tbState = null;
			if (pSymbol == pGapSymbol) {
				pElementJDBC.setGap((short) 1);
			} else if (pSymbol != pMissingSymbol) {
				Long stateIdValue = pStateSymbolToIdMap.get(String.valueOf(pSymbol));
				if (stateIdValue != null) {
					// need to check null to avoid NPE.
					stateId = stateIdValue;
				}

			}
		//}
		pElementJDBC.setDiscreteCharState_ID(stateId);

	}

	/**
	 * Similar to findDiscreteStateId, this method find the id and append it to the
	 * compoundElementJDBC.
	 * 
	 * The gap element in compoundElements are not supported. The compoundValues field is NOT
	 * populated since we need to deal with symbol anyway.
	 * 
	 * @param pStates
	 * @param pColIndex
	 * @param pCategoricalData
	 * @param pRowSymbolsBuf
	 * @param pStateDescToIdMap
	 * @param pCompoundElement
	 */
	private void addDiscreteStateIds(
		int[] pStates,
		int pColIndex,
		CategoricalData pCategoricalData,
		StringBuffer pRowSymbolsBuf,
		Map<String, Long> pStateSymbolToIdMap,
		CompoundElementJDBC pCompoundElement) {

		//FIXME use ( instead of {
		pRowSymbolsBuf.append('{');

		for (int i = 0; i < pStates.length; i++) {
			int aState = pStates[i];
			//String stateName = pCategoricalData.getStateName(pColIndex, aState);
			char aSymbol = pCategoricalData.getSymbol(aState);

			Long stateId = new Long(-1); // signal missing symbol or gap symbol.

			// Do not support gap or missing. Any statename not found in the current
			// char is treated as missing.

			//if (stateName != null) {
				Long stateIdValue = pStateSymbolToIdMap.get(aSymbol);
				if (stateIdValue != null) {
					// need to check null to avoid NPE.
					stateId = stateIdValue;
				}
			//}
			pCompoundElement.getDiscreteCharState_IDs().add(stateId);

			// TODO: test: do we need to worry about missing symbol here?
			pRowSymbolsBuf.append(aSymbol);

		}

		pRowSymbolsBuf.append('}');

	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.mesquite.MesquiteMatrixConverter#processMatrixElements(org.cipres.treebase.dao.jdbc.MatrixJDBC,
	 *      java.sql.Connection)
	 */
	@Override
	public void processMatrixElements(MatrixJDBC pMatrixJDBC, Connection pCon) {
		if (pMatrixJDBC == null) {
			return;
		}

		List<DiscreteMatrixElementJDBC> elements = new ArrayList<DiscreteMatrixElementJDBC>();

		long[] colIds = pMatrixJDBC.getColIDs();
		DiscreteMatrixJDBC discreteMatrixJDBC = (DiscreteMatrixJDBC) pMatrixJDBC;

		char gapSymbol = pMatrixJDBC.getCharacterMatrix().getGapSymbol();
		char missingSymbol = pMatrixJDBC.getCharacterMatrix().getMissingSymbol();

		for (int colIndex = 0; colIndex < colIds.length; colIndex++) {
			long colId = colIds[colIndex];

			createColJDBCElements(
				colId,
				colIndex,
				gapSymbol,
				missingSymbol,
				discreteMatrixJDBC,
				elements);

		}

		discreteMatrixJDBC.batchUpdateRowSymbol(pCon);
		DiscreteMatrixElementJDBC.batchDiscreteElements(elements, pCon);
	}

	/**
	 * Create matrixJDBC elements for a column and append them to the elements list.
	 * 
	 */
	private void createColJDBCElements(
		long pColId,
		int pColIndex,
		char pGapSymbol,
		char pMissingSymbol,
		DiscreteMatrixJDBC pMatrixJDBC,
		List<DiscreteMatrixElementJDBC> pElements) {

		CategoricalData categoricalData = (CategoricalData) pMatrixJDBC.getMesquiteCharacterData();

		Map<String, Long> stateSymbolToIdMap = pMatrixJDBC.getStateSymbolToIdMap(pColIndex);
		long[] rowIds = pMatrixJDBC.getRowIDs();
		List<StringBuffer> symbolBufs = pMatrixJDBC.getRowSymbolBufs();

		// add discrete elements for the specified column:
		for (int rowIndex = 0; rowIndex < rowIds.length; rowIndex++) {

			StringBuffer rowSymbols = symbolBufs.get(rowIndex);
			char symbol = pMissingSymbol;

			// convert the mesquite character state to treebase state:
			long statesLong = categoricalData.getState(pColIndex, rowIndex);

			DiscreteMatrixElementJDBC element = new DiscreteMatrixElementJDBC();
			if (categoricalData.isInapplicable(pColIndex, rowIndex)) {
				// gap:
				element.setGap((short) 1);
				// symbol = pGapSymbol;
				rowSymbols.append(pGapSymbol);
			} else if (!categoricalData.isUnassigned(pColIndex, rowIndex)) {
				// not missing :
				// element = new DiscreteMatrixElementJDBC();

				int[] states = CategoricalState.expand(statesLong);

				if (states.length > 1) {
					// multiple states, create a compound element.
					// The original single element "element" is still used as a place holder.
					CompoundElementJDBC compoundElement = new CompoundElementJDBC();

					addDiscreteStateIds(
						states,
						pColIndex,
						categoricalData,
						rowSymbols,
						stateSymbolToIdMap,
						compoundElement);

					compoundElement.setColIndex(pColIndex);
					compoundElement.setRowIndex(rowIndex);
					pMatrixJDBC.getCompoundElements().add(compoundElement);

				} else { // XXX: Can the number of states be 0?  If so, we need a guard condition here 20090121 MJD
					// single state, use single element:
					//String stateName = categoricalData.getStateName(pColIndex, states[0]);
					char aSymbol = categoricalData.getSymbol(states[0]);
					rowSymbols.append(aSymbol);

					findDiscreteStateId(
						stateSymbolToIdMap,
						aSymbol,
						pGapSymbol,
						pMissingSymbol,
						element);
				}

			} else {
				rowSymbols.append(pMissingSymbol);
			}

			element.setElementOrder(pColIndex);
			element.setMatrixRowID(rowIds[rowIndex]);
			element.setMatrixColID(pColId);
			pElements.add(element);
		}
	}

	/**
	 * Not used anymore. Replaced by createColJDBCElements()
	 * 
	 * @see org.cipres.treebase.domain.nexus.mesquite.MesquiteMatrixConverter#addRowElements(org.cipres.treebase.domain.matrix.MatrixRow,
	 *      int, mesquite.lib.characters.CharacterData)
	 */
	@Override
	@Deprecated
	protected void addRowElements(MatrixRow pRow, int pRowIndex, CharacterData pMesqMatrix) {

		CategoricalData categoricalData = (CategoricalData) pMesqMatrix;
		int numChars = categoricalData.getNumChars();

		CharacterMatrix matrix = pRow.getMatrix();
		List<MatrixColumn> columns = matrix.getColumnsReadOnly();

		String gapSymbolStr = "" + matrix.getGapSymbol();
		String missingSymbolStr = "" + matrix.getMissingSymbol();

		// add matrix row elements
		for (int colIndex = 0; colIndex < numChars; colIndex++) {
			// first find mesquite char and treebase char:
			MatrixColumn matrixColumn = columns.get(colIndex);
			DiscreteChar columnChar = (DiscreteChar) matrixColumn.getCharacter();

			// convert the mesquite character state to treebase state:
			long statesLong = categoricalData.getState(colIndex, pRowIndex);

			MatrixElement element = null;
			if (categoricalData.isInapplicable(colIndex, pRowIndex)) {
				// gap:
				element = createElement(columnChar, gapSymbolStr, 0, matrix);

			} else if (categoricalData.isUnassigned(colIndex, pRowIndex)) {
				// missing
				element = createElement(columnChar, missingSymbolStr, 0, matrix);

			} else {

				int[] states = CategoricalState.expand(statesLong);

				if (states.length > 1) {
					// multiple states, use compound element:
					CompoundMatrixElement compoundElement = new CompoundMatrixElement();
					Set<DiscreteMatrixElement> elements = new HashSet<DiscreteMatrixElement>();

					// pre calculate the compound values and cache it in compoundElement.
					StringBuilder compoundValues = new StringBuilder();
					compoundValues.append('{');

					for (int i = 0; i < states.length; i++) {
						int aState = states[i];
						String stateName = categoricalData.getStateName(colIndex, aState);

						DiscreteMatrixElement anElement = createElement(
							columnChar,
							stateName,
							i,
							matrix);
						anElement.setColumn(matrixColumn);
						elements.add(anElement);

						anElement.appendValue(compoundValues);

						if (i < states.length - 1) {
							compoundValues.append(' ');
						}
					}

					compoundValues.append('}');
					compoundElement.setCompoundValue(compoundValues.toString());

					compoundElement.setElements(elements);
					element = compoundElement;

				} else {
					// single state, use single element:
					String stateName = categoricalData.getStateName(colIndex, states[0]);
					element = createElement(columnChar, stateName, 0, matrix);
				}
			}

			element.setColumn(matrixColumn);
			pRow.addElement(element);
		}
	}

	/**
	 * 
	 * @param pColumnChar
	 * @param pStateName
	 * @param pStateIndex
	 * @return
	 */
	private DiscreteMatrixElement createElement(
		DiscreteChar pColumnChar,
		String pStateName,
		int pStateIndex,
		CharacterMatrix pMatrix) {

		DiscreteCharState tbState = null;

		DiscreteMatrixElement element = new DiscreteMatrixElement();
		element.setGap(false);

		if (pStateName != null) {
			// first check the symbol match either "-" gap
			// or "?" missing, set tbState = null;
			if (pStateName.compareToIgnoreCase("" + pMatrix.getGapSymbol()) == 0) {
				element.setGap(true);
			} else if (pStateName.compareToIgnoreCase("" + pMatrix.getMissingSymbol()) != 0) {
				tbState = pColumnChar.getStateByDescription(pStateName);

			}
		}
		element.setCharState(tbState);

		return element;
	}

}
