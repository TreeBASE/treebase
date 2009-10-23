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

package org.cipres.treebase.domain.admin;

import java.util.Collection;
import java.util.List;

import org.cipres.treebase.domain.DomainHome;

/**
 * PersonHome.java
 * 
 * Created on May 9, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface PersonHome extends DomainHome {

	/**
	 * Return a list of persons for a given last name.
	 * 
	 * @param pLastName
	 * @return
	 */
	Collection<Person> findByLastName(String pLastName);

	/**
	 * Return a Person object by exact matching.
	 * 
	 * The matching fields are: last name, first name, and email address.
	 * 
	 * @param pPerson
	 * @return
	 */
	Person findByExactMatch(Person pPerson);

	/**
	 * Find a list of email addresses start with the partial match.
	 * Case insensitive. 
	 * 
	 * @param pPartialEmailAddress
	 * @return
	 */
	public List<String> findCompleteEmailAddress(String pPartialEmailAddress);

	/**
	 * Audit method. 
	 * If there are multiple persons with the same lastname and firstname, returns all persons with 
	 * the matching last name. 
	 * 
	 * Note: The query criteria is the same lastname and firstname. However the return list includes
	 * all entries with the same last name satisfying the query. 
	 * 
	 * For example: If there are two John Doe in the system, the returned list would include 
	 * all persons with the last name Doe. 
	 *  
	 * @return
	 */
	public List<Person> findDuplicateWithFirstAndLastNames();
	
	
	/**
	 * Audit method. 
	 * If there are multiple persons with the same lastname, returns all persons with 
	 * the matching last name. 
	 * 
	 * Note: The query criteria is the same lastname. The return list includes
	 * all entries with the same last name satisfying the query. 
	 * 
	 * For example: If there are two Doe in the system, the returned list would include 
	 * all persons with the last name Doe. 
	 *  
	 * @return
	 */	
	public List<Person> findDuplicateWithLastNames();
}
