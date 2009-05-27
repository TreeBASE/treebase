/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

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
