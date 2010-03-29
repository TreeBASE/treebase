
package org.cipres.treebase.domain.matrix;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.study.Study;

/**
 * MatrixHome.java
 * 
 * Created on Apr 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface MatrixHome extends DomainHome {

	/**
	 * Load a matrix with full data: column, row, and elements.
	 * 
	 * @param pMatrixID
	 * @return
	 */
	Matrix loadMatrixEagerFetch(Long pMatrixID);

	/**
	 * Find all matrices for the given studies.
	 * 
	 * @param pStudies
	 * @return
	 */
	Collection<Matrix> findByStudies(Collection<Study> pStudies);

	/**
	 * Delete the instances from the database.
	 * 
	 * @param pMatriices
	 */
	void delete(Collection<Matrix> pMatrices);

	/**
	 * Delete the instance from the database. Handles bi-directional relationships and cascade
	 * delete.
	 * 
	 * @param pMatrix
	 */
	void delete(Matrix pMatrix);

	/**
	 * Delete all matrix columns.
	 * 
	 * @param pMatrix
	 */
	void cascadeDeleteColumns(CharacterMatrix pMatrix);
	void cascadeDelete(CharacterMatrix pMatrix);
	/**
	 * Delete a collection of matrix rows.
	 * 
	 * @param pRows
	 */
	void cascadeDeleteRows(Collection<MatrixRow> pRows);
	void cascadeDeleteRows(CharacterMatrix pMatrix);
	/**
	 * Delete a collection of ancestral state sets.
	 * 
	 * @param pAncStateSets
	 */
	void cascadeDeleteAncStateSet(Set<AncStateSet> pAncStateSets);

	/**
	 * Delete a collection of character weight sets.
	 * 
	 * @param pWeightSets
	 */
	void cascadeDeleteCharWeightSet(Set<CharWeightSet> pWeightSets);

	/**
	 * Delete a collection of genetic code sets.
	 * 
	 * @param pCodeSets
	 */
	void cascadeDeleteCodeSet(Set<GeneticCodeSet> pCodeSets);

	/**
	 * Delete a collection of type sets.
	 * 
	 * @param pTypeSets
	 */
	void cascadeDeleteTypeSet(Set<TypeSet> pTypeSets);

	/**
	 * Persist all elements. 
	 * 
	 * @param pCharacterMatrix
	 */
	void cascadePersistElements(CharacterMatrix pCharacterMatrix);

	/**
	 * Delete all elements.
	 * 
	 * @param pMatrix
	 */
	void cascadeDeleteElements(CharacterMatrix pMatrix);

	/**
	 * Save the list of matrices to database. 
	 * 
	 * Performance, Performance, Performance!
	 * 
	 * @param pMatrices
	 */
	void persistAll(List<Matrix> pMatrices);

	
	/**
	 * @param title
	 * @return the set of matrices with this title
	 * @author mjd
	 */
	Set<Matrix> findMatricesByTitle(String title);

	/**
	 * Get all matrices by name in the specified submission.
	 * 
	 * @param pSubmissionId
	 * @param pFileName
	 * @return
	 */
	Collection<Matrix> findByNexusFileName(Long pSubmissionId, String pFileName);

	Collection<Matrix> findByNexusFile(String fn);
	
	/**
	 * Return a Matrix object by its TB1MatrixID.
	 * 
	 * @param pTB1MatrixID
	 * @return
	 */
	Collection<Matrix> findByTB1MatrixID(String pTB1MatrixID);

	/**
	 * Find a list strings for displaying the table from column start to column end.
	 * One string for each row.
	 * 
	 * @param pMatrix
	 * @param pStart
	 * @param pEnd
	 * @return
	 */
	List<String> findRowAsString(CharacterMatrix pMatrix, int pStart, int pEnd);

	/**
	 * Update the published flag for all matrices in the study. 
	 * Returns the updated count. 
	 * 
	 * @param pStudy
	 * @param pPublished
	 * @return
	 */
	int updatePublishedFlagByStudy(Study pStudy, boolean pPublished);

	/**
	 * Find the matrix kind by its description.
	 * 
	 * @return MatrixKind
	 */
	MatrixKind findKindByDescription(String pDescription);

}
