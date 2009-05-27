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

package org.cipres.treebase.service.admin;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.PersonHome;
import org.cipres.treebase.domain.admin.PersonService;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserHome;
import org.cipres.treebase.domain.study.CitationHome;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * PersonServiceImpl.java
 * 
 * Created on May 9, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class PersonServiceImpl extends AbstractServiceImpl implements PersonService {
	private static final Logger LOGGER = Logger.getLogger(PersonServiceImpl.class);

	private PersonHome mPersonHome;
	private UserHome mUserHome;
	private CitationHome mCitationHome;

	/**
	 * Constructor.
	 */
	public PersonServiceImpl() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getPersonHome();
	}

	/**
	 * Return the PersonHome field.
	 * 
	 * @return PersonHome mPersonHome
	 */
	private PersonHome getPersonHome() {
		return mPersonHome;
	}

	/**
	 * Set the PersonHome field.
	 */
	public void setPersonHome(PersonHome pNewPersonHome) {
		mPersonHome = pNewPersonHome;
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
	 * Return the CitationHome field.
	 * 
	 * @return CitationHome mCitationHome
	 */
	public CitationHome getCitationHome() {
		return mCitationHome;
	}

	/**
	 * Set the CitationHome field.
	 */
	public void setCitationHome(CitationHome pNewCitationHome) {
		mCitationHome = pNewCitationHome;
	}
	
	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.PersonService#findPotentialDuplicate(org.cipres.treebase.domain.admin.Person)
	 */
	public Collection<Person> findPotentialDuplicate(Person pPerson) {
		if (pPerson == null) {
			return new ArrayList<Person>();
		}

		return getPersonHome().findByLastName(pPerson.getLastName());
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.PersonService#deletePerson(org.cipres.treebase.domain.admin.Person)
	 */
	public void deletePerson(Person pPerson) {
		if (pPerson != null) {

			// An excpetion is thrown if the person has associated user or citation.
			getPersonHome().deletePersist(pPerson);
		}

	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.PersonService#findByID(java.lang.Long)
	 */
	public Person findByID(Long pPersonID) {
		if (pPersonID == null) {
			return null;
		}
		return getPersonHome().findPersistedObjectByID(Person.class, pPersonID);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.PersonService#createPerson(org.cipres.treebase.domain.admin.Person)
	 */
	public void createPerson(Person pPerson) {
		if (pPerson != null) {
			getPersonHome().store(pPerson);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.PersonService#findByExactMatch(org.cipres.treebase.domain.admin.Person)
	 */
	public Person findByExactMatch(Person pPerson) {
		return getPersonHome().findByExactMatch(pPerson);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.admin.PersonService#mergePerson(java.lang.Long,
	 *      java.lang.Long)
	 */
	public int mergePerson(Long pSourceId, Long pTargetId) {
		Person personSrc = getPersonHome().findPersistedObjectByID(Person.class, pSourceId);
		Person personTarget = getPersonHome().findPersistedObjectByID(Person.class, pTargetId);
		if (personSrc == null || personTarget == null) {
			return -1;
		}

		// If there are two user accounts, then both are pointing to the same target person.
		// Merge author/editor records
		// delete the source person record.
		
		int changedCount = 0;
		User userSrc = getUserHome().findByPerson(personSrc);
		if (userSrc != null) {
			userSrc.setPerson(personTarget);

			User userTarget = getUserHome().findByPerson(personTarget);
			if (userTarget != null && userSrc.getSubmissionCount() > 0) {
				// Move all submissions:
				Collection<Submission> subCopy = userSrc.getSubmissionsCopy();

				for (Submission aSubmission : subCopy) {

					userSrc.removeSubmission(aSubmission);
					userTarget.addSubmission(aSubmission);

					changedCount++;
				}
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("submission moved count=" + changedCount); //$NON-NLS-1$
			}
		}

		// merge author/editor:
		changedCount += getCitationHome().replaceAuthorAndEditor(personSrc, personTarget);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("replace author/editor count=" + changedCount); //$NON-NLS-1$
		}
		
		//delete source:
		deletePerson(personSrc);
	
		return changedCount;
	}

	@Override
	public Class defaultResultClass() {
		return Person.class;
	}
}
