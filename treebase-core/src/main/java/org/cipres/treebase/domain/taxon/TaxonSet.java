package org.cipres.treebase.domain.taxon;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.IndexColumn;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TaxonSet.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TAXONSET")
@AttributeOverride(name = "id", column = @Column(name = "TAXONSET_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "taxonCache")
public class TaxonSet extends AbstractPersistedObject {

	private String mTitle;

	private List<Taxon> mTaxonList = new ArrayList<Taxon>();

	/**
	 * Constructor.
	 */
	public TaxonSet() {
		super();
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String
	 */
	@Column(name = "Title", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Set the Title field.
	 */
	public void setTitle(String pNewTitle) {
		mTitle = pNewTitle;
	}

	/**
	 * Append a new Taxon to the end of the list.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTaxon Taxon
	 */
	public void addTaxon(Taxon pTaxon) {
		if (pTaxon != null) {
			getTaxonList().add(pTaxon);
		}
	}

	/**
	 * Remove the Taxon.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTaxon Taxon
	 */
	public void removeTaxon(Taxon pTaxon) {
		if (pTaxon != null) {
			getTaxonList().remove(pTaxon);
		}
	}

	/**
	 * Return the TaxonList field.
	 * 
	 * @return List<Taxon>
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "TAXONSET_TAXON", joinColumns = {@JoinColumn(name = "TAXONSET_ID")}, inverseJoinColumns = @JoinColumn(name = "TAXON_ID"))
	@IndexColumn(name = "TAXON_ORDER")
	protected List<Taxon> getTaxonList() {
		return mTaxonList;
	}

	/**
	 * Set the TaxonList field.
	 */
	protected void setTaxonList(List<Taxon> pTaxonList) {
		mTaxonList = pTaxonList;
	}

}
