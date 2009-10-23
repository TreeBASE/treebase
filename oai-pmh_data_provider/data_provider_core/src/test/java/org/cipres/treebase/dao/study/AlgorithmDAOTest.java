/*
 * CIPRES Copyright (c) 2005- 2007, The Regents of the University of California All rights reserved.
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
package org.cipres.treebase.dao.study;

import org.apache.log4j.Logger;

import java.util.List;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.study.AlgorithmHome;

/**
 * @author madhu
 * 
 */
public class AlgorithmDAOTest extends AbstractDAOTest {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(AlgorithmDAOTest.class);

	private AlgorithmHome mFixture;

	/**
	 * Return the Fixture field.
	 * 
	 * @return AlgorithmHome mFixture
	 */
	private AlgorithmHome getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	public void setFixture(AlgorithmHome pNewFixture) {
		mFixture = pNewFixture;
	}

	/**
	 * Test method for
	 * {@link org.cipres.treebase.dao.study.AlgorithmDAO#findAllUniqueAlgorithmDescriptions()}.
	 */
	public void testFinalAllUniqueAlgorithmDescriptions() {
		String testName = "finalAllUniqueAlgorithmDescriptions";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. test
		List<String> descriptions = getFixture().findAllUniqueAlgorithmDescriptions();
		assertTrue("empty descriptions.", descriptions != null && !descriptions.isEmpty());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("descriptions size = " + descriptions.size()); //$NON-NLS-1$
		}

		// 2. verify
		String sqlStr = "select count (distinct lower(description)) from algorithm";
		// String sqlStr = "select count (distinct description) from algorithm";
		int count = jdbcTemplate.queryForInt(sqlStr);
		assertTrue("description size has to match.", count == descriptions.size());

		// assertTrue(result != null);
		// assertTrue(result.getAccessionNumber().equals(accessNum));

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}

	/**
	 * Test method for
	 * {@link org.cipres.treebase.dao.study.AlgorithmDAO#findAllUniqueAlgorithmDescriptions()}.
	 */
	public void testFindAllUniqueOtherAlgorithmDescriptions(String pPartialValue) {
		String testName = "findAllUniqueOtherAlgorithmDescriptions";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. test
		List<String> descriptions = getFixture().findAllUniqueOtherAlgorithmDescriptions(
			pPartialValue);
		assertTrue("empty descriptions.", descriptions != null && !descriptions.isEmpty());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("descriptions size = " + descriptions.size()); //$NON-NLS-1$
		}

		// 2. verify
		String sqlStr = "select count (distinct lower(description)) from algorithm where type='O'";
		// String sqlStr = "select count (distinct description) from algorithm";
		int count = jdbcTemplate.queryForInt(sqlStr);
		assertTrue("description size has to match.", count == descriptions.size());

		// assertTrue(result != null);
		// assertTrue(result.getAccessionNumber().equals(accessNum));

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}

}
