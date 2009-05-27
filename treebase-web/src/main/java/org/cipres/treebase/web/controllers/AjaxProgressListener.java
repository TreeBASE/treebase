/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */



package org.cipres.treebase.web.controllers;

import java.text.NumberFormat;

import org.apache.commons.fileupload.ProgressListener;

import org.cipres.treebase.web.model.UploadInfoBean;

public class AjaxProgressListener implements ProgressListener {

	private static long bytesTransferred = 0;
	private static long fileSize = -100;
	private long totalBytesRead = 0;
	private long fiveKBRead = -1;
	private UploadInfoBean uploadInfoBean = null;

	public AjaxProgressListener() {
		uploadInfoBean = new UploadInfoBean();
	}

	// function called from javascript to retrieve the status of the upload
	public UploadInfoBean getStatus() {
		// per looks like 0% - 100%, remove % before submission
		uploadInfoBean.setTotalSize(fileSize / 1024);
		uploadInfoBean.setBytesRead(totalBytesRead / 1024);
		System.out.println("KB read: " + bytesTransferred / 1024);
		System.out.println("File size: " + fileSize);
		String per = NumberFormat.getPercentInstance().format(
			(double) bytesTransferred / (double) fileSize);
		System.out.println("% Of File Uploaded: "
			+ Integer.parseInt(per.substring(0, per.length() - 1)));
		uploadInfoBean.setPercentage(Integer.parseInt(per.substring(0, per.length() - 1)));
		return uploadInfoBean;
	}

	// public int getStatusPercentage() {
	// System.out.println("STATUS: " + getStatus().getPercentage());
	// return getStatus().getPercentage();
	// }

	// function called by multipartResolver to notify the change of the upload status
	public void update(long bytesRead, long contentLength, int items) {
		// update bytesTransferred and fileSize (if required) every 5 KB is read
		long fiveKB = bytesRead / 5120;
		totalBytesRead = bytesRead;
		if (fiveKBRead == fiveKB) {
			return;
		}
		fiveKBRead = fiveKB;
		bytesTransferred = bytesRead;
		if (fileSize != contentLength) {
			fileSize = contentLength;
		}
	}

}
