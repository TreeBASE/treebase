/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */

package org.cipres.treebase.domain.nexus;

import org.springframework.beans.factory.FactoryBean;

import org.cipres.treebase.domain.matrix.ItemDefinitionHome;
import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.service.nexus.NexusServiceMesquite;
import org.cipres.treebase.service.nexus.NexusServiceNCL;

/**
 * NexusParserFinder.java
 * 
 * Created on Aug 21, 2006
 * @author Jin Ruan
 *
 */
public class NexusParserFinder implements FactoryBean {

	//Note: match to the properties file:
	private static final String PARSER_NCL = "NexusServiceNCL";
	private static final String PARSER_MESQUITE = "NexusServiceMesquite";
	
	private String mNexusParserStr;

	private NexusService mNexusServiceImpl;

	private TaxonLabelHome mTaxonLabelHome;
	private MatrixDataTypeHome mMatrixDataTypeHome;
	private ItemDefinitionHome mItemDefinitionHome;

	/**
	 * Constructor.
	 */
	public NexusParserFinder() {
		super();
	}

	/**
	 * Return the NexusServiceImpl field. Uses lazy initialization.
	 * @return NexusService mNexusServiceImpl
	 */
	private NexusService getNexusServiceImpl() {
		if (mNexusServiceImpl == null) {
			if (PARSER_NCL.compareToIgnoreCase(getNexusParserStr()) == 0) {
				NexusServiceNCL serviceNCL = new NexusServiceNCL();
				
				serviceNCL.setMatrixDataTypeHome(getMatrixDataTypeHome());
				serviceNCL.setTaxonLabelHome(getTaxonLabelHome());
				
				mNexusServiceImpl = serviceNCL;
			} else {
				NexusServiceMesquite serviceMesquite = new NexusServiceMesquite();
				
				serviceMesquite.setItemDefinitionHome(getItemDefinitionHome());
				serviceMesquite.setMatrixDataTypeHome(getMatrixDataTypeHome());
				serviceMesquite.setTaxonLabelHome(getTaxonLabelHome());
				
				mNexusServiceImpl = serviceMesquite;
				
			}
		}
		return mNexusServiceImpl;
	}
	
	/**
	 * Return the NexusParserImpl field.
	 * 
	 * @return String mNexusParserStr
	 */
	public String getNexusParserStr() {
		return mNexusParserStr;
	}

	/**
	 * Set the NexusParserImpl field.
	 */
	public void setNexusParserStr(String pNewNexusParserImpl) {
		mNexusParserStr = pNewNexusParserImpl;
		
		//regenerate service impl
		// setNexusServiceImpl(null);
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
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return getNexusServiceImpl();
	}

	/** 
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return NexusService.class;
	}

	/** 
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @param pNexusServiceImpl the nexusServiceImpl to set
	 */
	public void setNexusServiceImpl(NexusService pNexusServiceImpl) {
		mNexusServiceImpl = pNexusServiceImpl;
	}

	/**
	 * Return the TaxonLabelHome field.
	 * 
	 * @return TaxonLabelHome mTaxonLabelHome
	 */
	private TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	/**
	 * Set the TaxonLabelHome field.
	 */
	public void setTaxonLabelHome(TaxonLabelHome pNewTaxonLabelHome) {
		mTaxonLabelHome = pNewTaxonLabelHome;
	}

	/**
	 * Return the MatrixDataTypeHome field.
	 * 
	 * @return MatrixDataTypeHome mMatrixDataTypeHome
	 */
	private MatrixDataTypeHome getMatrixDataTypeHome() {
		return mMatrixDataTypeHome;
	}

	/**
	 * Set the MatrixDataTypeHome field.
	 */
	public void setMatrixDataTypeHome(MatrixDataTypeHome pNewMatrixDataTypeHome) {
		mMatrixDataTypeHome = pNewMatrixDataTypeHome;
	}

}
