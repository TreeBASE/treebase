
package org.cipres.treebase.service.nexus;

import java.io.File;
import java.util.Collection;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import mesquite.lib.MesquiteModule;

import org.apache.log4j.Logger;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.ItemDefinitionHome;
import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.NexusService;
import org.cipres.treebase.domain.nexus.mesquite.MesquiteConverter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.event.ProgressionListener;
import org.cipres.treebase.service.AbstractServiceImpl;
import org.springframework.beans.factory.InitializingBean;

/**
 * NexusServiceMesquite.java
 * 
 * Created on Apr 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class NexusServiceMesquite extends AbstractServiceImpl implements NexusService, InitializingBean {
	private static final Logger LOGGER = Logger.getLogger(NexusServiceMesquite.class);
	private static final String MESQUITE_FOLDER_DIR_KEY = "mesquite.folder_dir";

	private TaxonLabelHome mTaxonLabelHome;
	private MatrixDataTypeHome mMatrixDataTypeHome;
	private ItemDefinitionHome mItemDefinitionHome;

	/**
	 * Constructor.
	 */
	public NexusServiceMesquite() {
		super();
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
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return null; // do not need persistence service.
	}
	
	/** Looks up where Mesquite is installed on the host system and informs MesquiteModule of the location.  
        (This is an implementation of a standard Spring bean initialization method, 
         which is invoked after all properties are set.)
   */
	public void afterPropertiesSet() throws Exception {
		String mesquiteFolderDir = null; 
		InitialContext ic;
		try {
			ic = new InitialContext();
			mesquiteFolderDir  = (String) ic.lookup("java:comp/env/tb2/MesquiteFolder");
		} catch (NamingException e) {
			LOGGER.fatal("Error looking up tb/MesquiteFolder via JNDI"); 
		}
		mesquiteFolderDir = mesquiteFolderDir + "/foo";   //since the last path element is somehow dropped subsequently... VG 2010-02-07

		System.setProperty(MESQUITE_FOLDER_DIR_KEY, mesquiteFolderDir);
		MesquiteModule.mesquiteDirectory = new File(mesquiteFolderDir);
		MesquiteModule.mesquiteDirectoryPath = mesquiteFolderDir;				
	}
	

	/** 
	 * 
	 * @see org.cipres.treebase.domain.nexus.NexusService#parseNexus(org.cipres.treebase.domain.study.Study, java.io.File)
	 */
	public NexusDataSet parseNexus(Study pStudy, File pNexusFile) {
		if (pStudy == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("parseNexus - Study is null"); //$NON-NLS-1$
			}
			return null;
		}

		NexusDataSet data = new NexusDataSet();

		MesquiteConverter converter = new MesquiteConverter();
		converter.setMatrixDataTypeHome(getMatrixDataTypeHome());
		converter.setItemDefinitionHome(getItemDefinitionHome());
		converter.setTaxonLabelHome(getTaxonLabelHome());

		//converter.processLoadFile(pNexusFiles, pStudy, data, pListener);
		if ( LOGGER.isInfoEnabled() ) {
			LOGGER.info("Mesquite is located at: '"+System.getProperty(MESQUITE_FOLDER_DIR_KEY)+"'");
		}
		
		converter.parseOneFile(pNexusFile, pStudy, data);

		return data;	
	}
	
	/**
	 * Returns true only if all files exist.
	 * 
	 * @param pNexusFiles
	 * @return
	 */
	private boolean checkFiles(Collection<File> pNexusFiles) {
		if (pNexusFiles == null) {
			return false;
		}

		boolean hasError = false;
		for (File file : pNexusFiles) {
			if (!file.exists()) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info(" input file does not exist:" + file.getAbsolutePath()); //$NON-NLS-1$
				}
				hasError = true;
			}
		}

		return !hasError;
	}
	
	/**
	 * 
	 * @see org.cipres.treebase.domain.nexus.NexusService#parseNexus(org.cipres.treebase.domain.study.Study,
	 *      java.util.Collection, org.cipres.treebase.event.ProgressionListener)
	 */
	public NexusDataSet parseNexus(
		Study pStudy,
		Collection<File> pNexusFiles,
		ProgressionListener pListener) {

		if (pStudy == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("parseNexus - Study is null"); //$NON-NLS-1$
			}
			return null;
		}

		if (!checkFiles(pNexusFiles)) {
			return null;
		}

		NexusDataSet data = new NexusDataSet();

		MesquiteConverter converter = new MesquiteConverter();
		converter.setMatrixDataTypeHome(getMatrixDataTypeHome());
		converter.setItemDefinitionHome(getItemDefinitionHome());
		converter.setTaxonLabelHome(getTaxonLabelHome());

		converter.processLoadFile(pNexusFiles, pStudy, data, pListener);

		return data;
	}
	
	@Override
	public Class defaultResultClass() {
		return null;
	}

	public String serialize(NexusDataSet nexusDataSet) {
		// TODO Auto-generated method stub
		return null;
	}

	public String serialize(Study study) {
		// TODO Auto-generated method stub
		return null;
	}

	public String serialize(NexusDataSet nexusDataSet, Properties properties) {
		// TODO Auto-generated method stub
		return null;
	}

	public String serialize(Study study, Properties properties) {
		// TODO Auto-generated method stub
		return null;
	}

}
