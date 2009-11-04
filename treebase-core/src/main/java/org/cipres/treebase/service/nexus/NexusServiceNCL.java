
package org.cipres.treebase.service.nexus;

import java.io.File;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.NexusService;
import org.cipres.treebase.domain.nexus.ncl.NCLNexusConverter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.event.ProgressionListener;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * NexusServiceNCL.java
 * 
 * Created on Apr 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class NexusServiceNCL extends AbstractServiceImpl implements NexusService {
	private static final Logger LOGGER = Logger.getLogger(NexusServiceNCL.class);

	private TaxonLabelHome mTaxonLabelHome;
	private MatrixDataTypeHome mMatrixDataTypeHome;

	/**
	 * Constructor.
	 */
	public NexusServiceNCL() {
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
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return null; // do not need persistence service.
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
	 * @see org.cipres.treebase.domain.nexus.NexusService#parseNexus(org.cipres.treebase.domain.study.Study, java.util.Collection, org.cipres.treebase.event.ProgressionListener)
	 */
	public NexusDataSet parseNexus(Study pStudy, Collection<File> pNexusFiles, ProgressionListener pListener) {
		
		if (pStudy == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER
					.info("parseNexus - Study is null"); //$NON-NLS-1$
			}
			return null;
		}

		if (!checkFiles(pNexusFiles)) {
			return null;
		}

		NexusDataSet data = new NexusDataSet();
		
		NCLNexusConverter converter = new NCLNexusConverter();
		converter.setMatrixDataTypeHome(getMatrixDataTypeHome());
		converter.setTaxonLabelHome(getTaxonLabelHome());
		
		converter.processLoadFile(pNexusFiles, pStudy, data, pListener);
		
		return data;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.nexus.NexusService#parseNexus(org.cipres.treebase.domain.study.Study, java.io.File)
	 */
	public NexusDataSet parseNexus(Study pStudy, File pNexusFile) {
		//TODO: parseNexus
		return null;
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
