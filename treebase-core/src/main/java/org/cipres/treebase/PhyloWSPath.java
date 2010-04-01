package org.cipres.treebase;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.cipres.treebase.domain.TBPersistable;

public class PhyloWSPath {
	private NamespacedGUID mNamespacedGUID;
	private Package mPackage;
	private static final Logger LOGGER = Logger.getLogger(PhyloWSPath.class);
	
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
	 * Constructs a PURL, based on the base URL of the PURL service (looked up through JNDI)
	 * the phylows path and the namespaced GUID for the object.
	 * @return the PURL for the object
	 */
	public URL getPurl () {
		StringBuilder sb = new StringBuilder(TreebaseUtil.getPurlBase());
		sb = getPath(sb).append(mNamespacedGUID.toString());
		LOGGER.info(sb);
		URL url = null;		
		try {
			url = new URL(sb.toString());
		} catch (MalformedURLException e) {
			LOGGER.warn("MalformedURLException: "+e.getMessage());
			e.printStackTrace();
		}		
		return url;
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
