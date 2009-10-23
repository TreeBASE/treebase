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
