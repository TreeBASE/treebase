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

package org.cipres.treebase.auxdata;

import org.hibernate.SessionFactory;

public class TreeStudyMapping extends GenericAuxDataAction implements
		CompleteStudyAction {

	private SessionFactory sessionFactory;
	
	public Value perform(Value v) {
		AuxData aux = new AuxData((ValueStudy) v);
		String studyID = aux.getStudyID();
		for (ValueSection matrixSection : aux.getMatrixSections()) {
			String legacyID = matrixSection.getString("matrix_id");
			System.out.println(legacyID + " " + studyID);
		}
		
		for (ValueSection treeSection : aux.getTreeSections()) {
			String legacyID = treeSection.getString("tree_id");	
			System.out.println(legacyID + " " + studyID);
		}
		
		return v;
	}

	public SessionFactory getSessionFactory() {
		// TODO Auto-generated method stub
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public boolean isForTesting() {
		// ignored
		return false;
	}

	public void setIsForTesting(boolean ift) {
		// ignored
	}
}
