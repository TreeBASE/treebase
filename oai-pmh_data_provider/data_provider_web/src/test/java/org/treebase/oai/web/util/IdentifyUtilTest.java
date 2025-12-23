package org.treebase.oai.web.util;

import org.junit.Before;
import org.junit.Test;
import org.treebase.oai.web.command.OAIPMHCommand;

import static org.junit.Assert.assertEquals;

public class IdentifyUtilTest {
	private OAIPMHCommand params;
	
	@Before
	public void setUp() {
		params = new OAIPMHCommand();
	}
		
	@Test
	public void testParseID() {
		params.setIdentifier("http://arXiv.org/abs/cs/0112017");
		assertEquals(112017L, IdentifyUtil.parseID(params));
		params.setIdentifier("oai:HUBerlin.de:3000218");
		assertEquals(3000218L, IdentifyUtil.parseID(params));
		params.setIdentifier("http://purl.org/phylo/treebase/phylows/study/TB2:S6705");
		assertEquals(6705L, IdentifyUtil.parseID(params));
	}
}
