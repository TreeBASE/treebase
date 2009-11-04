package org.cipres.treebase.domain.tree;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/**
 * TreePartition.java
 * 
 * Created on Mar 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "TREEPARTITION")
@AttributeOverride(name = "id", column = @Column(name = "TREEPARTITION_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
public class TreePartition extends AbstractPersistedObject {

	private static final long serialVersionUID = -1624253859305916674L;

	private String mTitle;

	private Collection<TreeGroup> mTreeGroups = new ArrayList<TreeGroup>();

	/**
	 * Constructor.
	 */
	public TreePartition() {
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
	 * Append a new TreeGroups to the Set.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTreeGroup TreeGroup
	 */
	public void addTreeGroup(TreeGroup pTreeGroup) {
		if (pTreeGroup != null) {
			getTreeGroups().add(pTreeGroup);
			pTreeGroup.setPartition(this);
		}
	}

	/**
	 * Remove the TreeGroups.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTreeGroup TreeGroup
	 */
	public void removeTreeGroup(TreeGroup pTreeGroup) {
		if (pTreeGroup != null) {
			getTreeGroups().remove(pTreeGroup);
			pTreeGroup.setPartition(null);
		}
	}

	/**
	 * Return the TreeGroupsSet field.
	 * 
	 * @return Collection<TreeGroup>
	 */
	@OneToMany(mappedBy = "partition", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
	protected Collection<TreeGroup> getTreeGroups() {
		return mTreeGroups;
	}

	/**
	 * Set the TreeGroupsSet field.
	 */
	protected void setTreeGroups(Collection<TreeGroup> pTreeGroups) {
		mTreeGroups = pTreeGroups;
	}

}
