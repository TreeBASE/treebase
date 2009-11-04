
package org.cipres.treebase.web.model;

/**
 * FileUpload.java
 * 
 * Command object for file upload form
 * 
 * Created on May 18, 2006
 * @author lcchan
 *
 */
public class FileBean {

	private byte[] data;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}	
}
