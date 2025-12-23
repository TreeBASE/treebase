
package org.cipres.treebase.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixHome;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.hibernate.Hibernate;

/**
 * Utility for attaching raw nexus file data to treeas, matrices, and studies.
 * 
 * <p>The initial import of TB1 data into TB2 omitted the original nexus files, which are required for the
 * "download original nexus file" feature.  The data <i>in</i> the nexus files was parsed and installed
 * into the database, but the unmodified  nexus files themselves were not.  This utility corrects that.</p>
 * 
 * <p>Given a nexus filename on the command line, the program will match it with the trees or matrices that 
 * were derived from it and assoicate it in the database with the correct study.  Given a directory name, 
 * the program will process its contents similarly.</p>
 * 
 * Created on Feb 18, 2009
 * @author rvosa
 *
 */
public class RawNexusImporter extends AbstractStandalone implements RawNexusImporterInterface {

	public boolean testMode = false;
	public boolean verbose = false;
	public List<String> errors = new LinkedList<String> ();
	public MatrixHome matrixHome;
	public PhyloTreeHome phyloTreeHome;
	public static RawNexusImporterInterface importer;
	public PrintStream err = System.err;
	
	/**
	 * 
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		setupContext();
		importer = getBean();
		
		int firstFileIndex = 0;
		boolean bad = false;
		for (int i = 0; i < args.length && args[i].startsWith("-"); i++) {
			firstFileIndex = i+1;
			if (args[i].equals("-v")) importer.setVerboseMode(true);
			else if (args[i].startsWith("-test")) importer.setTestMode(true);
			else {
				warn("Unkown option flag '" + args[i] + "'");
				bad = true;
			}
		}
		if (bad || args.length == 0) {
			System.err.println("Usage: program [-v] [-testrun] nexusfile... nexusdir...");
			System.exit(1);		
		}

		boolean allOK = true;
		for (int i = firstFileIndex; i < args.length; i++) 
			allOK = doFileOrDirectory(args[i]) && allOK;
	
		System.exit(allOK ? 0 : 1);
	}
	
	/**
	 * Process this file or the files in this directory, as appropriate
	 * 
	 * <p>If argument names a plain file, process it.  If argument names a directory,
	 * process its contents.</p>
	 * 
	 * @param fileOrDirectoryName the path to the file or directory to process
	 * @return true if all files processed successfully, false otherwise; errors written to error stream
	 * @throws UnsupportedEncodingException
	 * @author mjd 20090420
	 */
	public static boolean doFileOrDirectory(String fileOrDirectoryName) throws UnsupportedEncodingException {
		File f = new File(fileOrDirectoryName);
		if (f.isDirectory()) return doDirectory(f);
		else return importer.doFile(f);
	}

	/**
	 * Process all relevant files in the specified directory
	 * 
	 * @param directory - the directory
	 * @return true if all files processed successfully, false otherwise; errors written to error stream
	 * @throws UnsupportedEncodingException
	 * @author mjd 20090420
	 */
	public static boolean doDirectory(File directory) throws UnsupportedEncodingException {
		boolean allOK = true;
		for (File f : directory.listFiles())
			allOK = importer.doFile(f) && allOK;
		return allOK;
	}

	/* (non-Javadoc)
	 * @link org.cipres.treebase.util.RawNexusMatrixImporterInterface#doFile(java.io.File)
	 */
	public boolean doFile(File file) throws UnsupportedEncodingException {
		String fileName = file.getName();
		if (! expectedFileName(fileName)) {
			err.println(fileName + ": skipping oddly-named file");
			return false;
		}
		
		String fileContents = TreebaseUtil.readFileToString(file);
		if (fileContents == null) return false;
		
		Set<Study> studies = new HashSet<Study> ();
		studies.addAll(handleMatricesInFile(file));
		studies.addAll(handleTreesInFile(file));
		
		for (Study s : studies) {
			if (liveMode())
				s.getNexusFiles().put(fileName, fileContents);
			verbose("Study S" + s.getId() + " now linked to nexus file " + fileName);
		}
		return true;
	}
	
	private boolean expectedFileName(String fileName) {
		return fileName.matches("M[xc0-9]+\\.nex")
		|| fileName.matches("A[xc0-9]+\\.tre")
		|| fileName.matches("S\\d+A\\d+\\.tre");
	}

	public Set<Study> handleMatricesInFile(File f) {
		Collection<Matrix> matrices = getMatrixHome().findByNexusFile(f.getName());
		Set<Study> studies = new HashSet<Study> ();
		
		for (Matrix m : matrices) {
			verbose("Pointing matrix M" + m.getId() + " at nexus file " + f.getName());
			if (liveMode())
				m.setNexusFileName(f.getName());
			if (m.getStudy() != null) studies.add(m.getStudy());
		}
		
		return studies;
	}

	public Set<Study> handleTreesInFile(File f) {
		Collection<PhyloTree> trees = getPhyloTreeHome().findByNexusFile(f.getName());
		Set<Study> studies = new HashSet<Study> ();
		
		for (PhyloTree t : trees) {
			if (liveMode())
				t.setNexusFileName(f.getName());
			verbose("Pointing tree T" + t.getId() + " at nexus file " + f.getName());
			if (t.getStudy() != null) studies.add(t.getStudy());
		}
		
		return studies;
	}

	/**
	 * Given a nexus file, read its contents into a {@link Clob}
	 * @param f the {@link File} containing Nexus data
	 * @return the {@link Clob}
	 * @throws IOException
	 */
	public Clob buildClob(File f) throws IOException {
		StringBuffer nexusString = new StringBuffer();

		char[] buf = new char[8192];
		InputStreamReader in = new InputStreamReader(new FileInputStream(f.getAbsolutePath()), "UTF8");
		int nr;
		while ((nr = in.read(buf)) > 0) {
			nexusString.append(new String(buf, 0, nr));
		}
		// Hibernate 4.x: use Session.getLobHelper() instead of Hibernate.createClob()
		Clob nexusContent = getSession().getLobHelper().createClob(nexusString.toString());
		return nexusContent;
	}

	public void setTestMode(boolean b) {
		testMode = b;
	}

	public MatrixHome getMatrixHome() {
		return matrixHome;
	}

	public void setMatrixHome(MatrixHome matrixHome) {
		this.matrixHome = matrixHome;
	}

	public PhyloTreeHome getPhyloTreeHome() {
		return phyloTreeHome;
	}

	public void setPhyloTreeHome(PhyloTreeHome phyloTreeHome) {
		this.phyloTreeHome = phyloTreeHome;
	}

	public boolean isTestMode() {
		return testMode;
	}	
	
	private boolean liveMode() {
		return ! isTestMode();
	}

	public boolean isVerboseMode() {
		return verbose;
	}

	public void setVerboseMode(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Issue warning message, but only in verbose mode
	 * 
	 * @param s - message
	 * @author mjd 20090420
	 */
	public void verbose(String s) {
		if (isVerboseMode()) getErrorStream().println(s);
	}
	
	public PrintStream getErrStream() {
		return err;
	}

	public void setErrStream(PrintStream err) {
		this.err = err;
	}

	public static RawNexusImporterInterface getBean() {
		return (RawNexusImporterInterface) ContextManager.getBean("rawNexusImporter");
	}

}