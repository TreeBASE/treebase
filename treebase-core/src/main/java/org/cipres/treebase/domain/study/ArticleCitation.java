
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
