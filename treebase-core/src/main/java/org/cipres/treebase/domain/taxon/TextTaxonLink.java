package org.cipres.treebase.domain.taxon;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.cipres.treebase.domain.TBPersistable;

/**
 * TextTaxonLink.java
 * 
 * Created on Mar 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("T")
public class TextTaxonLink extends TaxonLink {

	private String mForeignTaxonID;

	/**
	 * Constructor.
	 */
	public TextTaxonLink() {
		super();
	}

	/**
	 * Return the ForeignTaxonID field.
	 * 
	 * @return String
	 */
	@Column(name = "ForeignTaxonID", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getForeignTaxonID() {
		return mForeignTaxonID;
	}

	/**
	 * Set the ForeignTaxonID field.
	 */
	public void setForeignTaxonID(String pNewForeignTaxonID) {
		mForeignTaxonID = pNewForeignTaxonID;
	}

}
