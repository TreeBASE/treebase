/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

