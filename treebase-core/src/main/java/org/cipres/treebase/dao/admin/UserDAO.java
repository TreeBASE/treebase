
package org.cipres.treebase.dao.admin;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserHome;
import org.cipres.treebase.domain.admin.UserRole;
import org.cipres.treebase.domain.study.Submission;

/**
 * UserDAO.java
 * 
 * Created on September 29, 2005
 * 
 * @author Jin Ruan
 * 
 */
public class UserDAO extends AbstractDAO implements UserHome {

	private static final Logger LOGGER = Logger.getLogger(UserDAO.class);

	/**
	 * Constructor.
	 */
	public UserDAO() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findByUserName(java.lang.String)
	 */
	public User findByUserName(String pUserName) {
		if (TreebaseUtil.isEmpty(pUserName)) {
			return null;
		}

		// The class name, property name (e.g. User, userName) is case sensitive.
		// use the java bean name "userName" not the actual variable name: mUserName.
		// The search is the search case sensitive. case insensitive search:
		// String query = "from User where lower(userName) = lower(:username)";

		String query = "from User where userName = :username";
		List result = getHibernateTemplate().findByNamedParam(query, "username", pUserName);
		if (result == null || result.isEmpty()) {
			return null;
		}

		return (User) result.get(0);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#delete(org.cipres.treebase.domain.admin.User)
	 */
	public void delete(User pUser) {

		// relationship managmenet:
		// * if the user has submission:
		Iterator<Submission> iter = pUser.getSubmissionIterator();
		while (iter.hasNext()) {
			Submission element = iter.next();
			element.setSubmitter(null);
		}

		super.deletePersist(pUser);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findByEmail(java.lang.String, boolean)
	 */
	public List<User> findByEmail(String pEmailAddress, boolean pExactMatch) {
		if (TreebaseUtil.isEmpty(pEmailAddress)) {
			return null;
		}

		// The search is case insensitive search:
		if (pExactMatch) {
			String query = "from User u where lower(u.person.emailAddress.emailAddressString) = :email";
			List result = getHibernateTemplate().findByNamedParam(
				query,
				"email",
				pEmailAddress.toLowerCase());

			// return (User) result.get(0);
			return result;
		} else {
			Criteria c = getSession().createCriteria(User.class).createAlias("person", "p");
			c.add(Restrictions.ilike(
				"p.emailAddress.emailAddressString",
				pEmailAddress,
				MatchMode.ANYWHERE));

			return c.list();
		}

	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#getUserRole()
	 */
	public UserRole getUserRole() {
		String userRole = UserRole.ROLE_USER;
		return findUserRole(userRole);
	}

	/**
	 * 
	 * @param pUserRole
	 * @return
	 */
	public UserRole findUserRole(String pUserRole) {
		String query = "from UserRole where authority = :role";
		List result = getHibernateTemplate().findByNamedParam(query, "role", pUserRole);
		if (result == null || result.isEmpty()) {
			return null;
		}

		return (UserRole) result.get(0);
	}

	/**
	 * 
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findUserByLastName(java.lang.String)
	 */
	public List<User> findUserByLastName(String pLastName) {
		if (TreebaseUtil.isEmpty(pLastName)) {
			return null;
		}

		// The search is case insensitive search:
		// The search uses wildcards.
		String query = "from User u where lower(u.person.lastName) like :lname";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("IN FIND USER BY LAST NAME: " + pLastName); //$NON-NLS-1$
		}

		List result = getHibernateTemplate().findByNamedParam(
			query,
			"lname",
			'%' + pLastName.toLowerCase() + '%');

		return result;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findByUserNameLike(java.lang.String)
	 */
	public List<User> findByUserNameLike(String pUserName) {
		List<User> returnVal = new ArrayList<User>();

		if (!TreebaseUtil.isEmpty(pUserName)) {
			Criteria c = getSession().createCriteria(User.class);

			c.add(Expression.ilike("username", '%' + pUserName + '%'));
			returnVal = c.list();
		}

		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findbyUserRole(java.lang.String)
	 */
	public List<User> findbyUserRole(String pUserRole) {
		List<User> users = new ArrayList<User>();

		if (!TreebaseUtil.isEmpty(pUserRole)) {
			String query = "select u from User as u where u.role.authority = :role";
			List result = getHibernateTemplate().findByNamedParam(query, "role", pUserRole);

			users.addAll(result);
		}
		return users;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findByPerson(org.cipres.treebase.domain.admin.Person)
	 */
	public User findByPerson(Person pPerson) {
		if (pPerson == null) {
			return null;
		}
		
		Criteria c = getSession().createCriteria(User.class);
		c.add(Restrictions.eq("person", pPerson));
		
		return (User) c.uniqueResult();
	}

}
