
package org.cipres.treebase.domain.matrix;

import java.util.List;

import org.cipres.treebase.domain.DomainHome;

/**
 * RowSegmentHome.java
 * 
 * Created on Jun 16, 2006
 * @author Jin Ruan
 *
 */
public interface RowSegmentHome extends DomainHome {

	/**
	 * Find all row segments for the matrix. 
	 * 
	 * @param pMatrixID
	 * @return
	 */
	List<RowSegment> findByMatrixID(Long pMatrixID);
	
	/**
	 * Delete all row segments in the column range of (start, end) for the specified matrix.
	 * The column index is 0 based. 
	 * 
	 * @param pMatrixID
	 * @param pStart
	 * @param pEnd
	 * @return
	 */
	int deleteByMatrixAndColumnRange(Long pMatrixID, int pStart, int pEnd);
	
}
