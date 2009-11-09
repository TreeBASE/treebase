package org.treebase.oai.web.controller;

import org.cipres.treebase.core.CoreServiceLauncher;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;



public class OAIPMHControllerTest extends AbstractDependencyInjectionSpringContextTests {

	/**
	 * constructor.
	 */
	
	@Override
	protected String[] getConfigLocations() {
		return new String[]{"applicationContext.xml"};
	}

	public void testHandle() {
		this.assertEquals(true, true);
	}
}
