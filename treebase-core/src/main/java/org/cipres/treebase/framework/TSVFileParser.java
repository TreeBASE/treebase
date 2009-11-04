
package org.cipres.treebase.framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.cipres.treebase.TreebaseUtil;

/**
 * TSVFileParser.java
 * 
 * Responsible to read in a TSV (tab separated values) formated tabular text file, output is a list
 * of list of string values.
 * 
 * Created on May 5, 2008
 * 
 * @author Jin Ruan
 * 
 */
public class TSVFileParser {

	/**
	 * Constructor.
	 */
	public TSVFileParser() {
		super();
	}

	/**
	 * Parse the file. Return a list of list of strings: each string represents a data value, each
	 * list of strings represent a row of data.
	 * 
	 * @param pFile data file.
	 * @param pSkipFirstRow whether to skip the first row.
	 * @param pExecution record execution status.
	 * @return
	 */
	public List<List<String>> parseFile(
		File pFile,
		boolean pSkipFirstRow,
		ExecutionResult pExecution) {

		boolean hasError = false;
		String errMsg = null;
		List<List<String>> values = new ArrayList<List<String>>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(pFile));

			if (reader == null) {
				hasError = true;
			} else {
			// First skip the column header if indicated to do so:
			if (pSkipFirstRow) {
				reader.readLine();
			}

			values = readData(reader);
			}
		} catch (FileNotFoundException ex) {
			hasError = true;
			errMsg = "Data File cannot be found.";
		} catch (IOException ex) {
			hasError = true;
			errMsg = "Failed to read in data " + ex.toString();
		}

		if (hasError) {
			String errTitle = "Failed to import data file.\n" + errMsg;
			pExecution.addErrorMessage(errTitle);
		}

		return values;
	}

	/**
	 * 
	 * @param pReader
	 * @return
	 * @throws IOException 
	 */
	private List<List<String>> readData(BufferedReader pReader) throws IOException {
		List<List<String>> values = new ArrayList<List<String>>();
		
		String lineStr;
		StringTokenizer st;
		String aValue = null;

		boolean hasOneDeliminator = true;

		while ((lineStr = pReader.readLine()) != null) {
			
			//lineStr = lineStr.trim();
			
			// ignore empty lines
			if (!TreebaseUtil.isEmpty(lineStr)) {
				List<String> aline = new ArrayList<String>();
				
				hasOneDeliminator = true;
				st = new StringTokenizer(lineStr, "\t", true);
				while (st.hasMoreTokens()) {

					aValue = st.nextToken();

					if ("\t".equals(aValue)) {
						if (hasOneDeliminator) {
							aValue = ""; // Missing Value

							aline.add(aValue);
						}

						hasOneDeliminator = true;
					} else {
						hasOneDeliminator = false;

						if (aValue != null) {
							// Trim: 
							aValue = aValue.trim();

							// Handles (ignore) double quotes: 
							if (aValue.startsWith("\"") && aValue.endsWith("\"")) {
								int length = aValue.length();
								if (length >= 2) {
									aValue = aValue.substring(1, length - 1);
								}
							}
						}

						aline.add(aValue);
					}
				}
				values.add(aline);
			}
		}
		pReader.close();
				
		return values;
	}

}
