package org.cipres.treebase.domain.nexus.nexml;

import org.cipres.treebase.Constants;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.nexml.model.Document;
import org.nexml.model.OTU;
import org.nexml.model.OTUs;

public class NexmlOTUWriter extends NexmlObjectConverter {
	
	/**
	 * 
	 * @param study
	 * @param taxonLabelHome
	 */
	public NexmlOTUWriter(Study study,TaxonLabelHome taxonLabelHome,Document document) {
		super(study,taxonLabelHome,document);	
	}
		
	/**
	 * 
	 * @param taxonLabelSet
	 * @return
	 */
	public OTUs fromTreeBaseToXml(TaxonLabelSet taxonLabelSet) {
		OTUs xmlOTUs = getDocument().createOTUs();
		
		// attach base uri and skos:historyNote
		xmlOTUs.setBaseURI(mTaxonBaseURI);
		xmlOTUs.addAnnotationValue("skos:historyNote", Constants.SKOSURI, "Mapped from TreeBASE schema using "+this.toString()+" $Rev$");
		
		xmlOTUs.setLabel(taxonLabelSet.getTitle());
		attachTreeBaseID(xmlOTUs,taxonLabelSet,TaxonLabelSet.class);
		for ( TaxonLabel taxonLabel : taxonLabelSet.getTaxonLabelsReadOnly() ) {
			fromTreeBaseToXml(taxonLabel,xmlOTUs);
		}
		return xmlOTUs;
	}
	
	/**
	 * 
	 * @param taxonLabel
	 * @param xmlOTUs
	 * @return
	 */
	private OTU fromTreeBaseToXml(TaxonLabel taxonLabel,OTUs xmlOTUs) {
		OTU xmlOTU = xmlOTUs.createOTU();
		if ( null != taxonLabel.getTaxonLabel() ) {
			xmlOTU.setLabel(taxonLabel.getTaxonLabel());
		}
		attachTreeBaseID(xmlOTU,taxonLabel,TaxonLabel.class);
		return xmlOTU;
	}

}
