
package org.cipres.treebase.dao.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.dao.jdbc.PhyloTreeJDBC;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedDataHome;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.cipres.treebase.domain.tree.PhyloTreeNode;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.domain.tree.TreeKind;
import org.cipres.treebase.domain.tree.TreeQuality;
import org.cipres.treebase.domain.tree.TreeType;
import org.hibernate.Criteria;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;


/**
 * PhyloTreeDAO.java
 * 
 * Created on Apr 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class PhyloTreeDAO extends AbstractDAO implements PhyloTreeHome {

	private static final Logger LOGGER = LogManager.getLogger(PhyloTreeDAO.class);
	   
	private SubmissionHome mSubmissionHome;
	private AnalyzedDataHome mAnalyzedDataHome;
	private TaxonLabelHome mTaxonLabelHome;

	/**
	 * Constructor.
	 */
	public PhyloTreeDAO() {
		super();
	}

	/**
	 * Return the AnalyzedDataHome field.
	 * 
	 * @return AnalyzedDataHome mAnalyzedDataHome
	 */
	private AnalyzedDataHome getAnalyzedDataHome() {
		return mAnalyzedDataHome;
	}

	/**
	 * Set the AnalyzedDataHome field.
	 */
	public void setAnalyzedDataHome(AnalyzedDataHome pNewAnalyzedDataHome) {
		mAnalyzedDataHome = pNewAnalyzedDataHome;
	}

	/**
	 * Return the SubmissionHome field.
	 * 
	 * @return SubmissionHome mSubmissionHome
	 */
	private SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	/**
	 * Set the SubmissionHome field.
	 */
	public void setSubmissionHome(SubmissionHome pNewSubmissionHome) {
		mSubmissionHome = pNewSubmissionHome;
	}

	public TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	public void setTaxonLabelHome(TaxonLabelHome taxonLabelHome) {
		mTaxonLabelHome = taxonLabelHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#delete(java.util.Collection)
	 */
	public void delete(Collection<PhyloTree> pTrees) {
		if (pTrees == null || pTrees.isEmpty()) {
			return;
		}

		for (PhyloTree tree : pTrees) {
			delete(tree);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#delete(org.cipres.treebase.domain.tree.PhyloTree)
	 */
	public void delete(PhyloTree pTree) {
		if (pTree != null && pTree.getId() != null) {

			// bi-directional relationships:
			// * delete treeblock -> tree
			// * delete submission-> tree
			// * delete analyzedData -> tree
			TreeBlock block = pTree.getTreeBlock();
			if (block != null) {
				block.removePhyloTree(pTree);

				if (block.isEmpty()) {
					//TaxonLabelSet tSet=block.getTaxonLabelSet();
					deleteTreeBlock(block);
					//getTaxonLabelHome().clean(tSet);
				}
			}

			realDelete(pTree);
		}

	}

	/**
	 * 
	 * @param pTree
	 */
	private void realDelete(PhyloTree pTree) {
		// A unique constraint violation could occur if two objects are both being updated, one
		// is "releasing" a value and the other is "obtaining" the same value.
		// TODO A workaround is to flush() the session manually after updating the first object
		// and before updating the second.

		Collection<AnalyzedData> dataLink = getAnalyzedDataHome().findByTree(pTree);
		for (AnalyzedData data : dataLink) {
			data.getAnalysisStep().removeAnalyzedData(data);

			// getHibernateTemplate().delete(data);
		}
        
		Set<TaxonLabel> tSet=pTree.getAllTaxonLabels();
		// Delete by direct JDBC:
		// * Tree nodes
		// ** for each tree node, node attribute.
		// ** (TODO) NodeEdge
		//PhyloTreeJDBC.deletePhyloTreeNodeSQL(pTree, getSession());

		// cascade delete by hibernate:
		// * tree atrribute
		getHibernateTemplate().delete(pTree);
		getTaxonLabelHome().clean(new ArrayList<TaxonLabel>(tSet));
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#deleteTreeBlock(org.cipres.treebase.domain.tree.TreeBlock)
	 */
	public void deleteTreeBlock(TreeBlock pTreeBlock) {
		if (pTreeBlock != null && pTreeBlock.getId() != null) {
			
			TaxonLabelSet tSet=pTreeBlock.getTaxonLabelSet();
			
			// bi-directional relationships:
			// * submission -> treeblock
			Submission sub = getSubmissionHome().findByTreeBlock(pTreeBlock);
			if (sub != null) {
				sub.removePhyloTreeBlock(pTreeBlock);
			}

			// cascade delete all trees by hibernate:
			// bi-directional relationships:
			// * remove analysisStep --> phyloTree
			for (PhyloTree tree : pTreeBlock.getTreeList()) {
				realDelete(tree);
			}

			;getHibernateTemplate().delete(pTreeBlock);
			
			getTaxonLabelHome().clean(tSet);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#deleteTreeBlocks(java.util.Collection)
	 */
	public void deleteTreeBlocks(Collection<TreeBlock> pTreeBlocks) {
		if (pTreeBlocks == null || pTreeBlocks.isEmpty()) {
			return;
		}

		for (TreeBlock treeBlock : pTreeBlocks) {
			deleteTreeBlock(treeBlock);
		}
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#deleteNodes(org.cipres.treebase.domain.tree.PhyloTree)
	 */
	public void deleteNodes(PhyloTree pTree) {
		PhyloTreeJDBC.deletePhyloTreeNodeSQL(pTree, getSession());
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#findByAnyTaxonLabel(java.util.List)
	 */
	public Collection<PhyloTree> findByAnyTaxonLabel(List<TaxonLabel> pTaxonLabels) {
		Collection<PhyloTree> returnVal = new ArrayList<PhyloTree>();

		if (pTaxonLabels != null && !pTaxonLabels.isEmpty()) {
			String query =
			// "select t from PhyloTree t join t.treeNodes n join n.taxonLabel label where label in
			// (:labels)";
			"select t from PhyloTree t join t.treeNodes n where n.taxonLabel in (:labels)";

			Query q = getSession().createQuery(query);

			q.setParameterList("labels", pTaxonLabels);
			returnVal = q.list();
			// returnVal.addAll(results);

		}
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#deleteTreeBlockByID(java.lang.Long)
	 */
	public void deleteTreeBlockByID(Long pTreeBlockID) {
		deleteTreeBlock((TreeBlock) findPersistedObjectByID(TreeBlock.class, pTreeBlockID));

	}

	public TreeBlock findTreeBlockById(Long pId) {
		if (pId == null) {
			return null;
		}

		return findPersistedObjectByID(TreeBlock.class, pId);

	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#findKindByDescription(java.lang.String)
	 */
	public TreeKind findKindByDescription(String pDescription) {
		if (TreebaseUtil.isEmpty(pDescription)) {
			return null;
		}
		
		TreeKind returnVal = null;

		Criteria c = getSession().createCriteria(TreeKind.class).add(
			org.hibernate.criterion.Expression.eq("description", pDescription));

		returnVal = (TreeKind) c.uniqueResult();
		return returnVal;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#findQualityByDescription(java.lang.String)
	 */
	public TreeQuality findQualityByDescription(String pDescription) {
		if (TreebaseUtil.isEmpty(pDescription)) {
			return null;
		}
		
		TreeQuality returnVal = null;

		Criteria c = getSession().createCriteria(TreeQuality.class).add(
			org.hibernate.criterion.Expression.eq("description", pDescription));

		returnVal = (TreeQuality) c.uniqueResult();
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#findTypeByDescription(java.lang.String)
	 */
	public TreeType findTypeByDescription(String pDescription) {
		if (TreebaseUtil.isEmpty(pDescription)) {
			return null;
		}
		
		TreeType returnVal = null;

		Criteria c = getSession().createCriteria(TreeType.class).add(
			org.hibernate.criterion.Expression.eq("description", pDescription));

		returnVal = (TreeType) c.uniqueResult();
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#findByStudies(java.util.Collection)
	 */
	public Collection<PhyloTree> findByStudies(Collection<Study> pStudies) {

		// FIXME: findByStudies
		Collection<PhyloTree> returnVal = new ArrayList<PhyloTree>();

		if (pStudies != null && !pStudies.isEmpty()) {
			String query = "select t from PhyloTree t where study in (:studies)";

			Query q = getSession().createQuery(query);

			q.setParameterList("studies", pStudies);
			List results = q.list();

			returnVal.addAll(results);

		}
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#findTreeBlocksByNexusFileName(java.lang.Long,
	 *      java.lang.String)
	 */
	public Collection<TreeBlock> findTreeBlocksByNexusFileName(Long pSubmissionId, String pFileName) {
		StringBuffer query = new StringBuffer(
			"select distinct b from Submission s join s.submittedTreeBlocks b join b.treeList t")
			.append(" where s.id = :submissionId and t.nexusFileName = :nexusFileName");

		// FIXME: need more efficient use of sId. one less join..
		// Need eager fetch all trees.
		Query q = getSession().createQuery(query.toString());

		q.setParameter("submissionId", pSubmissionId);
		q.setParameter("nexusFileName", pFileName);

		return q.list();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#updatePublishedFlagByStudy(org.cipres.treebase.domain.study.Study,
	 *      boolean)
	 */
	public int updatePublishedFlagByStudy(Study pStudy, boolean pPublish) {
		if (pStudy == null) {
			return 0;
		}

		String query = "update PhyloTree t set t.published = :pub where t.study = :study";

		Query q = getSession().createQuery(query);
		q.setBoolean("pub", pPublish);
		q.setParameter("study", pStudy);

		return q.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#findByTopology3(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public Set<PhyloTree> findByTopology3(TaxonVariant a, TaxonVariant b, TaxonVariant c) {
		Set<PhyloTree> returnVal = new HashSet<PhyloTree>();
				
		Set<TaxonVariant> aTV = getTaxonLabelHome().expandTaxonVariant(a);
		Set<TaxonVariant> bTV = getTaxonLabelHome().expandTaxonVariant(b);
		Set<TaxonVariant> cTV = getTaxonLabelHome().expandTaxonVariant(c);
		
		if (aTV.isEmpty() || bTV.isEmpty() || cTV.isEmpty()) {
			// XXX This avoids a query syntax error later on but maybe isn't
			// the best behavior.
			return new HashSet<PhyloTree> ();
		}
		
		String query = "select distinct a.tree.rootNode "
			+
			// Find the trees with three nodes a, b, and c, such that...
			"from PhyloTreeNode as a, PhyloTreeNode as b, PhyloTreeNode as c, "
			+
			// There's a node "ab" (which will be an ancestor of both a and b)
			"PhyloTreeNode as ab "
			+
			// All four nodes are in the same tree
			"where a.tree = b.tree " + "and a.tree = c.tree " + "and a.tree = ab.tree "
			+
			"and a.taxonLabel.taxonVariant in (:a) " +
			"and b.taxonLabel.taxonVariant in (:b) " +
			"and c.taxonLabel.taxonVariant in (:c) " +
						// ab is an ancestor of a
			"and ab.leftNode < a.leftNode and ab.rightNode > a.rightNode " +
			// ab is an ancestor of b
			"and ab.leftNode < b.leftNode and ab.rightNode > b.rightNode " +
			// ab is NOT an ancestor of c
			"and (ab.leftNode >= c.leftNode or ab.rightNode <= c.rightNode) ";
		
		Query q = getSession().createQuery(query);

		q.setParameterList("a", aTV);
		q.setParameterList("b", bTV);
		q.setParameterList("c", cTV);
		List<PhyloTreeNode> rootNodes = q.list();

		if (rootNodes.isEmpty()) {
			return returnVal;
		}

		Query t = getSession().createQuery("from PhyloTree where rootNode in (:roots)");
		t.setParameterList("roots", rootNodes);

		returnVal.addAll(t.list());
		return returnVal;
	}
	
	@Deprecated
	public Set<PhyloTree> findByTopology3SLOW(String a, String b, String c) {
		Set<PhyloTree> returnVal = new HashSet<PhyloTree>();
		
		Set<TaxonVariant> aTV = getTaxonLabelHome().findTaxonVariantWithSubstring(a, false);
		Set<TaxonVariant> bTV = getTaxonLabelHome().findTaxonVariantWithSubstring(b, false);
		Set<TaxonVariant> cTV = getTaxonLabelHome().findTaxonVariantWithSubstring(c, false);
		
		aTV = getTaxonLabelHome().expandTaxonVariantSet(aTV);
		bTV = getTaxonLabelHome().expandTaxonVariantSet(bTV);
		cTV = getTaxonLabelHome().expandTaxonVariantSet(cTV);
		
		String query = "select distinct a.tree.rootNode "
			+
			// Find the trees with three nodes a, b, and c, such that...
			"from PhyloTreeNode as a, PhyloTreeNode as b, PhyloTreeNode as c, "
			+
			// There's a node "ab" (which will be an ancestor of both a and b)
			"PhyloTreeNode as ab, "
			+
			
			"TaxonVariant as tva, TaxonVariant as tvb, TaxonVariant as tvc "
			+
			// All four nodes are in the same tree
			"where a.tree = b.tree " + "and a.tree = c.tree " + "and a.tree = ab.tree "
			+
			"and tva.fullName like :a " +
			"and tvb.fullName like :b " +
			"and tvc.fullName like :c " +
			"and a.taxonLabel.taxonVariant.taxon = tva.taxon " +
			"and b.taxonLabel.taxonVariant.taxon = tvb.taxon " +
				// ab is an ancestor of a
			"and ab.leftNode < a.leftNode and ab.rightNode > a.rightNode " +
			// ab is an ancestor of b
			"and ab.leftNode < b.leftNode and ab.rightNode > b.rightNode " +
			// ab is NOT an ancestor of c
			"and (ab.leftNode >= c.leftNode or ab.rightNode <= c.rightNode) ";
		
		Query q = getSession().createQuery(query);

		q.setParameter("a", "%"+a+"%");
		q.setParameter("b", "%"+b+"%");
		q.setParameter("c", "%"+c+"%");
		List<PhyloTreeNode> rootNodes = q.list();

		if (rootNodes.isEmpty()) {
			return returnVal;
		}

		Query t = getSession().createQuery("from PhyloTree where rootNode in (:roots)");
		t.setParameterList("roots", rootNodes);

		returnVal.addAll(t.list());
		return returnVal;
	}

	public Set<PhyloTree> findByTopology4a(TaxonVariant a, TaxonVariant b, TaxonVariant c, TaxonVariant d) {
		Set<PhyloTree> returnVal = new HashSet<PhyloTree>();

		Set<TaxonVariant> aTV = getTaxonLabelHome().expandTaxonVariant(a);
		Set<TaxonVariant> bTV = getTaxonLabelHome().expandTaxonVariant(b);
		Set<TaxonVariant> cTV = getTaxonLabelHome().expandTaxonVariant(c);
		Set<TaxonVariant> dTV = getTaxonLabelHome().expandTaxonVariant(d);
		
		if (aTV.isEmpty() || bTV.isEmpty() || cTV.isEmpty() || dTV.isEmpty()) {
			// XXX This avoids a query syntax error later on but maybe isn't
			// the best behavior.
			return new HashSet<PhyloTree> ();
		}
	
		String query = "select distinct a.tree.rootNode "
			+
			// Find the trees with four nodes a, b, c, and d, such that...
			"from PhyloTreeNode as a, PhyloTreeNode as b, "
			+ "PhyloTreeNode as c, PhyloTreeNode as d,"
			+
			// There's a node "ab" (which will be an ancestor of both a and b)
			"PhyloTreeNode as ab, "
			+
			// and a node "abc" (which will be an ancestor of both ab and c)
			"PhyloTreeNode as abc "
			+

			// All six nodes are in the same tree
			"where a.tree = b.tree " + "and a.tree = c.tree " + "and a.tree = d.tree "
			+ "and a.tree = ab.tree " + "and a.tree = abc.tree "
			+
			// a, b, c, and d contain the specified taxon labels
			"and a.taxonLabel.taxonVariant in (:a) " + "and b.taxonLabel.taxonVariant in (:b) "
			+ "and c.taxonLabel.taxonVariant in (:c) " + "and d.taxonLabel.taxonVariant in (:d) " +

			// ab is an ancestor of a
			"and ab.leftNode < a.leftNode and ab.rightNode > a.rightNode " +
			// ab is an ancestor of b
			"and ab.leftNode < b.leftNode and ab.rightNode > b.rightNode " +
			// ab is NOT an ancestor of c
			"and (ab.leftNode >= c.leftNode or ab.rightNode <= c.rightNode) " +

			// abc is an ancestor of ab
			"and abc.leftNode < ab.leftNode and abc.rightNode > ab.rightNode " +
			// abc is an ancestor of c
			"and abc.leftNode < c.leftNode and abc.rightNode > c.rightNode " +
			// abc is NOT an ancestor of d
			"and (abc.leftNode >= d.leftNode or abc.rightNode <= d.rightNode) ";

		Query q = getSession().createQuery(query);

		q.setParameterList("a", aTV);
		q.setParameterList("b", bTV);
		q.setParameterList("c", cTV);
		q.setParameterList("d", dTV);
		List<PhyloTreeNode> rootNodes = q.list();
		if (rootNodes.isEmpty()) {
			return returnVal;
		}

		Query t = getSession().createQuery("from PhyloTree where rootNode in (:roots)");
		t.setParameterList("roots", rootNodes);

		returnVal.addAll(t.list());
		return returnVal;
	}

	public Set<PhyloTree> findByTopology4s(TaxonVariant a, TaxonVariant b, TaxonVariant c, TaxonVariant d) {
		Set<PhyloTree> returnVal = new HashSet<PhyloTree>();
		
		Set<TaxonVariant> aTV = getTaxonLabelHome().expandTaxonVariant(a);
		Set<TaxonVariant> bTV = getTaxonLabelHome().expandTaxonVariant(b);
		Set<TaxonVariant> cTV = getTaxonLabelHome().expandTaxonVariant(c);
		Set<TaxonVariant> dTV = getTaxonLabelHome().expandTaxonVariant(d);

		if (aTV.isEmpty() || bTV.isEmpty() || cTV.isEmpty() || dTV.isEmpty()) {
			// XXX This avoids a query syntax error later on but maybe isn't
			// the best behavior.
			return new HashSet<PhyloTree> ();
		}

		String query = "select distinct a.tree.rootNode "
			+
			// Find the trees with four nodes a, b, c, and d, such that...
			"from PhyloTreeNode as a, PhyloTreeNode as b, "
			+ "PhyloTreeNode as c, PhyloTreeNode as d,"
			+
			// There's a node "ab" (which will be an ancestor of both a and b)
			"PhyloTreeNode as ab, "
			+
			// and a node "cd" (which will be an ancestor of both c and d)
			"PhyloTreeNode as cd "
			+

			// All six nodes are in the same tree
			"where a.tree = b.tree " + "and a.tree = c.tree " + "and a.tree = d.tree "
			+ "and a.tree = ab.tree " + "and a.tree = cd.tree "
			+
			// a, b, c, and d contain the specified taxon labels
			"and a.taxonLabel.taxonVariant in (:a) " + "and b.taxonLabel.taxonVariant in (:b) "
			+ "and c.taxonLabel.taxonVariant in (:c) " + "and d.taxonLabel.taxonVariant in (:d) " +

			// ab is an ancestor of a
			"and ab.leftNode < a.leftNode and ab.rightNode > a.rightNode " +
			// ab is an ancestor of b
			"and ab.leftNode < b.leftNode and ab.rightNode > b.rightNode " +
			// ab is NOT an ancestor of c
			"and (ab.leftNode >= c.leftNode or ab.rightNode <= c.rightNode) " +
			// ab is NOT an ancestor of d
			"and (ab.leftNode >= d.leftNode or ab.rightNode <= d.rightNode) " +

			// cd is an ancestor of c
			"and cd.leftNode < c.leftNode and cd.rightNode > c.rightNode " +
			// cd is an ancestor of d
			"and cd.leftNode < d.leftNode and cd.rightNode > d.rightNode " +
			// cd is NOT an ancestor of a
			"and (cd.leftNode >= a.leftNode or cd.rightNode <= a.rightNode) " +
			// cd is NOT an ancestor of b
			"and (cd.leftNode >= b.leftNode or cd.rightNode <= b.rightNode) ";

		Query q = getSession().createQuery(query);
/*		
		LOGGER.debug("taxonvariantsets aTV: " + aTV.toString());
		LOGGER.debug("taxonvariantsets bTV: " + bTV.toString());
		LOGGER.debug("taxonvariantsets cTV: " + cTV.toString());
		LOGGER.debug("taxonvariantsets dTV: " + dTV.toString());
*/
		
		q.setParameterList("a", aTV);
		q.setParameterList("b", bTV);
		q.setParameterList("c", cTV);
		q.setParameterList("d", dTV);
		List<PhyloTreeNode> rootNodes = q.list();
		if (rootNodes.isEmpty()) {
			return returnVal;
		}

		Query t = getSession().createQuery("from PhyloTree where rootNode in (:roots)");
		t.setParameterList("roots", rootNodes);

		returnVal.addAll(t.list());
		return returnVal;
	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#findMatricesByTitle(java.lang.String)
	 */
	public Set<PhyloTree> findTreesByLabel(String label) {
		Set<PhyloTree> returnVal = new HashSet<PhyloTree>();
		String query = "select t from PhyloTree t where label = :label";

		Query q = getSession().createQuery(query);

		q.setParameter("label", label);
		@SuppressWarnings("unchecked")
		List<PhyloTree> results = q.list();

		returnVal.addAll(results);
		return returnVal;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<PhyloTree> findByNexusFile(String fn) {
		Query q = getSession().createQuery("from PhyloTree where nexusfilename = :fn");
		q.setParameter("fn", fn);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	public Set<PhyloTreeNode> findNodesByTaxonLabel(TaxonLabel tl) {
		Session sess = getSession();
		SessionFactory sf = getSessionFactory();
		Query q = sess.createQuery("select n from PhyloTreeNode n where taxonLabel = :tl");
		q.setParameter("tl", tl);
		Set<PhyloTreeNode> result = new HashSet<PhyloTreeNode> ();
		

		result.addAll((List<PhyloTreeNode>) q.list());

		return result;
	}

	public Collection<PhyloTree> findByTB1TreeID(String legacyID) {
		List<PhyloTree> trees =
			getSession().createQuery("from PhyloTree where TB1TreeID = :sid")
				.setParameter("sid", legacyID)
				.list();
			
			return trees;
	}
	

	public Collection<PhyloTree> findTreesByNCBINodes(String pNcbiId) {

		
		Set<PhyloTree> returnVal = new HashSet<PhyloTree>();
		
		Object[] ncbiNode =  (Object []) getSession()
			.createSQLQuery("SELECT * FROM ncbi_nodes WHERE tax_id = ?")
			.addScalar("left_id", StandardBasicTypes.INTEGER)
			.addScalar("right_id", StandardBasicTypes.INTEGER)
			.setInteger(0, Integer.parseInt(pNcbiId))
			.uniqueResult();

		
		
		
		List phyloTreeNodes =  getSession()
			.createSQLQuery("select ptn.phylotree_id " +
			"from PhyloTreeNode ptn "+ 
			"join TaxonLabel tl on (ptn.taxonlabel_id = tl.taxonlabel_id) "+ 
			"join TaxonVariant tv on (tl.taxonvariant_id = tv.taxonvariant_id) "+ 
			"join Taxon tx on (tv.taxon_id = tx.taxon_id) "+ 
			"join ncbi_nodes nno on (tx.ncbitaxid = nno.tax_id) "+ 
			"where nno.left_id >= "+ ncbiNode[0] + " " +
			"and nno.right_id <= "+ ncbiNode[1] + " " +
			"group by ptn.phylotree_id ")
			.addScalar("phylotree_id", StandardBasicTypes.LONG)
			.list();
		
		if (phyloTreeNodes.isEmpty()) {
			return returnVal;
		}
		
		Collection <PhyloTree> phylotrees = (Collection<PhyloTree>) getSession()
				.createQuery("from PhyloTree where id in (:ids)")
				.setParameterList("ids", phyloTreeNodes)
				.list();
		return phylotrees;
	}

}
