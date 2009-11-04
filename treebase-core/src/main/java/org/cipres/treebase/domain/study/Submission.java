
package org.cipres.treebase.domain.study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Type;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;

/**
 * Submission.java
 * 
 * Created on Feb 15, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "SUBMISSION")
@AttributeOverride(name = "id", column = @Column(name = "SUBMISSION_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
@BatchSize(size = 5)
public class Submission extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;
	private Integer mTest;

	private String mSubmissionNumber;

	private User mSubmitter;
	private Study mStudy;
	private Date mCreateDate = new Date();

	private Collection<Matrix> mSubmittedMatrices = new ArrayList<Matrix>();
	private Collection<TreeBlock> mSubmittedTreeBlocks = new ArrayList<TreeBlock>();
	private Set<TaxonLabel> mSubmittedTaxonLabels = new HashSet<TaxonLabel>();

	/**
	 * Constructor.
	 */
	public Submission() {
		super();
	}
	
	/**
	 * Return the Test field.
	 * 
	 * @return Integer
	 */
	@Column(name = "Test")
	public Integer getTest() {
		if ( mTest == null ) {
			return new Integer(1);
		}
		else {
			return mTest;
		}
	}
	
	/**
	 * Set the Test field.
	 * 
	 * @param pNewTest
	 */
	public void setTest(Integer pNewTest) {
		mTest = pNewTest;
	}

	/**
	 * Return the SubmissionNumber field.
	 * 
	 * @return String
	 */
	@Column(name = "SubmissionNumber", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getSubmissionNumber() {
		return mSubmissionNumber;
	}

	/**
	 * Set the SubmissionNumber field.
	 */
	public void setSubmissionNumber(String pNewSubmissionNumber) {
		mSubmissionNumber = pNewSubmissionNumber;
	}

	/**
	 * Submission create date.
	 * 
	 * @return Date
	 */
	@Column(name = "CreateDate")
	@Temporal(TemporalType.DATE)
	public Date getCreateDate() {
		return mCreateDate;
	}

	/**
	 * Set the CreateDate field.
	 */
	public void setCreateDate(Date pNewCreateDate) {
		mCreateDate = pNewCreateDate;
	}

	/**
	 * Return the Submitter field.
	 * 
	 * @return User
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "USER_ID")
	public User getSubmitter() {
		return mSubmitter;
	}

	/**
	 * Set the Submitter field.
	 */
	public void setSubmitter(User pNewSubmitter) {
		mSubmitter = pNewSubmitter;
	}

	/**
	 * Return the Study field.
	 * 
	 * @return Study
	 */
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return mStudy;
	}

	/**
	 * Set the Study field.
	 */
	public void setStudy(Study pNewStudy) {
		mStudy = pNewStudy;
	}

	/**
	 * Return the SubmittedTaxa field.
	 * 
	 * @return Set<Taxon>
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	// @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "SUB_TAXONLABEL", joinColumns = {@JoinColumn(name = "SUBMISSION_ID")}, inverseJoinColumns = @JoinColumn(name = "TAXONLABEL_ID"))
	// @IndexColumn(name = "TAXONLABEL_ORDER")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
	@BatchSize(size=20)
	protected Set<TaxonLabel> getSubmittedTaxonLabels() {
		return mSubmittedTaxonLabels;
	}

	/**
	 * Set the SubmittedTaxa field.
	 */
	protected void setSubmittedTaxonLabels(Set<TaxonLabel> pNewSubmittedTaxonLabels) {
		mSubmittedTaxonLabels = pNewSubmittedTaxonLabels;
	}

	/**
	 * Return the SubmittedTrees field.
	 * 
	 * @return List<PhyloTree> mSubmittedTrees
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "SUB_TREEBLOCK", joinColumns = {@JoinColumn(name = "SUBMISSION_ID")}, inverseJoinColumns = @JoinColumn(name = "TREEBLOCK_ID"))
	// @IndexColumn(name = "TREE_ORDER")
	@CollectionId(columns = @Column(name = "COLLECTION_ID", nullable = false), type = @Type(type = "long"), generator = "sequence")
	// @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
	protected Collection<TreeBlock> getSubmittedTreeBlocks() {
		return mSubmittedTreeBlocks;
	}

	/**
	 * Set the SubmittedTreeBlocks field.
	 */
	protected void setSubmittedTreeBlocks(Collection<TreeBlock> pNewSubmittedTreeBlocks) {
		mSubmittedTreeBlocks = pNewSubmittedTreeBlocks;
	}

	/**
	 * Return the SubmittedMatrices field.
	 * 
	 * @return List<Matrix> mSubmittedMatrices
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "SUB_MATRIX", joinColumns = {@JoinColumn(name = "SUBMISSION_ID")}, inverseJoinColumns = @JoinColumn(name = "MATRIX_ID"))
	// @IndexColumn(name = "MATRIX_ORDER")
	@CollectionId(columns = @Column(name = "COLLECTION_ID", nullable = false), type = @Type(type = "long"), generator = "sequence")
	// @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
	protected Collection<Matrix> getSubmittedMatrices() {
		return mSubmittedMatrices;
	}

	/**
	 * Return a read only list of matrices.
	 * 
	 * @return
	 */
	@Transient
	public List<Matrix> getSubmittedMatricesReadOnly() {
		List<Matrix> matrixList = new ArrayList<Matrix>();
		matrixList.addAll(getSubmittedMatrices());
		return matrixList;
		// return Collections.unmodifiableCollection(getSubmittedMatrices());
	}

	/**
	 * Return a read only list of treeblocks.
	 * 
	 * @return
	 */
	@Transient
	public Collection<TreeBlock> getSubmittedTreeBlocksReadOnly() {
		return Collections.unmodifiableCollection(getSubmittedTreeBlocks());
	}

	/**
	 * Return all submitted trees.
	 * 
	 * @return
	 */
	@Transient
	public Collection<PhyloTree> getAllSubmittedTrees() {
		Collection<PhyloTree> allTrees = new ArrayList<PhyloTree>();
		for (TreeBlock block : getSubmittedTreeBlocks()) {
			allTrees.addAll(block.getTreeList());
		}
		return allTrees;
	}

	/**
	 * Return a read only list of taxon labels.
	 * 
	 * @return
	 */
	@Transient
	public List<TaxonLabel> getSubmittedTaxonLabelsReadOnly() {
		List<TaxonLabel> labelList = new ArrayList<TaxonLabel>();
		// FIXME: cache the labellist as a transient field.
		labelList.addAll(getSubmittedTaxonLabels());
		Collections.sort(labelList, AbstractPersistedObject.COMPARATOR_ID);
		return labelList;
	}

	/**
	 * Set the SubmittedMatrices field.
	 */
	protected void setSubmittedMatrices(Collection<Matrix> pNewSubmittedMatrices) {
		mSubmittedMatrices = pNewSubmittedMatrices;
	}

	/**
	 * Like addPhyloTreeBlock, but don't add the block if it is already there
	 * Creation date: Jul 3, 2008
	 * @author mjd
	 * @param pTreeBlock
	 */
	public void addPhyloTreeBlockIfNecessary(TreeBlock pTreeBlock) {
		if (pTreeBlock != null && ! getSubmittedTreeBlocks().contains(pTreeBlock)) {
			addPhyloTreeBlock(pTreeBlock);
		}
	}
	
	/**
	 * Append a new Tree block to the end of the list.
	 * Don't check if it is already there; that will instantiate the entire submitted tree blocks list,
	 * which could be expensive.  Use addPhyloTreeBlockIfNecessary to add it only if it is not yet there.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTreeBlock
	 */
	public void addPhyloTreeBlock(TreeBlock pTreeBlock) {
		if (pTreeBlock != null) {
			getSubmittedTreeBlocks().add(pTreeBlock);
		}
	}

	/**
	 * Append new Trees to the end of the list.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTrees Collection<TreeBlock>
	 */
	public void addPhyloTreeBlocks(Collection<TreeBlock> pTreeBlocks) {
		if (pTreeBlocks != null) {
			getSubmittedTreeBlocks().addAll(pTreeBlocks);
		}
	}

	/**
	 * Remove the Tree.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTree PhyloTree
	 */
	public void removePhyloTreeBlock(TreeBlock pTreeBlock) {
		if (pTreeBlock != null) {
			getSubmittedTreeBlocks().remove(pTreeBlock);
		}
	}

	/**
	 * Append a new TaxonLabel.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTaxonLabel TaxonLabel
	 */
	public void addTaxonLabel(TaxonLabel pTaxonLabel) {
		if (pTaxonLabel != null) {
			getSubmittedTaxonLabels().add(pTaxonLabel);
		}
	}

	/**
	 * Add all TaxonLabels.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTaxonLabels Collection<TaxonLabel>
	 */
	public void addTaxonLabels(Collection<TaxonLabel> pTaxonLabels) {
		if (pTaxonLabels != null) {
			getSubmittedTaxonLabels().addAll(pTaxonLabels);
		}
	}

	/**
	 * Remove the TaxonLabel.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTaxonLabel
	 */
	public void removeTaxonLabel(TaxonLabel pTaxonLabel) {
		if (pTaxonLabel != null) {
			getSubmittedTaxonLabels().remove(pTaxonLabel);
		}
	}

	/**
	 * Append a new Matrix to the end of the list.
	 * Don't check if it is already there; that will instantiate the entire submitted matrices list,
	 * which could be expensive.  Use addMatrixIfNecessary to add it only if it is not yet there.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pMatrix
	 */
	public void addMatrix(Matrix pMatrix) {
		if (pMatrix != null) {
			getSubmittedMatrices().add(pMatrix);
		}
	}
	
	/**
	 * Like addMatrix, but don't add the matrix if it is already there
	 * Creation date: Jul 3, 2008
	 * @author mjd
	 * @param pTreeBlock
	 */
	public void addMatrixIfNecessary(Matrix pMatrix) {
		if (pMatrix != null && ! getSubmittedMatrices().contains(pMatrix)) {
			addMatrix(pMatrix);
		}
	}
	
	/**
	 * Append new Matrices to the end of the list.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pMatrices
	 */
	public void addMatrices(Collection<Matrix> pMatrices) {
		if (pMatrices != null) {
			getSubmittedMatrices().addAll(pMatrices);
		}
	}

	/**
	 * Remove the Matrix.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pMatrix
	 */
	public void removeMatrix(Matrix pMatrix) {
		if (pMatrix != null) {
			getSubmittedMatrices().remove(pMatrix);
		}
	}

	/**
	 * Returns true if the submission is published.
	 * 
	 * @return
	 */
	@Transient
	public boolean isPublished() {
		return getStudy().isPublished();
	}

	/**
	 * Returns true if the submission is ready for publish.
	 * 
	 * @return
	 */
	@Transient
	public boolean isReady() {
		return getStudy().isReady();
	}

	/**
	 * The last modified date.
	 * 
	 * @return
	 */
	@Transient
	public Date getLastModifiedDate() {
		return getStudy().getLastModifiedDate();
	}

	/**
	 * Returns true if the submission is in progress.
	 * 
	 * @return
	 */
	@Transient
	public boolean isInProgress() {
		return getStudy().isInProgress();
	}

	/**
	 * Remove all submitted data references.
	 */
	public void clearSubmittedData() {
		getSubmittedMatrices().clear();
		getSubmittedTaxonLabels().clear();
		getSubmittedTreeBlocks().clear();
	}

	/**
	 * Return the access right.
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService#getPermission(java.lang.String,
	 *      java.lang.Long)
	 * @param pUser
	 * @return
	 */
	public TBPermission getPermission(User pUser) {

		TBPermission perm = TBPermission.READ_ONLY;
		if (pUser == null) {
			// either the submission has no submitter or the user is not logged in.
			if (isPublished()) {
				perm = TBPermission.READ_ONLY;
			} else {
				perm = TBPermission.NONE;
			}
		} else if (pUser.getRole().isAdmin() && pUser.getRole().isAssociateEditor()) {
			perm = TBPermission.WRITE;
		} else if ( pUser.getRole().isAdmin() ) { /// XXX RAV - added, aren't admins all-powerful?
			perm = TBPermission.WRITE;
		} else if (pUser.getRole().isReviewer()) {
			// FIXME: handle reviewer -> submission
			// need another link table
			perm = TBPermission.READ_ONLY;
		} else if (pUser.getRole().isUser()) {
			if (getSubmitter() == pUser) {
				if (isInProgress()) {
					perm = TBPermission.WRITE;
				} else if (isReady()) {
					perm = TBPermission.SUBMITTED_WRITE;
				}
			} else {
				if (isPublished()) {
					perm = TBPermission.READ_ONLY;
				} else {
					perm = TBPermission.NONE;
				}
			}
		}
		return perm;
	}
}
