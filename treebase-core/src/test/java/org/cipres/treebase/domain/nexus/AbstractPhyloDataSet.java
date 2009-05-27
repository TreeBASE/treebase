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

package org.cipres.treebase.domain.nexus;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.cipres.CipresIDL.api1.DataMatrix;
import org.cipres.CipresIDL.api1.Tree;
import org.cipres.datatypes.PhyloDataset;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * AbstractPhyloDataSet.java
 * 
 * Created on Jun 19, 2006
 * 
 * @author Jin Ruan
 * 
 */
public abstract class AbstractPhyloDataSet implements PhyloDataSetProvider {

	/**
	 * Constructor.
	 */
	public AbstractPhyloDataSet() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.PhyloDataSetProvider#getCipresData()
	 */
	public abstract PhyloDataset getCipresData();

	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.PhyloDataSetProvider#getTaxaLabels()
	 */
	public abstract List<String> getTaxaLabels();

	/**
	 * Return the DataMatrix.
	 * 
	 * @return
	 */
	protected abstract DataMatrix getDataMatrix();

	/**
	 * Return the Trees field.
	 * 
	 * @return Tree[]
	 */
	protected abstract Tree[] getTrees();

	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.PhyloDataSetProvider#verify(org.cipres.treebase.domain.nexus.NexusDataSet)
	 */
	public void verify(NexusDataSet pNexusData) {
		verifyTaxonLabel(pNexusData.getTaxonLabels());
		verifyTrees(pNexusData.getPhyloTrees());
		verifyMatrix(pNexusData.getMatrices());
	}

	/**
	 * 
	 * @param pMatrices
	 */
	protected void verifyMatrix(List<Matrix> pMatrices) {
		// NCL has only one matrix in one nexus:
		Assert.assertTrue("matrix size does not match.", pMatrices.size() == 1);

		// TODO: verifyMatrix, need in subclass.

	}

	/**
	 * 
	 * @param pPhyloTrees
	 */
	protected void verifyTrees(List<PhyloTree> pPhyloTrees) {
		Tree[] cipresTrees = getTrees();
		Assert.assertTrue("tree size does not match.", pPhyloTrees.size() == cipresTrees.length);

		for (int i = 0; i < cipresTrees.length; i++) {
			Tree cipresTree = cipresTrees[i];

			// Iterator<PhyloTree> treeIter = pPhyloTrees.iterator();
			boolean foundMatch = false;
			for (PhyloTree aTree : pPhyloTrees) {

				if (TreebaseUtil.isEqual(aTree.getLabel(), cipresTree.m_name)) {
					foundMatch = true;

					// TODO: verifyTrees

				}
			}

			Assert.assertTrue("Cannot find match: " + cipresTree.m_name, foundMatch);
		}

	}

	/**
	 * 
	 * @param pTaxonLabels
	 */
	protected void verifyTaxonLabel(List<TaxonLabel> pTaxonLabels) {
		List<String> cipresTaxonLabels = getTaxaLabels();
		Assert.assertTrue("taxon size does not match.", pTaxonLabels.size() == cipresTaxonLabels
			.size());

		Iterator<TaxonLabel> labelIter = pTaxonLabels.iterator();
		for (String cipresTaxonLabel : cipresTaxonLabels) {

			Assert.assertTrue("taxon does not match: " + cipresTaxonLabel, cipresTaxonLabel
				.compareTo(labelIter.next().getTaxonLabel()) == 0);
		}
	}

}
