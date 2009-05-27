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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.cipres.treebase.TreebaseUtil;

import mesquite.lib.StringUtil;

/**
 * StandardMatrix
 * 
 * Created on Feb 23, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("S")
public class StandardMatrix extends DiscreteMatrix {

	private static final long serialVersionUID = 1L;

	private boolean mCaseSensitive;

	/**
	 * Constructor.
	 */
	public StandardMatrix() {
		super();

	}

	/**
	 * Return the CaseSensitive field.
	 * 
	 * @return boolean
	 */
	@Column(name = "CaseSensitive")
	public boolean isCaseSensitive() {
		return mCaseSensitive;
	}

	/**
	 * Set the CaseSensitive field.
	 */
	public void setCaseSensitive(boolean pNewCaseSensitive) {
		mCaseSensitive = pNewCaseSensitive;
	}

	/**
	 * Add CHARSTATELABELS section
	 * 
	 * @see org.cipres.treebase.domain.matrix.Matrix#generateFormatInfo(java.lang.StringBuilder)
	 */
	@Override
	protected StringBuilder generateFormatInfo(StringBuilder pBuilder) {
		super.generateFormatInfo(pBuilder);

		if (getDataType() != null && !getDataType().isSequence()) {
			int colCount = getColumns().size();
			StringBuilder charStateLabels = new StringBuilder();
			
			for (int i = 0; i < colCount; i++) {
				DiscreteChar aChar = (DiscreteChar) getColumns().get(i).getCharacter();
				String aCharLabelInfo = aChar.generateCharLabelsInfo(getSymbols());

				if (!TreebaseUtil.isEmpty(aChar.getDescription())
					|| !TreebaseUtil.isEmpty(aCharLabelInfo)) {
					charStateLabels.append("\t\t" + (i + 1) + "  ");

					if (!TreebaseUtil.isEmpty(aChar.getDescription())) {
						charStateLabels.append(StringUtil.tokenize(aChar.getDescription()));
					} else {
						charStateLabels.append(' ');
					}

					if (!TreebaseUtil.isEmpty(aCharLabelInfo)) {
						charStateLabels.append(" / ").append(aCharLabelInfo);
					}

					charStateLabels.append(',').append(TreebaseUtil.LINESEP);
				}
			}

			if (charStateLabels.length() > 1) {
				// Ok, there is charastate label defined, let's add it to nexus output:
				pBuilder.append("\tCHARSTATELABELS").append(TreebaseUtil.LINESEP);
				pBuilder.append(charStateLabels.toString()).append("\t\t;").append(
					TreebaseUtil.LINESEP);
			}
		}
		return pBuilder;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.DiscreteMatrix#cascadeDelete(org.cipres.treebase.domain.matrix.MatrixHome)
	 */
	// @Override
	// public void cascadeDelete(MatrixHome pMatrixHome) {
	// super.cascadeDelete(pMatrixHome);
	//
	// // Cascade delete: remove all rows:
	// pMatrixHome.cascadeDeleteRows(getRows());
	// }
}
