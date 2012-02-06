package org.cipres.treebase.domain.nexus.nexml;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.Constants;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.search.MatrixSearchResults;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.StudySearchResults;
import org.cipres.treebase.domain.search.TaxonSearchResults;
import org.cipres.treebase.domain.search.TreeSearchResults;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.nexml.model.Document;
import org.nexml.model.OTUs;
import org.nexml.model.TreeBlock;

public class NexmlDocumentWriter extends NexmlObjectConverter {

	/**
	 * 
	 * @param study
	 * @param taxonLabelHome
	 * @param document
	 */
	public NexmlDocumentWriter(Study study, TaxonLabelHome taxonLabelHome,
			Document document) {
		super(study, taxonLabelHome, document);
	}
	
	public NexmlDocumentWriter(Study study, TaxonLabelHome taxonLabelHome,
			Document document,String baseURI) {
		super(study, taxonLabelHome, document,baseURI);
	}
	
	/**
	 * 
	 * @param pNexusDataSet
	 * @return
	 */
	public Document fromTreeBaseToXml(NexusDataSet pNexusDataSet) {
		
		NexmlOTUWriter noc = new NexmlOTUWriter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( TaxonLabelSet taxonLabelSet : pNexusDataSet.getTaxonLabelSets() ) {
			noc.fromTreeBaseToXml(taxonLabelSet);
		}
		
		NexmlMatrixWriter nmc = new NexmlMatrixWriter(getStudy(),getTaxonLabelHome(),getDocument());
		for (org.cipres.treebase.domain.matrix.Matrix matrix : pNexusDataSet.getMatrices() ) {
			if ( matrix instanceof CharacterMatrix ) {
				nmc.fromTreeBaseToXml((CharacterMatrix)matrix,null);
			}
		}
		
		NexmlTreeBlockWriter ntc = new NexmlTreeBlockWriter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( org.cipres.treebase.domain.tree.TreeBlock treeBlock : pNexusDataSet.getTreeBlocks() ) {
			ntc.fromTreeBaseToXML(treeBlock);
		}
		
		return getDocument();
		
	}
	
	/**
	 * 
	 * @param pStudy
	 * @return
	 */
	public Document fromTreeBaseToXml(Study pStudy) {
		attachTreeBaseID(getDocument(), pStudy,Study.class);
		Date d = new Date();
		getDocument().addAnnotationValue("skos:historyNote", Constants.SKOSURI, "Mapped from TreeBASE schema using "+this.toString()+" $Rev$");
		getDocument().addAnnotationValue("skos:changeNote", Constants.SKOSURI, "Generated on "+d.toString());
		getDocument().setAbout("#" + pStudy.getTreebaseIDString().toString());
		
		NexmlOTUWriter noc = new NexmlOTUWriter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( TaxonLabelSet taxonLabelSet : pStudy.getTaxonLabelSets() ) {
			noc.fromTreeBaseToXml(taxonLabelSet);
		}
		
		NexmlMatrixWriter nmc = new NexmlMatrixWriter(getStudy(),getTaxonLabelHome(),getDocument());
		for (org.cipres.treebase.domain.matrix.Matrix matrix : pStudy.getMatrices() ) {
			if ( matrix instanceof CharacterMatrix ) {
				nmc.fromTreeBaseToXml((CharacterMatrix)matrix,null);
			}
		}
		
		NexmlTreeBlockWriter ntc = new NexmlTreeBlockWriter(getStudy(),getTaxonLabelHome(),getDocument());
		for ( org.cipres.treebase.domain.tree.TreeBlock treeBlock : pStudy.getTreeBlocks() ) {
			ntc.fromTreeBaseToXML(treeBlock);
		}		
		
		return getDocument();
	}
	
	public Document fromTreeBaseToXml(SearchResults<?> searchResults) {
		getDocument().addAnnotationValue("skos:historyNote", Constants.SKOSURI, "Mapped from TreeBASE schema using "+this.toString()+" $Rev$");
		SearchResultsType srt = searchResults.resultType();
		switch(srt) {
			case STUDY : return fromTreeBaseToXml((StudySearchResults) searchResults);
			case MATRIX : return fromTreeBaseToXml((MatrixSearchResults) searchResults);
			case TREE : return fromTreeBaseToXml((TreeSearchResults) searchResults);
			case TAXON : return fromTreeBaseToXml((TaxonSearchResults) searchResults);
			case NONE : ; break;
		}
		return null;
	}
	
	public Document fromTreeBaseToXml(StudySearchResults searchResults) {
		NexmlOTUWriter noc = new NexmlOTUWriter(null,getTaxonLabelHome(),getDocument());
		NexmlMatrixWriter nmc = new NexmlMatrixWriter(null,getTaxonLabelHome(),getDocument());
		NexmlTreeBlockWriter ntc = new NexmlTreeBlockWriter(null,getTaxonLabelHome(),getDocument());
		for ( Study pStudy : searchResults.getResults() ) {
			for ( TaxonLabelSet taxonLabelSet : pStudy.getTaxonLabelSets() ) {
				noc.fromTreeBaseToXml(taxonLabelSet);
			}
			for (org.cipres.treebase.domain.matrix.Matrix matrix : pStudy.getMatrices() ) {
				if ( matrix instanceof CharacterMatrix ) {
					nmc.fromTreeBaseToXml((CharacterMatrix)matrix,null);
				}
			}	
			for ( org.cipres.treebase.domain.tree.TreeBlock treeBlock : pStudy.getTreeBlocks() ) {
				ntc.fromTreeBaseToXML(treeBlock);
			}			
		}
		return getDocument();
	}
	
	public Document fromTreeBaseToXml(MatrixSearchResults searchResults) {
		
		// create a merged set of all taxon labels in all matrices
		Set<TaxonLabel> mergedLabels = new HashSet<TaxonLabel>();
		for (Matrix m : searchResults.getResults()) {
			List<TaxonLabel> labelsInTree = m.getAllTaxonLabels();
			mergedLabels.addAll(labelsInTree);
		}
		
		// convert merged set of taxon labels to XML OTUs		
		NexmlOTUWriter noc = new NexmlOTUWriter(null,getTaxonLabelHome(),getDocument());
		OTUs xmlOTUs = noc.fromTreeBaseToXml(mergedLabels);
		
		// convert matrices to XML trees
		NexmlMatrixWriter nmc = new NexmlMatrixWriter(null,getTaxonLabelHome(),getDocument());
		for (Matrix m : searchResults.getResults()) {
			nmc.fromTreeBaseToXml((CharacterMatrix)m,xmlOTUs);
		}
		
		return getDocument();
	}	

	public Document fromTreeBaseToXml(TreeSearchResults searchResults) {		
				
		// create a merged set of all taxon labels in all trees
		Set<TaxonLabel> mergedLabels = new HashSet<TaxonLabel>();
		for (PhyloTree t : searchResults.getResults()) {
			Set<TaxonLabel> labelsInTree = t.getAllTaxonLabels();
			mergedLabels.addAll(labelsInTree);
		}
		
		// convert merged set of taxon labels to XML OTUs		
		NexmlOTUWriter noc = new NexmlOTUWriter(null,getTaxonLabelHome(),getDocument());
		OTUs xmlOTUs = noc.fromTreeBaseToXml(mergedLabels);
		
		// convert phylotrees to XML trees
		NexmlTreeBlockWriter ntc = new NexmlTreeBlockWriter(null,getTaxonLabelHome(),getDocument());
		TreeBlock xmlTreeBlock = getDocument().createTreeBlock(xmlOTUs);
		for (PhyloTree t : searchResults.getResults()) {
			ntc.fromTreeBaseToXml(t, xmlTreeBlock);
		}		
		return getDocument();
	}	
	
	public Document fromTreeBaseToXml(TaxonSearchResults searchResults) {
		NexmlOTUWriter noc = new NexmlOTUWriter(null,getTaxonLabelHome(),getDocument());
		OTUs xmlOTUs = getDocument().createOTUs();
		for (Taxon t : searchResults.getResults()) {
			noc.fromTreeBaseToXml(t, xmlOTUs);
		}
		return getDocument();
	}	
	
}
