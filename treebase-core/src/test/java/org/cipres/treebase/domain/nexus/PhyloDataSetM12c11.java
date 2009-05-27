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

import java.util.ArrayList;
import java.util.List;

import org.cipres.CipresIDL.api1.DataMatrix;
import org.cipres.CipresIDL.api1.DiscreteDatatypes;
import org.cipres.CipresIDL.api1.Tree;
import org.cipres.datatypes.PhyloDataset;

/**
 * PhyloDataSetM12c11.java
 * 
 * A test data set w/ char label and state label info.
 * 
 * Created on Jun 14, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class PhyloDataSetM12c11 extends AbstractPhyloDataSet implements PhyloDataSetProvider {

	private PhyloDataset mCipresData;
	private List<String> mTaxaLabels;
	private Tree[] mTrees;
	private DataMatrix mDataMatrix;

	/**
	 * Constructor.
	 */
	public PhyloDataSetM12c11() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.PhyloDataSetProvider#getCipresData()
	 */
	@Override
	public PhyloDataset getCipresData() {
		if (mCipresData == null) {
			mCipresData = new PhyloDataset();

			mCipresData.setDataMatrix(getDataMatrix());
			mCipresData.setTrees(getTrees());
			mCipresData.setTaxaInfo(getTaxaLabels().toArray(new String[1]));
		}
		return mCipresData;
	}

	/**
	 * Return the DataMatrix field. Uses lazy initialization.
	 * 
	 * @return DataMatrix mDataMatrix
	 */
	@Override
	protected DataMatrix getDataMatrix() {
		if (mDataMatrix == null) {
			mDataMatrix = new DataMatrix();

			// based on m1389
			mDataMatrix.m_symbols = "0123?";
			mDataMatrix.m_numStates = 4;
			mDataMatrix.m_numCharacters = 54;
			mDataMatrix.m_datatype = DiscreteDatatypes.from_int(4);
			mDataMatrix.m_charStateLookup = new short[][] { {0}, {1}, {2}, {3}, {-1, 0, 1, 2, 3},
				{0, 1, 2, 3}};

			mDataMatrix.m_matrix = new short[][] {
				{0, 0, 0, 0, 4, 0, 4, 4, 4, 4, 0, 0, 4, 4, 0, 0, 4, 0, 0, 4, 4, 4, 0, 0, 0, 0, 4,
					4, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0},

				{0, 0, 1, 0, 1, 4, 0, 1, 1, 1, 0, 1, 0, 1, 4, 4, 0, 0, 0, 0, 1, 0, 4, 4, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},

				{0, 0, 0, 0, 0, 0, 0, 1, 4, 1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 4, 0, 0, 0, 0,
					0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 4, 0, 1, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1},

				{0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0,
					1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1},

				{0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 4, 0, 1, 1, 0, 0, 0, 0, 0, 1, 4, 1, 4, 0, 0, 0,
					1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 4, 0, 1, 0, 2},

				{0, 0, 1, 4, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 4, 1, 4, 0, 0, 0,
					1, 0, 1, 0, 1, 0, 0, 0, 0, 4, 0, 0, 1, 0, 1, 0, 4, 0, 4, 0, 0, 0, 4, 0, 1, 0, 1},

				{0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 4, 0, 0, 1, 0, 0, 0,
					0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 4, 0, 1, 0, 0, 0, 0, 1, 0, 0},

				{0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 1, 4, 0, 0, 1, 4, 1, 4,
					0, 0, 1, 0, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 4, 0, 1, 0, 0, 0, 4, 0, 4, 0, 1},

				{0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 2, 0, 1, 0, 1, 4,
					0, 0, 0, 0, 1, 4, 4, 1, 4, 0, 1, 0, 2, 0, 0, 0, 0, 4, 4, 0, 0, 0, 1, 0, 1, 0, 1},

				{0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 2, 4, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,
					1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 4, 0, 4, 0, 1, 0, 4},

				{0, 0, 0, 0, 0, 0, 1, 0, 0, 4, 0, 4, 2, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 1, 0, 1, 0, 0, 4, 1, 0, 0, 4, 1, 1, 2, 0, 1, 0, 0, 0, 0, 0, 4, 0, 0, 0, 1},

				{0, 0, 1, 0, 0, 0, 0, 0, 4, 1, 0, 0, 1, 0, 2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 4, 0, 0, 0, 0},

				{0, 4, 4, 0, 4, 0, 0, 0, 4, 1, 0, 4, 1, 0, 2, 4, 0, 0, 1, 1, 2, 3, 0, 4, 4, 4, 0,
					0, 0, 1, 0, 1, 1, 1, 1, 1, 4, 1, 0, 1, 1, 1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

				{0, 0, 1, 1, 0, 1, 4, 0, 0, 1, 0, 0, 1, 0, 2, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
					1, 0, 1, 0, 0, 1, 0, 4, 0, 1, 0, 1, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 2, 4, 0, 2},

				{0, 0, 1, 0, 0, 4, 1, 0, 0, 1, 0, 1, 2, 0, 2, 0, 4, 0, 4, 4, 0, 0, 0, 0, 0, 0, 1,
					4, 0, 1, 0, 1, 0, 0, 4, 1, 1, 0, 4, 1, 0, 2, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},

				{0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 4, 1, 0, 1, 1, 2, 0, 0, 2, 4, 2, 0, 1, 1, 0, 4,
					0, 1, 1, 0, 1, 4, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 4},

				{0, 1, 1, 0, 4, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 2, 0, 0, 3, 2, 3, 0, 4, 1, 0, 1,
					0, 0, 1, 0, 2, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1},

				{0, 4, 1, 4, 4, 0, 0, 0, 0, 1, 0, 4, 0, 1, 4, 1, 2, 0, 0, 3, 2, 3, 0, 4, 1, 0, 1,
					0, 0, 1, 0, 2, 4, 0, 4, 4, 4, 0, 0, 2, 1, 2, 0, 1, 0, 0, 0, 0, 1, 4, 1, 0, 0, 1},

				{4, 4, 1, 1, 0, 4, 4, 0, 0, 1, 0, 4, 0, 0, 1, 4, 2, 0, 0, 2, 4, 1, 0, 1, 1, 0, 1,
					0, 0, 1, 0, 1, 1, 1, 4, 0, 4, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0},

				{1, 1, 0, 0, 4, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 2, 0, 0, 2, 1, 0, 0, 0, 0, 0, 4,
					4, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 4, 1, 0, 1, 1},

				{1, 1, 4, 0, 4, 0, 0, 0, 0, 0, 1, 4, 4, 0, 4, 0, 2, 1, 0, 2, 1, 1, 0, 1, 1, 0, 1,
					0, 0, 0, 0, 0, 0, 0, 0, 4, 1, 0, 0, 0, 0, 3, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 4, 1},

				{1, 1, 2, 0, 4, 0, 0, 0, 0, 0, 1, 1, 4, 4, 4, 1, 2, 1, 0, 2, 1, 0, 0, 0, 0, 0, 1,
					0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 3, 0, 1, 0, 0, 0, 0, 0, 1, 2, 4, 1, 0},

				{0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 4, 0, 1, 0, 0, 0, 0, 0, 0,
					0, 0, 1, 1, 1, 1, 1, 0, 0, 4, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1},

				{0, 0, 1, 0, 4, 0, 0, 1, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 4, 0,
					0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1},

				{0, 4, 1, 0, 4, 0, 4, 0, 0, 0, 0, 1, 0, 4, 0, 0, 2, 0, 1, 2, 4, 0, 0, 1, 4, 0, 4,
					0, 0, 1, 1, 1, 1, 4, 1, 4, 1, 0, 0, 0, 0, 4, 0, 4, 0, 1, 0, 0, 0, 1, 0, 0, 4, 0},

				{0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 4, 0, 4, 2, 0, 1, 1, 2, 2, 0, 0, 0, 0, 1,
					0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 4, 0, 0, 0, 0, 4},

				{0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 4, 0, 4, 4, 0, 1, 1, 2, 2, 0, 0, 0, 0, 1,
					0, 0, 1, 1, 1, 1, 4, 1, 4, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 4, 0, 4, 0, 0, 0, 4},

				{1, 1, 2, 0, 4, 4, 0, 0, 0, 0, 4, 0, 0, 1, 4, 4, 4, 4, 4, 4, 4, 1, 0, 1, 1, 0, 1,
					0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 4, 0, 4, 4, 4, 0, 0, 0, 4, 0, 0, 4, 4}};

		}
		return mDataMatrix;
	}

	/**
	 * Return the Trees field. Uses lazy initialization.
	 * 
	 * @return Tree[] mTrees
	 */
	@Override
	protected Tree[] getTrees() {
		if (mTrees == null) {

			// based on m1389
			mTrees = new Tree[1];
			Tree tree = new Tree();
			tree.m_name = "NYMPH-BASAL";
			tree.m_newick = "(1,((20,21),(((26,27),(25,22)),((((18,17),28),(19,16)),(((23,24),3),(((((12,13),(11,15)),10),14),(6,(8,(5,(4,(2,(9,7))))))))))));";
			tree.m_score = null; // not needed.
			tree.m_leafSet = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
				18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28};

			mTrees[0] = tree;
		}
		return mTrees;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.PhyloDataSetProvider#getTaxaLabels()
	 */
	@Override
	public List<String> getTaxaLabels() {
		if (mTaxaLabels == null) {
			// based on m1389:
			mTaxaLabels = new ArrayList<String>();

			mTaxaLabels.add("Hyp. Ancestor");
			mTaxaLabels.add("GMagnoliaceae");
			mTaxaLabels.add("Winteraceae");
			mTaxaLabels.add("Degeneria");
			mTaxaLabels.add("Eupomatia");
			mTaxaLabels.add("Himantandraceae");
			mTaxaLabels.add("Annonaceae");
			mTaxaLabels.add("Canellaceae");
			mTaxaLabels.add("Myristicaceae");
			mTaxaLabels.add("Austrobaileya");

			mTaxaLabels.add("Amborella");
			mTaxaLabels.add("Trimeniaceae");
			mTaxaLabels.add("Chloranthaceae");
			mTaxaLabels.add("Calycanthaceae");
			mTaxaLabels.add("Monimiaceae");
			mTaxaLabels.add("Lactoris");
			mTaxaLabels.add("Saururaceae");
			mTaxaLabels.add("Piperaceae");
			mTaxaLabels.add("Aritolochiaceae");
			mTaxaLabels.add("Nymphaeaceae");

			mTaxaLabels.add("Cobombaceae");
			mTaxaLabels.add("Nelumbo");
			mTaxaLabels.add("Illicium");
			mTaxaLabels.add("Schisandraceae");
			mTaxaLabels.add("Ranunculaceae");
			mTaxaLabels.add("Trochodendrales");
			mTaxaLabels.add("Hamamelidales");
			mTaxaLabels.add("Liliopsida");
		}
		return mTaxaLabels;
	}

}
