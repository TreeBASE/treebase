package org.treebase.oai.web.command;

import java.util.Date;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;


public class IdentifyTest extends AbstractDependencyInjectionSpringContextTests {

	private Identify identify;
	
	public void setIdentify(Identify identify) {
		this.identify = identify;
	}

	@Override
	protected String[] getConfigLocations() { 
	return new String[]{"applicationContext.xml"};
	} 
	
	public void testLoadIdentify() {
	
		this.assertNotNull(identify);
		
	}


}