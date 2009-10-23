/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */

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
