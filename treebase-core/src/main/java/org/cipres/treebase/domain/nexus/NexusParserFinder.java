
package org.cipres.treebase.domain.nexus;

import org.springframework.beans.factory.FactoryBean;

import org.cipres.treebase.domain.matrix.ItemDefinitionHome;
import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.service.nexus.NexusServiceMesquite;
//import org.cipres.treebase.service.nexus.NexusServiceNCL;

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
			//if (PARSER_NCL.compareToIgnoreCase(getNexusParserStr()) == 0) {
				//NexusServiceNCL serviceNCL = new NexusServiceNCL();
				
				//serviceNCL.setMatrixDataTypeHome(getMatrixDataTypeHome());
				//serviceNCL.setTaxonLabelHome(getTaxonLabelHome());
				
				//mNexusServiceImpl = serviceNCL;
			//} else {
				NexusServiceMesquite serviceMesquite = new NexusServiceMesquite();
				
				serviceMesquite.setItemDefinitionHome(getItemDefinitionHome());
				serviceMesquite.setMatrixDataTypeHome(getMatrixDataTypeHome());
				serviceMesquite.setTaxonLabelHome(getTaxonLabelHome());
				
				mNexusServiceImpl = serviceMesquite;
				
			//}
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
