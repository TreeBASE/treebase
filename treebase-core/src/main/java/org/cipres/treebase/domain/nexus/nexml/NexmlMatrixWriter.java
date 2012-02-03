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
import org.cipres.treebase.domain.taxon.SpecimenLabel;
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
	 * This is the method that is called by the NexmlDocumentWriter when turning
	 * a study or data set into a NeXML document 
	 * @param tbMatrix
	 * @param xmlOTUs
	 * @return
	 * XXX doesn't handle the following data types: 	
	 * public static final String MATRIX_DATATYPE_NUCLEOTIDE = "Nucleotide";
	 * public static final String MATRIX_DATATYPE_DISTANCE = "Distance";
	 * public static final String MATRIX_DATATYPE_MIXED = "Mixed";
	 */		
	public org.nexml.model.Matrix<?> fromTreeBaseToXml(CharacterMatrix tbMatrix,OTUs xmlOTUs) {
		
		// here we decide what subtype of character matrix to instantiate
		org.nexml.model.Matrix<?> xmlMatrix = createMatrix(tbMatrix, xmlOTUs);
		
		// here we create column/character sets
		createCharacterSets(tbMatrix, xmlMatrix);
		
		return xmlMatrix;
	}



	/**
	 * 
	 * @param tbMatrix
	 * @param xmlOTUs
	 * @return
	 */
	private org.nexml.model.Matrix<?> createMatrix(CharacterMatrix tbMatrix, OTUs xmlOTUs) {
		
		// here we decide what (super-)type to instantiate: discrete or continuous
		if ( tbMatrix instanceof DiscreteMatrix ) {
			org.nexml.model.Matrix<CharacterState> xmlDiscreteMatrix = null;
			
			// 'standard' data is treated separately because we don't have an alphabet for it
			if ( tbMatrix.getDataType().getDescription().equals(MatrixDataType.MATRIX_DATATYPE_STANDARD) ) {
				
				// standard categorical
				xmlDiscreteMatrix = createStandardNexmlMatrix((StandardMatrix) tbMatrix,xmlOTUs);
			}
			else {
				
				// molecular
				xmlDiscreteMatrix = createMolecularNexmlMatrix((DiscreteMatrix) tbMatrix,xmlOTUs);				
			}
			populateDiscreteNexmlMatrix(xmlDiscreteMatrix,(DiscreteMatrix)tbMatrix);
			return xmlDiscreteMatrix;
		}
		else if ( tbMatrix instanceof ContinuousMatrix ) {
			
			// continuous
			org.nexml.model.ContinuousMatrix xmlContinuousMatrix = createContinuousNexmlMatrix((ContinuousMatrix) tbMatrix,xmlOTUs);			
			populateContinuousNexmlMatrix(xmlContinuousMatrix,(ContinuousMatrix)tbMatrix);
			return xmlContinuousMatrix;
		}
		return null;
	}	
	
	
	/**
	 * Creates and populates characters (i.e. columns) with their annotations,
	 * and state sets, with their annotations. For standard data (including 
	 * those matrices that are actually mostly molecular) we flatten the 
	 * (fictional, but modeled) stateset mapping of all state symbols, plus
	 * missing ('?') and gap ('-').
	 * 
	 * @param tbMatrix
	 * @return an xml matrix with empty rows
	 */
	private CategoricalMatrix createStandardNexmlMatrix(StandardMatrix tbMatrix,OTUs xmlOTUs) {
		if ( null == xmlOTUs ) {
			xmlOTUs = getOTUsById(tbMatrix.getTaxa().getId());
		}
		CategoricalMatrix xmlMatrix = getDocument().createCategoricalMatrix(xmlOTUs);		
		copyMatrixAttributes(tbMatrix,xmlMatrix);
		
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
		
		// the missing symbol ("?") includes all others, including gap ("-")
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
			copyCharacterAttributes(tbColumn, xmlCharacter);
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
	private MolecularMatrix createMolecularNexmlMatrix(DiscreteMatrix tbMatrix,OTUs xmlOTUs) {
		if ( null == xmlOTUs ) {
			xmlOTUs = getOTUsById(tbMatrix.getTaxa().getId());
		}
		String tbDataType = tbMatrix.getDataType().getDescription();
		MolecularMatrix xmlMatrix = null;
		CharacterStateSet xmlStateSet = null;
		
		// create the matrix and constant (IUPAC) state set
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
		copyMatrixAttributes(tbMatrix,xmlMatrix);
		
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
			copyCharacterAttributes(tbColumn, xmlCharacter);
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
	private org.nexml.model.ContinuousMatrix createContinuousNexmlMatrix(ContinuousMatrix tbMatrix,OTUs xmlOTUs) {
		if ( null == xmlOTUs ) {
			xmlOTUs = getOTUsById(tbMatrix.getTaxa().getId());
		}
		org.nexml.model.ContinuousMatrix xmlMatrix = getDocument().createContinuousMatrix(xmlOTUs);
		copyMatrixAttributes(tbMatrix,xmlMatrix);
		
		for ( MatrixColumn tbColumn : tbMatrix.getColumnsReadOnly() ) {
			org.nexml.model.Character xmlCharacter = xmlMatrix.createCharacter();
			copyCharacterAttributes(tbColumn, xmlCharacter);
			
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
	
	/**
	 * 
	 * @param tbMatrix
	 * @param xmlMatrix
	 */
	private void createCharacterSets(CharacterMatrix tbMatrix, org.nexml.model.Matrix<?> xmlMatrix) {
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
				Integer tbRepeatInterval = tbColumnRange.getRepeatInterval();
				if ( null != tbRepeatInterval ) {						
					tbInc = tbRepeatInterval;
				}				
				
				// create the equivalent nexml character set
				Subset xmlSubset = xmlMatrix.createSubset(tbCharSet.getLabel());
			
				// assign character objects to the subset. Here we get the full list
				List<org.nexml.model.Character> xmlCharacters = xmlMatrix.getCharacters();
		
				// now we iterate over the coordinates and assign the nexml characters to the set
				for ( int i = tbStart; i <= tbStop; i += tbInc ) {
					xmlSubset.addThing(xmlCharacters.get(i));
				}
			}
		}
	}	

	/**
	 * 
	 * @param xmlMatrix
	 * @param tbMatrix
	 * @param xmlOTUs
	 * @param stateSet
	 */
	private void populateDiscreteNexmlMatrix(org.nexml.model.Matrix<CharacterState> xmlMatrix, DiscreteMatrix tbMatrix) {	
		
		OTUs xmlOTUs = xmlMatrix.getOTUs();
		List<org.nexml.model.Character> xmlCharacters = xmlMatrix.getCharacters();
		
		// iterates over all matrix rows, i.e. ntax times
		for ( MatrixRow tbRow : tbMatrix.getRowsReadOnly() ) {
			Set<RowSegment> tbSegments = tbRow.getSegmentsReadOnly();
			OTU xmlOTU = getOTUById(xmlOTUs, tbRow.getTaxonLabel().getId());
			if ( xmlCharacters.size() <= MAX_GRANULAR_NCHAR && xmlOTUs.getAllOTUs().size() <= MAX_GRANULAR_NTAX ) {
				populateDiscreteVerboseNexmlMatrix(xmlMatrix,tbMatrix,xmlCharacters,tbRow,tbSegments,xmlOTU);
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
				
				// this often only happens once, when the row has only 1 segment
				for ( RowSegment tbSegment : tbSegments ) {
					copyDarwinCoreAnnotations(tbSegment, xmlOTU);
				}				
			}
		}	
	}

	/**
	 * 
	 * @param xmlMatrix
	 * @param tbMatrix
	 * @param xmlCharacterList
	 * @param tbRow
	 * @param tbSegments
	 * @param xmlOTU
	 */
	private void populateDiscreteVerboseNexmlMatrix(
			org.nexml.model.Matrix<CharacterState> xmlMatrix,
			DiscreteMatrix tbMatrix,
			List<org.nexml.model.Character> xmlCharacterList,MatrixRow tbRow,
			Set<RowSegment> tbSegments, OTU xmlOTU) {
				
		// iterates over all characters, i.e. nchar times
		int charIndex = 0;
		String seq = tbRow.getSymbolString();
		for ( MatrixColumn tbColumn : ((CharacterMatrix)tbMatrix).getColumns() ) {
			
			org.nexml.model.Character xmlCharacter = xmlCharacterList.get(charIndex);
			MatrixCell<CharacterState> xmlCell = xmlMatrix.getCell(xmlOTU, xmlCharacter);
			String value = "" + seq.charAt(charIndex);
			CharacterState xmlState = xmlMatrix.parseSymbol(value);
			xmlCell.setValue(xmlState);
			attachTreeBaseID ((Annotatable) xmlCell,  tbColumn , DiscreteMatrixElement.class);
			
			for ( RowSegment tbSegment : tbSegments ) {
				if ( tbSegment.getStartIndex() <= charIndex && charIndex <= tbSegment.getEndIndex() ) {
					copyDarwinCoreAnnotations(tbSegment, (Annotatable)xmlCell);
				}
			}
			charIndex++;
		}
	}
	
	/**
	 * 
	 * @param xmlMatrix
	 * @param tbMatrix
	 */
	private void populateContinuousNexmlMatrix(org.nexml.model.ContinuousMatrix xmlMatrix,
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
				copyDarwinCoreAnnotations(tbSegment,xmlOTU);
			}
		}		
	}	

	/**
	 * 
	 * @param tbSegment
	 * @param xmlAnnotatable
	 */
	private void copyDarwinCoreAnnotations(RowSegment tbSegment, Annotatable xmlAnnotatable) {
		
		SpecimenLabel tbSpec = tbSegment.getSpecimenLabel();		
		Map<String,String> predicateToObjectMap = new HashMap<String,String>();
		
		predicateToObjectMap.put("DwC:institutionCode", tbSpec.getInstAcronym());
		predicateToObjectMap.put("DwC:collectionCode", tbSpec.getCollectionCode());
		predicateToObjectMap.put("DwC:catalogNumber", tbSpec.getCatalogNumber());
		predicateToObjectMap.put("DwC:associatedSequences", tbSpec.getGenBankAccession());
		predicateToObjectMap.put("DwC:otherCatalogNumbers", tbSpec.getOtherAccession());
		predicateToObjectMap.put("DwC:eventDate", tbSpec.getSampleDateString());
		predicateToObjectMap.put("DwC:scientificName", tbSegment.getSpecimenTaxonLabelAsString());
		predicateToObjectMap.put("DwC:recordedBy", tbSpec.getCollector());
		predicateToObjectMap.put("DwC:country", tbSpec.getCountry());
		predicateToObjectMap.put("DwC:locality", tbSpec.getLocality());
		predicateToObjectMap.put("DwC:stateProvince", tbSpec.getState());
		predicateToObjectMap.put("DwC:datasetName", tbSegment.getTitle());
		predicateToObjectMap.put("DwC:occurenceRemarks", tbSpec.getNotes());
		
		for ( String predicate : predicateToObjectMap.keySet() ) {
			String objectString = predicateToObjectMap.get(predicate);
			if ( null != objectString ) {
				xmlAnnotatable.addAnnotationValue(predicate, Constants.DwCURI, objectString);
			}
		}

		//output geographic latitude in decimal degrees using geodeticDatum spatial reference system
		Double latitude = tbSpec.getLatitude();
		if ( null != latitude ) {			
			xmlAnnotatable.addAnnotationValue("DwC:decimalLatitude", Constants.DwCURI, latitude);
		}
		
		//output geographic longitude in decimal degrees using geodeticDatum spatial reference system
		Double longitude = tbSpec.getLongitude();
		if ( null != longitude ) {			
			xmlAnnotatable.addAnnotationValue("DwC:decimalLongitude", Constants.DwCURI, longitude);
		}
		
		//there are two different Darwin Core terms for elevation depending on elevation value
		//outputs geographic elevation of sample		
		Double elevation = tbSpec.getElevation();
		if ( null != elevation ) {
			if ( elevation >= 0) {
				//above local surface in meters
				xmlAnnotatable.addAnnotationValue("DwC:verbatimElevation", Constants.DwCURI, elevation);
			}
			else {
				//below local surface in meters
				xmlAnnotatable.addAnnotationValue("DwC:verbatimDepth", Constants.DwCURI, elevation);
			}
		}
	}
	
	/**
	 * 
	 * @param tbColumn
	 * @param xmlCharacter
	 */

	private void copyCharacterAttributes(MatrixColumn tbColumn,org.nexml.model.Character xmlCharacter) {
		PhyloChar tbCharacter = tbColumn.getCharacter();
		if ( null != tbCharacter.getDescription() ) {
			xmlCharacter.setLabel(tbCharacter.getLabel());
		}			
		attachTreeBaseID((Annotatable)xmlCharacter,tbColumn,MatrixColumn.class);
	}

	/**
	 * 
	 * @param tbMatrix
	 * @param xmlMatrix
	 */
	private void copyMatrixAttributes(CharacterMatrix tbMatrix,org.nexml.model.Matrix<?> xmlMatrix) {
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


}
