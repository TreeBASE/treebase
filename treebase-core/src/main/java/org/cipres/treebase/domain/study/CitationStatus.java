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

import java.util.ArrayList;
import java.util.List;

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
 * CitationStatus
 * 
 * Created on Feb 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "CITATIONSTATUS")
@AttributeOverride(name = "id", column = @Column(name = "CITATIONSTATUS_ID"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "staticCache")
public class CitationStatus extends AbstractPersistedObject {

	private static final long serialVersionUID = -6811040978004379985L;

	// Follow the order:
	public static final String INPREP = "In Prep";
	public static final String INREVIEW = "In Review";
	public static final String ACCEPTED_MINIOR_CHANGES = "Accepted with Minor Changes";
	public static final String INPRESS = "In Press";
	public static final String PUBLISHED = "Published";

	private String mDescription;

	/**
	 * Constructor.
	 */
	protected CitationStatus() {
		super();
	}

	/**
	 * Return an ordered list of all citation status.
	 * 
	 * @return
	 */
	public static List<String> getCitationStatusList() {
		List<String> statusList = new ArrayList<String>();

		// order here!
		statusList.add(INPREP);
		statusList.add(INREVIEW);
		statusList.add(ACCEPTED_MINIOR_CHANGES);
		statusList.add(INPRESS);
		statusList.add(PUBLISHED);

		return statusList;
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String
	 */
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_50)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Determine if the status is in press.
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isInPress() {
		return INPRESS.equalsIgnoreCase(getDescription());
	}

	/**
	 * Determine if the status is in prep.
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isInPrep() {
		return INPREP.equalsIgnoreCase(getDescription());
	}

	/**
	 * Determine if the status is in review.
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isInReview() {
		return INREVIEW.equalsIgnoreCase(getDescription());
	}

	/**
	 * Determine if the status is published
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isPublished() {
		return PUBLISHED.equalsIgnoreCase(getDescription());
	}

	/**
	 * Determine if the status is accepted with minor changes.
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isAccepted() {
		return ACCEPTED_MINIOR_CHANGES.equalsIgnoreCase(getDescription());
	}

	/**
	 * @param pDescription The description to set.
	 */
	@SuppressWarnings("unused")
	private void setDescription(String pDescription) {
		mDescription = pDescription;
	}
}
