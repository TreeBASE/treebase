
package org.cipres.treebase.domain.tree;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;

import mesquite.lib.MesquiteDouble;
import mesquite.lib.StringUtil;

import org.cipres.treebase.Constants;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.Annotation;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NamedNativeQuery;

/**
 * PhyloTree.java
 * 
 * Created on Feb 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@Table(name = "PHYLOTREE")
@AttributeOverride(name = "id", column = @Column(name = "PHYLOTREE_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
@BatchSize(size = 10)
@NamedNativeQuery(name="loadNodes", query="select PhyloTreeNode_id, version, name, nodeDepth, " +
		"branchLength, rightNode, leftNode, phyloTree_id, NodeAttribute_id, TaxonLabel_id, Child_id, Sibling_id from PhyloTreeNode where phyloTree_id = ?", 
	resultSetMapping="nodeMapping")
@SqlResultSetMapping(name="nodeMapping", entities = {@EntityResult(entityClass = org.cipres.treebase.domain.tree.PhyloTreeNode.class, 
	fields= {@FieldResult(name="id", column="PhyloTreeNode_id"),
	@FieldResult(name="version", column="version"),
	@FieldResult(name="name", column="name"),
	@FieldResult(name="nodeDepth", column="nodeDepth"),
	@FieldResult(name="branchLength", column="branchLength"),
	@FieldResult(name="leftNode", column="leftNode"),
	@FieldResult(name="rightNode", column="rightNode"),
	@FieldResult(name="tree", column="phyloTree_id"),
	@FieldResult(name="nodeAttribute", column="NodeAttribute_id"),
	@FieldResult(name="taxonLabel", column="TaxonLabel_id"),
	@FieldResult(name="childNode", column="Child_id"),
	@FieldResult(name="siblingNode", column="Sibling_id")
	})})

public class PhyloTree extends AbstractPersistedObject {

	private static final long serialVersionUID = -6428014854319580751L;

	private String mTitle;
	private String mLabel;
	private Boolean mRootedTree;
	private Boolean mBigTree;
	private String mNewickString;
	private String mNexusFileName;
	private String mTB1TreeID;

	private boolean mPublished;

	private TreeType mTreeType;
	private TreeKind mTreeKind;
	private TreeQuality mTreeQuality;
	private PhyloTreeNode mRootNode;
	private TreeAttribute mTreeAttribute;
	private Study mStudy;
	private TreeBlock mTreeBlock;
	
	private Set<PhyloTreeNode> mTreeNodes = new HashSet<PhyloTreeNode>();
	private Integer mNtax;	// 20081205 mjd

	//transient:
	private String mTypeDescription;
	private String mKindDescription;
	private String mQualityDescription;
	private boolean mAnalyzed;

	/**
	 * Constructor.
	 */
	public PhyloTree() {
		super();
	}

	/**
	 * Return the NewickString field.
	 * 
	 * @return String
	 */
	@Lob
	@Column(name = "NewickString", length = 4194304)
	public String getNewickString() {
		// CLOB size: 4MB.
		return mNewickString;
	}

	/**
	 * Set the NewickString field.
	 */
	public void setNewickString(String pNewNewickString) {
		mNewickString = pNewNewickString;
	}

	/**
	 * Return the BigTree field.
	 * 
	 * @return Boolean
	 */
	@Column(name = "BigTree")
	public Boolean getBigTree() {
		return mBigTree;
	}

	/**
	 * Set the BigTree field.
	 */
	public void setBigTree(Boolean pNewBigTree) {
		mBigTree = pNewBigTree;
	}

	/**
	 * Return the RootedTree field.
	 * 
	 * @return Boolean
	 */
	@Column(name = "RootedTree")
	public Boolean getRootedTree() {
		return mRootedTree;
	}

	/**
	 * Set the RootedTree field.
	 */
	public void setRootedTree(Boolean pNewRootedTree) {
		mRootedTree = pNewRootedTree;
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
	 * Return the Published field.
	 * 
	 * @return boolean mPublished
	 */
	@Column(name = "Published")
	public boolean isPublished() {
		return mPublished;
	}

	/**
	 * Set the Published field.
	 */
	public void setPublished(boolean pNewPublished) {
		mPublished = pNewPublished;
	}

	/**
	 * Return the Title field. A tree title is not stored in nexus/newick string.
	 * 
	 * The user needs to input the tree title through treebase gui.
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
	 * Return the Label field. A tree label is the field specified in a newick string. Like the
	 * "Fig._5" in :
	 * 
	 * Tree Fig._5 = (1, (2,3) ...
	 * 
	 * @return String
	 */
	@Override
	@Column(name = "Label", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getLabel() {
		return mLabel;
	}

	/**
	 * Set the Label field.
	 */
	public void setLabel(String pLabel) {
		mLabel = pLabel;
	}

	/**
	 * Return the NexusFileName field.
	 * 
	 * @return String
	 */
	@Column(name = "NexusFileName", length = TBPersistable.COLUMN_LENGTH_STRING)
	public String getNexusFileName() {
		return mNexusFileName;
	}

	/**
	 * Set the NexusFileName field.
	 */
	public void setNexusFileName(String pNewNexusFileName) {
		mNexusFileName = pNewNexusFileName;
	}

	/**
	 * Return the TBTreeID field.
	 * 
	 * @return String 
	 */
	@Column(name = "TB1_TREEID", length = TBPersistable.COLUMN_LENGTH_30)
	public String getTB1TreeID() {
		return mTB1TreeID;
	}

	/**
	 * Set the TB1TreeID field.
	 */
	public void setTB1TreeID(String pNewTB1TreeID) {
		mTB1TreeID = pNewTB1TreeID;
	}
	
	/**
	 * Return the TreeQuality field.
	 * 
	 * @return TreeQuality
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinColumn(name = "TREEQUALITY_ID")
	@Fetch(FetchMode.JOIN)
	public TreeQuality getTreeQuality() {
		return mTreeQuality;
	}

	/**
	 * Set the TreeQuality field.
	 */
	public void setTreeQuality(TreeQuality pNewTreeQuality) {
		mTreeQuality = pNewTreeQuality;
	}

	/**
	 * Return the TreeBlock field.
	 * 
	 * @return TreeBlock mTreeBlock
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TREEBLOCK_ID", nullable = true)
	public TreeBlock getTreeBlock() {
		return mTreeBlock;
	}

	/**
	 * Set the TreeBlock field.
	 */
	public void setTreeBlock(TreeBlock pNewTreeBlock) {
		mTreeBlock = pNewTreeBlock;
	}

	/**
	 * Return the Tree kind.
	 * 
	 * @return TreeKind
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TREEKIND_ID")
	@Fetch(FetchMode.JOIN)
	public TreeKind getTreeKind() {
		return mTreeKind;
	}

	/**
	 * Set the Tree kind.
	 */
	public void setTreeKind(TreeKind pTreeKind) {
		mTreeKind = pTreeKind;
	}

	/**
	 * Return the TreeType field.
	 * 
	 * @return TreeType
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinColumn(name = "TREETYPE_ID")
	@Fetch(FetchMode.JOIN)
	public TreeType getTreeType() {
		return mTreeType;
	}

	/**
	 * Set the TreeType field.
	 */
	public void setTreeType(TreeType pNewTreeType) {
		mTreeType = pNewTreeType;
	}

	/**
	 * Return the RootNode field.
	 * 
	 * @return PhyloTreeNode
	 */
	@OneToOne(targetEntity = PhyloTreeNode.class, fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "ROOTNODE_ID")
	public PhyloTreeNode getRootNode() {
		return mRootNode;
	}

	/**
	 * Set the RootNode field.
	 */
	public void setRootNode(PhyloTreeNode pNewRootNode) {
		mRootNode = pNewRootNode;
	}

	/**
	 * Return the TreeAttribute field.
	 * 
	 * @return TreeAttribute
	 */
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@JoinColumn(name = "TREEATTRIBUTE_ID", nullable = true)
	public TreeAttribute getTreeAttribute() {
		return mTreeAttribute;
	}

	/**
	 * Set the TreeAttribute field.
	 */
	public void setTreeAttribute(TreeAttribute pNewTreeAttribute) {
		mTreeAttribute = pNewTreeAttribute;
	}

	/**
	 * Return the TreeNodes in read only.
	 * 
	 * @return Set<TreeNode>
	 */
	@Transient
	public Set<PhyloTreeNode> getTreeNodesReadOnly() {
		return Collections.unmodifiableSet(getTreeNodes());
	}

	/**
	 * Return the TreeNodes field. Use hibernate Subselect optimization to
	 * load all nodes in two sql statements. 
	 * 
	 * @return Set<TreeNode> 
	 */
	//@OneToMany(mappedBy = "tree", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@OneToMany(mappedBy = "tree", cascade = {CascadeType.ALL})
	//@Fetch(FetchMode.SUBSELECT)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "treeCache")
	//@Loader(namedQuery="loadNodes")
	//@BatchSize(size=20)
	protected Set<PhyloTreeNode> getTreeNodes() {
		return mTreeNodes;
	}

	/**
	 * Set the TreeNodes field.
	 */
	protected void setTreeNodes(Set<PhyloTreeNode> pNewTreeNodes) {
		mTreeNodes = pNewTreeNodes;
	}

	/**
	 * Append a new Tree node.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTreeNode
	 */
	public void addTreeNode(PhyloTreeNode pTreeNode) {
		if (pTreeNode != null && !getTreeNodes().contains(pTreeNode)) {
			getTreeNodes().add(pTreeNode);
			pTreeNode.setTree(this);
		}
	}

	/**
	 * Remove a Tree node.
	 * 
	 * Creation date: Mar 14, 2006
	 * 
	 * @param pTreeNode
	 */
	public void removeTreeNode(PhyloTreeNode pTreeNode) {
		if (pTreeNode != null && getTreeNodes().contains(pTreeNode)) {
			getTreeNodes().remove(pTreeNode);
			pTreeNode.setTree(null);
		}
	}

	/**
	 * Return the Study field.
	 * 
	 * @return Study mStudy
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	@Fetch(FetchMode.JOIN)
	public Study getStudy() {
		return mStudy;
	}

	/**
	 * Set the Study field.
	 */
	public void setStudy(Study pNewStudy) {
		mStudy = pNewStudy;
	}

	/**
	 * Format the newick string for display. Break the string into lines.
	 * 
	 * @return
	 */
	@Transient
	public String getNewickForDisplay() {
		return insertSpaces(getNewickString());
	}

	/**
	 * Inserting spaces in the newickString. Break the string into lines.
	 * 
	 * @return
	 */
	@Transient
	public String insertSpaces(String myString) {

		final int strlength = myString.length();
		final int delimiter = 65; // This parameter controls the line size.
		final int strmod = strlength / delimiter;

		StringBuilder stbr = new StringBuilder();

		// System.out.println("String is: " + myString);
		// System.out.println("String Length = " + strlength);
		// System.out.println("Mod Value = " + strmod);

		if (strlength > delimiter) {
			for (int i = 0; i < strmod; i++) {
				// System.out.println("Value of i = " + i);
				stbr.append(myString.substring(i * delimiter, (i + 1) * delimiter));
				stbr.append(" ");// This empty space is critical for wrapping up of the String in
				// JSP page
				// System.out.println("Start = " + i * delimiter + " End = " + (i+1) * delimiter);

			}
		}

		stbr.append(myString.substring(strmod * delimiter, strlength));
		return stbr.toString();
	}

	/**
	 * Regenerate the newick string.
	 */
	public void updateNewickString() {
		String newick = buildNewick();
		setNewickString(newick);
	}

	/**
	 * 
	 * @return
	 */
	protected String buildNewick() {
		StringBuilder sb = new StringBuilder("");

		//Important: need this to trigger loading all nodes at once:
		loadAllNodes();
		
		buildNewick(getRootNode(), sb);

		sb.append(';');

		return sb.toString();
	}

	/**
	 * Build the nexus block for the tree.
	 * 
	 * @param pBuilder
	 */
	public void buildNexusBlock(StringBuilder pBuilder) {
		if (pBuilder == null) {
			return;
		}
		
		String blockTitle = " ";
		String taxaTitle = " ";
		if (getTreeBlock() != null) {
			blockTitle = getTreeBlock().getTitle();
			taxaTitle = getTreeBlock().getTaxonLabelSetTitle();
		}
		
		pBuilder.append("BEGIN TREES;\n");
		pBuilder
			.append("      TITLE ").append(StringUtil.tokenize(blockTitle)).append(";\n");
		pBuilder.append("      LINK TAXA = ").append(StringUtil.tokenize(taxaTitle.replaceAll("Input|Output", ""))).append(";\n");
		pBuilder.append("      TREE ").append(StringUtil.tokenize(getLabel())).append(" = ");

		if (getRootedTree() != null) {
			if (getRootedTree().booleanValue()) {
				pBuilder.append("[&R] ");
			} else {
				pBuilder.append("[&U] ");
			}

		}
		pBuilder.append(getNewickString()).append("\n");
		pBuilder.append("[!  TreeBASE tree URI: ").append(getPhyloWSPath().getPurl()).append("]\n");

		pBuilder.append("\n\nEND;\n");
	}
	
	/**
	 * Use this method to load all nodes efficiently from database in one go.
	 *  
	 */
	public void loadAllNodes() {
		getTreeNodes().size();
	}

	/**
	 * Recursive method to build newick string.
	 * 
	 * @param pNode
	 * @param pNewick
	 */
	private void buildNewick(PhyloTreeNode pNode, StringBuilder pNewick) {
		// ALERT: move to phylotreenode class since it knows the implementation
		// of the tree node.

		if (!pNode.isLeaf()) {
			pNewick.append('(');

			PhyloTreeNode childNode = pNode.getChildNode();
			buildNewick(childNode, pNewick);

			if (childNode != null) {
				PhyloTreeNode sisterNode = childNode.getSiblingNode();
				while (sisterNode != null) {
					pNewick.append(',');
					buildNewick(sisterNode, pNewick);

					sisterNode = sisterNode.getSiblingNode();
				}
			}
			pNewick.append(')');

			// node label: match the Mesquite implementation:
			// see MesquiteTree.getNodeLabel()
			String nodeLabel = pNode.getName();
			if (nodeLabel != null) {
				// !!match the mesquite behavior!!
				// see MesquiteTree.writeTreeByNamesGeneral() method
				// Use Mesquite tokenize method to handle quote for
				// punctuation, and whitespace.
				pNewick.append(StringUtil.tokenize(nodeLabel));
			}
		} else {
			// is leaf node:
			pNewick.append(StringUtil.tokenize(pNode.getTaxonLabelAsString()));
		}

		// branch length:
		if (!pNode.isRootNode() && pNode.hasBranchLength()) {
			// !!match the mesquite behavior!!
			// see MesquiteTree.writeTreeByNamesGeneral() method
			pNewick.append(':').append(MesquiteDouble.toStringDigitsSpecified(pNode.getBranchLength().doubleValue(), -1));
		}

		// Note: Mesquite: write associated info here:
		// [%< .... >%]

		// Note: Mesquite truncate the taxon names to 10 characters.
	}

	/**
	 * Returns the tree kind description. Uses a transient variable to enable
	 * setting the tree kind as a string from the GUI. 
	 * 
	 * @return String 
	 */
	@Transient
	public String getKindDescription() {
		if (mKindDescription == null) {
			if (getTreeKind() != null) {
				mKindDescription = getTreeKind().getDescription();
			}
		}
		return mKindDescription;
	}

	/**
	 * Set the KindDescription field.
	 */
	public void setKindDescription(String pNewKindDescription) {
		mKindDescription = pNewKindDescription;
	}
	
	
	/**
	 * Returns the tree quality description. Uses a transient variable to enable
	 * setting the tree quality as a string from the GUI. 
	 * 
	 * @return String
	 */
	@Transient
	public String getQualityDescription() {
		if (mQualityDescription == null) {
			if (getTreeQuality() != null) {
				mQualityDescription = getTreeQuality().getDescription();
			}
		}
		return mQualityDescription;
	}

	/**
	 * Set the QualityDescription field.
	 */
	public void setQualityDescription(String pNewQualityDescription) {
		mQualityDescription = pNewQualityDescription;
	}
	
	/**
	 * Returns the tree type description. Uses a transient variable to enable
	 * setting the tree type as a string from the GUI. 
	 * 
	 * @return String
	 */
	@Transient
	public String getTypeDescription() {
		if (mTypeDescription == null) {
			if (getTreeType() != null) {
				mTypeDescription = getTreeType().getDescription();
			}
		}
		return mTypeDescription;
	}

	/**
	 * @param pTypeDescription
	 */
	public void setTypeDescription(String pTransientDescription) {
		mTypeDescription = pTransientDescription;
	}

	/**
	 * Update the leftNode and rightNode data for every node.
	 * 
	 * @author mjd
	 */
	public void updateSubtreeBounds() {
		if (getRootNode() != null) {
			getRootNode().updateSubtreeBounds(1L);
		}
	}

	/**
	 * Tree structure updated. Need to regenerate following: ** node bounds ** newick string
	 * 
	 */
	public void structureChanged() {
		updateSubtreeBounds();
		updateNewickString();
	}

	/**
	 * Return all taxon labels for this tree.
	 * 
	 * @return
	 */
	@Transient
	public Set<TaxonLabel> getAllTaxonLabels() {
		Set<TaxonLabel> allTaxonLabels = new HashSet<TaxonLabel>();

		// if possible, go through tree block to avoid loading all tree nodes
		if (getTreeBlock() != null) {
			allTaxonLabels.addAll(getTreeBlock().getTaxonLabelList());
		} else {
			for (PhyloTreeNode aNode : getTreeNodes()) {
				if (aNode.getTaxonLabel() != null) {
					allTaxonLabels.add(aNode.getTaxonLabel());
				}
			}
		}

		return allTaxonLabels;
	}
	
	/**
	 * Return the number of taxa in this tree
	 * 
	 * <p>Actually returns the number of leaf nodes, as per Bill's clarification<p>
	 * 
	 * @return number of taxa in this tree
	 * @author mjd 20081121
	 */
	// added in implementation where the number of tips is calculated and
	// stored if the internal boxed member mNtax is null (i.e. unassigned)
	@Column(name = "ntax")
	public Integer getnTax() {
		if ( mNtax == null ) {
			int ntax = 0;
			Iterator<PhyloTreeNode> nodeIterator = getTreeNodes().iterator();
			while ( nodeIterator.hasNext() ) {
				PhyloTreeNode node = nodeIterator.next();
				if ( node.isLeaf() ) {
					ntax++;
				}
			}
			mNtax = ntax;
		}
		return mNtax;
	}
	public void setnTax(Integer nTax) {
		mNtax = nTax;
	}

	/**
	 * Clear all nodes. Clear newick string. 
	 */
	public void clearNodes() {
		getTreeNodes().clear();
		setRootNode(null);
		setNewickString(null);
	}
	
	@Transient
	public List<Annotation> getAnnotations() {
		List<Annotation> annotations = super.getAnnotations();
		try {
			if ( null != getKindDescription() ) {
				annotations.add(new Annotation(Constants.TBTermsURI, "tb:kind.tree", getKindDescription()));
			}
			if ( null != getTypeDescription() ) {
				annotations.add(new Annotation(Constants.TBTermsURI, "tb:type.tree", getTypeDescription()));
			}
			if ( null != getQualityDescription() ) {
				annotations.add(new Annotation(Constants.TBTermsURI, "tb:quality.tree", getQualityDescription()));
			}
			if ( null != getnTax() ) {
				annotations.add(new Annotation(Constants.TBTermsURI, "tb:ntax.tree", getnTax()));
			}	
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return annotations;
	}
	
	@Override
	@Transient
	public Study getContext() {
		// org.hibernate.LazyInitializationException
		return getStudy();
	}

}
