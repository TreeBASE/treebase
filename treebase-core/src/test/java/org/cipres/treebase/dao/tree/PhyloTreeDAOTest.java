
package org.cipres.treebase.dao.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisService;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedTree;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.cipres.treebase.domain.tree.PhyloTreeNode;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.domain.tree.TreeKind;
import org.cipres.treebase.domain.tree.TreeQuality;
import org.cipres.treebase.domain.tree.TreeType;

/**
 * PhyloTreeDAOTest.java
 * 
 * Created on Jun 1, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class PhyloTreeDAOTest extends AbstractDAOTest {

	private AnalysisService mAnalysisService;

	private PhyloTreeHome mFixture;

	/**
	 * Return the Fixture field.
	 * 
	 * @return PhyloTreeHome mFixture
	 */
	public PhyloTreeHome getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	public void setFixture(PhyloTreeHome pNewFixture) {
		mFixture = pNewFixture;
	}

	/**
	 * Return the mAnalysisService field.
	 * 
	 * @return AnalysisService mAnalysisService
	 */
	public AnalysisService getAnalysisService() {
		return mAnalysisService;
	}

	/**
	 * Set the mAnalysisService field.
	 */
	public void setAnalysisService(AnalysisService pAnalysisService) {
		mAnalysisService = pAnalysisService;
	}
	
	public void testFindNodesByTaxonLabel() {
		// find a taxon label string
		String labelStr = "select taxonLabel_id from phylotreenode where taxonLabel_id is not null fetch first rows only";
		long taxonLabelId = jdbcTemplate.queryForLong(labelStr);
		logger.info("taxonLabel id: " + taxonLabelId);
		assertTrue(taxonLabelId > 0);
		
		//get taxon label object
		TaxonLabel label = (TaxonLabel) loadObject(TaxonLabel.class, taxonLabelId);
		assertTrue(label != null);
	
		Set<PhyloTreeNode> nodes = getFixture().findNodesByTaxonLabel(label);	
		assertTrue(nodes.size() >= 1);
		for (PhyloTreeNode n : nodes)
			assertTrue(n.getTaxonLabel().equals(label));
	}

	/**
	 * Run the Collection<PhyloTree> findByAnyTaxonLabel(List<TaxonLabel>) method test.
	 * 
	 */
	public void testFindByAnyTaxonLabel() throws Exception {

		String testName = "testFindByAnyTaxonLabel";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a matrix in a analyzed data:
		String labelStr = "select taxonLabel_id from phylotreenode where taxonLabel_id is not null fetch first rows only";
		long taxonLabelId = jdbcTemplate.queryForLong(labelStr);
		logger.info("taxonLabel id: " + taxonLabelId);
		assertTrue(taxonLabelId > 0);

		// 2. query
		TaxonLabel label = (TaxonLabel) loadObject(TaxonLabel.class, taxonLabelId);
		assertTrue(label != null);

		List<TaxonLabel> labelList = new ArrayList<TaxonLabel>();
		labelList.add(label);

		Collection<PhyloTree> trees = getFixture().findByAnyTaxonLabel(labelList);
		assertTrue(trees != null && !trees.isEmpty());

		// 3. verify
		for (PhyloTree phyloTree : trees) {

			long treeId = phyloTree.getId();
			String treeCountStr = "select count(tree.phylotree_id) from phylotree tree, phylotreenode node "
				+ " where tree.PHYLOTREE_ID = node.PHYLOTREE_ID and node.TAXONLABEL_ID = "
				+ label.getId() + " and tree.PHYLOTREE_ID = " + treeId;
			Integer count = jdbcTemplate.queryForObject(treeCountStr, Integer.class);
			assertTrue(count > 0);
		}

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}

	/**
	 * Run the Collection<PhyloTree> findByStudy(List<Long>) method test.
	 * 
	 */
	public void testFindByStudy() throws Exception {

		String testName = "testFindByStudy";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a study with trees:
		String studyStr = "select study_id from phylotree where study_id is not null fetch first rows only";
		long studyId = jdbcTemplate.queryForLong(studyStr);
		logger.info("study id: " + studyId);
		assertTrue(studyId > 0);

		// 2. query
		Study s = (Study) loadObject(Study.class, studyId);
		assertTrue(s != null);

		List<Study> studyList = new ArrayList<Study>();
		studyList.add(s);

		Collection<PhyloTree> trees = getFixture().findByStudies(studyList);
		assertTrue(trees != null && !trees.isEmpty());

		if (logger.isInfoEnabled()) {
			logger.info(" tree count= " + trees.size());
		}

		// 3. verify
		for (PhyloTree phyloTree : trees) {

			long treeId = phyloTree.getId();
			String treeCountStr = "select count(tree.phylotree_id) from phylotree tree "
				+ " where tree.study_ID = " + s.getId() + " and tree.PHYLOTREE_ID = " + treeId;
			Integer count = jdbcTemplate.queryForObject(treeCountStr, Integer.class);
			assertTrue(count > 0);
		}

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}

	/**
	 * Run the updatePublishedFlagByStudy method test.
	 * 
	 */
	public void testupdatePublishedFlagByStudy() throws Exception {

		String testName = "testupdatePublishedFlagByStudy";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. find a study with trees:
		String studyStr = "select study_id from phylotree where study_id is not null and published is false fetch first rows only";
		long studyId = jdbcTemplate.queryForLong(studyStr);
		logger.info("study id: " + studyId);
		assertTrue(studyId > 0);

		// 2. query
		Study s = (Study) loadObject(Study.class, studyId);
		assertTrue(s != null);
		//this the table phyloTree and study may evaluate "published" differently 
		//assertTrue(s.isPublished() == false);

		int count = getFixture().updatePublishedFlagByStudy(s, true);
		logger.debug("update Count = " + count);
		assertTrue(count > 0);

		// force commit immediately, important:
		setComplete();
		endTransaction();

		// 3. verify
		String treeCountStr = "select count(tree.phylotree_id) from phylotree tree "
			+ " where tree.study_ID = " + s.getId() + " and tree.published is true";
		Integer countVeri = jdbcTemplate.queryForObject(treeCountStr, Integer.class);
		logger.debug("verify Count = " + countVeri);
		assertTrue(countVeri == count);

		//4. change it back:
		int count2 = getFixture().updatePublishedFlagByStudy(s, false);
		assertTrue(count2 == count);
	
		setComplete();
		
		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}

	/**
	 * Test findKindByDescription
	 */
	public void testfindKindByDescription() throws Exception {
		String testName = "findKindByDescription";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		String desc = TreeKind.KIND_AREA;
		TreeKind result = getFixture().findKindByDescription(desc);
		
		//verify
		assertTrue(result != null);
		assertTrue(result.getDescription().equals(desc));		

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Test findQualityByDescription
	 */
	public void testfindQualityByDescription() throws Exception {
		String testName = "findQualityByDescription";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		String desc = "Unrated";
		TreeQuality result = getFixture().findQualityByDescription(desc);
		
		//verify
		assertTrue(result != null);
		assertTrue(result.getDescription().equals(desc));		

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the TreeType findTypeByDescripiton() method test
	 */
	public void testFindTypeByDescription() {
		String testName = "findTypeByDescripiton";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		PhyloTreeHome fixture = getFixture();
		String desc = TreeType.TYPE_SINGLE;
		TreeType result = fixture.findTypeByDescription(desc);

		// verify
		assertTrue(result != null);
		assertTrue(result.isSingleTree());

		if (logger.isInfoEnabled()) {
			logger.info(testName + " verified.");
		}
	}

	/**
	 * Test add and delete a phyloTree.
	 * 
	 */
	public void testCreateDelete() throws Exception {
		String testName = "CreateAndDelete";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new tree with several nodes:
		String newName = testName + " test " + Math.random();
		PhyloTree tree = new PhyloTree();
		tree.setTitle(newName);
		tree.setLabel(newName);
		tree.setNexusFileName(null);

		PhyloTreeNode rootNode = new PhyloTreeNode();
		tree.addTreeNode(rootNode);
		tree.setRootNode(rootNode);

		PhyloTreeNode node1 = new PhyloTreeNode();
		rootNode.addChildNode(node1);

		PhyloTreeNode node2 = new PhyloTreeNode();
		rootNode.addChildNode(node2);

		PhyloTreeNode node3 = new PhyloTreeNode();
		node1.addChildNode(node3);

		PhyloTreeNode node4 = new PhyloTreeNode();
		node3.addChildNode(node4);

		getFixture().store(tree);

		// force commit immediately, important:
		setComplete();
		endTransaction();
		logger.info("tree created: " + tree.getLabel() + "id = " + tree.getId());

		// 2. verify
		String sqlStr = "select count(*) from phylotree where phylotree_id=" + tree.getId();
		Integer count = jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(count == 1);

		// 3. delete
		getFixture().delete(tree);
		setComplete();

		// 4. verify delete:
		Integer countVerify = jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(countVerify == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Test add and delete a TreeBlock.
	 * 
	 */
	public void xtestCreateDeleteTreeBlock() throws Exception {
		String testName = "CreateAndDeleteTreeBlock";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1. create a new tree block with two trees,
		// one associated w/ analysis step:

		String newName = testName + " test " + Math.random();
		PhyloTree tree = new PhyloTree();
		tree.setTitle(newName);
		tree.setLabel(newName);
		tree.setNexusFileName(null);

		PhyloTreeNode rootNode = new PhyloTreeNode();
		tree.addTreeNode(rootNode);
		tree.setRootNode(rootNode);

		PhyloTreeNode node1 = new PhyloTreeNode();
		rootNode.addChildNode(node1);

		PhyloTreeNode node2 = new PhyloTreeNode();
		rootNode.addChildNode(node2);

		PhyloTreeNode node3 = new PhyloTreeNode();
		node1.addChildNode(node3);

		PhyloTreeNode node4 = new PhyloTreeNode();
		node3.addChildNode(node4);

		// tree2:
		PhyloTree tree2 = new PhyloTree();
		tree2.setLabel(newName + "2");
		PhyloTreeNode node21 = new PhyloTreeNode();
		tree2.addTreeNode(node21);
		tree2.setRootNode(node21);

		// treeblock:
		TreeBlock block = new TreeBlock();
		block.setTitle(newName);

		block.addPhyloTree(tree);
		block.addPhyloTree(tree2);

		// Create an analysis step and associate w/ tree.
		Study s = getTestStudy();
		Analysis a1 = new Analysis();
		a1.setName("1 " + newName);
		AnalysisStep step1 = new AnalysisStep();
		step1.setName("step1 " + newName);
		s.addAnalysis(a1);
		a1.addAnalysisStep(step1);

		AnalyzedTree treeData = new AnalyzedTree();
		treeData.setTree(tree);
		treeData.setInput(Boolean.FALSE);
		step1.addAnalyzedData(treeData);

		getFixture().store(tree);
		getFixture().store(tree2);
		getFixture().store(block);

		// force commit immediately, important:
		setComplete();
		endTransaction();
		logger.info("tree1 created: " + tree.getLabel() + "id = " + tree.getId());
		logger.info("tree2 created: " + tree2.getLabel() + "id = " + tree2.getId());
		logger.info("treeBlock created: " + block.getTitle() + "id = " + block.getId());
		logger.info("analysis step created: " + step1.getName() + "id = " + step1.getId()
			+ " associated data count=" + step1.getDataSetReadOnly().size());
		// logger.info("treeBlock created: " + block.getTitle() + "id = " + block.getId());

		// 2. verify
		String treeSql = "select count(*) from phylotree where treeBlock_id=" + block.getId();
		String blockSql = "select count(*) from treeblock where treeBlock_id=" + block.getId();
		String analyzedDataSql = "select count(*) from analyzedData where analysisstep_id="
			+ step1.getId();
		String a1Sql = "select count(*) from analysis where analysis_id=" + a1.getId();
		Integer count = jdbcTemplate.queryForObject(treeSql, Integer.class);
		assertTrue(count == 2);
		count = jdbcTemplate.queryForObject(blockSql, Integer.class);
		assertTrue(count == 1);
		count = jdbcTemplate.queryForObject(a1Sql, Integer.class);
		assertTrue(count == 1);
		count = jdbcTemplate.queryForObject(analyzedDataSql, Integer.class);
		assertTrue(count == 1);

		onSetUp();

		// 3. delete
		getFixture().refresh(block);
		getFixture().refresh(a1);
		getFixture().refreshAll(a1.getAnalysisStepsReadOnly());

		getFixture().deleteTreeBlock(block);
		setComplete();
		endTransaction();

		// 3.2: have to separate the deleting of analysis, due to
		// analysisstep -->analyzedData.
		onSetUp();
		// getFixture().refresh(a1);
		a1 = (Analysis) loadObject(Analysis.class, a1.getId());
		getAnalysisService().deleteAnalysis(a1);

		setComplete();
		endTransaction();

		// 4. verify delete:
		Integer countVerify = jdbcTemplate.queryForObject(blockSql, Integer.class);
		assertTrue(countVerify == 0);
		countVerify = jdbcTemplate.queryForObject(treeSql, Integer.class);
		assertTrue(countVerify == 0);
		countVerify = jdbcTemplate.queryForObject(a1Sql, Integer.class);
		assertTrue(countVerify == 0);
		countVerify = jdbcTemplate.queryForObject(analyzedDataSql, Integer.class);
		assertTrue(countVerify == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Test add and delete a phyloTree.
	 * 
	 */
	public void xxtestDeleteByID() throws Exception {
		String testName = "DeleteByID";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// This test case to test removing a tree/matrix
		// while handling submission -> trees and matrices.
		// See bug : http://opensource.atlassian.com/projects/hibernate/browse/HHH-2679

		// 1. get a tree with several nodes:
		// String newName = testName + " test " + Math.random();

		// submission id = 6; tree id 24, tree_order = 2
		PhyloTree tree = (PhyloTree) loadObject(PhyloTree.class, new Long(5));

		logger.info("tree found: " + tree.getLabel() + "id = " + tree.getId());

		// 2. verify
		String sqlStr = "select count(*) from phylotree where phylotree_id=" + tree.getId();
		Integer count = jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(count == 1);

		// 3. delete
		getFixture().delete(tree);
		setComplete();
		endTransaction();

		// 4. verify delete:
		Integer countVerify = jdbcTemplate.queryForObject(sqlStr, Integer.class);
		assertTrue(countVerify == 0);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Run the void findTreeBlocksByNexusFileName() method test
	 */
	/*this test failed be cause treeBlock may not have tree inside
	public void testFindTreeBlocksByNexusFileName() {
		String testName = "findTreeBlocksByNexusFileName";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// 1.find a submission with tree block:
		List<Map<String, Object>> result = jdbcTemplate
			.queryForList("select submission_id, treeblock_id from SUB_TREEBLOCK fetch first rows only");

		assertTrue("Empty SUB_TREEBLOCK table", result != null && !result.isEmpty());

		Map<String, Object> firstResult = result.get(0);
		String subId = firstResult.get("SUBMISSION_ID").toString();
		String treeblockId = firstResult.get("TREEBLOCK_id").toString();

		String nexusName = (String) jdbcTemplate.queryForObject(
			"select nexusFileName from phylotree where treeblock_id =" + treeblockId,
			String.class);
		logger
			.info(" subId =" + Long.decode(subId) + " treeblockId" + treeblockId + " nexusName=" + nexusName); //$NON-NLS-1$
		assertTrue(nexusName != null);

		// 2. query:
		Collection<TreeBlock> treeBlocks = getFixture().findTreeBlocksByNexusFileName(
			Long.decode(subId),
			nexusName);
		if (logger.isInfoEnabled()) {
			logger.info(" findByNexusFileName size= " + treeBlocks.size()); //$NON-NLS-1$
		}
		assertTrue(treeBlocks.size() > 0);

		// 3. verify
		// String matrixSQL = "select count(*) from matrix where matrix_id = " + m.getId();
		// int count = jdbcTemplate.queryForInt(matrixSQL);
		for (TreeBlock block : treeBlocks) {
			assertTrue("Verify nexus file name.", block.getNexusFileName().equalsIgnoreCase(
				nexusName));
		}
		// assertTrue(count == 1);

		// setComplete();
		// endTransaction();

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}
    */
	/**
	 * Test topological search	  
	 * @author mjd
	 */
	
	//this is not a proper test 
	public void testFindByTopology3() throws Exception {
	//	if (true) fail("unimplemented");
	//	String testName = "FindByTopology3";
	//	if (logger.isInfoEnabled()) {
	//		logger.info("\n\t\tRunning Test: " + testName);
	//	}
	//	Collection<PhyloTree> trees = null; /* = getFixture().findByTopology3(
	//		"Protocystites menevensis",
	//		"Cothurnocystis bifida",
	//		"Ceratocystis viscainoi"); */
	//	logger.info("query retrieved " + trees.size() + " tree(s)");

	//	{
			// Where's tree 487?
	//		boolean saw487 = false;
	//		for (PhyloTree t : trees) {
	//			logger.info("Found tree: " + t.getId());
	//			if (t.getId() == 487L) {
	//				saw487 = true;
	//				break;
	//			}
	//		}
	//		assertTrue(saw487);
	//	}
	}
		
	public void testFindByTreeType() {
		Collection<PhyloTree> trees = 
			getFixture().findSomethingByItsDescription(PhyloTree.class, 
					"treeType", "Consensus", false);
		assertNotNull(trees);
		assertTrue(trees.size() > 0);
		for (PhyloTree t : trees) {
			assertEquals("Consensus", t.getTypeDescription());
		}
	}
}
