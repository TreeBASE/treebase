
package org.cipres.treebase.service.matrix;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.Study;

/**
 * Base class for mock service objects that implement the MatrixService interface
 * 
 * <p>This class implements the {@see MatrixService} interface, but every method
 * throws an {@see UnimplementedServiceException} when called.  Subclasses of this one
 * can implement {@see MatrixService} by overriding some of the stub methods with actual
 * implementations.</p>
 * 
 * @author mjd 20080805
 *
 */
public abstract class MockMatrixService implements MatrixService {

	public boolean deleteMatrix(Matrix matrix) {
		throw new UnimplementedServiceException();
	}

	public Matrix findByID(Long matrixID) {
		throw new UnimplementedServiceException();
	}

	public Collection<Matrix> findByNexusFile(String fn) {
		throw new UnimplementedServiceException();
	}

	public Collection<Matrix> findByStudies(Set<Study> results) {
		throw new UnimplementedServiceException();
	}

	public Collection<Matrix> findByStudy(Study study) {
		throw new UnimplementedServiceException();
	}

	public Set<Matrix> findMatricesByTitle(String title) {
		throw new UnimplementedServiceException();
	}

	public List<String> findRowAsString(CharacterMatrix matrix, int start,
			int end) {
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

	public <T extends TBPersistable> T update(T persistable) {
		throw new UnimplementedServiceException();
	}

	public <T extends TBPersistable> Collection<T> updateCollection(
			Collection<T> persistables) {
		throw new UnimplementedServiceException();
	}
	
}