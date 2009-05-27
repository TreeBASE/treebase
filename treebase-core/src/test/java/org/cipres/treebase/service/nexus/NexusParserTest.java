/*
 * CIPRES Copyright (c) 2005- 2006, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.cipres.treebase.service.nexus;

import java.io.File;

import org.cipres.CipresIDL.api1.DataMatrix;
import org.cipres.CipresIDL.api1.Tree;
import org.cipres.datatypes.PhyloDataset;

import org.cipres.treebase.service.AbstractServiceTest;

/**
 * @author Jin Ruan
 * 
 */
public class NexusParserTest extends AbstractServiceTest {

	private static final String TEST_NEX_FILE = "/TestNexusFile.nex";

	/**
	 * Test the phyloDataSet
	 * 
	 * Creation date: Apr 18, 2006 2:50:18 PM
	 */
	public void testLoadPhyloDataSet() throws Exception {
		String testName = "loadPhyloDataSet";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		File nexusFile = new File(getClass().getResource(TEST_NEX_FILE).toURI());
		assertTrue("Test File " + TEST_NEX_FILE + " cannot be found.", nexusFile.exists());

		PhyloDataset data = new PhyloDataset(nexusFile);

		DataMatrix matrix = data.getDataMatrix();
		assertTrue("Empty matrix.", matrix != null);

		Tree tree = data.getFirstTree();
		assertTrue("Empty tree.", tree != null);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}
}
