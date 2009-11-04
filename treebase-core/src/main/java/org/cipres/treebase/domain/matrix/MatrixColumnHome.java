
package org.cipres.treebase.domain.matrix;

import java.util.Collection;

import org.cipres.treebase.domain.DomainHome;

/**
 * RowSegmentHome.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface MatrixColumnHome extends DomainHome {

	/**
	 * Delete a collection of columns. Handles bi-directional relationship management and cascade
	 * delete.
	 * 
	 * @param pColumns
	 */
	void deleteColumns(Collection<MatrixColumn> pColumns);

}
