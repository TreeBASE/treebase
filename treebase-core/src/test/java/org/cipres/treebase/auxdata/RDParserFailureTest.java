package org.cipres.treebase.auxdata;



import junit.framework.TestCase;
import static org.junit.Assert.*;

public class RDParserFailureTest extends TestCase {
	RDParserFailure f = new RDParserFailure();
	
	public void testSuccess() {
		assertFalse(f.success());
	}

	public void testFailed() {
		assertTrue(f.failed());
	}

	public void testRDParserFailure() {
		assertNotNull(f);
	}

}
