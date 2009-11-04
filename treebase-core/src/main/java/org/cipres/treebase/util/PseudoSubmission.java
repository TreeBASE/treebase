
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