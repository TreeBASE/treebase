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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * StudyStatus.java
 * 
 * Created on Feb 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "STUDYSTATUS")
@AttributeOverride(name = "id", column = @Column(name = "STUDYSTATUS_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "staticCache")
public class StudyStatus extends AbstractPersistedObject {

	private static final long serialVersionUID = 5929250114381882675L;

	public static final String INPROGRESS = "In Progress";
	public static final String READY = "Ready";
	public static final String PUBLISHED = "Published";

	private String mDescription;

	/**
	 * Constructor.
	 */
	protected StudyStatus() {
		super();
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String
	 */
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Determine if initial
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isInProgress() {
		return INPROGRESS.equalsIgnoreCase(getDescription());
	}

	/**
	 * Determine if published
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isReady() {
		return READY.equalsIgnoreCase(getDescription());
	}

	/**
	 * Determine if published
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isPublished() {
		return PUBLISHED.equalsIgnoreCase(getDescription());
	}

	/**
	 * @param pDescription The description to set.
	 */
	@SuppressWarnings("unused")
	private void setDescription(String pDescription) {
		mDescription = pDescription;
	}

}
