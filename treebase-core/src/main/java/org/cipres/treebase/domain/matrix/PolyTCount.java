package org.cipres.treebase.domain.matrix;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * PolyTCount
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "POLYTCount")
@AttributeOverride(name = "id", column = @Column(name = "PolyTCount_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class PolyTCount extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;
	private String mDescription;

	/**
	 * Constructor.
	 */
	public PolyTCount() {
		super();
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String mDescription
	 */
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Set the Description field.
	 */
	@SuppressWarnings("unused")
	private void setDescription(String pNewDescription) {
		mDescription = pNewDescription;
	}

}
