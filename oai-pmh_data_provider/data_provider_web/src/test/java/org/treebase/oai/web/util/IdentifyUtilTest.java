package org.treebase.oai.web.util;

import org.treebase.oai.web.command.OAIPMHCommand;

import junit.framework.TestCase;



public class IdentifyUtilTest extends TestCase{
	private OAIPMHCommand params;
	
	public void setUp() {
		params= new OAIPMHCommand();
		
	}
		
	public void testParseID() {
		params.setIdentifier("http://arXiv.org/abs/cs/0112017");
		this.assertEquals(112017L, IdentifyUtil.parseID(params));
		params.setIdentifier("oai:HUBerlin.de:3000218");
		this.assertEquals(3000218L, IdentifyUtil.parseID(params));
		params.setIdentifier("http://purl.org/phylo/treebase/phylows/study/TB2:S6705");
		this.assertEquals(6705L, IdentifyUtil.parseID(params));
	
		
	}


	
}
