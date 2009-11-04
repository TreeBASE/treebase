package org.cipres.treebase.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.study.Citation;

/**
 * @author madhu
 * 
 */

public final class AddAuthorController extends AddPersonController {

	@Override
	protected List<Person> getPeople(Citation pCitation) {
		return pCitation.getAuthors();
	}

	@Override
	protected String provideAddMessageParameter() {
		return "author.add.success";
	}

	@Override
	protected String provideDeleteMessageParameter() {
		return "author.delete.success";
	}

	@Override
	protected void setPeople(Citation pCitation, List<Person> pPeople) {
		pCitation.setAuthors(pPeople);
	}

	@Override
	protected void setSessionVariable(HttpServletRequest pRequest) {
		pRequest.getSession().setAttribute("PEOPLE", "Author");

	}

}
