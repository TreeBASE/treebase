


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
