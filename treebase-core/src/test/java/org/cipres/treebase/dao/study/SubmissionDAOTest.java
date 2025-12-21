package org.cipres.treebase.dao.study;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * The class <code>SubmissionDAOTest</code> contains tests for the class {@link
 * <code>SubmissionDAO</code>}
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro at 6/29/07 2:21 PM
 * 
 * @author Jin Ruan
 * 
 * @version $Revision$
 */
public class SubmissionDAOTest extends AbstractDAOTest {

	private SubmissionHome mFixture;

	/**
	 * Return the Fixture field.
	 * 
	 * @return SubmissionHome mFixture
	 */
	public SubmissionHome getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	public void setFixture(SubmissionHome pNewFixture) {
		mFixture = pNewFixture;
	}

	/**
	 * Run the Submission findBySubmissionNumber(String) method test
	 */
	public void testFindBySubmissionNumber() {
		String testName = "findBySubmissionNumber";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find an author:
		// TODO: unit test
		// Person pAuthor = null;
		// StudyDAO fixture = getFixture();
		// Collection<Study> result = getFixture().findByAuthor(pAuthor);
		//
		// // verify
		// assertTrue(result != null);
		// assertTrue(result.getAuthors().equals(accessNum));

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}

	/**
	 * Run the void delete(Submission) method test
	 */
	public void testDelete() {
	// fail("Newly generated method - fix or disable");
	// // add test code here
	// SubmissionDAO fixture = new SubmissionDAO();
	// Submission pSubmission = null;
	// fixture.delete(pSubmission);
	// assertTrue(false);
	}

	/**
	 * Run the Submission findByMatrix(Matrix) method test
	 */
	public void testFindByMatrix() {
		String testName = "testFindByMatrix";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a matrix in a submission:
		String matrixStr = "select matrix_id from sub_matrix fetch first rows only";
		long matrixId = jdbcTemplate.queryForLong(matrixStr);
		logger.info("matrix id: " + matrixId);
		assertTrue(matrixId > 0);

		// 2. query
		Matrix m = (Matrix) loadObject(Matrix.class, matrixId);
		assertTrue(m != null);

		Submission s = getFixture().findByMatrix(m);
		assertTrue(s != null);

		// 3. verify
		String sqlStr = "select count(*) from sub_matrix where submission_id = " + s.getId()
			+ " and matrix_id = " + m.getId();
		int count = jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(count == 1);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}

	/**
	 * Run the Submission findByTree() method test
	 */
	public void testFindByTree() {
		String testName = "testFindByTree";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a tree in a submission:
		String treeStr = "select phylotree_id from phylotree t where treeblock_id in (select treeblock_id from sub_treeblock ) fetch first rows only";
		long treeId = jdbcTemplate.queryForLong(treeStr);
		logger.info("tree id: " + treeId);
		assertTrue(treeId > 0);

		// 2. query
		PhyloTree tree = (PhyloTree) loadObject(PhyloTree.class, treeId);
		assertTrue(tree != null);

		Submission s = getFixture().findByTree(tree);
		assertTrue(s != null);

		// 3. verify
		String sqlStr = "select count(*) from sub_treeblock st, phylotree t where st.submission_id = " + s.getId()
			+ " and st.treeblock_id = t.treeblock_id and t.phylotree_id = " + tree.getId();
		int count = jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(count == 1);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}
	
	/**
	 * Run the Submission findByReadyState() method test
	 */
	public void testFindByReadyState() {
		String testName = "testFindByReadyState";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 2. query
		Collection<Submission> s = getFixture().findByReadyState();
		assertTrue(s.size() > 0);

		// 3. verify
		String sqlStr = "select count(*) from study where studyStatus_ID = 2";
		int count = jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(count > 0);
		assertTrue(s.size() == count);
		
		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified. submission size = " + count);
		}
	}
	
	public void testFindByCreateDateRange() {
		String testName = "testFindByCreateDateRange";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		Date from = (new GregorianCalendar(2006,1,1)).getTime();
		Date until = (new GregorianCalendar(2007,1,1)).getTime();
		
		Collection<Submission> s = getFixture().findByCreateDateRange(from, until);
		assertTrue(s.size() > 0);
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: found " + s.size());
		}
	}
	
	public void testFindByLastModifiedDateRange() {
		String testName = "testFindByLastModifiedDateRange";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		Date from = (new GregorianCalendar(2011,1,1)).getTime();
		Date until = (new GregorianCalendar(2011,3,1)).getTime();
		
		Collection<Submission> s = getFixture().findByLastModifiedDateRange(from, until);
		
		
		
		assertTrue(s.size() > 0);
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: found " + s.size());
		}
	}	
	
}

/*
 * $CPS$ This comment was generated by CodePro. Do not edit it. patternId =
 * com.instantiations.assist.eclipse.pattern.testCasePattern strategyId =
 * com.instantiations.assist.eclipse.pattern.testCasePattern.junitTestCase additionalTestNames =
 * assertTrue = false callTestMethod = true createMain = false createSetUp = false createTearDown =
 * false createTestFixture = false createTestStubs = true methods = package =
 * org.cipres.treebase.dao.study package.sourceFolder = treebase-core/src/test/java superclassType =
 * junit.framework.TestCase testCase = SubmissionDAOTest testClassType =
 * org.cipres.treebase.dao.study.SubmissionDAO
 */