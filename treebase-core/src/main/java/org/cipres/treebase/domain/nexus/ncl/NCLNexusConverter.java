/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.domain.nexus.ncl;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import org.cipres.CipresIDL.api1.DataMatrix;
import org.cipres.CipresIDL.api1.Tree;
import org.cipres.datatypes.PhyloDataset;

import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.nexus.AbstractNexusConverter;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.NexusParserConverter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeNode;
import org.cipres.treebase.event.ProgressionListener;

/**
 * NCLNexusConverter.java
 * 
 * Created on Aug 23, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class NCLNexusConverter extends AbstractNexusConverter implements NexusParserConverter {
	private static final Logger LOGGER = Logger.getLogger(NCLNexusConverter.class);

	/**
	 * Constructor.
	 */
	public NCLNexusConverter() {
		super();
	}

	/**
	 * 
	 * @param pDataSet
	 * @param pCipresData
	 */
	public void parsePhyloDataSet(Study pStudy, NexusDataSet pDataSet, PhyloDataset pCipresData) {
		if (pCipresData == null) {
			return;
		}

		setStudy(pStudy);

		// convert taxa block first:
		convertTaxonData(pDataSet, pCipresData);
		convertMatrixData(pDataSet, pCipresData);
		convertTreeData(pDataSet, pCipresData);
	}

	/**
	 * 
	 * @param pCipresData
	 */
	private void convertMatrixData(NexusDataSet pDataSet, PhyloDataset pCipresData) {

		DataMatrix cipresMatrix = pCipresData.getDataMatrix();
		if (cipresMatrix == null) {
			return;
		}

		Matrix matrix = NCLMatrixConverter.convertMatrix(
			cipresMatrix,
			pDataSet.getTaxonLabels(),
			getMatrixDataTypeHome());

		List<Matrix> matrices = pDataSet.getMatrices();

		matrices.clear();
		matrices.add(matrix);
	}

	/**
	 * 
	 * @param pCipresData
	 */
	private void convertTaxonData(NexusDataSet pDataSet, PhyloDataset pCipresData) {

		String[] taxaInfo = pCipresData.getTaxaInfo();
		if (taxaInfo == null) {
			return;
		}

		List<TaxonLabel> taxonLabels = pDataSet.getTaxonLabels();
		taxonLabels.clear();

		for (int i = 0; i < taxaInfo.length; i++) {
			String aLabel = taxaInfo[i];

			TaxonLabel taxonLabel = getTaxonLabelHome()
				.getByDescriptionAndStudy(aLabel, getStudy());

			taxonLabels.add(i, taxonLabel);
		}
	}

	/**
	 * 
	 * @param pCipresData
	 */
	private void convertTreeData(NexusDataSet pDataSet, PhyloDataset pCipresData) {

		Tree[] trees = pCipresData.getTrees();

		if (trees == null) {
			return;
		}

		List<PhyloTree> phyloTrees = pDataSet.getPhyloTrees();
		phyloTrees.clear();
		List<TaxonLabel> taxonLabels = pDataSet.getTaxonLabels();

		for (int i = 0; i < trees.length; i++) {
			Tree tree = trees[i];

			PhyloTree phyloTree = new PhyloTree();
			phyloTree.setLabel(tree.m_name);
			phyloTree.setNewickString(createNodes(tree.m_newick, phyloTree, taxonLabels));

			// TODO: tree score, ignore for now
			// TODO: leafset, ignore for now
			// TODO: decode newick string.

			phyloTrees.add(phyloTree);
		}
	}

	/**
	 * Create tree nodes; replace the index in newich string to use taxa label.
	 * 
	 * @param pNewick
	 * @param pTree
	 * @return
	 */
	private String createNodes(String pNewick, PhyloTree pTree, List<TaxonLabel> pTaxonLabels) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("replaceTaxaLabel:" + pNewick); //$NON-NLS-1$
		}

		// replaceTaxaLabel
		StringTokenizer st = new StringTokenizer(pNewick, NEWICK_DELIMITERS, true);
		int taxaSize = pTaxonLabels.size();
		StringBuilder newickTaxa = new StringBuilder();

		while (st.hasMoreTokens()) {
			String element = st.nextToken();
			int i = -1;
			boolean isNumber = false;
			try {
				// Notes: NCL parsed output is 1 based.
				i = Integer.parseInt(element) - 1;
				if (i >= 0 && i < taxaSize) {
					isNumber = true;
				}

			} catch (NumberFormatException ex) {
				isNumber = false;
			}

			if (isNumber) {
				TaxonLabel taxonLabel = pTaxonLabels.get(i);
				PhyloTreeNode aNode = new PhyloTreeNode();
				aNode.setTaxonLabel(taxonLabel);
				pTree.addTreeNode(aNode);

				newickTaxa.append(taxonLabel.getTaxonLabel());
			} else {
				newickTaxa.append(element);
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" input:" + pNewick); //$NON-NLS-1$
			LOGGER.debug(" output:" + newickTaxa.toString()); //$NON-NLS-1$
		}
		return newickTaxa.toString();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.NexusParserConverter#processLoadFile(java.util.Collection,
	 *      org.cipres.treebase.domain.study.Study, org.cipres.treebase.domain.nexus.NexusDataSet,
	 *      org.cipres.treebase.event.ProgressionListener)
	 */
	public void processLoadFile(
		Collection<File> pNexusFiles,
		Study pStudy,
		NexusDataSet pDataSet,
		ProgressionListener pListener) {

		int fileCount = pNexusFiles.size();
		int processedCount = 0;

		for (File file : pNexusFiles) {
			PhyloDataset cipresData = null;
			try {
				cipresData = new PhyloDataset(file);
			} catch (Exception ex) {
				// It could be the nexus server has error
				// or the nexus file has error:
				String msg = "Failed to parse input file:" + file.getPath();

				LOGGER.error(msg, ex);

				// TODO: use an Error object to transfer the msg to user.
				msg += ex.getLocalizedMessage();

				cipresData = null;
				// convert exception to run time exception:
				// throw new RuntimeException(msg, ex);
			}

			if (cipresData != null) {
				NexusDataSet oneFileData = new NexusDataSet();

				parsePhyloDataSet(pStudy, oneFileData, cipresData);

				pDataSet.addAllData(oneFileData);
			}

			// TODO: if pListener is not null, launch a thread to do the parsing.
			processedCount++;
			if (pListener != null) {
				pListener.updateProgress(processedCount, fileCount);
			}
		}
	}

}
