
package org.cipres.treebase.dao.jdbc;

/**
 * ItemDefinitionJDBC.java
 * Helper class for directly JDBC insert to database. Represents an item definition
 * and MatrixColumn_itemDefinition. 
 * 
 * Created on May 20, 2008
 * @author Jin Ruan
 *
 */
public class ItemDefinitionJDBC {

	private Long mItemDefinitionId;

	private int mColumnIndex;
	private String mItemDefinitionDesc;

	/**
	 * Constructor.
	 */
	public ItemDefinitionJDBC() {
		super();
	}

	/**
	 * Return the ItemDefinitionId field.
	 * 
	 * @return Long mItemDefinitionId
	 */
	public Long getItemDefinitionId() {
		return mItemDefinitionId;
	}

	/**
	 * Set the ItemDefinitionId field.
	 */
	public void setItemDefinitionId(Long pNewItemDefinitionId) {
		mItemDefinitionId = pNewItemDefinitionId;
	}
	
	/**
	 * Return the ItemDefinitionDesc field.
	 * 
	 * @return String mItemDefinitionDesc
	 */
	public String getItemDefinitionDesc() {
		return mItemDefinitionDesc;
	}

	/**
	 * Set the ItemDefinitionDesc field.
	 */
	public void setItemDefinitionDesc(String pNewItemDefinitionDesc) {
		mItemDefinitionDesc = pNewItemDefinitionDesc;
	}
	
	/**
	 * Return the ColumnIndex field.
	 * 
	 * @return int mColumnIndex
	 */
	public int getColumnIndex() {
		return mColumnIndex;
	}

	/**
	 * Set the ColumnIndex field.
	 */
	public void setColumnIndex(int pNewColumnIndex) {
		mColumnIndex = pNewColumnIndex;
	}
	
}
