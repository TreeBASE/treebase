
package org.cipres.treebase.domain.matrix;

import org.cipres.treebase.domain.DomainHome;

/**
 * ItemDefinitionHome.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface ItemDefinitionHome extends DomainHome {

	// Notes: must match the database entries.
	static final String[] PREDEFINED_ITEMDEFINITIONS = {"Min", "Max", "Median", "Avg", "Average",
		"Variance", "StdError", "SampleSize", "States"};

	/**
	 * Return a predefined Item definition by description. Returns null if there is no match for
	 * predefined item definition.
	 * 
	 * The comparison is case insensitive.
	 * 
	 * The list of predefined item definitions: Min, Max, Median, Avg (Average), Variance, StdError,
	 * SampleSize, States.
	 * 
	 * @param pDescription
	 */
	ItemDefinition findPredefinedItemDefinition(String pDescription);

	/**
	 * Find an ItemDefinition by description. The comparison is case insensitive.
	 * 
	 * @param pDesc
	 * @return
	 */
	ItemDefinition findByDescription(String pDesc);
}
