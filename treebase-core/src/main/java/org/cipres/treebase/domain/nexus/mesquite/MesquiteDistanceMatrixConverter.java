
package org.cipres.treebase.domain.nexus.mesquite;

import java.sql.Connection;

import mesquite.lib.characters.CharacterData;

import org.cipres.treebase.dao.jdbc.MatrixJDBC;
import org.cipres.treebase.domain.matrix.DistanceMatrix;
import org.cipres.treebase.domain.matrix.MatrixRow;

/**
 * MesquiteDistanceMatrixConverter.java
 * 
 * Created on Feb 13, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class MesquiteDistanceMatrixConverter extends MesquiteMatrixConverter {

	/**
	 * Constructor.
	 */
	public MesquiteDistanceMatrixConverter() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.mesquite.MesquiteMatrixConverter#createMatrix(mesquite.lib.characters.CharacterData)
	 */
	@Override
	protected MatrixJDBC createMatrix(CharacterData pMesqMatrix) {
		// TODO: createMatrix
		DistanceMatrix m = new DistanceMatrix();

		return null;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.mesquite.MesquiteMatrixConverter#addRowElements(org.cipres.treebase.domain.matrix.MatrixRow,
	 *      int, mesquite.lib.characters.CharacterData)
	 */
	@Override
	protected void addRowElements(MatrixRow pRow, int pRowIndex, CharacterData pMesqMatrix) {
	// TODO: addRowElements

	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.nexus.mesquite.MesquiteMatrixConverter#processMatrixElements(org.cipres.treebase.dao.jdbc.MatrixJDBC, java.sql.Connection)
	 */
	@Override
	public void processMatrixElements(MatrixJDBC pMatrixJDBC, Connection pCon) {
		//FIXME: processMatrixElements
		
	}

}
