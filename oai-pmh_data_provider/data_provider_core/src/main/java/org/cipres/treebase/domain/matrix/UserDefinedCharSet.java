/*
 * CIPRES Copyright (c) 2005- 2006, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
		sb.append(getNexusPrefix()).append(getTitle()).append(" = ");

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
