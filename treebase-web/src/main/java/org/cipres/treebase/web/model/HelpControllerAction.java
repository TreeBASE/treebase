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

package org.cipres.treebase.web.model;

import org.cipres.treebase.domain.admin.Help;
import org.cipres.treebase.domain.admin.HelpService;

/**
 * HelpControllerAction.java
 * 
 * Created on December 2, 2008
 * 
 * @author mjd 20081202
 * 
 */

public class HelpControllerAction {
	Help helpObject;
	HelpService helpService;
	boolean isAdmin = false;

	public HelpControllerAction() {
		super();
		helpObject = null;
	}
	
	public HelpControllerAction(HelpService pHelpService) {
		this();
		setHelpService(pHelpService);
	}

	public void setHelp(Help help) {
		helpObject = help;
	}

	public void setId(Long id) {
		Help theHelpObject = getHelpService().findByID(id);
		if (theHelpObject == null) {
			theHelpObject = new Help();
			getHelpService().save(theHelpObject);
		} else {
			setHelp(theHelpObject);
		}
	}

	public void setTag(String tag) {
		Help theHelpObject = getHelpService().findByTag(tag);
		if (theHelpObject == null) {
			theHelpObject = getHelpService().createHelp(tag);
		}
		setHelp(theHelpObject);
	}

	public Boolean getAdminMode() {
		return isAdmin();
	}

	public String getHelpTag() {
		return helpObject == null ? "(none)" : helpObject.getTag();
	}

	public String getHelpText() {
		return helpObject == null ? "(none)" : helpObject.getHelpText();	
	}

	public HelpService getHelpService() {
		return helpService;
	}

	public void setHelpService(HelpService helpService) {
		this.helpService = helpService;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void makeHelpUnknown() {
		this.setHelp(getHelpService().unknownHelp());
	}
}

