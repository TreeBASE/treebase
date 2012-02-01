package org.cipres.treebase.domain.nexus.nexml;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cipres.treebase.Constants;
import org.cipres.treebase.domain.matrix.CharSet;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.ColumnRange;
import org.cipres.treebase.domain.matrix.ContinuousMatrix;
import org.cipres.treebase.domain.matrix.ContinuousMatrixElement;
import org.cipres.treebase.domain.matrix.DiscreteCharState;
import org.cipres.treebase.domain.matrix.DiscreteMatrix;
import org.cipres.treebase.domain.matrix.DiscreteMatrixElement;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixColumn;
import org.cipres.treebase.domain.matrix.MatrixDataType;
import org.cipres.treebase.domain.matrix.MatrixElement;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.PhyloChar;
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.matrix.StandardMatrix;
import org.cipres.treebase.domain.matrix.StateSet;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.nexml.model.Annotatable;
import org.nexml.model.CategoricalMatrix;
import org.nexml.model.CharacterState;
import org.nexml.model.CharacterStateSet;
import org.nexml.model.Document;
import org.nexml.model.MatrixCell;
import org.nexml.model.MolecularMatrix;
import org.nexml.model.OTUs;
import org.nexml.model.OTU;
import org.nexml.model.Subset;
import org.nexml.model.UncertainCharacterState;

public class NexmlMatrixWriter extends NexmlObjectConverter {

	private static final int MAX_GRANULAR_NCHAR = 1000;
	private static final int MAX_GRANULAR_NTAX = 30;

	/**
	 * 
	 * @param study
	 * @param taxonLabelHome
	 */
	public NexmlMatrixWriter(Study study,TaxonLabelHome taxonLabelHome,Document document) {
		super(study,taxonLabelHome,document);	
	}	
	
	
	/**
	 * Creates and populates characters (i.e. columns) with their annotations,
	 * and state sets, with their annotations
	 * 
	 * @param tbMatrix
	 * @return an xml matrix with empty rows
	 */
	private CategoricalMatrix fromTreeBaseToXml(StandardMatrix tbMatrix,OTUs xmlOTUs) {
		if ( null == xmlOTUs ) {
			xmlOTUs = getOTUsById(tbMatrix.getTaxa().getId());
		}
		CategoricalMatrix xmlMatrix = getDocument().createCategoricalMatrix(xmlOTUs);		
		setMatrixAttributes(xmlMatrix,tbMatrix);
		
		// first flatten the two-dimensional list into a map, we will always only create a single state set
		List<List<DiscreteCharState>> tbStateLabels = tbMatrix.getStateLabels();
		Map<Character,DiscreteCharState> stateForSymbol = new HashMap<Character,DiscreteCharState>();
		CharacterStateSet xmlStateSet = xmlMatrix.createCharacterStateSet();
		for ( int i = 0; i < tbStateLabels.size(); i++ ) {
			for ( int j = 0; j < tbStateLabels.get(i).size(); j++ ) {
				Character symbol = tbStateLabels.get(i).get(j).getSymbol();
				DiscreteCharState state = tbStateLabels.get(i).get(j);
				stateForSymbol.put(symbol, state);			
			}
		}
		
		// then create the single state set out of the map, assigning all non-missing characters to missing
		Set<CharacterState> xmlMissingStates = new HashSet<CharacterState>();
		for ( Character symbol : stateForSymbol.keySet() ) {
			char sym = symbol.charValue();			
			if ( sym != '-' && sym != '?' ) {
				String symString = symbol.toString();
				CharacterState xmlState = xmlStateSet.createCharacterState(symString);
				xmlMissingStates.add(xmlState);
				DiscreteCharState tbState = stateForSymbol.get(symbol);
				xmlState.setLabel(symString);
				attachTreeBaseID((Annotatable)xmlState,tbState,DiscreteCharState.class);				
			}				
		}
		
		UncertainCharacterState gap = xmlStateSet.createUncertainCharacterState("-", new HashSet<CharacterState>());
		gap.setLabel("-");
		xmlMissingStates.add(gap);
		UncertainCharacterState missing = xmlStateSet.createUncertainCharacterState("?", xmlMissingStates);
		missing.setLabel("?");
		
		// then create the XML characters, assigning them all the same state set
		List<MatrixColumn> tbColumns = tbMatrix.getColumnsReadOnly();
		for ( int i = 0; i < tbColumns.size(); i++ ) {
			MatrixColumn tbColumn = tbColumns.get(i);			
			org.nexml.model.Character xmlCharacter = xmlMatrix.createCharacter(xmlStateSet);
			setCharacterAttributes(tbColumn, xmlCharacter);
		}		
		return xmlMatrix;
	}

	private void setCharacterAttributes(MatrixColumn tbColumn,org.nexml.model.Character xmlCharacter) {
		PhyloChar tbCharacter = tbColumn.getCharacter();
		if ( null != tbCharacter.getDescription() ) {
			xmlCharacter.setLabel(tbCharacter.getLabel());
		}			
		attachTreeBaseID((Annotatable)xmlCharacter,tbColumn,MatrixColumn.class);
	}

	private void setMatrixAttributes(org.nexml.model.Matrix<?> xmlMatrix,CharacterMatrix tbMatrix) {
		// attach matrix identifiers
		attachTreeBaseID((Annotatable)xmlMatrix, tbMatrix,Matrix.class);
		String tb1MatrixID = tbMatrix.getTB1MatrixID();
		if ( null != tb1MatrixID ) {
			((Annotatable)xmlMatrix).addAnnotationValue("tb:identifier.matrix.tb1", Constants.TBTermsURI, tb1MatrixID);
		}		
		
		xmlMatrix.addAnnotationValue("skos:historyNote", Constants.SKOSURI, "Mapped from TreeBASE schema using "+this.toString()+" $Rev$");
		xmlMatrix.setBaseURI(mMatrixBaseURI);
		xmlMatrix.setLabel(tbMatrix.getLabel());				
	}	
	
	/**
	 * Creates and populates characters (i.e. columns) with their annotations,
	 * and state sets, with their annotations
	 * 
	 * @param tbMatrix
	 * @return an xml matrix with empty rows
	 */	
	private MolecularMatrix fromTreeBaseToXml(DiscreteMatrix tbMatrix,OTUs xmlOTUs) {
		if ( null == xmlOTUs ) {
			xmlOTUs = getOTUsById(tbMatrix.getTaxa().getId());
		}
		String tbDataType = tbMatrix.getDataType().getDescription();
		MolecularMatrix xmlMatrix = null;
		CharacterStateSet xmlStateSet = null;
		
		// create the matrix and constant state set
		if ( tbDataType.equals(MatrixDataType.MATRIX_DATATYPE_DNA) ) {
			xmlMatrix = getDocument().createMolecularMatrix(xmlOTUs, MolecularMatrix.DNA);
			xmlStateSet = ((MolecularMatrix)xmlMatrix).getDNACharacterStateSet();
		}
		else if ( tbDataType.equals(MatrixDataType.MATRIX_DATATYPE_RNA) ) {
			xmlMatrix = getDocument().createMolecularMatrix(xmlOTUs, MolecularMatrix.RNA);
			xmlStateSet = ((MolecularMatrix)xmlMatrix).getRNACharacterStateSet();
		}
		else if ( tbDataType.equals(MatrixDataType.MATRIX_DATATYPE_PROTEIN) ) {
			xmlMatrix = getDocument().createMolecularMatrix(xmlOTUs, MolecularMatrix.Protein);
			xmlStateSet = ((MolecularMatrix)xmlMatrix).getProteinCharacterStateSet();
		}
		setMatrixAttributes(xmlMatrix,tbMatrix);
		
		// lookup the equivalent state in tb and attach identifiers
		for(StateSet tbStateSet : tbMatrix.getStateSets() ) {
			for (DiscreteCharState tbState : tbStateSet.getStates() ) {
				String tbSymbol = tbState.getSymbol().toString().toUpperCase();
				CharacterState xmlState = xmlStateSet.lookupCharacterStateBySymbol(tbSymbol);
				if ( null == xmlState ) {
					xmlState = xmlStateSet.createCharacterState(tbSymbol);
				}
				attachTreeBaseID((Annotatable)xmlState,tbState,DiscreteCharState.class);
			}
		}
		
		// create columns and attach identifiers
		for ( MatrixColumn tbColumn : tbMatrix.getColumnsReadOnly() ) {
			org.nexml.model.Character xmlCharacter = xmlMatrix.createCharacter(xmlStateSet);
			setCharacterAttributes(tbColumn, xmlCharacter);
		}
		return xmlMatrix;
	}
	
	/**
	 * Creates and populates characters (i.e. columns) with their annotations,
	 * and state sets, with their annotations
	 * 
	 * @param tbMatrix
	 * @return an xml matrix with empty rows
	 */		
	private org.nexml.model.ContinuousMatrix fromTreeBaseToXml(ContinuousMatrix tbMatrix,OTUs xmlOTUs) {
		if ( null == xmlOTUs ) {
			xmlOTUs = getOTUsById(tbMatrix.getTaxa().getId());
		}
		org.nexml.model.ContinuousMatrix xmlMatrix = getDocument().createContinuousMatrix(xmlOTUs);
		setMatrixAttributes(xmlMatrix,tbMatrix);
		
		for ( MatrixColumn tbColumn : tbMatrix.getColumnsReadOnly() ) {
			org.nexml.model.Character xmlCharacter = xmlMatrix.createCharacter();
			setCharacterAttributes(tbColumn, xmlCharacter);
			
			//coerce the tbMatrix into a character matrix to get its character sets
			CharacterMatrix tbCharacterMatrix = (CharacterMatrix)tbMatrix;
			Set<CharSet> tbCharSets = tbCharacterMatrix.getCharSets();
			for ( CharSet tbCharSet : tbCharSets ) {
				Collection<ColumnRange> tbColumnRanges = tbCharSet.getColumns(tbCharacterMatrix);
				
				for ( ColumnRange tbColumnRange : tbColumnRanges ) {
					
					// these are the beginning and end of the range
					int tbStart = tbColumnRange.getStartColIndex();
					int tbStop = tbColumnRange.getEndColIndex();
						
					// increment from beginning to end. This number is probably either null, for a
					// contiguous range, or perhaps 3 for codon positions
					int tbInc = 1;
					// create the equivalent nexml character set
					Subset nexSubset = xmlMatrix.createSubset(tbCharSet.getLabel());
				
					// assign character objects to the subset. Here we get the full list
					List<org.nexml.model.Character> nexCharacters = xmlMatrix.getCharacters();
			
					// now we iterate over the coordinates and assign the nexml characters to the set
					for ( int i = tbStart; i <= tbStop; i += tbInc ) {
						nexSubset.addThing(nexCharacters.get(i));
					}
				}
			}
			
		}
		
		return xmlMatrix;
	}
	

// XXX doesn't handle the following data types: 	
//	public static final String MATRIX_DATATYPE_NUCLEOTIDE = "Nucleotide";
//	public static final String MATRIX_DATATYPE_DISTANCE = "Distance";
//	public static final String MATRIX_DATATYPE_MIXED = "Mixed";		
	@SuppressWarnings("unchecked")
	public org.nexml.model.Matrix<?> fromTreeBaseToXml(CharacterMatrix tbMatrix,OTUs xmlOTUs) {
		org.nexml.model.Matrix<?> xmlMatrix = null;
		if ( tbMatrix instanceof DiscreteMatrix ) {
			if ( tbMatrix.getDataType().getDescription().equals(MatrixDataType.MATRIX_DATATYPE_STANDARD) ) {
				xmlMatrix = fromTreeBaseToXml((StandardMatrix) tbMatrix,xmlOTUs);
			}
			else {
				xmlMatrix = fromTreeBaseToXml((DiscreteMatrix) tbMatrix,xmlOTUs);				
			}
			populateXmlMatrix((org.nexml.model.Matrix<CharacterState>)xmlMatrix,(DiscreteMatrix)tbMatrix);
		}
		else if ( tbMatrix instanceof ContinuousMatrix ) {
			xmlMatrix = fromTreeBaseToXml((ContinuousMatrix) tbMatrix,xmlOTUs);			
			populateXmlMatrix((org.nexml.model.ContinuousMatrix)xmlMatrix,(ContinuousMatrix)tbMatrix);
		}
		
		// here we copy the character sets for all matrix types
		Set<CharSet> tbCharSets = tbMatrix.getCharSets();
		for ( CharSet tbCharSet : tbCharSets ) {
			Collection<ColumnRange> tbColumnRanges = tbCharSet.getColumns(tbMatrix);
			
			for ( ColumnRange tbColumnRange : tbColumnRanges ) {
				
				// these are the beginning and end of the range
				int tbStart = tbColumnRange.getStartColIndex();
				int tbStop = tbColumnRange.getEndColIndex();
					
				// increment from beginning to end. This number is probably either null, for a
				// contiguous range, or perhaps 3 for codon positions
				int tbInc = 1;
				
				// need to do this to prevent nullpointerexceptions
				if ( null != tbColumnRange.getRepeatInterval()) {						
					tbInc = tbColumnRange.getRepeatInterval();
				}				
				
				// create the equivalent nexml character set
				Subset nexSubset = xmlMatrix.createSubset(tbCharSet.getLabel());
			
				// assign character objects to the subset. Here we get the full list
				List<org.nexml.model.Character> nexCharacters = xmlMatrix.getCharacters();
		
				// now we iterate over the coordinates and assign the nexml characters to the set
				for ( int i = tbStart; i <= tbStop; i += tbInc ) {
					nexSubset.addThing(nexCharacters.get(i));
				}
			}
		}
		
		return xmlMatrix;
	}

	/**
	 * 
	 * @param xmlMatrix
	 * @param tbMatrix
	 * @param xmlOTUs
	 * @param stateSet
	 */
	private void populateXmlMatrix(
			org.nexml.model.Matrix<CharacterState> xmlMatrix,
			DiscreteMatrix tbMatrix) {	
		OTUs xmlOTUs = xmlMatrix.getOTUs();
		List<org.nexml.model.Character> characterList = xmlMatrix.getCharacters();
		for ( MatrixRow tbRow : tbMatrix.getRowsReadOnly() ) {
			Set<RowSegment> tbSegments = tbRow.getSegmentsReadOnly();
			OTU xmlOTU = getOTUById(xmlOTUs, tbRow.getTaxonLabel().getId());
			int charIndex = 0;
			if ( characterList.size() <= MAX_GRANULAR_NCHAR && xmlOTUs.getAllOTUs().size() <= MAX_GRANULAR_NTAX ) {
				for ( MatrixColumn tbColumn : ((CharacterMatrix)tbMatrix).getColumns() ) {
					String seq = tbRow.getNormalizedSymbolString();
					xmlMatrix.setSeq(seq, xmlOTU);
					org.nexml.model.Character xmlCharacter = characterList.get(charIndex);
					MatrixCell<CharacterState> xmlCell = xmlMatrix.getCell(xmlOTU, xmlCharacter);							
		
					attachTreeBaseID ((Annotatable) xmlCell,  tbColumn , DiscreteMatrixElement.class);
				
				//The following is commented out as tbRow.getElements() does not work directly and crashes the loop. 
				//The above for loop fixes this issue. 
				/*
				for ( MatrixElement tbCell : tbRow.getElements() ) {
					org.nexml.model.Character xmlCharacter = characterList.get(charIndex);
					MatrixCell<CharacterState> xmlCell = xmlMatrix.getCell(xmlOTU, xmlCharacter);				
					DiscreteCharState tbState = ((DiscreteMatrixElement)tbCell).getCharState();
					String tbSymbolString = ( null == tbState ) ? "?" : tbState.getSymbol().toString();
					CharacterState xmlState = xmlCharacter.getCharacterStateSet().lookupCharacterStateBySymbol(tbSymbolString);								
					xmlCell.setValue(xmlState);								
					attachTreeBaseID((Annotatable)xmlCell,tbCell,DiscreteMatrixElement.class);
				*/
					
					for ( RowSegment tbSegment : tbSegments ) {
						if ( tbSegment.getStartIndex() <= charIndex && charIndex <= tbSegment.getEndIndex() ) {
							//declare variables for row-segment annotations
							String title = tbSegment.getTitle();
							String institutionCode = tbSegment.getSpecimenLabel().getInstAcronym();
							String collectionCode = tbSegment.getSpecimenLabel().getCollectionCode();
							String catalogNumber = tbSegment.getSpecimenLabel().getCatalogNumber();
							String accessionNumber = tbSegment.getSpecimenLabel().getGenBankAccession();
							String otherAccessionNumber = tbSegment.getSpecimenLabel().getOtherAccession();
							String dateSampled = tbSegment.getSpecimenLabel().getSampleDateString();
							String scientificName = tbSegment.getSpecimenTaxonLabelAsString();
							String collector = tbSegment.getSpecimenLabel().getCollector();
							Double latitude = tbSegment.getSpecimenLabel().getLatitude();
							Double longitude = tbSegment.getSpecimenLabel().getLongitude();
							Double elevation = tbSegment.getSpecimenLabel().getElevation();
							String country = tbSegment.getSpecimenLabel().getCountry();
							String state = tbSegment.getSpecimenLabel().getState();
							String locality = tbSegment.getSpecimenLabel().getLocality();
							String notes = tbSegment.getSpecimenLabel().getNotes();
							
							//if the value is not null, output the xmlOTU annotation.
							//DwC refers to the Darwin Core term vocabulary for the associated annotation
							if (null != title){
								//output name identifying the data set from which the record was derived
								((Annotatable)xmlCell).addAnnotationValue("DwC:datasetName", Constants.DwCURI, title);
							}
							if ( null != institutionCode ) {
								//output name or acronym of institution that has custody of information referred to in record
								((Annotatable)xmlCell).addAnnotationValue("DwC:institutionCode", Constants.DwCURI, institutionCode);
							}
							if ( null != collectionCode ) {
								//output name or code that identifies collection or data set from which record was derived
								((Annotatable)xmlCell).addAnnotationValue ("DwC:collectionCode", Constants.DwCURI, collectionCode);
							}
							if ( null != catalogNumber ){
								//output unique (usually) identifier for the record within data set or collection
								((Annotatable)xmlCell).addAnnotationValue("DwC:catalogNumber", Constants.DwCURI, catalogNumber);
							}
							if ( null != accessionNumber) {
								//output a list of genetic sequence information associated with occurrence
								((Annotatable)xmlCell).addAnnotationValue("DwC:associatedSequences", Constants.DwCURI, accessionNumber);
							}
							if ( null != otherAccessionNumber ) {
								//list of previous or alternate fully catalog numbers (i.e. Genbank) or human-used identifiers 
								((Annotatable)xmlCell).addAnnotationValue("DwC:otherCatalogNumbers", Constants.DwCURI, otherAccessionNumber);
							}
							if ( null != dateSampled ) {
								//output date sampled in ISO 8601 format
								((Annotatable)xmlCell).addAnnotationValue("DwC:eventDate", Constants.DwCURI, dateSampled);
							}
							if ( null != scientificName ) {
								//output full scientific name
								((Annotatable)xmlCell).addAnnotationValue("DwC:scientificName", Constants.DwCURI, scientificName);
							}
							if ( null != collector ) {
								//output names of people associated with recording of original occurrence 
								((Annotatable)xmlCell).addAnnotationValue("DwC:recordedBy", Constants.DwCURI, collector);
							}
							if ( null != latitude ) {
								//output geographic latitude in decimal degrees using geodeticDatum spatial reference system
								((Annotatable)xmlCell).addAnnotationValue("DwC:decimalLatitude", Constants.DwCURI, latitude);
							}
							if ( null != longitude ) {
								//output geographic longitude in decimal degrees using geodeticDatum spatial reference system
								((Annotatable)xmlCell).addAnnotationValue("DwC:decimalLongitude", Constants.DwCURI, longitude);
							}
							if ( null != elevation ) {
								//there are two different Darwin Core terms for elevation depending on elevation value
								//outputs geographic elevation of sample
								if ( elevation >= 0) {
									//above local surface in meters
									((Annotatable)xmlCell).addAnnotationValue("DwC:verbatimElevation", Constants.DwCURI, elevation);
								}
								else {
									//below local surface in meters
									((Annotatable)xmlCell).addAnnotationValue("DwC:verbatimDepth", Constants.DwCURI, elevation);
								}
							}
							if ( null != country ) {
								//output country in which location occurs
								((Annotatable)xmlCell).addAnnotationValue("DwC:country", Constants.DwCURI, country);
							}
							if ( null != state ) {
								//output name of next smaller administrative region than country (i.e. state, province, region)
								((Annotatable)xmlCell).addAnnotationValue ("DwC:stateProvince", Constants.DwCURI, state);
							}
							if ( null != locality) {
								//output brief description of sample location
								((Annotatable)xmlCell).addAnnotationValue("DwC:locality", Constants.DwCURI, locality);
							}
							if ( null != notes ) {
								//output any additional information about specimen
								((Annotatable)xmlCell).addAnnotationValue("DwC:occurenceRemarks", Constants.DwCURI, notes);
							}
						}
					}
					charIndex++;
				}
			}
			else {
				String seq = tbRow.getNormalizedSymbolString();
				if ( tbMatrix.getDataType().getDescription().equals(MatrixDataType.MATRIX_DATATYPE_STANDARD) ) {
					StringBuilder sb = new StringBuilder();
					for ( int i = 0; i < seq.length(); i++ ) {
						sb.append(seq.charAt(i));
						if ( i < seq.length() - 1 ) {
							sb.append(' ');
						}
					}
				}				
				xmlMatrix.setSeq(seq,xmlOTU);
			}
			for ( RowSegment tbSegment : tbSegments ) {
				//declare variables for row-segment annotations
				String title = tbSegment.getTitle();
				String institutionCode = tbSegment.getSpecimenLabel().getInstAcronym();
				String collectionCode = tbSegment.getSpecimenLabel().getCollectionCode();
				String catalogNumber = tbSegment.getSpecimenLabel().getCatalogNumber();
				String accessionNumber = tbSegment.getSpecimenLabel().getGenBankAccession();
				String otherAccessionNumber = tbSegment.getSpecimenLabel().getOtherAccession();
				String dateSampled = tbSegment.getSpecimenLabel().getSampleDateString();
				String scientificName = tbSegment.getSpecimenTaxonLabelAsString();
				String collector = tbSegment.getSpecimenLabel().getCollector();
				Double latitude = tbSegment.getSpecimenLabel().getLatitude();
				Double longitude = tbSegment.getSpecimenLabel().getLongitude();
				Double elevation = tbSegment.getSpecimenLabel().getElevation();
				String country = tbSegment.getSpecimenLabel().getCountry();
				String state = tbSegment.getSpecimenLabel().getState();
				String locality = tbSegment.getSpecimenLabel().getLocality();
				String notes = tbSegment.getSpecimenLabel().getNotes();
				
				//if the value is not null, output the xmlOTU annotation.
				//DwC refers to the Darwin Core term vocabulary for the associated annotation
				if (null != title){
					//output name identifying the data set from which the record was derived
					xmlOTU.addAnnotationValue("DwC:datasetName", Constants.DwCURI, title);
				}
				if ( null != institutionCode ) {
					//output name or acronym of institution that has custody of information referred to in record
					xmlOTU.addAnnotationValue("DwC:institutionCode", Constants.DwCURI, institutionCode);
				}
				if ( null != collectionCode ) {
					//output name or code that identifies collection or data set from which record was derived
					xmlOTU.addAnnotationValue ("DwC:collectionCode", Constants.DwCURI, collectionCode);
				}
				if ( null != catalogNumber ){
					//output unique (usually) identifier for the record within data set or collection
					xmlOTU.addAnnotationValue("DwC:catalogNumber", Constants.DwCURI, catalogNumber);
				}
				if ( null != accessionNumber) {
					//output a list of genetic sequence information associated with occurrence
					xmlOTU.addAnnotationValue("DwC:associatedSequences", Constants.DwCURI, accessionNumber);
				}
				if ( null != otherAccessionNumber ) {
					//list of previous or alternate fully catalog numbers (i.e. Genbank) or human-used identifiers 
					xmlOTU.addAnnotationValue("DwC:otherCatalogNumbers", Constants.DwCURI, otherAccessionNumber);
				}
				if ( null != dateSampled ) {
					//output date sampled in ISO 8601 format
					xmlOTU.addAnnotationValue("DwC:eventDate", Constants.DwCURI, dateSampled);
				}
				if ( null != scientificName ) {
					//output full scientific name
					xmlOTU.addAnnotationValue("DwC:scientificName", Constants.DwCURI, scientificName);
				}
				if ( null != collector ) {
					//output names of people associated with recording of original occurrence 
					xmlOTU.addAnnotationValue("DwC:recordedBy", Constants.DwCURI, collector);
				}
				if ( null != latitude ) {
					//output geographic latitude in decimal degrees using geodeticDatum spatial reference system
					xmlOTU.addAnnotationValue("DwC:decimalLatitude", Constants.DwCURI, latitude);
				}
				if ( null != longitude ) {
					//output geographic longitude in decimal degrees using geodeticDatum spatial reference system
					xmlOTU.addAnnotationValue("DwC:decimalLongitude", Constants.DwCURI, longitude);
				}
				if ( null != elevation ) {
					//there are two different Darwin Core terms for elevation depending on elevation value
					//outputs geographic elevation of sample
					if ( elevation >= 0) {
						//above local surface in meters
						xmlOTU.addAnnotationValue("DwC:verbatimElevation", Constants.DwCURI, elevation);
					}
					else {
						//below local surface in meters
						xmlOTU.addAnnotationValue("DwC:verbatimDepth", Constants.DwCURI, elevation);
					}
				}
				if ( null != country ) {
					//output country in which location occurs
					xmlOTU.addAnnotationValue("DwC:country", Constants.DwCURI, country);
				}
				if ( null != state ) {
					//output name of next smaller administrative region than country (i.e. state, province, region)
					xmlOTU.addAnnotationValue ("DwC:stateProvince", Constants.DwCURI, state);
				}
				if ( null != locality) {
					//output brief description of sample location
					xmlOTU.addAnnotationValue("DwC:locality", Constants.DwCURI, locality);
				}
				if ( null != notes ) {
					//output any additional information about specimen
					xmlOTU.addAnnotationValue("DwC:occurenceRemarks", Constants.DwCURI, notes);
				}
			}
		}	
	}
	
	/**
	 * 
	 * @param xmlMatrix
	 * @param tbMatrix
	 */
	private void populateXmlMatrix(org.nexml.model.ContinuousMatrix xmlMatrix,
			ContinuousMatrix tbMatrix) {
		List<org.nexml.model.Character> characterList = xmlMatrix.getCharacters();
		OTUs xmlOTUs = xmlMatrix.getOTUs();
		for ( MatrixRow tbRow : tbMatrix.getRowsReadOnly() ) {			
			List<MatrixElement> elements = tbRow.getElements();
			OTU xmlOTU = getOTUById(xmlOTUs, tbRow.getTaxonLabel().getId());
			if ( characterList.size() <= MAX_GRANULAR_NCHAR && xmlOTUs.getAllOTUs().size() <= MAX_GRANULAR_NTAX ) {
				for ( int elementIndex = 0; elementIndex < tbMatrix.getnChar(); elementIndex++ ) {
					ContinuousMatrixElement tbCell = (ContinuousMatrixElement)elements.get(elementIndex);
					MatrixCell<Double> xmlCell = xmlMatrix.getCell(xmlOTU, characterList.get(elementIndex));
					xmlCell.setValue(tbCell.getValue());
					attachTreeBaseID((Annotatable)xmlCell,tbCell,DiscreteMatrixElement.class);
				}
			}
			else {
				String seq = tbRow.buildElementAsString();
				xmlMatrix.setSeq(seq,xmlOTU);
			}
			Set<RowSegment> tbSegments = tbRow.getSegmentsReadOnly();
			for ( RowSegment tbSegment : tbSegments ) {
				//declare variables for row-segment annotations
				String title = tbSegment.getTitle();
				String institutionCode = tbSegment.getSpecimenLabel().getInstAcronym();
				String collectionCode = tbSegment.getSpecimenLabel().getCollectionCode();
				String catalogNumber = tbSegment.getSpecimenLabel().getCatalogNumber();
				String accessionNumber = tbSegment.getSpecimenLabel().getGenBankAccession();
				String otherAccessionNumber = tbSegment.getSpecimenLabel().getOtherAccession();
				String dateSampled = tbSegment.getSpecimenLabel().getSampleDateString();
				String scientificName = tbSegment.getSpecimenTaxonLabelAsString();
				String collector = tbSegment.getSpecimenLabel().getCollector();
				Double latitude = tbSegment.getSpecimenLabel().getLatitude();
				Double longitude = tbSegment.getSpecimenLabel().getLongitude();
				Double elevation = tbSegment.getSpecimenLabel().getElevation();
				String country = tbSegment.getSpecimenLabel().getCountry();
				String state = tbSegment.getSpecimenLabel().getState();
				String locality = tbSegment.getSpecimenLabel().getLocality();
				String notes = tbSegment.getSpecimenLabel().getNotes();
				
				//if the value is not null, output the xmlOTU annotation.
				//DwC refers to the Darwin Core term vocabulary for the associated annotation
				if (null != title){
					//output name identifying the data set from which the record was derived
					xmlOTU.addAnnotationValue("DwC:datasetName", Constants.DwCURI, title);
				}
				if ( null != institutionCode ) {
					//output name or acronym of institution that has custody of information referred to in record
					xmlOTU.addAnnotationValue("DwC:institutionCode", Constants.DwCURI, institutionCode);
				}
				if ( null != collectionCode ) {
					//output name or code that identifies collection or data set from which record was derived
					xmlOTU.addAnnotationValue ("DwC:collectionCode", Constants.DwCURI, collectionCode);
				}
				if ( null != catalogNumber ){
					//output unique (usually) identifier for the record within data set or collection
					xmlOTU.addAnnotationValue("DwC:catalogNumber", Constants.DwCURI, catalogNumber);
				}
				if ( null != accessionNumber) {
					//output a list of genetic sequence information associated with occurrence
					xmlOTU.addAnnotationValue("DwC:associatedSequences", Constants.DwCURI, accessionNumber);
				}
				if ( null != otherAccessionNumber ) {
					//list of previous or alternate fully catalog numbers (i.e. Genbank) or human-used identifiers 
					xmlOTU.addAnnotationValue("DwC:otherCatalogNumbers", Constants.DwCURI, otherAccessionNumber);
				}
				if ( null != dateSampled ) {
					//output date sampled in ISO 8601 format
					xmlOTU.addAnnotationValue("DwC:eventDate", Constants.DwCURI, dateSampled);
				}
				if ( null != scientificName ) {
					//output full scientific name
					xmlOTU.addAnnotationValue("DwC:scientificName", Constants.DwCURI, scientificName);
				}
				if ( null != collector ) {
					//output names of people associated with recording of original occurrence 
					xmlOTU.addAnnotationValue("DwC:recordedBy", Constants.DwCURI, collector);
				}
				if ( null != latitude ) {
					//output geographic latitude in decimal degrees using geodeticDatum spatial reference system
					xmlOTU.addAnnotationValue("DwC:decimalLatitude", Constants.DwCURI, latitude);
				}
				if ( null != longitude ) {
					//output geographic longitude in decimal degrees using geodeticDatum spatial reference system
					xmlOTU.addAnnotationValue("DwC:decimalLongitude", Constants.DwCURI, longitude);
				}
				if ( null != elevation ) {
					//there are two different Darwin Core terms for elevation depending on elevation value
					//outputs geographic elevation of sample
					if ( elevation >= 0) {
						//above local surface in meters
						xmlOTU.addAnnotationValue("DwC:verbatimElevation", Constants.DwCURI, elevation);
					}
					else {
						//below local surface in meters
						xmlOTU.addAnnotationValue("DwC:verbatimDepth", Constants.DwCURI, elevation);
					}
				}
				if ( null != country ) {
					//output country in which location occurs
					xmlOTU.addAnnotationValue("DwC:country", Constants.DwCURI, country);
				}
				if ( null != state ) {
					//output name of next smaller administrative region than country (i.e. state, province, region)
					xmlOTU.addAnnotationValue ("DwC:stateProvince", Constants.DwCURI, state);
				}
				if ( null != locality) {
					//output brief description of sample location
					xmlOTU.addAnnotationValue("DwC:locality", Constants.DwCURI, locality);
				}
				if ( null != notes ) {
					//output any additional information about specimen
					xmlOTU.addAnnotationValue("DwC:occurenceRemarks", Constants.DwCURI, notes);
				}
			}
		}		
	}

}
