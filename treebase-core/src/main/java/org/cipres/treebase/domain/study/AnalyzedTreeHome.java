
package org.cipres.treebase.domain.study;

import org.cipres.treebase.domain.DomainHome;

/**
 * The home interface for the Analyzed Tree domain objects.
 * 
 * Created on Sep 29, 2005
 * 
 * @author Jin Ruan
 * 
 */
public interface AnalyzedTreeHome extends DomainHome {

	/**
	 * Delete the instance from the database. Handles relationships and cascading deleting. 
	 * 
	 * @param pAnalyzedTree
	 */
	void delete(AnalyzedTree pAnalyzedTree);

}
