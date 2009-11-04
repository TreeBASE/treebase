
package org.cipres.treebase.web.model;

import java.util.ArrayList;
import java.util.List;

import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * AnalyzedDataCommand.java
 * 
 * Created on Jun 21, 2006
 * 
 * @author lcchan
 * 
 */
@SuppressWarnings("serial")
public class AnalyzedDataCommand extends AnalyzedData {

	private Long mId; // analyziedData id (data has been added to analysis step)
	private String mInputOutputType;
	private String mDataType;
	private String mDisplayName;
	private String mStep;
	private Long mDataId; // id from either Matrix or PhyloTree table (data not added)
	private List<EditFieldCommand> mMatrixList = new ArrayList<EditFieldCommand>();
	private List<EditFieldCommand> mTreeList = new ArrayList<EditFieldCommand>();
	private List<EditFieldCommand> mTreeBlockList = new ArrayList<EditFieldCommand>();

	/**
	 * Return the PhyloTreeBlockList field.
	 * 
	 * @return List<EditFieldCommand> mTreeBlockList
	 */	
	public List<EditFieldCommand> getTreeBlockList() {
		return mTreeBlockList;
	}
	
	/**
	 * Set the PhyloTreeBlockList field.
	 */	
	public void setTreeBlockList(List<EditFieldCommand> pNewTreeBlockList) {
		mTreeBlockList = pNewTreeBlockList;
	}
	
	/**
	 * Return the PhyloTreeList field.
	 * 
	 * @return List<EditFieldCommand> mPhyloTreeList
	 */
	public List<EditFieldCommand> getTreeList() {
		return mTreeList;
	}

	/**
	 * Set the PhyloTreeList field.
	 */
	public void setTreeList(List<EditFieldCommand> pNewTreeList) {
		mTreeList = pNewTreeList;
	}

	/**
	 * Return the MatrixList field.
	 * 
	 * @return List<EditFieldCommand> mMatrixList
	 */
	public List<EditFieldCommand> getMatrixList() {
		return mMatrixList;
	}

	/**
	 * Set the MatrixList field.
	 */
	public void setMatrixList(List<EditFieldCommand> pNewMatrixList) {
		mMatrixList = pNewMatrixList;
	}

	/**
	 * Return the DataId field.
	 * 
	 * @return Long mDataId
	 */
	public Long getDataId() {
		return mDataId;
	}

	/**
	 * Set the DataId field.
	 */
	public void setDataId(Long pNewDataId) {
		mDataId = pNewDataId;
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
	 * Return the Step field.
	 * 
	 * @return String mStep
	 */
	public String getStep() {
		return mStep;
	}

	/**
	 * Set the Step field.
	 */
	public void setStep(String pNewStep) {
		mStep = pNewStep;
	};

	/**
	 * Return the Title field.
	 * 
	 * @return String mTitle
	 */
	public String getDisplayName() {
		return mDisplayName;
	}

	/**
	 * Set the Title field.
	 */
	public void setDisplayName(String pNewTitle) {
		mDisplayName = pNewTitle;
	}

	/**
	 * Return the DataType field.
	 * 
	 * @return String mDataType
	 */
	public String getDataType() {
		return mDataType;
	}

	/**
	 * Set the DataType field.
	 */
	public void setDataType(String pNewDataType) {
		mDataType = pNewDataType;
	}

	/**
	 * Return the InputOutput field.
	 * 
	 * @return String mInputOutput
	 */
	public String getInputOutputType() {
		return mInputOutputType;
	}

	/**
	 * Set the InputOutput field.
	 */
	public void setInputOutputType(String pNewInputOutputType) {
		mInputOutputType = pNewInputOutputType;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.study.AnalyzedData#getMatrixData()
	 */
	@Override
	public Matrix getMatrixData() {
		return null;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.study.AnalyzedData#getTreeData()
	 */
	@Override
	public PhyloTree getTreeData() {
		return null;
	}

}
