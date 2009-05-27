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

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * CharWeight.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "CHARWEIGHT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.CHAR)
@AttributeOverride(name = "id", column = @Column(name = "CHARWEIGHT_ID"))
@DiscriminatorValue("-")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public abstract class CharWeight extends AbstractPersistedObject {

	private Collection<ColumnRange> mWeightColumns = new ArrayList<ColumnRange>();

	/**
	 * Constructor.
	 */
	public CharWeight() {
		super();
	}

	/**
	 * Return the weight columns.
	 * 
	 * @return
	 */
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "CharWeight_ColRange", joinColumns = {@JoinColumn(name = "CHARWEIGHT_ID")}, inverseJoinColumns = @JoinColumn(name = "COLUMNRANGE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Collection<ColumnRange> getWeightColumns() {
		return mWeightColumns;
	}

	/**
	 * Set the weight columns.
	 */
	public void setWeightColumns(Collection<ColumnRange> pWeightColumns) {
		mWeightColumns = pWeightColumns;
	}

	/**
	 * Print the weight to the string buffer.
	 * 
	 * Return the string buffer for call chaining.
	 * 
	 * @return
	 */
	public abstract StringBuilder appendWeight(StringBuilder pBuilder);

	/**
	 * Get the weight and the associated columns.
	 * 
	 * @return
	 */
	@Transient
	public String getWeightAndColumnAsString() {
		StringBuilder sb = new StringBuilder();
		appendWeight(sb).append(": ");

		for (ColumnRange colRange : getWeightColumns()) {
			colRange.appendRange(sb).append(" ");
		}
		return sb.toString();
	}
}
