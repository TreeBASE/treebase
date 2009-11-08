package org.treebase.oai.web.controller;

import org.cipres.treebase.core.CoreServiceLauncher;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;



public class OAIPMHControllerTest extends AbstractDependencyInjectionSpringContextTests {

	/**
	 * constructor.
	 */
	public OAIPMHControllerTest () {
		super();
	}

	
	@Override
	protected String[] getConfigLocations() {
		return CoreServiceLauncher.getSpringConfigurations();
	}

}
