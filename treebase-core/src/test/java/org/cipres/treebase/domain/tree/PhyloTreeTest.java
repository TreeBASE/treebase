
package org.cipres.treebase.domain.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;

/**
 * PhyloTreeTest.java
 * 
 * Created on Sep 17, 2007
 * @author JRUAN
 *
 */
public class PhyloTreeTest extends AbstractDAOTest {

	private SubmissionService mSubmissionService;
	private StudyService mStudyService;
	
	private PhyloTreeHome mPhyloTreeHome;

	/**
	 * Return the PhyloTreeHome field.
	 * 
	 * @return PhyloTreeHome mPhyloTreeHome
	 */
	public PhyloTreeHome getPhyloTreeHome() {
		return mPhyloTreeHome;
	}

	/**
	 * Set the PhyloTreeHome field.
	 */
	public void setPhyloTreeHome(PhyloTreeHome pNewPhyloTreeHome) {
		mPhyloTreeHome = pNewPhyloTreeHome;
	}
	
	/**
	 * Test build newick string performance
	 */
	public void testBuildNewickStringPerf() throws Exception {
		String testName = "buildNewickStringPerformance";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		// 1. find a tree w/ enought tree nodes:
//		String findTree = 
//			"select Phylotree_id, count(*) from PHYLOTREENODE group by PHYLOTREE_ID having count(*) > 10 ";
//		
//		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(findTree);
//		Long phyloTreeID = -1L;
//		int nodeCount = -1;
//		if (rowSet != null && rowSet.next()) {
//			phyloTreeID = rowSet.getLong(1);
//			nodeCount = rowSet.getInt(2);
//		}
//		
//		logger.info("treeId = "  + phyloTreeID + " NodeCount =" + nodeCount);
//		
//		assertTrue("No big tree found.", phyloTreeID != -1);
		Long phyloTreeID = 380L;
		
		// 2. Test:
		PhyloTree tree = (PhyloTree) loadObject(PhyloTree.class, phyloTreeID);
		
		// Skip test if database is empty
		if (tree == null) {
			logger.info("SKIPPED: " + testName + " - No PhyloTree data found in database (treeId=" + phyloTreeID + "). Test requires populated database.");
			return;
		}
		
		long t1 = System.currentTimeMillis();
		//getFixture().refresh(sub);
		int nodeCount2  = -1; //tree.getTreeNodes().size();
		String newick = tree.buildNewick();
		
		long t2 = System.currentTimeMillis();

		logger.info("build newick size: " + nodeCount2 + " time = " + (t2 - t1));
		logger.info(": " + newick);

		// 3. verify
		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Test method for {@link org.cipres.treebase.domain.tree.PhyloTree#updateNewickString()}.
	 */
	public void testUpdateNewickString() throws Exception {
		String testName = "updateNewickString";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		// 1. create a new submission:
		String newName = testName + " test " + Math.random();

		User submitter = (User) loadObject(User.class);
		
		// Skip test if database is empty
		if (submitter == null) {
			logger.info("SKIPPED: " + testName + " - No User data found in database. Test requires populated database.");
			return;
		}
		
		assertTrue("Empty user table.", submitter != null);

		Study s = new Study();
		s.setName(newName);

		Submission sub = getSubmissionService().createSubmission(submitter, s);

		getPhyloTreeHome().store(sub);

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
		 String path = "/wtset.nex";
		//String path = "/multiTrees-label.nex";
		//String path = "/charset-taxset-partition.nex";
		 //String path = "/49LBR.PDI-branch-length-continue.nex"; // continuous matrix, provided by
		// Peter M.
		File nexusFile = new File(getClass().getResource(path).toURI());
		Collection<File> files = new ArrayList<File>();
		files.add(nexusFile);

		long t1 = System.currentTimeMillis();
		getPhyloTreeHome().refresh(sub);
		// sub = (Submission) loadObject(Submission.class, sub.getId());
		s = sub.getStudy();
		assertTrue("Failed to refresh submission.", sub != null);

		getSubmissionService().addNexusFiles(sub, files, null);
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
		
		// 4. Test:
		Submission sub2 = (Submission) getPhyloTreeHome().merge(sub);
		for (PhyloTree tree : sub2.getAllSubmittedTrees()) {
			String oldNewick = tree.getNewickString();
			
			long t3 = System.currentTimeMillis();
			tree.updateNewickString();
			String newNewick = tree.getNewickString();
			
			long t4 = System.currentTimeMillis();
			logger.info("Tree Label:" + tree.getLabel() +" updateNewick time=" + (t4-t3));
			logger.info(" old newick=" + oldNewick);
			logger.info(" new newick=" + newNewick);
			//clobLength + "content = " + clobStr);
			assertTrue(newNewick.length() > 1);
			
			
		}

		// 5. delete: delete submission:
		// after add Nexus files, sub is outdated:

		//hibernateTemplate.setFlushMode(HibernateAccessor.FLUSH_COMMIT);

		Submission s2 = (Submission) hibernateTemplate.get(Submission.class, sub.getId());
		getSubmissionService().deleteSubmission(s2);
		// getStudyService().deleteStudy(s);
		setComplete();
		endTransaction();

		// 6. verify delete:
		int countVerify = jdbcTemplate.queryForInt(subSQL);
		assertTrue("Submission deletion failed.", countVerify == 0);
		countVerify = jdbcTemplate.queryForInt(studySQL);
		assertTrue("Study deletion failed.", countVerify == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * @return the submissionService
	 */
	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * @param pSubmissionService the submissionService to set
	 */
	public void setSubmissionService(SubmissionService pSubmissionService) {
		mSubmissionService = pSubmissionService;
	}

	/**
	 * @return the studyService
	 */
	public StudyService getStudyService() {
		return mStudyService;
	}

	/**
	 * @param pStudyService the studyService to set
	 */
	public void setStudyService(StudyService pStudyService) {
		mStudyService = pStudyService;
	}

}
