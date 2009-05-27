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
