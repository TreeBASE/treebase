
package org.cipres.treebase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.framework.ExecutionResult;

/**
 * A collection of useful utility methods.
 * 
 * Created on October 4, 2005
 * 
 * @author Jin Ruan
 * 
 */
public class TreebaseUtil {

	/**
	 * Constructor.
	 */

	public static final String FILESEP = System.getProperty("file.separator");
	public static final String ANEMPTYSPACE = " ";
	public static final String LINESEP = System.getProperty("line.separator");
	public static final int citationMaxLength = 5000;
	private static final Logger LOGGER = Logger.getLogger(TreebaseUtil.class);
	private static String mPurlBase;
	private static String mSiteUrl; 
	private static String mSmtpHost; 

	private TreebaseUtil() {
		super();
	}

	/**
	 * Return true if the string is null or its length is 0.
	 * 
	 * @return boolean
	 * @param pString java.lang.String
	 */
	public static boolean isEmpty(String pString) {

		if (pString == null || pString.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Return true if the two objects are both null or equal.
	 * 
	 * @return boolean
	 * @param pObj1, pObj2
	 */
	public static boolean isEqual(Object pObj1, Object pObj2) {

		if (pObj1 == null) {
			return (pObj2 == null);
		} else {
			return pObj1.equals(pObj2);
		}
	}

	/**
	 * Return a hash code based on two string objects. The first object cannot be null.
	 * 
	 * @param pStr1
	 * @param pStr2
	 * @return
	 */
	public static int getHashCode(String pStr1, String pStr2) {
		int code = 17;
		code = 37 * code + pStr1.hashCode();

		if (pStr2 != null) {
			code = 37 * code + pStr2.hashCode();
		}

		return code;
	}

	/**
	 * Return a hash code based on three string objects. The first object cannot be null.
	 * 
	 * @param pStr1
	 * @param pStr2
	 * @param pStr3
	 * 
	 * @return
	 */
	public static int getHashCode(String pStr1, String pStr2, String pStr3) {
		// Credit:
		// based on: http://mindprod.com/jgloss/hashcode.html
		// This method will not work well if some of the strings are equal:

		int code = pStr1.hashCode();

		if (pStr2 != null) {
			code ^= pStr2.hashCode();
		}

		if (pStr3 != null) {
			code ^= pStr3.hashCode();
		}

		return code;
	}

	/**
	 * For Testing use. Output the content of the array to a string. If the all element parameter is
	 * false, output only the first 10 elements.
	 * 
	 * @param pArray
	 * @param pAllElement
	 * @return
	 */
	public static String printElement(long[] pArray, boolean pAllElement) {
		int size = pArray.length;
		if (!pAllElement && size > 10) {
			size = 10;
		}

		if (size != 10) {
			return Arrays.toString(pArray);
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < size; i++) {
			buf.append(i).append("= ").append(pArray[i]).append(ANEMPTYSPACE);
		}

		return buf.toString();
	}
	
	public static String removeTroublesomeCharacters(String inString) {
	    if ( inString == null ) {
	    	return null;
	    }
	    StringBuilder newString = new StringBuilder();
	    for (int i = 0; i < inString.length(); i++) {
	        char ch = inString.charAt(i);
	        // remove any characters outside the valid UTF-8 range as well as all control characters
	        // except tabs and new lines
	        if ((ch < 0x00FD && ch > 0x001F) || ch == '\t' || ch == '\n' || ch == '\r') {
	            newString.append(ch);
	        }
	    }
	    return newString.toString();

	}
	
	
	
	/**
	 * Reads the contents of a file into a string
	 * @param file
	 * @return file contents as string
	 */
	public static String readFileToString(File file) {
		StringBuilder contents = new StringBuilder();

		try {
			//use buffering, reading one line at a time
			//FileReader always assumes default encoding is OK!
			BufferedReader input =  new BufferedReader(new FileReader(file));
			try {
				String line = null; //not declared within while loop
				/*
				 * readLine is a bit quirky :
				 * it returns the content of a line MINUS the newline.
				 * it returns null only for the END of the stream.
				 * it returns an empty String if two newlines appear in a row.
				 */
				while (( line = input.readLine()) != null){
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			}
			finally {
				input.close();
			}
		}
		catch (IOException ex){
			ex.printStackTrace();
		}	    
		return contents.toString();		
	}

	/**
	 * For Testing use. Output the content of the array to a string. If the all element parameter is
	 * false, output only the first 10 elements.
	 * 
	 * @param pArray
	 * @param pAllElement
	 * @return
	 */
	public static String printElement(Long[] pArray, boolean pAllElement) {
		int size = pArray.length;
		if (!pAllElement && size > 10) {
			size = 10;
		}

		if (size != 10) {
			return Arrays.toString(pArray);
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < size; i++) {
			buf.append(i).append("= ").append(pArray[i]).append(ANEMPTYSPACE);
		}

		return buf.toString();
	}

	/**
	 * For Testing use. Output the content of the collection to a string. If the all element
	 * parameter is false, output only the first 10 elements.
	 * 
	 * @param pA
	 * @param pAllElement
	 * @return
	 */
	public static String printElement(Collection<?> pCollection, boolean pAllElement) {
		int size = pCollection.size();
		if (!pAllElement && size > 10) {
			size = 10;
		}

		StringBuffer buf = new StringBuffer();
		Iterator<?> iter = pCollection.iterator();
		for (int i = 0; i < size; i++) {
			buf.append(i).append("= ").append(iter.next()).append(ANEMPTYSPACE);
		}

		return buf.toString();
	}

	/**
	 * 
	 * @param n number of new line characters
	 * @return n new line characters as String
	 */
	public static String getLineSeparators(int n) {

		if (n <= 0) {
			return "";
		} else {
			StringBuilder lineSeparators = new StringBuilder();
			for (int i = 0; i < n; i++) {
				lineSeparators.append(LINESEP);
			}
			return lineSeparators.toString();
		}
	}

	/**
	 * 
	 * @param n number of spaces
	 * @return n spaces as String
	 */
	public static String getSpaces(int n) {
		if (n <= 0) {
			return "";
		} else {
			StringBuilder spaces = new StringBuilder();
			for (int i = 0; i < n; i++) {
				spaces.append(ANEMPTYSPACE);
			}
			return spaces.toString();
		}
	}

	/**
	 * Format the date to ISO 8601 format: "yyyy-MM-dd'T'HH:mm:ss"
	 * 
	 * @param pDate
	 * @param pShowDateOnly if true, only show date "yyyy-MM-dd"
	 * @return
	 */
	public static String formatDate(Date pDate, boolean pShowDateOnly) {

		if (pDate == null) {
			return "";
		}

		SimpleDateFormat dateFormat = null;

		if (pShowDateOnly) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		}

		return dateFormat.format(pDate);
	}

	/**
	 * Parse the date according to ISO 8601 format: "yyyy-MM-dd'T'HH:mm:ss"
	 * 
	 * Return null if parsing failed, and add warning message to the execution result object if it
	 * is available.
	 * 
	 * @param pDateStr
	 * @param pShowDateOnly if true, only parse date "yyyy-MM-dd"
	 * @param pExecution add warning message if parsing failed.
	 * @return
	 */
	public static Date parseDate(String pDateStr, boolean pShowDateOnly, ExecutionResult pExecution) {

		if (TreebaseUtil.isEmpty(pDateStr)) {
			return null;
		}

		SimpleDateFormat dateFormat = null;

		if (pShowDateOnly) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		}

		try {
			Date d = dateFormat.parse(pDateStr);
			return d;
		} catch (ParseException ex) {
			String errMsg = "Failed to parse to date : " + pDateStr;
			if (pExecution != null) {
				pExecution.addErrorMessage(errMsg);
				return null;
			}
			throw new RuntimeException(errMsg, ex);
		}
	}

	/**
	 * Parse a string to a double.
	 * 
	 * Return null if parsing failed, and add warning message to the execution result object if it
	 * is available.
	 * 
	 * @param pValue
	 * @param pExecution add warning message if parsing failed.
	 * @return
	 */
	public static Double parseDouble(String pValue, ExecutionResult pExecution) {

		if (pValue == null) {
			return null;
		}

		try {
			Double d = Double.valueOf(pValue);
			return d;
		} catch (NumberFormatException ex) {
			if (pExecution != null) {
				pExecution.addErrorMessage("Failed to parse to double: " + pValue);
			}
			return null;
		}
	}

	/**
	 * Parse a string to a Long.
	 * 
	 * Return null if parsing failed, and add warning message to the execution result object if it
	 * is available.
	 * 
	 * @param pValue
	 * @param pExecution add warning message if parsing failed.
	 * @return
	 */
	public static Long parseLong(String pValue, ExecutionResult pExecution) {

		if (pValue == null) {
			return null;
		}

		try {
			Long l = Long.valueOf(pValue);
			return l;
		} catch (NumberFormatException ex) {
			if (pExecution != null) {
				pExecution.addErrorMessage("Failed to parse to double: " + pValue);
			}
			return null;
		}
	}

	/**
	 * Parse a string to an Integer.
	 * 
	 * Return null if parsing failed, and add warning message to the execution result object if it
	 * is available.
	 * 
	 * @param pValue
	 * @param pExecution add warning message if parsing failed.
	 * @return
	 */
	public static Integer parseInteger(String pValue, ExecutionResult pExecution) {

		if (pValue == null) {
			return null;
		}

		try {
			Integer d = Integer.valueOf(pValue);
			return d;
		} catch (NumberFormatException ex) {
			if (pExecution != null) {
				pExecution.addErrorMessage("Failed to parse to integer : " + pValue);
			}
			return null;
		}
	}

	/**
	 * This method returns the current time in GMT.
	 * 
	 * @return current time in GMT
	 */
	private static String getTimeAndDateAtGMT() {

		String pattern = "MMMMM dd, yyyy; H:mm";
		SimpleDateFormat simpleformat = new SimpleDateFormat(pattern);

		TimeZone tz = TimeZone.getTimeZone("GMT");
		java.util.Calendar cal = Calendar.getInstance(tz);
		simpleformat.setCalendar(cal);

		String date = simpleformat.format(new Date());
		// System.out.println(date + " GMT");

		return date + " GMT";
	}

	
	/** Looks up a JNDI Environment parameter that carries a string value.
	 * 
	 */
	private static String lookupJndiEnvironmentString(String name, String fallback) {
		String result = fallback; 
		try {
			InitialContext ic = new InitialContext();
			result  = (String) ic.lookup("java:comp/env/" + name);
		} catch (NamingException e) {
			LOGGER.info("Failure looking up " + name + " via JNDI"); 
			e.printStackTrace();
		}
		return result; 
	}
	
	
	
	/**
	 * Returns the base URL of the PURL service associated with this Treebase instance,
	 * which can be used to construct full PURLs by suffixing with a PhyloWS command, e.g. "/study/TB2:S1925"
	 * 
	 * @return the base URL of the PURL service
	 */
	public static String getPurlBase() {
		if (null == mPurlBase) 
			mPurlBase = lookupJndiEnvironmentString("tb2/PurlBase", Constants.BaseURI.toString()); 
		return mPurlBase; 
	}
	
	/**
	 * Returns the base URL of this Treebase instance, by looking it up in Tomcat via JNDI.
	 * 
	 * @return the base URL of of this Treebase instance
	 */
	public static String getSiteUrl() {
		if (null == mSiteUrl)
			mSiteUrl = lookupJndiEnvironmentString("tb2/SiteUrl", "http://DUMMY.SITE.COM/"); 
		return mSiteUrl; 
	}

	/**
	 * 
	 * @return the SMTP host to use for automated email
	 */
	public static String getSmtpHost() { 
		if (null == mSmtpHost) 
			mSmtpHost = lookupJndiEnvironmentString("tb2/SmtpHost", "smtp.DUMMY.HOST");
		return mSmtpHost; 
	}
	
	
	
	/**
	 * This method appends header information upon formatting to the nexus file.
	 * 
	 * @param pStudy Study is needed to extract the citation information
	 * @param pBuilder
	 */
	public static void attachStudyHeader(Study pStudy, StringBuilder pBuilder) {
		pBuilder
			.append(
				"[!This data set was downloaded from TreeBASE, a relational database of phylogenetic knowledge. TreeBASE has been supported by the NSF, Harvard University, Yale University, SDSC and UC Davis. Please do not remove this acknowledgment from the Nexus file.\n\n\nGenerated on "
					+ getTimeAndDateAtGMT()).append(TreebaseUtil.LINESEP).append(
				"\nTreeBASE (cc) 1994-2008").append(TreebaseUtil.LINESEP);

		Citation cit = pStudy.getCitation();
		pBuilder.append(TreebaseUtil.LINESEP).append("Study reference:").append(
			TreebaseUtil.LINESEP);

		if (cit != null) {
			String citationString = cit.getAuthorsCitationStyleWithoutHtml();

			int citationLength = citationString.length();
			int splitPoint = 80;

			if (citationLength > splitPoint) {
				StringBuilder tmp = new StringBuilder(citationString);
				int citationMod = citationLength / splitPoint;

				for (int p = 1; p < citationMod + 1; p++) {
					int index = citationString.indexOf(TreebaseUtil.ANEMPTYSPACE, splitPoint * p);
					// important: check whether find it.
					if (index >= 0) {
						tmp.replace(index, index + 1, TreebaseUtil.LINESEP);
					}
				}

				pBuilder.append(tmp.toString());

			} else {
				pBuilder.append(citationString);
			}
		}
		pBuilder
			.append(TreebaseUtil.getLineSeparators(2)).append("TreeBASE Study URI:  " + pStudy.getPhyloWSPath().getPurl())
			.append("]").append(TreebaseUtil.getLineSeparators(2));
	}
}
