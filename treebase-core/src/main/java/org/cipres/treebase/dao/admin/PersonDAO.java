
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

			Criteria c = getSessionFactory().getCurrentSession().createCriteria(Person.class);
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
		Criteria c = getSessionFactory().getCurrentSession().createCriteria(Person.class);
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
			Query q = getSessionFactory().getCurrentSession()
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
		Query q = getSessionFactory().getCurrentSession()
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
		Query q = getSessionFactory().getCurrentSession()
		.createQuery(
			"from Person where lastName IN (select lastName from Person group by lastName having (count(lastName)) > 1)");	
		return q.list();
	}
	
	

}
