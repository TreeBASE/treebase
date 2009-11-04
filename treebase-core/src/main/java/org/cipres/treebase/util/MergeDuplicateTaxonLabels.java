
package org.cipres.treebase.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.MatrixRowHome;
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.cipres.treebase.domain.tree.PhyloTreeNode;
import org.hibernate.exception.ConstraintViolationException;

public class MergeDuplicateTaxonLabels extends AbstractStandalone implements MergeDuplicateTaxonLabelsInterface {
	 TaxonLabelHome taxonLabelHome;
	 StudyHome studyHome;
	 PhyloTreeHome treeHome;
	 MatrixRowHome matrixRowHome;
	
	public static void main(String[] args) {
		setupContext();
		MergeDuplicateTaxonLabelsInterface me = (MergeDuplicateTaxonLabelsInterface) ContextManager.getBean("mergeDuplicateTaxonLabels");
		GetOpts<UnixOptions> go = new GetOpts<UnixOptions>(new UnixOptions ("rs"));
		UnixOptions opts = go.getOpts(args);
		args = go.getArgs();
		boolean splitMode = false;

		if (opts.getBoolOpt("r")) {
			if (opts.getBoolOpt("s"))
				die("-r and -s are incompatible");
			splitMode = false;
		} else if (opts.getBoolOpt("s")) {
			splitMode = true;
		} else {
			die("One of -r or -s must be suppplied");
		}
		
		Collection<Study> studies = new LinkedList<Study> ();
		
		if (args.length > 0) {
			for (String arg : args) {
				Long id = Long.valueOf(arg);
				Study s = me.getStudyHome().findPersistedObjectByID(Study.class, id);
				if (s == null) {
					warn("No study with ID " + arg + "; skipping");
					continue;
				} 
				studies.add(s);
			}
		} else {
			studies = me.getTaxonLabelHome().findAll(Study.class);
		}
		
		warn("Starting");
		
		for (Study s : studies) {
			try {
				if (splitMode) 
					me.splitStudyTaxonLabels(s);
				else
					me.remapStudyTaxonLabels(s);	
			} catch (ConstraintViolationException e) {
				warn("  Exception: SQL state " + e.getSQLState());
				warn("    Constraint violation: " + e.getConstraintName());
			}
		}
	}
	

	/**
	 * <p>Split the taxon labels of Study <var>s</var> away from any other study that might be sharing them.<p>
	 * 
	 * <p>If Study <var>s</var> happens to share any of its taxon labels with other studies, the shared
	 * taxon labels will be copied, and all of the references from <var>s</var> to the shared labels will be 
	 * replaced with references to the fresh copies.</p>
	 * 
	 * @author mjd 20090225
	 * @param s
	 */
	public void splitStudyTaxonLabels(Study s) {
		warn("Remapping study " + s.getId());
	
		// Map taxon labels to their new representatives
		Map<TaxonLabel,TaxonLabel> replacement = new HashMap<TaxonLabel, TaxonLabel> ();

		bindSession();
		getStudyHome().reattachUnmodified(s);
		
		// Map each shared taxon label to a new copy
		for (TaxonLabel tl : findAllTaxonLabels(s)) {
			warn("  Examining taxonLabel " + tl.getId());
			Set<Study> referringStudies = getReferringStudies(tl);
			referringStudies.remove(s);
			{ 
				String studies = referringStudies.size() == 1 ? "study" : "studies";
				if (referringStudies.size() > 0)
					warn("    Found in " + referringStudies.size() + " other " + studies);
			}
			

			// If this taxon label is not shared by some other study, ignore it
			if (referringStudies.isEmpty()) continue;

			{
				TaxonLabel replacementTaxonLabel = new TaxonLabel();
				replacementTaxonLabel.setTaxonLabel(tl.getTaxonLabel());
				replacementTaxonLabel.setStudy(s);
				replacementTaxonLabel.setTaxonVariant(tl.getTaxonVariant());
				getTaxonLabelHome().save(replacementTaxonLabel);
				
				replacement.put(tl, replacementTaxonLabel);
			}	
		}
		
		// Now apply the remapping to the study
		if (replacement.keySet().size() > 0) {
			warn("  Found " + replacement.keySet().size() + " labels to split");
			for (Matrix m : s.getMatrices())
				remapMatrixTaxonLabels(m, replacement);
	
			for (PhyloTree t : s.getTrees()) 
				remapPhyloTreeTaxonLabels(t, replacement);

			for (TaxonLabelSet tls : s.getTaxonLabelSets()) 
				remapTaxonLabelSetTaxonLabels(tls, replacement);

		} else {
			warn("  No remapping necessary.");
		}
	}
	
	/** 
	 * <p>Find all the taxon labels directly or indirectly referred to by this study.</p>
	 * 
	 * <p>Studies refer to taxon labels in several ways.  
	 * The label might be in a matrix row of one of the study's matrices
	 * it might be in a tree node of one of the study's trees,
	 * it might be in one of the study's taxonlabelsets.  This function finds all the labels that
	 * are referred to by the specified study <var>s</var> by one of those paths.</p>
	 * 
	 * @param s - the study of interest
	 * @return the set of taxon labels that are directly or indirectly contained in <var>s</var>
	 * @author mjd 20090225
	 */
	private Set<TaxonLabel> findAllTaxonLabels(Study s) {
		Set<TaxonLabel> result = new HashSet<TaxonLabel> ();
		
//		bindSession();
		result.addAll(s.getTaxonLabels());
		for (PhyloTree t : s.getTrees()) 
			result.addAll(t.getAllTaxonLabels());
		for (Matrix m : s.getMatrices())
			result.addAll(m.getAllTaxonLabels());
		for (TaxonLabelSet tls : s.getTaxonLabelSets())
			result.addAll(tls.getTaxonLabelsReadOnly());
		
		return result;
	}
	
	/**
	 * <p>Find all the studies that directly or indirectly refer to this taxon label.</p>
	 * 
	 * <p>Studies refer to taxon labels in several ways.  
	 * The label might be in a matrix row of one of the study's matrices
	 * it might be in a tree node of one of the study's trees,
	 * it might be in one of the study's taxonlabelsets.  This function finds all the studies that
	 * refer to the specified label <var>tl</var> by one of those paths.</p>
	 * 
	 * 
	 * @param tl - the taxon label of interest
	 * @return the set of studies that directly or indirectly contain <var>tl</var>
	 * @author mjd 20090225
	 */
	private Set<Study> getReferringStudies(TaxonLabel tl) {
		Set<Study> studies = new HashSet<Study> ();
		studies.add(tl.getStudy());
		
		for (PhyloTree t: getReferringTrees(tl))
			studies.add(t.getStudy());
		
		for (Matrix m : getReferringMatrices(tl))
			studies.add(m.getStudy());
		
		for (TaxonLabelSet tls : getReferringTaxonLabelSets(tl))
			studies.add(tls.getStudy());
		
		return studies;
	}

	private Set<TaxonLabelSet> getReferringTaxonLabelSets(TaxonLabel tl) {
		return getTaxonLabelHome().findTaxonLabelSets(tl);
	}
	
	private Set<PhyloTree> getReferringTrees(TaxonLabel tl) {
		Set<PhyloTree> trees = new HashSet<PhyloTree> ();
		
		for (PhyloTreeNode n : getTreeHome().findNodesByTaxonLabel(tl)) {
			trees.add(n.getTree());
		}
		
		return trees;
	}
	
	private Set<Matrix> getReferringMatrices(TaxonLabel tl) {
		Set<Matrix> matrices = new HashSet<Matrix> ();
		
		for (MatrixRow mr : getMatrixRowHome().findByTaxonLabel(tl)) {
			matrices.add(mr.getMatrix());
		}
		
		return matrices;
	}
	
	public void remapStudyTaxonLabels(Study s) {

		bindSession();
		getStudyHome().reattachUnmodified(s);
		warn("Remapping study " + s.getId());
		
		// Map taxon labels to their canonical representatives
		Map<TaxonLabel,TaxonLabel> canonical = new HashMap<TaxonLabel, TaxonLabel> ();

		{
			// map taxon label strings to canonical taxon label objects
			Map<String,TaxonLabel> label = new HashMap<String, TaxonLabel> ();

			for (TaxonLabel tl : getTaxonLabelHome().findByStudy(s)) {
				String labelString = tl.getTaxonLabel();
				if (! label.containsKey(labelString))
					label.put(labelString, tl);
				canonical.put(tl, label.get(labelString));
			}
			
			// Special case: if there are any null references to taxon labels. leave them alone
			canonical.put(null, null); 
		}
		
		{
			int k = canonical.keySet().size();
			Set<TaxonLabel> valueSet = new HashSet<TaxonLabel>(canonical.values());
			int v = valueSet.size();
			if (k == v) {
				warn("  No remapping necessary");
				return;
			} else {
				warn("  " + (k-v) + " labels to delete");
			}
		}
				
		// the canonical Map now contains the mapping from taxon label objects
		// to the canonical taxon label objects we will use
		
		// First let's make sure that the taxon labels point to the right study
		for (TaxonLabel tl : canonical.values()) {
			if (tl != null)
				tl.setStudy(s);
		}

		// Now replace the taxon labels in each tree and matrix with their canonical replacements
		for (AnalyzedData d : s.getAnalyzedData()) {
			Matrix m = d.getMatrixData();
			if (m != null) remapMatrixTaxonLabels(m, canonical);
			
			PhyloTree t = d.getTreeData();
			if (t != null) remapPhyloTreeTaxonLabels(t, canonical);
		}
		
		// Do the same for the taxonlabelsets
		for (TaxonLabelSet tls : s.getTaxonLabelSets()) {
			remapTaxonLabelSetTaxonLabels(tls, canonical);
		}	
		
		// Now remove the non-canonical labels from the old submission
		{
			warn("  Removing labels from submissions");
			for (TaxonLabel tl : canonical.keySet()) {
				if (tl == null) continue;
				TaxonLabel tlCanonical = canonical.get(tl);
				if (tl == tlCanonical) continue;
				
				Submission newLabelSub = tlCanonical.getSubmission();
				Submission oldLabelSub = tl.getSubmission();
				
				// Remove old label from submission
				oldLabelSub.removeTaxonLabel(tl);
				
				// Transfer canonical label to new submission
				if (oldLabelSub.getId() != newLabelSub.getId()) {
					newLabelSub.removeTaxonLabel(tlCanonical);
					oldLabelSub.addTaxonLabel(tlCanonical);
				}
				
				taxonLabelHome.flush();
			}
		}

		destroyUnusedTaxonlabels(canonical);
	}

	private void destroyUnusedTaxonlabels(
			Map<TaxonLabel, TaxonLabel> canonical) {
		if (true) return;  // TODO: Garbage-collect these later.
		int n = 0;
		for (TaxonLabel tl : canonical.keySet()) {
			if (tl == canonical.get(tl)) continue;
			{
				Set<TaxonLabelSet> tlsSet = taxonLabelHome.findTaxonLabelSets(tl);
				for (TaxonLabelSet tls : tlsSet) {
					tls.removePhyloTaxonLabel(tl);
				}
			}
			taxonLabelHome.delete(tl);
			n += 1;
		}
		String taxonlabels = n == 1 ? "taxon label" : "taxon labels";
		warn(n + " " + taxonlabels + " destroyed");
	}

	public void remapTaxonLabelSetTaxonLabels(TaxonLabelSet tls,
			Map<TaxonLabel, TaxonLabel> canonical) {
		warn("  Remapping TaxonLabelSet " + tls.getId());

		boolean doRemap = false;
		
		// list of canonical representatives corresponding to the
		// taxon labels in this taxonlabelset.
		List<TaxonLabel> newTaxonLabels = new LinkedList<TaxonLabel> ();
		for (TaxonLabel tl : tls.getTaxonLabelsReadOnly()) {
			if (canonical.containsKey(tl))  {
				newTaxonLabels.add(canonical.get(tl));
				doRemap = true;
			}
			else
				newTaxonLabels.add(tl);
		}
		// scrub the old list and replace it with the new one
		if (doRemap) {
			tls.clearTaxonLabelList();
			for (TaxonLabel tl : newTaxonLabels) {
				tls.addPhyloTaxonLabel(tl);
			}
		}
	}

	public void remapPhyloTreeTaxonLabels(PhyloTree t,
			Map<TaxonLabel, TaxonLabel> canonical) {
		warn("  Remapping PhyloTree " + t.getId());

		for (PhyloTreeNode tn : t.getTreeNodesReadOnly()) {
			if (tn.getTaxonLabel() == null) continue;
			if (! canonical.containsKey(tn.getTaxonLabel())) continue;
			tn.setTaxonLabel(canonical.get(tn.getTaxonLabel()));
		}
	}
	
	public void remapMatrixTaxonLabels(Matrix matrix,
			Map<TaxonLabel, TaxonLabel> canonical) {
		if (! (matrix instanceof CharacterMatrix)) return;
		warn("  Remapping Matrix " + matrix.getId());

		CharacterMatrix m = (CharacterMatrix) matrix;
		for (MatrixRow mr : m.getRowsReadOnly()) {
			if (canonical.containsKey(mr.getTaxonLabel()))
				mr.setTaxonLabel(canonical.get(mr.getTaxonLabel()));
			for (RowSegment rs : mr.getSegmentsReadOnly()) {
				if (! canonical.containsKey(rs.getTaxonLabel())) continue;
				warn("    Remapping segment " + rs.getId() + " in row " + mr.getId());
				rs.setTaxonLabel(canonical.get(rs.getTaxonLabel()));
			}
		}
	}

	public TaxonLabelHome getTaxonLabelHome() {
		return taxonLabelHome;
	}

	public void setTaxonLabelHome(TaxonLabelHome taxonLabelHome) {
		this.taxonLabelHome = taxonLabelHome;
	}

	public StudyHome getStudyHome() {
		return studyHome;
	}

	public void setStudyHome(StudyHome studyHome) {
		this.studyHome = studyHome;
	}

	public PhyloTreeHome getTreeHome() {
		return treeHome;
	}

	public void setTreeHome(PhyloTreeHome treeHome) {
		this.treeHome = treeHome;
	}

	public MatrixRowHome getMatrixRowHome() {
		return matrixRowHome;
	}

	public void setMatrixRowHome(MatrixRowHome matrixRowHome) {
		this.matrixRowHome = matrixRowHome;
	}
}