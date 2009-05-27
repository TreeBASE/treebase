/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.service.admin;

import org.apache.log4j.Logger;
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
	private static final Logger LOGGER = Logger.getLogger(HelpServiceImpl.class);

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
