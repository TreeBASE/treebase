package org.cipres.treebase.domain.study;

import java.util.List;

import org.cipres.treebase.domain.DomainHome;

/**
 * Interface algorithm home.
 * 
 * @author madhu
 * 
 */
public interface AlgorithmHome extends DomainHome {

	/**
	 * Find all defined unique algorithm descriptions.
	 */
	List<String> findAllUniqueAlgorithmDescriptions();

	/**
	 * Find all defined unique "other" algorithm descriptions.
	 */
	List<String> findAllUniqueOtherAlgorithmDescriptions(String pPartialValue);

	/**
	 * Find the algorithm based on the description. The search is not case sensitive.
	 * 
	 * Returns null if there is no match.
	 * 
	 * @param pDesc
	 * @return
	 */
	// Algorithm findByDescription(String pDesc);
}
