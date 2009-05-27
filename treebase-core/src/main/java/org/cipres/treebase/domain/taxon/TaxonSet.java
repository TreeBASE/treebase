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
