/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

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
