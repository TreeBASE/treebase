/*
 * Copyright 2005 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.service.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserHome;
import org.cipres.treebase.domain.admin.UserRole;

/**
 * Mock object for unit testing.
 * 
 * Created on October 14, 2005
 * 
 * @author Jin Ruan
 */
public class DummyUserDAO extends AbstractDAO implements UserHome {

	private User mStoredUser;
	private User mNewUser;

	/**
	 * Constructor.
	 */
	public DummyUserDAO() {
		super();
	}

	/**
	 * Return the NewUser field. Uses lazy initialization.
	 * 
	 * @return User mNewUser
	 */
	public User getNewUser() {
		if (mNewUser == null) {
			mNewUser = new User();

			mNewUser.setUsername("newUser");
			mNewUser.setFirstName("New");
			mNewUser.setLastName("User");
			mNewUser.setPassword("dummyPasswd");
			mNewUser.setEmailAddressString("dummy@sdsc.edu");
		}
		return mNewUser;
	}

	/**
	 * Return the DummyUser field. Uses lazy initialization.
	 * 
	 * @return User mDummyUser
	 */
	public User getStoredUser() {
		if (mStoredUser == null) {
			mStoredUser = new User();

			mStoredUser.setUsername("dummyUser");
			mStoredUser.setFirstName("Dummy");
			mStoredUser.setLastName("User");
			mStoredUser.setPassword("dummyPasswd");
			mStoredUser.setEmailAddressString("dummy@sdsc.edu");
		}
		return mStoredUser;
	}

	/**
	 * @param pDummyUser The dummyUser to set.
	 */
	@SuppressWarnings("unused")
	private void setStoredUser(User pDummyUser) {
		mStoredUser = pDummyUser;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findByUserName(java.lang.String)
	 */
	public User findByUserName(String pUserName) {
		if (getStoredUser().getUsername().equalsIgnoreCase(pUserName)) {
			return getStoredUser();
		}
		return null;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#delete(org.cipres.treebase.domain.admin.User)
	 */
	public void delete(User pUser) {
		if (pUser != null && !getStoredUser().equals(pUser)) {
			// not good. I cannot handle other user:
			throw new RuntimeException("I cannot delete user:" + pUser.getUsername());
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#refresh(org.cipres.treebase.domain.TBPersistable)
	 */
	@Override
	public void refresh(TBPersistable pPersistable) {
		if (!getStoredUser().equals(pPersistable)) {
			// not good. I cannot handle other user:
			throw new RuntimeException("I cannot handle refresh user:" + pPersistable);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#refreshAll(java.util.Collection)
	 */
	@Override
	public void refreshAll(Collection<? extends TBPersistable> pDomainObjects) {}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#store(org.cipres.treebase.domain.TBPersistable)
	 */
	@Override
	public void store(TBPersistable pPersistable) {
		if (!getNewUser().equals(pPersistable)) {
			if (getStoredUser().equals(pPersistable)) {
				// not good. it is already stored.
				throw new RuntimeException("The user is already stored:" + pPersistable);
			} else {
				// not good. I cannot handle other user:
				throw new RuntimeException("I cannot handle store user:" + pPersistable);
			}
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.DomainHome#flush()
	 */
	@Override
	public void flush() {}

	/**
	 * 
	 * @see org.cipres.treebase.dao.AbstractDAO#merge(T)
	 */
	@Override
	public <T extends TBPersistable> T merge(T pPersistable) {
		return pPersistable;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findByEmail(java.lang.String)
	 */
	public List<User> findByEmail(String pEmailAddress, boolean pExactMatch) {

		if (getStoredUser().getEmailAddressString().equalsIgnoreCase(pEmailAddress)) {
			List<User> result = new ArrayList<User>();
			result.add(getStoredUser());
			return result;
		}

		return null;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#getUserRole()
	 */
	public UserRole getUserRole() {
		UserRole role = new UserRole();
		role.setAuthority("User");
		return role;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findUserByLastName(java.lang.String)
	 */
	public List<User> findUserByLastName(String pLastName) {
		List<User> users = new ArrayList<User>();
		
		if (getStoredUser().getLastName().equalsIgnoreCase(pLastName)) {
			users.add(getStoredUser());
		}
		
		return users;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findByUserNameLike(java.lang.String)
	 */
	public List<User> findByUserNameLike(String pUserName) {
		if (getStoredUser().getUsername().equalsIgnoreCase(pUserName)) {
			List<User> result = new ArrayList<User>();
			result.add(getStoredUser());
			return result;
		}

		return null;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findUserRole(java.lang.String)
	 */
	public UserRole findUserRole(String pUserRole) {
		if (UserRole.ROLE_USER.equals(pUserRole)) {
			UserRole role = new UserRole();
			role.setAuthority("User");
			return role;
		}
		return null;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.admin.UserHome#findbyUserRole(java.lang.String)
	 */
	public List<User> findbyUserRole(String pUserRole) {
		List<User> users = new ArrayList<User>();
		
		if (UserRole.ROLE_USER.equals(pUserRole)) {
			users.add(getStoredUser());
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
		
		if (getStoredUser().getFirstName().equals(pPerson.getFirstName())
			&& getStoredUser().getLastName().equals(pPerson.getLastName())
			&& getStoredUser().getEmailAddressString().equals(pPerson.getEmailAddressString())) {
			
			return getStoredUser();
		}
		
		return null;
	}

}
