
package org.cipres.treebase.web.model;

import java.util.ArrayList;
import java.util.List;

/**
 * MultiFileUpload.java
 * 
 * This is the command object to handle multiple file upload on the same form
 * 
 * Created on May 18, 2006
 * @author lcchan
 *
 */
public class MultiFileBean {

	private List<FileBean> files = new ArrayList<FileBean>();
	
	public MultiFileBean() {
		files.add(new FileBean());
	}
	public List<FileBean> getFiles() {
		return files;
	}
	public void setFiles(List<FileBean> files) {
		this.files = files;
	}
}
