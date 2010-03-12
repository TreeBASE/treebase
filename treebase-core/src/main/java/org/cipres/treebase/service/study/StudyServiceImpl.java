
package org.cipres.treebase.service.study;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Clob;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixHome;
import org.cipres.treebase.domain.study.AnalysisService;
import org.cipres.treebase.domain.study.CitationService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyCriteria;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.StudyStatusHome;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * StudyServiceImpl.java
 * 
 * Created on May 2, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class StudyServiceImpl extends AbstractServiceImpl implements StudyService {
	private static final Logger LOGGER = Logger.getLogger(StudyServiceImpl.class);

	private AnalysisService mAnalysisService;
	private CitationService mCitationService;

	private StudyStatusHome mStudyStatusHome;
	private SubmissionHome mSubmissionHome;
	private StudyHome mStudyHome;
	private TaxonLabelHome mTaxonLabelHome;
	private PhyloTreeHome mPhyloTreeHome;
	private MatrixHome mMatrixHome;

	// private TaxonHome mTaxonHome;

	/**
	 * @return the matrixHome
	 */
	private MatrixHome getMatrixHome() {
		return mMatrixHome;
	}

	/**
	 * @param pMatrixHome the matrixHome to set
	 */
	public void setMatrixHome(MatrixHome pMatrixHome) {
		mMatrixHome = pMatrixHome;
	}

	/**
	 * @return the phyloTreeHome
	 */
	private PhyloTreeHome getPhyloTreeHome() {
		return mPhyloTreeHome;
	}

	/**
	 * @param pPhyloTreeHome the phyloTreeHome to set
	 */
	public void setPhyloTreeHome(PhyloTreeHome pPhyloTreeHome) {
		mPhyloTreeHome = pPhyloTreeHome;
	}

	/**
	 * Constructor.
	 */
	public StudyServiceImpl() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getStudyHome();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyService#findBySubmissionNumber(java.lang.String)
	 */
	public Study findBySubmissionNumber(String pSubmissionNumber) {
		Submission sub = getSubmissionHome().findBySubmissionNumber(pSubmissionNumber);
		if (sub != null) {
			return sub.getStudy();
		}
		return null;
	}

	/**
	 * Return the CitationService field.
	 * 
	 * @return CitationService mCitationService
	 */
	private CitationService getCitationService() {
		return mCitationService;
	}

	/**
	 * Set the CitationService field.
	 */
	public void setCitationService(CitationService pNewCitationService) {
		mCitationService = pNewCitationService;
	}

	/**
	 * @return Returns the studyHome.
	 */
	private StudyHome getStudyHome() {
		return mStudyHome;
	}

	/**
	 * @param pStudyHome The studyHome to set.
	 */
	public void setStudyHome(StudyHome pStudyHome) {
		mStudyHome = pStudyHome;
	}

	/**
	 * @return Returns the studyStatusHome.
	 */
	private StudyStatusHome getStudyStatusHome() {
		return mStudyStatusHome;
	}

	/**
	 * @param pStudyStatusHome The studyStatusHome to set.
	 */
	public void setStudyStatusHome(StudyStatusHome pStudyStatusHome) {
		mStudyStatusHome = pStudyStatusHome;
	}

	/**
	 * @return Returns the submissionHome.
	 */
	private SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	/**
	 * @param pSubmissionHome The submissionHome to set.
	 */
	public void setSubmissionHome(SubmissionHome pSubmissionHome) {
		mSubmissionHome = pSubmissionHome;
	}

	/**
	 * Return the TaxonLabelHome field.
	 * 
	 * @return TaxonLabelHome mTaxonLabelHome
	 */
	private TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	/**
	 * Set the TaxonLabelHome field.
	 */
	public void setTaxonLabelHome(TaxonLabelHome pNewTaxonLabelHome) {
		mTaxonLabelHome = pNewTaxonLabelHome;
	}

	/**
	 * Return the AnalysisService field.
	 * 
	 * @return AnalysisService mAnalysisService
	 */
	private AnalysisService getAnalysisService() {
		return mAnalysisService;
	}

	/**
	 * Set the AnalysisService field.
	 */
	public void setAnalysisService(AnalysisService pNewAnalysisService) {
		mAnalysisService = pNewAnalysisService;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyService#findBySubmissionID(java.lang.Long)
	 */
	public Study findBySubmissionID(Long pSubmissionID) {
		Submission sub = getSubmissionHome().findPersistedObjectByID(
			Submission.class,
			pSubmissionID);

		if (sub != null) {
			return sub.getStudy();
		}
		return null;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyService#findByName(java.lang.String, boolean)
	 */
	public Collection<Study> findByName(String pStudyName, boolean pCaseSensitive) {
		return getStudyHome().findByName(pStudyName, pCaseSensitive);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyService#deleteStudy(org.cipres.treebase.domain.study.Study)
	 */
	public boolean deleteStudy(Study pStudy) {
		if (pStudy == null) {
			return false;
		}

		// for now only delete in progress study until we have a better
		// security handling:
		if (!pStudy.isInProgress()) {
			String msg = "This method deletes only in progress study until a better security is in place.";
			throw new IllegalArgumentException(msg);
		}

		// manage bi-directional relationships:
		// pStudy.getSubmitter().removeSubmission(pSubmission);

		// Note: ---important: the delete order is important---
		// Cascade delete
		// all the analyses
		// submission
		// all taxonLabelSet
		// all study specific taxonlabels
		// citation

		// getAnalysisService().deleteAnalyses(pStudy.getAnalyses());

		// FIXME getAnalysisService().deleteAnalyses(pStudy.getAnalysesReadOnly());

		Submission sub = pStudy.getSubmission();
		if (sub != null) {

			// Important: to avoid looping:
			sub.setStudy(null);

			// delete submission, with matrices, trees, and taxonlabels.
			getSubmissionHome().deletePersist(sub);

			pStudy.setSubmission(null);
		}

		getTaxonLabelHome().deleteByStudy(pStudy);

		// Notes: must delete study first before delete the citation, not-null constraint!
		getStudyHome().deletePersist(pStudy);

		if (pStudy.getCitation() != null) {

			getCitationService().deleteCitation(pStudy.getCitation());

			pStudy.setCitation(null);
		}

		// Study specific taxonlabels: handled by submission.delete.
		// all taxonLabelSet
		getStudyHome().deleteAll(pStudy.getTaxonLabelSets());

		return true;
	}

	public boolean deleteStudy(Long studyID) {
		Study s = findByID(studyID);
		if (s == null) return false;
		else return deleteStudy(s);
	}
	
	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyService#findByCriteria(org.cipres.treebase.domain.study.StudyCriteria)
	 */
	public Collection<Study> findByCriteria(StudyCriteria pCriteria) {

		// //: hardcode study search results
		// Study s1 = (Study) getStudyHome().findPersistedObjectByID(Study.class, new Long(851));
		// Study s2 = (Study) getStudyHome().findPersistedObjectByID(Study.class, new Long(848));
		// Study s3 = (Study) getStudyHome().findPersistedObjectByID(Study.class, new Long(878));
		// Study s4 = (Study) getStudyHome().findPersistedObjectByID(Study.class, new Long(817));
		// Study s5 = (Study) getStudyHome().findPersistedObjectByID(Study.class, new Long(799));
		// Study s6 = (Study) getStudyHome().findPersistedObjectByID(Study.class, new Long(754));
		// Study s7 = (Study) getStudyHome().findPersistedObjectByID(Study.class, new Long(736));
		//
		// List<Study> result = new ArrayList<Study>();
		// result.add(s1);
		// result.add(s2);
		// result.add(s3);
		// result.add(s4);
		// result.add(s5);
		// result.add(s6);
		// result.add(s7);
		//
		// return result;
		//
		return getStudyHome().findByCriteria(pCriteria);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyService#findByID(java.lang.Long)
	 */
	public Study findByID(Long pStudyID) {
		if (pStudyID == null) {
			return null;
		}
		return getStudyHome().findPersistedObjectByID(Study.class, pStudyID);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyService#addNexusFiles(org.cipres.treebase.domain.study.Study,
	 *      java.util.Collection)
	 */
	public void addNexusFiles(Study pStudy, Collection<File> pNexusFiles) {
		// Use this method to open a tx.

		if (pStudy == null || pNexusFiles == null || pNexusFiles.isEmpty()) {
			return;
		}

		// better to refresh:
		Study s = update(pStudy);

		Iterator<File> fileIter = pNexusFiles.iterator();
		while (fileIter.hasNext()) {
			File element = (File) fileIter.next();
			String fileName = element.getName();			    												
			s.addNexusFile(fileName, TreebaseUtil.readFileToString(element));
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyService#getPermission(java.lang.String,
	 *      java.lang.Long)
	 */
	public TBPermission getPermission(String pUsername, Long pStudyId) {

		return null;
		// Study study = findByID(pStudyId);
		//		
		// if (study == null) {
		// return TBPermission.NONE;
		// }
		//		
		// Submission sub = study.getSubmission();
		// if (sub == null) {
		// return TBPermission.NONE;
		// }
		//		
		// return getSubmissionService().getPermission(pUsername, sub.getId());
	}

	public void cleanCache() {
		getStudyHome().cleanCache();
	}

	public Collection<Study> findByKeyword(String keyword) {
		return getStudyHome().findByKeyword(keyword);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyService#generateXMLStringByKeyword(java.lang.String)
	 */
	public String generateXMLStringByKeyword(String pKeyword) {

		Collection<Study> studyCollection = getStudyHome().findByKeyword(pKeyword);

		if (studyCollection.size() == 0) {
			return "No Studies found containing keyword: '" + pKeyword + "'";
		} else {
			Element parent = new Element("studies");
			parent.setAttribute("keyword", pKeyword);

			for (Study aStudy : studyCollection) {
				parent.addContent(aStudy.generateXMLComponents());
			}
			Format format = Format.getRawFormat();
			format.setIndent("  ");
			format.setLineSeparator("\n");
			XMLOutputter xmloutput = new XMLOutputter(format);

			return xmloutput.outputString(parent);
		}
	}

	public Collection<Study> findByAuthor(String authorName) {
		return getStudyHome().findByAuthor(authorName);
	}

	public Collection<Study> findByTitle(String title) {
		return getStudyHome().findByTitle(title);
	}

	public Collection<Study> findByAbstract(String abstractWord) {
		return getStudyHome().findByAbstract(abstractWord);
	}

	public Collection<Study> findByCitation(String citationWord) {
		return getStudyHome().findByCitation(citationWord);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyService#generateReconstructedNexusFile(java.lang.Long,
	 *      java.lang.String)
	 */
	public String generateReconstructedNexusFile(Long pSubmissionId, String pFileName) {
		if (TreebaseUtil.isEmpty(pFileName)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		
		Study study = findBySubmissionID(pSubmissionId);
		TreebaseUtil.attachStudyHeader(study, sb);

		// first: get the list of matrices and tree blocks:
		Collection<Matrix> matrices = getMatrixHome().findByNexusFileName(pSubmissionId, pFileName);
		Collection<TreeBlock> treeBlocks = getPhyloTreeHome().findTreeBlocksByNexusFileName(
			pSubmissionId,
			pFileName);

		// assemble the nexus:
		
		//taxa:
		//get all taxa and print them:
		Set<TaxonLabelSet> alltlSets = new LinkedHashSet<TaxonLabelSet>();
		for (Matrix amatrix : matrices) {
			if (amatrix.getTaxa() != null) {
				alltlSets.add(amatrix.getTaxa());
			}
		}
		
		for (TreeBlock atreeblock : treeBlocks) {
			if (atreeblock.getTaxonLabelSet() != null) {
				alltlSets.add(atreeblock.getTaxonLabelSet());
			}
		}

		for (TaxonLabelSet taxonLabelSet : alltlSets) {
			taxonLabelSet.buildNexusBlockTaxa(sb, false, false);
		}

//		for (TreeBlock atreeblock : treeBlocks) {
//			atreeblock.getNexusTaxaOnlyForBlock(sb);
//		}

		//matrices:
		for (Matrix amatrix : matrices) {
			amatrix.generateNexusBlock(sb);
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Number of treeblocks: " + treeBlocks.size());
		}

		// trees:
		for (TreeBlock atreeblock : treeBlocks) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Tree Block id: " + atreeblock.getId());
			}
			atreeblock.generateAFileDynamically(sb);
		}

		return sb.toString();
	}

	public Collection<Study> findByTBStudyID(String studyID) {
		return getStudyHome().findByTB1StudyID(studyID);
	}

	public void setTBStudyID(Study theStudy, String studyID) {
		getStudyHome().setTBStudyID(theStudy, studyID);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.study.StudyService#findByTrees(java.util.Set)
	 */
	public Collection<Study> findByTrees(Set<PhyloTree> trees) {
		return getStudyHome().findByTrees(trees);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.study.StudyService#findByTaxonLabelName(java.lang.String)
	 */
	public Collection<Study> findByTaxonLabelName(String taxonLabel) {
		return getStudyHome().findByTaxonLabelName(taxonLabel);
	}

	public String getIDPrefix() {
		return "S";
	}

	@Override
	public Class defaultResultClass() {
		return Study.class;
	}

	public Collection<Study> findByJournal(String pJournal,
			boolean pCaseSensitive) {
		return getStudyHome().findByJournal(pJournal);
	}
}
