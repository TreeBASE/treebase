/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */
package org.cipres.treebase.web.model;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Transient;

import org.cipres.treebase.domain.study.ArticleCitation;
import org.cipres.treebase.domain.study.BookCitation;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.InBookCitation;

/**
 * CitationCommand.java
 * 
 * This is my custom command object for citation since I can't use the one
 * provided by the domain object directly
 * 
 * Created on May 17, 2006
 * 
 * @author lcchan
 * 
 */
@SuppressWarnings("serial")
public class CitationCommand extends Citation {
	
	private static final Logger LOGGER = Logger
			.getLogger(CitationCommand.class);

	private Long id;
	private String mCitationType;

	private Map<String, Object> citationMap = new HashMap<String, Object>();

	/**
	 * Default constructor Creation date: May 17, 2006 2:11:05 PM
	 */
	public CitationCommand() {
		citationMap.put(BookCitation.CITATION_TYPE_BOOK, new BookCitation());
		citationMap.put(ArticleCitation.CITATION_TYPE_ARTICLE, new ArticleCitation());
		citationMap.put(InBookCitation.CITATION_TYPE_BOOKSECTION, new InBookCitation());
	}

	/**
	 * Create a new citation object based on the type. 
	 * 
	 * @param pCitationType
	 * @return
	 */
	public Citation afactory(String pCitationType) {
		if (BookCitation.CITATION_TYPE_BOOK.compareToIgnoreCase(pCitationType) == 0) {
			return new BookCitation();
		} else if (ArticleCitation.CITATION_TYPE_ARTICLE.compareToIgnoreCase(pCitationType) == 0){ 
			return new ArticleCitation();
		} else if (InBookCitation.CITATION_TYPE_BOOKSECTION.compareToIgnoreCase(pCitationType) == 0) {
			return new InBookCitation();
		}
		
		//default:
		return new ArticleCitation();
	}
	
//	public Map<String, Object> getCitationMap() {
//		return citationMap;
//	}
//
	/* this is so we can access the map in jsp page */
	public Citation getCitationMap(String key) {
		return (Citation) citationMap.get(key);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.Citation#getCitationType()
	 */
	@Override
	public String getCitationType() {
		return mCitationType;
	}

	public void setMCitationType(String citationType) {
		this.mCitationType = citationType;
	}

	public void setCitationMap(String key, Object object) {
		this.citationMap.put(key, object);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/** 
	 * 
	 * @see org.cipres.treebase.domain.study.Citation#getRealCitationType()
	 */
	@Override
	@Transient
	protected String getRealCitationType() {
		return "CitationCommand";
	}

	
}