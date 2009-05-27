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

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserHome;
import org.cipres.treebase.domain.admin.UserRole;
import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.framework.ExecutionResult;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * UserServiceImpl.java
 * 
 * Created on Oct 13, 2005
 * 
 * @author Jin Ruan
 * 
 */
public class UserServiceImpl extends AbstractServiceImpl implements UserService {

	private UserHome mUserHome;

	/**
	 * Constructor.
	 */
	public UserServiceImpl() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getUserHome();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserService#createUser(org.cipres.treebase.domain.admin.User)
	 */
	public ExecutionResult createUser(User pUser) {
		ExecutionResult exeResult = new ExecutionResult();

		if (pUser == null) {
			exeResult.addErrorMessage("The user object is empty.");
		} else {

			// the match is case sensitive.
			if (findUserByName(pUser.getUsername()) != null) {
				exeResult.addErrorMessage("Username is already taken.  Please try another one.");
			}

			// the match is case insensitive.
			List<User> existingUsers = findUserByEmail(pUser.getEmailAddressString());

			if (existingUsers != null && !existingUsers.isEmpty()) {
				exeResult
					.addErrorMessage("An account has already been created for this email address.");
			}

			if (exeResult.isSuccessful()) {
				pUser.setRole(getUserHome().getUserRole());
				getUserHome().store(pUser);
			}
		}
		return exeResult;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserService#deleteUser(java.lang.String)
	 */
	public void deleteUser(String pUserName) {
		User user = findUserByName(pUserName);
		if (user == null) {
			throw new EntityNotFoundException("Delete User: cannot find user " + pUserName);
		} else {
			getUserHome().delete(user);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserService#findUserByName(java.lang.String)
	 */
	public User findUserByName(String pUserName) {
		return getUserHome().findByUserName(pUserName);
	}

	/**
	 * Return the UserHome field.
	 * 
	 * @return UserHome
	 */
	private UserHome getUserHome() {
		return mUserHome;
	}

	/**
	 * Set the UserHome field.
	 */
	public void setUserHome(UserHome pNewUserHome) {
		mUserHome = pNewUserHome;
	}

	/**
	 * 
	 * @see org.acegisecurity.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String pUsername)
		throws UsernameNotFoundException,
			DataAccessException {

		UserDetails user = findUserByName(pUsername);
		if (user == null) {
			throw new UsernameNotFoundException("user not found:" + pUsername);
		}

		return user;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserService#findUserByEmail(java.lang.String)
	 */
	public List<User> findUserByEmail(String pEmailAddress) {
		return getUserHome().findByEmail(pEmailAddress, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.admin.UserService#findUserByLastName(java.lang.String)
	 */
	public List<User> findUserByLastName(String pLastName) {
		return getUserHome().findUserByLastName(pLastName);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.UserService#moveSubmissions(java.lang.String,
	 *      java.lang.String)
	 */
	public int moveSubmissions(String pSourceUsername, String pTargetUsername) {

		// just return if there is nothing to move
		if (TreebaseUtil.isEmpty(pSourceUsername) || TreebaseUtil.isEmpty(pTargetUsername)
			|| pSourceUsername.equals(pTargetUsername)) {
			return 0;
		}

		User userSource = findUserByName(pSourceUsername);
		User userTarget = findUserByName(pTargetUsername);

		if (userSource.getSubmissionCount() == 0) {
			return 0;
		}

		Collection<Submission> subCopy = userSource.getSubmissionsCopy();

		int count = 0;
		for (Submission aSubmission : subCopy) {

			userSource.removeSubmission(aSubmission);
			userTarget.addSubmission(aSubmission);

			count++;
		}

		return count;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.admin.UserService#findUserRole(java.lang.String)
	 */
	public UserRole findUserRole(String pUserRole) {
		return getUserHome().findUserRole(pUserRole);
	}

	@Override
	public Class defaultResultClass() {
		return User.class;
	}

}
