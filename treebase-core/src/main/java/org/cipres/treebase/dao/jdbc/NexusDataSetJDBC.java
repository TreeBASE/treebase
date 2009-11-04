
package org.cipres.treebase.dao.jdbc;

import java.util.ArrayList;
import java.util.Collection;

import mesquite.lib.characters.CharacterData;

import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.nexus.mesquite.MesquiteMatrixConverter;

/**
 * NexusDataSetJDBC.java
 * Holds the data to be processed directly through the JDBC interface. 
 *  
 * Created on Feb 26, 2008
 * 
 * @author Jin Ruan
 */
public class NexusDataSetJDBC {

	private Collection<MatrixJDBC> mMatrixJDBCList = new ArrayList<MatrixJDBC>();

	/**
	 * Constructor.
	 */
	public NexusDataSetJDBC() {
		super();
	}

	/**
	 * Return the MatrixJDBCList field.
	 * 
	 * @return Collection<MatrixJDBC> 
	 */
	public Collection<MatrixJDBC> getMatrixJDBCList() {
		return mMatrixJDBCList;
	}

	/**
	 * Return a list of all character matrices. 
	 * 
	 * @return
	 */
	public Collection<CharacterMatrix> getAllMatrices() {
		Collection<CharacterMatrix> matrices = new ArrayList<CharacterMatrix>();
		
		for (MatrixJDBC matrixJDBC : getMatrixJDBCList()) {
			matrices.add(matrixJDBC.getCharacterMatrix());
		}
		
		return matrices;
	}
	/**
	 * Add a matrix entry. 
	 * 
	 * @param pMatrix
	 * @param pMesquiteData
	 */
	public void addData(CharacterMatrix pMatrix, CharacterData pMesquiteData, MesquiteMatrixConverter pConverter) {
		MatrixJDBC m = MatrixJDBC.factory(pMatrix, pMesquiteData, pConverter);
		
		getMatrixJDBCList().add(m);
	}

}
