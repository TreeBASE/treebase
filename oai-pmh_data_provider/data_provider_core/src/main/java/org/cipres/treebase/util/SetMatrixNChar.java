package org.cipres.treebase.util;

import java.util.Collection;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.MatrixService;

public class SetMatrixNChar extends AbstractStandalone implements
		SetMatrixNCharInterface {

	static MatrixService matrixService;
	public static void main(String[] args) throws Throwable {
		setupContext();
		SetMatrixNCharInterface me = (SetMatrixNCharInterface) ContextManager.getBean("setMatrixNChar");
		matrixService  = (MatrixService) ContextManager.getBean("matrixService");
		
				
		Collection<CharacterMatrix> matrices = ContextManager.getMatrixHome().findAll(CharacterMatrix.class);
		
		int nMatrices = matrices.size();
		int count = 0;
			
		for (CharacterMatrix m : matrices) {
			count++;	
			if (m.getnChar() != null && m.getnChar() != 0 && m.getnTax() != null && m.getnTax() != 0) continue;
			me.setMatrixNChar(m);
			int pct = (100 * count) / nMatrices;
			warn(pct + "% Matrix M" + m.getId() + " now has nCHAR = " + m.getnChar() + ", nTAX = " + m.getnTax());
		}	
	}

	public void setMatrixNChar(CharacterMatrix mDetached) {
		CharacterMatrix m = matrixService.findByID(CharacterMatrix.class,
				mDetached.getId());
		int nChar = m.getColumns().size();
		int nTax = m.getRowsReadOnly().size();
		m.setnChar(nChar);
		m.setnTax(nTax);
	}

}
