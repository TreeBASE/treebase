package org.cipres.treebase.domain.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.IndexColumn;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TreeSet.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TREESET")
@AttributeOverride(name = "id", column = @Column(name = "TREESET_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
public class TreeSet extends AbstractPersistedObject {

	private String mTitle;

	private List<PhyloTree> mTreeList = new ArrayList<PhyloTree>();

	/**
	 * Constructor.
	 */
	public TreeSet() {
		super();
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String
	 */
	@Column(name = "Title", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Set the Title field.
	 */
	public void setTitle(String pNewTitle) {
		mTitle = pNewTitle;
	}

	/**
	 * Append a new Tree to the end of the list.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTree PhyloTree
	 */
	public void addPhyloTree(PhyloTree pTree) {
		if (pTree != null) {
			getTreeList().add(pTree);
		}
	}

	/**
	 * Remove the Tree.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTree PhyloTree
	 */
	public void removePhyloTree(PhyloTree pTree) {
		if (pTree != null) {
			getTreeList().remove(pTree);
		}
	}

	/**
	 * Return the TreeList field.
	 * 
	 * @return List<PhyloTree>
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "TREESET_PHYLOTREE", joinColumns = {@JoinColumn(name = "TREESET_ID")}, inverseJoinColumns = @JoinColumn(name = "PHYLOTREE_ID"))
	@IndexColumn(name = "Tree_ORDER")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
	protected List<PhyloTree> getTreeList() {
		return mTreeList;
	}

	/**
	 * Set the TreeList field.
	 */
	protected void setTreeList(List<PhyloTree> pTreeList) {
		mTreeList = pTreeList;
	}

	/**
	 * Return the tree list interator.
	 * 
	 * Creation date: Apr 19, 2006 1:16:57 PM
	 */
	@Transient
	public Iterator<PhyloTree> getTreeListIterator() {
		return getTreeList().iterator();
	}

	/**
	 * Return the tree count.
	 * 
	 * Creation date: Apr 19, 2006 1:17:53 PM
	 */
	@Transient
	public int getTreeCount() {
		return getTreeList().size();
	}
}
