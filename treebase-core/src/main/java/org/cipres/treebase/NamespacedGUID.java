package org.cipres.treebase;

import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;

/**
 * Helper class for TreebaseIDString
 * 
 * @author rvosa
 *
 */
public class NamespacedGUID {
	private String mMamespacedIDString;
	public static final String NAMING_AUTHORITY_PREFIX_TB1 = "TB1";
	public static final String NAMING_AUTHORITY_PREFIX_TB2 = "TB2";
	public static final String GUID_DELIMITER = ":";
	
	/**
	 * @param a fully qualified treebase ID, e.g. TB2:Tr2312
	 */
	public NamespacedGUID(String namespacedIDString) {
		mMamespacedIDString = namespacedIDString;
	}
	
	/**
	 * @return a fully qualified treebase ID, e.g. TB2:Tr2312
	 */
	public String toString() {
		return mMamespacedIDString;
	}
	
	/**
	 * 
	 * @return A local TreebaseIDString, e.g. Tr2312
	 * @throws MalformedTreebaseIDString
	 */
	public TreebaseIDString getTreebaseIDString () throws MalformedTreebaseIDString {
		StringBuilder sb = new StringBuilder();
		sb
			.append(NAMING_AUTHORITY_PREFIX_TB2)
			.append(GUID_DELIMITER);
		TreebaseIDString treebaseIDString = new TreebaseIDString(mMamespacedIDString.substring(sb.length()));
		return treebaseIDString;
	}
	
	/**
	 * @return Returns whatever is normally used to make treebase id's globally unique
	 */
	public static String getDefaultGUIDPrefix () {
		return NAMING_AUTHORITY_PREFIX_TB2 + GUID_DELIMITER;
	}
	
}