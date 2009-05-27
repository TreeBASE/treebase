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

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * A TaxonVariant is usually corresponding to one uBio namebank id object.
 * 
 * Created on Feb 14, 2006
 * 
 * @author Jin Ruan
 * 
 */

@Entity
@Table(name = "TAXONVARIANT")
@AttributeOverride(name = "id", column = @Column(name = "TAXONVARIANT_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "taxonCache")
@BatchSize(size = 20)
public class TaxonVariant extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private Long mNamebankID;
	private String mName;
	private String mFullName;
	private String mLexicalQualifier;
	private Integer mTB1LegacyId; // mjd 20090415

	private Taxon mTaxon;

	/**
	 * Constructor.
	 */
	public TaxonVariant() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param nameBankID
	 * @param nameString
	 * @param fullNameString
	 * @param lexicalQualifier
	 */
	public TaxonVariant(
		Long nameBankID,
		String nameString,
		String fullNameString,
		String lexicalQualifier) {

		super();

		setNamebankID(nameBankID);
		setName(nameString);
		setFullName(fullNameString);
		setLexicalQualifier(lexicalQualifier);

	}

	/**
	 * uBio name variant short style.
	 * 
	 * @return the nameString
	 */
	@Column(name = "Name", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getName() {
		return mName;
	}

	/**
	 * @param pNameString the nameString to set
	 */
	private void setName(String pNameString) {
		mName = pNameString;
	}

	/**
	 * uBio name bank id.
	 * 
	 * @return
	 */
	@Column(name = "NamebankID", nullable = true)
	@Index(name = "TVARI_NBID_IDX") // index name length: 18 char
	public Long getNamebankID() {
		return mNamebankID;
	}

	/**
	 * @param pNameBankID
	 */
	private void setNamebankID(Long pNameBankID) {
		mNamebankID = pNameBankID;
	}

	/**
	 * uBio name variant long style.
	 * 
	 * @return
	 */
	@Column(name = "FullName", nullable = true, length = TBPersistable.COLUMN_LENGTH_STRING)
	@Index(name = "TVARI_FNAME_IDX")
	public String getFullName() {
		return mFullName;
	}

	/**
	 * @param pFullNameString
	 */
	private void setFullName(String pFullNameString) {
		mFullName = pFullNameString;
	}

	/**
	 * uBio qualifier (e.g. canonical form)
	 * 
	 * @return
	 */
	@Column(name = "LexicalQualifier", nullable = true, length = TBPersistable.COLUMN_LENGTH_50)
	public String getLexicalQualifier() {
		return mLexicalQualifier;
	}

	/**
	 * @param pLexicalQualifier
	 */
	private void setLexicalQualifier(String pLexicalQualifier) {
		mLexicalQualifier = pLexicalQualifier;
	}

	/**
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "TAXON_ID", nullable = false)
	@Index(name = "TVARI_TAXON_IDX")
	public Taxon getTaxon() {
		return mTaxon;
	}

	/**
	 * @param pTaxon the taxon to set
	 */
	public void setTaxon(Taxon pTaxon) {
		mTaxon = pTaxon;
	}

	@Column(name = "TB1LegacyId", nullable = true)
	public Integer getTB1LegacyId() {
		return mTB1LegacyId;
	}

	public void setTB1LegacyId(Integer legacyId) {
		mTB1LegacyId = legacyId;
	}
}
