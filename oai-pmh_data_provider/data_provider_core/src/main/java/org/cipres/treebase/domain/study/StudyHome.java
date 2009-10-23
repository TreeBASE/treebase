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

package org.cipres.treebase.domain.study;

import java.util.Collection;
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
}
