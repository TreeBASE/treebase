package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * CharPartition.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "CHARPARTITION")
@AttributeOverride(name = "id", column = @Column(name = "CHARPARTITION_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class CharPartition extends AbstractPersistedObject {

	private static final long serialVersionUID = 538572333294239135L;

	private String mTitle;

	private Collection<CharGroup> mCharGroups = new ArrayList<CharGroup>();

	/**
	 * Constructor.
	 */
	public CharPartition() {
		super();
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String
	 */
	@Column(name = "Title", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Set the Title field.
	 */
	public void setTitle(String pNewTitle) {
		mTitle = pNewTitle;
	}

	/**
	 * Append a new PhyloChar Groups to the Set.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pCharGroup CharGroup
	 */
	public void addCharGroup(CharGroup pCharGroup) {
		if (pCharGroup != null) {
			getCharGroups().add(pCharGroup);

			pCharGroup.setPartition(this);
		}
	}

	/**
	 * Remove a character group.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pCharGroup CharGroup
	 */
	public void removeCharGroup(CharGroup pCharGroup) {
		if (pCharGroup != null) {
			getCharGroups().remove(pCharGroup);

			pCharGroup.setPartition(null);
		}
	}

	/**
	 * Return the CharGroup Set field.
	 * 
	 * @return Collection<CharGroup>
	 */
	@OneToMany(mappedBy = "partition", cascade = {CascadeType.MERGE, CascadeType.PERSIST,
		CascadeType.REMOVE})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	protected Collection<CharGroup> getCharGroups() {
		return mCharGroups;
	}

	/**
	 * Set the CharGroups field.
	 * 
	 * @param pCharGroups
	 */
	protected void setCharGroups(Collection<CharGroup> pCharGroups) {
		mCharGroups = pCharGroups;
	}

	/**
	 * Return a Nexus formated string describing the char partition.
	 * 
	 * @return
	 */
	@Transient
	public String getNexusString() {
		StringBuilder sb = new StringBuilder();
		sb.append("charpartition ").append(getTitle()).append(" = ");

		for (CharGroup aGroup : getCharGroups()) {
			aGroup.appendNexusString(sb).append(", ");
		}
		return sb.toString();
	}
}
