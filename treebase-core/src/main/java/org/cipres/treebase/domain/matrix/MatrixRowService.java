
package org.cipres.treebase.domain.matrix;

import org.cipres.treebase.service.AbstractService;

/**
 * MatrixRowService.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface MatrixRowService extends AbstractService {

	/**
	 * Return a Matrix row object by id.
	 * 
	 * @param pMatrixRowID
	 * @return
	 */
	MatrixRow findByID(Long pMatrixRowID);

}
