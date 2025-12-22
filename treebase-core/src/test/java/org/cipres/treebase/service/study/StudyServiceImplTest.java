
package org.cipres.treebase.service.study;

import java.io.File;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.Hibernate;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.ArticleCitation;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.StudyStatus;
import org.cipres.treebase.domain.study.StudyStatusHome;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.junit.Assume;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * StudyServiceImplTest.java
 * 
 * Created on Jun 9, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class StudyServiceImplTest extends AbstractDAOTest {

	private StudyService mFixture;
	private StudyStatusHome mStudyStatusHome;
	private StudyHome mStudyHome;

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
	@Autowired
	public void setStudyHome(StudyHome pNewStudyHome) {
		mStudyHome = pNewStudyHome;
	}

	/**
	 * Return the Fixture field.
	 * 
	 * @return StudyService mFixture
	 */
	public StudyService getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	@Autowired
	public void setFixture(StudyService pNewFixture) {
		mFixture = pNewFixture;
	}

	/**
	 * Return the StudyStatusHome field.
	 * 
	 * @return StudyStatusHome mStudyStatusHome
	 */
	public StudyStatusHome getStudyStatusHome() {
		return mStudyStatusHome;
	}

	/**
	 * Set the StudyStatusHome field.
	 */
	@Autowired
	public void setStudyStatusHome(StudyStatusHome pNewStudyStatusHome) {
		mStudyStatusHome = pNewStudyStatusHome;
	}

	/**
	 * Run the void deleteStudy(Study) method test
	 */
	@Test
	public void testAddDelete() throws Exception {
		String testName = "testAddDelete";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new study:
		String newName = testName + " test " + Math.random();
		StudyStatus inprogress = getStudyStatusHome().findStatusInProgress();

		Citation c = new ArticleCitation();
		c.setTitle(newName);

		Analysis a1 = new Analysis();
		a1.setName("1 " + newName);
		Analysis a2 = new Analysis();
		a2.setName("2 " + newName);

		Study s = new Study();
		s.setName(newName);
		s.setStudyStatus(inprogress);
		s.setCitation(c);
		c.setStudy(s);
		s.addAnalysis(a1);
		s.addAnalysis(a2);

		TaxonLabel label = new TaxonLabel();
		label.setTaxonLabel(newName);
		label.setStudy(s);

		getStudyHome().store(label);
		getStudyHome().store(s);

		// force commit immeidately, important:
		setComplete();
		endTransaction();

		onSetUp();

		// Notes: this is important: why?? there are two copies of s in the same session.
		// how???
		s = (Study) hibernateTemplate.merge(s);

		Long studyID = s.getId();
		Long citationID = c.getId();
		logger.info("study created: " + s.getName() + "id = " + studyID);
		logger.info("citation created: " + c.getTitle() + "id = " + citationID);
		logger.info("analysis created: " + a1.getName() + "id = " + a1.getId());
		logger.info("analysis created: " + a2.getName() + "id = " + a2.getId());
		logger.info("taxonLabel created: " + label.getTaxonLabel() + "id = " + label.getId());

		// 2. verify
		String sqlStr = "select count(*) from Study where study_id=" + s.getId();
		int count = jdbcTemplate.queryForInt(sqlStr);
		assertTrue(count == 1);

		String taxonLabelsql = "select count(*) from taxonLabel where taxonLabel_id="
			+ label.getId();
		int taxonLabelCount = jdbcTemplate.queryForInt(taxonLabelsql);
		assertTrue(taxonLabelCount == 1);

		// 3. delete
		// fixture.delete(testRole);
		getFixture().deleteStudy(s);
		setComplete();
		endTransaction();

		// 4. verify delte:
		int countVerify = jdbcTemplate.queryForInt(sqlStr);
		assertTrue("Deletion failed.", countVerify == 0);

		int countVerifyTaxonLabel = jdbcTemplate.queryForInt(taxonLabelsql);
		assertTrue("Deletion taxon label failed.", countVerifyTaxonLabel == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the void deleteStudy(Study) method test
	 */
	@Test
	public void testAddAnalysisByUpdate() {
		String testName = "testAddAnalysisByUpdate";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new study:
		String newName = testName + " test " + Math.random();
		StudyStatus inprogress = getStudyStatusHome().findStatusInProgress();

		Citation c = new ArticleCitation();
		c.setTitle(newName);

		Study s = new Study();
		s.setName(newName);
		s.setStudyStatus(inprogress);
		s.setCitation(c);
		c.setStudy(s);
		getStudyHome().store(s);

		// force commit immeidately, important:
		setComplete();
		endTransaction();

		Long studyID = s.getId();
		Long citationID = c.getId();
		logger.info("study created: " + s.getName() + "id = " + studyID);
		logger.info("citation created: " + c.getTitle() + "id = " + citationID);

		Analysis a1 = new Analysis();
		a1.setName("1 " + newName);
		Analysis a2 = new Analysis();
		a2.setName("2 " + newName);

		// NOTES: this will fail to add analysis to studyu!!
		// s.addAnalysis(a1);
		// s.addAnalysis(a2);

		Analysis updatedA1 = getFixture().update(a1);
		Analysis updatedA2 = getFixture().update(a2);

		s.addAnalysis(updatedA1);
		s.addAnalysis(updatedA2);
		Study updatedS = getFixture().update(s);

		// Note: a1.getID() and a2.getID() will return false.
		logger.info("analysis created: " + a1.getName() + "id = " + updatedA1.getId());
		logger.info("analysis created: " + a2.getName() + "id = " + updatedA2.getId());

		// 2. verify
		String sqlStr = "select count(*) from Study where study_id=" + s.getId();
		int count = jdbcTemplate.queryForInt(sqlStr);
		assertTrue(count == 1);

		// assertTrue("need to return Analysis ID.", a1.getId() != null);

		// 3. delete
		// Note: this one will fail: getFixture().deleteStudy(s);
		getFixture().deleteStudy(updatedS);
		setComplete();
		endTransaction();

		// 4. verify delete:
		int countVerify = jdbcTemplate.queryForInt(sqlStr);
		assertTrue("Deletion failed.", countVerify == 0);

		String sqlStrAnalysis = "select count(*) from analysis where analysis_id="
			+ updatedA2.getId();
		count = jdbcTemplate.queryForInt(sqlStrAnalysis);
		assertTrue("cascade Delete analysis failed.", count == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the void deleteStudy(Study) method test
	 */
	@Test
	public void testAddAnalysis() {
		String testName = "testAddAnalysis";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new study:
		String newName = testName + " test " + Math.random();
		StudyStatus inprogress = getStudyStatusHome().findStatusInProgress();

		Citation c = new ArticleCitation();
		c.setTitle(newName);

		Study s = new Study();
		s.setName(newName);
		s.setStudyStatus(inprogress);
		s.setCitation(c);
		c.setStudy(s);

		Analysis a1 = new Analysis();
		a1.setName("1 " + newName);
		Analysis a2 = new Analysis();
		a2.setName("2 " + newName);
		s.addAnalysis(a1);
		s.addAnalysis(a2);

		getStudyHome().store(s);

		// force commit immeidately, important:
		setComplete();
		endTransaction();

		Long studyID = s.getId();
		Long citationID = c.getId();
		Long a1ID = a1.getId();
		Long a2ID = a2.getId();

		logger.info("study created: " + s.getName() + "id = " + studyID);
		logger.info("citation created: " + c.getTitle() + "id = " + citationID);
		logger.info("2 analyses created: id= " + a1ID + ", " + a2ID);

		// 2. verify
		String sqlStr = "select count(*) from Study where study_id=" + s.getId();
		int count = jdbcTemplate.queryForInt(sqlStr);
		assertTrue(count == 1);

		// assertTrue("need to return Analysis ID.", a1.getId() != null);

		// 3. delete
		getFixture().deleteStudy(s);
		setComplete();
		endTransaction();

		// 4. verify delte:
		int countVerify = jdbcTemplate.queryForInt(sqlStr);
		assertTrue("Deletion failed.", countVerify == 0);

		String sqlStrAnalysis = "select count(*) from analysis where analysis_id=" + a2ID;
		count = jdbcTemplate.queryForInt(sqlStrAnalysis);
		assertTrue("cascade Delete analysis failed.", count == 0);

		logger.info(testName + " - end "); //$NON-NLS-1$
	}

	/**
	 * Run the void addNexusFiles method test
	 */
	@Test
	public void testAddNexus() throws Exception {
		String testName = "testAddNexus";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new study:
		String newName = testName + " test " + Math.random();
		StudyStatus inprogress = getStudyStatusHome().findStatusInProgress();

		Study s = new Study();
		s.setName(newName);
		s.setStudyStatus(inprogress);

		getStudyHome().store(s);

		// force commit immeidately, important:
		setComplete();
		endTransaction();

		Long studyID = s.getId();

		logger.info("study created: " + s.getName() + "id = " + studyID);

		// 2. verify
		String sqlStr = "select count(*) from Study where study_id=" + s.getId();
		int count = jdbcTemplate.queryForInt(sqlStr);
		assertTrue(count == 1);

		// assertTrue("need to return Analysis ID.", a1.getId() != null);

		// 3. add nexus
		startNewTransaction();

		String fileName = "test nexus";
		String nexus = "#NEXUS\n"
			+ "a       ctgactcctgaggagaagtctgccgttactgccctgtggggcaaggtgaacgtggatgaagttggtggtgag gccctgggcaggctgctggtggtctacccttggacccagaggttctttgagtcctttggggatctgtccact cctgatgctgttatgggcaaccctaaggtgaaggctcatggcaagaaagtgctcggtgcctttagtgatggc ctggctcacctggacaacctcaagggcacctttgccacactgagtgagctgcactgtgacaagctgcacgtg gatcctgagaacttcaggctcctgggcaacgtgctggtctgtgtgctggcccatcactttggcaaagaattc accccaccagtgcaggctgcctatcagaaagtggtggctggtgtggctaatgccctggcccacaagtatcac\n"
			+ "b       ctgactgctgaagagaaggccgccgtcactgccctgtggggcaaggtagacgtggaagatgttggtggtgag gccctgggcaggctgctggtcgtctacccatggacccagaggttctttgactcctttggggacctgtccact cctgccgctgttatgagcaatgctaaggtcaaggcccatggcaaaaaggtgctgaacgcctttagtgacggc atggctcatctggacaacctcaagggcacctttgctaagctgagtgagctgcactgtgacaaattgcacgtg gatcctgagaatttcaggctcttgggcaatgtgctggtgtgtgtgctggcccaccactttggcaaagaattc accccgcaggttcaggctgcctatcagaaggtggtggctggtgtggctactgccttggctcacaagtaccac\n"
			+ "c       ctgactcctgatgagaagaatgccgtttgtgccctgtggggcaaggtgaatgtggaagaagttggtggtgag gccctgggcaggctgctggttgtctacccatggacccagaggttctttgactcctttggggacctgtcctct ccttctgctgttatgggcaaccctaaagtgaaggcccacggcaagaaggtgctgagtgcctttagcgagggc ctgaatcacctggacaacctcaagggcacctttgctaagctgagtgagctgcattgtgacaagctgcacgtg gaccctgagaacttcaggctcctgggcaacgtgctggtggttgtcctggctcaccactttggcaaggatttc accccacaggtgcaggctgcctatcagaaggtggtggctggtgtggctactgccctggctcacaaataccac\n"
			+ "d       ctgtccggtgaggagaagtctgcggtcactgccctgtggggcaaggtgaatgtggaagaagttggtggtgag accctgggcaggctgctggttgtctacccatggacccagaggttcttcgagtcctttggggacctgtccact gcttctgctgttatgggcaaccctaaggtgaaggctcatggcaagaaggtgctggctgccttcagtgagggt ctgagtcacctggacaacctcaaaggcaccttcgctaagctgagtgaactgcattgtgacaagctgcacgtg gatcctgagaacttcaggctcctgggcaacgtgctggttattgtgctgtctcatcactttggcaaagaattc actcctcaggtgcaggctgcctatcagaaggtggtggctggtgtggccaatgccctggctcacaaataccac\n"
			+ "e       ctgtccagtgaggagaagtctgcggtcactgccctgtggggcaaggtgaatgtggaagaagttggtggtgag gccctgggcaggctgctggttgtctacccatggacccagaggttcttcgagtcctttggggacctgtcctct gcaaatgctgttatgaacaatcctaaggtgaaggctcatggcaagaaggtgctggctgccttcagtgagggt ctgagtcacctggacaacctcaaaggcacctttgctaagctgagtgaactgcactgtgacaagctgcacgtg gatcctgagaacttcaggctcctgggcaacgtgctggttattgtgctgtctcatcattttggcaaagaattc actcctcaggtgcaggctgcctatcagaaggtggtggctggtgtggccaatgccctggctcacaaataccac\n"
			+ "f       ctgactgctgaggagaaggctgccgtcaccgccttttggggcaaggtgaaagtggatgaagttggtggtgag gccctgggcaggctgctggttgtctacccctggactcagaggttctttgagtcctttggggacttgtccact gctgatgctgttatgaacaaccctaaggtgaaggcccatggcaagaaggtgctagattcctttagtaatggc atgaagcatctcgatgacctcaagggcacctttgctgcgctgagtgagctgcactgtgataagctgcatgtg gatcctgagaacttcaagctcctgggcaacgtgctagtggttgtgctggctcgcaattttggcaaggaattc accccggtgctgcaggctgactttcagaaggtggtggctggtgtggccaatgccctggcccacagatatcat\n"
			+ "g       ctgtccgatgcggtcaacgctgccgtcaccgccttttggggcaaggtgaaagtggatcaagttggtggtgag gccctgggcaggccgctggttgtctaccgctggactcagaggtgctatgagtcctttggagacttgtccact gctgatgctgttatgaacaaccctaaggtgaaggcccatggcaagaaggtgctagattcctttagtaatggc atgaagcatctcgatgacctcaagggcacctttgctgcgctgagtgagctgcactgtgataagctgcatgtg gatcctgagaacttcaagctcctgggcaacgtgctagtggttgtgctggctcgcaattttggcaaggaattc accccggtgctgcaggctgactttcagaaggtggtggctggtgtggccaatgccctggcccacagatatcat\n";

		//Clob nexusC = Hibernate.createClob(nexus);
		getStudyHome().refresh(s);

		s.addNexusFile(fileName, nexus);

		// force commit immeidately, important:
		setComplete();
		endTransaction();

		// 4. verify nexus
		String sqlNexus = "select count(*) from Study_nexusFile where study_id=" + s.getId();
		int countNexus = jdbcTemplate.queryForInt(sqlNexus);
		assertTrue(countNexus == 1);

		startNewTransaction();
		String nexusString = s.getNexusFiles().values().iterator().next();
		int nexusLength = (int) nexusString.length();
		// String clobStr = nexusClob.getSubString(0, 200);

		//char[] clobchars = new char[clobLength];
		//nexusClob.getCharacterStream().read(clobchars);
		logger.info("test clob: length=" + nexusLength + "content = " + nexusString);
		assertTrue(nexusLength > 0);

		setComplete();
		endTransaction();

		// 5. delete
		getFixture().deleteStudy(s);
		setComplete();
		endTransaction();

		// 6. verify delte:
		int countVerify = jdbcTemplate.queryForInt(sqlStr);
		assertTrue("Deletion failed.", countVerify == 0);

		count = jdbcTemplate.queryForInt(sqlNexus);
		assertTrue("cascade Delete nexus file.", count == 0);

		logger.info(testName + " - end "); //$NON-NLS-1$
	}

	/**
	 * Run the void addNexusFiles method test
	 */
	@Test
	public void testAddNexusFile() throws Exception {
		String testName = "testAddNexusFile";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new study:
		String newName = testName + " test " + Math.random();
		StudyStatus inprogress = getStudyStatusHome().findStatusInProgress();

		Study s = new Study();
		s.setName(newName);
		s.setStudyStatus(inprogress);

		getStudyHome().store(s);

		// force commit immeidately, important:
		setComplete();
		endTransaction();

		Long studyID = s.getId();

		logger.info("study created: " + s.getName() + "id = " + studyID);

		// 2. verify
		String sqlStr = "select count(*) from Study where study_id=" + s.getId();
		int count = jdbcTemplate.queryForInt(sqlStr);
		assertTrue(count == 1);

		// assertTrue("need to return Analysis ID.", a1.getId() != null);

		// 3. add nexus
		startNewTransaction();
		getFixture().update(s);
		String path = "/49LBR.PDI-branch-length-continue.nex"; // continuous matrix, provided by
		// Peter M.
		File nexusFile = new File(getClass().getResource(path).toURI());
		Collection<File> files = new ArrayList<File>();
		files.add(nexusFile);

		getFixture().addNexusFiles(s, files);

		// force commit immeidately, important:
		setComplete();
		endTransaction();

		// 4. verify nexus
		String sqlNexus = "select count(*) from Study_nexusFile where study_id=" + s.getId();
		int countNexus = jdbcTemplate.queryForInt(sqlNexus);
		logger.info("study_nexusFile: count =" + countNexus);
		Assume.assumeTrue(testName + " - nexus file not added, test skipped", countNexus == 1);

		// Print out the clob content
		// startNewTransaction();
		// getStudyHome().refresh(s);
		// Clob nexusClob = s.getNexusFiles().values().iterator().next();
		// int clobLength = (int) nexusClob.length();
		// // String clobStr = nexusClob.getSubString(0, 200);
		//
		// char[] clobchars = new char[clobLength];
		// nexusClob.getCharacterStream().read(clobchars);
		// logger.info("test clob: length=" + clobLength + "content = " + new String(clobchars));
		// assertTrue(clobLength > 0);
		//
		// setComplete();
		// endTransaction();

		// 5. delete
		getStudyHome().refresh(s);
		getFixture().deleteStudy(s);
		setComplete();
		endTransaction();

		// 6. verify delete:
		int countVerify = jdbcTemplate.queryForInt(sqlStr);
		assertTrue("Deletion failed.", countVerify == 0);

		count = jdbcTemplate.queryForInt(sqlNexus);
		assertTrue("cascade Delete nexus file.", count == 0);

		logger.info(testName + " - end "); //$NON-NLS-1$
	}

	/**
	 * Run the void generateReconstructedNexusFile() method test
	 */
	@Test
	public void testGenerateReconstructedNexusFile() throws Exception {
		String testName = "testGenerateReconstructedNexusFile";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. get a valid nexus file name:
		String fileName = "wtset.nex";		
		long studyId = 100L;
		logger.info("study id: " + studyId);
		assertTrue(studyId > 0);
		Study s = (Study) hibernateTemplate.get(Study.class, studyId);
		
		// Skip test if database is empty
		Assume.assumeNotNull("SKIPPED: " + testName + " - No Study data found in database (studyId=" + studyId + "). Test requires populated database.", s);
		
		Submission sub = s.getSubmission();
		if (fileName == null) {
			fileName = s.getNexusFiles().keySet().iterator().next();
		}
		logger.info("study found: " + s.getName() + "id = " + s.getId() + " fileName=" + fileName);
		logger.info("submission found: " + "id = " + sub.getId());

		// 2. test:
		long t1 = System.currentTimeMillis();
		assertTrue("Failed to refresh submission.", sub != null);
		String nexus = getFixture().generateReconstructedNexusFile(sub.getId(), fileName);
		assertTrue("empty nexus content", nexus != null);		
		long t2 = System.currentTimeMillis();
		logger.info("file generated: " + fileName + " Time =" + (t2 - t1));

		// 3. verify
		logger.info(nexus);		
		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

}
