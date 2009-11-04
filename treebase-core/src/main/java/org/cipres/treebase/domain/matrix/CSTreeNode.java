package org.cipres.treebase.domain.matrix;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;

/**
 * CSTreeNode.java
 * 
 * Created on Mar 29, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "CSTREENODE")
@AttributeOverride(name = "id", column = @Column(name = "CSTREENODE_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
public class CSTreeNode extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private CSTree mTree;
	private DiscreteCharState mCharState;
	private CSTreeNode mParentNode;

	private Set<CSTreeNode> mChildNodes = new HashSet<CSTreeNode>();

	/**
	 * Constructor.
	 */
	public CSTreeNode() {
		super();
	}

	/**
	 * Return the ChildNodes field.
	 * 
	 * @return Set<CSTreeNode>
	 */
	@OneToMany(mappedBy = "parentNode", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<CSTreeNode> getChildNodes() {
		return mChildNodes;
	}

	/**
	 * Set the ChildNodes field.
	 */
	public void setChildNodes(Set<CSTreeNode> pNewChildNodes) {
		mChildNodes = pNewChildNodes;
	}

	/**
	 * Return the ParentNode field.
	 * 
	 * @return CSTreeNode
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "PARENTNODE_ID", nullable = true)
	public CSTreeNode getParentNode() {
		return mParentNode;
	}

	/**
	 * Set the ParentNode field.
	 */
	public void setParentNode(CSTreeNode pNewParentNode) {
		mParentNode = pNewParentNode;
	}

	/**
	 * Return the CharState field.
	 * 
	 * @return DiscreteCharState
	 */
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "DISCRETECHARSTATE_ID", nullable = true)
	public DiscreteCharState getCharState() {
		return mCharState;
	}

	/**
	 * Set the CharState field.
	 */
	public void setCharState(DiscreteCharState pNewCharState) {
		mCharState = pNewCharState;
	}

	/**
	 * Return the Tree field.
	 * 
	 * @return CSTree
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "CSTREE_ID", nullable = false)
	public CSTree getTree() {
		return mTree;
	}

	/**
	 * Set the Tree field.
	 */
	public void setTree(CSTree pNewTree) {
		mTree = pNewTree;
	}

}
