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
package org.cipres.treebase.domain.study;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * Algorithm.java
 * 
 * Created on Mar 13, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "ALGORITHM")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.CHAR)
@AttributeOverride(name = "id", column = @Column(name = "ALGORITHM_ID"))
@DiscriminatorValue("-")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "studyCache")
public abstract class Algorithm extends AbstractPersistedObject {

	private String mDescription;

	private String mPropertyName;
	private String mPropertyValue;

	/**
	 * Constructor.
	 */
	public Algorithm() {
		super();
	}

	/**
	 * Return the Description field.
	 * 
	 * @return String mDescription
	 */
	@Column(name = "Description", length = TBPersistable.COLUMN_LENGTH_STRING_NOTES)
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Set the Description field.
	 */
	public void setDescription(String pNewDescription) {
		mDescription = pNewDescription;
	}

	/**
	 * Return the PropertyValue field.
	 * 
	 * @return String mPropertyValue
	 */
	@Column(name = "PropertyValue", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getPropertyValue() {
		return mPropertyValue;
	}

	/**
	 * Set the PropertyValue field.
	 */
	public void setPropertyValue(String pNewPropertyValue) {
		mPropertyValue = pNewPropertyValue;
	}

	/**
	 * Return the PropertyName field.
	 * 
	 * @return String mPropertyName
	 */
	@Column(name = "PropertyName", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getPropertyName() {
		return mPropertyName;
	}

	/**
	 * Set the PropertyName field.
	 */
	public void setPropertyName(String pNewPropertyName) {
		mPropertyName = pNewPropertyName;
	}
	
	/* constants returned by getAlgorithmType */
	public static final String LikelihoodAlgorithm = "maximum likelihood";
	public static final String DistanceAlgorithm = "distance";
	public static final String OtherAlgorithm = "other algorithm";
	public static final String ParsimonyAlgorithm = "parsimony";
	
	/**
	 * @return a string describing the algorithm type (for example, "parsimony")
	 * @author mjd 20080723
	 */
	@Transient
	public abstract String getAlgorithmType();
}
