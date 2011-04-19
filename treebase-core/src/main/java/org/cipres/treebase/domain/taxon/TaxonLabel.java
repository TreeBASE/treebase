package org.cipres.treebase.domain.taxon;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import javax.persistence.Transient;

import org.cipres.treebase.Constants;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.Annotation;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.IndexColumn;

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
	//private Submission mSubmission;
    private Set<TaxonLabelSet> mTaxonLabelSet;  
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
	@Fetch(FetchMode.JOIN)
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
	@Fetch(FetchMode.JOIN)
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
	  
	//@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	//@JoinTable(name = "sub_taxonlabel", 
	//		joinColumns = @JoinColumn(name = "taxonlabel_id"),
	//        inverseJoinColumns = @JoinColumn(name="submission_id")

	//)
	//public Submission getSubmission() {
		
	//	return mSubmission;		
	//}

	/**
	 * Set the Submission field.
	 */
	//public void setSubmission(Submission pNewSubmission) {
	//	mSubmission = pNewSubmission;
	//}
	
	
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
	 * Return the TaxonLabelSet field.
	 * 
	 * @return List<PhyloTaxonLabel>
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "TaxonLabelSET_TaxonLabel", joinColumns = {@JoinColumn(name = "TaxonLabel_ID")}, inverseJoinColumns = @JoinColumn(name = "TaxonLabelSet_ID"))
	protected Set<TaxonLabelSet> getTaxonLabelSet() {
		return mTaxonLabelSet;
	}


	/**
	 * Set the TaxonLabelSet field.
	 */
	protected void setTaxonLabelSet(Set<TaxonLabelSet> pTaxonLabelSet) {
		mTaxonLabelSet = pTaxonLabelSet;
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
	
	@Transient
	public List<Annotation> getAnnotations() {
		List<Annotation> annotations = super.getAnnotations();
		try {
			TaxonVariant tv = getTaxonVariant();
			if ( null != tv ) {
				if ( null != tv.getNamebankID() ) {
					annotations.add(new Annotation(Constants.SKOSURI, "skos:closeMatch", URI.create(Constants.uBioBase+tv.getNamebankID())));					
					String fullName = tv.getFullName();
					if ( ! getLabel().equals(fullName) ) {
						annotations.add(new Annotation(Constants.SKOSURI, "skos:altLabel",fullName));
					}
					if ( null != getNcbiTaxID() ) {
						annotations.add(new Annotation(Constants.SKOSURI, "skos:closeMatch", URI.create(String.format(Constants.NCBITaxonomyFormat, getNcbiTaxID()))));
						Taxon taxon = tv.getTaxon();
						String taxonName = taxon.getLabel();
						if ( ! fullName.equals(taxonName) ) {
							annotations.add(new Annotation(Constants.SKOSURI, "skos:prefLabel",taxonName));
						}
						Integer tb1LegacyID = taxon.getTB1LegacyId();
						if ( null != tb1LegacyID && taxon.getId() != tb1LegacyID.longValue() ) {
							annotations.add(new Annotation(Constants.TBTermsURI, "tb:identifier.taxon.tb1", tb1LegacyID.longValue()));
						}						
					}					
				}
				Integer tb1TvId = tv.getTB1LegacyId();
				if ( null != tb1TvId && tv.getId() != tb1TvId.longValue() ) {
					annotations.add(new Annotation(Constants.TBTermsURI, "tb:identifier.taxonVariant.tb1", tb1TvId.longValue() ));
				}	
				annotations.add(new Annotation(Constants.TBTermsURI, "tb:identifier.taxonVariant", tv.getId()));
				Taxon taxon = tv.getTaxon();
				if ( null != taxon ) {
					annotations.add(new Annotation(Constants.TBTermsURI, "tb:identifier.taxon", taxon.getId()));
				}
			}
		}
		catch ( Exception e) {
			e.printStackTrace();
		}
		return annotations;
	}	
	
	@Transient
	public String getLabel() {
		return getTaxonLabel();
	}
	
	@Transient
	public Study getContext() {
		return getStudy();
	}
}
