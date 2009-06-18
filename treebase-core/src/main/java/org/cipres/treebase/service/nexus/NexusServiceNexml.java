package org.cipres.treebase.service.nexus;

import java.io.File;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.NexusService;
import org.cipres.treebase.domain.nexus.nexml.NexmlConverter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.event.ProgressionListener;
import org.cipres.treebase.service.AbstractServiceImpl;

public class NexusServiceNexml extends AbstractServiceImpl implements NexusService {
	private static final Logger LOGGER = Logger.getLogger(NexusServiceNexml.class);
	private DomainHome mDomainHome;

	@Override
	public Class defaultResultClass() {
		return null;
	}

	@Override
	protected DomainHome getDomainHome() {
		return mDomainHome;
	}
	
	public void setDomainHome(DomainHome domainHome) {
		mDomainHome = domainHome;
	}

	public NexusDataSet parseNexus(Study study, Collection<File> nexusFiles,
			ProgressionListener listener) {
		// TODO Auto-generated method stub
		return null;
	}

	public NexusDataSet parseNexus(Study study, File nexusFile) {
		if (study == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("parseNexus - Study is null"); //$NON-NLS-1$
			}
			return null;
		}		
		NexusDataSet data = new NexusDataSet();
		NexmlConverter converter = new NexmlConverter();
		converter.parseOneFile(nexusFile, study, data);
		return data;
	}

}
