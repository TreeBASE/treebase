
package org.cipres.treebase.domain.study;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * AnalyzedTree
 * 
 * Created on Feb 23, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("T")
public class AnalyzedTree extends AnalyzedData {

	private static final long serialVersionUID = 1L;

	private Integer mTreeLength;
	private PhyloTree mTree;

	/**
	 * Constructor.
	 */
	public AnalyzedTree() {
		super();
	}

	/**
	 * Return the TreeLength field.
	 * 
	 * @return Integer
	 */
	@Column(name = "TreeLength")
	public Integer getTreeLength() {
		return mTreeLength;
	}

	/**
	 * Set the TreeLength field.
	 */
	public void setTreeLength(Integer pNewTreeLength) {
		mTreeLength = pNewTreeLength;
	}

	/**
	 * Return the PhyloTree field.
	 * 
	 * @return PhyloTree
	 */
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "PHYLOTREE_ID")
	public PhyloTree getTree() {
		return mTree;
	}

	/**
	 * Set the PhyloTree field.
	 */
	public void setTree(PhyloTree pNewTree) {
		mTree = pNewTree;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalyzedData#registerAnalysisStep(org.cipres.treebase.domain.study.AnalysisStep)
	 */
	@Override
	public void registerAnalysisStep(AnalysisStep pNewAnalysisStep) {
		super.registerAnalysisStep(pNewAnalysisStep);

//		System.out.println("Tree should not be null:");
		Study s = null;
		if (pNewAnalysisStep != null) {
			s = pNewAnalysisStep.getAnalysis().getStudy();
		}
		
		if (getTree() != null) {
			getTree().setStudy(s);
		}
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.study.AnalyzedData#getMatrixData()
	 */
	@Override
	@Transient
	public Matrix getMatrixData() {
		return null;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.study.AnalyzedData#getTreeData()
	 */
	@Override
	@Transient
	public PhyloTree getTreeData() {
		return getTree();
	}

	@Override
	@Transient
	public String getDataType() {
		return "tree";
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.study.AnalyzedData#getDisplayName()
	 */
	@Override
	@Transient
	public String getDisplayName() {
		PhyloTree theTree = getTree();
		return theTree == null ? "(none)" : theTree.getLabel();
	}

}
