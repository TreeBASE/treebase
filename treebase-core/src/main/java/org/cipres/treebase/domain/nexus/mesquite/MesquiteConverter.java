
package org.cipres.treebase.domain.nexus.mesquite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mesquite.Mesquite;
import mesquite.lib.ListableVector;
import mesquite.lib.MesquiteDouble;
import mesquite.lib.MesquiteFile;
import mesquite.lib.MesquiteModule;
import mesquite.lib.MesquiteProject;
import mesquite.lib.MesquiteTree;
import mesquite.lib.MesquiteTrunk;
import mesquite.lib.Parser;
import mesquite.lib.StringUtil;
import mesquite.lib.Taxa;
import mesquite.lib.Taxon;
import mesquite.lib.Tree;
import mesquite.lib.TreeVector;
import mesquite.lib.duties.TreesManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.ItemDefinitionHome;
import org.cipres.treebase.domain.nexus.AbstractNexusConverter;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.NexusParserConverter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeNode;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.event.ProgressionListener;

/**
 * MesquiteConverter.java
 * 
 * Created on Aug 4, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class MesquiteConverter extends AbstractNexusConverter implements NexusParserConverter {
	private static final Logger LOGGER = LogManager.getLogger(MesquiteConverter.class);

	private static boolean mInitMesquite = false;

	private List<TaxonLabel> mCurrentTaxaList;
	private Taxa mCurrentTreeTaxa;

	private ItemDefinitionHome mItemDefinitionHome;

	/**
	 * Return the InitMesquite field.
	 * 
	 * @return boolean mInitMesquite
	 */
	private static boolean isInitMesquite() {
		return mInitMesquite;
	}

	/**
	 * Set the InitMesquite field.
	 */
	private static void setInitMesquite(boolean pNewInitMesquite) {
		mInitMesquite = pNewInitMesquite;
	}

	/**
	 * Read the Mesquite log file and capture the info regarding the parsing of specified NEXUS file.
	 * capture everything between:
	 *    start: Reading NEXUS file Combined_Bayes_orig.nex
	 *    end: File reading complete (file Combined_Bayes_orig.nex)
	 * 
	 * @param pFileName
	 * @return
	 */
	public static synchronized String getParsingLog(String pFileName) {

		if (pFileName == null) {
			return "File name is empty.";
		}
		
		StringBuilder builder = new StringBuilder();

		// read the mesquite log file about the file parsing output:
		// alert: code copied from MesquiteFile.writeToLog()
		String mesqLogPath = MesquiteModule.userDirectory + MesquiteFile.fileSeparator
			+ "Mesquite_Support_Files" + MesquiteFile.fileSeparator + MesquiteTrunk.logFileName; // TODO:
																									// should
		try {
			BufferedReader reader = new BufferedReader(new FileReader(mesqLogPath));

			boolean found = false;
			boolean done = false;
			String aLine;

			//capture everything between:
			// start: Reading NEXUS file Combined_Bayes_orig.nex
			// end: File reading complete (file Combined_Bayes_orig.nex)
			while ((aLine = reader.readLine()) != null && !done) {
				if (aLine.startsWith("Reading NEXUS file") && aLine.indexOf(pFileName) > 0) {
					found = true;
				} else if (aLine.startsWith("File reading complete") && aLine.indexOf(pFileName) > 0) {
					done = true;
				}

				if (found) {
					builder.append(aLine).append(TreebaseUtil.LINESEP);
				}
			}
			reader.close();
		} catch (IOException ex) {
			builder.append(ex.toString());
		}
		return builder.toString();
	}

	/**
	 * Constructor.
	 */
	public MesquiteConverter() {
		super();
	}

	/**
	 * Return the ItemDefinitionHome field.
	 * 
	 * @return ItemDefinitionHome mItemDefinitionHome
	 */
	private ItemDefinitionHome getItemDefinitionHome() {
		return mItemDefinitionHome;
	}

	/**
	 * Set the ItemDefinitionHome field.
	 */
	public void setItemDefinitionHome(ItemDefinitionHome pNewItemDefinitionHome) {
		mItemDefinitionHome = pNewItemDefinitionHome;
	}

	/**
	 * Return the TaxaList field.
	 * 
	 * @return List mCurrentTaxaList
	 */
	private List<TaxonLabel> getCurrentTaxaList() {
		return mCurrentTaxaList;
	}

	/**
	 * Set the TaxaList field.
	 */
	private void setCurrentTaxaList(List<TaxonLabel> pNewTaxaList) {
		mCurrentTaxaList = pNewTaxaList;
	}

	/**
	 * Return the CurrentTreeTaxa field.
	 * 
	 * @return Taxa mCurrentTreeTaxa
	 */
	private Taxa getCurrentTreeTaxa() {
		return mCurrentTreeTaxa;
	}

	/**
	 * Set the CurrentTreeTaxa field.
	 */
	private void setCurrentTreeTaxa(Taxa pNewCurrentTreeTaxa) {
		mCurrentTreeTaxa = pNewCurrentTreeTaxa;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.NexusParserConverter#processLoadFile(java.util.Collection,
	 *      org.cipres.treebase.domain.study.Study, org.cipres.treebase.domain.nexus.NexusDataSet,
	 *      org.cipres.treebase.event.ProgressionListener)
	 */
	@Deprecated
	public void processLoadFile(
		Collection<File> pNexusFiles,
		Study pStudy,
		NexusDataSet pDataSet,
		ProgressionListener pListener) {

		synchronized (MesquiteConverter.class) {
			if (!isInitMesquite()) {
				Mesquite.main(new String[] {"-w"});
				setInitMesquite(true);
			}
		}

		int fileCount = pNexusFiles.size();
		int processedCount = 0;

		for (File file : pNexusFiles) {

			parseOneFile(file, pStudy, pDataSet);

			// TODO: if pListener is not null, launch a thread to do the parsing.
			processedCount++;
			if (pListener != null) {
				pListener.updateProgress(processedCount, fileCount);
			}
		}
	}

	/**
	 * 
	 * @param pFile
	 * @param pDataSet
	 * @param pStudy
	 */
	public void parseOneFile(File pFile, Study pStudy, NexusDataSet pDataSet) {

		// make sure no two calls can fight each other:
		synchronized (MesquiteConverter.class) {
			if (!isInitMesquite()) {
				Mesquite.main(new String[] {"-w", "-b"});
				setInitMesquite(true);
			}
		}

		// Old code for Mesquite 1.11
		// MesquiteProject project = MesquiteTrunk.mesquiteTrunk.openFile(
		// pFile.getAbsolutePath(),
		// CommandRecord.scriptingRecord);

		// for Mesquite 2.01
		MesquiteProject project = MesquiteTrunk.mesquiteTrunk.openFile(pFile.getAbsolutePath());
		if (project == null) {
			LOGGER.warn("Mesquite failed to parse the file " + pFile.getAbsolutePath() + "; skipping it");
			return;
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("MesquiteCOnverter: MesquiteProject project=" + project); //$NON-NLS-1$
		}

		// taxon first:
		ListableVector taxas = project.getTaxas();
		Taxa t = null;

		for (int i = 0; i < taxas.size(); i++) {
			// One Taxa represents one TAXA block in a Nexus file.
			t = (Taxa) taxas.elementAt(i);
			if (!t.isDoomed()) {
				// a taxa is represented by a TaxonLabelSet
				// name is important:
				TaxonLabelSet labelSet = new TaxonLabelSet();
				labelSet.setStudy(pStudy);
				labelSet.setTitle(t.getName());
				labelSet.setTaxa(true);

				// TODO: make sure the name is not null
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("taxa(" + i + ") name=" + t.getName()); //$NON-NLS-1$
				}

				// FIXME: instead of using a loop, use one query!
				long[] taxaIDs = t.getTaxaIDs();
				for (int j = 0; j < taxaIDs.length; j++) {
					Taxon taxon = t.getTaxon(j);

					String aLabel = taxon.getName();

					TaxonLabel taxonLabel = getTaxonLabelHome().getByDescriptionAndStudy(
						aLabel,
						pStudy);

					labelSet.addPhyloTaxonLabel(taxonLabel);
				}

				pDataSet.addTaxonLabelSet(t, labelSet);
			}
		}

		// matrix:
		MesquiteMatrixConverter.convertMatrices(
			project,
			pDataSet,
			pFile.getName(),
			getMatrixDataTypeHome(),
			getItemDefinitionHome());

		// tree:
		TreesManager treeManager = (TreesManager) project.ownerModule
			.findElementManager(TreeVector.class);
		ListableVector treeVectors = treeManager.getTreeBlockVector();
		for (int i = 0; i < treeVectors.size(); i++) {
			// One treeVector represents one tree block
			TreeVector treeVector = (TreeVector) treeVectors.elementAt(i);
			// System.out.println(" treeVector: " + i + " - " + treeVector.getName());

			TreeBlock treeblock = new TreeBlock();
			treeblock.setTitle(treeVector.getName());

			// first get the taxon list for the trees:
			Taxa treeTaxa = treeVector.getTaxa();
			// String taxaName = treeTaxa.getName();
			TaxonLabelSet tlSet = pDataSet.getTaxonLabelSet(treeTaxa);
			treeblock.setTaxonLabelSet(tlSet);

			List<TaxonLabel> treeTaxonLabels = null;
			if (tlSet != null) {
				treeTaxonLabels = tlSet.getTaxonLabelsReadOnly();
			}

			// To be used by the tree traversal methods.
			setCurrentTaxaList(treeTaxonLabels);
			setCurrentTreeTaxa(treeTaxa);

			Enumeration e = treeVector.elements();
			while (e.hasMoreElements()) {
				Tree mesqTree = (Tree) e.nextElement();
				String newick = mesqTree.writeTreeByNames(true);

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" tree=" + newick); //$NON-NLS-1$
				}

				// System.out.println(" tree: " + mesqTree.getName() + " " + newick);

				PhyloTree phyloTree = new PhyloTree();
				phyloTree.setLabel(mesqTree.getName());
				phyloTree.setNexusFileName(pFile.getName());
				phyloTree.setPublished(false);
				phyloTree.setRootedTree(mesqTree.getRooted());
				phyloTree.setNewickString(newick);
				phyloTree.setStudy(pStudy);
				// phyloTree.setBigTree(false); //TODO
				// phyloTree.setTreeType(treeType);
				// phyloTree.setTreeAttribute(pNewTreeAttribute);
				// phyloTree.setTreeQuality(pNewTreeQuality);

				int rootIndex = mesqTree.getRoot();
				PhyloTreeNode rootNode = createNode(mesqTree, null, rootIndex);
				phyloTree.addTreeNode(rootNode);
				phyloTree.setRootNode(rootNode);

				treeTraversal(mesqTree, rootIndex, rootNode);

				phyloTree.updateSubtreeBounds();

				treeblock.addPhyloTree(phyloTree);
				// pDataSet.getPhyloTrees().add(phyloTree);

				// verification
				if (LOGGER.isDebugEnabled()) {
					Set<PhyloTreeNode> nodes = phyloTree.getTreeNodesReadOnly();
					LOGGER.debug("-num of nodes=" + nodes.size()); //$NON-NLS-1$
				}

			}

			pDataSet.getTreeBlocks().add(treeblock);
		}

		// Note: cannot dispose the project here. It needs to be disposed after all data
		// have been saved to db.
		// project.dispose();
		pDataSet.setMesqProject(project);

	}

	/**
	 * Rebuild the tree nodes from the newick string.
	 * 
	 * @param pTree
	 * @param pTaxonLabels
	 * @param pNewick
	 */
	public void buildNodesFromNewick(PhyloTree pTree, List<TaxonLabel> pTaxonLabels, String pNewick) {
		if (pTree == null) {
			return;
		}

		pTree.clearNodes();
		pTree.setNewickString(pNewick);

		if (TreebaseUtil.isEmpty(pNewick)) {
			return;
		}

		// make new taxa:
		Taxa taxa = new Taxa(pTaxonLabels.size());
		Iterator<TaxonLabel> iter = pTaxonLabels.iterator();
		Map<String, Integer> taxonLabelStrToIndexMap = new HashMap<String, Integer>();

		for (int it = 0; it < pTaxonLabels.size(); it++) {
			String taxonLabelStr = iter.next().getTaxonLabel();
			taxa.getTaxon(it).setName(taxonLabelStr);
			taxonLabelStrToIndexMap.put(StringUtil.blanksToUnderline(taxonLabelStr), it + 1);
			// taxa.getTaxon(it).setNameIsDefault(true);
		}

		// need to make sure newick string consists of indices:
		StringBuffer newick = new StringBuffer();
		Parser mesqParser = new Parser(pNewick);
		String aToken = mesqParser.getNextToken();
		int i = 0;
		while (!TreebaseUtil.isEmpty(aToken)) {
			// logger.debug(" " + i+ ": " + aToken);
			String aTokenwithUnderline = StringUtil.blanksToUnderline(aToken);
			if (taxonLabelStrToIndexMap.containsKey(aTokenwithUnderline)) {
				newick.append(taxonLabelStrToIndexMap.get(aTokenwithUnderline).toString());
			} else {
				newick.append(aToken);
			}
			i++;
			aToken = mesqParser.getNextToken();
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("replace newick=" + newick); //$NON-NLS-1$
		}

		// TODO: handle NPE in readClade()
		Tree mesqTree = new MesquiteTree(taxa, newick.toString());

		// used by tree traversal
		setCurrentTaxaList(pTaxonLabels);
		setCurrentTreeTaxa(taxa);

		// String newick = mesqTree.writeTreeByNames(true);

		// if (LOGGER.isDebugEnabled()) {
		// LOGGER.debug(" tree=" + newick); //$NON-NLS-1$
		// }

		// phyloTree.setLabel(mesqTree.getName());
		// phyloTree.setNexusFileName(pFile.getName());
		// phyloTree.setPublished(false);
		// phyloTree.setRootedTree(mesqTree.getRooted());
		// phyloTree.setNewickString(newick);
		// phyloTree.setBigTree(false); //
		// phyloTree.setTreeType(treeType);
		// phyloTree.setTreeAttribute(pNewTreeAttribute);
		// phyloTree.setTreeQuality(pNewTreeQuality);

		int rootIndex = mesqTree.getRoot();
		PhyloTreeNode rootNode = createNode(mesqTree, null, rootIndex);
		pTree.addTreeNode(rootNode);
		pTree.setRootNode(rootNode);

		treeTraversal(mesqTree, rootIndex, rootNode);

		pTree.updateSubtreeBounds();

		if (LOGGER.isDebugEnabled()) {
			Set<PhyloTreeNode> nodes = pTree.getTreeNodesReadOnly();
			LOGGER.debug("-num of nodes=" + nodes.size()); //$NON-NLS-1$
		}
	}

	/**
	 * Recursively traversal a tree from the specified parent node.
	 * 
	 * @param pMesquiteTree
	 * @param pParentNodeIndex
	 * @param pParentNode
	 */
	private void treeTraversal(Tree pMesquiteTree, int pParentNodeIndex, PhyloTreeNode pParentNode) {

		int index = pMesquiteTree.firstDaughterOfNode(pParentNodeIndex);
		if (!pMesquiteTree.nodeExists(index)) {
			return;
		}

		PhyloTreeNode aNode = createNode(pMesquiteTree, pParentNode, index);

		// for sibling nodes:
		int siblingNodeIndex = pMesquiteTree.nextSisterOfNode(index);
		while (siblingNodeIndex > 0) {
			PhyloTreeNode siblingNode = createNode(pMesquiteTree, pParentNode, siblingNodeIndex);

			treeTraversal(pMesquiteTree, siblingNodeIndex, siblingNode);

			siblingNodeIndex = pMesquiteTree.nextSisterOfNode(siblingNodeIndex);
		}
		treeTraversal(pMesquiteTree, index, aNode);

		//		
		// for(int d = pMesquiteTree.firstDaughterOfNode(pNodeIndex); pMesquiteTree.nodeExists(d);
		// d=pMesquiteTree.nextSisterOfNode(d)) {
		// treeTraversal(pMesquiteTree, d);
		// }
	}

	/**
	 * Create a node.
	 * 
	 * @param pMesquiteTree
	 * @param pParentNode
	 * @param pNodeIndex
	 * @return
	 */
	private PhyloTreeNode createNode(Tree pMesquiteTree, PhyloTreeNode pParentNode, int pNodeIndex) {
		PhyloTreeNode aNode = new PhyloTreeNode();
		// PhyloTree tree = pParentNode.getTree();
		// aNode.setTree(tree);

		double branchLength = pMesquiteTree.getBranchLength(pNodeIndex);
		if (MesquiteDouble.unassigned != branchLength) {
			aNode.setBranchLength(branchLength);
		}

		String nodeLabel = pMesquiteTree.getNodeLabel(pNodeIndex);
		if (!TreebaseUtil.isEmpty(nodeLabel)) {

			// Note: the node label is the same as the taxon label name
			// for qualified leave nodes.
			aNode.setName(nodeLabel);

			// TODO: need verify
			List<TaxonLabel> currentTaxa = getCurrentTaxaList();
			Taxa currentTreeTaxa = getCurrentTreeTaxa();

			String taxonLabelName = currentTreeTaxa.getTaxonName(pMesquiteTree
				.taxonNumberOfNode(pNodeIndex));
			aNode.setTaxonLabel(getTaxaLabel(currentTaxa, taxonLabelName));

		}

		// TODO aNode.setNodeDepth();

		if (pParentNode != null) {
			pParentNode.addChildNode(aNode);
		}

		return aNode;
	}

	/**
	 * 
	 * @param pCurrentTaxa
	 * @param pTaxonLabelName
	 * @return
	 */
	private TaxonLabel getTaxaLabel(List<TaxonLabel> pCurrentTaxa, String pTaxonLabelName) {

		for (TaxonLabel aLabel : pCurrentTaxa) {
			if (aLabel.getTaxonLabel() != null
				&& aLabel.getTaxonLabel().equalsIgnoreCase(pTaxonLabelName)) {
				return aLabel;
			}
		}

		return null;
	}
}
