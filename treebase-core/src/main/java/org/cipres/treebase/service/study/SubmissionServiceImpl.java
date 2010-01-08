
package org.cipres.treebase.service.study;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.cipres.treebase.dao.jdbc.MatrixJDBC;
import org.cipres.treebase.dao.jdbc.NexusDataSetJDBC;
import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserHome;
import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;
import org.cipres.treebase.domain.matrix.MatrixHome;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.NexusService;
import org.cipres.treebase.domain.study.AnalysisService;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.CitationService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.StudyStatus;
import org.cipres.treebase.domain.study.StudyStatusHome;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.domain.study.UploadFileResult;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.event.ProgressionListener;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * SubmissionServiceImpl.java
 * 
 * Created on April 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class SubmissionServiceImpl extends AbstractServiceImpl implements SubmissionService {
	private static final Logger LOGGER = Logger.getLogger(SubmissionServiceImpl.class);

	private MatrixHome mMatrixHome;
	private MatrixDataTypeHome mMatrixDataTypeHome;
	private PhyloTreeHome mPhyloTreeHome;
	private StudyHome mStudyHome;
	private StudyStatusHome mStudyStatusHome;
	private SubmissionHome mSubmissionHome;
	private TaxonHome mTaxonHome;
	private TaxonLabelHome mTaxonLabelHome;
	private UserHome mUserHome;

	private AnalysisService mAnalysisService;
	private CitationService mCitationService;
	private StudyService mStudyService;
	private NexusService mNexusService;

	/**
	 * Constructor.
	 */
	public SubmissionServiceImpl() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getSubmissionHome();
	}

	/**
	 * Return the StudyService field.
	 * 
	 * @return StudyService mStudyService
	 */
	private StudyService getStudyService() {
		return mStudyService;
	}

	/**
	 * Set the StudyService field.
	 */
	public void setStudyService(StudyService pNewStudyService) {
		mStudyService = pNewStudyService;
	}

	/**
	 * Return the UserHome field.
	 * 
	 * @return UserHome mUserHome
	 */
	private UserHome getUserHome() {
		return mUserHome;
	}

	/**
	 * Set the UserHome field.
	 */
	public void setUserHome(UserHome pNewUserHome) {
		mUserHome = pNewUserHome;
	}

	/**
	 * Return the NexusService field.
	 * 
	 * @return NexusService mNexusService
	 */
	public NexusService getNexusService() {
		return mNexusService;
	}

	/**
	 * Set the NexusService field.
	 */
	public void setNexusService(NexusService pNewNexusService) {
		mNexusService = pNewNexusService;
	}

	/**
	 * Return the Analysis Service field.
	 * 
	 * @return
	 */
	private AnalysisService getAnalysisService() {
		return mAnalysisService;
	}

	/**
	 * Set the AnalysisService field.
	 */
	public void setAnalysisService(AnalysisService pAnalysisService) {
		mAnalysisService = pAnalysisService;
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
	 * Return the TaxonHome field.
	 * 
	 * @return TaxonHome mTaxonHome
	 */
	private TaxonHome getTaxonHome() {
		return mTaxonHome;
	}

	/**
	 * Set the TaxonHome field.
	 */
	public void setTaxonHome(TaxonHome pNewTaxonHome) {
		mTaxonHome = pNewTaxonHome;
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
	 * Return the PhyloTreeHome field.
	 * 
	 * @return PhyloTreeHome mPhyloTreeHome
	 */
	private PhyloTreeHome getPhyloTreeHome() {
		return mPhyloTreeHome;
	}

	/**
	 * Set the PhyloTreeHome field.
	 */
	public void setPhyloTreeHome(PhyloTreeHome pNewPhyloTreeHome) {
		mPhyloTreeHome = pNewPhyloTreeHome;
	}

	/**
	 * Return the MatrixHome field.
	 * 
	 * @return MatrixHome mMatrixHome
	 */
	private MatrixHome getMatrixHome() {
		return mMatrixHome;
	}

	/**
	 * Set the MatrixHome field.
	 */
	public void setMatrixHome(MatrixHome pNewMatrixHome) {
		mMatrixHome = pNewMatrixHome;
	}

	/**
	 * Return the StudyStatusHome field.
	 * 
	 * @return StudyStatusHome mStudyStatusHome
	 */
	private StudyStatusHome getStudyStatusHome() {
		return mStudyStatusHome;
	}

	/**
	 * Set the StudyStatusHome field.
	 */
	public void setStudyStatusHome(StudyStatusHome pNewStudyStatusHome) {
		mStudyStatusHome = pNewStudyStatusHome;
	}

	/**
	 * Return the StudyHome field.
	 * 
	 * @return StudyHome mStudyHome
	 */
	private StudyHome getStudyHome() {
		return mStudyHome;
	}

	/**
	 * Set the StudyHome field.
	 */
	public void setStudyHome(StudyHome pNewStudyHome) {
		mStudyHome = pNewStudyHome;
	}

	/**
	 * Return the SubmissionHome field.
	 * 
	 * @return SubmissionHome mSubmissionHome
	 */
	private SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	/**
	 * Set the SubmissionHome field.
	 */
	public void setSubmissionHome(SubmissionHome pNewSubmissionHome) {
		mSubmissionHome = pNewSubmissionHome;
	}

	/**
	 * Return the MatrixDataTypeHome field.
	 * 
	 * @return MatrixDataTypeHome mMatrixDataTypeHome
	 */
	private MatrixDataTypeHome getMatrixDataTypeHome() {
		return mMatrixDataTypeHome;
	}

	/**
	 * Set the MatrixDataTypeHome field.
	 */
	public void setMatrixDataTypeHome(MatrixDataTypeHome pNewMatrixDataTypeHome) {
		mMatrixDataTypeHome = pNewMatrixDataTypeHome;
	}

	public Submission findSubmissionByID(Long pSubmissionID) {
		Submission sub = getSubmissionHome().findPersistedObjectByID(
			Submission.class,
			pSubmissionID);

		return sub;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#createSubmission(org.cipres.treebase.domain.admin.User,
	 *      org.cipres.treebase.domain.study.Study)
	 */
	public Submission createSubmission(User pSubmitter, Study pStudy) {
		long beginTime = System.currentTimeMillis();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("createSubmission(User) - start"); //$NON-NLS-1$
		}

		Submission sub = new Submission();
		if (pSubmitter != null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" : new submission created"); //$NON-NLS-1$
			}

			// TODO
			// User pSubmitter might be a detached entity and thus
			// will force an LazyInitializationException() when you access
			// lazy loaded collection submissions
			// Solution reattach the User pSubmitter
			// Jin needs to decide whether getUserHome().update(pSubmitter)
			// is more appropriate
			pSubmitter = getUserHome().merge(pSubmitter);
			// end of fix

			pSubmitter.addSubmission(sub);
		}

		// create a new study. Initialize bi-directional relationships.
		StudyStatus initStatus = getStudyStatusHome().findStatusInProgress();

		if (pStudy == null) {
			pStudy = new Study();
		}
		pStudy.setStudyStatus(initStatus);
		pStudy.setLastModifiedDate(new Date());
		sub.setStudy(pStudy);
		pStudy.setSubmission(sub);

		getSubmissionHome().store(sub);
		getSubmissionHome().flush();

		// default submission number to the unique id.
		sub.setSubmissionNumber("" + sub.getId());
		pStudy.setAccessionNumber("" + pStudy.getId());

		// may not necessary: getSubmissionHome().update();

		if (LOGGER.isDebugEnabled()) {
			LOGGER
				.debug("createSubmission(User) - end. Time=" + (System.currentTimeMillis() - beginTime)); //$NON-NLS-1$
		}
		return sub;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#deleteSubmission(org.cipres.treebase.domain.study.Submission)
	 */
	public boolean deleteSubmission(Submission pSubmission) {
		if (pSubmission == null) {
			return false;
		}

		// FIXME: rework the delete logic, make it faster!!

		// first delete all analyzed data before deleting trees:
		Study s = pSubmission.getStudy();

		getAnalysisService().deleteAnalyses(s);

		deleteSubmittedData(pSubmission);

		if (s != null) {
			// getStudyHome().refresh(s);

			// for now only delete in progress study until we have a better
			// security handling:

			/*
			 * if (!s.isInProgress()) { String msg = "This method deletes only in progress study
			 * until a better security is in place."; throw new IllegalArgumentException(msg); }
			 */

			// Cascade delete:
			// * study
			// * matrices,
			// * trees
			// * taxon labels
			// getMatrixHome().delete(new
			// ArrayList<Matrix>(pSubmission.getSubmittedMatricesReadOnly()));
			// getPhyloTreeHome().deleteTreeBlocks(
			// new ArrayList<TreeBlock>(pSubmission.getSubmittedTreeBlocksReadOnly()));
			//
			// pSubmission.clearSubmittedData();
			// // taxon labels need to be deleted last, after everyone referencing to them
			// // are gone
			// getTaxonLabelHome().delete(pSubmission.getSubmittedTaxonLabelsReadOnly());
			//
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
			// getAnalysisService().deleteAnalyses(pStudy.getAnalysesReadOnly());
			// Study specific taxonlabels: handled by submission.delete.
			// all taxonLabelSet
			getStudyHome().deleteAll(s.getTaxonLabelSets());
			s.getTaxonLabelSets().clear();

			// manage bi-directional relationships:
			if (pSubmission.getSubmitter() != null) {
				pSubmission.getSubmitter().removeSubmission(pSubmission);
			}

			getSubmissionHome().deletePersist(pSubmission);

			// need to delete study after deleting the submission.

			// have to after delete all taxonLabel sets:
			getDomainHome().flush();
			getTaxonLabelHome().deleteByStudy(s);

			Citation c = s.getCitation();

			getStudyHome().deletePersist(s);

			// Notes: must delete study first before delete the citation, not-null constraint!
			if (c != null) {

				getCitationService().deleteCitation(c);
			}

		} else {

			// manage bi-directional relationships:
			if (pSubmission.getSubmitter() != null) {
				pSubmission.getSubmitter().removeSubmission(pSubmission);
			}
			getSubmissionHome().deletePersist(pSubmission);
		}

		return true;
	}

	/**
	 * 
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#deleteSubmittedData(org.cipres.treebase.domain.study.Submission)
	 */
	public boolean deleteSubmittedData(Submission pSubmission) {
		if (pSubmission == null) {
			return false;
		}

		// getMatrixHome().delete(new
		// ArrayList<Matrix>(pSubmission.getSubmittedMatricesReadOnly()));
		// getPhyloTreeHome().deleteTreeBlocks(
		// new ArrayList<TreeBlock>(pSubmission.getSubmittedTreeBlocksReadOnly()));
		//
		// pSubmission.clearSubmittedData();
		// // taxon labels need to be deleted last, after everyone referencing to them
		// // are gone
		// getTaxonLabelHome().delete(pSubmission.getSubmittedTaxonLabelsReadOnly());

		// Cascade delete:
		// * matrices,
		// * trees
		// * taxon labels

		Collection<Matrix> matrices = new ArrayList<Matrix>(pSubmission
			.getSubmittedMatricesReadOnly());
		Collection<TreeBlock> treeBlocks = new ArrayList<TreeBlock>(pSubmission
			.getSubmittedTreeBlocksReadOnly());
		Collection<TaxonLabel> taxonLabels = new HashSet<TaxonLabel>(pSubmission
			.getSubmittedTaxonLabelsReadOnly());

		pSubmission.clearSubmittedData();
		// matrices.size();
		// treeBlocks.size();

		deleteData(matrices, treeBlocks, taxonLabels);

		return true;
	}

	/**
	 * 
	 * @param pMatrices
	 * @param pTreeBlocks
	 * @param pTaxonLabels
	 */
	private void deleteData(
		Collection<Matrix> pMatrices,
		Collection<TreeBlock> pTreeBlocks,
		Collection<TaxonLabel> pTaxonLabels) {

		// FIXME: delete matrix should cascade delete elements first!!
		getMatrixHome().delete(pMatrices);
		getMatrixHome().flush();
		// getMatrixHome().clear();

		getPhyloTreeHome().deleteTreeBlocks(pTreeBlocks);
		getMatrixHome().flush();
		// getMatrixHome().clear();

		// taxon labels need to be deleted last, after everyone referencing to them
		// are gone
		// FIXME: don't need this one. ! deletebyStudy()
		getTaxonLabelHome().delete(pTaxonLabels);
	}

	/**
	 * Returns true only if all files exist.
	 * 
	 * @param pNexusFiles
	 * @return
	 */
	private boolean checkFiles(Collection<File> pNexusFiles) {
		if (pNexusFiles == null) {
			return false;
		}

		boolean hasError = false;
		for (File file : pNexusFiles) {
			if (!file.exists()) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info(" input file does not exist:" + file.getAbsolutePath()); //$NON-NLS-1$
				}
				hasError = true;
			}
		}

		return !hasError;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#addNexusFilesJDBC(org.cipres.treebase.domain.study.Submission, java.util.Collection, org.cipres.treebase.event.ProgressionListener)
	 */
	public UploadFileResult addNexusFilesJDBC(
		Submission pSubmission,
		Collection<File> pNexusFiles,
		ProgressionListener pListener) {

		UploadFileResult result = new UploadFileResult();
		if (pSubmission == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("processNexusFiles() - Submission is null"); //$NON-NLS-1$
			}
			return result;
		}

		if (!checkFiles(pNexusFiles)) {
			return result;
		}

		int fileCount = pNexusFiles.size();
		int processedCount = 0;

		long timeStart = System.currentTimeMillis();

		int matrixCount = 0;
		int treeCount = 0;
		
		// Process one file at a time:
		for (File file : pNexusFiles) {

			long timeFile = System.currentTimeMillis();

			NexusDataSet dataSet = null;
			try {
				// first start a transaction, use Hibernate to add and persist most of the data
				dataSet = addNexusFile(pSubmission, file);
				NexusDataSetJDBC dataJDBC = dataSet.getDataJDBC();

				// Next use direct JDBC to batch insert data, for performance reason.
				// getDomainHome().refreshAll(dataJDBC.getAllMatrices());

				// TODO: create a new method:
				Connection conn = getSubmissionHome().getConnection();
				for (MatrixJDBC matrixJDBC : dataJDBC.getMatrixJDBCList()) {
					matrixJDBC.batchInsertColumn(conn);
					matrixJDBC.prepElementBatchInsert(conn);
					matrixJDBC.processMatrixElements(conn);
					// MesquiteMatrixConverter converter = matrixJDBC.getMesqMatrixConverter();
					// converter.processMatrixElements(matrixJDBC, conn);

					matrixJDBC.batchInsertCompoundElements(conn);
					matrixCount ++;
				}

				treeCount += dataSet.getTotalTreeCount();

				// ALERT: Do not use. will get connection closed exception!
				// try {
				// conn.close();
				// } catch (SQLException ex) {
				// throw new CleanupFailureDataAccessException("Failed to close connection.", ex);
				// }
			} finally {
				// Dispose the mesquite project after all usage:
				if (dataSet != null && dataSet.getMesqProject()!=null) dataSet.getMesqProject().dispose();
			}
			
			for (Matrix m : dataSet.getMatrices()) {
				getMatrixHome().refresh(m);
				m.setDimensions();
				getMatrixHome().merge(m);
			}

			// TODO: if pListener is not null, launch a thread to do the parsing.
			processedCount++;
			if (pListener != null) {
				pListener.updateProgress(processedCount, fileCount);
			}

			if (LOGGER.isDebugEnabled()) {
				long timeFileEnd = System.currentTimeMillis();
				LOGGER.debug("one file time=" + (timeFileEnd - timeFile) / 1000); //$NON-NLS-1$
			}

		}

		// Third start a new transaction, use Hibernate to store the original nexus files.
		Study study = pSubmission.getStudy();
		getStudyService().addNexusFiles(study, pNexusFiles);
		
		if (LOGGER.isDebugEnabled()) {
			long timeEnd = System.currentTimeMillis();
			LOGGER.debug("total time=" + (timeEnd - timeStart) / 1000); //$NON-NLS-1$
			LOGGER.debug("total matrix=" + matrixCount + " treeCount =" + treeCount); //$NON-NLS-1$
		}
		
		result.setMatrixCount(matrixCount);
		result.setTreeCount(treeCount);
		return result;
	}

	/**
	 * 
	 */
	private NexusDataSet addNexusFile(Submission pSubmission, File pNexusFile) {

		// better to refresh:
		Submission sub = update(pSubmission);

		Study study = sub.getStudy();

		NexusDataSet data = getNexusService().parseNexus(study, pNexusFile);

		if (data != null) {

			long timeStart = System.currentTimeMillis();

			// need to store the taxon labels, don't ask why...
			Collection<TaxonLabel> labels = data.getAllTaxonLabels();
			getTaxonLabelHome().storeAll(labels);
			getTaxonLabelHome().storeAll(data.getTaxonLabelSets());
			sub.addTaxonLabels(labels);
			getTaxonHome().flush();

			sub.getStudy().getTaxonLabelSets().addAll(data.getTaxonLabelSets());

			// Store matrice:
			getMatrixHome().persistAll(data.getMatrices());
			sub.addMatrices(data.getMatrices());
			getMatrixHome().flush();

			// need to store the trees, don't ask why...
			// List<PhyloTree> alltrees = new ArrayList<PhyloTree>();
			// for (TreeBlock block : data.getTreeBlocks()) {
			// alltrees.addAll(block.getTreeList());
			// }
			// getPhyloTreeHome().storeAll(alltrees);
			getPhyloTreeHome().storeAll(data.getTreeBlocks());
			sub.addPhyloTreeBlocks(data.getTreeBlocks());
			getPhyloTreeHome().flush();
			update(sub);

			if (LOGGER.isDebugEnabled()) {
				long timeEnd = System.currentTimeMillis();
				LOGGER.debug("add nexus time=" + (timeEnd - timeStart) / 1000); //$NON-NLS-1$
			}
		}

		return data;
	}

	/**
	 * Replaced by addNexusFilesJDBC
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#addNexusFiles(org.cipres.treebase.domain.study.Submission,
	 *      java.util.Collection, org.cipres.treebase.event.ProgressionListener)
	 */
	@Deprecated
	public NexusDataSetJDBC addNexusFiles(
		Submission pSubmission,
		Collection<File> pNexusFiles,
		ProgressionListener pListener) {

		if (pSubmission == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("addNexusFiles() - Submission is null"); //$NON-NLS-1$
			}
			return null;
		}

		// better to refresh:
		Submission sub = update(pSubmission);

		Study study = sub.getStudy();

		NexusDataSet data = getNexusService().parseNexus(study, pNexusFiles, pListener);
		NexusDataSetJDBC dataJDBC = null;

		if (data != null) {

			long timeStart = System.currentTimeMillis();

			// need to store the taxon labels, don't ask why...
			Collection<TaxonLabel> labels = data.getAllTaxonLabels();
			getTaxonLabelHome().storeAll(labels);
			getTaxonLabelHome().storeAll(data.getTaxonLabelSets());
			sub.addTaxonLabels(labels);
			getTaxonHome().flush();

			sub.getStudy().getTaxonLabelSets().addAll(data.getTaxonLabelSets());

			// Store matrice:
			getMatrixHome().persistAll(data.getMatrices());
			sub.addMatrices(data.getMatrices());

			// need to store the trees, don't ask why...
			// List<PhyloTree> alltrees = new ArrayList<PhyloTree>();
			// for (TreeBlock block : data.getTreeBlocks()) {
			// alltrees.addAll(block.getTreeList());
			// }
			// getPhyloTreeHome().storeAll(alltrees);
			getPhyloTreeHome().storeAll(data.getTreeBlocks());

			sub.addPhyloTreeBlocks(data.getTreeBlocks());

			update(sub);

			if (LOGGER.isDebugEnabled()) {
				long timeEnd = System.currentTimeMillis();
				LOGGER.debug("add nexus time=" + (timeEnd - timeStart) / 1000); //$NON-NLS-1$
			}

			dataJDBC = data.getDataJDBC();
		}

		return dataJDBC;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#getPermission(java.lang.String,
	 *      java.lang.Long)
	 */
	public TBPermission getPermission(String pUsername, Long pSubmissionID) {
		Submission sub = findSubmissionByID(pSubmissionID);
		User user = getUserHome().findByUserName(pUsername);

		if (sub == null) {
			return TBPermission.NONE;
		}

		return sub.getPermission(user);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#updateStudyStatusPublish(java.lang.Long)
	 */
	public void updateStudyStatusPublish(Long pSubmissionID) {
		Submission sub = findSubmissionByID(pSubmissionID);

		if (sub == null) {
			throw new IllegalArgumentException(
				"Failed to request publishing a study. The submission is not found:"
					+ pSubmissionID);
		}

		Study study = sub.getStudy();

		if (study == null) {
			throw new IllegalArgumentException(
				"Failed to request publishing a study. The study is not found for submission id:"
					+ pSubmissionID);
		}

		if (study.isInProgress()) {
			// show err
			throw new IllegalArgumentException(
				"Failed to publish a study. study is not ready for publishing for submission id:"
					+ pSubmissionID);
		}

		if (study.isReady()) {
			StudyStatus status = getStudyStatusHome().findStatusPublished();
			study.setStudyStatus(status);
			Date today = new Date();
			study.setReleaseDate(today);
			study.setLastModifiedDate(today);
		}

		updatePublishedFlag(study, true);
	}

	/**
	 * This method to be called by all updateStudyStatusxxx methods to update following: change
	 * published flags in: Citation, Matrix, PhyloTree
	 * 
	 * @param pStudy
	 * @param pPublished
	 */
	private void updatePublishedFlag(Study pStudy, boolean pPublished) {
		Citation citation = pStudy.getCitation();
		// study might not yet have a citation associated with it.
		if ( citation != null ) {
			citation.setPublished(pPublished);
		}
		getPhyloTreeHome().updatePublishedFlagByStudy(pStudy, pPublished);
		getMatrixHome().updatePublishedFlagByStudy(pStudy, pPublished);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#updateStudyStatusInProgress(java.lang.Long)
	 */
	public void updateStudyStatusInProgress(Long pSubmissionID) {
		Submission sub = findSubmissionByID(pSubmissionID);
		if (sub == null) {
			// show err
			throw new IllegalArgumentException(
				"Failed to make a study in progress. The submission is not found:" + pSubmissionID);
		}

		Study study = sub.getStudy();
		if (study == null) {
			throw new IllegalArgumentException(
				"Failed to make a study in progress. The study is not found for submission id:"
					+ pSubmissionID);
		}

		if (!study.isInProgress()) {
			StudyStatus status = getStudyStatusHome().findStatusInProgress();
			study.setStudyStatus(status);
			study.setLastModifiedDate(new Date());
			
			updatePublishedFlag(study, false);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#updateStudyStatusReady(java.lang.Long)
	 */
	public void updateStudyStatusReady(Long pSubmissionID) {
		Submission sub = findSubmissionByID(pSubmissionID);
		if (sub == null) {
			// show err
			throw new IllegalArgumentException(
				"Failed to request making a study ready. The submission is not found:"
					+ pSubmissionID);
		}

		Study study = sub.getStudy();

		if (study == null) {
			throw new IllegalArgumentException(
				"Failed to request making a study ready. The study is not found for submission id:"
					+ pSubmissionID);
		}

		if (!study.isReady()) {
			StudyStatus readyStatus = getStudyStatusHome().findStatusReady();
			study.setStudyStatus(readyStatus);
			study.setLastModifiedDate(new Date());
		}

	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#deleteSubmission(java.lang.Long)
	 */
	public boolean deleteSubmission(Long pSubId) {
		Submission sub = findSubmissionByID(pSubId);

		return deleteSubmission(sub);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#findReadySubmissions()
	 */
	public Collection<Submission> findReadySubmissions() {
		return getSubmissionHome().findByReadyState();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#findReadySubmissions()
	 */
	public Collection<Submission> findPublishedSubmissions() {
		return getSubmissionHome().findByPublishedState();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#findReadySubmissions()
	 */
	public Collection<Submission> findInProgressSubmissions() {
		return getSubmissionHome().findByInProgressState();
	}

	public Submission findByMatrix(Matrix pMatrix) {
		return getSubmissionHome().findByMatrix(pMatrix);
	}

	
	public Collection<Submission> findSubmissionByCreateDateRange(Date from, Date until) {
		

		return getSubmissionHome().findByCreateDateRange(from, until);
	}

	
	
	@Override
	public Class defaultResultClass() {
		return Submission.class;
	}
	
}
