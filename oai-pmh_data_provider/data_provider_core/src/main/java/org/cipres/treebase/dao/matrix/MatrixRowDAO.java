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

import java.util.ArrayList;
import java.util.Collection;

import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.MatrixRowHome;
import org.cipres.treebase.domain.matrix.RowSegmentHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * MatrixRowDAO.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class MatrixRowDAO extends AbstractDAO implements MatrixRowHome {

	private RowSegmentHome mRowSegmentHome;

	/**
	 * Constructor.
	 */
	public MatrixRowDAO() {
		super();
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
	 * @see org.cipres.treebase.domain.matrix.MatrixRowHome#deleteRows(java.util.Collection)
	 */
	public void deleteRows(Collection<MatrixRow> pRows) {
		if (pRows == null || pRows.isEmpty()) {
			return;
		}

		// use a new collection to avoid concurrent modification
		Collection<MatrixRow> rows = new ArrayList<MatrixRow>(pRows);

		for (MatrixRow row : rows) {
			if (row != null && row.getId() != null) {

				// bi-directional relationship management:
				// matrix -> matrixRow
				if (row.getMatrix() != null) {
					row.getMatrix().removeRow(row);
				}

				// Cascade delete: handled by Hibernate.
				// row segment, which in turn, cascade delete matrix element, item values,
				// and state modifiers.

				getHibernateTemplate().delete(row);
			}
		}
	}

	/**
	 * @see org.cipres.treebase.domain.matrix.MatrixRowHome#findByLabel(java.lang.String)
	 */
	public Collection<MatrixRow> findByLabel(String pTaxonLabel) {
		Criteria c;

		c = getSession().createCriteria(MatrixRow.class);
		c.createCriteria("taxonLabel").add(Restrictions.eq("taxonLabel", pTaxonLabel));
		return c.list();
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.MatrixRowHome#findByTaxonLabel(org.cipres.treebase.domain.taxon.TaxonLabel)
	 */
	public Collection<MatrixRow> findByTaxonLabel(TaxonLabel taxonLabel) {
		Criteria c;

		c = getSession().createCriteria(MatrixRow.class);
		c.add(Restrictions.eq("taxonLabel", taxonLabel));
		return c.list();
	}

}
