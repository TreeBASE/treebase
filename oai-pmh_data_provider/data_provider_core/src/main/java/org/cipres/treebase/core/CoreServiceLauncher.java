/*
 * Copyright 2005 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.core;

import org.apache.log4j.Logger;

import org.cipres.treebase.ContextManager;

/**
 * The main application launcher for the core Treebase services, including the services layer, the
 * persistence layer.
 * 
 * Created on September 26, 2005
 * 
 * @author Jin Ruan
 * 
 */
public class CoreServiceLauncher {
	private static final Logger LOGGER = Logger.getLogger(CoreServiceLauncher.class);

	private static final String[] SPRING_CONFIG_FILES = new String[] {// "applicationContext.xml",
	"applicationContext-dao.xml",
	"applicationContext-service.xml",
	"applicationContext-standalone.xml" // 20090222 MJD
	};

	/**
	 * Constructor.
	 */
	protected CoreServiceLauncher() {
		super();
	}

	/**
	 * 
	 * Creation date: September 26, 2005 3:11:41 PM
	 */
	public static String[] getSpringConfigurations() {
		return SPRING_CONFIG_FILES;
	}

	/**
	 * The Core Service Launcher.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("CoreServiceLauncher.main(String[])"); //$NON-NLS-1$
		}

		ContextManager.createSpringContext(getSpringConfigurations());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("CoreServiceLauncher.main(String[]) - end"); //$NON-NLS-1$
		}
	}

	public static CoreServiceLauncher getInstance() {
		return new CoreServiceLauncher();
	}

}
