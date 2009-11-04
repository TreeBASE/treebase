
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
