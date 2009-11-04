
package org.cipres.treebase.service.tree;


import java.util.Collection;
import java.util.Set;

import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;

/**
 * Base class for mock service objects that implement the PhyloTreeService interface
 * 
 * <p>This class implements the {@see PhyloTreeService} interface, but every method
 * throws an {@see UnimplementedServiceException} when called.  Subclasses of this one
 * can implement {@see PhyloTreeService} by overriding some of the stub methods with actual
 * implementations.</p>
 * 
 * @author mjd 20080805
 *
 */
public abstract class MockPhyloTreeService implements PhyloTreeService {

	public boolean deletePhyloTree(PhyloTree phyloTree) {
		throw new UnimplementedServiceException();
	}

	public PhyloTree findByID(Long phyloTreeID) {
		throw new UnimplementedServiceException();
	}

	public Collection<PhyloTree> findByStudies(Set<Study> results) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<PhyloTree> findByStudy(Study study) {
		throw new UnimplementedServiceException();
	}

	public Set<PhyloTree> findByTopology3(String a, String b, String c) {
		throw new UnimplementedServiceException();
	}

	public Set<PhyloTree> findByTopology4a(String a, String b, String c,
			String d) {
		throw new UnimplementedServiceException();
	}

	public Set<PhyloTree> findByTopology4s(String a, String b, String c,
			String d) {
		throw new UnimplementedServiceException();
	}

	public Set<PhyloTree> findTreesByLabel(String label) {
		throw new UnimplementedServiceException();
	}

	public void updateByRearrangeNodes(Long phyloTreeId, String newick) {
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