
package org.cipres.treebase.service.study;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisService;
import org.cipres.treebase.domain.study.ArticleCitation;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.StudyStatus;
import org.cipres.treebase.domain.study.StudyStatusHome;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

/**
 * AnalysisDAOTest.java
 * 
 * Created on Jul 5, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class AnalysisServiceImplTest extends AbstractDAOTest {

	private AnalysisService mFixture;
	private StudyStatusHome mStudyStatusHome;
	private StudyHome mStudyHome;
	private StudyService mStudyService;

	/**
	 * Return the StudyService field.
	 * 
	 * @return StudyService mStudyService
	 */
	private StudyService getStudyService() {
		return mStudyService;
	}

	/**
	 * Set the StudyService field.
	 */
	@Autowired
	public void setStudyService(StudyService pNewStudyService) {
		mStudyService = pNewStudyService;
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
	@Autowired
	public void setStudyHome(StudyHome pNewStudyHome) {
		mStudyHome = pNewStudyHome;
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
	 * Return the Fixture field.
	 * 
	 * @return AnalysisHome mFixture
	 */
	private AnalysisService getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	@Autowired
	public void setFixture(AnalysisService pNewFixture) {
		mFixture = pNewFixture;
	}

	/**
	 * Run the void deleteStudy(Study) method test
	 */
	@Test
	public void testAddDelete() {
		String testName = "testAddDeleteAnalysis";
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

		// force commit immediately, important:
		setComplete();
		endTransaction();

		Long studyID = s.getId();
		Long citationID = c.getId();
		Long a1ID = a1.getId();
		Long a2ID = a2.getId();

		logger.info("study created: " + s.getName() + "id = " + studyID);
		logger.info("citation created: " + c.getTitle() + "id = " + citationID);
		logger.info("2 analyses created: id= " + a1ID + ", " + a2ID);
		assertTrue("need to return Analysis ID.", a1.getId() != null && a2.getId() != null);

		// 2. verify
		String sqlStr = "select count(*) from analysis where study_id=" + s.getId();
		int count = jdbcTemplate.queryForInt(sqlStr);
		assertTrue(count == 2);

		// 3. delete

		// need to start new tx since lazy loading of collections
		// need the new tx.
		startNewTransaction();

		// a1 = (Analysis) loadObject(Analysis.class, a1ID);
		// a1 = getFixture().findByID(a1ID);
		a1 = getFixture().update(a1);
		getFixture().deleteAnalysis(a1);
		setComplete();
		endTransaction();

		// 4. verify delete:
		String a1Sql = "select count(*) from analysis where analysis_id=" + a1ID;
		int countVerify = jdbcTemplate.queryForInt(a1Sql);
		assertTrue("Deletion failed.", countVerify == 0);

		// 5. delete study

		startNewTransaction();

		// need to reload s otherwise optimiticLockingException
		s = (Study) loadObject(Study.class, studyID);
		getStudyService().deleteStudy(s);
		setComplete();
		endTransaction();

		count = jdbcTemplate.queryForInt(sqlStr);
		assertTrue("Delete analysis failed.", count == 0);

		String sqlStrAnalysis = "select count(*) from analysis where analysis_id=" + a2ID;
		count = jdbcTemplate.queryForInt(sqlStrAnalysis);
		assertTrue("cascade Delete analysis failed.", count == 0);

		logger.info(testName + " - end "); //$NON-NLS-1$
	}

}
