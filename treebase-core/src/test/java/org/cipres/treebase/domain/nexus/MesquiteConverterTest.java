
package org.cipres.treebase.domain.nexus;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.cipres.treebase.domain.nexus.mesquite.MesquiteConverter;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.tree.PhyloTree;

import junit.framework.TestCase;

/**
 * MesquiteConverterTest.java
 * 
 * Created on May 29, 2008
 * 
 * @author Jin Ruan
 * 
 */
public class MesquiteConverterTest extends TestCase {
	private static final Logger logger = LogManager.getLogger(MesquiteConverterTest.class);

	/**
	 * Constructor.
	 * 
	 * @param name
	 */
	public MesquiteConverterTest(String name) {
		super(name);
	}

	/**
	 * Test method for
	 * {@link org.cipres.treebase.domain.nexus.mesquite.MesquiteConverter#buildNodesFromNewick(org.cipres.treebase.domain.tree.PhyloTree, java.util.Collection, java.lang.String)}.
	 */
	public void testBuildNodesFromNewick() throws Exception {
		String testName = "testBuildNodesFromNewick";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

//		1	Heppiella_verticillata,
//		2	Heppiella_ulmifolia,
//		3	Heppiella_viscida,
//		4	Heppiella_repens,
//		5	Gloxinia,
//	;
//	TREE T260c2x3x96c12c17c23 =  [&R] (5,(4,1,3,2));
		
		
		// 1. create a list of taxon labels and a tree:
		String newName = testName + " test " + Math.random();

		PhyloTree tree = new PhyloTree();
		tree.setTitle(newName);
		
		List<TaxonLabel> labels = new ArrayList<TaxonLabel>();
		labels.add(new TaxonLabel("Heppiella_verticillata"));
		labels.add(new TaxonLabel("Heppiella_ulmifolia"));
		labels.add(new TaxonLabel("Heppiella_viscida"));
		labels.add(new TaxonLabel("Heppiella_repens"));
		labels.add(new TaxonLabel("Gloxinia"));
		
		//String newick2 = "[&R] (Gloxinia,Gloxinia_1234,(Heppiella_repens,Heppiella_verticillata,Heppiella_viscida,Heppiella_ulmifolia))";
		String newick = "[&R] (Gloxinia,(Heppiella_repens,Heppiella_verticillata,Heppiella_viscida,Heppiella_ulmifolia))";
		
//		Parser mesqParser = new Parser(newick2);
//		String aToken = mesqParser.getNextToken();
//		int i =0;
//		while (!TreebaseUtil.isEmpty(aToken)) {
//			logger.debug(" " + i+ ": " + aToken);
//			i++;
//			aToken = mesqParser.getNextToken();
//		}
//
		
//		String newick = "[&R] (5,(4,1,3,2))";
		
		// 2. test
		MesquiteConverter converter = new MesquiteConverter();
		converter.buildNodesFromNewick(tree, labels, newick);
		
		//3. verify:
		tree.updateNewickString();
		logger.debug("treenode size=" + tree.getTreeNodesReadOnly().size() + "\n" + tree.getNewickString());
		//assertTrue(condition);
		
		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

}
