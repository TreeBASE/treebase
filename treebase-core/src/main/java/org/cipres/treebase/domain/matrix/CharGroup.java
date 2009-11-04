package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TreeGroup.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "CHARGROUP")
@AttributeOverride(name = "id", column = @Column(name = "CHARGROUP_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class CharGroup extends AbstractPersistedObject {

	private static final long serialVersionUID = -6215673532165359873L;

	private String mTitle;

	private CharPartition mPartition;

	private Collection<ColumnRange> mCharColumns = new ArrayList<ColumnRange>();

	/**
	 * Constructor.
	 */
	public CharGroup() {
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
	 * Return the Partition field.
	 * 
	 * @return CharPartition mPartition
	 */
	@ManyToOne
	@JoinColumn(name = "CHARPARTITION_ID")
	public CharPartition getPartition() {
		return mPartition;
	}

	/**
	 * Set the Partition field.
	 */
	public void setPartition(CharPartition pNewPartition) {
		mPartition = pNewPartition;
	}

	/**
	 * Return the weight columns.
	 * 
	 * @return
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "CharGroup_ColRange", joinColumns = {@JoinColumn(name = "CHARGROUP_ID")}, inverseJoinColumns = @JoinColumn(name = "COLUMNRANGE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<ColumnRange> getCharColumns() {
		return mCharColumns;
	}

	/**
	 * Set the weight columns.
	 */
	public void setCharColumns(Collection<ColumnRange> pWeightColumns) {
		mCharColumns = pWeightColumns;
	}

	/**
	 * Append the description.
	 * 
	 * @param pBuilder
	 * @return
	 */
	public StringBuilder appendNexusString(StringBuilder pBuilder) {
		pBuilder.append(getTitle()).append(":");

		for (ColumnRange colRange : getCharColumns()) {
			pBuilder.append(" ");
			colRange.appendRange(pBuilder);
		}
		return pBuilder;
	}

	// /**
	// * Append a new PhyloChar to the Set.
	// *
	// * Creation date: Mar 14, 2006
	// *
	// * @param pChar PhyloChar
	// */
	// public void addCharacter(PhyloChar pChar) {
	// if (pChar != null) {
	// getCharSet().add(pChar);
	// }
	// }
	//
	// /**
	// * Remove a character.
	// *
	// * Creation date: Mar 14, 2006
	// *
	// * @param pChar PhyloChar
	// */
	// public void removeCharacter(PhyloChar pChar) {
	// if (pChar != null) {
	// getCharSet().remove(pChar);
	// }
	// }
	//
	// /**
	// * Return the PhyloChar Set field.
	// *
	// * @return Collection<PhyloChar>
	// */
	// @ManyToMany
	// @JoinTable(name = "CHARGROUP_PHYLOCHAR", joinColumns = {@JoinColumn(name = "CHARGROUP_ID")},
	// inverseJoinColumns = @JoinColumn(name = "PHYLOCHAR_ID"))
	// protected Collection<PhyloChar> getCharSet() {
	// return mCharSet;
	// }
	//
	// /**
	// * Set the character Set field.
	// */
	// protected void setCharSet(Collection<PhyloChar> pCharSet) {
	// mCharSet = pCharSet;
	// }
	//
}
