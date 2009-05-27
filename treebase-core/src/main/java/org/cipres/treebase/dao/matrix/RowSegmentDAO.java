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

package org.cipres.treebase.dao.matrix;

import java.util.List;

import org.hibernate.Query;

import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.matrix.RowSegmentHome;

/**
 * RowSegmentDAO.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class RowSegmentDAO extends AbstractDAO implements RowSegmentHome {

	/**
	 * Constructor.
	 */
	public RowSegmentDAO() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.RowSegmentHome#findByMatrixID(java.lang.Long)
	 */
	public List<RowSegment> findByMatrixID(Long pMatrixID) {
		if (pMatrixID <= 0) {
			return null;
		}

		StringBuffer query = new StringBuffer(
			"select rs from RowSegment rs join rs.matrixRow row where row.matrix = :matrixId");

		Query q = getSession().createQuery(query.toString());

		// Important: use setLong() instead of setParameter() !!
		q.setLong("matrixId", pMatrixID);
		return q.list();
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.matrix.RowSegmentHome#deleteByMatrixAndColumnRange(java.lang.Long, int, int)
	 */
	public int deleteByMatrixAndColumnRange(Long pMatrixId, int pStart, int pEnd) {

		// error checking:
		CharacterMatrix m = getPersistedObjectByID(CharacterMatrix.class, pMatrixId);
		if (m == null || pStart < 0 || pEnd < 0 || pStart > pEnd) {
			return 0;
		}

		// int columnCount = m.getColumnsReadOnly().size();
		// another way to find the size of a collection w/o initialze it:
		int columnCount = ((Number) getSession()
			.createFilter(m.getColumns(), "select count(*)").uniqueResult()).intValue();

		if (pStart >= columnCount || pEnd >= columnCount) {
			return 0;
		}

		StringBuffer query = new StringBuffer(
			"delete from RowSegment r where r in (select r2 from RowSegment r2 join r2.matrixRow row " +
		"where row.matrix = :matrixId and r2.startIndex between :start and :endIndex and r2.endIndex between :start and :endIndex)");
//			"delete RowSegment r join r.matrixRow row where row.matrix = :matrixId and "
//				+ "r.startIndex between :start and :endIndex and r.endIndex between :start and :endIndex");

		Query q = getSession().createQuery(query.toString());

		// Important: use setLong() instead of setParameter() !!
		q.setLong("matrixId", pMatrixId);
		q.setInteger("start", pStart);
		q.setInteger("endIndex", pEnd);

		int count = q.executeUpdate();
		return count;
	}
}
