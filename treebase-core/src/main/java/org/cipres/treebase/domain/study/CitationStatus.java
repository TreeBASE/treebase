
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
	@Override
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
