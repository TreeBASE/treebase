package org.cipres.treebase.domain.nexus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.nexus.nexml.NexmlDocumentWriter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.nexml.model.Document;
import org.nexml.model.DocumentFactory;
import org.nexml.model.Network;
import org.nexml.model.Node;
import org.nexml.model.OTU;
import static org.junit.Assert.*;

public class NexmlTreeConverterTest extends AbstractDAOTest {
	private TaxonLabelHome mTaxonLabelHome;
	
	public void testNexmlTreeConverter() {
		String testName = "testNexmlTreeConverter";
		//signal beginning of test
		if (logger.isInfoEnabled()) {
			logger.info("Running Test: " + testName);
		}
		
		// this study had mismatching taxon labels, according to @rdmpage
		long studyId = 2048;  
		
		// this is the full study as it is stored by the database
		Study tbStudy = (Study)loadObject(Study.class, studyId);

		if (tbStudy != null) {
			// this becomes an object representation of a NeXML document
			Document nexDoc = DocumentFactory.safeCreateDocument();
			
			// the converter populates the NeXML document with the contents of the treebase study
			NexmlDocumentWriter ndc = new NexmlDocumentWriter(tbStudy,getTaxonLabelHome(),nexDoc);
			ndc.fromTreeBaseToXml(tbStudy); // here is where the conversion happens
					
			// these are the NeXML tree blocks that were created from the study  		
			List<org.nexml.model.TreeBlock> nexTreeBlocks = nexDoc.getTreeBlockList();
			
			// there most be more than zero tree blocks in this study
			Assert.assertTrue(nexTreeBlocks.size() != 0 );
			
			// now we're going to match up the NeXML taxa in trees with their equivalent treebase ones
			for ( org.nexml.model.TreeBlock nexTreeBlock : nexTreeBlocks ) {
				
				// get the equivalent taxa block in treebase for the NeXML tree block's OTU set
				TaxonLabelSet tbTls = (TaxonLabelSet)findEquivalentObject(nexTreeBlock.getOTUs().getId(),"Tls",tbStudy.getTaxonLabelSets());
				
				// now iterate over all nodes in all trees in the focal tree block
				for ( Network<?> nexTree : nexTreeBlock ) {
					for ( Node nexNode : nexTree.getNodes() ) {
						
						// check to see if there is a taxon; this may be null because we also visit internal nodes
						OTU nexOTU = nexNode.getOTU();
						if ( null != nexOTU ) {
							
							// populate a set to pass into findEquivalentObject()
							Set<TaxonLabel> tbTlset = new HashSet<TaxonLabel>(); 
							tbTlset.addAll(tbTls.getTaxonLabelsReadOnly());
							
							// this must not be null, though
							TaxonLabel tbTl = (TaxonLabel)findEquivalentObject(nexOTU.getId(),"Tl",tbTlset);
							Assert.assertNotNull("Have to find taxon "+nexNode.getId()+" for node "+nexNode.getId(), tbTl);
						}
					}
				}		
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(testName + " - empty database, test skipped");
			}
		}
	}
	
	/**
	 * 
	 * @param id - a NeXML id attribute's value
	 * @param prefix - a prefix as defined by TreebaseIDString 
	 * @param objects - a set of TBPersistable objects, one of which has a matching id
	 * @return the TBPersistable object whose prefix + primary key == id
	 */
	private TBPersistable findEquivalentObject(String id,String prefix,Set<?> objects) {
		TBPersistable result = null;
		for ( Object oObject : objects ) {
			TBPersistable object = (TBPersistable)oObject; 
			if ( id.equals(prefix + object.getId()) ) {
				result = object;
				break;
			}
		}
		Assert.assertNotNull("Found object with id "+id, result);
		return result;
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
