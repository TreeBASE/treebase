
package org.cipres.treebase.domain.study;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * Software.java
 * 
 * Created on Feb 23, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "SOFTWARE")
@AttributeOverride(name = "id", column = @Column(name = "SOFTWARE_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
public class Software extends AbstractPersistedObject {

	private static final long serialVersionUID = -4121808047109148322L;

	private String mName;
	private String mSoftwareVersion;
	private String mDescription;
	private String mSoftwareURL;

	/**
	 * Constructor.
	 */
	public Software() {
		super();
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String
	 */
	@Override
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_STRING_NOTES)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Set the Description field.
	 */
	public void setDescription(String pNewDescription) {
		mDescription = pNewDescription;
	}

	/**
	 * Return the SoftwareVersion field.
	 * 
	 * @return String
	 */
	@Column(name = "SoftwareVersion", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getSoftwareVersion() {
		return mSoftwareVersion;
	}

	/**
	 * Set the SoftwareVersion field.
	 */
	public void setSoftwareVersion(String pNewSoftwareVersion) {
		mSoftwareVersion = pNewSoftwareVersion;
	}

	/**
	 * Return the Name field.
	 * 
	 * @return String
	 */
	@Column(name = "Name", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getName() {
		return mName;
	}

	/**
	 * Set the Name field.
	 */
	public void setName(String pNewName) {
		mName = pNewName;
	}

	@Column(name = "SoftwareURL", length = TBPersistable.COLUMN_LENGTH_500)
	public String getSoftwareURL() {
		return mSoftwareURL;
	}

	public void setSoftwareURL(String pSoftwareURL) {
		mSoftwareURL = pSoftwareURL;
	}

}
