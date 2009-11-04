
package org.cipres.treebase.dao.jdbc;

import java.util.List;

/**
 * MatrixRowJDBC.java
 * 
 * 
 * Created on Jan 16, 2008
 * @author Jin Ruan
 *
 */
public class MatrixRowJDBC {

	private List<DiscreteMatrixElementJDBC> mElements;

	/**
	 * Constructor.
	 */
	public MatrixRowJDBC() {
		super();
	}

	/**
	 * Return the Elements field.
	 * 
	 * @return List<MatrixElementJDBC> 
	 */
	public List<DiscreteMatrixElementJDBC> getElements() {
		return mElements;
	}

	/**
	 * Set the Elements field.
	 */
	public void setElements(List<DiscreteMatrixElementJDBC> pNewElements) {
		mElements = pNewElements;
	}
	
}
