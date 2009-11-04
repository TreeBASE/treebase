
package org.cipres.treebase.service;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import org.cipres.treebase.core.CoreServiceLauncher;

/**
 * Extends AbstractDependencyInjectionSpringContextTests, no need to handle transaction??
 * 
 * Created on Oct 7, 2005
 * 
 * @author Jin Ruan
 * 
 */
public abstract class AbstractServiceTest extends AbstractDependencyInjectionSpringContextTests {

	/**
	 * constructor.
	 */
	public AbstractServiceTest() {
		super();
	}

	/**
	 * 
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	@Override
	protected String[] getConfigLocations() {
		return CoreServiceLauncher.getSpringConfigurations();
	}

}
