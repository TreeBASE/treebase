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

import org.cipres.treebase.service.AbstractService;

/**
 * PersonService.java
 * 
 * Created on May 9, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface PersonService extends AbstractService {

	/**
	 * Return a Person object by id.
	 * 
	 * @param pPersonID
	 * @return
	 */
	Person findByID(Long pPersonID);

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
	 * For a given Person, find potential duplicates from the database.
	 * 
	 * @param pPerson
	 * @return
	 */
	Collection<Person> findPotentialDuplicate(Person pPerson);
	
	/**
	 * Store the person object to the database.
	 * 
	 * @param pPerson
	 */
	//void createPerson(Collection<Person> pPersons);
	
	/**
	 * Store the person object to the database.
	 * 
	 * @param pPerson
	 */
	void createPerson(Person pPerson);

	/**
	 * Delete the person object from the database. Throws exception if the person has associated
	 * User or Citation.
	 * 
	 * @param pPerson
	 */
	void deletePerson(Person pPerson);

	/**
	 * Merge the source person to the target person. Following are affected:
	 *  ** If there are two user accounts, then merge the source to the target person. 
	 *  ** Merge author/editor records
	 *  ** delete the source person record.
	 * 
	 * @param pSourceId
	 * @param pTargetId
	 * @return the count of changed submissions / authors / editors. 
	 */
	int mergePerson(Long pSourceId, Long pTargetId);
}
