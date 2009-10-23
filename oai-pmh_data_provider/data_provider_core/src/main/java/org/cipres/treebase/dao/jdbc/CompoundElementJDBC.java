/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */

package org.cipres.treebase.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

/**
 * CompoundElementJDBC.java
 * 
 * Created on Jan 16, 2008
 * @author Jin Ruan 
 */
public class CompoundElementJDBC {

	private int mRowIndex;
	private int mColIndex;
	//private short mAndLogic;

	//for continuous elements:
	private List<Double> mValues = new ArrayList<Double>();
	private List<Long> mItemDefinitionIDs = new ArrayList<Long>();
	private String mCompoundValue;
	
	//for discrete elements:
	private List<Long> mDiscreteCharState_IDs = new ArrayList<Long>();
	
	
//	UPDATE MATRIXELEMENT 
//    SET TYPE='', MATRIXELEMENT_ID=0, VERSION=0, 
//	 COMPOUNDVALUE='', ANDLOGIC=0, 
//	VALUE=0, GAP=0, MATRIXCOLUMN_ID=0, ITEMDEFINITION_ID=0, MATRIXROW_ID=0, 
//	DISCRETECHARSTATE_ID=0, ELEMENT_ORDER=0
//    WHERE 

//	INSERT INTO COMPOUND_ELEMENT(COMPOUND_ID, ELEMENT_ID) 
//    VALUES(0, 0)

	
	/**
	 * Constructor.
	 */
	public CompoundElementJDBC() {
		super();
	}


	/**
	 * Return the CompoundValue field.
	 * 
	 * @return String mCompoundValue
	 */
	public String getCompoundValue() {
		return mCompoundValue;
	}

	/**
	 * Set the CompoundValue field.
	 */
	public void setCompoundValue(String pNewCompoundValue) {
		mCompoundValue = pNewCompoundValue;
	}
	
//	/**
//	 * Return the AndLogic field.
//	 * 
//	 * @return short mAndLogic
//	 */
//	public short getAndLogic() {
//		return mAndLogic;
//	}
//
//	/**
//	 * Set the AndLogic field.
//	 */
//	public void setAndLogic(short pNewAndLogic) {
//		mAndLogic = pNewAndLogic;
//	}
//	
	
	/**
	 * Return the ColIndex field.
	 * 
	 * @return int mColIndex
	 */
	public int getColIndex() {
		return mColIndex;
	}

	/**
	 * Set the ColIndex field.
	 */
	public void setColIndex(int pNewColIndex) {
		mColIndex = pNewColIndex;
	}
	
	/**
	 * Return the RowIndex field.
	 * 
	 * @return int mRowIndex
	 */
	public int getRowIndex() {
		return mRowIndex;
	}

	/**
	 * Set the RowIndex field.
	 */
	public void setRowIndex(int pNewRowIndex) {
		mRowIndex = pNewRowIndex;
	}

	/**
	 * @return the values
	 */
	public List<Double> getValues() {
		return mValues;
	}

	/**
	 * Return the DiscreteCharState_IDs field.
	 * 
	 * @return List<Long> mDiscreteCharState_IDs
	 */
	public List<Long> getDiscreteCharState_IDs() {
		return mDiscreteCharState_IDs;
	}


	/**
	 * Return the ItemDefinitionIDs field.
	 * 
	 * @return List<Long> mItemDefinitionIDs
	 */
	public List<Long> getItemDefinitionIDs() {
		return mItemDefinitionIDs;
	}


}
