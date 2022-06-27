
package org.cipres.treebase.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private static final Logger LOGGER = LogManager.getLogger(CoreServiceLauncher.class);

	private static final String[] SPRING_CONFIG_FILES = new String[] {// "applicationContext.xml",
	"applicationContext-db-standalone.xml",      // 2010-01-12 VG
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
