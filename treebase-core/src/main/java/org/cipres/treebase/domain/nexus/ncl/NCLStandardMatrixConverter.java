/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.domain.nexus.ncl;

import java.util.List;

import org.cipres.CipresIDL.api1.DataMatrix;

import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.DiscreteChar;
import org.cipres.treebase.domain.matrix.DiscreteCharState;
import org.cipres.treebase.domain.matrix.DiscreteMatrixElement;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixColumn;
import org.cipres.treebase.domain.matrix.MatrixDataType;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.StandardMatrix;

/**
 * NCLStandardMatrixConverter.java
 * 
 * Created on Jun 13, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class NCLStandardMatrixConverter extends NCLMatrixConverter {

	/**
	 * Constructor.
	 */
	public NCLStandardMatrixConverter() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.ncl.NCLMatrixConverter#convertMatrix()
	 */
	@Override
	protected Matrix convertMatrix() {

		// DataMatrix cipresMatris = getCipresMatrix();
		// int numState = cipresMatris.m_numStates;
		// int numChar = cipresMatris.m_numCharacters;
		// short[][] cipresElements = cipresMatris.m_matrix;
		// short[][] charStateLookup = cipresMatris.m_charStateLookup;
		// String symbols = cipresMatris.m_symbols;

		CharacterMatrix matrix = new StandardMatrix();

		// NOTES: properties that not in Cipres matrix:
		// matrix.setTitle();
		// matrix.setDescription();
		// matrix.setGapSymbol("-"); // assumed by NCL
		// matrix.setMissingSymbol("?"); //NCL default
		// matrix.setCaseSensitive();

		MatrixDataType dataType = getMatrixDataType();
		matrix.setDataType(dataType);

		// create columns first, then rows.
		createColumns(matrix);
		createRows(matrix);

		return matrix;
	}

	/**
	 * Create columns and associate to characters.
	 * 
	 * @param pMatrix
	 */
	private void createColumns(CharacterMatrix pMatrix) {

		DataMatrix cipresMatris = getCipresMatrix();
		int numChar = cipresMatris.m_numCharacters;
		String symbols = cipresMatris.m_symbols;
		MatrixDataType dataType = getMatrixDataType();

		// For standard matrix, we know it has to be discrete char:
		DiscreteChar defaultCharacter = (DiscreteChar) dataType.getDefaultCharacter();

		for (int i = 0; i < numChar; i++) {
			MatrixColumn aColumn = new MatrixColumn();
			DiscreteChar columnChar = defaultCharacter;

			if (columnChar == null) {
				// it is probably a Standard dataType,
				// need to create characters for each column.

				// TODO: the Cipres does not carry the charLabel and stateLabel info yet.
				// ALERT: create numbered states, and add to char.
				// rules:
				// 1. add one state for each Symbol, which usually includes "?"
				// 2. the symbol "-1" in NCL has no corresponding state. It is represented
				// by the MatrixElement has NULL for associated DiscreteState.
				// 3. sometimes the symbol list + '-' still does not match the size
				// of char lookup array. It is likely 'N' is added to the lookup
				// array. Don't add it now, let's see what happens.
				columnChar = new DiscreteChar();

				for (int j = 0; j < symbols.length(); j++) {
					String aSymbol = symbols.substring(j, j + 1);

					DiscreteCharState aState = new DiscreteCharState();
					aState.setDescription(aSymbol);

					columnChar.addCharState(aState);
				}
			}
			aColumn.setCharacter(columnChar);

			pMatrix.addColumn(aColumn);
		}
	}

	/**
	 * 
	 * @param cipresElements
	 * @param symbols
	 * @param matrix
	 */
	private void createRows(CharacterMatrix pMatrix) {

		DataMatrix cipresMatris = getCipresMatrix();
		// int numState = cipresMatris.m_numStates;
		// int numChar = cipresMatris.m_numCharacters;
		short[][] cipresElements = cipresMatris.m_matrix;
		short[][] charStateLookup = cipresMatris.m_charStateLookup;
		String symbols = cipresMatris.m_symbols;

		List<MatrixColumn> columns = pMatrix.getColumnsReadOnly();

		for (int i = 0; i < cipresElements.length; i++) {
			short[] cipresRow = cipresElements[i];

			// create a row with no segment:
			MatrixRow aRow = new MatrixRow();
			for (int j = 0; j < cipresRow.length; j++) {
				int elementInt = cipresRow[j];

				MatrixColumn matrixColumn = columns.get(j);
				DiscreteChar columnChar = (DiscreteChar) matrixColumn.getCharacter();

				// elements:
				// TODO: cache this in a map:
				List<DiscreteCharState> charStates = columnChar.getStatesBySymbol(symbols);

				DiscreteCharState elementState = null;
				if (elementInt != GAP_STATE_VALUE) {

					// TODO check: charStates != null
					if (elementInt >= 0 && elementInt < symbols.length() && charStates != null) {
						elementState = charStates.get(elementInt);
					} else {
						// ALERT: default to unknown.
						elementState = columnChar.getStateByDescription(UNKNOWN_STATE_STRING);
					}
				}

				DiscreteMatrixElement anElement = new DiscreteMatrixElement();

				anElement.setColumn(matrixColumn);
				anElement.setCharState(elementState);

				aRow.addElement(anElement);
			}

			// Default: no segment.
			// RowSegment aSegment = new RowSegment();
			// aSegment.setStartIndex(0);
			// aSegment.setEndIndex(aSegment.getElementsReadOnly().size() - 1);
			//
			// aRow.addSegment(aSegment);

			aRow.setTaxonLabel(getTaxonLables().get(i));

			pMatrix.addRow(aRow);
		}
	}

}
