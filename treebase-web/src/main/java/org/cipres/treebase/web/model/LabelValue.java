
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
