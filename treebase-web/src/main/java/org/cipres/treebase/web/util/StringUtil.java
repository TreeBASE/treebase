package org.cipres.treebase.web.util;

import java.security.MessageDigest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lcchan
 * 
 */
public class StringUtil {

	private final static Logger LOGGER = LogManager.getLogger(StringUtil.class);

	/**
	 * 
	 * Creation date: Apr 12, 2006 5:44:59 PM
	 */
	public static String encodePassword(String password, String algorithm) {

		byte[] unencodedPassword = password.getBytes();

		MessageDigest md = null;

		try {
			// first create an instance, given the provider
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			LOGGER.error("Exception: " + e);

			return password;
		}

		md.reset();

		// call the update method one or more times
		// (useful when you don't know the size of your data, eg. stream)
		md.update(unencodedPassword);

		// now calculate the hash
		byte[] encodedPassword = md.digest();

		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("HERE IS THE ENCRYPED PASSWORD: " + buf.toString());
		}

		return buf.toString();

	}
}
