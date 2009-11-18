
package org.cipres.treebase.service.study;

import java.io.File;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Collection;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.nexus.mesquite.MesquiteConverter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.study.SubmissionService;

/**
 * SubmissionServiceImplTest.java
 * 
 * Created on Jun 9, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class SubmissionServiceImplTest extends AbstractDAOTest {

	private SubmissionService mFixture;

	private StudyService mStudyService;
	private StudyHome mStudyHome;
	private SubmissionHome mSubmissionHome;

	/**
	 * Constructor.
	 */
	public SubmissionServiceImplTest() {
		super();
	}

	/**
	 * Return the SubmissionHome field.
	 * 
	 * @return SubmissionHome mSubmissionHome
	 */
	public SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	/**
	 * Set the SubmissionHome field.
	 */
	public void setSubmissionHome(SubmissionHome pNewSubmissionHome) {
		mSubmissionHome = pNewSubmissionHome;
	}

	/**
	 * Return the StudyHome field.
	 * 
	 * @return StudyHome mStudyHome
	 */
	public StudyHome getStudyHome() {
		return mStudyHome;
	}

	/**
	 * Set the StudyHome field.
	 */
	public void setStudyHome(StudyHome pNewStudyHome) {
		mStudyHome = pNewStudyHome;
	}

	/**
	 * Return the Fixture field.
	 * 
	 * @return SubmissionService mFixture
	 */
	public SubmissionService getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	public void setFixture(SubmissionService pNewFixture) {
		mFixture = pNewFixture;
	}

	/**
	 * Return the field.
	 * 
	 * @return
	 */
	public StudyService getStudyService() {
		return mStudyService;
	}

	/**
	 * Set the field.
	 */
	public void setStudyService(StudyService pStudyService) {
		mStudyService = pStudyService;
	}

	/**
	 * Run the void createSubmissiona and deleteSubmission() methods test
	 */
	public void xtestAddDelete() throws Exception {
		String testName = "testAddDelete";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new submission:
		String newName = testName + " test " + Math.random();

		User submitter = (User) loadObject(User.class);
		assertTrue("Empty user table.", submitter != null);

		Study s = new Study();
		s.setName(newName);

		Submission sub = getFixture().createSubmission(submitter, s);

		getSubmissionHome().store(s);

		// force commit immediately, important:
		setComplete();
		endTransaction();

		logger.info("study created: " + s.getName() + "id = " + s.getId());
		logger.info("submission created: " + "id = " + sub.getId());

		onSetUp();

		// 2. verify
		String studySQL = "select count(*) from Study where study_id=" + s.getId();
		String subSQL = "select count(*) from Submission where submission_id=" + sub.getId();
		int count = jdbcTemplate.queryForInt(studySQL);
		assertTrue(count == 1);

		count = jdbcTemplate.queryForInt(subSQL);
		assertTrue(count == 1);

		// 3. delete: delete submission:
		sub = (Submission) hibernateTemplate.merge(sub);

		getFixture().deleteSubmission(sub);
		setComplete();
		endTransaction();

		// 5. verify delete:
		int countVerify = jdbcTemplate.queryForInt(subSQL);
		assertTrue("Submission deletion failed.", countVerify == 0);
		countVerify = jdbcTemplate.queryForInt(studySQL);
		assertTrue("Study deletion failed.", countVerify == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the void processNexusfile() method test
	 */
	public void testProcessNexusFile() throws Exception {
		String testName = "testProcessNexusFile";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new submission:
		String newName = testName + " test " + Math.random();

		User submitter = (User) loadObject(User.class);
		assertTrue("Empty user table.", submitter != null);

		Study s = new Study();
		s.setName(newName);

		Submission sub = getFixture().createSubmission(submitter, s);

		getSubmissionHome().store(sub);

		// force commit immediately, important:
		setComplete();
		endTransaction();

		logger.info("study created: " + s.getName() + "id = " + s.getId());
		logger.info("submission created: " + "id = " + sub.getId());

		onSetUp();

		// 2. add a nexus file:
		// String fileName = "12Tx432C.nex"; Failure
		// String fileName = "TestNexusFile.nex";
		// String fileName = "M12c11.nex";
		// String fileName = "M1389.nex"; //17k
		// String fileName = "M1001"; //489k
		// String fileName = "M999.nx";
		// String fileName = "EF1 Spiders.nex";
		String fileName = "wtset.nex";
		// String fileName = "multiTrees-label.nex";
		// String fileName = "charset-taxset-partition.nex";
		// String fileName = "49LBR.PDI-branch-length-continue.nex"; // continuous matrix, provided by
		// Peter M.

		// String fileName = "A100c2x3x96c12c17c08.tre"; //only has a tree, no matrix.
		// String fileName = "M99c2x3x96c12c31c40.nex"; // only has a matrix, no tree.
		//String fileName = "avian-ovomucoids.nex";
		//String fileName = "Combined_Bayes_orig.nex";
		
		String path = "/" + fileName;
		
		File nexusFile = new File(getClass().getResource(path).toURI());
		Collection<File> files = new ArrayList<File>();
		files.add(nexusFile);

		long t1 = System.currentTimeMillis();
		getSubmissionHome().refresh(sub);
		// sub = (Submission) loadObject(Submission.class, sub.getId());
		s = sub.getStudy();
		assertTrue("Failed to refresh submission.", sub != null);

		getFixture().addNexusFilesJDBC(sub, files, null);
		// getStudyService().addNexusFiles(s, files);

		// force commit immediately, important:
		setComplete();
		endTransaction();

		long t2 = System.currentTimeMillis();

		logger.info("files added: " + s.getName() + "id = " + s.getId() + " Time =" + (t2 - t1));
		logger.info("mesquite logging =" + MesquiteConverter.getParsingLog(fileName));
		
		// 3. verify
		String studySQL = "select count(*) from Study where study_id=" + s.getId();
		String subSQL = "select count(*) from Submission where submission_id=" + sub.getId();
		int count = jdbcTemplate.queryForInt(studySQL);
		assertTrue(count == 1);

		count = jdbcTemplate.queryForInt(subSQL);
		assertTrue(count == 1);

		onSetUp();

		// 4. delete: delete submission:
		// after add Nexus files, sub is outdated:

		// hibernateTemplate.setFlushMode(HibernateAccessor.FLUSH_COMMIT);

		Submission s2 = (Submission) hibernateTemplate.get(Submission.class, sub.getId());
		t1 = System.currentTimeMillis();
		getFixture().deleteSubmission(s2);
		// getStudyService().deleteStudy(s);
		setComplete();
		endTransaction();
		t2 = System.currentTimeMillis();

		// 5. verify delete:
		int countVerify = jdbcTemplate.queryForInt(subSQL);
		assertTrue("Submission deletion failed.", countVerify == 0);
		countVerify = jdbcTemplate.queryForInt(studySQL);
		assertTrue("Study deletion failed.", countVerify == 0);

		logger.info("study deleted: Time =" + (t2 - t1));

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	// /**
	// * Run the void processNexusfile() method test
	// */
	// public void testProcessNexusFile() throws Exception {
	// String testName = "testProcessNexusFile";
	// if (logger.isInfoEnabled()) {
	// logger.info("\n\t\tRunning Test: " + testName);
	// }
	//
	// // 1. create a new submission:
	// String newName = testName + " test " + Math.random();
	//
	// User submitter = (User) loadObject(User.class);
	// assertTrue("Empty user table.", submitter != null);
	//
	// Study s = new Study();
	// s.setName(newName);
	//
	// Submission sub = getFixture().createSubmission(submitter, s);
	//
	// getSubmissionHome().store(sub);
	//
	// // force commit immediately, important:
	// setComplete();
	// endTransaction();
	//
	// logger.info("study created: " + s.getName() + "id = " + s.getId());
	// logger.info("submission created: " + "id = " + sub.getId());
	//
	// onSetUp();
	//
	// // 2. add a nexus file:
	// // String path = "/12Tx432C.nex"; Failure
	// // String path = "/TestNexusFile.nex";
	// // String path = "/M12c11.nex";
	// String path = "/M1389.nex"; //17k
	// // String path = "/M1001"; //489k
	// //String path = "/M999.nx";
	// // String path = "/EF1 Spiders.nex";
	// //String path = "/wtset.nex";
	// //String path = "/multiTrees-label.nex";
	// // String path = "/charset-taxset-partition.nex";
	// // String path = "/49LBR.PDI-branch-length-continue.nex"; // continuous matrix, provided by
	// // Peter M.
	// File nexusFile = new File(getClass().getResource(path).toURI());
	// Collection<File> files = new ArrayList<File>();
	// files.add(nexusFile);
	//
	// long t1 = System.currentTimeMillis();
	// getSubmissionHome().refresh(sub);
	// // sub = (Submission) loadObject(Submission.class, sub.getId());
	// s = sub.getStudy();
	// assertTrue("Failed to refresh submission.", sub != null);
	//
	// getFixture().processNexusFiles(sub, files, null);
	// getStudyService().addNexusFiles(s, files);
	//
	// // force commit immediately, important:
	// setComplete();
	// endTransaction();
	//
	// long t2 = System.currentTimeMillis();
	//
	// logger.info("files added: " + s.getName() + "id = " + s.getId() + " Time =" + (t2 - t1));
	//
	// // 3. verify
	// String studySQL = "select count(*) from Study where study_id=" + s.getId();
	// String subSQL = "select count(*) from Submission where submission_id=" + sub.getId();
	// int count = jdbcTemplate.queryForInt(studySQL);
	// assertTrue(count == 1);
	//
	// count = jdbcTemplate.queryForInt(subSQL);
	// assertTrue(count == 1);
	//
	// onSetUp();
	//
	// // verify clob:
	// // Notes: this is important: why?? there are two copies of s in the same session.
	// // how???
	//
	// Study study2 = (Study) hibernateTemplate.load(Study.class, sub.getStudy().getId());
	//
	// // hibernateTemplate.refresh(sub);
	// Clob nexusClob = study2.getNexusFiles().values().iterator().next();
	// int clobLength = (int) nexusClob.length();
	// char[] clobchars = new char[clobLength];
	// nexusClob.getCharacterStream().read(clobchars);
	// String clobStr = new String(clobchars);
	// logger.info("test clob: length=" + clobLength + "content = " + clobStr);
	// assertTrue(clobLength > 0);
	//
	// // 4. delete: delete submission:
	// // after add Nexus files, sub is outdated:
	//
	// // hibernateTemplate.setFlushMode(HibernateAccessor.FLUSH_COMMIT);
	//
	// Submission s2 = (Submission) hibernateTemplate.get(Submission.class, sub.getId());
	// t1 = System.currentTimeMillis();
	// getFixture().deleteSubmission(s2);
	// // getStudyService().deleteStudy(s);
	// setComplete();
	// endTransaction();
	// t2 = System.currentTimeMillis();
	//
	// // 5. verify delete:
	// int countVerify = jdbcTemplate.queryForInt(subSQL);
	// assertTrue("Submission deletion failed.", countVerify == 0);
	// countVerify = jdbcTemplate.queryForInt(studySQL);
	// assertTrue("Study deletion failed.", countVerify == 0);
	//
	// logger.info("study deleted: Time =" + (t2 - t1));
	//
	// if (logger.isInfoEnabled()) {
	// logger.info(testName + " - end "); //$NON-NLS-1$
	// }
	// }

	/**
	 * Run the void addNexus() method test
	 */
	public void xxtestAddNexus() throws Exception {
		String testName = "testAddNexus";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new submission:
		String newName = testName + " test " + Math.random();

		User submitter = (User) loadObject(User.class);
		assertTrue("Empty user table.", submitter != null);

		Study s = new Study();
		s.setName(newName);

		Submission sub = getFixture().createSubmission(submitter, s);

		getSubmissionHome().store(sub);

		// force commit immediately, important:
		setComplete();
		endTransaction();

		logger.info("study created: " + s.getName() + "id = " + s.getId());
		logger.info("submission created: " + "id = " + sub.getId());

		onSetUp();

		// 2. add a nexus file:
		// String path = "/12Tx432C.nex"; Failure
		// String path = "/TestNexusFile.nex";
		// String path = "/M12c11.nex";
		String path = "/M1389.nex"; // 17k
		// String path = "/M1001"; //489k
		// String path = "/M999.nx";
		// String path = "/EF1 Spiders.nex";
		// String path = "/wtset.nex";
		// String path = "/multiTrees-label.nex";
		// String path = "/charset-taxset-partition.nex";
		// String path = "/49LBR.PDI-branch-length-continue.nex"; // continuous matrix, provided by
		// Peter M.
		File nexusFile = new File(getClass().getResource(path).toURI());
		Collection<File> files = new ArrayList<File>();
		files.add(nexusFile);

		long t1 = System.currentTimeMillis();
		getSubmissionHome().refresh(sub);
		// sub = (Submission) loadObject(Submission.class, sub.getId());
		s = sub.getStudy();
		assertTrue("Failed to refresh submission.", sub != null);

		getFixture().addNexusFiles(sub, files, null);
		getStudyService().addNexusFiles(s, files);

		// force commit immediately, important:
		setComplete();
		endTransaction();

		long t2 = System.currentTimeMillis();

		logger.info("files added: " + s.getName() + "id = " + s.getId() + " Time =" + (t2 - t1));

		// 3. verify
		String studySQL = "select count(*) from Study where study_id=" + s.getId();
		String subSQL = "select count(*) from Submission where submission_id=" + sub.getId();
		int count = jdbcTemplate.queryForInt(studySQL);
		assertTrue(count == 1);

		count = jdbcTemplate.queryForInt(subSQL);
		assertTrue(count == 1);

		onSetUp();

		// verify clob:
		// Notes: this is important: why?? there are two copies of s in the same session.
		// how???

		Study study2 = (Study) hibernateTemplate.load(Study.class, sub.getStudy().getId());

		// hibernateTemplate.refresh(sub);
		String nexusString = study2.getNexusFiles().values().iterator().next();
		int nexusStringLength = (int) nexusString.length();
		logger.info("test clob: length=" + nexusStringLength + "content = " + nexusString);
		assertTrue(nexusStringLength > 0);

		// 4. delete: delete submission:
		// after add Nexus files, sub is outdated:

		// hibernateTemplate.setFlushMode(HibernateAccessor.FLUSH_COMMIT);

		Submission s2 = (Submission) hibernateTemplate.get(Submission.class, sub.getId());
		t1 = System.currentTimeMillis();
		getFixture().deleteSubmission(s2);
		// getStudyService().deleteStudy(s);
		setComplete();
		endTransaction();
		t2 = System.currentTimeMillis();

		// 5. verify delete:
		int countVerify = jdbcTemplate.queryForInt(subSQL);
		assertTrue("Submission deletion failed.", countVerify == 0);
		countVerify = jdbcTemplate.queryForInt(studySQL);
		assertTrue("Study deletion failed.", countVerify == 0);

		logger.info("study deleted: Time =" + (t2 - t1));

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the void addNexus() method test
	 */
	public void xtestAddNexusToExisting() throws Exception {
		String testName = "testAddNexusToExistingStudy";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		Study s = getTestStudy();
		Submission sub = s.getSubmission();

		// force commit immediately, important:
		setComplete();
		endTransaction();

		logger.info("study found: " + s.getName() + "id = " + s.getId());
		logger.info("submission found: " + "id = " + sub.getId());

		onSetUp();

		// 2. add a nexus file:
		// String path = "/12Tx432C.nex"; Failure
		// String path = "/TestNexusFile.nex";
		// String path = "/M12c11.nex";
		// String path = "/wtset.nex";
		String path = "/charset-taxset-partition.nex";
		// String path = "/49LBR.PDI-branch-length-continue.nex"; // continuous matrix, provided by
		// Peter M.
		File nexusFile = new File(getClass().getResource(path).toURI());
		Collection<File> files = new ArrayList<File>();
		files.add(nexusFile);

		long t1 = System.currentTimeMillis();
		getSubmissionHome().refresh(sub);
		// sub = (Submission) loadObject(Submission.class, sub.getId());
		s = sub.getStudy();
		assertTrue("Failed to refresh submission.", sub != null);

		getFixture().addNexusFiles(sub, files, null);
		getStudyService().addNexusFiles(s, files);

		// force commit immediately, important:
		setComplete();
		endTransaction();

		long t2 = System.currentTimeMillis();

		logger.info("files added: " + s.getName() + "id = " + s.getId() + " Time =" + (t2 - t1));

		onSetUp();

		// 3. verify
		String studySQL = "select count(*) from Study where study_id=" + s.getId();
		String subSQL = "select count(*) from Submission where submission_id=" + sub.getId();
		int count = jdbcTemplate.queryForInt(studySQL);
		assertTrue(count == 1);

		count = jdbcTemplate.queryForInt(subSQL);
		assertTrue(count == 1);

		// verify clob:
		hibernateTemplate.refresh(sub);
		String nexusString = sub.getStudy().getNexusFiles().values().iterator().next();
		int nexusStringLength = (int) nexusString.length();
		logger.info("test clob: length=" + nexusStringLength + "content = " + nexusString);
		assertTrue(nexusStringLength > 0);

		// 4. delete: delete submission:
		// after add Nexus files, sub is outdated:

		// getFixture().deleteSubmission(sub);
		// getStudyService().deleteStudy(s);
		// setComplete();
		// endTransaction();

		// 5. verify delete:
		// int countVerify = jdbcTemplate.queryForInt(subSQL);
		// assertTrue("Submission deletion failed.", countVerify == 0);
		// countVerify = jdbcTemplate.queryForInt(studySQL);
		// assertTrue("Study deletion failed.", countVerify == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the void deleteSubmittedData() method test
	 */
	public void xNotWorkingtestDeleteSubmittedData() throws Exception {
		String testName = "testDeleteSubmittedData";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new submission:
		String newName = testName + " test " + Math.random();

		User submitter = (User) loadObject(User.class);
		assertTrue("Empty user table.", submitter != null);

		Study s = new Study();
		s.setName(newName);

		Submission sub = getFixture().createSubmission(submitter, s);

		getSubmissionHome().store(s);

		// force commit immediately, important:
		setComplete();
		endTransaction();

		logger.info("study created: " + s.getName() + "id = " + s.getId());
		logger.info("submission created: " + "id = " + sub.getId());

		onSetUp();

		// 2. add a nexus file:
		// String path = "/TestNexusFile.nex";
		String path = "/M12c11.nex";
		File nexusFile = new File(getClass().getResource(path).toURI());
		Collection<File> files = new ArrayList<File>();
		files.add(nexusFile);

		getSubmissionHome().refresh(sub);
		getFixture().addNexusFiles(sub, files, null);
		getStudyService().addNexusFiles(s, files);

		// force commit immediately, important:
		setComplete();
		endTransaction();

		logger.info("files added: " + s.getName() + "id = " + s.getId());

		onSetUp();

		// 3. verify
		String studySQL = "select count(*) from Study where study_id=" + s.getId();
		String subSQL = "select count(*) from Submission where submission_id=" + sub.getId();
		int count = jdbcTemplate.queryForInt(studySQL);
		assertTrue(count == 1);

		count = jdbcTemplate.queryForInt(subSQL);
		assertTrue(count == 1);

		logger.info(" verified: " + s.getName() + "id = " + s.getId());

		// 4. delete: delete submission:
		// after add Nexus files, sub is outdated:
		hibernateTemplate.refresh(sub);

		getFixture().deleteSubmittedData(sub);

		setComplete();
		endTransaction();

		// 5. verify
		String subTreeSQL = "select count(*) from sub_treeblock where submission_id = "
			+ sub.getId();
		String subMatrixSQL = "select count(*) from sub_Matrix where submission_id = "
			+ sub.getId();
		String subTaxonLabelSQL = "select count(*) from sub_TaxonLabel where submission_id = "
			+ sub.getId();
		count = jdbcTemplate.queryForInt(subTreeSQL);
		assertTrue(count == 0);
		count = jdbcTemplate.queryForInt(subMatrixSQL);
		assertTrue(count == 0);
		count = jdbcTemplate.queryForInt(subTaxonLabelSQL);
		assertTrue(count == 0);

		logger.info("verifiy submitted data deleted: ");

		// 6. delete: delete submission:
		// after add Nexus files, sub is outdated:
		onSetUp();
		hibernateTemplate.refresh(sub);

		getFixture().deleteSubmission(sub);
		setComplete();
		endTransaction();

		// 7. verify delete:
		int countVerify = jdbcTemplate.queryForInt(subSQL);
		assertTrue("Submission deletion failed.", countVerify == 0);
		countVerify = jdbcTemplate.queryForInt(studySQL);
		assertTrue("Study deletion failed.", countVerify == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the void deleteSubmittedData() method test
	 */
	public void xtestDeleteSubmittedData() throws Exception {
		String testName = "testDeleteSubmittedData";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new submission:
		Study s = getTestStudy();
		Submission sub = s.getSubmission();

		getFixture().deleteSubmittedData(sub);

		setComplete();
		endTransaction();

		// 5. verify
		String subTreeSQL = "select count(*) from sub_TreeBlock where submission_id = "
			+ sub.getId();
		String subMatrixSQL = "select count(*) from sub_Matrix where submission_id = "
			+ sub.getId();
		String subTaxonLabelSQL = "select count(*) from sub_TaxonLabel where submission_id = "
			+ sub.getId();
		int count = jdbcTemplate.queryForInt(subTreeSQL);
		assertTrue(count == 0);
		count = jdbcTemplate.queryForInt(subMatrixSQL);
		assertTrue(count == 0);
		count = jdbcTemplate.queryForInt(subTaxonLabelSQL);
		assertTrue(count == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the getPermission() method test
	 */
	public void xtestGetPermission() throws Exception {
		String testName = "testGetPermission";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// // 1. get a submission:
		// TODO get permission
		// String subSQL = "select sub.submission_id from submission sub, user u where sub.USER_ID =
		// u.user_id and u.userRole_id =";
		//		
		// Collection<Study> studies = getStudyService().findByName(PARSER_STUDY_NAME, true);
		//
		// Study s = studies.iterator().next();
		// Submission sub = s.getSubmission();
		//
		// getFixture().deleteSubmittedData(sub);
		//
		// setComplete();
		// endTransaction();
		//
		// // 5. verify
		// String subTreeSQL = "select count(*) from sub_phlyotree where submission_id = "
		// + sub.getId();
		// String subMatrixSQL = "select count(*) from sub_Matrix where submission_id = "
		// + sub.getId();
		// String subTaxonLabelSQL = "select count(*) from sub_TaxonLabel where submission_id = "
		// + sub.getId();
		// int count = jdbcTemplate.queryForInt(subTreeSQL);
		// assertTrue(count == 0);
		// count = jdbcTemplate.queryForInt(subMatrixSQL);
		// assertTrue(count == 0);
		// count = jdbcTemplate.queryForInt(subTaxonLabelSQL);
		// assertTrue(count == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}
}
