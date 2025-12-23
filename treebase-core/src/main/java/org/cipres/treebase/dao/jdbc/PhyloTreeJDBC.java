
package org.cipres.treebase.dao.jdbc;

import org.hibernate.Query;
import org.hibernate.Session;

import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * Helper class for direct tree related SQL operations. Bypass the hibernate framework for 
 * high performance database operations. 
 * 
 * 
 * Created on Oct 2, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class PhyloTreeJDBC {

	// INSERT INTO PHYLOTREE(PHYLOTREE_ID, VERSION, LABEL, TITLE, PUBLISHED,
	// NEXUSFILENAME, NEWICKSTRING, BIGTREE, ROOTEDTREE, STUDY_ID,
	// TREEQUALITY_ID, TREEBLOCK_ID, TREETYPE_ID, ROOTNODE_ID, TREEATTRIBUTE_ID)
	// VALUES(0, 0, '', '', 0, '', '', 0, 0, 0, 0, 0, 0, 0, 0)

	/**
	 * Constructor.
	 */
	public PhyloTreeJDBC() {
		super();
	}

	/**
	 * Delete all nodes in a tree using direct SQL.
	 * 
	 * @param pTree
	 */
	public static void deletePhyloTreeNodeSQL(PhyloTree pTree, Session pSession) {
		// pTree.setRootNode(null);
		// getSessionFactory().getCurrentSession().flush();

		//first delete the root node from the tree. Then delete all nodes in one go. 
		String query = "update phylotree set rootNode_id = null where phylotree_id = :treeID";
		Query q = pSession.createSQLQuery(query);
		q.setParameter("treeID", pTree.getId());
		q.executeUpdate();

		query = "delete from phylotreenode where phylotree_id = :treeID";
		q = pSession.createSQLQuery(query);
		q.setParameter("treeID", pTree.getId());
		q.executeUpdate();
	}

}
