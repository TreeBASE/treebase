
package org.cipres.treebase.auxdata;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class ValueAnalysisSection extends ValueSection {
	Collection<ValueSection> io = new LinkedList<ValueSection> ();
	
	ValueAnalysisSection(
			ValueSequence io_sections, 
			int i,
			HashMap<String,Value> m) {
		super("ANALYSIS", i, m);
		for (Value io_sect : io_sections.subvalues()) {
			io.add((ValueSection) io_sect);
		}
	}
	
	public Collection<ValueSection> getIOSections() {
		return io;
	}
	
	public Collection<ValueSection> getTreeSections() {
		Collection<ValueSection> treeSections = new LinkedList<ValueSection>();
		for (ValueSection sec : getIOSections()) {
			if (sec.label.equals("OUTPUT_TREE")) {
				treeSections.add(sec);
			}
		}
		return treeSections;
	}
}

