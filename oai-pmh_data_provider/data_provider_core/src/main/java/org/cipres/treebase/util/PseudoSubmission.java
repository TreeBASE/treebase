/*
 * Copyright 2009 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.util;

import java.util.Collection;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;

/**
 * This class represents the dummy Submission/Study pair into which TB1 nexus files are bulk-uploaded
 * 
 * @author mjd 20090114
 */
public class PseudoSubmission {
	static Submission pseudoSubmission = null;
	static final String magicSubmissionStudyName = "PSEUDO";

	public static Submission getPseudoSubmission() {
		return getPseudoSubmission(magicSubmissionStudyName);
	}
	
	public static Submission getPseudoSubmission(String name) {
		if (pseudoSubmission == null) {
			StudyHome sh = ContextManager.getStudyHome();
			SubmissionService ss = ContextManager.getSubmissionService();
			Collection<Study> pseudoStudies = sh.findByName(name, true);
			if (pseudoStudies.size() == 0) {
				pseudoSubmission = ss.createSubmission(null, null);
				pseudoSubmission.getStudy().setName(name);
				ss.save(pseudoSubmission.getStudy());
				sh.flush();
			} else if (pseudoStudies.size() == 1) {
				Study pseudoStudy = pseudoStudies.iterator().next();
				pseudoSubmission = pseudoStudy.getSubmission();
			} else {
				return null;
			}
		}
		return pseudoSubmission;
	}
}