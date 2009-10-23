/*
 * CIPRES Copyright (c) 2005- 2006, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.cipres.treebase.domain.matrix;

import java.util.List;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.RowSegmentService.RowSegmentField;
import org.cipres.treebase.domain.taxon.SpecimenLabel;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.framework.ExecutionResult;

/**
 * RowSegment.java
 * 
 * Created on Mar 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "ROWSEGMENT")
@AttributeOverride(name = "id", column = @Column(name = "ROWSEGMENT_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
@BatchSize(size = 20)
public class RowSegment extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private String mTitle;
	private int mStartIndex;
	private int mEndIndex;
	private transient boolean mChecked;

	private SpecimenLabel mSpecimenLabel;
	private MatrixRow mMatrixRow;
	private TaxonLabel mTaxonLabel;

	// Transient. Holds the specimen taxon label as a string during importing before creating
	// the real thing.
	private String mSpecimenTaxonLabelStr;

	// This causes conflicts w/ Matrix.study, if the matrix does not associate with a step.
	// private Study mStudy;

	/**
	 * Constructor.
	 */
	public RowSegment() {
		super();
	}

	/**
	 * Return the SpecimenTaxonLabelStr field. It is used internally during importing.
	 * 
	 * Do not use this one.
	 * 
	 * @return String
	 */
	@Transient
	public String getSpecimenTaxonLabelStr() {
		return mSpecimenTaxonLabelStr;
	}

	/**
	 * Set the SpecimenTaxonLabelStr field.
	 */
	private void setSpecimenTaxonLabelStr(String pNewSpecimenTaxonLabelStr) {
		mSpecimenTaxonLabelStr = pNewSpecimenTaxonLabelStr;
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String mTitle
	 */
	@Column(name = "Title", length = TBPersistable.COLUMN_LENGTH_STRING)
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
	 * Return the EndIndex field.
	 * 
	 * @return int mEndIndex
	 */
	@Column(name = "EndIndex")
	public int getEndIndex() {
		return mEndIndex;
	}

	/**
	 * Set the EndIndex field.
	 */
	public void setEndIndex(int pNewEndIndex) {
		mEndIndex = pNewEndIndex;
	}

	/**
	 * Return the StartIndex field.
	 * 
	 * @return int mStartIndex
	 */
	@Column(name = "StartIndex")
	public int getStartIndex() {
		return mStartIndex;
	}

	/**
	 * Set the StartIndex field.
	 */
	public void setStartIndex(int pNewStartIndex) {
		mStartIndex = pNewStartIndex;
	}

	/**
	 * Return the SpecimenLabel field.
	 * 
	 * @return SpecimenLabel
	 */
	// @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	// @JoinColumn(name = "SPECIMENLABEL_ID", nullable = true)
	@Embedded
	public SpecimenLabel getSpecimenLabel() {
		if (mSpecimenLabel == null) {
			mSpecimenLabel = new SpecimenLabel();
		}
		return mSpecimenLabel;
	}
	
	/**
	 * @return string with all available specimen label information
	 * @author mjd 200810114
	 */
	@Transient
	public String getSpecimenInfo() {
		SpecimenLabel sl = getSpecimenLabel();
		return sl == null ? null : sl.getInfo();
	}

	/**
	 * Set the SpecimenLabel field.
	 */
	public void setSpecimenLabel(SpecimenLabel pNewSpecimenLabel) {
		mSpecimenLabel = pNewSpecimenLabel;
	}

	/**
	 * Return the MatrixRow field.
	 * 
	 * @return MatrixRow
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATRIXROW_ID", nullable = false)
	public MatrixRow getMatrixRow() {
		return mMatrixRow;
	}

	/**
	 * Set the MatrixRow field.
	 */
	public void setMatrixRow(MatrixRow pNewMatrixRow) {
		mMatrixRow = pNewMatrixRow;
	}

	/**
	 * Return the TaxonLabel field.
	 * 
	 * @return TaxonLabel
	 */
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinColumn(name = "TAXONLABEL_ID", nullable = true)
	public TaxonLabel getTaxonLabel() {
		return mTaxonLabel;
	}

	/**
	 * Set the TaxonLabel field.
	 */
	public void setTaxonLabel(TaxonLabel pNewTaxon) {
		mTaxonLabel = pNewTaxon;
	}

	// /**
	// * Return the Study field.
	// *
	// * @return Study mStudy
	// */
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "STUDY_ID", nullable = false)
	// public Study getStudy() {
	// return mStudy;
	// }
	//
	// /**
	// * Set the Study field.
	// */
	// public void setStudy(Study pNewStudy) {
	// mStudy = pNewStudy;
	// }

	/**
	 * Build the row segment information as a TSV(Tab separated values) text. The order of
	 * properties is defined in RowSegmentServce.RowSegmentField enum (Except the first one IGNORE).
	 * 
	 * @param pBuffer
	 */
	public void buildExportTextTSV(StringBuffer pBuffer) {
		if (pBuffer == null) {
			return;
		}

		// The included properties and their order are defined in RowSegmentServce.RowSegmentField
		pBuffer.append(getId()).append('\t');
		pBuffer.append(getMatrixRow().getTaxonLabel().getTaxonLabel()).append('\t');

		if (getTitle() != null) {
			pBuffer.append(getTitle());
		}
		pBuffer.append('\t');

		pBuffer.append(getStartIndex()).append('\t');
		pBuffer.append(getEndIndex()).append('\t');

		if (getSpecimenLabel().getInstAcronym() != null) {
			pBuffer.append(getSpecimenLabel().getInstAcronym());
		}
		pBuffer.append('\t');

		if (getSpecimenLabel().getCollectionCode() != null) {
			pBuffer.append(getSpecimenLabel().getCollectionCode());
		}
		pBuffer.append('\t');

		if (getSpecimenLabel().getCatalogNumber() != null) {
			pBuffer.append(getSpecimenLabel().getCatalogNumber());
		}
		pBuffer.append('\t');

		if (getSpecimenLabel().getGenBankAccession() != null) {
			pBuffer.append(getSpecimenLabel().getGenBankAccession());
		}
		pBuffer.append('\t');

		if (getSpecimenLabel().getOtherAccession() != null) {
			pBuffer.append(getSpecimenLabel().getOtherAccession());
		}
		pBuffer.append('\t');

		if (getSpecimenLabel().getSampleDate() != null) {
			// show date only, ISO 8601 format:
			pBuffer.append(getSpecimenLabel().getSampleDateString());
		}
		pBuffer.append('\t');

		if (getTaxonLabel() != null) {
			pBuffer.append(getTaxonLabel().getTaxonLabel());
		}
		pBuffer.append('\t');

		if (getSpecimenLabel().getCollector() != null) {
			pBuffer.append(getSpecimenLabel().getCollector());
		}
		pBuffer.append('\t');

		if (getSpecimenLabel().getLatitude() != null) {
			pBuffer.append(getSpecimenLabel().getLatitude());
		}
		pBuffer.append('\t');

		if (getSpecimenLabel().getLongitude() != null) {
			pBuffer.append(getSpecimenLabel().getLongitude());
		}
		pBuffer.append('\t');

		if (getSpecimenLabel().getElevation() != null) {
			pBuffer.append(getSpecimenLabel().getElevation());
		}
		pBuffer.append('\t');

		if (getSpecimenLabel().getCountry() != null) {
			pBuffer.append(getSpecimenLabel().getCountry());
		}
		pBuffer.append('\t');

		if (getSpecimenLabel().getState() != null) {
			pBuffer.append(getSpecimenLabel().getState());
		}
		pBuffer.append('\t');

		if (getSpecimenLabel().getLocality() != null) {
			pBuffer.append(getSpecimenLabel().getLocality());
		}
		pBuffer.append('\t');

		pBuffer.append('\n');
	}

	/**
	 * update the row segment based on the field names and their corresponding values.
	 * 
	 * @param pMappedFields
	 * @param pValues
	 * @param pTaxonLabelRowMap
	 * @param pExecution
	 */
	public void update(
		List<RowSegmentField> pMappedFields,
		List<String> pValues,
		Map<String, MatrixRow> pTaxonLabelRowMap,
		ExecutionResult pExecution) {

		for (int i = 0; i < pMappedFields.size(); i++) {
			RowSegmentField mappedField = pMappedFields.get(i);

			if (mappedField == RowSegmentField.TITLE) {
				setTitle(pValues.get(i));
			} else if (mappedField == RowSegmentField.TAXONLABEL) {
				String rowTaxonLabel = pValues.get(i);

				if (rowTaxonLabel != null) {
					MatrixRow row = pTaxonLabelRowMap.get(rowTaxonLabel);
					setMatrixRow(row);
				}
			} else if (mappedField == RowSegmentField.START_INDEX) {
				setStartIndex(Integer.parseInt(pValues.get(i)));
			} else if (mappedField == RowSegmentField.END_INDEX) {
				setEndIndex(Integer.parseInt(pValues.get(i)));
			} else if (mappedField == RowSegmentField.INST_ACRONYM) {
				getSpecimenLabel().setInstAcronym(pValues.get(i));
			} else if (mappedField == RowSegmentField.COLLECTION_CODE) {
				getSpecimenLabel().setCollectionCode(pValues.get(i));
			} else if (mappedField == RowSegmentField.CATALOG_NUM) {
				getSpecimenLabel().setCatalogNumber(pValues.get(i));
			} else if (mappedField == RowSegmentField.GENBANK_ACC_NUM) {
				getSpecimenLabel().setGenBankAccession(pValues.get(i));
			} else if (mappedField == RowSegmentField.OTHER_ACC_NUM) {
				getSpecimenLabel().setOtherAccession(pValues.get(i));
			} else if (mappedField == RowSegmentField.SAMPLE_DATE) {
				getSpecimenLabel().setSampleDate(
					TreebaseUtil.parseDate(pValues.get(i), true, pExecution));
			} else if (mappedField == RowSegmentField.SAMPLE_TAXONLABEL) {
				setSpecimenTaxonLabelStr(pValues.get(i));
			} else if (mappedField == RowSegmentField.COLLECTOR) {
				getSpecimenLabel().setCollector(pValues.get(i));
			} else if (mappedField == RowSegmentField.LATITUDE) {
				getSpecimenLabel()
					.setLatitude(TreebaseUtil.parseDouble(pValues.get(i), pExecution));
			} else if (mappedField == RowSegmentField.LONGITUDE) {
				getSpecimenLabel().setLongitude(
					TreebaseUtil.parseDouble(pValues.get(i), pExecution));
			} else if (mappedField == RowSegmentField.ELEVATION) {
				getSpecimenLabel().setElevation(
					TreebaseUtil.parseDouble(pValues.get(i), pExecution));
			} else if (mappedField == RowSegmentField.COUNTRY) {
				getSpecimenLabel().setCountry(pValues.get(i));
			} else if (mappedField == RowSegmentField.STATE) {
				getSpecimenLabel().setState(pValues.get(i));
			} else if (mappedField == RowSegmentField.LOCALITY) {
				getSpecimenLabel().setLocality(pValues.get(i));
			}

		}
	}

	/**
	 * Validate: * matrix row is not null * start index, end index is required, and 0<= start <=end <=
	 * column count.Missing element states
	 * 
	 * @param pExecution
	 * @return
	 */
	public boolean validate(ExecutionResult pExecution) {

		boolean isValid = true;

		// check start and end indices!
		if (getMatrixRow() == null || getStartIndex() < 0 || getEndIndex() < 0) {

			isValid = false;
			String errTitle = getTitle();
			if (getMatrixRow() != null) {
				errTitle += " " + getMatrixRow().getTaxonLabel().getTaxonLabel();
			}
			pExecution
				.addErrorMessage("Error: taxon label, start index, and end index fields are required for row segment:"
					+ errTitle);
		} else {
			int columnCount = getMatrixRow().getMatrix().getColumnsReadOnly().size();

			if (getStartIndex() > getEndIndex() || getEndIndex() > columnCount) {
				isValid = false;
				String errTitle = getTitle() + " " + getMatrixRow().getTaxonLabel().getTaxonLabel();
				pExecution.addErrorMessage("Error: segment start or end indices for row segment:"
					+ errTitle);
			}
		}

		return isValid;
	}

	/**
	 * Return the specimen taxonLabel as a string if it is not null.
	 * 
	 */
	@Transient
	public String getSpecimenTaxonLabelAsString() {
		if (getTaxonLabel() != null) {
			return getTaxonLabel().getTaxonLabel();
		}

		return null;
	}

	/**
	 * Returns a string representation of the segment.
	 * 
	 * @return
	 */
	@Transient
	public String getSegmentData() {
		if (getEndIndex() - getStartIndex() <= 0) {
			return null;
		}
		return getMatrixRow().getElementAsString(getStartIndex(), getEndIndex());
	}

	/**
	 * @return the checked
	 */
	@Transient
	public boolean getChecked() {
		return mChecked;
	}

	/**
	 * @param pChecked the checked to set
	 */
	@Transient
	public void setChecked(boolean pChecked) {
		mChecked = pChecked;
	}
}
