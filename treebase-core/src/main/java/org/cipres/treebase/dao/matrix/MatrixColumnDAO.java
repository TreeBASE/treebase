
package org.cipres.treebase.dao.matrix;

import java.util.ArrayList;
import java.util.Collection;

import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.matrix.MatrixColumn;
import org.cipres.treebase.domain.matrix.MatrixColumnHome;

/**
 * MatrixColumnDAO.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class MatrixColumnDAO extends AbstractDAO implements MatrixColumnHome {

	/**
	 * Constructor.
	 */
	public MatrixColumnDAO() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixColumnHome#deleteColumns(java.util.Collection)
	 */
	public void deleteColumns(Collection<MatrixColumn> pColumns) {
		if (pColumns == null && pColumns.isEmpty()) {
			return;
		}

		// create a new collection to avoid concurrent error
		Collection<MatrixColumn> columns = new ArrayList<MatrixColumn>(pColumns);
		for (MatrixColumn column : columns) {
			deleteColumn(column);
		}
	}

	/**
	 * 
	 * @param pColumn
	 */
	private void deleteColumn(MatrixColumn pColumn) {
		if (pColumn != null && pColumn.getId() != null) {

			// bi-directional relationship management:
			// matrix -> matrixColumn
			if (pColumn.getMatrix() != null) {
				pColumn.getMatrix().removeColumn(pColumn);
			}

			// Cascade delete:
			// column -> item definitions
			// character
			pColumn.getItemDefinitions().clear();

			// FIXME: cascade delete character
			// do we need a delete service to remove teh orphon characters??
			// or here we need to call characterHome.isRemoveable()

			getHibernateTemplate().delete(pColumn);
		}

	}

}
