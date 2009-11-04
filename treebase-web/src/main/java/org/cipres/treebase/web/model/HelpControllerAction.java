
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

