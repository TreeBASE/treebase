
package org.cipres.treebase.domain.matrix;

import org.cipres.treebase.domain.DomainHome;

/**
 * MatrixDataTypeHome.java
 * 
 * Created on Jun 12, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface MatrixDataTypeHome extends DomainHome {

	/**
	 * Find a matrix data type by its desciption. The comparison is case sensitive.
	 * 
	 * @param pDescription
	 * @return
	 */
	MatrixDataType findByDescription(String pDescription);
}
