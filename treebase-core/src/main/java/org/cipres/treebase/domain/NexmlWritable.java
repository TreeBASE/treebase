package org.cipres.treebase.domain;

import java.util.List;

public interface NexmlWritable {
	public List<Annotation> getAnnotations ();
	public String getLabel();
	public String getDescription();
}
