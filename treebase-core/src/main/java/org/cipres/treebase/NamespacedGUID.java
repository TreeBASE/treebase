package org.cipres.treebase;

import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;


/**
 * Helper class for TreebaseIDString
 * 
 * @author rvosa
 *
 */
public class NamespacedGUID {
	private String mNamespacedIDString;
	public static final String NAMING_AUTHORITY_PREFIX_TB1 = "TB1";
	public static final String NAMING_AUTHORITY_PREFIX_TB2 = "TB2";
	public static final String NAMING_AUTHORITY_PREFIX_NCBI = "NCBI";
	public static final String NAMING_AUTHORITY_PREFIX_UBIO = "uBio";
	public static final String GUID_DELIMITER = ":";
	
	/**
	 * @param a fully qualified treebase ID, e.g. TB2:Tr2312
	 */
	public NamespacedGUID(String namespacedIDString) {
		mNamespacedIDString = namespacedIDString;
	}
	
	/**
	 * @return a fully qualified treebase ID, e.g. TB2:Tr2312
	 */
	public String toString() {
		return mNamespacedIDString;
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
		TreebaseIDString treebaseIDString = new TreebaseIDString(mNamespacedIDString.substring(sb.length()));
		return treebaseIDString;
	}
	
	/**
	 * 
	 * @return Returns an MD5-hashed version of the namespaced ID-string
	 */
	public String getHashedIDString() {
	    MessageDigest messageDigest = null;
	    try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	    messageDigest.update(mNamespacedIDString.getBytes(),0,mNamespacedIDString.length());
	    return new BigInteger(1,messageDigest.digest()).toString(16);
	}
	
	/**
	 * @return Returns whatever is normally used to make treebase id's globally unique
	 */
	public static String getDefaultGUIDPrefix () {
		return NAMING_AUTHORITY_PREFIX_TB2 + GUID_DELIMITER;
	}
	
}