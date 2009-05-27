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

package org.cipres.treebase.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.study.Citation;

/**
 * AuthorController.java
 * 
 * Created on October, 2007
 * 
 * @author Madhu
 * 
 * Old author form controller was abstracted to PeopleFormController and modified
 * 
 * @author lcchan
 * 
 */
public class AuthorFormController extends PeopleFormSearchController {

	private static final Logger LOGGER = Logger.getLogger(AuthorFormController.class);

	@Override
	public List<Person> getPeople(Citation pCitation) {
		return pCitation.getAuthors();
	}

	@Override
	public void setPeople(Citation pCitation, List<Person> people) {
		pCitation.setAuthors(people);
	}

	/**
	 * Session Variable People is used in peopleList.jsp, which is included in peopleForm.jsp
	 */

	@Override
	public void setSessionVariable(HttpServletRequest request) {
		request.getSession().setAttribute("PEOPLE", "Author");
	}

	@Override
	public String provideDeleteMessageParameter() {
		return "author.delete.success";
	}

	@Override
	public String provideAddMessageParameter() {
		return "author.add.success";
	}

}
