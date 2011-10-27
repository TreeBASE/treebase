package org.cipres.treebase.domain.nexus;

import java.util.Collection;
import java.util.HashSet;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.search.MatrixSearchResults;
import org.cipres.treebase.domain.search.StudySearchResults;
import org.cipres.treebase.domain.search.TaxonSearchResults;
import org.cipres.treebase.domain.search.TreeSearchResults;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.nexus.nexml.NexmlDocumentWriter;
import org.nexml.model.Document;
import org.nexml.model.DocumentFactory;

public class NexmlSearchResultConverterTest extends AbstractDAOTest {

	private TaxonLabelHome mTaxonLabelHome;
	private StudyHome mStudyHome;
	
	private StudySearchResults ssr = null;
	
	private Collection<Study> studies = null;
	private Collection<Matrix> matrices = null;
	private Collection<PhyloTree> trees = null;
	private Collection<TaxonLabel> taxa = null;
		
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();

		studies = getTestData();
		ssr = new StudySearchResults(studies);
		
		// populate set of matrices from test data
		matrices = new HashSet<Matrix> ();
		for (Study s : studies) {
			matrices.addAll(s.getMatrices());
		}
		
		// populate set of trees from test data
		trees = new HashSet<PhyloTree> ();
		for (Study s : studies) {
			trees.addAll(s.getTrees());
		}
				
		// populate set of taxa from test data
		taxa = new HashSet<TaxonLabel>();
		for ( Study s : studies) {
			taxa.addAll(s.getTaxonLabels());
		}				
	}

	public NexmlSearchResultConverterTest() {
		super();
	}
	
	public void testTrivial() {
		assertNotNull(getTaxonLabelHome());
		assertNotNull(getStudyHome());
	}
	
	public void testQuickCheck() { 
		assertFalse(studies.size() == 0);
		assertFalse(matrices.size() == 0);
		assertFalse(trees.size() == 0);
		assertFalse(taxa.size() == 0);
	}	
	
	private Collection<Study> getTestData(String accessionNumber) {
		Collection<Study> studies = new HashSet<Study>();
		studies.add(getStudyHome().findByAccessionNumber(accessionNumber));
		assertFalse(studies.size() == 0);	
		return studies;
	}
	
	private Collection<Study> getTestData() {
		return getTestData("S1787");
	}
	
	public void testStudySearchSerialization() {
		Document doc = DocumentFactory.safeCreateDocument();
		NexmlDocumentWriter ndw = new NexmlDocumentWriter(null, mTaxonLabelHome, doc);
		ndw.fromTreeBaseToXml(ssr);
		assertNotNull(doc.getXmlString());
	}
	
	public void testTaxonSearchSerialization() {
		Document doc = DocumentFactory.safeCreateDocument();
		NexmlDocumentWriter ndw = new NexmlDocumentWriter(null, mTaxonLabelHome, doc);		
		TaxonSearchResults tasr = ssr.convertToTaxa();
		ndw.fromTreeBaseToXml(tasr);
		assertNotNull(doc.getXmlString());
	}
	
	public void testMatrixSearchSerialization() {
		Document doc = DocumentFactory.safeCreateDocument();
		NexmlDocumentWriter ndw = new NexmlDocumentWriter(null, mTaxonLabelHome, doc);		
		MatrixSearchResults msr = ssr.convertToMatrices();
		ndw.fromTreeBaseToXml(msr);
		assertNotNull(doc.getXmlString());
	}
	
	public void testTreeSearchSerialization() {
		Document doc = DocumentFactory.safeCreateDocument();
		NexmlDocumentWriter ndw = new NexmlDocumentWriter(null, mTaxonLabelHome, doc);		
		TreeSearchResults tsr = ssr.convertToTrees();
		ndw.fromTreeBaseToXml(tsr);
		assertNotNull(doc.getXmlString());
	}
	
	public TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	public void setTaxonLabelHome(TaxonLabelHome pNewTaxonLabelHome) {
		mTaxonLabelHome = pNewTaxonLabelHome;
	}

	public StudyHome getStudyHome() {
		return mStudyHome;
	}

	public void setStudyHome(StudyHome studyHome) {
		mStudyHome = studyHome;
	}
	
}	