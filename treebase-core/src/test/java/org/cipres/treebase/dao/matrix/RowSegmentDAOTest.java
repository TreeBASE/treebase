
package org.cipres.treebase.dao.matrix;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.matrix.RowSegmentHome;
import org.cipres.treebase.domain.matrix.RowSegmentService;
import org.cipres.treebase.domain.matrix.RowSegmentService.RowSegmentField;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.framework.ExecutionResult;

/**
 * RowSegmentDAOTest.java
 * 
 * Created on May 2, 2008
 * @author JRUAN
 *
 */
public class RowSegmentDAOTest extends AbstractDAOTest {

	private RowSegmentHome mFixture;
	private SubmissionService mSubmissionService;
	private RowSegmentService mRowSegmentService;

	/**
	 * Constructor.
	 */
	public RowSegmentDAOTest() {
		super();
	}

	/**
	 * Return the RowSegmentService field.
	 * 
	 * @return RowSegmentService mRowSegmentService
	 */
	public RowSegmentService getRowSegmentService() {
		return mRowSegmentService;
	}

	/**
	 * Set the RowSegmentService field.
	 */
	public void setRowSegmentService(RowSegmentService pNewRowSegmentService) {
		mRowSegmentService = pNewRowSegmentService;
	}
	
	/**
	 * Return the Fixture field.
	 * 
	 * @return RowSegmentHome mFixture
	 */
	public RowSegmentHome getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	public void setFixture(RowSegmentHome pNewFixture) {
		mFixture = pNewFixture;
	}
	
	/**
	 * Return the SubmissionService field.
	 * 
	 * @return SubmissionService mSubmissionService
	 */
	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService field.
	 */
	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
	}
	
	/**
	 * Test method for {@link org.cipres.treebase.dao.matrix.RowSegmentDAO#findByMatrixID(java.lang.Long)}.
	 */
	public void testFindByMatrixID() {
		String testName = "testFindByMatrixID";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1.find a matrix with rowsegment:
		//TODO
		Long matrixId = 1544L; 
		
		//2. query:
		List<RowSegment> rowSegs = getFixture().findByMatrixID(matrixId);
		
		if (logger.isInfoEnabled()) {
			logger.info(" size= " + rowSegs.size()); //$NON-NLS-1$
		}
		
//		for (String string : rowStrings) {
//			assertTrue(string != null);
//		}
		
		// 3. verify
		//String matrixSQL = "select count(*) from matrix where matrix_id = " + m.getId();
		//int count = jdbcTemplate.queryForInt(matrixSQL);
		//assertTrue(count == 1);

		//setComplete();
		//endTransaction();

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the deleteByMatrixAndColumnRange test.
	 * 
	 */
	//this test was hanged forever by mjd
	public void testdeleteByMatrixAndColumnRange() throws Exception {

	/*	fail(); // hangs forever 20081020 mjd
		
		String testName = "deleteByMatrixAndColumnRange";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new study:
		String newName = testName + " test " + Math.random();
		User submitter = (User) loadObject(User.class);
		assertTrue("Empty user table.", submitter != null);

		Study s = new Study();
		s.setName(newName);

		Submission sub = getSubmissionService().createSubmission(submitter, s);

		getFixture().store(sub);

		// force commit immediately, important:
		setComplete();
		endTransaction();

		logger.info("study created: " + s.getName() + "id = " + s.getId());
		logger.info("submission created: " + "id = " + sub.getId());

		onSetUp();

		Long studyID = s.getId();

		// 2. verify
		// String sqlStr = "select count(*) from Study where study_id=" + s.getId();
		// int count = jdbcTemplate.queryForInt(sqlStr);
		// assertTrue(count == 1);
		String studySQL = "select count(*) from Study where study_id=" + s.getId();
		String subSQL = "select count(*) from Submission where submission_id=" + sub.getId();

		// import a matrix:
		String path = "/wtset.nex"; 
		String segmentPath = "/wtset-segment.txt"; 
		File nexusFile = new File(getClass().getResource(path).toURI());
		File segmentFile = new File(getClass().getResource(segmentPath).toURI());

		Collection<File> files = new ArrayList<File>();
		files.add(nexusFile);

		long t1 = System.currentTimeMillis();
		getFixture().refresh(sub);
		// sub = (Submission) loadObject(Submission.class, sub.getId());
		s = sub.getStudy();
		assertTrue("Failed to refresh submission.", sub != null);

		getSubmissionService().addNexusFilesJDBC(sub, files, null);
		// getStudyService().addNexusFiles(s, files);

		// force commit immediately, important:
		setComplete();
		endTransaction();

		// get the matrix
		Matrix m = sub.getSubmittedMatricesReadOnly().get(0);

		logger.info("matrixId: " + m.getId());
		assertTrue(m.getId() > 0);

		//mapping fields match the file:
		List<RowSegmentField> mappedFields = new ArrayList<RowSegmentField>();
		mappedFields.add(RowSegmentField.TAXONLABEL);
		mappedFields.add(RowSegmentField.LATITUDE);
		mappedFields.add(RowSegmentField.LONGITUDE);
		mappedFields.add(RowSegmentField.COUNTRY);
		mappedFields.add(RowSegmentField.STATE);
		mappedFields.add(RowSegmentField.LOCALITY);
		//mappedFields.add(RowSegmentField.ELEVATION);
		mappedFields.add(RowSegmentField.SAMPLE_DATE);
		mappedFields.add(RowSegmentField.START_INDEX);
		mappedFields.add(RowSegmentField.END_INDEX);
		mappedFields.add(RowSegmentField.GENBANK_ACC_NUM);
		//mappedFields.add(RowSegmentField.OTHER_ACC_NUM);
		mappedFields.add(RowSegmentField.INST_ACRONYM);
		mappedFields.add(RowSegmentField.ELEVATION);
		//mappedFields.add(RowSegmentField.NOTES);
		
		ExecutionResult result = getRowSegmentService().createSegments(studyID, m.getId(), segmentFile, mappedFields, false);

		//total 14 entries in the mapping file:
		logger.info(result.getErrorMessage());
		logger.info(" succeed=" + result.getSuccessfulCount() + " failure=" + result.getFailureCount());
		assertTrue("total count mismatch.", result.getSuccessfulCount() + result.getFailureCount() == 14);
		
		
		// 4. verify
	   String sqlStr = "select count(*) from ROWSEGMENT rs, MATRIXROW r where r.MATRIXROW_ID = rs.MATRIXROW_ID and r.MATRIX_ID =" + m.getId();
	   int count = jdbcTemplate.queryForInt(sqlStr);
	   assertTrue("verify succeed count", count == result.getSuccessfulCount());

	   //5 test:
	   int start = 5;
	   int end = 30;
	   String colSizeStr = "select count(*) from ROWSEGMENT rs, MATRIXROW r where r.MATRIXROW_ID = rs.MATRIXROW_ID and r.MATRIX_ID =" + m.getId()
	   + " and rs.startIndex between " + start + " and " + end + " and rs.endIndex between " + start + " and " + end;
	   int colSizeJDBC = jdbcTemplate.queryForInt(colSizeStr);
	   
	   int deleteCount = getFixture().deleteByMatrixAndColumnRange(m.getId(), start, end);
	   
	   logger.info("colSizeJD =" + colSizeJDBC + " deleteCount=" + deleteCount);
	   assertTrue("verify count", colSizeJDBC == deleteCount);
	   
	   //6, delete
		onSetUp();

		Submission s2 = (Submission) hibernateTemplate.get(Submission.class, sub.getId());
		t1 = System.currentTimeMillis();
		getSubmissionService().deleteSubmission(s2);
		setComplete();
		endTransaction();

		// 6. verify delete:
		int countVerify = jdbcTemplate.queryForInt(subSQL);
		assertTrue("Submission deletion failed.", countVerify == 0);
		countVerify = jdbcTemplate.queryForInt(studySQL);
		assertTrue("Study deletion failed.", countVerify == 0);
		countVerify = jdbcTemplate.queryForInt(sqlStr);
		assertTrue("Segment deletion failed.", countVerify == 0);


		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	*/}
}
