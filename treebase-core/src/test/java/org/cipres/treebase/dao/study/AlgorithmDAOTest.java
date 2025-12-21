package org.cipres.treebase.dao.study;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.study.AlgorithmHome;
import org.cipres.treebase.domain.study.BayesianAlgorithm;
import org.cipres.treebase.domain.study.EvolutionAlgorithm;
import org.cipres.treebase.domain.study.JoiningAlgorithm;
import org.cipres.treebase.domain.study.LikelihoodAlgorithm;
import org.cipres.treebase.domain.study.ParsimonyAlgorithm;
import org.cipres.treebase.domain.study.UPGMAAlgorithm;

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
	 * Initialize test data before transaction.
	 */
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		super.onSetUpBeforeTransaction();
		
		// Create standard algorithm types for testing
		LikelihoodAlgorithm likelihood = new LikelihoodAlgorithm();
		likelihood.setDescription("maximum likelihood");
		
		BayesianAlgorithm bayesian = new BayesianAlgorithm();
		bayesian.setDescription("bayesian inference");
		
		ParsimonyAlgorithm parsimony = new ParsimonyAlgorithm();
		parsimony.setDescription("parsimony");
		
		EvolutionAlgorithm evolution = new EvolutionAlgorithm();
		evolution.setDescription("minimum evolution");
		
		JoiningAlgorithm joining = new JoiningAlgorithm();
		joining.setDescription("neighbor joining");
		
		UPGMAAlgorithm upgma = new UPGMAAlgorithm();
		upgma.setDescription("UPGMA");
		
		// Save to database using hibernateTemplate
		hibernateTemplate.save(likelihood);
		hibernateTemplate.save(bayesian);
		hibernateTemplate.save(parsimony);
		hibernateTemplate.save(evolution);
		hibernateTemplate.save(joining);
		hibernateTemplate.save(upgma);
		hibernateTemplate.flush();
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
