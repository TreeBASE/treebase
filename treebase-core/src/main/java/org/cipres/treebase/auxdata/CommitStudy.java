
package org.cipres.treebase.auxdata;

import static org.cipres.treebase.TreebaseUtil.citationMaxLength;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Algorithm;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedMatrix;
import org.cipres.treebase.domain.study.AnalyzedTree;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.BayesianAlgorithm;	
import org.cipres.treebase.domain.study.EvolutionAlgorithm;
import org.cipres.treebase.domain.study.JoiningAlgorithm;
import org.cipres.treebase.domain.study.UPGMAAlgorithm;
import org.cipres.treebase.domain.study.LikelihoodAlgorithm;
import org.cipres.treebase.domain.study.OtherAlgorithm;
import org.cipres.treebase.domain.study.ParsimonyAlgorithm;
import org.cipres.treebase.domain.study.Software;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.util.MergeDuplicateTaxonLabelsInterface;
import org.cipres.treebase.util.RegenerateTaxonLabelSetsInterface;
import org.hibernate.SessionFactory;


public class CommitStudy extends GenericAuxDataAction implements CompleteStudyAction  {
	boolean isForTesting = false; // Whether to mark created submissions as tests
	SessionFactory sessionFactory;
	PrintStream diagnostics = System.err;
	MergeDuplicateTaxonLabelsInterface merge;
	RegenerateTaxonLabelSetsInterface rtls;
	CompleteStudyAction addMetaData;
	
	public CommitStudy() {
		super();
	}

	public CommitStudy(PrintStream diagnostics) {
		super();
		this.diagnostics = diagnostics;
	}
	
	@Override
	public Value perform(Value v) {
		
		if (isDisabled()) {
			warn("Skipping disabled action: CommitStudy");
			return v;
		}
		
		
		AuxData aux = new AuxData((ValueStudy) v);
		if (aux.skip()) {
			warn("Study " + aux.getStudyID() + " marked for skipping; skipping");
			return new ValueNone();
		} else if (! isInterestingAuxData(aux)) 
			return new ValueNone();

		{
			String tb1StudyID = aux.getStudyID();
			
			Collection<Study> oldStudies = ContextManager.getStudyService().findByTBStudyID(tb1StudyID);
			if (oldStudies.size() > 0) {
				warn("Study " + tb1StudyID + " already imported; skipping");
				return new ValueNone();
			}
		}		
		
		Map<String,Matrix> matrices = null;
		Map<String,PhyloTree> trees = null;
		try {
			matrices = aux.getMatrices(ContextManager.getMatrixService()); 
			trees = aux.getTrees(ContextManager.getPhyloTreeService());
		} catch (MultipleMatchError e) {
			// no action
		}
		
		String study_id = aux.getStudyID();
		warn("Processing TB study " + study_id);
		
		if (matrices == null) {
			warn("  One or more matrices belonging to study " + study_id + " is missing from or duplicated in the database; skipping");
			return new ValueNone();
		} else if (trees == null) {
			warn("  One or more trees belonging to study " + study_id + " is missing from or duplicated in the database; skipping");
			return new ValueNone();
		}
		
		{
			warn("  Removing matrices and trees from pseudosubmissions");
//			Transaction t2 = getSessionFactory().getCurrentSession().beginTransaction();
			for (Matrix m : matrices.values()) {
				// Detach this matrix from the pseudosubmission it was temporarily attached to
				Submission pseudoSubmission = ContextManager.getSubmissionService().findByMatrix(m);
				if (pseudoSubmission != null) {
					warn("    Removing matrix " + m.getId());
					pseudoSubmission.removeMatrix(m);
					ContextManager.getSubmissionService().save(pseudoSubmission);
				}
				flush();
			}
			for (PhyloTree t : trees.values()) {
				// Detach this tree from the pseudosubmission it was temporarily attached to
				TreeBlock tb = t.getTreeBlock();
				if (tb != null) {
					warn("    Removing tree " + t.getId());
					tb.removePhyloTree(t);
					ContextManager.getPhyloTreeService().save(tb);
				}
				flush();
			}
//			t2.commit();
		}
		
//		Transaction t = getSessionFactory().getCurrentSession().beginTransaction();

		Study theStudy = aux.study(ContextManager.getStudyService());
		if (theStudy == null) {
			theStudy = new Study();
			ContextManager.getSubmissionService().createSubmission(null, theStudy);
		}
		theStudy.setTB1StudyID(study_id);
		theStudy.setAccessionNumber(study_id);
		theStudy.setStudyStatus(ContextManager.getStudyStatusHome().findStatusPublished());
		ContextManager.getStudyService().save(theStudy);
		
		Submission theSubmission = theStudy.getSubmission();
		ContextManager.getSubmissionService().save(theSubmission);

		Citation theCitation = theStudy.getCitation();
		if (theCitation == null) {
			theCitation = ContextManager.getCitationService().createCitation("TB1");
			theCitation.setStudy(theStudy);
			theStudy.setCitation(theCitation);
			ContextManager.getStudyService().save(theStudy);
		}
		
		{ // Truncate extremely long abstracts
			String theAbstract = aux.getString(null, "abstract");
			if (theAbstract.length() > citationMaxLength - 4) {
				theAbstract = theAbstract.substring(0, citationMaxLength - 4) + "...";
			}
			theCitation.setAbstract(theAbstract);
		}
		// this next isn't quite right, because TB1 "citation" contains title, author, publisher, etc.
		// nothing to be done here, I think.  20080229 MJD
		// We can postprocess the citation data with Perl or something
		// to break it up into its component parts. 20080605 MJD
		// Piel says that Rod Page already did this. 20080702 MJD

		// Rutger has a program in util.CitationDataImporter that should do it 20090220 MJD
		{
			String citation = aux.getString(null, "citation");
			if (citation.length() > 250) {
				// It won't all fit into the "title" field of the database
				citation = citation.substring(0, 250); // XXX
			}
			theCitation.setTitle(citation); // XXX
		}	
		ContextManager.getCitationService().save(theCitation);
	
		// Create or link authors
		for (ValueSection a : aux.getSections("AUTHOR")) {
			Person theAuthor = new Person();
			theAuthor.setFirstName(a.getsval("first_name"));
			theAuthor.setLastName(a.getsval("last_name"));
			theAuthor.setMiddleName("");
//			ContextManager.getPersonService().save(theAuthor);
			// TB1 "author_id" ??
			Person oldAuthor = ContextManager.getPersonService().findByExactMatch(theAuthor);
			if (oldAuthor != null) { 
				theAuthor = oldAuthor; 
			} else {
				ContextManager.getPersonService().createPerson(theAuthor);
				theAuthor = ContextManager.getPersonService().findByExactMatch(theAuthor);
			}

			String oldEmail = theAuthor.getEmailAddressString();
			if (oldEmail == null || oldEmail.equals("")) {
				theAuthor.setEmailAddressString(a.getsval("email"));
			}	

			ContextManager.getPersonService().save(theAuthor);
			theCitation.addAuthor(theAuthor);
		}
		ContextManager.getCitationHome().flush();
		ContextManager.getCitationService().save(theCitation);

		// TODO: submission.setSubmitter
		theSubmission.setTest(isForTesting() ? 0 : 1);

		{
			Date creationDate = aux.getCreationDate();
			if (creationDate != null) {
				theSubmission.setCreateDate(creationDate);
			} else {
				warn("  Couldn't find creation date");
			}
		}

		for (ValueAnalysisSection an : aux.getAnalysisSections()) {
			Analysis theAnalysis = new Analysis();
			theAnalysis.setStudy(theStudy);
			theAnalysis.setName(an.getString("analysis_name"));
			
			// TODO: TB_ANALYSISID

			AnalysisStep theAnalysisStep = new AnalysisStep();
			theAnalysisStep.setAnalysis(theAnalysis);
			theAnalysis.addAnalysisStep(theAnalysisStep);
			ContextManager.getAnalysisService().save(theAnalysis);
			ContextManager.getAnalysisStepService().save(theAnalysisStep);
			
			Algorithm theAlgorithm;
			String tb1Algorithm = an.getString("algorithm");
			if (tb1Algorithm.equalsIgnoreCase("parsimony")) {
				theAlgorithm = new ParsimonyAlgorithm();
			} else if (tb1Algorithm.equalsIgnoreCase("bayesian")) {
				theAlgorithm = new BayesianAlgorithm();
			} else if (tb1Algorithm.equalsIgnoreCase("evolution")) {
				theAlgorithm = new EvolutionAlgorithm();
			} else if (tb1Algorithm.equalsIgnoreCase("joining")) {
				theAlgorithm = new JoiningAlgorithm();
			} else if (tb1Algorithm.equalsIgnoreCase("UPGMA")) {
				theAlgorithm = new UPGMAAlgorithm();
			} else if (tb1Algorithm.equalsIgnoreCase("likelihood")) {
				theAlgorithm = new LikelihoodAlgorithm();
			} else {
				theAlgorithm = new OtherAlgorithm();	
			}
			theAlgorithm.setDescription(tb1Algorithm);
			ContextManager.getAlgorithmHome().save(theAlgorithm); 
			theAnalysisStep.setAlgorithmInfo(theAlgorithm);
			
			{ // handle INPUT_MATRIX and OUTPUT_TREE in each ANALYSIS section
				TreeBlock theTreeBlock = null;
				for (ValueSection ioSection : an.getIOSections()) {
					AnalyzedData theData;
					if (ioSection.label.equals("INPUT_MATRIX")) {
						AnalyzedMatrix theMatrix = new AnalyzedMatrix();
						theMatrix.setInput(true);
						String matrixID = ioSection.getsval("matrix_id");
						
						Matrix matrixData = matrices.get(matrixID);
									
						if (matrixData == null) {
							warn("Matrix " + matrixID + "is used in analysis " + an.getString("analysis_id") + " but is not listed in any MATRIX section for its study");
							return new ValueNone();
						}

						matrixData.setStudy(theStudy);
						ContextManager.getTaxonLabelService().updateStudyForAllLabels(matrixData, theStudy);
						matrixData.setTB1MatrixID(matrixID);
						ContextManager.getMatrixService().save(matrixData);
						theSubmission.addMatrixIfNecessary(matrixData);
						ContextManager.getSubmissionService().save(theSubmission);
						theMatrix.setMatrix(matrixData);
						theData = theMatrix;
					} else if (ioSection.label.equals("OUTPUT_TREE")) {
					
						if (theTreeBlock == null) {
							theTreeBlock = new TreeBlock();
							ContextManager.getStudyService().save(theTreeBlock); // XXX should be some treeblock-specific service
							theSubmission.addPhyloTreeBlock(theTreeBlock);
							ContextManager.getSubmissionService().save(theSubmission);
						}			
						
						AnalyzedTree theTree = new AnalyzedTree();
						theTree.setInput(false);
						String treeID = ioSection.getsval("tree_id");
						PhyloTree treeData = trees.get(treeID);

						treeData.setStudy(theStudy);
						ContextManager.getTaxonLabelService().updateStudyForAllLabels(treeData, theStudy);
						treeData.setTB1TreeID(treeID);
						ContextManager.getPhyloTreeService().save(treeData);
						theTreeBlock.addPhyloTree(treeData);
						theSubmission.addPhyloTreeBlockIfNecessary(theTreeBlock);
						ContextManager.getSubmissionService().save(theSubmission);
						theTree.setTree(treeData);
						theData = theTree;
					} else {
						throw new Error("Unknown IO section '" + ioSection.label + "' in study " + aux.getString(null, "study_id"));
					}
					theData.setAnalysisStep(theAnalysisStep);
					ContextManager.getAnalyzedDataService().save(theData);
					theAnalysisStep.addAnalyzedData(theData);
					ContextManager.getAnalysisStepService().save(theAnalysisStep);
				}
			}
			
			// TODO: fix submission taxa list
							
			Software theSoftware = new Software();
			theSoftware.setName(an.getString("software"));
			ContextManager.getStudyService().save(theSoftware); // XXX should be SoftwareService
			theAnalysisStep.setSoftwareInfo(theSoftware);
					
			theAnalysisStep.setNotes("Imported from TB1");
			ContextManager.getAnalysisStepService().save(theAnalysisStep);
			
			theStudy.addAnalysis(theAnalysis);
			ContextManager.getStudyService().save(theStudy);
		}

		warn("  Commiting the study (" + theStudy.getId() + ") to the database");
//		t.commit(); 
		warn("    Committed");
		
		getAddMetaData().perform(v);
		
		if (false) {
			fixUpTaxa(theStudy);
			fixUpNexusFiles(theStudy);
		}
		
		return v;
	}
	
	private void fixUpNexusFiles(Study theStudy) {
		// Study_nexusfile associations
		// for each matrix and each tree, associate its nexusfile with this study
	}

	private void fixUpTaxa(Study s) {
		getMerge().remapStudyTaxonLabels(s);
		getRtls().createTaxonLabelSets(s);
	}

	@SuppressWarnings("unused")
	private void warn(String warning) {
		diagnostics.println(warning);
		diagnostics.flush();
	}
	
	@SuppressWarnings("unused")
	private void die(String warning) {
		warn("Aborting" + warning);
		System.exit(2);
	}
	
	private void flush() {
		ContextManager.getSubmissionHome().flush();
	}
	

	public void setIsForTesting(boolean ift) {
		isForTesting = ift;
	}
	
	public boolean isForTesting() {
		return isForTesting;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public MergeDuplicateTaxonLabelsInterface getMerge() {
		return merge;
	}

	public void setMerge(MergeDuplicateTaxonLabelsInterface merge) {
		this.merge = merge;
	}

	public RegenerateTaxonLabelSetsInterface getRtls() {
		return rtls;
	}

	public void setRtls(RegenerateTaxonLabelSetsInterface rtls) {
		this.rtls = rtls;
	}

	public CompleteStudyAction getAddMetaData() {
		return addMetaData;
	}

	public void setAddMetaData(CompleteStudyAction addMetaData) {
		this.addMetaData = addMetaData;
	}
}
