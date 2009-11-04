
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