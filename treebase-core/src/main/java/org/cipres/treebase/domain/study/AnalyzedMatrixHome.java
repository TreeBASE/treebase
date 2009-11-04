
package org.cipres.treebase.domain.study;

import org.cipres.treebase.domain.DomainHome;

/**
 * The home interface for the Analyzed Matrix domain objects.
 * 
 * Created on September 29, 2005
 * 
 * @author Jin Ruan
 * 
 */
public interface AnalyzedMatrixHome extends DomainHome {

	/**
	 * Delete the instance from the database. Handles relationships and cascading deleting. TODO:
	 * cascading delete the matrix?
	 * 
	 * @param pAnalyzedMatrix
	 */
	void delete(AnalyzedMatrix pAnalyzedMatrix);

}
