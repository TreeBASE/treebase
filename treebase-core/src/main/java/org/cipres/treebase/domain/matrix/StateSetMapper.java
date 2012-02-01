package org.cipres.treebase.domain.matrix;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * This class (and its subclasses) help with mapping between sets
 * of fundamental states (such as {ACGT}) and IUPAC single character
 * ambiguity symbols. This is especially important for valid NeXML
 * generation, which requires that molecular <seq> elements only
 * contain single character symbols, not sets.
 * @author rvosa
 *
 */
public abstract class StateSetMapper {
	private Map<Character,HashSet<Character>> symbolToAmbig;
	private Map<HashSet<Character>,Character> ambigToSymbol;
	
	/**
	 * Returns a map keyed on IUPAC single character ambiguity symbols
	 * http://droog.gs.washington.edu/parc/images/iupac.html with values
	 * containing a set (possibly of 1) with fundamental states.
	 * @return
	 */
	protected abstract Map<Character,HashSet<Character>> makeSymbolToAmbigMap();	
	

	public StateSetMapper () {
		symbolToAmbig = makeSymbolToAmbigMap();
		ambigToSymbol = new HashMap<HashSet<Character>,Character>();
		for ( Character key : symbolToAmbig.keySet() ) {
			HashSet<Character> value = symbolToAmbig.get(key);
			ambigToSymbol.put(value, key);
		}
	}
	
	/**
	 * Instantiates appropriate subclass given datatype
	 * @param datatype - one of the types in MatrixDataType
	 * @return
	 */
	public static StateSetMapper createMapperForDataType(String datatype) {
		if ( MatrixDataType.MATRIX_DATATYPE_DNA.equals(datatype) ) {
			return new StateSetMapperDna();
		}
		else if ( MatrixDataType.MATRIX_DATATYPE_RNA.equals(datatype) ) {
			return new StateSetMapperRna();
		}
		else if ( MatrixDataType.MATRIX_DATATYPE_PROTEIN.equals(datatype) ) {
			return new StateSetMapperProtein();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Looks up the IUPAC single character symbol for a set of fundamental states
	 * @return
	 */
	public Character getSymbolForAmbiguousSet(HashSet<Character> ambiguousSet) {
		return ambigToSymbol.get(ambiguousSet);
	}
	
	/**
	 * Looks up the set of fundamental states for an IUPAC single character symbol
	 * @return
	 */
	public HashSet<Character> getAmbiguousSetForSymbol(Character symbol) {
		return symbolToAmbig.get(symbol);
	}
	
	/**
	 * Returns a data type from MatrixDataType.MATRIX_DATATYPE_.*
	 * @return
	 */
	public abstract String getDataType();
	
	/**
	 * Helper method for populating the underlying Map object
	 * @param symbol
	 * @param symbolArray
	 * @param result
	 */
	protected void addMapping(Character symbol,Character[] symbolArray,Map<Character, HashSet<Character>> result) {
		HashSet<Character> symbolSet = new HashSet<Character>(symbolArray.length);
		for ( int i = 0; i < symbolArray.length; i++ ) {
			symbolSet.add(symbolArray[i]);
		}
		result.put(symbol, symbolSet);
	}	
}
