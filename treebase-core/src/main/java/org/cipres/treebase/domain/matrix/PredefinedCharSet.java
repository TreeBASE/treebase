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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * PredefinedCharSet.java
 * 
 * Created on May 5, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("P")
public class PredefinedCharSet extends CharSet {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public PredefinedCharSet() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.CharSet#getColumns(org.cipres.treebase.domain.matrix.CharacterMatrix)
	 */
	@Override
	public Collection<ColumnRange> getColumns(CharacterMatrix pMatrix) {
		// FIXME: getColumns
		return null;
	}

}
