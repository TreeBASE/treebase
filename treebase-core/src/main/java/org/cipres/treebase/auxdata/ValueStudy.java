
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
