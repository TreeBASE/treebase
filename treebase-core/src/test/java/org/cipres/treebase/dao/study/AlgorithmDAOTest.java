package org.cipres.treebase.dao.study;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.study.AlgorithmHome;
import org.junit.Test;

/**
 * @author madhu
 * 
 */
public class AlgorithmDAOTest extends AbstractDAOTest {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LogManager.getLogger(AlgorithmDAOTest.class);

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
	@Test
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
		Integer count = (Integer) jdbcTemplate.queryForObject(sqlStr, Integer.class);
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
	@Test
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
		Integer count = (Integer) jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue("description size has to match.", count == descriptions.size());

		// assertTrue(result != null);
		// assertTrue(result.getAccessionNumber().equals(accessNum));

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}

}
