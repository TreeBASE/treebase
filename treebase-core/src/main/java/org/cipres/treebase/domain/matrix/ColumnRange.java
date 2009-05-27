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

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * ColumnRange.java
 * 
 * Created on Mar 29, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "COLUMNRANGE")
@AttributeOverride(name = "id", column = @Column(name = "COLUMNRANGE_ID"))
public class ColumnRange extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private Integer mStartColIndex;
	private Integer mEndColIndex;
	private Integer mRepeatInterval;

	/**
	 * Constructor.
	 */
	public ColumnRange() {
		super();
	}

	/**
	 * Return the RepeatInterval field.
	 * 
	 * @return Integer
	 */
	@Column(name = "RepeatInterval", nullable = true)
	public Integer getRepeatInterval() {
		return mRepeatInterval;
	}

	/**
	 * Set the RepeatInterval field.
	 */
	public void setRepeatInterval(Integer pNewRepeatInterval) {
		mRepeatInterval = pNewRepeatInterval;
	}

	/**
	 * Return the EndColIndex field.
	 * 
	 * @return Integer
	 */
	@Column(name = "EndColIndex", nullable = true)
	public Integer getEndColIndex() {
		return mEndColIndex;
	}

	/**
	 * Set the EndColIndex field.
	 */
	public void setEndColIndex(Integer pNewEndColIndex) {
		mEndColIndex = pNewEndColIndex;
	}

	/**
	 * Return the StartColIndex field.
	 * 
	 * @return Integer
	 */
	@Column(name = "StartColIndex", nullable = true)
	public Integer getStartColIndex() {
		return mStartColIndex;
	}

	/**
	 * Set the StartColIndex field.
	 */
	public void setStartColIndex(Integer pNewStartColIndex) {
		mStartColIndex = pNewStartColIndex;
	}

	/**
	 * Print the column range to the string buffer. Return the string buffer for call chaining.
	 */
	public StringBuilder appendRange(StringBuilder pBuilder) {

		// Note: convert 0 based index to 1 based column index when printing:

		if (getStartColIndex() != null) {
			pBuilder.append(getStartColIndex() + 1);
			if (!getStartColIndex().equals(getEndColIndex())) {
				pBuilder.append("-").append(getEndColIndex() + 1);
			}

			if (getRepeatInterval() != null && getRepeatInterval() != 0) {
				pBuilder.append("\\").append(getRepeatInterval());
			}
		}

		return pBuilder;
	}

}
