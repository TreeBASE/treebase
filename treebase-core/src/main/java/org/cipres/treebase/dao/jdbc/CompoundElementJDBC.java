
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
