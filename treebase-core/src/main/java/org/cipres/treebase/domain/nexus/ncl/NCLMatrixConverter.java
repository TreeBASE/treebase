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

package org.cipres.treebase.domain.nexus.ncl;

import java.util.List;

import org.cipres.CipresIDL.api1.DataMatrix;
import org.cipres.CipresIDL.api1.DiscreteDatatypes;

import org.cipres.treebase.domain.matrix.DiscreteCharState;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixDataType;
import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;

/**
 * NCLMatrixConverter.java Conert a NCL matrix data structure (actually, Cipres PhyloDataSet Matrix
 * Data) to the TreeBASE II matrix objects.
 * 
 * Created on Jun 13, 2006
 * 
 * @author Jin Ruan
 * 
 */
public abstract class NCLMatrixConverter {
	protected static final int GAP_STATE_VALUE = -1;
	protected static final String UNKNOWN_STATE_STRING = "" + DiscreteCharState.MISSING_SYMBOL;

	private DataMatrix mCipresMatrix;
	private MatrixDataType mMatrixDataType;
	private MatrixDataTypeHome mMatrixDataTypeHome;

	private List<TaxonLabel> mTaxonLables;

	/**
	 * Constructor.
	 */
	protected NCLMatrixConverter() {
		super();
	}

	/**
	 * Return a matrix object based on the Cipres DataMatrix.
	 * 
	 * @param pCipresMatrix
	 * @param pTaxaList
	 * @param pDataTypeHome
	 * @return
	 */
	public static Matrix convertMatrix(
		DataMatrix pCipresMatrix,
		List<TaxonLabel> pTaxaList,
		MatrixDataTypeHome pDataTypeHome) {

		Matrix matrix = null;
		if (pCipresMatrix == null) {
			return matrix;
		}
		NCLMatrixConverter converter = getConverter(pCipresMatrix.m_datatype, pDataTypeHome);
		converter.setMatrixDataTypeHome(pDataTypeHome);
		converter.setCipresMatrix(pCipresMatrix);
		converter.setTaxonLabels(pTaxaList);

		return converter.convertMatrix();
	}

	/**
	 * 
	 * @param pCipresDatatype
	 * @param pDataTypeHome
	 * @return
	 */
	private static NCLMatrixConverter getConverter(
		DiscreteDatatypes pCipresDatatype,
		MatrixDataTypeHome pDataTypeHome) {

		NCLMatrixConverter converter = null;
		if (pCipresDatatype.value() == DiscreteDatatypes._DNA_DATATYPE) {
			converter = new NCLStandardMatrixConverter();

			MatrixDataType dataType = pDataTypeHome
				.findByDescription(MatrixDataType.MATRIX_DATATYPE_DNA);
			converter.setMatrixDataType(dataType);
		} else if (pCipresDatatype.value() == DiscreteDatatypes._RNA_DATATYPE) {
			converter = new NCLStandardMatrixConverter();

			MatrixDataType dataType = pDataTypeHome
				.findByDescription(MatrixDataType.MATRIX_DATATYPE_RNA);
			converter.setMatrixDataType(dataType);
		} else if (pCipresDatatype.value() == DiscreteDatatypes._AA_DATATYPE) {
			converter = new NCLStandardMatrixConverter();

			MatrixDataType dataType = pDataTypeHome
				.findByDescription(MatrixDataType.MATRIX_DATATYPE_PROTEIN);
			converter.setMatrixDataType(dataType);
		} else if (pCipresDatatype.value() == DiscreteDatatypes._CODON_DATATYPE) {
			converter = new NCLStandardMatrixConverter();

			// TODO: per Mark H. CODON should be depreciated in NCL.
			MatrixDataType dataType = pDataTypeHome
				.findByDescription(MatrixDataType.MATRIX_DATATYPE_DNA);
			converter.setMatrixDataType(dataType);
		} else if (pCipresDatatype.value() == DiscreteDatatypes._GENERIC_DATATYPE) {
			converter = new NCLStandardMatrixConverter();

			MatrixDataType dataType = pDataTypeHome
				.findByDescription(MatrixDataType.MATRIX_DATATYPE_STANDARD);
			converter.setMatrixDataType(dataType);
		} else {
			// TODO: other types: nucleotide, distance, continuous, ...

			// a missing datatype means use standard type:
			converter = new NCLStandardMatrixConverter();

			MatrixDataType dataType = pDataTypeHome
				.findByDescription(MatrixDataType.MATRIX_DATATYPE_STANDARD);
			converter.setMatrixDataType(dataType);
		}

		return converter;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	protected abstract Matrix convertMatrix();

	/**
	 * Return the MatrixDataTypeHOme field.
	 * 
	 * @return MatrixDataTypeHome mMatrixDataTypeHome
	 */
	protected MatrixDataTypeHome getMatrixDataTypeHome() {
		return mMatrixDataTypeHome;
	}

	/**
	 * Set the MatrixDataTypeHOme field.
	 */
	private void setMatrixDataTypeHome(MatrixDataTypeHome pNewMatrixDataTypeHOme) {
		mMatrixDataTypeHome = pNewMatrixDataTypeHOme;
	}

	/**
	 * Return the MatrixDataType field.
	 * 
	 * @return MatrixDataType mMatrixDataType
	 */
	protected MatrixDataType getMatrixDataType() {
		return mMatrixDataType;
	}

	/**
	 * Set the MatrixDataType field.
	 */
	private void setMatrixDataType(MatrixDataType pNewMatrixDataType) {
		mMatrixDataType = pNewMatrixDataType;
	}

	/**
	 * Return the TaxaList field.
	 * 
	 * @return List<TaxonLabel> mTaxonLables
	 */
	protected List<TaxonLabel> getTaxonLables() {
		return mTaxonLables;
	}

	/**
	 * Set the TaxaList field.
	 */
	private void setTaxonLabels(List<TaxonLabel> pTaxonLabels) {
		mTaxonLables = pTaxonLabels;
	}

	/**
	 * Return the CipresMatrix field.
	 * 
	 * @return DataMatrix mCipresMatrix
	 */
	protected DataMatrix getCipresMatrix() {
		return mCipresMatrix;
	}

	/**
	 * Set the CipresMatrix field.
	 */
	private void setCipresMatrix(DataMatrix pNewCipresMatrix) {
		mCipresMatrix = pNewCipresMatrix;
	}

}
