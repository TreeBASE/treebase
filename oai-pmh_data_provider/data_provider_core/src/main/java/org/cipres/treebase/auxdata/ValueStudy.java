/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.auxdata;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ValueStudy extends Value {
	ValueAssignmentMap props;
	Collection<ValueSection> items;
	List<ValueAnalysisSection> analyses;
	
	public ValueStudy(ValueAssignmentMap props, Collection<Value> items, Collection<ValueAnalysisSection> analyses) {
		super();
		this.props = props;
		this.items = new LinkedList<ValueSection> ();
		for (Value item : items)
			if (item.getClass() != ValueSection.class) 
				throw new RDParserArgumentFailureException("");
			else 
				this.items.add((ValueSection) item);
		this.analyses = new LinkedList<ValueAnalysisSection>();
		this.analyses.addAll(analyses);
	}
	
	
}
