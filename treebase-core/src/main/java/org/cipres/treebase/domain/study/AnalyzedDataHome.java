
package org.cipres.treebase.domain.study;

import java.util.Collection;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * The home interface for the Analyzed Data domain objects.
 * 
 * Created on September 29, 2005
 * 
 * @author Jin Ruan
 * 
 */
public interface AnalyzedDataHome extends DomainHome {

	/**
	 * Find by the associated matrix. Returns empty collection if no match is found.
	 * 
	 * @param pMatrix
	 * @return
	 */
	Collection<AnalyzedData> findByMatrix(Matrix pMatrix);

	/**
	 * Find by the associated tree. Returns empty collection if no match is found.
	 * 
	 * @param pTree
	 * @return
	 */
	Collection<AnalyzedData> findByTree(PhyloTree pTree);

}
