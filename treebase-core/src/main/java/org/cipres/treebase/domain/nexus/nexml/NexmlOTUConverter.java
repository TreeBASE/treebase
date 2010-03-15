package org.cipres.treebase.domain.nexus.nexml;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.nexml.model.Document;
import org.nexml.model.OTU;
import org.nexml.model.OTUs;

public class NexmlOTUConverter extends NexmlObjectConverter {
	
	/**
	 * 
	 * @param study
	 * @param taxonLabelHome
	 */
	public NexmlOTUConverter(Study study,TaxonLabelHome taxonLabelHome,Document document) {
		super(study,taxonLabelHome,document);	
	}
	
	/**
	 * 
	 * @param xmlOTUs
	 * @return
	 */
	public TaxonLabelSet fromXmlToTreeBase (OTUs xmlOTUs) {
		TaxonLabelSet labelSet = new TaxonLabelSet();
		attachTreeBaseID(xmlOTUs,labelSet,TaxonLabelSet.class);
		labelSet.setStudy(getStudy());
		if ( null != xmlOTUs.getLabel() ) {
			labelSet.setTitle(xmlOTUs.getLabel());
		}
		labelSet.setTaxa(true);
		for ( OTU xmlOTU : xmlOTUs.getAllOTUs() ) {
			TaxonLabel taxonLabel = fromXmlToTreeBase(xmlOTU);
			labelSet.addPhyloTaxonLabel(taxonLabel);
		}	
		return labelSet;
	}
	
	/**
	 * 
	 * @param taxonLabelSet
	 * @return
	 */
	public OTUs fromTreeBaseToXml(TaxonLabelSet taxonLabelSet) {
		OTUs xmlOTUs = getDocument().createOTUs();
		xmlOTUs.setLabel(taxonLabelSet.getTitle());
		attachTreeBaseID(xmlOTUs,taxonLabelSet,TaxonLabelSet.class);
		for ( TaxonLabel taxonLabel : taxonLabelSet.getTaxonLabelsReadOnly() ) {
			fromTreeBaseToXml(taxonLabel,xmlOTUs);
		}
		return xmlOTUs;
	}
	
	/**
	 * 
	 * @param xmlOTU
	 * @return
	 */
	public TaxonLabel fromXmlToTreeBase(OTU xmlOTU) {
		TaxonLabel taxonLabel = getTaxonLabelHome().getByDescriptionAndStudy(xmlOTU.getLabel(), getStudy());
		taxonLabel.setStudy(getStudy());
		attachTreeBaseID(xmlOTU,taxonLabel,TaxonLabel.class);
		return taxonLabel;		
	}
	
	/**
	 * 
	 * @param taxonLabel
	 * @param xmlOTUs
	 * @return
	 */
	public OTU fromTreeBaseToXml(TaxonLabel taxonLabel,OTUs xmlOTUs) {
		OTU xmlOTU = xmlOTUs.createOTU();
		if ( null != taxonLabel.getTaxonLabel() ) {
			xmlOTU.setLabel(taxonLabel.getTaxonLabel());
		}
		attachTreeBaseID(xmlOTU,taxonLabel,TaxonLabel.class);
		return xmlOTU;
	}

}
