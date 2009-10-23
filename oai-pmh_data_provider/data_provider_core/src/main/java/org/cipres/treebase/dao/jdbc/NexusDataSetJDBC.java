/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */

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
