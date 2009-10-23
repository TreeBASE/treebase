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
