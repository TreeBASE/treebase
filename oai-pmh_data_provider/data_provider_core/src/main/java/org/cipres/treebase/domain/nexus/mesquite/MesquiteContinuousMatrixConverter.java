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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mesquite.cont.lib.ContinuousData;
import mesquite.lib.Double2DArray;
import mesquite.lib.characters.CharacterData;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.jdbc.CompoundElementJDBC;
import org.cipres.treebase.dao.jdbc.ContinuousMatrixElementJDBC;
import org.cipres.treebase.dao.jdbc.ContinuousMatrixJDBC;
import org.cipres.treebase.dao.jdbc.ItemDefinitionJDBC;
import org.cipres.treebase.dao.jdbc.MatrixColumnJDBC;
import org.cipres.treebase.dao.jdbc.MatrixJDBC;
import org.cipres.treebase.domain.matrix.CompoundMatrixElement;
import org.cipres.treebase.domain.matrix.ContinuousChar;
import org.cipres.treebase.domain.matrix.ContinuousMatrix;
import org.cipres.treebase.domain.matrix.ContinuousMatrixElement;
import org.cipres.treebase.domain.matrix.ItemDefinition;
import org.cipres.treebase.domain.matrix.ItemDefinitionHome;
import org.cipres.treebase.domain.matrix.MatrixColumn;
import org.cipres.treebase.domain.matrix.MatrixElement;
import org.cipres.treebase.domain.matrix.MatrixRow;

/**
 * MesquiteContinuousMatrixConverter.java
 * 
 * Created on Feb 13, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class MesquiteContinuousMatrixConverter extends MesquiteMatrixConverter {

	private ItemDefinitionHome mItemDefinitionHome;

	private Map<Double2DArray, ItemDefinition> mItemDefMap;
	private String[] mItemDefinitionNames;
	private Double2DArray[] mItemDefinitions;


	/**
	 * Constructor.
	 */
	public MesquiteContinuousMatrixConverter() {
		super();
	}

	/**
	 * Return the ItemDefinitionHome field.
	 * 
	 * @return ItemDefinitionHome mItemDefinitionHome
	 */
	private ItemDefinitionHome getItemDefinitionHome() {
		return mItemDefinitionHome;
	}

	/**
	 * Set the ItemDefinitionHome field.
	 */
	public void setItemDefinitionHome(ItemDefinitionHome pNewItemDefinitionHome) {
		mItemDefinitionHome = pNewItemDefinitionHome;
	}

	/**
	 * Return the ItemDefMap field.
	 * 
	 * @return Map<Double2DArray, ItemDefinition> mItemDefMap
	 */
	private Map<Double2DArray, ItemDefinition> getItemDefMap() {
		if (mItemDefMap == null) {
			mItemDefMap = new HashMap<Double2DArray, ItemDefinition>();
		}
		return mItemDefMap;
	}

	/**
	 * Return the ItemDefinitions field.
	 * 
	 * @return Double2DArray[] mItemDefinitions
	 */
	private Double2DArray[] getItemDefinitions() {
		return mItemDefinitions;
	}

	/**
	 * Set the ItemDefinitions field.
	 */
	private void setItemDefinitions(Double2DArray[] pNewItemDefinitions) {
		mItemDefinitions = pNewItemDefinitions;
	}
	
	/**
	 * Return the ItemDefinitionNames field.
	 * 
	 * @return String[] mItemDefinitionNames
	 */
	private String[] getItemDefinitionNames() {
		return mItemDefinitionNames;
	}

	/**
	 * Set the ItemDefinitionNames field.
	 */
	private void setItemDefinitionNames(String[] pNewItemDefinitionNames) {
		mItemDefinitionNames = pNewItemDefinitionNames;
	}
	
	/**
	 * create a continuous matrix object for Mesquite ContinuoudData.
	 * 
	 * @see org.cipres.treebase.domain.nexus.mesquite.MesquiteMatrixConverter#createMatrix(mesquite.lib.characters.CharacterData)
	 */
	@Override
	protected MatrixJDBC createMatrix(CharacterData pMesqMatrix) {

		ContinuousData continuousData = (ContinuousData) pMesqMatrix;
		ContinuousMatrix m = new ContinuousMatrix();
		
		ContinuousMatrixJDBC matrixJDBC = new ContinuousMatrixJDBC(m, continuousData, this);

		int numChars = continuousData.getNumChars();
		int numItems = continuousData.getNumItems();

		mItemDefinitions = new Double2DArray[numItems];
		mItemDefinitionNames = new String[numItems];
		
		Set<ItemDefinition> itemDefs = new HashSet<ItemDefinition>();

		List<MatrixColumnJDBC> columnJDBCs = new ArrayList<MatrixColumnJDBC>();
		List<ItemDefinitionJDBC> itemDefJDBCs = new ArrayList<ItemDefinitionJDBC>();
		
		// add Item definitions if there are any:
		for (int j = 0; j < numItems; j++) {
			Double2DArray anItem = continuousData.getItem(j);

			String itemName = continuousData.getItemName(j);
			mItemDefinitionNames[j] = itemName;
			mItemDefinitions[j] = anItem;
				
			// This can be null, means the default definition 'AVG' is used.
			ItemDefinition definition = getItemDefinition(itemName);
			//ItemDefinitionJDBC itemDefJDBC = new ItemDefinitionJDBC();
			//itemDefJDBC.setColumnIndex(pNewColumnIndex);
			//itemDefJDBC.setItemDefinitionDesc(itemName);
			
			if (definition != null) {
				itemDefs.add(definition);
				getItemDefMap().put(anItem, definition);
			}
		}
		
		if (!itemDefs.isEmpty()) {
			m.setItemDefinitions(itemDefs);
		}

		// add character/column:
		for (int i = 0; i < numChars; i++) {

			//MatrixColumn aColumn = new MatrixColumn();
			MatrixColumnJDBC aColumnJDBC = new MatrixColumnJDBC();
			
			ContinuousChar tbChar = new ContinuousChar();

			String charName = continuousData.getCharacterName(i);
			tbChar.setDescription(charName);

			//aColumn.setCharacter(tbChar);
			aColumnJDBC.setPhyloChar(tbChar);
			
			//Note: Set for itemDefs per column. Disabled for now. 
//			if (!itemDefs.isEmpty()) {
//				aColumn.setItemDefinitions(itemDefs);
//			}
			//m.addColumn(aColumn);
			columnJDBCs.add(aColumnJDBC);
		}

		matrixJDBC.setMatrixColumnJDBCs(columnJDBCs);
		return matrixJDBC;
	}

	/**
	 * Returns an ItemDefinition object. Returns the existing one if the item definition is
	 * predefined. Otherwise creates a new item definition.
	 * 
	 * @param pItemName
	 * @return
	 */
	private ItemDefinition getItemDefinition(String pItemName) {
		ItemDefinition anItem = null;
		if (!TreebaseUtil.isEmpty(pItemName)) {
			anItem = getItemDefinitionHome().findPredefinedItemDefinition(pItemName);

			if (anItem == null) {
				// not default, create one:
				anItem = new ItemDefinition();
				anItem.setDescription(pItemName);
			}
		}

		return anItem;
	}

	/**
	/**
	 * Not used anymore. Replaced by createColJDBCElements()
	 * @see org.cipres.treebase.domain.nexus.mesquite.MesquiteMatrixConverter#addRowElements(org.cipres.treebase.domain.matrix.MatrixRow,
	 *      int, mesquite.lib.characters.CharacterData)
	 */
	@Override
	@Deprecated
	protected void addRowElements(MatrixRow pRow, int pRowIndex, CharacterData pMesqMatrix) {
		ContinuousData continuousData = (ContinuousData) pMesqMatrix;
		int numChars = continuousData.getNumChars();
		int numItems = continuousData.getNumItems();

		List<MatrixColumn> columns = pRow.getMatrix().getColumnsReadOnly();

		// add matrix row elements
		for (int colIndex = 0; colIndex < numChars; colIndex++) {
			// first find mesquite char and treebase char:
			MatrixColumn matrixColumn = columns.get(colIndex);
			//ContinuousChar columnChar = (ContinuousChar) matrixColumn.getCharacter();

			MatrixElement element = null;

			if (numItems > 1) {

				CompoundMatrixElement compoundElement = new CompoundMatrixElement();
				Set<ContinuousMatrixElement> elements = new HashSet<ContinuousMatrixElement>();

				// pre calculate the compound values and cache it in compoundElement.
				StringBuilder compoundValues = new StringBuilder();
				compoundValues.append('(');

				for (int j = 0; j < numItems; j++) {
					Double2DArray anItem = continuousData.getItem(j);
					double elementValue = anItem.getValue(colIndex, pRowIndex);

					ContinuousMatrixElement anElement = new ContinuousMatrixElement();
					anElement.setValue(elementValue);
					anElement.setColumn(matrixColumn);

					// This can be null, means the default definition 'AVG' is used.
					ItemDefinition definition = getItemDefMap().get(anItem);

					if (definition != null) {
						anElement.setDefinition(definition);
					}

					anElement.appendValue(compoundValues);

					if (j < numItems - 1) {
						compoundValues.append(' ');
					}
				}

				compoundValues.append(')');
				compoundElement.setCompoundValue(compoundValues.toString());

				compoundElement.setElements(elements);
				element = compoundElement;

			} else {
				// single item definition, use single element:
				double elementValue = continuousData.getState(colIndex, pRowIndex, 0);
				ContinuousMatrixElement anElement = new ContinuousMatrixElement();
				anElement.setValue(elementValue);
				anElement.setColumn(matrixColumn);

				element = anElement;
			}
			element.setColumn(matrixColumn);
			pRow.addElement(element);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.mesquite.MesquiteMatrixConverter#processMatrixElements(org.cipres.treebase.dao.jdbc.MatrixJDBC,
	 *      java.sql.Connection)
	 */
	@Override
	public void processMatrixElements(MatrixJDBC pMatrixJDBC, Connection pCon) {
		if (pMatrixJDBC == null) {
			return ;
		}

		List<ContinuousMatrixElementJDBC> elements = new ArrayList<ContinuousMatrixElementJDBC>();
								
			long[] colIds = pMatrixJDBC.getColIDs();
			ContinuousMatrixJDBC cMatrixJDBC = (ContinuousMatrixJDBC) pMatrixJDBC;
			
			for (int colIndex = 0; colIndex < colIds.length; colIndex++) {
				long colId = colIds[colIndex];
				
				createColJDBCElements(colId, colIndex, cMatrixJDBC, elements);
				
			}
		
			ContinuousMatrixElementJDBC.batchContinuousElements(elements, pCon);
	}
	
	/**
	 * Create matrixJDBC elements for a column and append them to the elements list.
	 * 
	 */
	private void createColJDBCElements(
		long pColId,
		int pColIndex,
		ContinuousMatrixJDBC pMatrixJDBC,
		List<ContinuousMatrixElementJDBC> pElements) {
		
		ContinuousData continuousData = (ContinuousData) pMatrixJDBC.getMesquiteCharacterData();
		//int numChars = continuousData.getNumChars();
		int numItems = continuousData.getNumItems();

		long[] rowIds = pMatrixJDBC.getRowIDs();
		//String gapSymbol = pMatrixJDBC.getCharacterMatrix().getGapSymbol();
		//String missingSymbol = pMatrixJDBC.getCharacterMatrix().getMissingSymbol();

		//FIXME: worry about symbols string for continuous matrix??

		if (numItems > 1) {
			// multiple item definitions, use single element as a place holder, add an entry
			// to the matrix compoundElement list:
		
			// add elements for the column
			for (int rowIndex = 0; rowIndex < rowIds.length; rowIndex++) {
				
				// place holder:
				ContinuousMatrixElementJDBC element = new ContinuousMatrixElementJDBC();
				
				// the compound element to be added later:
				CompoundElementJDBC compoundElement = new CompoundElementJDBC();
				
				//Set<ContinuousMatrixElement> elements = new HashSet<ContinuousMatrixElement>();

				// pre calculate the compound values and cache it in compoundElement.
				StringBuilder compoundValues = new StringBuilder();
				compoundValues.append('(');

				for (int j = 0; j < numItems; j++) {
					Double2DArray anItem = getItemDefinitions()[j];
					double elementValue = anItem.getValue(pColIndex, rowIndex);

					compoundElement.getValues().add(elementValue);
					
					String itemName = getItemDefinitionNames()[j];

					// This can be null, means the default definition 'AVG' is used.
					Long itemDefId = pMatrixJDBC.getItemDefNameToIDMap().get(itemName);
					if (itemDefId == null) {
						itemDefId = -1L;
					}
					compoundElement.getItemDefinitionIDs().add(itemDefId);
					
		
					compoundValues.append(elementValue);
					if (j < numItems - 1) {
						compoundValues.append(' ');
					}
				}

				compoundValues.append(')');
				compoundElement.setCompoundValue(compoundValues.toString());

				compoundElement.setColIndex(pColIndex);
				compoundElement.setRowIndex(rowIndex);
				pMatrixJDBC.getCompoundElements().add(compoundElement);
				
				element.setElementOrder(pColIndex);
				element.setMatrixRowID(rowIds[rowIndex]);
				element.setMatrixColID(pColId);
				
				pElements.add(element);
			}
		} else {
			// single item definition, use single element:
			
			// add elements for the column
			for (int rowIndex = 0; rowIndex < rowIds.length; rowIndex++) {
				
				ContinuousMatrixElementJDBC element = new ContinuousMatrixElementJDBC();
				
				if (continuousData.isInapplicable(pColIndex, rowIndex)) {
					// gap:
					element.setGap((short)1);
				} else if (!continuousData.isUnassigned(pColIndex, rowIndex)) {

					double elementValue = continuousData.getState(pColIndex, rowIndex, 0);
					element.setValue(elementValue);					
				}
				
				element.setElementOrder(pColIndex);
				element.setMatrixRowID(rowIds[rowIndex]);
				element.setMatrixColID(pColId);
				
				pElements.add(element);
	
		}
			
	}

}
}
