package org.cipres.treebase.domain.matrix;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * CSTree.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("C")
public class CSTree extends UserType {

	private static final long serialVersionUID = 1L;

	private Set<CSTreeNode> mTreeNodes = new HashSet<CSTreeNode>();

	/**
	 * Constructor.
	 */
	public CSTree() {
		super();
	}

	/**
	 * Return the TreeNodes field.
	 * 
	 * @return Set<CSTreeNode>
	 */
	@OneToMany(mappedBy = "tree", cascade = CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<CSTreeNode> getTreeNodes() {
		return mTreeNodes;
	}

	/**
	 * Set the TreeNodes field.
	 */
	public void setTreeNodes(Set<CSTreeNode> pNewTreeNodes) {
		mTreeNodes = pNewTreeNodes;
	}

}
