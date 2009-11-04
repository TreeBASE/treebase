
package org.cipres.treebase.web.model;

import org.apache.log4j.Logger;

import org.cipres.treebase.domain.matrix.RowSegment;

/**
 * RowSegmentCommand.java
 * 
 * Created on June 23, 2006
 * 
 * @author lcchan
 * 
 */
public class RowSegmentCommand {
	private static final Logger LOGGER = Logger.getLogger(RowSegmentCommand.class);

	private String mTaxonLabel;
	private String mMatrixRowData;
	private RowSegment mRowSegment = new RowSegment();
	private String mMatrixRowDataSelected;
	private Long mTaxonLabelId;

	/**
	 * Return the MatrixRowDataSelected field.
	 * 
	 * @return String mMatrixRowDataSelected
	 */
	public String getMatrixRowDataSelected() {
		return mMatrixRowDataSelected;
	}

	/**
	 * Set the MatrixRowDataSelected field.
	 */
	public void setMatrixRowDataSelected(String pNewMatrixRowDataSelected) {
		mMatrixRowDataSelected = pNewMatrixRowDataSelected;
	}

	/**
	 * Return the RowSegment field.
	 * 
	 * @return RowSegment mRowSegment
	 */
	public RowSegment getRowSegment() {
		return mRowSegment;
	}

	/**
	 * Set the RowSegment field.
	 */
	public void setRowSegment(RowSegment pNewRowSegment) {
		mRowSegment = pNewRowSegment;
	}

	/**
	 * Return the MatrixRowData field.
	 * 
	 * @return String mMatrixRowData
	 */
	public String getMatrixRowData() {
		return mMatrixRowData;
	}

	/**
	 * Set the MatrixRowData field.
	 */
	public void setMatrixRowData(String pNewMatrixRowData) {
		mMatrixRowData = pNewMatrixRowData;
	}

	/**
	 * Return the TaxonLabel field.
	 * 
	 * @return String mTaxonLabel
	 */
	public String getTaxonLabel() {
		return mTaxonLabel;
	}

	/**
	 * Set the TaxonLabel field.
	 */
	public void setTaxonLabel(String pNewTaxonLabel) {
		mTaxonLabel = pNewTaxonLabel;
	}

	public Long getTaxonLabelId() {
		return mTaxonLabelId;
	}

	public void setTaxonLabelId(Long pTexonLabelId) {
		mTaxonLabelId = pTexonLabelId;
	}

}
