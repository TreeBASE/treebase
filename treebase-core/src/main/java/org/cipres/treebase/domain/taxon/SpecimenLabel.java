package org.cipres.treebase.domain.taxon;

import static java.lang.Math.abs;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.TBPersistable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * This class describes a specimen. 
 * 
 * Note: 
 * At this time the class does not store the information in its own database table. Instead, the
 * information is stored into the RowSegment table. There is one to one relationship between the
 * RowSegment and the SpecimenLabel. 
 * 
 * Created on May 1, 2008
 * 
 * @author Jin Ruan
 * 
 */
//@Entity
//@Table(name = "SPECIMENLABEL")
//@AttributeOverride(name = "id", column = @Column(name = "SPECIMENLABEL_ID"))
@Embeddable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
//public class SpecimenLabel extends AbstractPersistedObject {
public class SpecimenLabel {

	private static final long serialVersionUID = -42118473885428569L;

	//See: http://www.biorepositories.org/
	private String mInstitutionAcronym;
	private String mCollectionCode;
	private String mCatalogNumber;
	
	private String mGenBankAccession;
	private String mOtherAccession;

	private Date mSampleDate;
	private String mCollector;
	private String mNotes;
	
	//Location info:
	private Double mLatitude;
	private Double mLongitude;
	private Double mElevation;
	private String mCountry;
	private String mState;
	private String mLocality;

	/**
	 * Constructor.
	 */
	public SpecimenLabel() {
		super();
	}

	/**
	 * Return the Locality field.
	 * 
	 * @return String 
	 */
	@Column(name = "Locality", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getLocality() {
		return mLocality;
	}

	/**
	 * Set the Locality field.
	 */
	public void setLocality(String pNewLocality) {
		mLocality = pNewLocality;
	}
	
	/**
	 * Return the State field.
	 * 
	 * @return String 
	 */
	@Column(name = "State", length = TBPersistable.COLUMN_LENGTH_50)
	public String getState() {
		return mState;
	}

	/**
	 * Set the State field.
	 */
	public void setState(String pNewState) {
		mState = pNewState;
	}
	
	/**
	 * Return the Country field.
	 * 
	 * @return String 
	 */
	@Column(name = "Country", length = TBPersistable.COLUMN_LENGTH_50)
	public String getCountry() {
		return mCountry;
	}

	/**
	 * Set the Country field.
	 */
	public void setCountry(String pNewCountry) {
		mCountry = pNewCountry;
	}
	
	/**
	 * Return the Collector field.
	 * 
	 * @return String 
	 */
	@Column(name = "Collector", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getCollector() {
		return mCollector;
	}

	/**
	 * Set the Collector field.
	 */
	public void setCollector(String pNewCollector) {
		mCollector = pNewCollector;
	}
	
	/**
	 * Return the SampleDate field.
	 * 
	 * @return Date 
	 */
	@Column(name = "SampleDate")
	@Temporal(TemporalType.DATE)
	public Date getSampleDate() {
		return mSampleDate;
	}

	/**
	 * Set the SampleDate field.
	 */
	public void setSampleDate(Date pNewSampleDate) {
		mSampleDate = pNewSampleDate;
	}
	
	/**
	 * Return the SampleDate field as a string. 
	 * Show date only, ISO 8601 format:
	 * 
	 * @return
	 */
	@Transient
	public String getSampleDateString() {
		// show date only, ISO 8601 format:
		return TreebaseUtil.formatDate(getSampleDate(), true);
	}

	/**
	 * Set the SampleDate field as a string.
	 */
	public void setSampleDateString(String pDateString) {
		//Note: how to handle exception??
		setSampleDate(
			TreebaseUtil.parseDate(pDateString, true, null));
	}
	
	/**
	 * Return the Elevation field. Decimal number. Negative indicates depth.
	 * 
	 * @return Double
	 */
	@Column(name = "Elevation")
	public Double getElevation() {
		return mElevation;
	}
	
	/**
	 * @return the elevation as a string, or the empty string if none
	 * @author mjd 20081017
	 */
	@Transient
	public String getElevationString() {
		if (mElevation == null) return "";
		else return mElevation.toString();
	}

	/**
	 * Set the Elevation field.
	 */
	public void setElevation(Double pNewElevation) {
		mElevation = pNewElevation;
	}

	/**
	 * Return the Longitude field.
	 * Negative is South.
	 * 
	 * @return Double
	 */
	@Column(name = "Longitude")
	public Double getLongitude() {
		return mLongitude;
	}
	
	/**
	 * @return the longitude as a string, or the empty string if none
	 * @author mjd 20081017
	 */
	@Transient
	public String getLongitudeString() {
		if (mLongitude == null) return "";
		else return abs(mLongitude) + (mLongitude < 0 ? "S" : "N");
	}

	/**
	 * Set the Longitude field.
	 */
	public void setLongitude(Double pNewLongitude) {
		mLongitude = pNewLongitude;
	}

	/**
	 * Return the Latitude field.
	 * Negative is West.
	 * 
	 * @return Double
	 */
	@Column(name = "Latitude")
	public Double getLatitude() {
		return mLatitude;
	}
	
	/**
	 * @return the latitude as a string, or the empty string if none
	 * @author mjd 20081017
	 */
	@Transient
	public String getLatitudeString() {
		if (mLatitude == null) return "";
		else return abs(mLatitude) + (mLatitude < 0 ? "W" : "E");
	}

	/**
	 * Set the Latitude field.
	 */
	public void setLatitude(Double pNewLatitude) {
		mLatitude = pNewLatitude;
	}

	/**
	 * Return the Notes field.
	 * 
	 * @return String 
	 */
	@Column(name = "Notes", length = TBPersistable.COLUMN_LENGTH_STRING_NOTES)
	public String getNotes() {
		return mNotes;
	}

	/**
	 * Set the Notes field.
	 */
	public void setNotes(String pNewNotes) {
		mNotes = pNewNotes;
	}

	/**
	 * Return the Catalog Number field.
	 * 
	 * @return String
	 */
	@Column(name = "CatalogNum", nullable = true, length = TBPersistable.COLUMN_LENGTH_50)
	public String getCatalogNumber() {
		return mCatalogNumber;
	}

	/**
	 * Set the Catalog Number field.
	 */
	public void setCatalogNumber(String pNewTissue) {
		mCatalogNumber = pNewTissue;
	}

	/**
	 * Return the CollectionCode field.
	 * 
	 * @return String 
	 */
	@Column(name = "CollectionCode", length = TBPersistable.COLUMN_LENGTH_50)
	public String getCollectionCode() {
		return mCollectionCode;
	}

	/**
	 * Set the CollectionCode field.
	 */
	public void setCollectionCode(String pNewCollectionCode) {
		mCollectionCode = pNewCollectionCode;
	}
	
	/**
	 * Return the Institution Acronym field.
	 * 
	 * @return String
	 */
	@Column(name = "InstAcronym", nullable = true, length = TBPersistable.COLUMN_LENGTH_50)
	@Index(name = "SPECIM_INST_IDX")
	public String getInstAcronym() {
		return mInstitutionAcronym;
	}

	/**
	 * Set the Institution Acronym  field.
	 */
	public void setInstAcronym (String pMuseumNumber) {
		mInstitutionAcronym = pMuseumNumber;
	}

	/**
	 * Return the GenBank Accession Number field.
	 * 
	 * @return String
	 */
	@Column(name = "GenBAccession", nullable = true, length = TBPersistable.COLUMN_LENGTH_30)
	public String getGenBankAccession() {
		return mGenBankAccession;
	}

	/**
	 * Set the GenBankID field.
	 */
	public void setGenBankAccession(String pNewGenBankID) {
		mGenBankAccession = pNewGenBankID;
	}


	/**
	 * Return the OtherAccession field.
	 * 
	 * @return String 
	 */
	@Column(name = "OtherAccession", length = TBPersistable.COLUMN_LENGTH_50)
	public String getOtherAccession() {
		return mOtherAccession;
	}

	/**
	 * Set the OtherAccession field.
	 */
	public void setOtherAccession(String pNewOtherAccession) {
		mOtherAccession = pNewOtherAccession;
	}
	
	/**
	 * @return a string describing all the available information
	 * @author mjd 20081016
	 */
	@Transient
	public String getInfo() {
		StringBuilder s = new StringBuilder();

		appendIfNonEmpty(s, "GenBankAccession#", getGenBankAccession());
		appendIfNonEmpty(s, "Latitude", getLatitudeString());
		appendIfNonEmpty(s, "Longitude", getLongitudeString());
		appendIfNonEmpty(s, "Elevation", getElevationString());
		appendIfNonEmpty(s, "Inst.", getInstAcronym());
		appendIfNonEmpty(s, "OtherAccession#", getOtherAccession());
		appendIfNonEmpty(s, "SampleDate", getSampleDateString());
		appendIfNonEmpty(s, "Catalog#", getCatalogNumber());
		appendIfNonEmpty(s, "Collector", getCollector());
		appendIfNonEmpty(s, "CollectionCode", getCollectionCode());
		appendIfNonEmpty(s, "Country", getCountry());
		appendIfNonEmpty(s, "State", getState());
		appendIfNonEmpty(s, "Locality", getLocality());
		appendIfNonEmpty(s, "Notes", getNotes());

		if (s.length() < 2) {
			return "(no specimen information)";
		}
		
		s.delete(s.length()-2, s.length()-1);

		return s.toString();
	}
	
	private void appendIfNonEmpty(StringBuilder s, String label, String value) {
		if (TreebaseUtil.isEmpty(value)) return; 
		s.append(label + ": " + value + "; ");
	}
}
