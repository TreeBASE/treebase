package org.cipres.treebase.dao.matrix;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.ItemDefinition;
import org.cipres.treebase.domain.matrix.ItemDefinitionHome;

/**
 * The class <code>ItemDefinitionDAOTest</code> contains tests for the class
 * {@link <code>ItemDefinitionDAO</code>}
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro at 3/29/07 3:32 PM
 * 
 * @author Jin Ruan
 * 
 * @version $Revision$
 */
public class ItemDefinitionDAOTest extends AbstractDAOTest {

	private ItemDefinitionHome mFixture;

	/**
	 * Return the object that is being tested.
	 * 
	 * @return the test fixture
	 * 
	 * @see org.cipres.treebase.dao.matrix.ItemDefinitionDAO
	 */
	public ItemDefinitionHome getFixture() {
		return mFixture;
	}

	/**
	 * Set the object that is being tested.
	 * 
	 * @param mFixture the test fixture
	 */
	public void setFixture(ItemDefinitionHome pFixture) {
		mFixture = pFixture;
	}

	/**
	 * Run the ItemDefinition findPredefinedItemDefinition(String) method test
	 */
	public void testFindPredefinedItemDefinition() {
		String testName = "findPredefinedItemDefinition";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		String desc = "AveraGE";
		ItemDefinition result = getFixture().findPredefinedItemDefinition(desc);

		// verify
		assertTrue(result != null);
		assertTrue(result.getDescription().equals("Avg"));

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified predefinied item:" + desc);
		}
	}

	/**
	 * Run the ItemDefinition findByDescription(String) method test
	 */
	public void testFindByDescription() {
		String testName = "findByDescription";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		String desc = "MiN";
		ItemDefinition result = getFixture().findByDescription(desc);

		// verify
		assertTrue(result != null);
		assertTrue(result.getDescription().equalsIgnoreCase(desc));

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified type:" + desc);
		}
	}
}

/*
 * $CPS$ This comment was generated by CodePro. Do not edit it. patternId =
 * com.instantiations.assist.eclipse.pattern.testCasePattern strategyId =
 * com.instantiations.assist.eclipse.pattern.testCasePattern.junitTestCase additionalTestNames =
 * assertTrue = false callTestMethod = true createMain = false createSetUp = false createTearDown =
 * false createTestFixture = true createTestStubs = true methods = package =
 * org.cipres.treebase.dao.matrix package.sourceFolder = treebase-core/src/test/java superclassType =
 * junit.framework.TestCase testCase = ItemDefinitionDAOTest testClassType =
 * org.cipres.treebase.dao.matrix.ItemDefinitionDAO
 */