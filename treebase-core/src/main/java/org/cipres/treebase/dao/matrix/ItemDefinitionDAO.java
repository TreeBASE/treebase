
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
