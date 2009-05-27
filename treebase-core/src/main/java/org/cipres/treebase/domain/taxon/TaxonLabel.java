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

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * TaxonLabel usually associates with one Taxon. If it does not, it usually means the referred
 * matrix data contains multiple taxa data.
 * 
 * A TaxonLabel is associated with one specific Study, it does not applie to all the studies.
 * 
 * Created on Mar 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "TAXONLABEL")
@AttributeOverride(name = "id", column = @Column(name = "TAXONLABEL_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "taxonCache")
@BatchSize(size = 40)
public class TaxonLabel extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private String mTaxonLabel;
	private boolean mAttemptedLinking;

	private TaxonVariant mTaxonVariant;
	private Study mStudy;

	/**
	 * Constructor.
	 */
	public TaxonLabel() {
		super();
	}

	/**
	 * Constructor.
	 */
	public TaxonLabel(String pLabelStr) {
		super();
		
		setTaxonLabel(pLabelStr);
	}

	/**
	 * Return the TaxonLabel field.
	 * 
	 * @return String
	 */
	@Column(name = "TaxonLabel", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTaxonLabel() {
		return mTaxonLabel;
	}

	/**
	 * Set the TaxonLabel field.
	 */
	public void setTaxonLabel(String pNewTaxonLabel) {
		mTaxonLabel = pNewTaxonLabel;
	}

	/**
	 * Whether we have tried to link this taxon label to a taxon. If the value is true but there is
	 * no associated TaxonVariant object, it indicates the attempted linking effort was failed.
	 * 
	 * @return boolean
	 */
	@Column(name = "LINKED")
	public boolean getAttemptedLinking() {
		return mAttemptedLinking;
	}

	/**
	 * Set the Linked field.
	 */
	public void setAttemptedLinking(Boolean pAttempted) {
		if (pAttempted == null) {
			mAttemptedLinking = false;
		} else {
			mAttemptedLinking = pAttempted;
		}
	}

	// /**
	// * Return the Taxon field.
	// *
	// * @return Taxon
	// */
	// @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	// @JoinColumn(name = "TAXON_ID", nullable = true)
	// public Taxon getTaxon() {
	// return mTaxon;
	// }
	//
	// /**
	// * Set the Taxon field.
	// */
	// public void setTaxon(Taxon pNewTaxon) {
	// mTaxon = pNewTaxon;
	// }
	//

	/**
	 * Return the TaxonVariant field.
	 * 
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "TAXONVARIANT_ID", nullable = true)
	public TaxonVariant getTaxonVariant() {
		return mTaxonVariant;
	}

	/**
	 * Set the TaxonVariant field.
	 */
	public void setTaxonVariant(TaxonVariant pNewTaxonVariant) {
		mTaxonVariant = pNewTaxonVariant;
	}

	/**
	 * Return the Study field.
	 * 
	 * @return Study
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID", nullable = true)
	@Index(name = "TLABEL_STUDY_IDX")
	public Study getStudy() {
		return mStudy;
	}

	/**
	 * Set the Study field.
	 */
	public void setStudy(Study pNewStudy) {
		mStudy = pNewStudy;
	}
	
	/**
	 * Return the submission this taxonLabel inhabits
	 * 
	 * @return the submission
	 * @author mjd 20080929
	 */
	@Transient
	public Submission getSubmission() {
		Study s = getStudy();
		return s == null ? null : s.getSubmission();		
	}

	/**
	 * Return the taxon name if it available.
	 */
	@Transient
	public String getTaxonName() {
		if (getTaxonVariant() != null && getTaxonVariant().getTaxon() != null) {
			return getTaxonVariant().getTaxon().getName();
		}
		return null;
	}

	/**
	 * Return the taxon name if it available.
	 */
	@Transient
	public Integer getNcbiTaxID() {
		TaxonVariant variant = getTaxonVariant();
		Integer result = null;
		if ( variant != null ) {
			Taxon taxon = variant.getTaxon();
			if ( taxon != null ) {
				result = taxon.getNcbiTaxId();
			}
		}
		return result;
	}
}
