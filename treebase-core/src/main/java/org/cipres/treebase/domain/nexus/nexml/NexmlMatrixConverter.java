package org.cipres.treebase.domain.nexus.nexml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.Constants;
import org.cipres.treebase.dao.jdbc.ContinuousMatrixElementJDBC;
import org.cipres.treebase.dao.jdbc.ContinuousMatrixJDBC;
import org.cipres.treebase.dao.jdbc.DiscreteMatrixElementJDBC;
import org.cipres.treebase.dao.jdbc.DiscreteMatrixJDBC;
import org.cipres.treebase.dao.jdbc.MatrixColumnJDBC;
import org.cipres.treebase.domain.matrix.CharSet;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.ColumnRange;
import org.cipres.treebase.domain.matrix.ContinuousChar;
import org.cipres.treebase.domain.matrix.ContinuousMatrix;
import org.cipres.treebase.domain.matrix.ContinuousMatrixElement;
import org.cipres.treebase.domain.matrix.DiscreteChar;
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
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
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

public class NexmlMatrixConverter extends NexmlObjectConverter {

	private static final int MAX_GRANULAR_NCHAR = 1000;
	private static final int MAX_GRANULAR_NTAX = 30;

	/**
	 * 
	 * @param study
	 * @param taxonLabelHome
	 */
	public NexmlMatrixConverter(Study study,TaxonLabelHome taxonLabelHome,Document document) {
		super(study,taxonLabelHome,document);	
	}	
	
	/**
	 * 
	 * @param xmlMatrix
	 * @return
	 */
	public Matrix fromXmlToTreeBase(CategoricalMatrix xmlMatrix) {
		DiscreteMatrix tbMatrix = new DiscreteMatrix();
		DiscreteMatrixJDBC matrixJDBC = new DiscreteMatrixJDBC(tbMatrix, xmlMatrix, this);
		List<MatrixColumnJDBC> columnJDBCs = new ArrayList<MatrixColumnJDBC>();
		long[] colIds = matrixJDBC.getColIDs();
		long[] rowIds = matrixJDBC.getRowIDs();		
		List<DiscreteMatrixElementJDBC> elements = new ArrayList<DiscreteMatrixElementJDBC>();
		int colIndex = 0;
		for ( org.nexml.model.Character xmlCharacter : xmlMatrix.getCharacters() ) {
			String charName = xmlCharacter.getLabel();
			DiscreteChar tbChar = new DiscreteChar();
			tbChar.setDescription(charName);
			MatrixColumnJDBC aColumnJDBC = new MatrixColumnJDBC();
			aColumnJDBC.setPhyloChar(tbChar);			
			columnJDBCs.add(aColumnJDBC);
			int rowIndex = 0;
			for ( OTU xmlOTU : xmlMatrix.getOTUs().getAllOTUs() ) {
				@SuppressWarnings("unused")
				MatrixCell<CharacterState> xmlCell = xmlMatrix.getCell(xmlOTU, xmlCharacter);
				DiscreteMatrixElementJDBC element = new DiscreteMatrixElementJDBC();
				//element.setValue(xmlCell.getValue()); // XXX nested stateset lookup song & dance here
				element.setElementOrder(colIndex);				
				element.setMatrixRowID(rowIds[rowIndex]);
				element.setMatrixColID(colIds[colIndex]);
				elements.add(element);
				rowIndex++;
			}			
			colIndex++;
		}
		DiscreteMatrixElementJDBC.batchDiscreteElements(elements, getTaxonLabelHome().getConnection());		
		return tbMatrix;		
	}
	
	/**
	 * 
	 * @param xmlMatrix
	 * @return
	 */
	public Matrix fromXmlToTreeBase(org.nexml.model.ContinuousMatrix xmlMatrix) {		
		ContinuousMatrix tbMatrix = new ContinuousMatrix();		
		ContinuousMatrixJDBC matrixJDBC = new ContinuousMatrixJDBC(tbMatrix, xmlMatrix, this);		
		List<MatrixColumnJDBC> columnJDBCs = new ArrayList<MatrixColumnJDBC>();
		long[] colIds = matrixJDBC.getColIDs();
		long[] rowIds = matrixJDBC.getRowIDs();
		List<ContinuousMatrixElementJDBC> elements = new ArrayList<ContinuousMatrixElementJDBC>();
		
		int colIndex = 0;
		for ( org.nexml.model.Character xmlCharacter : xmlMatrix.getCharacters() ) {
			String charName = xmlCharacter.getLabel();
			ContinuousChar tbChar = new ContinuousChar();
			tbChar.setDescription(charName);
			MatrixColumnJDBC aColumnJDBC = new MatrixColumnJDBC();
			aColumnJDBC.setPhyloChar(tbChar);			
			columnJDBCs.add(aColumnJDBC);
			int rowIndex = 0;
			for ( OTU xmlOTU : xmlMatrix.getOTUs().getAllOTUs() ) {
				MatrixCell<Double> xmlCell = xmlMatrix.getCell(xmlOTU, xmlCharacter);
				ContinuousMatrixElementJDBC element = new ContinuousMatrixElementJDBC();
				element.setValue(xmlCell.getValue());
				element.setElementOrder(colIndex);				
				element.setMatrixRowID(rowIds[rowIndex]);
				element.setMatrixColID(colIds[colIndex]);
				elements.add(element);
				rowIndex++;
			}
			colIndex++;
		}	
		ContinuousMatrixElementJDBC.batchContinuousElements(elements, getTaxonLabelHome().getConnection());
		return tbMatrix;
	}

	/**
	 * 
	 * @param xmlMatrix
	 * @return
	 */
	public Matrix fromXmlToTreeBase(org.nexml.model.Matrix<?> xmlMatrix) {
		OTUs xmlOTUs = xmlMatrix.getOTUs();
		Matrix tbMatrix = null;
		TaxonLabelSet tbTaxa = null;
		Long tbTaxonLabelSetID = readTreeBaseID((Annotatable) xmlOTUs);
		if ( null != tbTaxonLabelSetID ) {
			tbTaxa = getTaxonLabelHome()
				.findPersistedObjectByID(TaxonLabelSet.class, tbTaxonLabelSetID);
		}		
		if ( xmlMatrix instanceof CategoricalMatrix ) {
			tbMatrix = fromXmlToTreeBase((CategoricalMatrix) xmlMatrix);
		}
		else if ( xmlMatrix instanceof org.nexml.model.ContinuousMatrix ) {
			tbMatrix = fromXmlToTreeBase((org.nexml.model.ContinuousMatrix) xmlMatrix);
		}
		if ( null != tbMatrix ) {
			attachTreeBaseID((Annotatable) xmlMatrix, tbMatrix,Matrix.class);
			if ( null != tbTaxa ) {
				tbMatrix.setTaxa(tbTaxa);
			}
			tbMatrix.setStudy(getStudy());
			tbMatrix.setTitle(xmlMatrix.getLabel());
			tbMatrix.setPublished(false);			
		}
		return tbMatrix;		
	}
	
	/**
	 * Creates and populates characters (i.e. columns) with their annotations,
	 * and state sets, with their annotations
	 * 
	 * @param tbMatrix
	 * @return an xml matrix with empty rows
	 */
	public CategoricalMatrix fromTreeBaseToXml(StandardMatrix tbMatrix) {
		OTUs xmlOTUs = getOTUsById(tbMatrix.getTaxa().getId());
		CategoricalMatrix xmlMatrix = getDocument().createCategoricalMatrix(xmlOTUs);
		xmlMatrix.setBaseURI(mMatrixBaseURI);
		List<List<DiscreteCharState>> tbStateLabels = tbMatrix.getStateLabels();
		List<MatrixColumn> tbColumns = tbMatrix.getColumnsReadOnly();
		for ( int i = 0; i < tbColumns.size(); i++ ) {
			CharacterStateSet xmlStateSet = xmlMatrix.createCharacterStateSet();
			for ( DiscreteCharState tbState : tbStateLabels.get(i) ) {
				CharacterState xmlState = xmlStateSet.createCharacterState(tbState.getSymbol().toString());
				if ( null != tbState.getDescription() ) {
					xmlState.setLabel(tbState.getDescription());
				}	
//				if ( null != tbState.getNotes() ) {
//					((Annotatable)xmlState).addAnnotationValue("dcterms:description", Constants.DCURI, tbState.getNotes());
//				}
				attachTreeBaseID((Annotatable)xmlState,tbState,DiscreteCharState.class);
			}			
			org.nexml.model.Character xmlCharacter = xmlMatrix.createCharacter(xmlStateSet);
			PhyloChar tbCharacter = tbColumns.get(i).getCharacter();
			if ( null != tbCharacter.getDescription() ) {
				xmlCharacter.setLabel(tbCharacter.getDescription());
			}			
			attachTreeBaseID((Annotatable)xmlCharacter,tbCharacter,PhyloChar.class);
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
	public MolecularMatrix fromTreeBaseToXml(DiscreteMatrix tbMatrix) {
		OTUs xmlOTUs = getOTUsById(tbMatrix.getTaxa().getId());
		String tbDataType = tbMatrix.getDataType().getDescription();
		MolecularMatrix xmlMatrix = null;
		CharacterStateSet xmlStateSet = null;
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
		xmlMatrix.setBaseURI(mMatrixBaseURI);
		for ( MatrixColumn tbColumn : tbMatrix.getColumnsReadOnly() ) {
			org.nexml.model.Character xmlCharacter = xmlMatrix.createCharacter(xmlStateSet);
			PhyloChar tbCharacter = tbColumn.getCharacter();
			if ( null != tbCharacter.getDescription() ) {
				xmlCharacter.setLabel(tbCharacter.getDescription());
			}
			if ( null != tbCharacter.getDescription() && ! tbCharacter.getDescription().equals(tbDataType) ) {
				((Annotatable)xmlCharacter).addAnnotationValue("dcterms:description", Constants.DCTermsURI, tbCharacter.getDescription());
			}			
			if ( null != tbCharacter.getId() && tbCharacter.getId() != 2 ) { // XXX is PhyloChar.id 2 some sort of magic number?
				attachTreeBaseID((Annotatable)xmlCharacter,tbCharacter,PhyloChar.class);
			}
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
	public org.nexml.model.ContinuousMatrix fromTreeBaseToXml(ContinuousMatrix tbMatrix) {
		OTUs xmlOTUs = getOTUsById(tbMatrix.getTaxa().getId());
		org.nexml.model.ContinuousMatrix xmlMatrix = getDocument().createContinuousMatrix(xmlOTUs);
		xmlMatrix.setBaseURI(mMatrixBaseURI);
		for ( MatrixColumn tbColumn : tbMatrix.getColumnsReadOnly() ) {
			org.nexml.model.Character xmlCharacter = xmlMatrix.createCharacter();
			PhyloChar tbCharacter = tbColumn.getCharacter();
			if ( null != tbCharacter.getDescription() ) {
				xmlCharacter.setLabel(tbCharacter.getDescription());
				((Annotatable)xmlCharacter).addAnnotationValue("dcterms:description", Constants.DCTermsURI, tbCharacter.getDescription());
			}			
			attachTreeBaseID((Annotatable)xmlCharacter,tbCharacter,PhyloChar.class);
			
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
	public org.nexml.model.Matrix<?> fromTreeBaseToXml(CharacterMatrix tbMatrix) {
		org.nexml.model.Matrix<?> xmlMatrix = null;
		if ( tbMatrix instanceof DiscreteMatrix ) {
			if ( tbMatrix.getDataType().getDescription().equals(MatrixDataType.MATRIX_DATATYPE_STANDARD) ) {
				xmlMatrix = fromTreeBaseToXml((StandardMatrix) tbMatrix);
			}
			else {
				xmlMatrix = fromTreeBaseToXml((DiscreteMatrix) tbMatrix);				
			}
			populateXmlMatrix((org.nexml.model.Matrix<CharacterState>)xmlMatrix,(DiscreteMatrix)tbMatrix);
		}
		else if ( tbMatrix instanceof ContinuousMatrix ) {
			xmlMatrix = fromTreeBaseToXml((ContinuousMatrix) tbMatrix);			
			populateXmlMatrix((org.nexml.model.ContinuousMatrix)xmlMatrix,(ContinuousMatrix)tbMatrix);
		}
		xmlMatrix.setLabel(tbMatrix.getTitle());
		attachTreeBaseID((Annotatable)xmlMatrix, tbMatrix,Matrix.class);
		String tb1MatrixID = tbMatrix.getTB1MatrixID();
		if ( null != tb1MatrixID ) {
			((Annotatable)xmlMatrix).addAnnotationValue("tb:identifier.matrix.tb1", Constants.TBTermsURI, tb1MatrixID);
		}
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
				for ( MatrixElement tbCell : tbRow.getElements() ) {
					org.nexml.model.Character xmlCharacter = characterList.get(charIndex);
					MatrixCell<CharacterState> xmlCell = xmlMatrix.getCell(xmlOTU, xmlCharacter);				
					DiscreteCharState tbState = ((DiscreteMatrixElement)tbCell).getCharState();
					String tbSymbolString = ( null == tbState ) ? "?" : tbState.getSymbol().toString();
					CharacterState xmlState = xmlCharacter.getCharacterStateSet().lookupCharacterStateBySymbol(tbSymbolString);								
					xmlCell.setValue(xmlState);								
					attachTreeBaseID((Annotatable)xmlCell,tbCell,DiscreteMatrixElement.class);
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
				String seq = tbRow.buildElementAsString();
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
