package org.cipres.treebase.domain.matrix;

import java.util.HashSet;
import java.util.Map;
import org.cipres.treebase.domain.matrix.MatrixDataType;

public class StateSetMapperRna extends StateSetMapperDna {
	
	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.StateSetMapperDna#makeSymbolToAmbigMap()
	 */
	@Override
	protected Map<Character, HashSet<Character>> makeSymbolToAmbigMap() {
		return makeSymbolToAmbigMapWithSymbols('A', 'C', 'G', 'U');
	}
	
	/**
	 * Returns MatrixDataType.MATRIX_DATATYPE_RNA
	 */
	@Override
	public String getDataType() {
		return MatrixDataType.MATRIX_DATATYPE_RNA;
	}	
}
