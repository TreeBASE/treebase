package org.cipres.treebase.domain.matrix;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.cipres.treebase.domain.matrix.MatrixDataType;

public class StateSetMapperDna extends StateSetMapper {

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.StateSetMapper#makeSymbolToAmbigMap()
	 */
	@Override
	protected Map<Character, HashSet<Character>> makeSymbolToAmbigMap() {
		return this.makeSymbolToAmbigMapWithSymbols('A', 'C', 'G', 'T');		
	}
	
	/**
	 * 
	 * @param A
	 * @param C
	 * @param G
	 * @param T
	 * @return
	 */
	protected Map<Character, HashSet<Character>> makeSymbolToAmbigMapWithSymbols(char A, char C, char G, char T) {
		Map<Character, HashSet<Character>> result = new HashMap<Character, HashSet<Character>>();
		
		addMapping(A,new Character[]{A}, result); // A
		addMapping(C,new Character[]{C}, result); // C
		addMapping(G,new Character[]{G}, result); // G
		addMapping(T,new Character[]{T}, result); // T
		
		addMapping('M',new Character[]{A,C}, result); // M => (A,C)        
		addMapping('R',new Character[]{A,G}, result); // R => (A,G)		        
		addMapping('W',new Character[]{A,T}, result); // W => (A,T)		
		addMapping('S',new Character[]{C,G}, result); // S => (C,G)		
		addMapping('Y',new Character[]{C,T}, result); // Y => (C,T) XXX                
        addMapping('K',new Character[]{G,T}, result); // K => (G,T)

        addMapping('V',new Character[]{A,C,G}, result); // V => (A,C,G)
        addMapping('H',new Character[]{A,C,T}, result); // H => (A,C,T)  
        addMapping('D',new Character[]{A,G,T}, result); // D => (A,G,T)
        addMapping('B',new Character[]{C,G,T}, result); // B => (C,G,T)
        
        addMapping('N',new Character[]{A,C,G,T}, result); // N => (A,C,G,T) 
        addMapping('?',new Character[]{A,C,G,T,'-'}, result); // ? => (A,C,G,T,-)        
        addMapping('-',new Character[]{'-'}, result); // - => ()        
		
		return result;		
	}

	/**
	 * Returns MatrixDataType.MATRIX_DATATYPE_DNA
	 */
	@Override
	public String getDataType() {
		return MatrixDataType.MATRIX_DATATYPE_DNA;
	}

	


}
