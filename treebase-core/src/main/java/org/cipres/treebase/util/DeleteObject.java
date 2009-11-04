package org.cipres.treebase.util;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;

public class DeleteObject extends AbstractStandalone implements DeleteObjectInterface {

	MatrixHome matrixHome;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setupContext();
		DeleteObjectInterface me = (DeleteObjectInterface) ContextManager.getBean("deleteObject");
		String className = args[0].toLowerCase();
		
		for (int i=1; i < args.length; i++) {
			Long mId = Long.decode(args[i]);
			me.doDeleteObject(className, mId);
		}
	}
	
	public void doDeleteObject(String className, Long id) {
		if (className.equals("matrix")) doDeleteMatrix(id);
		else if (className.equals("taxonlabel")) doDeleteTaxonLabel(id);
		else die("Unrecognized class name '" + className + "'");
	}
	
	public void doDeleteTaxonLabel(Long id) {
		TaxonLabelHome tlh = (TaxonLabelHome) ContextManager.getBean("taxonLabelHome");
		TaxonLabel tl = tlh.findPersistedObjectByID(TaxonLabel.class, id);
		if (tl == null) return;
		tlh.delete(tl);
	}

	public void doDeleteMatrix(Long id) {
		MatrixHome mh = (MatrixHome) ContextManager.getBean("matrixHome");
		Matrix m = mh.findPersistedObjectByID(Matrix.class, id);
		if (m == null) return;
		
		if (m instanceof CharacterMatrix) {
			CharacterMatrix cm = (CharacterMatrix) m;
			mh.cascadeDeleteRows(cm.getRowsReadOnly());
			mh.cascadeDeleteElements(cm);
			mh.cascadeDeleteColumns(cm);
		}
		mh.delete(m);
	}

}
