
package org.cipres.treebase.domain.admin;

import org.cipres.treebase.domain.DomainHome;

/**
 * HelpHome.java
 * 
 * Created on November 17, 2008
 * 
 * @author mjd 20081117
 * 
 */
public interface HelpHome extends DomainHome {

	/**
	 * Return a Help object by id.
	 * 
	 * @param helpId
	 * @return
	 */
	Help findByID(Long helpId);

	/**
	 * Return a Help object by tag.
	 * 
	 * @param tag
	 * @return
	 */
	Help findByTag(String tag);

	/**
	 * Manufacture and save a new help object with the specified tag
	 * 
	 * @param helpTag
	 * @return the new persistent object
	 * @author mjd 20081202
	 */
	Help createHelp(String helpTag);
}
