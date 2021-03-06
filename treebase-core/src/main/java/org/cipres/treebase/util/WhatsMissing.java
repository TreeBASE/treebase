
package org.cipres.treebase.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.cipres.treebase.ContextManager;


/**
 * Utility that reports on which TB1 trees, matrices, and studies are missing from the database.
 * 
 * <p>This program is way too slow.  Maybe retrieve proxy objects only?  Or ignore Hibernate entirely?
 * 
 * @author mjd 20090320
 *
 */
public class WhatsMissing extends AbstractStandalone implements WhatsMissingInterface {

	private static boolean verbose = true;
	
	public static void main(String[] args) {
		setupContext();
		WhatsMissingInterface me = (WhatsMissingInterface) ContextManager.getBean("whatsMissing");
		WhatsMissing wm = new WhatsMissing();

		GetOpts<UnixOptions> go = new GetOpts<UnixOptions>(new UnixOptions("Qtms"));
		UnixOptions opts = go.getOpts(args);

		boolean do_all = !opts.getBoolOpt("t") && !opts.getBoolOpt("m") && !opts.getBoolOpt("s");
		verbose = ! opts.getBoolOpt("Q");
		
		boolean okay = true;

		if (do_all || opts.getBoolOpt("s"))
			okay = me.doCheck("studies.lst", wm.new StudyCounter()) && okay;

		if (do_all || opts.getBoolOpt("m"))
			okay = me.doCheck("matrices.lst", wm.new MatrixCounter()) && okay;

		if (do_all || opts.getBoolOpt("t"))
			okay = me.doCheck("trees.lst", wm.new TreeCounter()) && okay;

		System.exit(okay ? 0 : 1);
	}

	private String[] readStringArrayFromConfigFile(String configFileName) {
		InputStream configFileStream = this.getClass().getResourceAsStream("/" + configFileName);
		BufferedReader configFH = new BufferedReader(new InputStreamReader(configFileStream));
		String line;
		LinkedList<String> lines = new LinkedList<String> ();
		
		try {
			while ((line = configFH.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			return null;
		}
		
		if (verbose) warn(configFileName + ": " + lines.size() + " lines");
		
		String[] lineArray = new String[lines.size()];
		lines.toArray(lineArray);
		return lineArray;
	}

	public static void warn(String s) {
		AbstractStandalone.warn(s);
	}

	public boolean doCheck(String propertyName, ObjectCounter objectCounter) {
		boolean good = true;
		int missing = 0;
		String[] idStrings = readStringArrayFromConfigFile(propertyName);
		for (String legacyID : idStrings) {
			int n = objectCounter.objectCount(legacyID);
			if (n == 0) {
				warn(legacyID + " is missing");
				missing++;
				good = false;
			} else if (n > 1) {
				warn(legacyID + " is present multiple times");
				good = false;
			}
		}
		if (verbose && missing > 0) warn("> " + missing + (missing == 1 ? " item" : " items") + " missing");
		return good;
	}
	
	public interface ObjectCounter {
		int objectCount(String idString);
	}
	
	private class StudyCounter implements ObjectCounter {
		public int objectCount(String idString) {
			return ContextManager.getStudyHome().findByTB1StudyID(idString).size();
		}
	}
	
	private class MatrixCounter implements ObjectCounter {
		public int objectCount(String idString) {
			return ContextManager.getMatrixHome().findByTB1MatrixID(idString).size();
		}
	}
	
	private class TreeCounter implements ObjectCounter {
		public int objectCount(String idString) {
			return ContextManager.getPhyloTreeHome().findByTB1TreeID(idString).size();
		}
	}
}
