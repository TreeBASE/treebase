
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

		c = getSessionFactory().getCurrentSession().createCriteria(MatrixRow.class);
		c.createCriteria("taxonLabel").add(Restrictions.eq("taxonLabel", pTaxonLabel));
		return c.list();
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.MatrixRowHome#findByTaxonLabel(org.cipres.treebase.domain.taxon.TaxonLabel)
	 */
	public Collection<MatrixRow> findByTaxonLabel(TaxonLabel taxonLabel) {
		Criteria c;

		c = getSessionFactory().getCurrentSession().createCriteria(MatrixRow.class);
		c.add(Restrictions.eq("taxonLabel", taxonLabel));
		return c.list();
	}

}
