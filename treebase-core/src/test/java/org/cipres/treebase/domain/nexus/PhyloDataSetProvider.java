
package org.cipres.treebase.domain.nexus;

import java.util.List;

import org.cipres.datatypes.PhyloDataset;

/**
 * PhyloDataSetProvider.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface PhyloDataSetProvider {

	/**
	 * Return the cipres phylo data set field.
	 * 
	 * @return PhyloDataset
	 */
	PhyloDataset getCipresData();

	/**
	 * Return the TaxaLabels field.
	 * 
	 * @return List<String>
	 */
	List<String> getTaxaLabels();

	/**
	 * Verify the nexus data set is mapped from the cipres phylodataset.
	 * 
	 * Use Assert.assert to check the mapping.
	 * 
	 * @param pNexusData
	 */
	void verify(NexusDataSet pNexusData);

}