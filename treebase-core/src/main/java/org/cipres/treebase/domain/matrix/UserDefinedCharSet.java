package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import mesquite.lib.StringUtil;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * UserDefinedCharSet.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("U")
public class UserDefinedCharSet extends CharSet {

	private static final long serialVersionUID = 1L;

	private Collection<ColumnRange> mColumns = new ArrayList<ColumnRange>();

	/**
	 * Constructor.
	 */
	public UserDefinedCharSet() {
		super();
	}

	/**
	 * Return the columns.
	 * 
	 * @return
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "CharSET_ColRange", joinColumns = {@JoinColumn(name = "CHARSET_ID")}, inverseJoinColumns = @JoinColumn(name = "COLUMNRANGE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<ColumnRange> getColumns() {
		return mColumns;
	}

	/**
	 * Set the columns.
	 */
	public void setColumns(Collection<ColumnRange> pColumns) {
		mColumns = pColumns;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.CharSet#getColumns(org.cipres.treebase.domain.matrix.CharacterMatrix)
	 */
	@Override
	public Collection<ColumnRange> getColumns(CharacterMatrix pMatrix) {
		return getColumns();
	}

	/**
	 * Return a Nexus formated string describing the char partition.
	 * 
	 * @return
	 */
	@Transient
	@Override
	public String getNexusString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getNexusPrefix()).append(getTitle()).append(" (CHARACTERS = ").append(StringUtil.tokenize(getMatrix().getTitle())).append(") = ");

		for (ColumnRange colRange : getColumns()) {
			sb.append(" ");
			colRange.appendRange(sb);
		}
		return sb.toString();
	}
	/**
	 * Return the Characters field.
	 * 
	 * @return Set<PhyloChar>
	 */
	// @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	// @JoinColumn(name = "CHARSET_ID", nullable = true)
	// @ManyToMany
	// @JoinTable(name = "CHARSET_PHYLOCHAR", joinColumns = {@JoinColumn(name = "CHARSET_ID")},
	// inverseJoinColumns = @JoinColumn(name = "PHYLOCHAR_ID"))
	// public Set<PhyloChar> getCharacters() {
	// return mCharacters;
	// }
}
