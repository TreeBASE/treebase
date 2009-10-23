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
