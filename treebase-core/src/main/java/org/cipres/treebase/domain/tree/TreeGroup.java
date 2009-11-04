package org.cipres.treebase.domain.tree;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TreeGroup.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "TREEGROUP")
@AttributeOverride(name = "id", column = @Column(name = "TREEGROUP_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
public class TreeGroup extends AbstractPersistedObject {

	private static final long serialVersionUID = 1058478001937176453L;

	private String mTitle;

	private TreePartition mPartition;

	private Set<PhyloTree> mTreeSet;

	/**
	 * Constructor.
	 */
	public TreeGroup() {
		super();

		mTreeSet = new HashSet<PhyloTree>();
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
	 * Return the Partition field.
	 * 
	 * @return TreePartition mPartition
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TREEPARTITION_ID")
	public TreePartition getPartition() {
		return mPartition;
	}

	/**
	 * Set the Partition field.
	 */
	public void setPartition(TreePartition pNewPartition) {
		mPartition = pNewPartition;
	}

	/**
	 * Append a new Tree to the Set.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTree PhyloTree
	 */
	public void addPhyloTree(PhyloTree pTree) {
		if (pTree != null) {
			getTreeSet().add(pTree);
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
			getTreeSet().remove(pTree);
		}
	}

	/**
	 * Return the TreeSet field.
	 * 
	 * @return Set<PhyloTree>
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "TREEGROUP_PHYLOTREE", joinColumns = {@JoinColumn(name = "TREEGROUP_ID")}, inverseJoinColumns = @JoinColumn(name = "PHYLOTREE_ID"))
	protected Set<PhyloTree> getTreeSet() {
		return mTreeSet;
	}

	/**
	 * Set the TreeSet field.
	 */
	protected void setTreeSet(Set<PhyloTree> pTreeSet) {
		mTreeSet = pTreeSet;
	}

}
