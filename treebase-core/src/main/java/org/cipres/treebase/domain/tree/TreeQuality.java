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

package org.cipres.treebase.domain.tree;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TreeQuality.java
 * 
 * Created on Feb 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TREEQUALITY")
@AttributeOverride(name = "id", column = @Column(name = "TREEQUALITY_ID"))
public class TreeQuality extends AbstractPersistedObject {

	//default: is unrated. 
	private static final String QUALITY_UNRATED = "Unrated";
	private static final String QUALITY_PREFERRED = "Preferred Tree";
	private static final String QUALITY_ALTERNATIVE = "Alternative Tree";
	private static final String QUALITY_SUBOPTIMAL = "Suboptimal Tree";

	private String mDescription;

	/**
	 * Return a list of all predefined instances as String. 
	 * 
	 * @return
	 */
	public static List<String> allInstanceDescriptions() {
		List<String> allInsts = new ArrayList<String>();
		allInsts.add(QUALITY_UNRATED);
		allInsts.add(QUALITY_PREFERRED);
		allInsts.add(QUALITY_ALTERNATIVE);
		allInsts.add(QUALITY_SUBOPTIMAL);

		return allInsts;
	}
	
	/**
	 * Constructor.
	 */
	public TreeQuality() {
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
	 * Determine if unrated tree.
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean isQualityUnrated() {
		return QUALITY_UNRATED.equalsIgnoreCase(getDescription());
	}

	/**
	 * @param pDescription The description to set.
	 */
	public void setDescription(String pDescription) {
		mDescription = pDescription;
	}

}
