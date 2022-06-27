
package org.cipres.treebase.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	private static final Logger LOGGER = LogManager.getLogger(AuthorFormController.class);

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
