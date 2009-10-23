/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.service.study;

import java.io.File;
import java.util.Collection;
import java.util.Set;

import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyCriteria;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * Base class for mock service objects that implement the StudyService interface
 * 
 * <p>This class implements the {@see StudyService} interface, but every method
 * throws an {@see UnimplementedServiceException} when called.  Subclasses of this one
 * can implement {@see StudyService} by overriding some of the stub methods with actual
 * implementations.</p>
 * 
 * @author mjd 20080805
 *
 */
public abstract class MockStudyService implements StudyService {

	public void addNexusFiles(Study study, Collection<File> nexusFiles) {
		throw new UnimplementedServiceException();
	}

	public void cleanCache() {
		throw new UnimplementedServiceException();
	}

	public boolean deleteStudy(Study study) {
		throw new UnimplementedServiceException();
	}
	
	public boolean deleteStudy(Long studyId) {
		throw new UnimplementedServiceException();
	}

	public Collection<Study> findByAbstract(String abstractWord) {
		throw new UnimplementedServiceException();
	}

	public Collection<Study> findByAuthor(String authorName) {
		throw new UnimplementedServiceException();
	}

	public Collection<Study> findByCitation(String citationWord) {
		throw new UnimplementedServiceException();
	}

	public Collection<Study> findByCriteria(StudyCriteria criteria) {
		throw new UnimplementedServiceException();
	}

	public Study findByID(Long studyID) {
		throw new UnimplementedServiceException();
	}

	public Collection<Study> findByKeyword(String keyword) {
		throw new UnimplementedServiceException();
	}

	public Collection<Study> findByName(String studyName, boolean caseSensitive) {
		throw new UnimplementedServiceException();
	}

	public Study findBySubmissionID(Long submissionID) {
		throw new UnimplementedServiceException();
	}

	public Study findBySubmissionNumber(String submissionNumber) {
		throw new UnimplementedServiceException();
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.study.StudyService#findByTaxonLabelName(java.lang.String)
	 */
	public Collection<Study> findByTaxonLabelName(String taxonLabel) {
		throw new UnimplementedServiceException();
	}

	public Collection<Study> findByTBStudyID(String studyID) {
		throw new UnimplementedServiceException();
	}

	public Collection<Study> findByTitle(String title) {
		throw new UnimplementedServiceException();
	}

	public Collection<Study> findByTrees(Set<PhyloTree> trees) {
		throw new UnimplementedServiceException();
	}

	public <T extends TBPersistable> Collection<T> findSomethingBySubstring(
			Class T, String attributeName, String target) {
		throw new UnimplementedServiceException();
	}

	public <T extends TBPersistable> Collection<T> findSomethingBySubstring(
			Class T, String attributeName, String target, Boolean caseSensitive) {
		throw new UnimplementedServiceException();
	}

	public String generateReconstructedNexusFile(Long submissionId,
			String fileName) {
		throw new UnimplementedServiceException();
	}

	public String generateXMLStringByKeyword(String keyword) {
		throw new UnimplementedServiceException();
	}

	public TBPermission getPermission(String username, Long studyId) {
		throw new UnimplementedServiceException();
	}

	public <T extends TBPersistable> T resurrect(T persistable) {
		throw new UnimplementedServiceException();
	}

	public <T extends TBPersistable> Collection<T> resurrectAll(
			Collection<T> persistable) throws InstantiationException {
		throw new UnimplementedServiceException();
	}

	public <T extends TBPersistable> Long save(T persistable) {
		throw new UnimplementedServiceException();
	}

	public void setTBStudyID(Study theStudy, String studyID) {
		throw new UnimplementedServiceException();
	}

	public <T extends TBPersistable> T update(T persistable) {
		throw new UnimplementedServiceException();
	}

	public <T extends TBPersistable> Collection<T> updateCollection(
			Collection<T> persistables) {
		throw new UnimplementedServiceException();
	}

}