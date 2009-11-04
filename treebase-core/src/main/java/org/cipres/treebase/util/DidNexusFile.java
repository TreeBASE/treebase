
package org.cipres.treebase.util;

import java.io.File;

import org.cipres.treebase.ContextManager;
/**
 * Class for asking whether data derived from a certain nexus file is already in the database
 * 
 * Plus associated utility program
 * 
 * @author mjd 20090116
 *
 */
public class DidNexusFile extends AbstractStandalone {
	
	public static void main(String[] args) {
		DidNexusFile me = new DidNexusFile();
		me.setupContext();
		GetOpts<UnixOptions> go = new GetOpts<UnixOptions>(new UnixOptions ("yn"));
		UnixOptions opts = go.getOpts(args);
		boolean showDone = opts.getBoolOpt("y");
		boolean showUndone = opts.getBoolOpt("n");
		boolean showBoth = showDone && showUndone;
		if (!showDone && !showUndone) showUndone = true;
		
		args = go.getArgs();

		for (String s : args) {
			boolean did = didFile(s);
			if (showBoth) {
				String mark = did ? "* " : "  ";
				out(mark + s);
			} else if (showDone && did || showUndone && !did) {
				out(s);
			}
		}
	}
	
	private static void out(String s) {
		System.err.println(s);
	}
	
	public static boolean didFile(File f) {
		return didFile(f.getName());
	}
	
	
	public static boolean didFile(String fn) {
		// Force processing of  this file, if requested
		String baseFileName = baseName(fn);
		
		// If there's a tree in the database from this file, we must have done it already
		if (ContextManager.getPhyloTreeHome().findByNexusFile(baseFileName).size() > 0) {
			return true;
		}
		
		// If there's a matrix in the database from this file, we must have done it already
		if (ContextManager.getMatrixService().findByNexusFile(baseFileName).size() > 0) {
			return true;
		}

		return false;
	}
	
	
	private static String baseName(String fn) {
		File f = new File(fn);
		return f.getName();	
	}

}
