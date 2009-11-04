
package org.cipres.treebase.domain.matrix;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cipres.treebase.framework.ExecutionResult;
import org.cipres.treebase.service.AbstractService;

/**
 * RowSegmentService.java
 * 
 * Created on May 5, 2008
 * 
 * @author Jin Ruan
 * 
 */
public interface RowSegmentService extends AbstractService {

	/**
	 * A enum representing the fields that can be imported directly to create RowSegments.
	 * 
	 * Created on May 5, 2008
	 * 
	 * @author Jin Ruan
	 * 
	 */
	public enum RowSegmentField {
		// Current total 21 Fields are defined here:
		IGNORE("Ignore"), ROWSEGMENTID("RowSegmentID"), TAXONLABEL("Row Taxon Label"), TITLE(
			"Title"), START_INDEX("Start Index"), END_INDEX("End Index"), INST_ACRONYM(
			"Inst. Acronym"), COLLECTION_CODE("Collection Code"), CATALOG_NUM("Catalog Number"), GENBANK_ACC_NUM(
			"GenBank Accession"), OTHER_ACC_NUM("Other Accession Num"), SAMPLE_DATE("Sample Date"), SAMPLE_TAXONLABEL(
			"Sample Taxon Label"), COLLECTOR("Collector"), LATITUDE("Latitude"), LONGITUDE(
			"Longitude"), ELEVATION("Elevation"), COUNTRY("Country"), STATE("State"), LOCALITY(
			"Locality"), NOTES("Notes");

		private String mDisplayName;
		private static Map<String, RowSegmentField> sDisplayNameMap = new HashMap<String, RowSegmentField>();

		/**
		 * Constructor.
		 * 
		 * @param pName
		 */
		RowSegmentField(String pDisplayName) {
			mDisplayName = pDisplayName;
		}

		/**
		 * Return our version of display name.
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return mDisplayName;
		}

		/**
		 * Find the RowSegmentField enum by display name.
		 * 
		 * @param pDisplayName
		 * @return
		 */
		public static RowSegmentField findByDisplayName(String pDisplayName) {
			if (sDisplayNameMap.isEmpty()) {
				EnumSet<RowSegmentField> allFields = EnumSet.allOf(RowSegmentField.class);
				for (RowSegmentField rowSegmentField : allFields) {
					sDisplayNameMap.put(rowSegmentField.mDisplayName, rowSegmentField);
				}
			}

			return sDisplayNameMap.get(pDisplayName);
		}

		/**
		 * Return a list of display names for RowSegmentField enum. It is order by the enum order.
		 * 
		 * @return
		 */
		public static List<String> getDisplayNameList() {
			EnumSet<RowSegmentField> allFields = EnumSet.allOf(RowSegmentField.class);

			// rsHeaderList.remove(RowSegmentField.IGNORE);
			List<String> headerNameList = new ArrayList<String>();

			for (RowSegmentField rowSegmentField : allFields) {
				headerNameList.add(rowSegmentField.mDisplayName);
			}

			// Collections.sort(headerNameList, new GenericComparator());
			// headerNameList.add(0, RowSegmentField.IGNORE.toString());

			return headerNameList;
		}

	};

	/**
	 * Return a row segment object by id.
	 * 
	 * @param pRowSegmentID
	 * @return
	 */
	RowSegment findByID(Long pRowSegmentID);

	/**
	 * Create a new segment. The start and end indices are inclusive.
	 * 
	 * @param pRow
	 * @param pStart
	 * @param pEnd
	 * @return
	 */
	RowSegment createSegment(MatrixRow pRow, int pStart, int pEnd);

	/**
	 * Delete a row segment and its associated objects.
	 * 
	 * Return true if the deletion is successful.
	 * 
	 * @param pSegment
	 * @return
	 */
	boolean deleteRowSegment(RowSegment pSegment);

	/**
	 * Find all row segments for the matrix.
	 * 
	 * @param pMatrixID
	 * @return
	 */
	List<RowSegment> findByMatrixID(Long pMatrixID);

	/**
	 * Return a TSV(tab separated value) text representing all row segments in a matrix.
	 * 
	 * @param pMatrixID
	 * @return
	 */
	String generateRowSegmentTextTSV(Long pMatrixID);

	/**
	 * Return a TSV(tab separated value) text representing segment template in a matrix. It contains
	 * all the row segment fields (except the row segment id) and a full set of rows with the row
	 * taxon label pre-filled (and let's also prefill the "Sample Taxon Label" with whatever is in
	 * the "Row Taxon Label" -- that way it's easy for the user to edit this column).
	 * 
	 * The fields contained in the template file are the same as in the sgenerateRowSegmentTextTSV()
	 * (row segment export feature), with one exception: the row segment id field is not in the
	 * template file but only in the export file.
	 * 
	 * @param pMatrixID
	 * @return
	 */
	String generateRowSegmentTemplateTSV(Long pMatrixID);

	/**
	 * Parse the files, create and store the row segments for the matrix. For each row segment that
	 * fail to create, add an error/warning message to the ExecutionResult.
	 * 
	 * @param pStudyId the study for storing/accessing taxonLabels.
	 * @param pMatrixId the matrix these row segments should be created
	 * @param pFile the tab delimited file
	 * @param pMappedFields the columns are mapped to the row segment fields
	 * @param pSkipFirstRow whether the first row contains column header
	 * @return
	 */
	ExecutionResult createSegments(
		Long pStudyId,
		Long pMatrixId,
		File pFile,
		List<RowSegmentField> pMappedFields,
		boolean pSkipFirstRow);

}
