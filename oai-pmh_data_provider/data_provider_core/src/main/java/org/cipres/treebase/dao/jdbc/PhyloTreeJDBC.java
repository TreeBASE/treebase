/*
 * Copyright 2007 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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
		// getSession().flush();

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
