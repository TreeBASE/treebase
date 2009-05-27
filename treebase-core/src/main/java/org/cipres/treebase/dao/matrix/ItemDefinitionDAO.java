/*
 * Copyright 2007 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.dao.matrix;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.matrix.ItemDefinition;
import org.cipres.treebase.domain.matrix.ItemDefinitionHome;

/**
 * ItemDefinitionDAO.java
 * 
 * Created on Mar 29, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class ItemDefinitionDAO extends AbstractDAO implements ItemDefinitionHome {

	// constants to check AVG = AVERAGE
	private static final String AVG = "Avg";
	private static final String AVERAGE = "Average";

	/**
	 * Constructor.
	 */
	public ItemDefinitionDAO() {}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.ItemDefinitionHome#findPredefinedItemDefinition(java.lang.String)
	 */
	public ItemDefinition findPredefinedItemDefinition(String pDescription) {
		if (TreebaseUtil.isEmpty(pDescription)) {
			return null;
		}

		ItemDefinition item = null;
		String[] predefinedItems = ItemDefinitionHome.PREDEFINED_ITEMDEFINITIONS;
		for (int i = 0; i < predefinedItems.length; i++) {
			String anItemDes = predefinedItems[i];
			if (anItemDes.compareToIgnoreCase(pDescription) == 0) {
				// Average is equivalent to Avg
				if (anItemDes.compareToIgnoreCase(AVERAGE) == 0) {
					anItemDes = AVG;
				}

				// Match found. it is predefined.
				item = findByDescription(anItemDes);
			}
		}
		return item;
	}

	/**
	 * Find by description.
	 * 
	 * Creation date: Apr 20, 2006 2:34:56 PM
	 */
	public ItemDefinition findByDescription(String pDesc) {
		ItemDefinition returnVal = null;

		Criteria c = getSession().createCriteria(ItemDefinition.class).add(
			Expression.eq("description", pDesc).ignoreCase());

		List results = c.list();
		if (!results.isEmpty()) {
			returnVal = (ItemDefinition) c.list().get(0);
		}
		return returnVal;
	}

}
