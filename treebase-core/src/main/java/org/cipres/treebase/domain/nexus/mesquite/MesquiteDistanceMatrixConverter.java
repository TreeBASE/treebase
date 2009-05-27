/*
 * Copyright 2007 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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
