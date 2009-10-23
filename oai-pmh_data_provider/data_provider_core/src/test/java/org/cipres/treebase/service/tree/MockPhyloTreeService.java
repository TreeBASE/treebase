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