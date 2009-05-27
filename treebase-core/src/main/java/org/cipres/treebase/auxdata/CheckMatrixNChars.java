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

import java.util.Collection;

import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.util.AbstractStandalone;
import org.hibernate.SessionFactory;

/**
 * Plugin for {@see AuxiliaryDataImporter}.
 * 
 *  <p>Scan the dump file, checking that the nchar values given there match the actual values in the database.
 *  
 * @author mjd 20090320
 *
 */
public class CheckMatrixNChars extends GenericAuxDataAction implements CompleteStudyAction {

	SessionFactory sessionFactory;
	MatrixService matrixService;
	
	public Value perform(Value v) {
		AuxData aux = new AuxData((ValueStudy) v);
		for (ValueSection sec : aux.getMatrixSections()) {
			String nCharString = sec.getString("nchar");
			int expected = Integer.parseInt(nCharString);
			String legacyID = sec.getString("matrix_id");
			Collection<TBPersistable> objs 
				= getMatrixService().findSomethingByString(Matrix.class, "TB1MatrixID", legacyID);
                        if (objs.isEmpty()) {
				AbstractStandalone.warn("No matrices found for legacy ID " + legacyID);
				continue;
			}
			for (TBPersistable obj : objs) {
				CharacterMatrix m = (CharacterMatrix) obj;
				Integer actual =  m.getnChar();
				if (actual == null || actual.intValue() == 0) {
					actual = m.getColumnsReadOnly().size();
					AbstractStandalone.warn("M" + m.getId() + " is missing nChar");
				}
				if (actual != expected)
					AbstractStandalone.warn("M" + m.getId() + " has nChar " + actual + ", expected " + expected);
			}
		}
		return v;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public boolean isForTesting() {
		return false;
	}

	public void setIsForTesting(boolean ift) {
		// Ignored
	}

	public MatrixService getMatrixService() {
		return matrixService;
	}

	public void setMatrixService(MatrixService matrixService) {
		this.matrixService = matrixService;
	}

}
