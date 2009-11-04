
package org.cipres.treebase.domain.study;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * InBookCitation.java
 * 
 * Created on Feb 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("I")
public class InBookCitation extends BookCitation {

	public static final String CITATION_TYPE_BOOKSECTION = "Book Section";

	/**
	 * Constructor
	 */
	public InBookCitation() {
		super();
	}

	/**
	 * Return the SessionPages field.
	 * 
	 * @return String mSessionPages
	 */
	@Transient
	public String getSectionPages() {
		return getPages();
	}

	/**
	 * Set the SessionPages field.
	 */
	public void setSectionPages(String pNewSectionPages) {
		setPages(pNewSectionPages);
	}

	/**
	 * Return the SessionTitle field.
	 * 
	 * @return String mSessionTitle
	 */
	@Transient
	public String getSectionTitle() {
		return getTitle();
	}

	/**
	 * Set the Section Title field.
	 */
	public void setSectionTitle(String pNewSectionTitle) {
		setTitle(pNewSectionTitle);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.Citation#getRealCitationType()
	 */
	@Override
	@Transient
	protected String getRealCitationType() {
		return CITATION_TYPE_BOOKSECTION;
	}

	@Transient
	@Override
	public void getDetailedPublicationInformation(StringBuilder pBuilder, boolean pGenerateHtml) {
		pBuilder.append(". ").append("\"").append(getTitleInformation()).append("\"").append(
			" In: ").append(getEditorsAsString(pGenerateHtml)).append(", eds. ").append(
			getBookTitleInformation()).append(" pp. ").append(getPages()).append(". ").append(
			getCity()).append(", ").append(getPublisher()).append(".");
	}

}
