package org.cipres.treebase;

import org.cipres.treebase.domain.TBPersistable;

public class PhyloWSPath {
	private NamespacedGUID mNamespacedGUID;
	private Package mPackage;
	
	public StringBuilder getPath(StringBuilder sb) {
		String[] packagePathFragments = mPackage.getName().split("\\.");
		return sb
			// i.e. admin, matrix, study, taxon or tree
			.append(packagePathFragments[packagePathFragments.length-1]) 
			.append('/');
	}
	
	public String getPath() {
		return getPath(new StringBuilder()).toString();
	}	
	
	/**
	 * 
	 */
	public String toString() {	
		StringBuilder sb = new StringBuilder();
		return getPath(sb).append(mNamespacedGUID.toString()).toString();
	}
	
	/**
	 * 
	 * @param tbPersistable
	 */
	public PhyloWSPath(TBPersistable tbPersistable) {
		mNamespacedGUID = tbPersistable.getNamespacedGUID();
		mPackage = tbPersistable.getClass().getPackage();
	}
	
	/**
	 * 
	 * @param pPackage
	 * @param namespacedGUID
	 */
	public PhyloWSPath(Package pPackage, NamespacedGUID namespacedGUID) {
		mPackage = pPackage;
		mNamespacedGUID = namespacedGUID;
	}

}
