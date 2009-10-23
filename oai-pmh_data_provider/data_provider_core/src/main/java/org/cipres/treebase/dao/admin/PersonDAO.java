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

package org.cipres.treebase.dao.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.PersonHome;

/**
 * PersonDAO.java
 * 
 * Created on May 9, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class PersonDAO extends AbstractDAO implements PersonHome {

	/**
	 * Constructor.
	 */
	public PersonDAO() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.PersonHome#findByLastName(java.lang.String)
	 */
	public Collection<Person> findByLastName(String pLastName) {
		Collection<Person> results = new ArrayList<Person>();

		if (!TreebaseUtil.isEmpty(pLastName)) {

			Criteria c = getSession().createCriteria(Person.class);
			c.add(Expression.eq("lastName", pLastName).ignoreCase());
			results = c.list();

		}
		return results;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.PersonHome#findByExactMatch(org.cipres.treebase.domain.admin.Person)
	 */
	public Person findByExactMatch(Person pPerson) {
		if (pPerson == null || TreebaseUtil.isEmpty(pPerson.getLastName())) {
			return null;
		}

		Example examplePerson = Example.create(pPerson);
		Criteria c = getSession().createCriteria(Person.class);
		c.add(examplePerson);

		// Notes: the query by criteria does not work!
		// c.add(Expression.eq("lastName", pPerson.getLastName()));
		// c.add(Expression.eq("firstName", pPerson.getFirstName()));
		// c.createCriteria("emailAddress").add(
		// Expression.eq("emailAddressString", pPerson.getEmailAddressString()));

		return (Person) c.uniqueResult();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.PersonHome#findCompleteEmailAddress(java.lang.String)
	 */
	public List<String> findCompleteEmailAddress(String pPartialEmailAddress) {
		List<String> alist = new ArrayList<String>();

		if (!TreebaseUtil.isEmpty(pPartialEmailAddress) && pPartialEmailAddress.length() >= 2) {

			// CASE insensitive
			Query q = getSession()
				.createQuery(
					"select distinct p.emailAddress.emailAddressString from Person p where lower(p.emailAddress.emailAddressString) like :mStr");

			q.setString("mStr", pPartialEmailAddress.toLowerCase() + '%');
			alist = q.list();
		}
		return alist;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.admin.PersonHome#findDuplicateWithFirstAndLastNames()
	 */
	public List<Person> findDuplicateWithFirstAndLastNames() {
		Query q = getSession()
		.createQuery(
			"from Person where lastName IN (select lastName from Person group by firstName, lastName having (count(lastName)) > 1)"); 
	
		return q.list();
		
		//SQL: for select duplicate with last name only:
		//select count(p.LASTNAME) as nameCount, p.LASTNAME from person p 
	    //group by p.LASTNAME having (count(p.LASTNAME)) > 1
		
		// for select duplicate with first and last name:
		//select person_id, firstname, lastname from person where LASTNAME IN (
		//	select lastname from person 
		//	    group by firstname, LASTNAME having (count(LASTNAME)) > 1)

	}

	/**
	 * @see org.cipres.treebase.domain.admin.PersonHome#findDuplicateWithLastNames()
	 */
	public List<Person> findDuplicateWithLastNames() {
		Query q = getSession()
		.createQuery(
			"from Person where lastName IN (select lastName from Person group by lastName having (count(lastName)) > 1)");	
		return q.list();
	}
	
	

}
