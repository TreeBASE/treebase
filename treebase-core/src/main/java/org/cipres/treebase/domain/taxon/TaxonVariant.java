
package org.cipres.treebase.domain.taxon;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

import org.cipres.treebase.Constants;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.Annotation;
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
	@Fetch(FetchMode.JOIN)
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
	
	@Transient
	public String getLabel() {
		return getFullName();
	}
	
	@Transient
	public List<Annotation> getAnnotations() {
		List<Annotation> annotations = super.getAnnotations();
		try {
			if ( null != getTB1LegacyId() ) {
				annotations.add(new Annotation(Constants.TBTermsURI, "tb:identifier.taxonVariant.tb1", getTB1LegacyId()));
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return annotations;
	}	
}
