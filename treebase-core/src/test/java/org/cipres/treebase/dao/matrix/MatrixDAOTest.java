package org.cipres.treebase.dao.matrix;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixHome;
import org.cipres.treebase.domain.matrix.MatrixKind;
import org.cipres.treebase.domain.study.Study;

/**
 * The class <code>MatrixDAOTest</code> contains tests for the class
 * {@link <code>MatrixDAO</code>}
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro at 6/20/06 10:58 AM
 * 
 * @author Jin Ruan
 * 
 * @version $Revision$
 */
public class MatrixDAOTest extends AbstractDAOTest {

	private MatrixHome mFixture;

	/**
	 * @return the fixture
	 */
	public MatrixHome getFixture() {
		return mFixture;
	}

	/**
	 * @param pFixture the fixture to set
	 */
	public void setFixture(MatrixHome pFixture) {
		mFixture = pFixture;
	}

	/**
	 * Run the void delete(Matrix) method test
	 */
	public void xtestAddDelete() {
		String testName = "testAddDeleteMatrix";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new submission:
		// Collection<Study> studies = getStudyService().findByName(PARSER_STUDY_NAME, true);
		//
		// Study s = studies.iterator().next();
		// Submission sub = s.getSubmission();
		//
		// getFixture().deleteSubmittedData(sub);
		//
		// setComplete();
		// endTransaction();

		Matrix m = getFixture().findPersistedObjectByID(Matrix.class, new Long(766));
		assertTrue(m != null);

		// 2. verify
		String matrixSQL = "select count(*) from matrix where matrix_id = " + m.getId();
		Integer count = (Integer) jdbcTemplate.queryForObject(matrixSQL, Integer.class);
		assertTrue(count == 1);

		// 3. delete:
		getFixture().delete(m);

		setComplete();
		endTransaction();

		// 4. Verify delete:
		count = (Integer) jdbcTemplate.queryForObject(matrixSQL, Integer.class);
		assertTrue("Matrix should be deleted", count == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the void findByNexusFileName() method test
	 */
	public void testfindByNexusFileName() {
		String testName = "testfindByNexusFileName";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1.find a submission with matrix:
		List<Map<String, Object>> result = 
			jdbcTemplate.queryForList("select submission_id, matrix_id from sub_matrix fetch first rows only");
		
		// 2. verify correct handling of empty database
		assertNotNull("Query should return non-null list", result);
		
		if (result != null && !result.isEmpty()) {
			Map<String, Object> firstResult = result.get(0);
			String subId = firstResult.get("SUBMISSION_ID").toString();
			String matrixId = firstResult.get("MATRIX_ID").toString();
			
			String nexusName = (String) jdbcTemplate.queryForObject("select nexusFileName from matrix where matrix_id =" + matrixId, String.class);
			logger.info(" subId =" + Long.decode(subId) + " matrixId" + matrixId + " nexusName=" + nexusName); //$NON-NLS-1$
			assertTrue(nexusName != null);

			//3. query: 
			Collection<Matrix> matrices = getFixture().findByNexusFileName(Long.decode(subId), nexusName);
			if (logger.isInfoEnabled()) {
				logger.info(" findByNexusFileName size= " + matrices.size()); //$NON-NLS-1$
			}
			assertTrue(matrices.size() > 0);
			
			// 4. verify
			//String matrixSQL = "select count(*) from matrix where matrix_id = " + m.getId();
			//int count = jdbcTemplate.queryForInt(matrixSQL);
			for (Matrix matrix : matrices) {
				assertTrue("Verify nexus file name.", matrix.getNexusFileName().equalsIgnoreCase(nexusName));
			}
			//assertTrue(count == 1);

			//setComplete();
			//endTransaction();

			if (logger.isInfoEnabled()) {
				logger.info(testName + " - end "); //$NON-NLS-1$
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(testName + " - empty database, test skipped");
			}
		}
	}

	/**
	 * Run the findfindRowAsString() method test
	 */
	public void testfindRowAsString() {
		String testName = "testfindRowAsString";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1.find a submission with matrix:
		Long matrixId = 265L; // 
		//Long matrixId = 1544L; // 40k+ columns!
		//Long matrixId = 1547L; // large, but not crazy.
		CharacterMatrix m = (CharacterMatrix) loadObject(CharacterMatrix.class, matrixId);
		
		if (m != null) {
			int start = 0;
			int end = 30;
			
			//2. query: 
			List<String> rowStrings = getFixture().findRowAsString(m, start, end);
			
			if (logger.isInfoEnabled()) {
				logger.info("row string size= " + rowStrings.size()); //$NON-NLS-1$
			}
			
			int i=0;
			for (String string : rowStrings) {
				assertTrue(string != null);
				System.out.println(" " + (i++) + ": " + string);
			}
			
			//assertTrue(rowStrings.size() == (end-start + 1));
			
			// 3. verify
			//String matrixSQL = "select count(*) from matrix where matrix_id = " + m.getId();
			//int count = jdbcTemplate.queryForInt(matrixSQL);
			//assertTrue(count == 1);

			//setComplete();
			//endTransaction();

			if (logger.isInfoEnabled()) {
				logger.info(testName + " - end "); //$NON-NLS-1$
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(testName + " - empty database, test skipped");
			}
		}
	}

	/**
	 * Run the updatePublishedFlagByStudy method test.
	 * 
	 */
	public void testupdatePublishedFlagByStudy() throws Exception {

		String testName = "testupdatePublishedFlagByStudy";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a study with matrices:
		String studyStr = "select study_id from matrix where study_id is not null and published is false fetch first rows only";
		List<Long> studyIds = jdbcTemplate.queryForList(studyStr, Long.class);
		
		// 2. verify correct handling of empty database
		assertNotNull("Query should return non-null list", studyIds);
		
		if (studyIds.size() > 0) {
			long studyId = studyIds.get(0);
			logger.info("study id: " + studyId);
			assertTrue(studyId > 0);

			// 3. query
			Study s = (Study) loadObject(Study.class, studyId);
			assertTrue(s != null);
			//table study and matrix may not agree on "published" 
			//assertTrue(s.isPublished() == false);

			int count = getFixture().updatePublishedFlagByStudy(s, true);
			logger.debug("update Count = " + count);
			assertTrue(count > 0);

			// force commit immediately, important:
			setComplete();
			endTransaction();

			// 4. verify
			String treeCountStr = "select count(m.matrix_id) from matrix m "
				+ " where m.study_ID = " + s.getId() + " and m.published is true";
			Integer countVeri = (Integer) jdbcTemplate.queryForObject(treeCountStr, Integer.class);
			logger.debug("verify Count = " + countVeri);
			assertTrue(countVeri == count);

			//5. change it back:
			int count2 = getFixture().updatePublishedFlagByStudy(s, false);
			assertTrue(count2 == count);
		
			setComplete();
			
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
	 * Test findKindByDescription
	 */
	public void testfindKindByDescription() throws Exception {
		String testName = "findKindByDescription";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		String desc = MatrixKind.KIND_AA;
		MatrixKind result = getFixture().findKindByDescription(desc);
		
		//verify
		assertTrue(result != null);
		assertTrue(result.getDescription().equals(desc));		

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

}

/*
 * $CPS$ This comment was generated by CodePro. Do not edit it. patternId =
 * com.instantiations.assist.eclipse.pattern.testCasePattern strategyId =
 * com.instantiations.assist.eclipse.pattern.testCasePattern.junitTestCase additionalTestNames =
 * assertTrue = false callTestMethod = true createMain = false createSetUp = false createTearDown =
 * false createTestFixture = true createTestStubs = false methods = delete(QMatrix;) package =
 * org.cipres.treebase.dao.matrix package.sourceFolder = treebase-core/src/test/java superclassType =
 * junit.framework.TestCase testCase = MatrixDAOTest testClassType =
 * org.cipres.treebase.dao.matrix.MatrixDAO
 */
