


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
