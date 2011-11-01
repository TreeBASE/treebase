package org.cipres.treebase.domain.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import mesquite.lib.StringUtil;

import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * TreeSet.java
 * 
 * Created on March 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "TREEBLOCK")
@AttributeOverride(name = "id", column = @Column(name = "TREEBLOCK_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
public class TreeBlock extends AbstractPersistedObject {

	private static final long serialVersionUID = 1L;

	private String mTitle;

	private TaxonLabelSet mTaxonLabelSet;
	private Collection<PhyloTree> mTreeList = new ArrayList<PhyloTree>();
	
	// Transient
	private boolean mAnalyzed;		

	/**
	 * Constructor.
	 */
	public TreeBlock() {
		super();
	}

	/**
	 * Return the Title field.
	 * 
	 * @return String
	 */
	@Column(name = "Title", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getTitle() {
		if ( ! TreebaseUtil.isEmpty(mTitle) ) {
			return mTitle;
		}
		else if ( null != getId() ){
			return TreebaseIDString.getIDString(this);
		}
		else {
			return null;
		}
	}

	/**
	 * Set the Title field.
	 */
	public void setTitle(String pNewTitle) {
		mTitle = pNewTitle;
	}

	/**
	 * Return the TaxonLabelSet field.
	 * 
	 * @return TaxonLabelSet mTaxonLabelSet
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "TAXONLABELSET_ID")
	public TaxonLabelSet getTaxonLabelSet() {
		return mTaxonLabelSet;
	}

	/**
	 * Set the TaxonLabelSet field.
	 */
	public void setTaxonLabelSet(TaxonLabelSet pNewTaxonLabelSet) {
		mTaxonLabelSet = pNewTaxonLabelSet;
	}

	/**
	 * Append a new Tree to the end of the list.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTree PhyloTree
	 */
	public void addPhyloTree(PhyloTree pTree) {
		if (pTree != null && (!getTreeList().contains(pTree))) {
			getTreeList().add(pTree);
			pTree.setTreeBlock(this);
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
			// if (pTree != null && (this.equals(pTree.getTreeBlock()))) {
			getTreeList().remove(pTree);
			pTree.setTreeBlock(null);
		}
	}

	/**
	 * Return the TreeList field.
	 * 
	 * @return List<PhyloTree>
	 */
	@OneToMany(mappedBy = "treeBlock", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
	public Collection<PhyloTree> getTreeList() {
		return mTreeList;
	}

	/**
	 * Set the TreeList field.
	 */
	protected void setTreeList(Collection<PhyloTree> pTreeList) {
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

	/**
	 * Return the Analyzed field.
	 * 
	 * @return boolean mAnalyzed
	 */
	@Transient
	public boolean isAnalyzed() {
		return mAnalyzed;
	}
	
	/**
	 * Set the Analyzed field.
	 */	
	public void setAnalyzed(boolean pNewAnalyzed) {
		mAnalyzed = pNewAnalyzed;
	}	
	
	/**
	 * Return whether the block has any tree.
	 * 
	 * @return
	 */
	@Transient
	public boolean isEmpty() {
		return getTreeList().isEmpty();
	}

	/**
	 * Return the nexus file name for the first tree. Returns null if there is no tree in the block.
	 * 
	 * @return
	 */
	@Transient
	public String getNexusFileName() {
		if (isEmpty()) {
			return null;
		}

		PhyloTree firstTree = getTreeList().iterator().next();

		return firstTree.getNexusFileName();
	}

	@Transient
	public void generateAFileDynamically(StringBuilder pBuilder) {

		TaxonLabelSet tlSet = getTaxonLabelSet();
		List<TaxonLabel> txnlbllist = tlSet.getTaxonLabelsReadOnly();
		int numoftxnlbls = getTaxonNumber();

		String title = getTitle(); 
		if (TreebaseUtil.isEmpty(title)) {
			//use the default title:
			//title = "List of Uploaded Tree Block";
		}
		pBuilder.append("BEGIN TREES;\n");
		pBuilder.append("      TITLE " + StringUtil.tokenize(getTitle()) + ";\n");
		pBuilder.append("      LINK TAXA = " + StringUtil.tokenize(tlSet.getTitle().replaceAll("Input|Output", "")) + ";\n");
		pBuilder.append("         TRANSLATE\n");

		tlSet.buildTaxonWithLineNumber(pBuilder);

		for (PhyloTree atree : getTreeList()) {
			pBuilder.append("      TREE " + StringUtil.tokenize(atree.getLabel()) + " = ");

			if (atree.getRootedTree() != null) {
				if (atree.getRootedTree().booleanValue()) {
					pBuilder.append("[&R] ");
				} else {
					pBuilder.append("[&U] ");
				}

			}
			String tmpnewick, newick = atree.getNewickString();
			for (int z = 0; z < numoftxnlbls; z++) {
				String label = StringUtil.tokenize(txnlbllist.get(z).getTaxonLabel());
				tmpnewick = newick.replace(label, String.valueOf(z + 1));
				newick = tmpnewick;
			}
			// out.append(atree.getNewickString());
			pBuilder.append(newick);
			pBuilder.append("\n");
		}
		pBuilder.append("\n\n\nEND;\n");

		// File did not exist and was created
		// } else {
		// File already exists
		// }

	}
	
	@Transient
	public void generateAFileDynamicallyNoTranslate(StringBuilder pBuilder) {

		TaxonLabelSet tlSet = getTaxonLabelSet();
		List<TaxonLabel> txnlbllist = tlSet.getTaxonLabelsReadOnly();
		int numoftxnlbls = getTaxonNumber();

		String title = getTitle(); 
		if (TreebaseUtil.isEmpty(title)) {
			//use the default title:
			//title = "List of Uploaded Tree Block";
		}
		pBuilder.append("BEGIN TREES;\n");
		pBuilder.append("      TITLE " + StringUtil.tokenize(getTitle()) + ";\n");
		pBuilder.append("      LINK TAXA = " + StringUtil.tokenize(tlSet.getTitle().replaceAll("Input|Output", "")) + ";\n");


		for (PhyloTree atree : getTreeList()) {
			pBuilder.append("      TREE " + StringUtil.tokenize(atree.getLabel()) + " = ");

			if (atree.getRootedTree() != null) {
				if (atree.getRootedTree().booleanValue()) {
					pBuilder.append("[&R] ");
				} else {
					pBuilder.append("[&U] ");
				}

			}
			String newick = atree.getNewickString();
			for (int z = 0; z < numoftxnlbls; z++) {
				newick.concat(StringUtil.tokenize(txnlbllist.get(z).getTaxonLabel()));
			}
			// out.append(atree.getNewickString());
			pBuilder.append(newick);
			pBuilder.append("\n");
		}
		pBuilder.append("\n\n\nEND;\n");

		// File did not exist and was created
		// } else {
		// File already exists
		// }

	}

	@Transient
	public String getTaxonLabelSetTitle() {
		return getTaxonLabelSet().getTitle();
	}

	@Transient
	public int getTaxonNumber() {
		return getTaxonLabelList().size();
	}

	@Transient
	public List<TaxonLabel> getTaxonLabelList() {
		TaxonLabelSet tlSet = getTaxonLabelSet();
		return tlSet == null ? new ArrayList<TaxonLabel>() 
				             : tlSet.getTaxonLabelsReadOnly();
	}
	
	@Transient
	public String getLabel() {
		return getTitle();
	}
	
	@Transient
	public Study getContext() {
		PhyloTree firstTree = getTreeList().iterator().next();
		if ( null != firstTree ) {
		return firstTree.getContext();
		}		
		return null;
	}

}
