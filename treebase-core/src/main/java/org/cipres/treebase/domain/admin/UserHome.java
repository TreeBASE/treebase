
package org.cipres.treebase.domain.admin;

import java.util.List;

import org.cipres.treebase.domain.DomainHome;

/**
 * The home interface for the User domain objects.
 * 
 * Created on Sep 29, 2005
 * 
 * @author Jin Ruan
 * 
 */
public interface UserHome extends DomainHome {

	/**
	 * Find a user by the user name. Return null if no match is found. The search is case sensitive.
	 * 
	 * @param pUserName String
	 * @return User
	 */
	User findByUserName(String pUserName);

	/**
	 * Find a user by the user name. Wildcard searches are used to include non-exact matches.
	 * The search is case insensitive.
	 * 
	 * @param pUserName String
	 * @return List<User>
	 */
	List<User> findByUserNameLike(String pUserName);

	/**
	 * Find a user by the person record. 
	 * Return null if no match is found.
	 * 
	 * @param pPerson
	 * @return User
	 */
	User findByPerson(Person pPerson);

	/**
	 * Delete the instance from the database.
	 * 
	 * @param pUser
	 */
	void delete(User pUser);

	/**
	 * Find a user by the email address. Return null if no match is found. The search is case
	 * insensitive.
	 * 
	 * 
	 * @param pEmailAddress
	 * @param pExactMatch
	 * @return
	 */
	List<User> findByEmail(String pEmailAddress, boolean pExactMatch);

	/**
	 * @param pLastName, find list of users with same last name.
	 * @return
	 */
	List<User> findUserByLastName(String pLastName);

	/**
	 * Find user role by description. 
	 * 
	 * @param pUserRole
	 * @return
	 */
	UserRole findUserRole(String pUserRole);
	
	/**
	 * Return the user role for a regular user.
	 * 
	 * @return
	 */
	UserRole getUserRole();

	/**
	 * Find all users for a specific role. 
	 * 
	 * @param pUserRole
	 * @return
	 */
	List<User> findbyUserRole(String pUserRole);
}
