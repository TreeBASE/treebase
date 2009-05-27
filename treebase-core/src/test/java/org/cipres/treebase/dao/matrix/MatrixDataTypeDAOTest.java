package org.cipres.treebase.dao.matrix;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.MatrixDataType;
import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;

/**
 * The class <code>MatrixDataTypeDAOTest</code> contains tests for the class
 * {@link <code>MatrixDataTypeDAO</code>}
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro at 6/12/06 4:22 PM
 * 
 * @author Jin Ruan
 * 
 * @version $Revision$
 */
public class MatrixDataTypeDAOTest extends AbstractDAOTest {

	private MatrixDataTypeHome mFixture;

	/**
	 * Return the Fixture field.
	 * 
	 * @return MatrixDataTypeHome mFixture
	 */
	public MatrixDataTypeHome getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	public void setFixture(MatrixDataTypeHome pNewFixture) {
		mFixture = pNewFixture;
	}

	/**
	 * Run the MatrixDataType findByDescription(String) method test
	 */
	public void testFindByDescription() {
		String testName = "findByDescription";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		String desc = MatrixDataType.MATRIX_DATATYPE_DNA;
		MatrixDataType result = getFixture().findByDescription(desc);

		// verify
		assertTrue(result != null);
		assertTrue(result.getDefaultCharacter() != null);
		assertTrue(result.getDescription().equals(desc));

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
 * junit.framework.TestCase testCase = MatrixDataTypeDAOTest testClassType =
 * org.cipres.treebase.dao.matrix.MatrixDataTypeDAO
 */