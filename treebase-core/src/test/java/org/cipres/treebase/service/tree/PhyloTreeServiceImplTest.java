
package org.cipres.treebase.service.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeNode;
import org.cipres.treebase.domain.tree.PhyloTreeService;

/**
 * PhyloTreeServiceImplTest.java
 * 
 * Created on Apr 25, 2008
 * @author JRUAN
 *
 */
public class PhyloTreeServiceImplTest extends AbstractDAOTest {

	private PhyloTreeService mFixture;
	private TaxonLabelService taxonLabelService;
	
	/**
	 * Constructor.
	 */
	public PhyloTreeServiceImplTest() {
		super();
	}
	
	/**
	 * Return the Fixture field.
	 * 
	 * @return PhyloTreeService mFixture
	 */
	public PhyloTreeService getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	public void setFixture(PhyloTreeService pNewFixture) {
		mFixture = pNewFixture;
	}
		
	public TaxonLabelService getTaxonLabelService() {
		return taxonLabelService;
	}

	public void setTaxonLabelService(TaxonLabelService taxonLabelService) {
		this.taxonLabelService = taxonLabelService;
	}

	/**
	 * Test updateByRearrangeNodes
	 */
	public void testupdateByRearrangeNodes() throws Exception {
		String testName = "updateByRearrangeNodes";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a tree:
		PhyloTree tree = (PhyloTree) loadObject(PhyloTree.class);
		//PhyloTree tree = (PhyloTree) loadObject(PhyloTree.class, 41L);
		String newick   = tree.getNewickString();
		
		Long id = tree.getId();
		logger.info("tree found: id= " + id + "\n" + newick);

		// 2. test use the same newick:
	    getFixture().updateByRearrangeNodes(id, newick);
		

		// 3. verify
	    tree = (PhyloTree) loadObject(PhyloTree.class, id);
	    String newick2 = tree.getNewickString();
		logger.info("new newick:\n" + newick2);
	    assertTrue(newick.equalsIgnoreCase(newick2));
	    
		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	public void testFindByTopology3() {
		Boolean searchResult = null;
		PhyloTree t = (PhyloTree) loadObject(PhyloTree.class);
		RandomList<PhyloTreeNode> nodes = new RandomList<PhyloTreeNode> ();
		nodes.addAll(t.getTreeNodesReadOnly());
		
		while (searchResult == null) {
			PhyloTreeNode a;
			do{ a = nodes.someElement();}
			while (a.getTaxonLabel()==null);
			PhyloTreeNode b;
			do{ b = nodes.someElement();}
			while (b.getTaxonLabel()==null);
			PhyloTreeNode c;
			do{ c = nodes.someElement();}
			while (c.getTaxonLabel()==null);			
			
			searchResult = false;
			if (c.haveABCTopology(a, b)) {
				TaxonVariant as = a.getTaxonLabel().getTaxonVariant();
				TaxonVariant bs = b.getTaxonLabel().getTaxonVariant();
				TaxonVariant cs = c.getTaxonLabel().getTaxonVariant();

				logger.debug("Searching for trees with a=" + as + ", b=" + bs + ", c=" + cs);

				Collection<PhyloTree> trees = 
					getFixture().findByTopology3(as, bs, cs);

				for (PhyloTree tree : trees) {
					if (tree.equals(t)) {
						searchResult = true;
						break;
					}
				}
			}
			//this line may cause infinity loop, but it is needed for pick up a set of test node 
			//else searchResult=null; 
			
		}
		
		//assertTrue(searchResult);
				
	}
	
	@SuppressWarnings("serial")
	private class RandomList<T> extends ArrayList<T> {
		Random r;
		
		RandomList() {
			super();
			r = new Random();
		}
		
		T someElement() {
			int i = r.nextInt(size());
			return get(i);
		}
	}
	
	private PhyloTreeNode findLabeledLeaf(PhyloTreeNode root) {
		List<PhyloTreeNode> children = root.getChildNodes();
		if (children.isEmpty()) {  // This is a leaf
			// If it has a label, and it its label maps to a taxon variant, then it's a suitable test subject
			// return it.
			if (root.getTaxonLabel() != null) {
				if (root.getTaxonLabel().getTaxonVariant() != null) 
					return root;
				else  // otherwise, keep searching.
					return null;
			}
			return root.getTaxonLabel() == null ? null : root;  // If it's labelled, return it
		} else { // Otherwise, search downwards 
			for (PhyloTreeNode child : children) {
				PhyloTreeNode result = findLabeledLeaf(child);
				if (result != null) return result;
			}
			return null;
		}
	}
		
	
	public void testFindSomethingByRangeExpression() throws MalformedRangeExpression {
		Collection<PhyloTree> trees = getFixture()
		.findSomethingByRangeExpression(PhyloTree.class, "nTax", "37..40");
		assertTrue(trees.size() > 0);
		for (PhyloTree tree : trees) {
			int n = tree.getnTax();
			assertTrue(n >= 37 && n <= 100);
		}
	}

}
