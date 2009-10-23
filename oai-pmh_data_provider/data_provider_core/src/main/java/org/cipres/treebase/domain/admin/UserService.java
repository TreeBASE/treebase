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

package org.cipres.treebase.domain.admin;

import java.util.List;

import org.acegisecurity.userdetails.UserDetailsService;

import org.cipres.treebase.framework.ExecutionResult;
import org.cipres.treebase.service.AbstractService;

/**
 * UserService.java
 * 
 * Created on Oct 13, 2005
 * 
 * @author Jin Ruan
 * 
 */
public interface UserService extends AbstractService, UserDetailsService {

	/**
	 * Store the new user and relevant information to the database. This method validates the user
	 * name or the email address is not already in the system.
	 * 
	 * @param pUser
	 * @return execution result indicates whether the action is successful, otherwise a message.
	 */
	ExecutionResult createUser(User pUser);

	/**
	 * Delete the user and relevant information from the database. Throws exception if the user is
	 * not found.
	 * 
	 * @param pUserName
	 */
	void deleteUser(String pUserName);

	/**
	 * Move all submissions from source user to the target user.
	 * 
	 * @param pSourceUsername
	 * @param pTargetUsername
	 * @return the number of moved submissions.
	 */
	int moveSubmissions(String pSourceUsername, String pTargetUsername);

	/**
	 * TODO: for testing only.
	 * 
	 * Find a user by the user name. Return null if no match is found. The search is case sensitive.
	 * 
	 * Creation date: Oct 13, 2005 3:27:58 PM
	 * 
	 * @param pUserName
	 * @return
	 */
	User findUserByName(String pUserName);

	/**
	 * Find user role by description. 
	 * 
	 * @param pUserRole
	 * @return
	 */
	UserRole findUserRole(String pUserRole);
	
	/**
	 * TODO: for testing only.
	 * 
	 * Find a user by the email address. Return null if no match is found. The search is case
	 * insensitive.
	 * 
	 * @param pEmailAddress
	 * @return
	 */
	List<User> findUserByEmail(String pEmailAddress);

	/**
	 * 
	 * 
	 * @param pLastName
	 * @return
	 */
	List<User> findUserByLastName(String pLastName);

}
