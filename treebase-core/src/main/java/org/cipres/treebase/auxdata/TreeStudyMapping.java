
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
