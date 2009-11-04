
package org.cipres.treebase.util;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.hibernate.Transaction;

public class DeleteStudy extends AbstractStandalone {

	StudyService studyService = null;

	public static void main(String[] args) {
		DeleteStudy ds = new DeleteStudy();
		ds.setupContext();
		for (String argStr : args) {
			Long studyID = Long.parseLong(argStr);
			Study s = ContextManager.getStudyService().findByID(studyID);
			if (s == null) continue;
			
			Submission sub = s.getSubmission();
			Boolean destroyed = false;
			
			if (sub != null) {
				Transaction t = ds.beginTransaction();
				try {
					ContextManager.getSubmissionService().deleteSubmission(sub);
					t.commit();
					destroyed = true;
				} catch (Throwable e) {
					t.rollback();
				}
			}

			System.err.println((destroyed ? "Destroyed" : "Failed to destroy") + " study " + studyID);
		}
	}
}


