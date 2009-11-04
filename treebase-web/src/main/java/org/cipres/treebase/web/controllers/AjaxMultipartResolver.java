


package org.cipres.treebase.web.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

public class AjaxMultipartResolver extends CommonsMultipartResolver {

	private ProgressListener progressListener = new AjaxProgressListener();

	public void setProgressListener(ProgressListener progressListener) {
		this.progressListener = progressListener;
	}

	public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request)
		throws MultipartException {
		String encoding = determineEncoding(request);
		FileUpload fileUpload = prepareFileUpload(encoding);
		if (progressListener != null) {
			fileUpload.setProgressListener(progressListener);
		}
		try {
			List fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
			MultipartParsingResult parsingResult = parseFileItems(fileItems, encoding);
			return new DefaultMultipartHttpServletRequest(request, parsingResult
				.getMultipartFiles(), parsingResult.getMultipartParameters());
		} catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
		} catch (FileUploadException ex) {
			throw new MultipartException("Could not parse multipart servlet request", ex);
		}
	}
}
