package org.cipres.treebase.domain.taxon;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TaxonLabelPartition.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "TAXONLABELPARTITION")
@AttributeOverride(name = "id", column = @Column(name = "TAXONLABELPARTITION_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "taxonCache")
public class TaxonLabelPartition extends AbstractPersistedObject {

	private static final long serialVersionUID = 8734164640178526143L;

	private String mTitle;

	private Collection<TaxonLabelGroup> mTaxonLabelGroups = new ArrayList<TaxonLabelGroup>();

	/**
	 * Constructor.
	 */
	public TaxonLabelPartition() {
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
	 * Append a new TaxonLabelGroups to the Set.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTaxonLabelGroup TaxonLabelGroup
	 */
	public void addTaxonLabelGroup(TaxonLabelGroup pTaxonLabelGroup) {
		if (pTaxonLabelGroup != null) {
			getTaxonLabelGroups().add(pTaxonLabelGroup);
			pTaxonLabelGroup.setPartition(this);
		}
	}

	/**
	 * Remove the TaxonLabelGroups.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTaxonLabelGroup TaxonLabel
	 */
	public void removeTaxonLabelGroup(TaxonLabelGroup pTaxonLabelGroup) {
		if (pTaxonLabelGroup != null) {
			getTaxonLabelGroups().remove(pTaxonLabelGroup);
			pTaxonLabelGroup.setPartition(null);
		}
	}

	/**
	 * Return the TaxonLabel GroupSet field.
	 * 
	 * @return Set<TaxonLabel>
	 */
	@OneToMany(mappedBy = "partition", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	// @CollectionId
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "taxonCache")
	protected Collection<TaxonLabelGroup> getTaxonLabelGroups() {
		return mTaxonLabelGroups;
	}

	/**
	 * Set the TaxonLabelGroupsSet field.
	 */
	protected void setTaxonLabelGroups(Collection<TaxonLabelGroup> pTaxonLabelGroups) {
		mTaxonLabelGroups = pTaxonLabelGroups;
	}

}
