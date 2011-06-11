package org.cipres.treebase.domain.nexus;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.cipres.treebase.Constants;
import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.nexus.nexml.NexmlDocumentConverter;
import org.cipres.treebase.domain.study.Algorithm;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.Software;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.nexml.model.Annotatable;
import org.nexml.model.Annotation;
import org.nexml.model.Document;
import org.nexml.model.DocumentFactory;
import org.nexml.model.Matrix;
import org.nexml.model.Network;
import org.nexml.model.TreeBlock;

public class NexmlAnalysisConverterTest extends AbstractDAOTest {
	private TaxonLabelHome mTaxonLabelHome;

	/**
	 * Test  for {@link org.cipres.treebase.domain.nexus.nexml.NexmlMatrixConverter#fromTreeBaseToXml(CharacterMatrix)}.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	public void testNexmlAnalysisConverter() throws URISyntaxException, IOException {
		String testName = "testNexmlAnalysisConverter";
		//signal beginning of test
		if (logger.isInfoEnabled()) {
			logger.info("Running Test: " + testName);
		}
		Map<String,AnalyzedData> analyzedDataForData = new HashMap<String,AnalyzedData>();
		long studyId = 794; // this study seems to have character sets
		
		// this is the full study as it is stored by the database
		Study tbStudy = (Study)loadObject(Study.class, studyId);
		
		// this is an object representation of a NeXML document
		Document nexDoc = DocumentFactory.safeCreateDocument();
		
		// the converter populates the NeXML document with the contents of the treebase study
		NexmlDocumentConverter ndc = new NexmlDocumentConverter(tbStudy,getTaxonLabelHome(),nexDoc);
		ndc.fromTreeBaseToXml(tbStudy); // here is where the conversion happens		
		
		attachAnalysisMetadata(analyzedDataForData, tbStudy, nexDoc);		
		
		// attach analysis step metadata to matrices
		for ( Matrix<?> nexMatrix : nexDoc.getMatrices() ) {
			attachAnalyzedDataMetadata(analyzedDataForData, nexMatrix);
		}
		
		// attach analysis step metadata to trees
		for ( TreeBlock nexTrees : nexDoc.getTreeBlockList() ) {
			int count = nexTrees.getSegmentCount();			
			for ( int i = 0; i < count; i++ ) {
				Network<?> nexTree = nexTrees.getSegment(i);
				attachAnalyzedDataMetadata(analyzedDataForData, nexTree);				
			}
		}
		
		FileWriter fstream = new FileWriter("/Users/rvosa/Desktop/outfile.xml");
		BufferedWriter out = new BufferedWriter(fstream);	
		out.write(nexDoc.getXmlString());
		out.close();
	}

	/**
	 * 
	 * @param analyzedDataForData
	 * @param nexAnnotatable
	 * @throws URISyntaxException 
	 */
	private void attachAnalyzedDataMetadata(Map<String, AnalyzedData> analyzedDataForData, Annotatable nexAnnotatable) throws URISyntaxException {
		AnalyzedData tbData = analyzedDataForData.get(nexAnnotatable.getId());
		URI tbStepPurl = tbData.getAnalysisStep().getPhyloWSPath().getPurl().toURI();
		String predicate = tbData.isInputData() ? "tb:input.analysistep" : "tb:output.analysisstep";
		nexAnnotatable.addAnnotationValue(predicate, Constants.TBTermsURI, tbStepPurl);
		logger.info("attached analyzed data metadata");
	}

	/**
	 * 
	 * @param analyzedDataForData
	 * @param tbStudy
	 * @param nexDoc
	 * @throws URISyntaxException 
	 */
	private void attachAnalysisMetadata(Map<String, AnalyzedData> analyzedDataForData, Study tbStudy, Document nexDoc) throws URISyntaxException {
		for ( Analysis tbAnalysis : tbStudy.getAnalyses() ) {
			URI tbAnalysisPurl = URI.create(tbAnalysis.getPhyloWSPath().getPurl().toString());
			Annotation nexAnalysis = nexDoc.addAnnotationValue("tb:identifier.analysis", Constants.TBTermsURI, tbAnalysisPurl);
			logger.info("attached analysis identifier " + tbAnalysisPurl);
			
			for ( AnalysisStep tbAnalysisStep : tbAnalysis.getAnalysisStepsReadOnly() ) {
				URI tbAnalysisStepPurl = URI.create(tbAnalysisStep.getPhyloWSPath().getPurl().toString());
				Annotation nexAnalysisStep = nexAnalysis.addAnnotationValue("tb:identifier.analysisstep", Constants.TBTermsURI, tbAnalysisStepPurl);
				logger.info("attached analysis step identifier " + tbAnalysisStepPurl);
				
				attachAnalysisStepMetadata(tbAnalysisStep, nexAnalysisStep);				
				for ( AnalyzedData tbAnalyzedData : tbAnalysisStep.getDataSetReadOnly() ) {
					String type = tbAnalyzedData.getDataType();
					logger.info("analyzed data is of type "+type);
					if ( "tree".equals(type) ) {
						PhyloTree tbTree = tbAnalyzedData.getTreeData();
						analyzedDataForData.put(tbTree.getTreebaseIDString().toString(), tbAnalyzedData);
					}
					else if ( "matrix".equals(type) ) {
						org.cipres.treebase.domain.matrix.Matrix tbMatrix = tbAnalyzedData.getMatrixData();
						analyzedDataForData.put(tbMatrix.getTreebaseIDString().toString(), tbAnalyzedData);
					}
					else {
						logger.warn("unexpected data type encountered: "+type);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param tbAnalysisStep
	 * @param nexAnalysisStep
	 * @throws URISyntaxException 
	 */
	private void attachAnalysisStepMetadata(AnalysisStep tbAnalysisStep,
			Annotation nexAnalysisStep) throws URISyntaxException {
		// attach analysis step name
		if ( null != tbAnalysisStep.getName() ) {
			nexAnalysisStep.addAnnotationValue("dc:title", Constants.DCURI, tbAnalysisStep.getName());
			logger.info("attached analysis step name");
		}		
		
		// attach analysis step commands
		if ( null != tbAnalysisStep.getCommands() ) {
			nexAnalysisStep.addAnnotationValue("tb:analysisstep.commands", Constants.TBTermsURI, tbAnalysisStep.getCommands());
			logger.info("attached analysis step commands");
		}		
		
		// process software metadata
		Software tbSoftware = tbAnalysisStep.getSoftwareInfo();
		attachSoftwareMetadata(nexAnalysisStep, tbSoftware);
		
		// process algorithm metadata
		Algorithm tbAlgorithm = tbAnalysisStep.getAlgorithmInfo();
		attachAlgorithmMetadata(nexAnalysisStep, tbAlgorithm);
	}

	/**
	 * 
	 * @param nexAnalysisStep
	 * @param tbAlgorithm
	 * @throws URISyntaxException 
	 */
	private void attachAlgorithmMetadata(Annotation nexAnalysisStep,
			Algorithm tbAlgorithm) throws URISyntaxException {
		URI tbAlgorithmPurl = tbAlgorithm.getPhyloWSPath().getPurl().toURI();
		Annotation nexAlgorithm = nexAnalysisStep.addAnnotationValue("tb:identifier.algorithm", Constants.TBTermsURI, tbAlgorithmPurl);
		logger.info("attached algorithm url "+tbAlgorithmPurl);
		
		// attach algorithm type
		if ( null != tbAlgorithm.getAlgorithmType() ) {
			nexAlgorithm.addAnnotationValue("dc:type", Constants.DCURI, tbAlgorithm.getAlgorithmType());
			logger.info("attached algorithm type");
		}
		
		// attach algorithm description
		if ( null != tbAlgorithm.getDescription() ) {
			nexAlgorithm.addAnnotationValue("dc:description", Constants.DCURI, tbAlgorithm.getDescription());
			logger.info("attached algorithm description");
		}
	}

	/**
	 * 
	 * @param nexAnalysisStep
	 * @param tbSoftware
	 * @throws URISyntaxException 
	 */
	private void attachSoftwareMetadata(Annotation nexAnalysisStep, Software tbSoftware) throws URISyntaxException {
		URI tbSoftwarePurl = tbSoftware.getPhyloWSPath().getPurl().toURI();
		Annotation nexSoftware = nexAnalysisStep.addAnnotationValue("tb:identifier.software", Constants.TBTermsURI, tbSoftwarePurl);
		logger.info("attached software identifier "+tbSoftwarePurl);
		
		// attach software name
		if ( null != tbSoftware.getName() ) {
			nexSoftware.addAnnotationValue("dc:title", Constants.DCURI, tbSoftware.getName());
			logger.info("attached software title");
		}
		
		// attach software version
		if ( null != tbSoftware.getSoftwareVersion() ) {
			nexSoftware.addAnnotationValue("dc:identifier", Constants.DCURI, tbSoftware.getSoftwareVersion());
			logger.info("attached software version");
		}
		
		// attach software url
		if ( null != tbSoftware.getSoftwareURL() ) {
			URI tbSoftwareURL = URI.create(tbSoftware.getSoftwareURL());
			nexSoftware.addAnnotationValue("dc:source", Constants.DCURI, tbSoftwareURL);
			logger.info("attached software url");
		}
	}
	
	/**
	 * Return the TaxonLabelHome field.
	 * 
	 * @return TaxonLabelHome mTaxonLabelHome
	 */
	public TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	/**
	 * Set the TaxonLabelHome field.
	 */
	public void setTaxonLabelHome(TaxonLabelHome pNewTaxonLabelHome) {
		mTaxonLabelHome = pNewTaxonLabelHome;
	}	

}
