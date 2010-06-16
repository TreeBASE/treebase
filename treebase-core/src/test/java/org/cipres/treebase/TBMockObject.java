package org.cipres.treebase;

import java.util.List;
import java.util.Set;

import org.cipres.treebase.domain.Annotation;
import org.cipres.treebase.domain.TBPersistable;

/**
 * A trivial, nonpersistent TBPersistable object, useful for incorporation into test classes.
 *  
 * @author mjd 20090414
 *
 */
public class TBMockObject extends Object implements TBPersistable {
	Long val;

	public TBMockObject(Long val) {
		super();
		this.val = val;
	}
	
	public TBMockObject(int val) {
		super();
		this.val = new Long(val);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof TBMockObject)) return false;
		TBMockObject o = (TBMockObject) obj;
		return this.getId().equals(o.getId());
	}

	public Long getId() {
		return val;
	}
	
	public String toString() {
		return "#" + getId().toString();
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	public NamespacedGUID getNamespacedGUID() {
		// TODO Auto-generated method stub
		return null;
	}

	public TreebaseIDString getTreebaseIDString() {
		// TODO Auto-generated method stub
		return null;
	}

	public PhyloWSPath getPhyloWSPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Annotation> getAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAnnotationsAsXmlString() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	public TBPersistable getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
