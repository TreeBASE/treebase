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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.TBPersistable;

/**
 * ArticleCitation.java
 * 
 * Created on February 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("A")
public class ArticleCitation extends Citation {

	public static final String CITATION_TYPE_ARTICLE = "Article";

	private String mJournal;
	private String mVolume;
	private String mIssue;

	/**
	 * Constructor.
	 */
	public ArticleCitation() {
		super();
	}

	/**
	 * Return the Issue field.
	 * 
	 * @return String
	 */
	@Column(name = "Issue", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getIssue() {
		return mIssue;
	}

	/**
	 * Set the Issue field.
	 */
	public void setIssue(String pNewIssue) {
		mIssue = pNewIssue;
	}

	/**
	 * Return the Volume field.
	 * 
	 * @return String
	 */
	@Column(name = "Volume", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getVolume() {
		return mVolume;
	}

	/**
	 * Set the Volume field.
	 */
	public void setVolume(String pNewVolume) {
		mVolume = pNewVolume;
	}

	/**
	 * Return the Journal field.
	 * 
	 * @return String
	 */
	@Column(name = "Journal", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getJournal() {
		return mJournal;
	}

	/**
	 * Set the Journal field.
	 */
	public void setJournal(String pNewJournal) {
		mJournal = pNewJournal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.Citation#getRealCitationType()
	 */
	@Override
	@Transient
	protected String getRealCitationType() {
		return CITATION_TYPE_ARTICLE;
	}

	@Transient
	@Override
	public void getDetailedPublicationInformation(StringBuilder pBuilder, boolean pGenerateHtml) {
		pBuilder.append(". ").append(getTitleInformation());
		pBuilder
			.append(" ").append(getJournal()).append(", ").append(getVolume());
		
		if (!TreebaseUtil.isEmpty(getIssue())) {
			pBuilder.append("(").append(
				getIssue()).append(")");
		}
		
		if (!TreebaseUtil.isEmpty(getPages())) {
			pBuilder.append(": ").append(getPages());
		}
		
		pBuilder.append(".");
	}

}
