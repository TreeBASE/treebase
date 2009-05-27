/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

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
