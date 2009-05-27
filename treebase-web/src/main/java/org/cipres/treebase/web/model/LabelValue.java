/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */

package org.cipres.treebase.web.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * LabelValue.java
 * 
 * Created on May 9, 2006
 * @author lcchan
 *
 */
@SuppressWarnings("serial")
public class LabelValue implements Comparable, Serializable {

	
	public static final Comparator CASE_INSENSITIVE_ORDER = new Comparator() {
		public int compare(Object o1, Object o2) {
			String name1 = ((LabelValue) o1).getLabel();
			String name2 = ((LabelValue) o2).getLabel();
			return name1.compareToIgnoreCase(name2);
		}
	};
	
	public int compareTo(Object o) {
		String label = ((LabelValue) o) .getLabel();
		return this.getLabel().compareTo(label);
	}
	
	private String label = null;
	private String value = null;

	/**
	 * Default constructor
	 * Creation date: May 9, 2006 5:36:50 PM
	 */
	public LabelValue() {
		super();
	}
	/**
	 * constructor to create an instance of LabelValue
	 * Creation date: May 9, 2006 5:37:28 PM
	 */
	public LabelValue(String label, String value) {
		this.label = label;
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
