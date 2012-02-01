package org.cipres.treebase.domain.matrix;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.cipres.treebase.domain.matrix.MatrixDataType;

public class StateSetMapperProtein extends StateSetMapper {

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.StateSetMapper#makeSymbolToAmbigMap()
	 */
	@Override
	protected Map<Character, HashSet<Character>> makeSymbolToAmbigMap() {
		Map<Character, HashSet<Character>> result = new HashMap<Character, HashSet<Character>>();
		
		addMapping('A',new Character[]{'A'}, result); // Alanine
		addMapping('B',new Character[]{'B'}, result); // Aspartic acid or asparagine        
		addMapping('C',new Character[]{'C'}, result); // Cysteine        
		addMapping('D',new Character[]{'D'}, result); // Aspartic acid        
		addMapping('E',new Character[]{'E'}, result); // Glutamic acid        
		addMapping('F',new Character[]{'F'}, result); // Phenylanine
		addMapping('G',new Character[]{'G'}, result); // Glycine
		addMapping('H',new Character[]{'H'}, result); // Histidine
		addMapping('I',new Character[]{'I'}, result); // Isoleucine        
		addMapping('K',new Character[]{'K'}, result); // Lysine        
		addMapping('L',new Character[]{'L'}, result); // Leucine
		addMapping('M',new Character[]{'M'}, result); // Methionine
		addMapping('N',new Character[]{'N'}, result); // Asparagine
		addMapping('P',new Character[]{'P'}, result); // Proline
		addMapping('Q',new Character[]{'Q'}, result); // Glutamine
		addMapping('R',new Character[]{'R'}, result); // Arginine
		addMapping('S',new Character[]{'S'}, result); // Serine
		addMapping('T',new Character[]{'T'}, result); // Threonine
		addMapping('U',new Character[]{'U'}, result); // Selenocysteine
		addMapping('V',new Character[]{'V'}, result); // Valine
		addMapping('W',new Character[]{'W'}, result); // Tryptophan
		addMapping('Y',new Character[]{'Y'}, result); // Tyrosine
		addMapping('Z',new Character[]{'Z'}, result); // Glutamic acid or glutamine
		addMapping('X',new Character[]{'A','B','C','D','E','F','G','H','I','K','L','M','N','P','Q','R','S','T','U','V','W','Y','Z'}, result);
		addMapping('?',new Character[]{'A','B','C','D','E','F','G','H','I','K','L','M','N','P','Q','R','S','T','U','V','W','Y','Z','-'}, result);
		addMapping('-',new Character[]{'-'}, result);
		
		return result;	
	}

	/**
	 * Returns MatrixDataType.MATRIX_DATATYPE_PROTEIN
	 */
	@Override
	public String getDataType() {
		return MatrixDataType.MATRIX_DATATYPE_PROTEIN;
	}

}
