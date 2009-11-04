
package org.cipres.treebase.util;

import java.sql.Clob;
import java.util.Collection;
import java.util.Map;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.hibernate.Transaction;

/**
 * Make sure a study's trees and matrices are properly attached to the study.
 * 
 * <p>This command-line utility scans every study in the database.
 * For each study, it drills down through its analyses and analysis steps
 * to find the analyzed data, the trees and matrices.</p>
 * 
 * <p>Each data item has its study set to the top-level study.
 * Additionally, each matrix is removed from whatever submission it is in
 * and is added to the top-level study's submission if it is not already there,
 * and similarly each tree's treeblock is moved to the study's submission.</p>
 * 
 * <p>An optional command-line argument, <code>minStudyId</code>, tells the program to
 * skip all the studies whose <code>study_id</code> is less than the specified value.</p>
 * 
 * <p>Note that this program does <i>not</i> similarly repatriate the taxonLabels attached to
 * the trees or matrices; use the RepatriateTaxonLabels program for that.</p>
 *  
 * @author mjd 20080729
 * @see RepatriateTaxonLabels
 */
class RepatriateData extends AbstractStandalone {
	
	public static void main(String [] args) {
		RepatriateData rt = new RepatriateData();
		rt.setupContext();
		Long minStudyId = 0L; // Skip studies with ID numbers less than this
		SubmissionHome sh = ContextManager.getSubmissionHome();
		
		if (args.length > 0) {
			minStudyId = Long.parseLong(args[0]);
		}
		
		for (TBPersistable tbS : ContextManager.getStudyHome().findAll(Study.class)) {
			Study s = (Study) tbS;
			if (s.getId() < minStudyId) { continue; }
			Submission sub = s.getSubmission();
			Collection<TreeBlock> tbs = sub.getSubmittedTreeBlocksReadOnly();
			Collection<Matrix> ms = sub.getSubmittedMatricesReadOnly();
			System.err.println("Study " + s.getId() + ": ");
			for (Analysis an : s.getAnalyses()) {
				for (AnalysisStep as : an.getAnalysisStepsReadOnly()) {
					Transaction tr = null;

					for (AnalyzedData ad : as.getDataSetReadOnly()) {
						if (tr == null) {
							tr = rt.beginTransaction();
						}
						Matrix m = ad.getMatrixData();
						PhyloTree t = ad.getTreeData();
						if (m != null) {
							System.err.println("  Repatriating matrix " + m.getId()
									+ " to study " + s.getId() + " submission " + sub.getId());
							
							if (m.getStudy() != s) { 
								String nexusFileName = m.getNexusFileName();
								Map<String,Clob> nexusMap = m.getStudy().getNexusFiles();
								Clob nexusFile = nexusMap.remove(nexusFileName);
								s.getNexusFiles().put(nexusFileName, nexusFile);
								m.setStudy(s); 
							}
							
							if (! ms.contains(m)) {
								Submission oldSub = sh.findByMatrix(m);
								if (oldSub != null) {
									oldSub.removeMatrix(m);
									sh.flush();
								}
								sub.addMatrix(m);
							}
						} else if (t != null) {
							System.err.println("  Repatriating tree " + t.getId()
									+ " to study " + s.getId() + " submission " + sub.getId());
							if (t.getStudy() != s) { 
								String nexusFileName = t.getNexusFileName();
								Map<String,Clob> nexusMap = t.getStudy().getNexusFiles();
								Clob nexusFile = nexusMap.remove(nexusFileName);
								s.getNexusFiles().put(nexusFileName, nexusFile);
								t.setStudy(s); 
							}
							TreeBlock tb = t.getTreeBlock();
							if (tb == null) {
								System.err.println("  No tree block!");
							} else if (! tbs.contains(tb)) {
								Submission oldSub = sh.findByTreeBlock(tb);
								if (oldSub != null) {
									oldSub.removePhyloTreeBlock(tb);
									sh.flush();
								}
								sub.addPhyloTreeBlock(tb);
							}
						}
					}

					if (tr != null) {
						tr.commit();
					}
				}
			}
		}
	}
}
