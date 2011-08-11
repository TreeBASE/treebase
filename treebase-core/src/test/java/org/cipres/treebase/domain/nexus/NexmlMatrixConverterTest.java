package org.cipres.treebase.domain.nexus;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import mesquite.lib.Annotatable;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.dao.jdbc.DiscreteMatrixElementJDBC;
import org.cipres.treebase.dao.jdbc.MatrixColumnJDBC;
import org.cipres.treebase.domain.Annotation;
import org.cipres.treebase.domain.matrix.CharSet;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.ColumnRange;
import org.cipres.treebase.domain.matrix.ContinuousMatrixElement;
import org.cipres.treebase.domain.matrix.DiscreteChar;
import org.cipres.treebase.domain.matrix.DiscreteCharState;
import org.cipres.treebase.domain.matrix.DiscreteMatrix;
import org.cipres.treebase.domain.matrix.DiscreteMatrixElement;
import org.cipres.treebase.domain.matrix.MatrixColumn;
import org.cipres.treebase.domain.matrix.MatrixElement;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.PhyloChar;
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.nexus.nexml.NexmlDocumentConverter;
import org.cipres.treebase.domain.nexus.nexml.NexmlObjectConverter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.nexml.model.CategoricalMatrix;
import org.nexml.model.Character;
import org.nexml.model.CharacterState;
import org.nexml.model.ContinuousMatrix;
import org.nexml.model.DocumentFactory;
import org.nexml.model.Document;
import org.nexml.model.Matrix;
import org.nexml.model.MatrixCell;
import org.nexml.model.MolecularMatrix;
import org.nexml.model.OTU;
import org.nexml.model.OTUs;
import org.nexml.model.Subset;

public class NexmlMatrixConverterTest extends AbstractDAOTest {
	private TaxonLabelHome mTaxonLabelHome;
	private static final int MAX_GRANULAR_NCHAR = 1000;
	private static final int MAX_GRANULAR_NTAX = 30;


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
	 * Test for {@link org.cipres.treebase.domain.nexus.nexml.NexmlMatrixConverter#}.
	 * This ensures that a matrix prints for a DiscreteMatrix matrices that
	 * suit the condition : if ( characterList.size() <= MAX_GRANULAR_NCHAR && xmlOTUs.getAllOTUs().size() <= MAX_GRANULAR_NTAX ) 
	 * in populateXmlMatrix() of NexmlMatrixConverter.
	 * 
	 * This is in response to bug #3303002
	 */
	public void testNexmlEmptyMatrix() {
		String testName = "testNexmlEmptyMatrix()";
		//signal beginning of test
		if (logger.isInfoEnabled()) {
			logger.info("Running Test: " + testName);
		}
		
		long studyId = 586; //this study is known to output an empty Nexml matrix
		
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
		
		//returns true if matrix contains no elements
		boolean nexTest = nexMatrices.isEmpty();
		
		//should be false if the matrix contains elements
		Assert.assertFalse(nexTest);
		
		// there most be more than zero matrices because every treebase study has at least one matrix
		Assert.assertTrue(nexMatrices.size() != 0 );

		
		// now we're going to match up the NeXML matrices with their equivalent treebase ones
		for ( Matrix<?> nexMatrix : nexMatrices ) {
			
			// the xml id is the same as the primary key of the equivalent matrix stored by treebase
			String nexId = nexMatrix.getId();
	
			
			for ( org.cipres.treebase.domain.matrix.Matrix tbMatrix : tbMatrices ) {				
				String tbId = "M" + tbMatrix.getId();
				// if true, the matrices are equivalent
				System.out.println(tbId);
				int otuIndex = 0;
				for ( MatrixRow tbRow : ((CharacterMatrix) tbMatrix).getRowsReadOnly() ) {
					OTUs xmlOTUs = nexMatrix.getOTUs();
					
					//need to make an OTU list because getOTUByID cannot be is private
					List<OTU> xmlOTUList = xmlOTUs.getAllOTUs();//getOTUById(xmlOTUs, tbRow.getTaxonLabel().getId());
					OTU xmlOTU = xmlOTUList.get(otuIndex); //get xmlOTUs
					List<org.nexml.model.Character> characterList = nexMatrix.getCharacters();
				
					int charIndex = 0;
					if ( characterList.size() <= MAX_GRANULAR_NCHAR && xmlOTUs.getAllOTUs().size() <= MAX_GRANULAR_NTAX ) { 
					
						//tbColumn is not used, but is in the actual NexmlMatrixConverter class. 
						//it is necessary so the for loop does not crash
						for ( MatrixColumn tbColumn : ((CharacterMatrix)tbMatrix).getColumns() ) {
							
							//this builds the matrix
							String string = tbRow.buildElementAsString();
							nexMatrix.setSeq(string, xmlOTU);
							
							//in NexmlMatrixConverter attachTreeBASEID would be called here. Not necessary for the test.
						}
					charIndex++;
					otuIndex++;
					
				}
				
				if ( nexId.equals(tbId) ) {
					Assert.assertTrue("NeXML matrix "+nexId+ " is one of the known subclasses", 
					nexMatrix instanceof CategoricalMatrix || nexMatrix instanceof MolecularMatrix || nexMatrix instanceof ContinuousMatrix);
					Assert.assertNotNull(nexMatrix);
					Assert.assertNotNull(tbMatrix);
							
				}

				}
			}
		}
		System.out.println(nexDoc.getXmlString());
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
		long studyId = 794;

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
					
					// NexmlMatrixConverter must have assigned character objects to zero or more subsets. Here we get the full list of characters
					List<org.nexml.model.Character> nexCharacters = nexMatrix.getCharacters();
					Assert.assertEquals("The number of characters in the NeXML matrix must match that of the TreeBASE matrix", (Integer)tbMatrix.getnChar(), (Integer)nexCharacters.size());
					
					if (tbCharSets.isEmpty() != true) {
						// a treebase matrix has zero or more character sets, we must iterate over them
						for ( CharSet tbCharSet : tbCharSets ) {
							
							// this is how we fetch the equivalent nexml character set
							Subset nexSubset = nexMatrix.getSubset(tbCharSet.getLabel());
							Assert.assertNotNull("If NexmlMatrixConverter works correctly, a Subset is returned", nexSubset);							
						
							//get names of TreeBASE and NeXML character set
							String tbCharSetName = tbCharSet.getLabel();
							String nexCharSetName = nexSubset.getLabel();
							
							//verify that the names are the same
							Assert.assertEquals("The NeXML character set must have copied the label of the TreeBASE character set",tbCharSetName,nexCharSetName);							
							
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
								
								// The NexmlMatrixConverter should have created a Subset for each tbCharSet
								if ( null != nexSubset ) {																		
								
									// now we iterate over the coordinates in this column range
									//and verify whether correct character objects are returned
									for ( int i = tbStart; i <= tbStop; i += tbInc ) {
										
										// get the nexml character that should have been created
										org.nexml.model.Character nexCharacter = nexCharacters.get(i);
										Assert.assertNotNull("The NeXML Character should not be null if there as an index into it in this set", nexCharacter);
										Assert.assertTrue("The Subset should contain the character at index i", nexSubset.containsThing(nexCharacter));
										
										//get the treebase character for the index in this column range
										PhyloChar tbCharacter = tbCharacterMatrix.getCharacter(i);
										Assert.assertNotNull("The TreeBASE PhyloChar should not be null if there as an index into it in this set", tbCharacter);										
										Assert.assertEquals("If the TreeBASE character has a label, then the NeXML character's label should match it", tbCharacter.getLabel(), nexCharacter.getLabel());
										
									}
								}
								else {
									System.out.println("NexmlMatrixConverter failed to create a Subset");
								}
							}
						}
					}
					else {
						System.out.println("This study has no associated character sets!");
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
