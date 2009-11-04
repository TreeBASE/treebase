
package org.cipres.treebase.service.matrix;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.RowSegmentHome;
import org.cipres.treebase.domain.matrix.RowSegmentService;
import org.cipres.treebase.domain.matrix.RowSegmentService.RowSegmentField;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.framework.ExecutionResult;

/**
 * RowSegmentServiceImplTest.java
 * 
 * Created on May 6, 2008
 * 
 * @author Jin Ruan
 * 
 */
public class RowSegmentServiceImplTest extends AbstractDAOTest {

	private RowSegmentService mFixture;
	private SubmissionService mSubmissionService;
	private RowSegmentHome mRowSegmentHome;

	/**
	 * Constructor.
	 * 
	 * @param name
	 */
	public RowSegmentServiceImplTest() {
		super();
	}

	/**
	 * Return the RowSegmentHome field.
	 * 
	 * @return RowSegmentHome mRowSegmentHome
	 */
	public RowSegmentHome getRowSegmentHome() {
		return mRowSegmentHome;
	}

	/**
	 * Set the RowSegmentHome field.
	 */
	public void setRowSegmentHome(RowSegmentHome pNewRowSegmentHome) {
		mRowSegmentHome = pNewRowSegmentHome;
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
	 * Return the Fixture field.
	 * 
	 * @return RowSegmentService mFixture
	 */
	public RowSegmentService getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	public void setFixture(RowSegmentService pNewFixture) {
		mFixture = pNewFixture;
	}

	/**
	 * Run the RowSegmentField enum test.
	 * 
	 */
	public void testEnumRowSegmentField() throws Exception {

		String testName = "testEnumRowSegmentField";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. test displayName, and findByDisplayName
		EnumSet<RowSegmentField> allFields = EnumSet.allOf(RowSegmentField.class);

		logger.info("allFields size: " + allFields.size());
		assertTrue(allFields.size() > 0);

		Iterator<RowSegmentField> iter = allFields.iterator();
		iter.next();
		iter.next();

		RowSegmentField aField = iter.next();
		logger.info(" aField.name= " + aField.name() + " displayName =" + aField);
		assertFalse(aField.name().equals(aField.toString()));

		RowSegmentField foundField = RowSegmentField.findByDisplayName(aField.toString());
		assertTrue(foundField == aField);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}

	/**
	 * Run the createRowSegments test.
	 * 
	 */
	public void testCreateRowSegments() throws Exception {

		String testName = "testCreateRowSegments";
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

		getRowSegmentHome().store(sub);

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
		getRowSegmentHome().refresh(sub);
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
		
		ExecutionResult result = getFixture().createSegments(studyID, m.getId(), segmentFile, mappedFields, false);

		//total 14 entries in the mapping file:
		logger.info(result.getErrorMessage());
		logger.info(" succeed=" + result.getSuccessfulCount() + " failure=" + result.getFailureCount());
		assertTrue("total count mismatch.", result.getSuccessfulCount() + result.getFailureCount() == 14);
		
		
		// 4. verify
	   String sqlStr = "select count(*) from ROWSEGMENT rs, MATRIXROW r where r.MATRIXROW_ID = rs.MATRIXROW_ID and r.MATRIX_ID =" + m.getId();
	   int count = jdbcTemplate.queryForInt(sqlStr);
	   assertTrue("verify succeed count", count == result.getSuccessfulCount());

	   //5 delete:
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
	}
}
