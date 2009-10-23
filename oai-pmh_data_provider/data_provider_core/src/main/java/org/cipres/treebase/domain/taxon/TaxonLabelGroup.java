/*
 * CIPRES Copyright (c) 2005- 2006, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
