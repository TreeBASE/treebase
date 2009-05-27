/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.study.BookCitation;
import org.cipres.treebase.domain.study.Citation;

/**
 * @author madhu
 * 
 * created on 26th September, 2007 Earlier version of AuthorFormController was abstracted to
 * PeopleFormController so that it could be used for both handling Editors and Authors
 */
public class EditorFormController extends PeopleFormController {

	private static final Logger LOGGER = Logger.getLogger(EditorFormController.class);

	@Override
	protected List<Person> getPeople(Citation pCitation) {

		if (pCitation instanceof BookCitation) {
			return ((BookCitation) pCitation).getEditors();
		} else {
			return new ArrayList<Person>();
		}
	}

	@Override
	protected void setPeople(Citation pCitation, List<Person> pPeople) {
		if (pCitation instanceof BookCitation) {
			((BookCitation) pCitation).setEditors(pPeople);
		}
	}

	/**
	 * Session Variable People is used in peopleList.jsp, which is included in peopleForm.jsp
	 */
	@Override
	public void setSessionVariable(HttpServletRequest request) {
		request.getSession().setAttribute("PEOPLE", "Editor");
	}

	@Override
	public String provideDeleteMessageParameter() {
		return "editor.delete.success";
	}

	@Override
	public String provideAddMessageParameter() {
		return "editor.add.success";
	}
}
