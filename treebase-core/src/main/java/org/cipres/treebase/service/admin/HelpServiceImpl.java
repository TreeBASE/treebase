
package org.cipres.treebase.service.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cipres.treebase.domain.admin.Help;
import org.cipres.treebase.domain.admin.HelpHome;
import org.cipres.treebase.domain.admin.HelpService;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * HelpServiceImpl.java
 * 
 * Created on November 17, 2008
 * 
 * @author mjd 20081117
 * 
 */
public class HelpServiceImpl extends AbstractServiceImpl implements HelpService {
	private static final Logger LOGGER = LogManager.getLogger(HelpServiceImpl.class);

	private HelpHome helpHome;

	private HelpHome getHelpHome() {
		return helpHome;
	}

	public void setHelpHome(HelpHome helpHome) {
		this.helpHome = helpHome;
	}

	@Override
	protected HelpHome getDomainHome() {
		return getHelpHome();
	}

	public Help findByID(Long helpId) {
		return getHelpHome().findByID(helpId);
	}

	public Help findByTag(String tag) {
		return getHelpHome().findByTag(tag);
	}

	public Help unknownHelp() {
		Help unknownHelp = new Help();
		unknownHelp.setId(0L);
		return unknownHelp;
	}

	public Help createHelp(String helpTag) {
		return getHelpHome().createHelp(helpTag);
	}

	@Override
	public Class defaultResultClass() {
		return Help.class;
	}
}
