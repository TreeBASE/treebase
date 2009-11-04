
package org.cipres.treebase.service.matrix;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.MatrixElement;
import org.cipres.treebase.domain.matrix.MatrixElementHome;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.matrix.RowSegmentHome;
import org.cipres.treebase.domain.matrix.RowSegmentService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.framework.ExecutionResult;
import org.cipres.treebase.framework.TSVFileParser;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * RowSegmentServiceImpl.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class RowSegmentServiceImpl extends AbstractServiceImpl implements RowSegmentService {

	private RowSegmentHome mRowSegmentHome;
	private MatrixElementHome mMatrixElementHome;
	private TaxonLabelHome mTaxonLabelHome;
	
	/**
	 * Constructor.
	 */
	public RowSegmentServiceImpl() {
		super();
	}

	/**
	 * Return the MatrixElementHome field.
	 * 
	 * @return MatrixElementHome mMatrixElementHome
	 */
	private MatrixElementHome getMatrixElementHome() {
		return mMatrixElementHome;
	}

	/**
	 * Set the MatrixElementHome field.
	 */
	public void setMatrixElementHome(MatrixElementHome pNewMatrixElementHome) {
		mMatrixElementHome = pNewMatrixElementHome;
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
	 * 
	 * @param pTaxonLabelHome
	 */
	public void setTaxonLabelHome(TaxonLabelHome pTaxonLabelHome) {
		mTaxonLabelHome = pTaxonLabelHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getRowSegmentHome();
	}

	/**
	 * Return the RowSegmentHome field.
	 * 
	 * @return RowSegmentHome mRowSegmentHome
	 */
	private RowSegmentHome getRowSegmentHome() {
		return mRowSegmentHome;
	}

	/**
	 * Set the RowSegmentHome field.
	 */
	public void setRowSegmentHome(RowSegmentHome pNewRowSegmentHome) {
		mRowSegmentHome = pNewRowSegmentHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.RowSegmentService#createSegment(org.cipres.treebase.domain.matrix.MatrixRow,
	 *      int, int)
	 */
	public RowSegment createSegment(MatrixRow pRow, int pStart, int pEnd) {
		// parameters validation:
		if (pRow == null || pStart < 0 || pEnd < 0 || pStart > pEnd) {
			return null;
		}

		List<MatrixElement> elements = pRow.getElementsReadOnly();
		if (elements.size() <= pEnd) {
			return null;
		}

		RowSegment seg = new RowSegment();

		seg.setStartIndex(pStart);
		seg.setEndIndex(pEnd);
		pRow.addSegment(seg);

		// use cascade persistence to
		update(pRow);
		seg = update(seg);
		return seg;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.RowSegmentService#findByID(java.lang.Long)
	 */
	public RowSegment findByID(Long pRowSegmentID) {
		if (pRowSegmentID == null) {
			return null;
		}
		return getRowSegmentHome().findPersistedObjectByID(RowSegment.class, pRowSegmentID);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.RowSegmentService#findByMatrixID(java.lang.Long)
	 */
	public List<RowSegment> findByMatrixID(Long pMatrixID) {
		return getRowSegmentHome().findByMatrixID(pMatrixID);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.RowSegmentService#generateRowSegmentTextTSV(java.lang.Long)
	 */
	public String generateRowSegmentTextTSV(Long pMatrixID) {
		List<RowSegment> rsList = findByMatrixID(pMatrixID);

		StringBuffer rsTxt = new StringBuffer();

		// First add the header info:
		// Remove the field: IGNORE
		EnumSet<RowSegmentField> allFields = EnumSet.allOf(RowSegmentField.class);
		allFields.remove(RowSegmentField.IGNORE);

		for (RowSegmentField rowSegmentField : allFields) {
			rsTxt.append(rowSegmentField.toString()).append('\t');
		}
		rsTxt.append('\n');

		// Now append one row segment for each row:
		for (RowSegment rs : rsList) {
			rs.buildExportTextTSV(rsTxt);
		}

		return rsTxt.toString();
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.matrix.RowSegmentService#generateRowSegmentTemplateTSV(java.lang.Long)
	 */
	public String generateRowSegmentTemplateTSV(Long pMatrixID) {
		CharacterMatrix m = getDomainHome().getPersistedObjectByID(CharacterMatrix.class, pMatrixID);
		if (m == null) {
			return null;
		}
		
		List<MatrixRow> rows = m.getRowsReadOnly();

		StringBuffer rsTxt = new StringBuffer();

		// First add the header info:
		// Remove the field: IGNORE, ROW_SEGMENT_ID
		EnumSet<RowSegmentField> allFields = EnumSet.allOf(RowSegmentField.class);
		allFields.remove(RowSegmentField.IGNORE);
		allFields.remove(RowSegmentField.ROWSEGMENTID);

		for (RowSegmentField rowSegmentField : allFields) {
			rsTxt.append(rowSegmentField.toString()).append('\t');
		}
		rsTxt.append('\n');

		int colCount = m.getColumns().size();
		
		// Now append one row segment for each row:
		for (MatrixRow aRow : rows) {
			// The included properties and their order are defined in RowSegmentServce.RowSegmentField
			//Id field is skipped. 
			//rsTxt.append('\t');
			String rowTaxonLabel = aRow.getTaxonLabel().getTaxonLabel();
			rsTxt.append(rowTaxonLabel).append('\t');

			//title, 
			rsTxt.append("\t");
			//start index, 
			rsTxt.append(1).append('\t');
			//end index,
			rsTxt.append(colCount).append('\t');
			
			//InstAcronym, CollectionCode, CatalogNumber
			//GenBankAccession, OtherAccession, SampleDate
			rsTxt.append("\t\t\t\t\t\t");
			
			//pre fill the segment taxon label to be the same as row taxon label.
			rsTxt.append(rowTaxonLabel).append('\t');

			//latitude, longitude, elevation, country, state, locality, collector
			rsTxt.append("\t\t\t\t\t\t\t");

			rsTxt.append('\n');
		}

		return rsTxt.toString();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.RowSegmentService#deleteRowSegment(org.cipres.treebase.domain.matrix.RowSegment)
	 */
	public boolean deleteRowSegment(RowSegment pSegment) {
		if (pSegment == null) {
			return false;
		}

		// bi-directional relationship management
		MatrixRow aRow = pSegment.getMatrixRow();
		if (aRow != null) {
			aRow.removeSegment(pSegment);
		}

		// Cascade delete:
		// TODO: need to delete specimenLabel if it has no other references.

		getRowSegmentHome().deletePersist(pSegment);

		return true;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.RowSegmentService#createSegments(Long, Long,
	 *      java.io.File, java.util.List, boolean)
	 */
	public ExecutionResult createSegments(
		Long pStudyId, Long pMatrixId,
		File pFile,
		List<RowSegmentField> pMappedFields,
		boolean pSkipFirstRow) {

		ExecutionResult returnVal = new ExecutionResult();
		if (pStudyId == null || pMatrixId == null || pFile == null || pMappedFields == null) {
			returnVal.addErrorMessage("Null parameters passed in. Cannot create Row Segment.");
			return returnVal;
		}
		
		TSVFileParser parser = new TSVFileParser();
		List<List<String>> values = parser.parseFile(pFile, pSkipFirstRow, returnVal);

		// check whether parsing is successful:
		if (returnVal.isSuccessful()) {
			List<RowSegment> newRS = new ArrayList<RowSegment>();
			List<RowSegment> updateRS = new ArrayList<RowSegment>();
			Study s = getDomainHome().loadPersistedObjectByID(Study.class, pStudyId);
			CharacterMatrix m = getDomainHome().loadPersistedObjectByID(CharacterMatrix.class, pMatrixId);
			
			mapToSegments(s, m, values, pMappedFields, newRS, updateRS, returnVal);

			// Save to database:
			getRowSegmentHome().storeAll(newRS);
			//Don't need to merge since it is the same session:
			for (RowSegment anUpdate : updateRS) {
				getRowSegmentHome().merge(anUpdate);
			}
			
			// Report result:
			int rowCount = values.size();
			int successCount = newRS.size() + updateRS.size();

			returnVal.setSuccessfulCount(successCount);
			returnVal.setFailureCount(rowCount - successCount);
		}

		return returnVal;
	}

	/**
	 * 
	 * @param pStudy
	 * @param pMatrix
	 * @param pValues
	 * @param pMappedFields
	 * @param pExecution
	 * @return
	 */
	private void mapToSegments(
		Study pStudy,
		CharacterMatrix pMatrix,
		List<List<String>> pValues,
		List<RowSegmentField> pMappedFields,
		List<RowSegment> pNewSegments,
		List<RowSegment> pUpdatedSegments,
		ExecutionResult pExecution) {
		
		// First we need to determine the identity columns for the input file:
		// if the row segment id is not null, we need to update this row segment
		// else the (taxonLabel, start, and end) must not be null, need to create a new segment
		int rowSegmentIdColIndex = pMappedFields.indexOf(RowSegmentField.ROWSEGMENTID);
		int rowTaxonLabelIndex = pMappedFields.indexOf(RowSegmentField.TAXONLABEL);
		int startIndex = pMappedFields.indexOf(RowSegmentField.START_INDEX);
		int endIndex = pMappedFields.indexOf(RowSegmentField.END_INDEX);

		if (rowSegmentIdColIndex < 0) {
			if (rowTaxonLabelIndex < 0 || startIndex < 0 || endIndex < 0) {
				pExecution
					.addErrorMessage("Either "
						+ RowSegmentField.ROWSEGMENTID.toString()
						+ " field must be mapped (for updating existing row segment or row taxon Label,"
						+ " start index and end index must be mapped (for creating new row segment).");

				return;
			}
		}

		for (List<String> lineStr : pValues) {
			boolean isCreate = true;
			boolean hasErr = false;
			RowSegment aRowSegment = null;

			if (rowSegmentIdColIndex >= 0) {
				// row seg id column is mapped, need to check whether it has
				// value:
				String rsIDStr = lineStr.get(rowSegmentIdColIndex);
				if (!TreebaseUtil.isEmpty(rsIDStr)) {
					isCreate = false;

					Long rsID = Long.valueOf(rsIDStr);
					RowSegment existingRS = getRowSegmentHome().getPersistedObjectByID(
						RowSegment.class,
						rsID);
					if (existingRS == null) {
						// Error:
						hasErr = true;
						pExecution.addErrorMessage("Error: Row Segment id " + rsID
							+ " is not found");
					} else {
						aRowSegment = existingRS;
					}
				}
			}

			if (!hasErr) {
				
				// check and create a new rs:
				if (isCreate) {
					aRowSegment = new RowSegment();
				}

				Map<String, MatrixRow> taxonLabelRowMap = pMatrix.buildTaxonLabelRowMap();
				aRowSegment.update(pMappedFields, lineStr, taxonLabelRowMap, pExecution);
				
				//specimen label
				String specimenTaxonLabel = aRowSegment.getSpecimenTaxonLabelStr();
				TaxonLabel taxonLabel = getTaxonLabelHome().getByDescriptionAndStudy(
					specimenTaxonLabel, pStudy);
				
				aRowSegment.setTaxonLabel(taxonLabel);
				
				//validate
				if (aRowSegment.validate(pExecution)) {
					if (isCreate) {
						pNewSegments.add(aRowSegment);
					} else {
						pUpdatedSegments.add(aRowSegment);
					}
				}
			}
			

		}
	}

	@Override
	public Class defaultResultClass() {
		return RowSegment.class;
	}

}
