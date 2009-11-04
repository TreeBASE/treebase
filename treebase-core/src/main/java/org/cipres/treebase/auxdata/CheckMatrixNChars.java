
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
