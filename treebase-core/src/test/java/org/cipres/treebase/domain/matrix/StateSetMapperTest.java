package org.cipres.treebase.domain.matrix;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.cipres.treebase.domain.matrix.MatrixDataType;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.StateSetMapper;
import static org.junit.Assert.*;

public class StateSetMapperTest extends TestCase {
	/**
	 * The inputSeq contains all possible permutations of the fundamental
	 * states, which are to be mapped onto the IUPAC single character symbols
	 */
	public void testDnaMapper() {
		String inputSeq  = "ACGTK{GT}M{AC}R{AG}S{CG}W{AT}B{CGT}D{AGT}H{ACT}V{ACG}N{ACGT}-?{ACGT-}";
		String outputSeq = "ACGTKKMMRRSSWWBBDDHHVVNN-??";
		String result = MatrixRow.buildNormalizedSymbolString(StateSetMapper.createMapperForDataType(MatrixDataType.MATRIX_DATATYPE_DNA), inputSeq);
		Assert.assertEquals(outputSeq, result);
	}
	
	/**
	 * The inputSeq contains all possible permutations of the fundamental
	 * states, which are to be mapped onto the IUPAC single character symbols
	 */	
	public void testRnaMapper() {
		String inputSeq  = "ACGUK{GU}M{AC}R{AG}S{CG}W{AU}B{CGU}D{AGU}H{ACU}V{ACG}N{ACGU}-?{ACGU-}";
		String outputSeq = "ACGUKKMMRRSSWWBBDDHHVVNN-??";
		String result = MatrixRow.buildNormalizedSymbolString(StateSetMapper.createMapperForDataType(MatrixDataType.MATRIX_DATATYPE_RNA), inputSeq);
		Assert.assertEquals(outputSeq, result);
	}
	
	/**
	 * The inputSeq contains all possible permutations of the fundamental
	 * states, which are to be mapped onto the IUPAC single character symbols
	 */	
	public void testProteinMapper() {
		String inputSeq  = "ABCDEFGHIKLMNPQRSTUVWYZ{ABCDEFGHIKLMNPQRSTUVWYZ}{ABCDEFGHIKLMNPQRSTUVWYZ-}-";
		String outputSeq = "ABCDEFGHIKLMNPQRSTUVWYZX?-";
		String result = MatrixRow.buildNormalizedSymbolString(StateSetMapper.createMapperForDataType(MatrixDataType.MATRIX_DATATYPE_PROTEIN), inputSeq);
		Assert.assertEquals(outputSeq, result);		
	}
	
	/**
	 * For STANDARD data there is no implied mapping. For now we will simply
	 * pass these through, though that's technically not valid.
	 */
	public void testStandardMapper() {
		String inputSeq  = "012{1}";
		String outputSeq = "012{1}";
		String result = MatrixRow.buildNormalizedSymbolString(StateSetMapper.createMapperForDataType(MatrixDataType.MATRIX_DATATYPE_STANDARD), inputSeq);
		Assert.assertEquals(outputSeq, result);		
	}
}
