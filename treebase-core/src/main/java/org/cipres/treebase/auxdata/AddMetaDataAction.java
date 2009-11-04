
package org.cipres.treebase.auxdata;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixKind;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.domain.tree.TreeType;
import org.cipres.treebase.util.AbstractStandalone;
import org.hibernate.SessionFactory;

/**
 * Action to add missing metadata to trees and matrices
 * 
 * <p>Specifically:
 * <ul>
 * <li>Matrix titles: called "matrix_name" in the dump file, title in the model
 * <li>Matrix character types: called "data_type" in the dump file, matrixKind in the model
 * <li>Matrix legacy IDs: called "matrix_id" in the dump file, TB1MatrixID in the model
 * <li>Tree titles: called "tree_title" in the dump file, title in the model
 * <li>Tree labels: called "tree_label" in the dump file, label in the model
 * </ul>
 * 
 * @author mjd 20090317
 *
 */
public class AddMetaDataAction extends GenericAuxDataAction implements
		CompleteStudyAction {
	
	MatrixService matrixService;
	PhyloTreeService treeService;
	
	public Value perform(Value v) {
		AuxData aux = new AuxData((ValueStudy) v);
		AbstractStandalone.warn("Analyzing study " + aux.getStudyID());
		if (! isInterestingAuxData(aux)) 
			return new ValueNone();
		
		for (ValueSection matrixSection : aux.getMatrixSections()) {
			String legacyID = matrixSection.getString("matrix_id");
			String title = matrixSection.getString("matrix_name");
			String matrixDataType = matrixSection.getString("data_type");
			
			Set<Matrix> matrices = matrixService.findMatricesByTitle(legacyID);
			for (TBPersistable obj : matrixService.findSomethingByString(Matrix.class, "TB1MatrixID", legacyID) )
				matrices.add((Matrix) obj);
			matrices.addAll(matrixService.findByNexusFile(legacyID + ".nex"));

			if (matrices.size() == 1) {
				Matrix m = matrices.iterator().next();
				warn("  Fixing matrix " + m.getId());
					
				// Map matrixDataType -> matrixKind
				{
					Collection<MatrixKind> mks = matrixService.findSomethingByString(MatrixKind.class, "description", matrixDataType);
					MatrixKind mk = null;
					if (mks.isEmpty()) {
						warn("Unknown MatrixKind '" + matrixDataType + "'; skipping it");
/*						// Or you can manufacture a fresh one here, which is not normally allowed
 *                      // Note that mk.setDescription is private, so you will have to make it public to use this code
 *                      // 20090320 MJD
                        mk = new MatrixKind();
						mk.setDescription(matrixDataType);
						mk.setId(matrixService.save(mk));
*/					} else {
						mk = mks.iterator().next();
					}
					if (mk != null) m.setMatrixKind(mk);
				}

				m.setTitle(title);
				m.setTB1MatrixID(legacyID);
				
			} else if (matrices.size() > 1) {
				warn("matrix legacy ID " + legacyID + " matches " + matrices(matrices.size()));
			}
		}
		
		for (ValueSection treeSection : aux.getTreeSections()) {
			String legacyID = treeSection.getString("tree_id");
			String label = treeSection.getString("tree_label");
			String title = treeSection.getString("tree_title");
			String type = treeSection.getString("tree_type");
			
			Set<PhyloTree> trees = new HashSet<PhyloTree> ();
			for (TBPersistable obj : treeService.findSomethingByString(PhyloTree.class, "TB1TreeID", legacyID))
				trees.add((PhyloTree) obj);
			
			if (trees.size() == 1) {
				PhyloTree t = trees.iterator().next();
				warn("  Fixing tree " + t.getId());
				
				t.setLabel(label);
				t.setTitle(title);
				
				{ // Map type -> treeType
					Collection<TreeType> tts = treeService.findSomethingByString(TreeType.class, "description", type);
					TreeType tt = null;
					if (tts.isEmpty()) {
						warn("Unknown TreeType '" + type + "'; skipping it");
/*						// Or you can manufacture a fresh one here, which is not normally allowed
 *                      // Note that tt.setDescription is private, so you will have to make it public to use this code
 *                      // 20090320 MJD
						tt = new TreeType();
						tt.setDescription(type);
						tt.setId(matrixService.save(tt));
 */
					} else {
						tt = tts.iterator().next();
					}
					if (tt != null) t.setTreeType(tt);
				}
			} else if (trees.size() > 1) {
				warn("Found " + trees(trees.size()) + " matching legacy ID " + legacyID);
			}
		}
		
		getSessionFactory().getCurrentSession().flush();		
		return v;
	}
	
	SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public boolean isForTesting() {
		return false;
	}

	public void setIsForTesting(boolean ift) {
		// ignored = ContextManager.getPhyloTreeService()
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private void warn(String s) { AbstractStandalone.warn(s); }

	private String matrices(int n) {
		switch(n) {
		case 0:	
			return "no matrices";
		case 1:
			return "one matrix";
		default:	
			return n + " matrices";
		}
	}
	
	private String trees(int n) {
		switch(n) {
		case 0:	
			return "no trees";
		case 1:
			return "one tree";
		default:	
			return n + " trees";
		}
	}

	public MatrixService getMatrixService() {
		return matrixService;
	}

	public void setMatrixService(MatrixService matrixService) {
		this.matrixService = matrixService;
	}

	public PhyloTreeService getTreeService() {
		return treeService;
	}

	public void setTreeService(PhyloTreeService treeService) {
		this.treeService = treeService;
	}
	
	
}
