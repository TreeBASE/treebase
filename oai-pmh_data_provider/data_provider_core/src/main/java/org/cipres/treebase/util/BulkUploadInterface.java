package org.cipres.treebase.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.study.Study;
import org.hibernate.Session;

public interface BulkUploadInterface extends Standalone {
	public Study addFilesSimple(List<File> files, User aUser, Session session) throws IOException;
	public void doFileOrDirectory(String fileOrDirectoryName) throws IOException;

	public void setForce(boolean b);
	public boolean getForce();
	public PrintStream getLogger() ;
	public void setLogger(PrintStream logger);
}
