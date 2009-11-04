
package org.cipres.treebase.web.model;

import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;

/**
 * EditFieldCommands.java
 * 
 * Object which contains fields that allow use to edit
 * when matrixList and treeList tables are shown
 * 
 * Created on Jun 23, 2006
 * @author lcchan
 *
 */
public class EditFieldCommand {

	private Long mId;
	private String mTitle;
	private String mDescription;
	private Boolean mChecked; // user's current selection 
	private Boolean mInput;
	private String mSelected; 
	private Matrix mMatrix;
	private PhyloTree mPhyloTree;
	private TreeBlock mTreeBlock;
	
	public EditFieldCommand() {}
	
	public EditFieldCommand(Matrix matrix, String selected) {
		this.mMatrix = matrix;
		this.mSelected = selected;
	}
	public EditFieldCommand(PhyloTree phyloTree, String selected) {
		this.mPhyloTree = phyloTree;
		this.mSelected = selected;
	}
	public EditFieldCommand(TreeBlock treeBlock, String selected) {
		this.mTreeBlock = treeBlock;
		this.mSelected = selected;
	}

	/**
	 * Return the TreeBlock field.
	 * 
	 * @return TreeBlock mTreeBlock
	 */	
	public TreeBlock getTreeBlock() {
		return mTreeBlock;
	}
	
	/**
	 * Set the TreeBlock field.
	 */	
	public void setTreeBlock(TreeBlock pNewTreeBlock) {
		mTreeBlock = pNewTreeBlock;
	}

	/**
	 * Return the PhyloTree field.
	 * 
	 * @return PhyloTree mPhyloTree
	 */
	public PhyloTree getPhyloTree() {
		return mPhyloTree;
	}

	/**
	 * Set the PhyloTree field.
	 */
	public void setPhyloTree(PhyloTree pNewPhyloTree) {
		mPhyloTree = pNewPhyloTree;
	}

	/**
	 * Return the Matrix field.
	 * 
	 * @return Matrix mMatrix
	 */
	public Matrix getMatrix() {
		return mMatrix;
	}

	/**
	 * Set the Matrix field.
	 */
	public void setMatrix(Matrix pNewMatrix) {
		mMatrix = pNewMatrix;
	}

	/**
	 * Return the Input field.
	 * 
	 * @return Boolean mInput
	 */
	public Boolean getInput() {
		return mInput;
	}

	/**
	 * Set the Input field.
	 */
	public void setInput(Boolean pNewInput) {
		mInput = pNewInput;
	}
	
	/**
	 * Return the Checked field.
	 * 
	 * @return Boolean mChecked
	 */
	public Boolean getChecked() {
		return mChecked;
	}

	/**
	 * Set the Checked field.
	 */
	public void setChecked(Boolean pNewChecked) {
		mChecked = pNewChecked;
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String mDescription
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Set the Description field.
	 */
	public void setDescription(String pNewDescription) {
		mDescription = pNewDescription;
	}
	
	/**
	 * Return the Title field.
	 * 
	 * @return String mTitle
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Set the Title field.
	 */
	public void setTitle(String pNewTitle) {
		mTitle = pNewTitle;
	}

	/**
	 * Return the Id field.
	 * 
	 * @return Long mId
	 */
	public Long getId() {
		return mId;
	}

	/**
	 * Set the Id field.
	 */
	public void setId(Long pNewId) {
		mId = pNewId;
	}
	/**
	 * Return the Slected field.
	 * 
	 * @return Long mSelected
	 */
	public String getSelected() {
		return mSelected;
	}

	/**
	 * Set the Id field.
	 */
	public void setSelected(String pNewSelected) {
		mSelected = pNewSelected;
	}

	public String getMSelected() {
		return mSelected;
	}

	public void setMSelected(String selected) {
		mSelected = selected;
	}
}
