package org.cipres.treebase.web.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.domain.AbstractPersistedObject;
import org.cipres.treebase.domain.TBPersistable;

/*
 * XXX this is work in progress, not functional right now
 */
public class PersistedObjectCommand extends AbstractPersistedObject {
	private static final long serialVersionUID = 1L;
	private TBPersistable mTBPersistable;
	private Set<TreebaseTriple> mTreebaseTripleList;
	
	public PersistedObjectCommand(TBPersistable tbPersistable) {
		mTBPersistable = tbPersistable;
		mTreebaseTripleList = new HashSet<TreebaseTriple>();
		Method[] methods = tbPersistable.getClass().getDeclaredMethods();
		for ( int i = 0; i < methods.length; i++ ) {
			String methodName = methods[i].getName();
			if ( methodName.equals("getLabel") || methodName.equals("getTitle")  ) {
				setDcTitle(methods[i]);
			}
			else if ( methodName.equals("getDescription") ) {
				setDcDescription(methods[i]);
			}			
		}
	}
	
	private void setDcTitle(Method method) {
		
	}
	
	private void setDcDescription(Method method) {
		
	}
	
	public List<TreebaseTriple> getTreebaseTripleList () {
		return null;
	}
	
}
