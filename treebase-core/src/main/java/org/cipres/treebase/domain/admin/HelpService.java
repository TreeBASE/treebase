
package org.cipres.treebase.domain.admin;

import org.cipres.treebase.service.AbstractService;

/**
 * HelpService.java
 * 
 * Created on November 17, 2008
 * 
 * @author mjd 20081117
 * 
 */
public interface HelpService extends AbstractService {

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

	Help unknownHelp();

	Help createHelp(String helpTag);
	
}
