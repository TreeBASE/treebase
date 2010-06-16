package org.cipres.treebase.domain.taxon;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TaxonAuthority.java
 * 
 * Created on Mar 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TAXONAUTHORITY")
@AttributeOverride(name = "id", column = @Column(name = "TAXONAUTHORITY_ID"))
public class TaxonAuthority extends AbstractPersistedObject {

	private String mName;
	private String mDescription;
	private String mConnectionStr;

	/**
	 * Constructor.
	 */
	public TaxonAuthority() {
		super();
	}

	/**
	 * Return the ConnectionStr field.
	 * 
	 * @return String
	 */
	@Column(name = "ConnectionStr", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING_1K)
	public String getConnectionStr() {
		return mConnectionStr;
	}

	/**
	 * Set the ConnectionStr field.
	 */
	public void setConnectionStr(String pNewConnectionStr) {
		mConnectionStr = pNewConnectionStr;
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String
	 */
	@Override
	@Column(name = "Description", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING_NOTES)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Set the Description field.
	 */
	public void setDescription(String pNewDescription) {
		mDescription = pNewDescription;
	}

	/**
	 * Return the Name field.
	 * 
	 * @return String
	 */
	@Column(name = "Name", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getName() {
		return mName;
	}

	/**
	 * Set the Name field.
	 */
	public void setName(String pNewName) {
		mName = pNewName;
	}

}
