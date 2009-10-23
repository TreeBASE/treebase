/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

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
