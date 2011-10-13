package org.cipres.treebase.domain.nexus.nexml;

import java.util.ArrayList;
import java.util.List;

import org.cipres.treebase.dao.jdbc.ContinuousMatrixElementJDBC;
import org.cipres.treebase.dao.jdbc.ContinuousMatrixJDBC;
import org.cipres.treebase.dao.jdbc.DiscreteMatrixElementJDBC;
import org.cipres.treebase.dao.jdbc.DiscreteMatrixJDBC;
import org.cipres.treebase.dao.jdbc.MatrixColumnJDBC;
import org.cipres.treebase.domain.matrix.ContinuousChar;
import org.cipres.treebase.domain.matrix.ContinuousMatrix;
import org.cipres.treebase.domain.matrix.DiscreteChar;
import org.cipres.treebase.domain.matrix.DiscreteMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.nexml.model.Annotatable;
import org.nexml.model.CategoricalMatrix;
import org.nexml.model.CharacterState;
import org.nexml.model.Document;
import org.nexml.model.MatrixCell;
import org.nexml.model.OTU;
import org.nexml.model.OTUs;

public class NexmlMatrixReader extends NexmlObjectConverter {

	public NexmlMatrixReader(Study study, TaxonLabelHome taxonLabelHome,
			Document document) {
		super(study, taxonLabelHome, document);
	}

	/**
	 * 
	 * @param xmlMatrix
	 * @return
	 */
	private Matrix fromXmlToTreeBase(CategoricalMatrix xmlMatrix) {
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
	private Matrix fromXmlToTreeBase(org.nexml.model.ContinuousMatrix xmlMatrix) {		
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
	
}
