
package org.cipres.treebase.util;

import org.cipres.treebase.ContextManager;
import org.hibernate.Transaction;

public class DestroySubmission extends AbstractStandalone {
	
	public static void main(String[] args) {
		DestroySubmission ds = new DestroySubmission();
		ds.setupContext();
		
		for (String argStr : args) {
			Long submissionID;
			if (argStr.equals("PSEUDO")) {
				submissionID =  PseudoSubmission.getPseudoSubmission().getId();
			} else {
				submissionID = Long.parseLong(argStr);
			}

			Boolean destroyed = false;
			Transaction t = ds.beginTransaction();
			try {
				ContextManager.getSubmissionService().deleteSubmission(submissionID);
				t.commit();
				destroyed = true;
			} catch (Throwable e) {
				t.rollback();
			}
			
			System.err.println((destroyed ? "Destroyed" : "Failed to destroy") + " submission " + argStr);
		}
	}
}

