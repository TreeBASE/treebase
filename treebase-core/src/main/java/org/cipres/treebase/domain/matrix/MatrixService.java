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

package org.cipres.treebase.domain.matrix;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.service.AbstractService;

/**
 * MatrixService.java
 * 
 * Created on June 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface MatrixService extends AbstractService {

	/**
	 * Return a Matrix object by id. The Matrix object if fully inflated with column, row, and
	 * elements data.
	 * 
	 * @param pMatrixID
	 * @return
	 */
	Matrix findByID(Long pMatrixID);

	/**
	 * Delete a matrix and its assoicated objects.
	 * 
	 * Return true if the deletion is successful.
	 * 
	 * @param pMatrix
	 * @return
	 */
	boolean deleteMatrix(Matrix pMatrix);

	/**
	 * Find a list strings for displaying the table from column start to column end. One string for
	 * each row.
	 * 
	 * @param pMatrix
	 * @param pStart
	 * @param pEnd
	 * @return
	 */
	List<String> findRowAsString(CharacterMatrix pMatrix, int pStart, int pEnd);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#findMatricesByTitle(java.lang.String)
	 */

	public Set<Matrix> findMatricesByTitle(String title);

	public Collection<Matrix> findByNexusFile(String fn);

	public Collection<Matrix> findByStudies(Set<Study> results);
	public Collection<Matrix> findByStudy(Study pStudy);

}
