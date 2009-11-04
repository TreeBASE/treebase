
package org.cipres.treebase.domain.matrix;

import java.util.Collection;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;

/**
 * MatrixRowHome.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface MatrixRowHome extends DomainHome {

	/**
	 * Delete a collection of rows. Handles bi-directional relationship management and cascade
	 * delete.
	 * 
	 * @param pRows
	 */
	void deleteRows(Collection<MatrixRow> pRows);

	Collection<MatrixRow> findByLabel(String pTaxonLabel);
	Collection<MatrixRow> findByTaxonLabel(TaxonLabel pTaxonLabel);
}
