
package org.cipres.treebase.domain.study;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * The home interface for the Study domain objects.
 * 
 * Created on Sep 29, 2005
 * 
 * @author Jin Ruan
 * 
 */
public interface StudyHome extends DomainHome {

	/**
	 * Find a study by the accession number. Return null if no match is found. The search is case
	 * sensitive.
	 * 
	 * @param pAccessionNumber String
	 * @return Study
	 */
	Study findByAccessionNumber(String pAccessionNumber);

	/**
	 * Find studies by name. Return an empty set if no match is found.
	 * 
	 * @param pName
	 * @param pCaseSensitive
	 * 
	 * @return Collection<Study>
	 */
	Collection<Study> findByName(String pStudyName, boolean pCaseSensitive);

	/**
	 * Find studies by an author. Return an empty set if no match is found.
	 * 
	 * @param pAuthor Person
	 * @return Set<Study>
	 */
	Collection<Study> findByAuthor(Person pAuthor);
	
	/**
	 * Find studies by journal name.
	 * 
	 * @param pJournal
	 * @return Collection<Study>
	 */
	Collection<Study> findByJournal(String pJournal);
	
	/**
	 * Find studies by journal name, with exact matching
	 * 
	 * @param pJournal
	 * @param pExactMatch
	 * @return Collection<Study>
	 */
	Collection<Study> findByJournal(String pJournal, boolean pExactMatch);

	/**
	 * Find studies submitted by an user. Return an empty set if no match is found.
	 * 
	 * @param pUser User
	 * @return Set<Study>
	 */
	Collection<Study> findBySubmitter(User pUser);

	/**
	 * Advanced study query by criteria.
	 * 
	 * @param pCriteria
	 * @return
	 */
	Collection<Study> findByCriteria(StudyCriteria pCriteria);
	
	/*
	 * mjd
	 */
	void cleanCache();

	Collection<Study> findByKeyword(String keyword);
	Collection<Study> findByAuthor(String pAuthorName);
	Collection<Study> findByTitle(String pTitle);
	Collection<Study> findByAbstract(String pAbstract);
	Collection<Study> findByCitation(String citationWord);

	Collection<Study> findByTB1StudyID(String studyID);

	void setTBStudyID(Study theStudy, String studyID);

	/**
	 * @param trees
	 * @return all studies that contain any of the specified trees
	 */
	Collection<Study> findByTrees(Set<PhyloTree> trees);

	/**
	 * Find the studies that contain a taxonlabel that looks like this
	 * 
	 * @param taxonLabel
	 * @return
	 * @author mjd 20080813
	 */
	Collection<Study> findByTaxonLabelName(String taxonLabel);
	
	/**
	 * Find the studies that were published within the specified date range
	 * @param from
	 * @param until
	 * @return
	 */
	Collection<Study> findByPublicationDateRange(Date from, Date until);

}
