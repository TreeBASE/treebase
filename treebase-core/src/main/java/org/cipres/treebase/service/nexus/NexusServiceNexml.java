package org.cipres.treebase.service.nexus;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.NexusService;
import org.cipres.treebase.domain.nexus.nexml.NexmlConverter;
import org.cipres.treebase.domain.nexus.nexml.NexmlDocumentWriter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.event.ProgressionListener;
import org.cipres.treebase.service.AbstractServiceImpl;
import org.nexml.model.Document;
import org.nexml.model.DocumentFactory;

public class NexusServiceNexml extends AbstractServiceImpl implements NexusService {
	private static final Logger LOGGER = Logger.getLogger(NexusServiceNexml.class);
	private TaxonLabelHome mTaxonLabelHome;

	@Override
	public Class<?> defaultResultClass() {
		return null;
	}

	@Override
	protected DomainHome getDomainHome() {
		return null; // do not need persistence service.
	}

	public NexusDataSet parseNexus(Study study, Collection<File> nexusFiles,
			ProgressionListener listener) {
		NexusDataSet data = new NexusDataSet();
		NexmlConverter converter = new NexmlConverter();
		for ( File nexusFile : nexusFiles ) {
			converter.parseOneFile(nexusFile, study, data);
		}
		return data;
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

	public String serialize(NexusDataSet nexusDataSet) {
		return serialize(nexusDataSet,null);
	}

	public String serialize(Study study) {
		return serialize(study,null);
	}

	public TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	public void setTaxonLabelHome(TaxonLabelHome taxonLabelHome) {
		mTaxonLabelHome = taxonLabelHome;
	}
	
	protected NexmlDocumentWriter getNexmlDocumentConverter(Study study,Properties properties) {
		String baseURI = null;
		if ( null != properties ) {
			baseURI = properties.getProperty("nexml.uri.base");
		}
		Document document = null;
		try {
			document = DocumentFactory.createDocument();
			document.setBaseURI(new URI(baseURI));//NPE
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		NexmlDocumentWriter ndc = new NexmlDocumentWriter(study,getTaxonLabelHome(),document,baseURI);
		return ndc;
	}

	public String serialize(NexusDataSet nexusDataSet, Properties properties) {
		NexmlDocumentWriter ndc = getNexmlDocumentConverter(null, properties);
		return ndc.fromTreeBaseToXml(nexusDataSet).getXmlString();
	}
	
	public String serialize(NexusDataSet nexusDataSet, Properties properties,Study study) {
		NexmlDocumentWriter ndc = getNexmlDocumentConverter(study, properties);
		return ndc.fromTreeBaseToXml(nexusDataSet).getXmlString();
	}	

	public String serialize(Study study, Properties properties) {
		NexmlDocumentWriter ndc = getNexmlDocumentConverter(study, properties);
		return ndc.fromTreeBaseToXml(study).getXmlString();
	}

}
