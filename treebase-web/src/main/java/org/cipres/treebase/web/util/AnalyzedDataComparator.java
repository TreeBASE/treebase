package org.cipres.treebase.web.util;

import java.util.Comparator;
import org.cipres.treebase.web.model.AnalyzedDataCommand;

/**
 * @author madhu
 * 
 * created on 28th August, 2007
 */

public class AnalyzedDataComparator implements Comparator<AnalyzedDataCommand> {

	public int compare(AnalyzedDataCommand adA, AnalyzedDataCommand adB) {

		String s1, s2;

		s1 = adA.getInputOutputType();
		s2 = adB.getInputOutputType();
		int typeCompare = s1.compareToIgnoreCase(s2);

		if (typeCompare != 0) return typeCompare;

		s1 = adA.getDataType();
		s2 = adB.getDataType();
		int typeData = s1.compareToIgnoreCase(s2);

		if (typeData != 0) return typeData;

		s1 = adA.getDisplayName();
		s2 = adB.getDisplayName();
		int titleCompare = s1.compareToIgnoreCase(s2);

		return titleCompare;

	}
}
