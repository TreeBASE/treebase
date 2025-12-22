package org.cipres.treebase.auxdata;



import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.Test;

public class RDParserFailureTest extends TestCase {
	RDParserFailure f = new RDParserFailure();
	
	@Test

	
	public void testSuccess() {
		assertFalse(f.success());
	}

	@Test


	public void testFailed() {
		assertTrue(f.failed());
	}

	@Test


	public void testRDParserFailure() {
		assertNotNull(f);
	}

}
