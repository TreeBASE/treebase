
package org.cipres.treebase.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.UploadFileResult;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * BulkUpload.java
 * 
 * Created on Jun 6, 2006
 * 
 * @author mjd
 * 
 */
public class BulkUpload  extends AbstractStandalone implements BulkUploadInterface {
	@SuppressWarnings(value = "unused")
	private static final Logger LOGGER = Logger.getLogger(BulkUpload.class);

	private MJDLogger mjdLog = null;
	private boolean force = false;

	/**
	 * Constructor. Need default constructor for Spring container.
	 */
	public BulkUpload() {
		super();
	}

	@SuppressWarnings(value = { "unused" })
	private class TrivialExceptionHandler implements UncaughtExceptionHandler {
		public void uncaughtException(Thread t, Throwable e) {
			e.printStackTrace();
			// Maybe explicitly shut down mesquite here
			// TODO: A much better implementation would be to destroy and build
			// the required objects and continue gracefully
			System.exit(1);
		}	
	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.BulkUpload#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	public static void main(String[] args) {
		setupContext();

		BulkUpload bu = new BulkUpload();
		Thread.setDefaultUncaughtExceptionHandler(bu.new TrivialExceptionHandler());

		BulkUploadInterface me = (BulkUploadInterface) ContextManager.getBean("bulkUpload");
		me.doIt(args);
	}
	
	public  void doIt(String[] args) {
		GetOpts<UnixOptions> go = new GetOpts<UnixOptions> (new UnixOptions("fl:"));
		UnixOptions opts = go.getOpts(args);
		
		setForce(opts.getBoolOpt("f"));
		if (opts.hasOpt("l")) {
			String logFileName = opts.getStringOpt("l");
			try {
				setLogger(new PrintStream(new File(logFileName)));
			} catch (FileNotFoundException e) {
				die("Log file '" + logFileName + "' not found");
			}
		} else {
			setLogger(System.err);
		}
		
		for (String arg : go.getArgs()) {
			try {
				doFileOrDirectory(arg);
			} catch (IOException e) {
				warn("I/O error processing file '" + arg + "': " + e.getMessage());
			}
		}
		System.exit(0);
	}
	
	public void doFileOrDirectory(String fileOrDirectoryName) throws IOException {
		File fileOrDirectory = new File(fileOrDirectoryName);
		if (fileOrDirectory.isDirectory()) {
			log("Processing directory '" + fileOrDirectoryName + "'");
			doFiles(fileOrDirectory.listFiles());
		} else if (fileOrDirectory.isFile()) {
			doFiles(fileOrDirectory);
		} else if (! fileOrDirectory.exists()) {
			log("File '" + fileOrDirectoryName + "' does not exist; skipping");
		} else {
			log("File '" + fileOrDirectoryName + "' exists, but is not a file or a directory; skipping");
		}
	}
		
	public void doFiles(File... files) throws IOException {
		List<File> undoneFiles = new LinkedList<File> ();
		
		for (File f : files) {
			String fn = f.getAbsolutePath();
			if (didFile(fn)) {
				log("Did file " + fn + " already; skipping");
				continue;
			} else {
				log("Doing file " + fn + " in this batch");
			}
			undoneFiles.add(f);
		}
		
		if (undoneFiles.isEmpty()) {
			if (files.length > 1)
				log("Files in this batch are already done");
			return;
		}
		else
			log("Doing " + undoneFiles.size() + "/" + files.length + " files in this batch.");

		User submitter = null; // No submitter

		//Study s = bulkUpload.addFileSimple(new File(dirName, fn), submitter, session);
		Study s = addFilesSimple(undoneFiles, submitter, null);
		if (s == null) {
			log("conversion of files failed");
		} else {
			log("conversion of files successful, saved nexus files as study id=" + s.getId() + " submission id="
					+ s.getSubmission().getId());
		}
	}

	private boolean didFile(String fn) {
		// Force processing of  this file, if requested
		if (this.force) return false;
		else return DidNexusFile.didFile(fn);
	}

	void log(String s) throws IOException {
		this.mjdLog.write(s);
	}

	void flush() {
		ContextManager.getTaxonLabelHome().flush();
	}
	
	Study addFileSimple(File f, User aUser, Session session) throws IOException {
		List<File> theFile = new LinkedList<File> ();
		theFile.add(f);
		
		return addFilesSimple(theFile, aUser, session);
	}
	
	public Study addFilesSimple(List<File> files, User aUser, Session session) throws IOException { 
		//Transaction t = session.beginTransaction();
		Submission sub = getPseudoSubmission();
		Study study = sub.getStudy();
		
		log("submission ID = " + sub.getId() + " study ID " + study.getId());

		UploadFileResult uploadResult = ContextManager.getSubmissionService().addNexusFilesJDBC(sub, files, null);
//		commitTransaction();
		log("added " + uploadResult.getMatrixCount() + " matrices and " + uploadResult.getTreeCount() + " trees.");

		return study;
	}
	
	private static Submission getPseudoSubmission() {
		return PseudoSubmission.getPseudoSubmission("TEST");
	}
	
	private class MJDLogger extends PrintStream {
		public MJDLogger(String filename) throws IOException {
			this(new FileOutputStream(filename));
		}
		public MJDLogger(OutputStream stream) {
			super(stream);
		}
		public void write(String s) throws IOException {
			long now = System.currentTimeMillis();
			super.print(String.format("%tc ", now));
			super.print(s + "\n");
			super.flush();
		}
	}

	public boolean getForce() {
		return force;
	}

	public void setForce(boolean b) {
		force = b;
	}

	public PrintStream getLogger() {
		return mjdLog;
	}

	public void setLogger(PrintStream logger) {
		this.mjdLog = new MJDLogger(logger);
	}
}
