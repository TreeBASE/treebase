package org.cipres.treebase.dao.study;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

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
	@Autowired
	public void setFixture(SubmissionHome pNewFixture) {
		mFixture = pNewFixture;
	}

	/**
	 * Run the Submission findBySubmissionNumber(String) method test
	 */
	@Test
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
	@Test
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
	@Test
	public void testFindByMatrix() {
		String testName = "testFindByMatrix";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a matrix in a submission:
		String matrixStr = "select matrix_id from sub_matrix fetch first rows only";
		List<Long> matrixIds = jdbcTemplate.queryForList(matrixStr, Long.class);
		
		// 2. verify correct handling of empty database
		assertNotNull("Query should return non-null list", matrixIds);
		
		if (matrixIds.size() > 0) {
			long matrixId = matrixIds.get(0);
			logger.info("matrix id: " + matrixId);
			assertTrue(matrixId > 0);

			// 3. query
			Matrix m = (Matrix) loadObject(Matrix.class, matrixId);
			assertTrue(m != null);

			Submission s = getFixture().findByMatrix(m);
			assertTrue(s != null);

			// 4. verify
			String sqlStr = "select count(*) from sub_matrix where submission_id = " + s.getId()
				+ " and matrix_id = " + m.getId();
			Integer count = (Integer) jdbcTemplate.queryForObject(sqlStr, Integer.class);
			assertTrue(count == 1);

			if (logger.isInfoEnabled()) {
				logger.info(testName + " verified.");
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(testName + " - empty database, test skipped");
			}
		}
	}

	/**
	 * Run the Submission findByTree() method test
	 */
	@Test
	public void testFindByTree() {
		String testName = "testFindByTree";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a tree in a submission:
		String treeStr = "select phylotree_id from phylotree t where treeblock_id in (select treeblock_id from sub_treeblock ) fetch first rows only";
		List<Long> treeIds = jdbcTemplate.queryForList(treeStr, Long.class);
		
		// 2. verify correct handling of empty database
		assertNotNull("Query should return non-null list", treeIds);
		
		if (treeIds.size() > 0) {
			long treeId = treeIds.get(0);
			logger.info("tree id: " + treeId);
			assertTrue(treeId > 0);

			// 3. query
			PhyloTree tree = (PhyloTree) loadObject(PhyloTree.class, treeId);
			assertTrue(tree != null);

			Submission s = getFixture().findByTree(tree);
			assertTrue(s != null);

			// 4. verify
			String sqlStr = "select count(*) from sub_treeblock st, phylotree t where st.submission_id = " + s.getId()
				+ " and st.treeblock_id = t.treeblock_id and t.phylotree_id = " + tree.getId();
			Integer count = (Integer) jdbcTemplate.queryForObject(sqlStr, Integer.class);
			assertTrue(count == 1);

			if (logger.isInfoEnabled()) {
				logger.info(testName + " verified.");
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(testName + " - empty database, test skipped");
			}
		}
	}
	
	/**
	 * Run the Submission findByReadyState() method test
	 */
	@Test
	public void testFindByReadyState() {
		String testName = "testFindByReadyState";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. query
		Collection<Submission> s = getFixture().findByReadyState();
		
		// 2. verify correct handling of empty database
		assertNotNull("Result should not be null", s);

		// 3. verify
		String sqlStr = "select count(*) from study where studyStatus_ID = 2";
		Integer count = (Integer) jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(s.size() == count);
		
		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified. submission size = " + count);
		}
	}
	
	@Test
	public void testFindByCreateDateRange() {
		String testName = "testFindByCreateDateRange";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		Date from = (new GregorianCalendar(2006,1,1)).getTime();
		Date until = (new GregorianCalendar(2007,1,1)).getTime();
		
		Collection<Submission> s = getFixture().findByCreateDateRange(from, until);
		
		// verify correct handling of empty database
		assertNotNull("Result should not be null", s);
		
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: found " + s.size());
		}
	}
	
	@Test
	public void testFindByLastModifiedDateRange() {
		String testName = "testFindByLastModifiedDateRange";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		Date from = (new GregorianCalendar(2011,1,1)).getTime();
		Date until = (new GregorianCalendar(2011,3,1)).getTime();
		
		Collection<Submission> s = getFixture().findByLastModifiedDateRange(from, until);
		
		// verify correct handling of empty database
		assertNotNull("Result should not be null", s);
		
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: found " + s.size());
		}
	}
	
	/**
	 * Test the findRecentPublishedSubmissions method.
	 * This method should return the most recent published submissions
	 * ordered by last modified date descending.
	 */
	@Test
	public void testFindRecentPublishedSubmissions() {
		String testName = "testFindRecentPublishedSubmissions";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		int limit = 10;
		Collection<Submission> submissions = getFixture().findRecentPublishedSubmissions(limit);
		
		// Verify the method returns a non-null collection
		assertNotNull("Result should not be null", submissions);
		
		// Verify the result size does not exceed the limit
		assertTrue("Result size should not exceed limit", submissions.size() <= limit);
		
		// Verify all returned submissions are published
		for (Submission sub : submissions) {
			assertNotNull("Study should not be null", sub.getStudy());
			assertTrue("Study should be published", sub.getStudy().isPublished());
		}
		
		// Count published studies in database to verify
		String sqlStr = "select count(*) from study where studyStatus_ID = 3"; // 3 = Published
		Integer publishedCount = (Integer) jdbcTemplate.queryForObject(sqlStr, Integer.class);
		
		// The result should have at most 'limit' or 'publishedCount' items, whichever is smaller
		int expectedMax = Math.min(limit, publishedCount);
		assertTrue("Result size should be at most " + expectedMax, submissions.size() <= expectedMax);
		
		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified. Found " + submissions.size() + 
				" recent published submissions (published in DB: " + publishedCount + ")");
		}
	}
	
	/**
	 * Test that findRecentPublishedSubmissions returns submissions ordered by last modified date.
	 */
	@Test
	public void testFindRecentPublishedSubmissionsOrdering() {
		String testName = "testFindRecentPublishedSubmissionsOrdering";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		Collection<Submission> submissions = getFixture().findRecentPublishedSubmissions(5);
		
		assertNotNull("Result should not be null", submissions);
		
		// Verify ordering: each item should have a lastModifiedDate >= the next item
		Date previousDate = null;
		for (Submission sub : submissions) {
			if (sub.getStudy() != null && sub.getStudy().getLastModifiedDate() != null) {
				Date currentDate = sub.getStudy().getLastModifiedDate();
				if (previousDate != null) {
					assertTrue("Submissions should be ordered by date descending",
						previousDate.compareTo(currentDate) >= 0);
				}
				previousDate = currentDate;
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
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