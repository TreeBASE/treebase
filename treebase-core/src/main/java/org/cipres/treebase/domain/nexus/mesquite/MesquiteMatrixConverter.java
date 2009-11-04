
package org.cipres.treebase.domain.nexus.mesquite;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import mesquite.lib.BitsSpecsSet;
import mesquite.lib.Listable;
import mesquite.lib.ListableVector;
import mesquite.lib.MesquiteInteger;
import mesquite.lib.MesquiteNumber;
import mesquite.lib.MesquiteProject;
import mesquite.lib.NumSpecsSet;
import mesquite.lib.NumberArray;
import mesquite.lib.SpecsSet;
import mesquite.lib.SpecsSetVector;
import mesquite.lib.StringUtil;
import mesquite.lib.Taxa;
import mesquite.lib.TaxaInclusionSet;
import mesquite.lib.TaxaPartition;
import mesquite.lib.characters.CharInclusionSet;
import mesquite.lib.characters.CharSelectionSet;
import mesquite.lib.characters.CharWeightSet;
import mesquite.lib.characters.CharacterData;
import mesquite.lib.characters.CharacterPartition;
import mesquite.lib.characters.CharacterStates;
import mesquite.lib.characters.CharactersGroup;
import mesquite.lib.characters.CodonPositionsSet;
import mesquite.parsimony.lib.ParsimonyModelSet;

import org.apache.log4j.Logger;

import org.cipres.treebase.dao.jdbc.MatrixJDBC;
import org.cipres.treebase.domain.matrix.CharGroup;
import org.cipres.treebase.domain.matrix.CharPartition;
import org.cipres.treebase.domain.matrix.CharWeight;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.CodonPositionSet;
import org.cipres.treebase.domain.matrix.ColumnRange;
import org.cipres.treebase.domain.matrix.IntegerCharWeight;
import org.cipres.treebase.domain.matrix.ItemDefinitionHome;
import org.cipres.treebase.domain.matrix.MatrixDataType;
import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.RealCharWeight;
import org.cipres.treebase.domain.matrix.TypeSet;
import org.cipres.treebase.domain.matrix.UserDefinedCharSet;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;

/**
 * MesquiteMatrixConverter.java
 * 
 * Created on Feb 13, 2007
 * 
 * @author Jin Ruan
 * 
 */
public abstract class MesquiteMatrixConverter {
	private static final Logger LOGGER = Logger.getLogger(MesquiteMatrixConverter.class);

	// Hard coded String values defined in Mesquite code:
	// defined in CategoricalData.java:
	private static final String MESQUITE_CATEGORICAL_TYPE = "Standard Categorical Data";
	// defined in DNAData.java:
	private static final String MESQUITE_DNA_TYPE = "DNA Data";
	// defined in RNAData.java:
	private static final String MESQUITE_RNA_TYPE = "RNA Data";
	// defined in ProteinData.java:
	private static final String MESQUITE_PROTEIN_TYPE = "Protein Data";

	// defined in ContinuousData.java:
	private static final String MESQUITE_CONTINUOUS_TYPE = "Continuous Data";
	// defined in GeographicData.java:
	private static final String MESQUITE_GEOGRAPHIC_TYPE = "Geographic Data";

	private MatrixDataType mMatrixDataType;

	/**
	 * Constructor.
	 */
	public MesquiteMatrixConverter() {
		super();
	}

	/**
	 * Convert all matrices in a Mesquite project and add them to the NexusDataSet object.
	 * 
	 * @param pMesqProject
	 * @param pDataSet
	 * @param pNexusFileName
	 * @return
	 */
	public static void convertMatrices(
		MesquiteProject pMesqProject,
		NexusDataSet pDataSet,
		String pNexusFileName,
		MatrixDataTypeHome pDataTypeHome,
		ItemDefinitionHome pItemDefHome) {

		if (pMesqProject == null || pDataSet == null) {
			return;
		}

		ListableVector matrices = pMesqProject.getCharacterMatrices();
		mesquite.lib.characters.CharacterData data;
		for (int i = 0; i < matrices.size(); i++) {
			data = (mesquite.lib.characters.CharacterData) matrices.elementAt(i);
			if (!data.isDoomed()) {
				// GOOD matrix
				if (LOGGER.isDebugEnabled()) {
					LOGGER
						.debug("convertMatrices: matrix: " + data.getName() + " -" + data.getNumChars()); //$NON-NLS-1$
				}

				MesquiteMatrixConverter converter = getConverter(data, pDataTypeHome, pItemDefHome);
				MatrixJDBC matrixJDBC = converter.convertMatrix(data, pDataSet);

				CharacterMatrix m = matrixJDBC.getCharacterMatrix();
				m.setNexusFileName(pNexusFileName);
				pDataSet.getMatrices().add(m);

				// setup jdbc dataset for direct jdbc batch inserts:
				// pDataSet.getDataJDBC().addData(m, data, converter);
				pDataSet.getDataJDBC().getMatrixJDBCList().add(matrixJDBC);
			}
		}

	}

	/**
	 * First convert matrixElements for saving to database using direct JDBC, then batch insert the
	 * elements to database.
	 * 
	 * For memory considerations, the large matrix elementJDBC list is returned, while the compound
	 * elements are created and stored inside the MatrixJDBC.
	 * 
	 * @param pMatrixJDBC
	 * @param pCon
	 * @return
	 */
	public abstract void processMatrixElements(MatrixJDBC pMatrixJDBC, Connection pCon);

	/**
	 * 
	 * @param pCipresDatatype
	 * @param pDataTypeHome
	 * @return
	 */
	private static MesquiteMatrixConverter getConverter(
		CharacterData pData,
		MatrixDataTypeHome pDataTypeHome,
		ItemDefinitionHome pItemDefHome) {

		String mesqDataType = pData.getDataTypeName();

		MesquiteMatrixConverter converter = null;
		if (mesqDataType == MESQUITE_DNA_TYPE) {
			converter = new MesquiteStandardMatrixConverter();

			MatrixDataType dataType = pDataTypeHome
				.findByDescription(MatrixDataType.MATRIX_DATATYPE_DNA);
			converter.setMatrixDataType(dataType);
		} else if (mesqDataType == MESQUITE_RNA_TYPE) {
			converter = new MesquiteStandardMatrixConverter();

			MatrixDataType dataType = pDataTypeHome
				.findByDescription(MatrixDataType.MATRIX_DATATYPE_RNA);
			converter.setMatrixDataType(dataType);
		} else if (mesqDataType == MESQUITE_PROTEIN_TYPE) {
			converter = new MesquiteStandardMatrixConverter();

			MatrixDataType dataType = pDataTypeHome
				.findByDescription(MatrixDataType.MATRIX_DATATYPE_PROTEIN);
			converter.setMatrixDataType(dataType);
			// } else if (mesqDataType == DiscreteDatatypes._CODON_DATATYPE) {
			// converter = new MesquiteStandardMatrixConverter();
			//
			// // TODO: per Mark H. CODON should be depreciated in NCL.
			// MatrixDataType dataType = pDataTypeHome
			// .findByDescription(MatrixDataType.MATRIX_DATATYPE_DNA);
			// converter.setMatrixDataType(dataType);
		} else if (mesqDataType == MESQUITE_CATEGORICAL_TYPE) {
			converter = new MesquiteStandardMatrixConverter();

			MatrixDataType dataType = pDataTypeHome
				.findByDescription(MatrixDataType.MATRIX_DATATYPE_STANDARD);
			converter.setMatrixDataType(dataType);
		} else if (mesqDataType == MESQUITE_CONTINUOUS_TYPE) {
			MesquiteContinuousMatrixConverter continousConverter = new MesquiteContinuousMatrixConverter();
			continousConverter.setItemDefinitionHome(pItemDefHome);

			converter = continousConverter;
			MatrixDataType dataType = pDataTypeHome
				.findByDescription(MatrixDataType.MATRIX_DATATYPE_CONTINUOUS);
			converter.setMatrixDataType(dataType);
		} else {
			// TODO: other types: nucleotide, distance, ...

			// a missing datatype means use standard type:
			converter = new MesquiteStandardMatrixConverter();

			MatrixDataType dataType = pDataTypeHome
				.findByDescription(MatrixDataType.MATRIX_DATATYPE_STANDARD);
			converter.setMatrixDataType(dataType);
		}

		return converter;
	}

	/**
	 * Return the MatrixDataType field.
	 * 
	 * @return MatrixDataType mMatrixDataType
	 */
	protected MatrixDataType getMatrixDataType() {
		return mMatrixDataType;
	}

	/**
	 * Set the MatrixDataType field.
	 */
	private void setMatrixDataType(MatrixDataType pNewMatrixDataType) {
		mMatrixDataType = pNewMatrixDataType;
	}

	/**
	 * 
	 * @param pData
	 * @return
	 */
	private MatrixJDBC convertMatrix(CharacterData pMesqMatrix, NexusDataSet pDataSet) {
		MatrixJDBC matrixJDBC = createMatrix(pMesqMatrix);
		CharacterMatrix m = matrixJDBC.getCharacterMatrix();

		// matrix properties:
		m.setTitle(pMesqMatrix.getName());
		m.setMissingSymbol(pMesqMatrix.getUnassignedSymbol());
		m.setGapSymbol(pMesqMatrix.getInapplicableSymbol());
		m.setDataType(getMatrixDataType());

		Taxa mesqTaxa = pMesqMatrix.getTaxa();
		TaxonLabelSet tlSet = pDataSet.getTaxonLabelSet(mesqTaxa);
		m.setTaxa(tlSet);
		List<TaxonLabel> taxonLabels = tlSet.getTaxonLabelsReadOnly();

		// add rows
		for (int rowIndex = 0; rowIndex < taxonLabels.size(); rowIndex++) {

			TaxonLabel label = taxonLabels.get(rowIndex);

			MatrixRow aRow = new MatrixRow();
			aRow.setTaxonLabel(label);
			m.addRow(aRow);

			// add elements for a row: replaced by the direct jdbc version
			// addRowElements(aRow, rowIndex, pMesqMatrix);
		}

		// convert associated sets
		Vector allSets = pMesqMatrix.getSpecSetsVectorVector();
		// Vector sets2 = pMesqMatrix.get
		// TODO: Some of the mesquite sets are not converted:
		// mesquite.lib.characters.ProbabilityModelSet
		// mesquite.parsimony.lib.ParsimonyModelSet
		// mesquite.molec.lib.GenCodeModelSet

		if (allSets != null) {
			Iterator<SpecsSetVector> specsSetIter = allSets.iterator();
			while (specsSetIter.hasNext()) {
				SpecsSetVector aSetVector = specsSetIter.next();

				Class setType = aSetVector.getType();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("SpecsSetVector class=" + setType + " size=" + aSetVector.size()); //$NON-NLS-1$
				}

				if (CharWeightSet.class == setType) {
					convertCharWeightSet(pMesqMatrix, aSetVector, m);
					// } else if (CodonPositionsSet.class == aSetVector.getClass()) {
					// convertCodonPositionsSet(pMesqMatrix, aSetVector, m);
					// } else if (CodonPositionsSet.class == aSetVector.getClass()) {
					// convertCodonPositionsSet(pMesqMatrix, aSetVector, m);
				} else if (ParsimonyModelSet.class == setType) {
					// FIXME: convertParsimonyModelSet(pMesqMatrix, aSetVector, m);
				} else if (CharSelectionSet.class == setType) {
					convertCharSet(pMesqMatrix, aSetVector, m);
				} else if (TaxaInclusionSet.class == setType) {
					convertTaxaInclusionSet(pMesqMatrix, aSetVector, m);
				} else if (CharInclusionSet.class == setType) {
					convertCharInclusionSet(pMesqMatrix, aSetVector, m);
				} else if (TaxaPartition.class == setType) {
					convertTaxaPartition(pMesqMatrix, aSetVector, m);
				} else if (CharacterPartition.class == setType) {
					convertCharacterPartition(pMesqMatrix, aSetVector, m);
				} else if (CodonPositionsSet.class == setType) {
					convertCodonPositionsSet(pMesqMatrix, aSetVector, m);
				} else {
					LOGGER
						.info("Unhandled matrix associated set. Class=" + setType + " size=" + aSetVector.size()); //$NON-NLS-1$
				}
			}
		}
		return matrixJDBC;
	}

	/**
	 * 
	 * @param pMesqMatrix
	 * @param pSetVector
	 * @param pMatrix
	 */
	private void convertParsimonyModelSet(
		CharacterData pMesqMatrix,
		SpecsSetVector pSetVector,
		CharacterMatrix pMatrix) {

		ParsimonyModelSet defaultModelSet = (ParsimonyModelSet) pMesqMatrix
			.getCurrentSpecsSet(ParsimonyModelSet.class);
		boolean foundDefault = false;
		for (int i = 0; i < pSetVector.size(); i++) {
			SpecsSet ss = (SpecsSet) pSetVector.elementAt(i);
			if (ss instanceof ParsimonyModelSet) {
				ParsimonyModelSet pmSet = (ParsimonyModelSet) ss;

				TypeSet typeSet = createTypeSet(pMesqMatrix, pmSet, pMatrix);

				if (pmSet == defaultModelSet) {
					foundDefault = true;
					pMatrix.setDefaultTypeSet(typeSet);
				}
				pMatrix.getTypeSets().add(typeSet);
			}
		}

		if (!foundDefault && defaultModelSet != null) {
			TypeSet typeSet = createTypeSet(pMesqMatrix, defaultModelSet, pMatrix);

			pMatrix.setDefaultTypeSet(typeSet);

			// Default typeset *is* in the above weight set collection.
			pMatrix.getTypeSets().add(typeSet);
		}
	}

	/**
	 * 
	 * @param pMesqMatrix
	 * @param pPmSet
	 * @param pMatrix
	 * @return
	 */
	private TypeSet createTypeSet(
		CharacterData pMesqMatrix,
		ParsimonyModelSet pPmSet,
		CharacterMatrix pMatrix) {

		TypeSet typeSet = new TypeSet();
		// typeSet.setTitle(pPmSet.getName3());
		//
		// int numChars = pMesqMatrix.getNumChars();
		// Set<CharWeight> tbWeights = new HashSet<CharWeight>();
		//
		// // create one char weight for each columnRangeConvertHelper
		// Collection<ColumnRangeConvertHelper> colRangeHelpers = decodeNumSpecsSet(pWtSet,
		// numChars);
		// for (ColumnRangeConvertHelper helper : colRangeHelpers) {
		// MesquiteNumber mesNum = helper.getNumber();
		//
		// CharWeight charWeight = null;
		// if (mesNum.getValueClass() == MesquiteNumber.INT) {
		// charWeight = new IntegerCharWeight(mesNum.getIntValue());
		// } else {
		// charWeight = new RealCharWeight(mesNum.getDoubleValue());
		// }
		//
		// charWeight.setWeightColumns(helper.getColRanges());
		//
		// tbWeights.add(charWeight);
		// }
		//
		// tbWeightSet.setCharWeights(tbWeights);
		//
		return typeSet;
		// FIXME: createTypeSet
	}

	/**
	 * 
	 * @param pMesqMatrix
	 * @param pSetVector
	 * @param pM
	 */
	private void convertCharSet(
		CharacterData pMesqMatrix,
		SpecsSetVector pSetVector,
		CharacterMatrix pMatrix) {

		for (int i = 0; i < pSetVector.size(); i++) {
			SpecsSet ss = (SpecsSet) pSetVector.elementAt(i);
			if (ss instanceof CharSelectionSet) {
				CharSelectionSet charSet = (CharSelectionSet) ss;
				UserDefinedCharSet tbCharSet = createCharSet(pMesqMatrix, charSet, pMatrix);

				pMatrix.getCharSets().add(tbCharSet);
			}
		}

		// TODO: for now default charSet is not in the above set collection.
		// CharSelectionSet defaultCharSet = (CharSelectionSet) pMesqMatrix
		// .getCurrentSpecsSet(CharSelectionSet.class);
		// if (defaultCharSet != null) {
		// CharSet tbDefaultCharSet = createCharSet(pMesqMatrix, defaultCharSet, pMatrix);
		//
		// pMatrix.setDefault(tbWeightSet);
		// }

	}

	/**
	 * 
	 * @param pMesqMatrix
	 * @param pCharSet
	 * @param pMatrix
	 * @return
	 */
	private UserDefinedCharSet createCharSet(
		CharacterData pMesqMatrix,
		CharSelectionSet pCharSet,
		CharacterMatrix pMatrix) {

		UserDefinedCharSet tbCharSet = new UserDefinedCharSet();
		tbCharSet.setTitle(pCharSet.getName());
		tbCharSet.setMatrix(pMatrix);

		int numChars = pMesqMatrix.getNumChars();
		// Set<CharWeight> tbWeights = new HashSet<CharWeight>();

		// create one char weight for each columnRangeConvertHelper
		ColumnRangeConvertHelper colRangeHelper = decodeBitsSpecsSet(pCharSet, numChars);
		tbCharSet.setColumns(colRangeHelper.getColRanges());

		if (LOGGER.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(tbCharSet.getTitle()).append(" = ");
			for (ColumnRange aRange : tbCharSet.getColumns()) {
				aRange.appendRange(sb).append(" ");
			}
			LOGGER.debug(sb.toString());
		}

		return tbCharSet;
	}

	/**
	 * 
	 * @param pMesqMatrix
	 * @param pSetVector
	 * @param pM
	 */
	private void convertTaxaInclusionSet(
		CharacterData pMesqMatrix,
		SpecsSetVector pSetVector,
		CharacterMatrix pM) {
	// FIXME: convertTaxaInclusionSet

	}

	/**
	 * 
	 * @param pMesqMatrix
	 * @param pSetVector
	 * @param pM
	 */
	private void convertCharInclusionSet(
		CharacterData pMesqMatrix,
		SpecsSetVector pSetVector,
		CharacterMatrix pM) {
	// FIXME: convertCharInclusionSet

	}

	/**
	 * 
	 * @param pMesqMatrix
	 * @param pSetVector
	 * @param pM
	 */
	private void convertTaxaPartition(
		CharacterData pMesqMatrix,
		SpecsSetVector pSetVector,
		CharacterMatrix pM) {
	// FIXME: convertTaxaPartition

	}

	/**
	 * 
	 * @param pMesqMatrix
	 * @param pSetVector
	 * @param pM
	 */
	private void convertCharacterPartition(
		CharacterData pMesqMatrix,
		SpecsSetVector pSetVector,
		CharacterMatrix pMatrix) {

		for (int i = 0; i < pSetVector.size(); i++) {
			SpecsSet ss = (SpecsSet) pSetVector.elementAt(i);
			if (ss instanceof CharacterPartition) {
				CharacterPartition partition = (CharacterPartition) ss;
				// org.cipres.treebase.domain.matrix.CharWeightSet tbWeightSet = create(
				// pMesqMatrix,
				// wtSet,
				// pMatrix);
				CharPartition tbPartition = createPartition(pMesqMatrix, partition);

				pMatrix.getCharPartitions().add(tbPartition);
			}
		}

		CharacterPartition defaultPartition = (CharacterPartition) pMesqMatrix
			.getCurrentSpecsSet(CharacterPartition.class);
		if (defaultPartition != null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("TODO: add support for default Char Partition.");
			}
			// TODO: add support for default char partition, It is not in the
			// nexus paper but mesquite handles it.

			// CharPartition tbDefaultPartition = createPartition(
			// pMesqMatrix,
			// defaultPartition);
			//
			// pMatrix.setDefaultPartition(tbDefaultPartition);
			//
			// // Default weightset *is* in the above weight set collection.
			// pMatrix.getCharPartitions().add(tbDefaultPartition);
		}

	}

	/**
	 * 
	 * @param pMesqMatrix
	 * @param partition
	 * @return
	 */
	private CharPartition createPartition(CharacterData pMesqMatrix, CharacterPartition partition) {
		CharPartition tbPartition = new CharPartition();

		tbPartition.setTitle(partition.getName());

		// int numChars = pMesqMatrix.getNumChars();
		// Collection<CharGroup> tbGroups = new ArrayList<CharGroup>();

		CharactersGroup[] parts = partition.getGroups();
		// boolean firstTime = true;

		if (parts != null) {
			for (int j = 0; j < parts.length; j++) {
				ColumnRangeConvertHelper aRangeHelper = getListOfMatches((Listable[]) partition
					.getProperties(), parts[j], CharacterStates.toExternal(0));
				if (aRangeHelper != null) {
					CharGroup aGroup = new CharGroup();
					aGroup.setTitle(StringUtil.tokenize(parts[j].getName()));
					aGroup.setCharColumns(aRangeHelper.getColRanges());

					tbPartition.addCharGroup(aGroup);
				}
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(tbPartition.getNexusString());
		}
		return tbPartition;
	}

	/**
	 * returns a string listing the elements of the array that are the passed object. In the format
	 * of NEXUS character, taxa lists (e.g., "1- 3 6 201-455". The offset is what the first element
	 * is to be numbered (e.g., 0 or 1)
	 * 
	 * Modified according to Mesquite code ListableVector.getListOfMatches().
	 * 
	 */
	private ColumnRangeConvertHelper getListOfMatches(Listable[] listArray, Object obj, int offset) {
		int continuing = 0;

		ColumnRangeConvertHelper columnRangeHelper = new ColumnRangeConvertHelper();

		// boolean found = false;
		// int lastWritten = -1;
		ColumnRange aRange = null; // new ColumnRange();

		for (int i = 0; i < listArray.length; i++) {
			if (listArray[i] == obj) {
				// found = true;
				if (continuing == 0) {
					// discountinue
					aRange = new ColumnRange();
					aRange.setStartColIndex(i - 1 + offset);
					// s += " " + (i + offset);
					// lastWritten = i;
					continuing = 1;
				} else if (continuing == 1) {
					// s += " - ";
					// continue
					// continuing = 2;
				}
			} else if (aRange != null) {
				// found the last index:
				aRange.setEndColIndex(i - 2 + offset);
				columnRangeHelper.getColRanges().add(aRange);
				aRange = null;
				continuing = 0;

				// if (lastWritten != i - 1) {
				// aRange.setEndColIndex(i - 1 + offset);
				//
				// // s += " " + (i-1 + offset);
				// lastWritten = i - 1;
				// } else {
				// lastWritten = -1;
				// }
				// continuing = 0;
			}
		}

		if (aRange != null) {
			aRange.setEndColIndex(listArray.length - 2 + offset);
			columnRangeHelper.getColRanges().add(aRange);
			aRange = null;
		}

		if (LOGGER.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(columnRangeHelper.getNumber()).append(":");
			for (ColumnRange r : columnRangeHelper.getColRanges()) {
				r.appendRange(sb).append(" ");
				// sb.append(r.toString()).append(" ");
			}
			LOGGER.debug(sb.toString());
		}

		return columnRangeHelper;
	}

	/**
	 * 
	 * @param pMesqMatrix
	 * @param pSetVector
	 * @param pM
	 */
	private void convertCodonPositionsSet(
		CharacterData pMesqMatrix,
		SpecsSetVector pSetVector,
		CharacterMatrix pMatrix) {

		// see mesquite.lists.CharListCodonPos.CharListCodonPos

		for (int i = 0; i < pSetVector.size(); i++) {
			SpecsSet ss = (SpecsSet) pSetVector.elementAt(i);
			if (ss instanceof CodonPositionsSet) {
				CodonPositionsSet cpSet = (CodonPositionsSet) ss;
				CodonPositionSet tbCodonPosSet = createCodonPosSet(pMesqMatrix, cpSet, pMatrix);

				pMatrix.getCodonPosSets().add(tbCodonPosSet);
			}
		}

		CodonPositionsSet defaultCodonPosSet = (CodonPositionsSet) pMesqMatrix
			.getCurrentSpecsSet(CodonPositionsSet.class);
		if (defaultCodonPosSet != null) {
			CodonPositionSet tbCodonPosSet = createCodonPosSet(
				pMesqMatrix,
				defaultCodonPosSet,
				pMatrix);

			pMatrix.setDefaultCodonPosSet(tbCodonPosSet);

			// Default codonPosSet *is* in the above CodonPos set collection.
			pMatrix.getCodonPosSets().add(tbCodonPosSet);
		}

	}

	/**
	 * Convert character weight set.
	 * 
	 * @param pMesqMatrix
	 * @param pSetVector
	 * @param pMatrix
	 */
	private void convertCharWeightSet(
		CharacterData pMesqMatrix,
		SpecsSetVector pSetVector,
		CharacterMatrix pMatrix) {

		for (int i = 0; i < pSetVector.size(); i++) {
			SpecsSet ss = (SpecsSet) pSetVector.elementAt(i);
			if (ss instanceof CharWeightSet) {
				CharWeightSet wtSet = (CharWeightSet) ss;
				org.cipres.treebase.domain.matrix.CharWeightSet tbWeightSet = createWeightSet(
					pMesqMatrix,
					wtSet,
					pMatrix);

				pMatrix.getWeightSets().add(tbWeightSet);
			}
		}

		CharWeightSet defaultWeightSet = (CharWeightSet) pMesqMatrix
			.getCurrentSpecsSet(CharWeightSet.class);
		if (defaultWeightSet != null) {
			org.cipres.treebase.domain.matrix.CharWeightSet tbWeightSet = createWeightSet(
				pMesqMatrix,
				defaultWeightSet,
				pMatrix);

			pMatrix.setDefaultWeightSet(tbWeightSet);

			// Default weightset *is* in the above weight set collection.
			pMatrix.getWeightSets().add(tbWeightSet);
		}

	}

	/**
	 * 
	 * @param pMesqMatrix
	 * @param pWtSet
	 * @param pTBMatrix
	 * @return
	 */
	private org.cipres.treebase.domain.matrix.CharWeightSet createWeightSet(
		CharacterData pMesqMatrix,
		CharWeightSet pWtSet,
		CharacterMatrix pTBMatrix) {

		org.cipres.treebase.domain.matrix.CharWeightSet tbWeightSet = new org.cipres.treebase.domain.matrix.CharWeightSet();
		tbWeightSet.setTitle(pWtSet.getName());

		int numChars = pMesqMatrix.getNumChars();
		Set<CharWeight> tbWeights = new HashSet<CharWeight>();

		// create one char weight for each columnRangeConvertHelper
		Collection<ColumnRangeConvertHelper> colRangeHelpers = decodeNumSpecsSet(pWtSet, numChars);
		for (ColumnRangeConvertHelper helper : colRangeHelpers) {
			MesquiteNumber mesNum = helper.getNumber();

			CharWeight charWeight = null;
			if (mesNum.getValueClass() == MesquiteNumber.INT) {
				charWeight = new IntegerCharWeight(mesNum.getIntValue());
			} else {
				charWeight = new RealCharWeight(mesNum.getDoubleValue());
			}

			charWeight.setWeightColumns(helper.getColRanges());

			tbWeights.add(charWeight);
		}

		tbWeightSet.setCharWeights(tbWeights);

		return tbWeightSet;
	}

	/**
	 * 
	 * @param pMesqMatrix
	 * @param pCodonPosSet
	 * @param pMatrix
	 * @return
	 */
	private CodonPositionSet createCodonPosSet(
		CharacterData pMesqMatrix,
		CodonPositionsSet pCodonPosSet,
		CharacterMatrix pMatrix) {

		// See ManageCodonPositionset.nexusStringForSpecsSet
		// Also see DNAData.getCodonsAsNexusCharSets

		CodonPositionSet tbCodonSet = new CodonPositionSet();
		tbCodonSet.setTitle(pCodonPosSet.getName());

		int numChars = pMesqMatrix.getNumChars();

		// create one collection of columnRange for each columnRangeConvertHelper
		decodeNumSpecsSetForCodonPosSet(tbCodonSet, pCodonPosSet, numChars);

		return tbCodonSet;

	}

	/**
	 * Return a collection of column range converter helper for a BitsSpecsSet.
	 * 
	 * Note: this method is based on the implementation in Mesquite code
	 * ManageCharsets.nexusStringForSpecsSet()
	 * 
	 * @param pBitsSpecsSet
	 * @param pNumChar
	 * @return
	 */
	private ColumnRangeConvertHelper decodeBitsSpecsSet(BitsSpecsSet pBitsSpecsSet, int pNumChar) {

		ColumnRangeConvertHelper aHelper = new ColumnRangeConvertHelper();

		if (pBitsSpecsSet != null) {

			ColumnRange range = null;

			int continuing = 0;
			for (int ic = 0; ic < pNumChar; ic++) {

				// 
				if (pBitsSpecsSet.isSelected(ic)) {
					if (continuing == 0) {
						// discontinue
						range = new ColumnRange();
						range.setStartColIndex(CharacterStates.toExternal(ic) - 1);

						continuing = 1;
					} else if (continuing == 1) {
						// continue..
					}
				} else if (range != null) {
					// found the last index:
					range.setEndColIndex(CharacterStates.toExternal(ic - 1) - 1);
					aHelper.getColRanges().add(range);
					range = null;
					continuing = 0;
				}
			}

			if (range != null) {
				range.setEndColIndex(CharacterStates.toExternal(pNumChar - 1) - 1);
				aHelper.getColRanges().add(range);
			}
		}
		return aHelper;
	}

	/**
	 * Return a collection of column range converter helper for a NumSpecsSet.
	 * 
	 * Note: this method is based on the implementation in Mesquite code
	 * ManageCharWeights.nexusStringForSpecsSet()
	 * 
	 * @param pNumSpecsSet
	 * @param pNumChar
	 * @return
	 */
	private Collection<ColumnRangeConvertHelper> decodeNumSpecsSet(
		NumSpecsSet pNumSpecsSet,
		int pNumChar) {

		Collection<ColumnRangeConvertHelper> colRangeHelpers = new ArrayList<ColumnRangeConvertHelper>();

		if (pNumSpecsSet != null) {

			// an array to keep track of distinct numbers are already converted
			NumberArray usedNumbers = new NumberArray(pNumChar);
			usedNumbers.deassignArray();

			for (int iw = 0; iw < pNumChar; iw++) {
				MesquiteNumber aNumber = new MesquiteNumber();
				MesquiteNumber secondNumber = new MesquiteNumber();

				pNumSpecsSet.placeValue(iw, aNumber);

				// bypass this char if the number is already converted
				if (usedNumbers.findValue(aNumber) < 0) {
					int continuing = 1;

					ColumnRangeConvertHelper aHelper = new ColumnRangeConvertHelper();
					aHelper.setNumber(aNumber);

					int startIndex = CharacterStates.toExternal(iw);
					ColumnRange range = new ColumnRange();
					range.setStartColIndex(startIndex - 1);

					for (int ic = iw + 1; ic < pNumChar; ic = ic + 1) {
						pNumSpecsSet.placeValue(ic, secondNumber);
						if (secondNumber.equals(aNumber)) {
							if (continuing == 0) {
								// discontinue
								range = new ColumnRange();
								range.setStartColIndex(ic);

								continuing = 1;
							} else if (continuing == 1) {
								// continuing
							}
						} else if (range != null) {
							// found the last index:
							range.setEndColIndex(ic - 1);
							aHelper.getColRanges().add(range);
							range = null;
							continuing = 0;
						}
					}

					if (range != null) {
						range.setEndColIndex(pNumChar - 1);
						aHelper.getColRanges().add(range);
					}

					colRangeHelpers.add(aHelper);

					// save the converted number
					usedNumbers.setOpenValue(aNumber);

					if (LOGGER.isDebugEnabled()) {
						StringBuilder sb = new StringBuilder();
						sb.append(aHelper.getNumber()).append(":");
						for (ColumnRange aRange : aHelper.getColRanges()) {
							aRange.appendRange(sb).append(" ");
						}
						LOGGER.debug(sb.toString());
					}
				}
			}
		}
		return colRangeHelpers;
	}

	/**
	 * Set the codonPosSet after decode a collection of column range converter helper for a
	 * NumSpecsSet.
	 * 
	 * Note: this method is a special case for the decodeNumSpecsSet(), the range interval for
	 * Noncoding = 1, for posSet 1, 2, 3 = 3.
	 * 
	 * @param pNumSpecsSet
	 * @param pNumChar
	 * @return
	 */
	private void decodeNumSpecsSetForCodonPosSet(
		CodonPositionSet pCodonPosSet,
		NumSpecsSet pNumSpecsSet,
		int pNumChar) {

		//Based on ManageCodonPositions.nexusStringForSpecsSet
		
		if (pNumSpecsSet != null) {

			CodonPositionsSet codonPossSet = (CodonPositionsSet) pNumSpecsSet;
			int unassignedPosition = 4;
			MesquiteNumber posIndex = new MesquiteNumber();

			boolean someValues = false;

			int interval = 1;

			for (int iw = 0; iw < 5; iw++) {
				posIndex.setValue(iw);
				int continuing = 0;

				ColumnRangeConvertHelper aHelper = new ColumnRangeConvertHelper();
				aHelper.setNumber(posIndex);

				if (iw == 0) {
					interval = 1;
				} else {
					interval = 3;
				}

				someValues = false;

				int lastWritten = -1;
				ColumnRange range = null;
				for (int ic = 0; ic < pNumChar; ic++) {
					
					//for noncoding ranges, the interval is 3, need add 2: 
					if (continuing > 0 && iw > 0 && ic < pNumChar -2) {
						ic = ic +2;
					}
					
					if (codonPossSet.equals(ic, posIndex)
						|| (codonPossSet.getInt(ic) == MesquiteInteger.unassigned && (iw == unassignedPosition))) {
						
						if (continuing == 0) {
							lastWritten = ic;

							range = new ColumnRange();
							range.setStartColIndex(ic);
							if (interval > 1) {
								range.setRepeatInterval(interval);
							}
							aHelper.getColRanges().add(range);

							continuing = 1;
							someValues = true;
						} else if (continuing == 1) {
							continuing = 2;
							someValues = true;
						}
					} else if (continuing > 0) {
						if (lastWritten != ic - 1) {
							range.setEndColIndex(ic - 1);

							lastWritten = ic - 1;
							someValues = true;
						} else {
							// start index and end index should be the same:
							range.setEndColIndex(ic - 1);

							lastWritten = -1;
						}
						continuing = 0;
					}

				}

				if (continuing > 0) {
					range.setEndColIndex(pNumChar - 1);

					someValues = true;
				}

				// the column ranges are not null:
				if (someValues) {
					switch (iw) {
						case 0:
							pCodonPosSet.setNonCodingColumns(aHelper.getColRanges());
							break;
						case 1:
							pCodonPosSet.setChar1Columns(aHelper.getColRanges());
							break;
						case 2:
							pCodonPosSet.setChar2Columns(aHelper.getColRanges());
							break;
						case 3:
							pCodonPosSet.setChar3Columns(aHelper.getColRanges());
							break;
						case 4:
							LOGGER.info("CodonPosSet: unassigned Pos encounted--Not handled!");
						default:
							LOGGER.info("Unhandled CodonPositionSet number :\n"
								+ aHelper.getNumber());
							break;
					}
				}

				if (LOGGER.isDebugEnabled()) {
					StringBuilder sb = new StringBuilder();
					sb.append(aHelper.getNumber()).append(":");
					for (ColumnRange aRange : aHelper.getColRanges()) {
						aRange.appendRange(sb).append(" ");
					}
					LOGGER.debug(sb.toString());
				}

			}
		}

	}

	/**
	 * Return a collection of column range converter helper for a ObjectSpecsSet.
	 * 
	 * Note: this method is based on the implementation in Mesquite code
	 * ManageCharPartitions.nexusStringForSpecsSet()
	 * 
	 * 
	 * @param pPartition
	 * @param pNumChars
	 * @return
	 */
	// private Collection<ColumnRangeConvertHelper> decodeObjectSpecsSet(
	// CharacterPartition pPartition,
	// int pNumChars) {
	// Collection<ColumnRangeConvertHelper> colRangeHelpers = new
	// ArrayList<ColumnRangeConvertHelper>();
	//
	// if (pPartition != null) {
	//
	// // an array to keep track of distinct numbers are already converted
	// NumberArray usedNumbers = new NumberArray(pNumChar);
	// usedNumbers.deassignArray();
	//
	// MesquiteNumber aNumber = new MesquiteNumber();
	// MesquiteNumber secondNumber = new MesquiteNumber();
	//
	// for (int iw = 0; iw < pNumChar; iw++) {
	// pNumSpecsSet.placeValue(iw, aNumber);
	//
	// // bypass this char if the number is already converted
	// if (usedNumbers.findValue(aNumber) < 0) {
	// int continuing = 1;
	//
	// ColumnRangeConvertHelper aHelper = new ColumnRangeConvertHelper();
	// aHelper.setNumber(aNumber);
	//
	// int startIndex = CharacterStates.toExternal(iw);
	// ColumnRange range = new ColumnRange();
	// range.setStartColIndex(startIndex - 1);
	//
	// for (int ic = iw + 1; ic < pNumChar; ic++) {
	// pNumSpecsSet.placeValue(ic, secondNumber);
	// if (secondNumber.equals(aNumber)) {
	// if (continuing == 0) {
	// // discontinue
	// range = new ColumnRange();
	// range.setStartColIndex(ic);
	//
	// continuing = 1;
	// } else if (continuing == 1) {
	// // continuing
	// }
	// } else if (range != null) {
	// // found the last index:
	// range.setEndColIndex(ic - 1);
	// aHelper.getColRanges().add(range);
	// range = null;
	// continuing = 0;
	// }
	// }
	//
	// if (range != null) {
	// range.setEndColIndex(pNumChar - 1);
	// aHelper.getColRanges().add(range);
	// }
	//
	// colRangeHelpers.add(aHelper);
	//
	// // save the converted number
	// usedNumbers.setOpenValue(aNumber);
	//
	// if (LOGGER.isDebugEnabled()) {
	// StringBuilder sb = new StringBuilder();
	// sb.append(aHelper.getNumber()).append(":");
	// for (ColumnRange aRange : aHelper.getColRanges()) {
	// sb.append(aRange.toString()).append(" ");
	// }
	// LOGGER.debug(sb.toString());
	// }
	// }
	// }
	// }
	// return colRangeHelpers;
	// }
	/**
	 * Add elements to the matrix row pRow.
	 * 
	 * For each element, needs to find out the value or state in the Mesquite matrix, convert it to
	 * the TreeBASE value or state, and add to the row.
	 * 
	 * @param pRow
	 * @param pRowIndex
	 * @param pMesqMatrix
	 */
	protected abstract void addRowElements(MatrixRow pRow, int pRowIndex, CharacterData pMesqMatrix);

	/**
	 * Create a matrix.
	 * 
	 * @param pMesqMatrix
	 * @return
	 */
	protected abstract MatrixJDBC createMatrix(CharacterData pMesqMatrix);

}
