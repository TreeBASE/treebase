
package org.cipres.treebase.util;

import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.cipres.treebase.domain.matrix.MatrixHome;
import org.cipres.treebase.domain.tree.PhyloTreeHome;

public interface RawNexusImporterInterface extends Standalone {

	/**
	 * Process a single nexus file: attach its contents to relevant studies, 
	 * and associate it with approrpiate trees and/or matrices.
	 * 
	 * <p> This is the main, transaction-embedded method
	 * 
	 * @author mjd  20090420
	 * @param file - path of the nexus file
	 * @return true on success, false on failure
	 * @throws UnsupportedEncodingException - should never happen
	 */
	public boolean doFile(File file) throws UnsupportedEncodingException;
	
	
	/**
	 * Put the bean into test mode, in which it performs no actions
	 * 
	 * @author mjd 20090420
	 * @param b - true to enable test mode, false to enable normal mode
	 */
	public void setTestMode(boolean b);
	
	/**
	 * Put the bean into verbose mode, in which it reports what it is doing
	 * 
	 * <p>Normal behavior is only to report errors
	 * @param b - true to enable verbose mode, false to make it be quiet
	 * @author mjd 20090420
	 */
	public void setVerboseMode(boolean b);
	
	
	/**
	 * Errors and verbose descriptions are reported on this {@link PrintStream}.
	 * <p>Default: {@link System.err}.
	 * 
	 * @author mjd 20090420
	 * @param err
	 */
	public void setErrStream(PrintStream err);
	public PrintStream getErrStream();

	public MatrixHome getMatrixHome();
	public void setMatrixHome(MatrixHome matrixHome);
	public PhyloTreeHome getPhyloTreeHome();
	public void setPhyloTreeHome(PhyloTreeHome phyloTreeHome);
}
