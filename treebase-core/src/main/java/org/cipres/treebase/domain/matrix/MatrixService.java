
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
