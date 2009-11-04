
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
