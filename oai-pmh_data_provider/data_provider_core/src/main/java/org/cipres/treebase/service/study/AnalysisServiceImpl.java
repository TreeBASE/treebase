/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.service.study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserHome;
import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisHome;
import org.cipres.treebase.domain.study.AnalysisService;
import org.cipres.treebase.domain.study.AnalysisStepService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * AnalsysisServiceImpl.java
 * 
 * Created on Jun 6, 2006
 * 
 * @author lcchan
 * 
 */
public class AnalysisServiceImpl extends AbstractServiceImpl implements AnalysisService {

	private AnalysisHome mAnalysisHome;
	private UserHome mUserHome;

	private AnalysisStepService mAnalysisStepService;

	/**
	 * Return the mAnalysisStepService field.
	 * 
	 * @return AnalysisStepService mmAnalysisStepService
	 */
	private AnalysisStepService getAnalysisStepService() {
		return mAnalysisStepService;
	}

	/**
	 * Set the mAnalysisStepService field.
	 */
	public void setAnalysisStepService(AnalysisStepService pNewAnalysisStepService) {
		mAnalysisStepService = pNewAnalysisStepService;
	}

	/**
	 * Return the AnalysisHome field.
	 * 
	 * @return AnalysisHome mAnalysisHome
	 */
	private AnalysisHome getAnalysisHome() {
		return mAnalysisHome;
	}

	/**
	 * Set the AnalysisHome field.
	 */
	public void setAnalysisHome(AnalysisHome pNewAnalysisHome) {
		mAnalysisHome = pNewAnalysisHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getAnalysisHome();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalysisService#deleteAnalysis(org.cipres.treebase.domain.study.Analysis)
	 */
	public boolean deleteAnalysis(Analysis pAnalysis) {
		if (pAnalysis == null) {
			return false;
		}

		// Need to make sure the object belongs to the current session:
		// Analysis currentAnalysis = update(pAnalysis);
		Analysis currentAnalysis = pAnalysis;

		// manage bi-directional relationships:
		Study study = currentAnalysis.getStudy();
		if (study != null) {
			study.removeAnalysis(currentAnalysis);
		}

		// Cascade delete:
		// * analysis step:
		getAnalysisStepService().deleteAnalysisSteps(currentAnalysis.getAnalysisStepsReadOnly());

		getAnalysisHome().deletePersist(currentAnalysis);

		return true;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalysisService#deleteAnalyses(java.util.Collection)
	 */
	public void deleteAnalyses(Collection<Analysis> pAnalyses) {
		if (pAnalyses == null || pAnalyses.isEmpty()) {
			return;
		}

		// make a copy to avoid the potential concurrent modification.
		Set<Analysis> copy = new HashSet<Analysis>(pAnalyses);

		for (Analysis analysis : copy) {
			deleteAnalysis(analysis);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalysisService#findByID(java.lang.Long)
	 */
	public Analysis findByID(Long pAnalysisID) {
		if (pAnalysisID == null) {
			return null;
		}
		return getAnalysisHome().findPersistedObjectByID(Analysis.class, pAnalysisID);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalysisService#getPermission(java.lang.String,
	 *      java.lang.Long)
	 */
	public TBPermission getPermission(String pUsername, Long pAnalysisId) {
		Analysis a = findByID(pAnalysisId);
		// Long submissionID = null;
		Submission sub = null;

		if (a != null) {
			sub = a.getStudy().getSubmission();
		}

		if (sub == null) {
			return TBPermission.NONE;
		}
		User user = getUserHome().findByUserName(pUsername);

		return sub.getPermission(user);
	}

	/**
	 * 
	 * @return
	 */
	private UserHome getUserHome() {
		return mUserHome;
	}

	/**
	 * Set the user home.
	 * 
	 * @param pUserHome
	 */
	public void setUserHome(UserHome pUserHome) {
		mUserHome = pUserHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalysisService#deleteAnalyses(org.cipres.treebase.domain.study.Study)
	 */
	public void deleteAnalyses(Study pStudy) {
		if (pStudy == null) {
			return;
		}

		// make a copy to avoid the potential concurrent modification.
		Collection<Analysis> copy = new ArrayList<Analysis>(pStudy.getAnalyses());

		// Need to make sure the object belongs to the current session:
		// Analysis currentAnalysis = update(pAnalysis);
		for (Analysis analysis : copy) {

			// Cascade delete:
			// * analysis step:
			getAnalysisStepService().deleteAnalysisSteps(analysis.getAnalysisStepsReadOnly());

		}
		
		// manage bi-directional relationships:
		pStudy.getAnalyses().clear();

		getAnalysisHome().deleteAll(copy);

	}

	@Override
	public Class defaultResultClass() {
		return Analysis.class;
	}

}
