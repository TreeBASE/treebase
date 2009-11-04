package org.cipres.treebase.domain.taxon;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TaxonLabelGroup.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TAXONLABELGROUP")
@AttributeOverride(name = "id", column = @Column(name = "TAXONLABELGROUP_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "taxonCache")
public class TaxonLabelGroup extends AbstractPersistedObject {

	private String mTitle;

	private TaxonLabelPartition mPartition;

	private Collection<TaxonLabel> mTaxonLabelSet = new ArrayList<TaxonLabel>();

	/**
	 * Constructor.
	 */
	public TaxonLabelGroup() {
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
	 * Append a new Taxon label to the Set.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pLabel TaxonLabel
	 */
	public void addTaxonLabel(TaxonLabel pLabel) {
		if (pLabel != null) {
			getTaxonLabelSet().add(pLabel);
		}
	}

	/**
	 * Remove the Taxon Label.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pLabel TaxonLabel
	 */
	public void removeTaxonLabel(TaxonLabel pLabel) {
		if (pLabel != null) {
			getTaxonLabelSet().remove(pLabel);
		}
	}

	/**
	 * Return the TaxonLabel field.
	 * 
	 * @return Collection<TaxonLabel>
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "TAXONLABELGROUP_TAXONLABEL", joinColumns = {@JoinColumn(name = "TAXONLABELGROUP_ID")}, inverseJoinColumns = @JoinColumn(name = "TAXONLABEL_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "taxonCache")
	protected Collection<TaxonLabel> getTaxonLabelSet() {
		return mTaxonLabelSet;
	}

	/**
	 * Set the TaxonLabel field.
	 */
	protected void setTaxonLabelSet(Collection<TaxonLabel> pLabelSet) {
		mTaxonLabelSet = pLabelSet;
	}

	/**
	 * Return the Partition field.
	 * 
	 * @return TaxonLabelPartition mPartition
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TAXONLABELPARTITION_ID")
	public TaxonLabelPartition getPartition() {
		return mPartition;
	}

	/**
	 * Set the Partition field.
	 */
	public void setPartition(TaxonLabelPartition pNewPartition) {
		mPartition = pNewPartition;
	}

}
