package org.cipres.treebase.web.util;

import java.util.Comparator;

public class GenericComparator implements Comparator {

	public int compare(Object pO1, Object pO2) {

		String s1, s2;

		s1 = (String) pO1;
		s2 = (String) pO2;
		int typeCompare = s1.compareToIgnoreCase(s2);

		return typeCompare;

	}

}
