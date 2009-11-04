
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
