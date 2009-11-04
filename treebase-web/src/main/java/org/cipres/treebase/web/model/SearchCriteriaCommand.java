
package org.cipres.treebase.web.model;

/**
 * CriteriaCommand.java
 * 
 * Created on Jun 16, 2006
 * @author lcchan
 *
 */
public class SearchCriteriaCommand {

	private int id; 
	private String attribute;
	private String match;
	private String value;
	private String operator; // And, OR
	
	public SearchCriteriaCommand() {}
	
	public SearchCriteriaCommand(
						   String attribute,
						   String match,
						   String value,
						   String operator)
	{
		this.attribute = attribute;
		this.match = match;
		this.value = value;
		this.operator = operator;
	}
	
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getMatch() {
		return match;
	}
	public void setMatch(String match) {
		this.match = match;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
