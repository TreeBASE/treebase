package org.cipres.treebase.domain.nexus;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.CharSet;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.ColumnRange;
import org.cipres.treebase.domain.nexus.nexml.NexmlDocumentConverter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.nexml.model.CategoricalMatrix;
import org.nexml.model.ContinuousMatrix;
import org.nexml.model.DocumentFactory;
import org.nexml.model.Document;
import org.nexml.model.Matrix;
import org.nexml.model.MolecularMatrix;
import org.nexml.model.Subset;

public class NexmlMatrixConverterTest extends AbstractDAOTest {
	private TaxonLabelHome mTaxonLabelHome;

	/**
	 * Test  for {@link org.cipres.treebase.domain.nexus.nexml.NexmlMatrixConverter#fromTreeBaseToXml(CharacterMatrix)}.
	 * Finds an equivalent, created NexmlMatrix within a NeXML document to go with the matrix
	 * fetched from the TreeBASE database.
	 */
	public void testNexmlMatrixConverter() {
		String testName = "testNexmlMatrixConverter";
		//signal beginning of test
		if (logger.isInfoEnabled()) {
			logger.info("Running Test: " + testName);
		}
		
		long studyId = 794; // this study seems to have character sets
		
		// this is the full study as it is stored by the database
		Study tbStudy = (Study)loadObject(Study.class, studyId);

		// these are the character state matrices that are part of the study
		Set<org.cipres.treebase.domain.matrix.Matrix> tbMatrices = tbStudy.getMatrices();

		// this is an object representation of a NeXML document
		Document nexDoc = DocumentFactory.safeCreateDocument();
		
		// the converter populates the NeXML document with the contents of the treebase study
		NexmlDocumentConverter ndc = new NexmlDocumentConverter(tbStudy,getTaxonLabelHome(),nexDoc);
		ndc.fromTreeBaseToXml(tbStudy); // here is where the conversion happens
		
		// these are the NeXML matrices that were created from the study  		
		List<Matrix<?>> nexMatrices = nexDoc.getMatrices();
		
		// there most be more than zero matrices because every treebase study has at least one matrix
		Assert.assertTrue(nexMatrices.size() != 0 );
		
		// now we're going to match up the NeXML matrices with their equivalent treebase ones
		for ( Matrix<?> nexMatrix : nexMatrices ) {
			
			// the xml id is the same as the primary key of the equivalent matrix stored by treebase
			String nexId = nexMatrix.getId();
			boolean foundEquivalentMatrix = false;

			// iterate over all treebase matrices for the study
			for ( org.cipres.treebase.domain.matrix.Matrix tbMatrix : tbMatrices ) {				
				String tbId = "M" + tbMatrix.getId();
		
				// although there is a class DistanceMatrix, it is my belief that we don't actually have
				// any distance matrices stored, nor can we convert them to NeXML
				Assert.assertTrue("TreeBASE matrix "+tbId+" must be a character matrix, not a distance matrix", tbMatrix instanceof CharacterMatrix);
				
				// if true, the matrices are equivalent
				if ( nexId.equals(tbId) ) {
					foundEquivalentMatrix = true;
					Assert.assertTrue("NeXML matrix "+nexId+ " is one of the known subclasses", 
							nexMatrix instanceof CategoricalMatrix || nexMatrix instanceof MolecularMatrix || nexMatrix instanceof ContinuousMatrix);
					
					// we have to coerce the tbMatrix into a character matrix to get its character sets
					CharacterMatrix tbCharacterMatrix = (CharacterMatrix)tbMatrix;
					Set<CharSet> tbCharSets = tbCharacterMatrix.getCharSets();
					
					// a treebase matrix has zero or more character sets, we must iterate over them
					for ( CharSet tbCharSet : tbCharSets ) {
						
						// the coordinates of the character set are defined by a collection of column ranges that we iterate over
						Collection<ColumnRange> tbColumnRanges = tbCharSet.getColumns(tbCharacterMatrix);
						
						for ( ColumnRange tbColumnRange : tbColumnRanges ) {
							
							// these are the beginning and end of the range
							int start = tbColumnRange.getStartColIndex();
							int stop = tbColumnRange.getEndColIndex();
							
							// this is how we increment from beginning to end. This number is probably either null, for a
							// contiguous range, or perhaps 3 for codon positions
							int inc = 1;
							
							// need to do this to prevent nullpointerexceptions
							if ( null != tbColumnRange.getRepeatInterval() ) {
								inc = tbColumnRange.getRepeatInterval();
							}
							
							// this is how we create the equivalent nexml character set
							// you will need to update CharSet to get the new implementation of getLabel(), which 
							// returns the same value as getTitle()
							Subset nexSubset = nexMatrix.createSubset(tbCharSet.getLabel()); 
							
							// we have to assign character objects to the subset. Here we get the full list
							List<org.nexml.model.Character> nexCharacters = nexMatrix.getCharacters();
							
							// now we iterate over the coordinates and assign the nexml characters to the set
							for ( int i = start; i <= stop; i += inc ) {
								nexSubset.addThing(nexCharacters.get(i));
							}
						}
					}
				}
			}
			Assert.assertTrue("Searched for equivalent to NeXML matrix "+nexId, foundEquivalentMatrix);
			System.out.println(nexDoc.getXmlString());
		}
	}
	
	/**
	 * Test  for {@link org.cipres.treebase.domain.nexus.nexml.NexmlMatrixConverter#}.
	 * It verifies that NexmlCharSets have the same name and coordinates as those in the 
	 * TreeBASE matrix.
	 */
	public void testNexmlMatrixCharSets() {
		String testName = "testNexmlCharSets";
		//signal beginning of test
		if (logger.isInfoEnabled()) {
			logger.info("Running Test: " + testName);
		}
		long studyId = 2000;

		// this is the full study as it is stored by the database
		Study tbStudy = (Study)loadObject(Study.class, studyId);

		// these are the character state matrices that are part of the study
		Set<org.cipres.treebase.domain.matrix.Matrix> tbMatrices = tbStudy.getMatrices();

		// this is an object representation of a NeXML document
		Document nexDoc = DocumentFactory.safeCreateDocument();
		
		// the converter populates the NeXML document with the contents of the treebase study
		NexmlDocumentConverter ndc = new NexmlDocumentConverter(tbStudy,getTaxonLabelHome(),nexDoc);
		ndc.fromTreeBaseToXml(tbStudy); // here is where the conversion happens
		
		// these are the NeXML matrices that were created from the study  		
		List<Matrix<?>> nexMatrices = nexDoc.getMatrices();
		
		// there most be more than zero matrices because every treebase study has at least one matrix
		Assert.assertTrue(nexMatrices.size() != 0 );
		
		// now we're going to match up the NeXML matrices with their equivalent treebase ones
		for ( Matrix<?> nexMatrix : nexMatrices ) {
			
			// the xml id is the same as the primary key of the equivalent matrix stored by treebase
			String nexId = nexMatrix.getId();

			// iterate over all treebase matrices for the study
			for ( org.cipres.treebase.domain.matrix.Matrix tbMatrix : tbMatrices ) {				
				String tbId = "M" + tbMatrix.getId();
		
				// if true, the matrices are equivalent
				if ( nexId.equals(tbId) ) {
					Assert.assertTrue("NeXML matrix "+nexId+ " is one of the known subclasses", 
							nexMatrix instanceof CategoricalMatrix || nexMatrix instanceof MolecularMatrix || nexMatrix instanceof ContinuousMatrix);
					
					// we have to coerce the tbMatrix into a character matrix to get its character sets
					CharacterMatrix tbCharacterMatrix = (CharacterMatrix)tbMatrix;
					Set<CharSet> tbCharSets = tbCharacterMatrix.getCharSets();
					
					// a treebase matrix has zero or more character sets, we must iterate over them
					for ( CharSet tbCharSet : tbCharSets ) {
						
						// the coordinates of the character set are defined by a collection of column ranges that we iterate over
						Collection<ColumnRange> tbColumnRanges = tbCharSet.getColumns(tbCharacterMatrix);
							
						for ( ColumnRange tbColumnRange : tbColumnRanges ) {
							
								// these are the beginning and end of the range
								int tbStart = tbColumnRange.getStartColIndex();
								int tbStop = tbColumnRange.getEndColIndex();
								
								// this is how we increment from beginning to end. This number is probably either null, for a
								// contiguous range, or perhaps 3 for codon positions
								int tbInc = 1;
									
								// need to do this to prevent nullpointerexceptions
								if ( null != tbColumnRange.getRepeatInterval()) {
										
									tbInc = tbColumnRange.getRepeatInterval();
								}
								// this is how we create the equivalent nexml character set
								Subset nexSubset = nexMatrix.createSubset(tbCharSet.getLabel());
									
								//get names of TreeBASE and NeXML character set
								String tbCharSetName = tbCharSet.getLabel();
								String nexCharSetName = nexSubset.getLabel();
									
								//verify that the names are the same
								Assert.assertTrue(tbCharSetName.equals(nexCharSetName));
									
								// we have to assign character objects to the subset. Here we get the full list
								List<org.nexml.model.Character> nexCharacters = nexMatrix.getCharacters();
							
								// now we iterate over the coordinates and assign the nexml characters to the set
								//and verify coordinates of TreeBASE characterset and nexml characterset
								//are the same
								for ( int i = tbStart; i <= tbStop; i += tbInc ) {
									nexSubset.addThing(nexCharacters.get(i));
									
									//declare coordinate index
									int nexCharSetCoordinate = nexCharacters.indexOf(nexCharacters.get(i));							
									int tbCharSetCoordinate = i; // you added the offset twice									
									Assert.assertTrue( nexCharSetCoordinate == tbCharSetCoordinate );
								}
						}
					}
				}
			}
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
