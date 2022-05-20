
package org.cipres.treebase.web.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * WebUtil.java
 * 
 * Created on Feb 21, 2007
 * 
 * @author lcchan
 * 
 */
public class WebUtil {

	private static final Logger LOGGER = LogManager.getLogger(WebUtil.class);
	private static final String separator = File.separator;

	/**
	 * read in result file and returns bytes array
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesFromFile(String dirPath, String filename) throws IOException {

		File file = new File(dirPath + separator + filename);
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			throw new IOException("File is too large.");
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
			&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	/**
	 * read content from a file and stored in a string
	 */
	public static String readFileContent(String filename) {
		StringBuilder content = new StringBuilder();
		BufferedReader in = null;

		// LOGGER.debug("Reading file content from " + filename);
		try {
			in = new BufferedReader(new FileReader(filename));
			String s;
			while ((s = in.readLine()) != null) {
				content.append(s);
			}
		} catch (IOException ex) {
			LOGGER.error("Error in reading file " + filename + ":" + ex.toString());
		} finally {
			if (in != null) try {
				in.close();
			} catch (Exception ex) {
				;
			}
		}
		return content.toString();
	}

	/**
	 * copy file from one to another
	 * 
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public static void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	/**
	 * send email message to user to notify user the link to pick up result
	 * 
	 * @param to: recipient of the message
	 * @param link: http link to get download the file
	 */
	public static void sendEmailAsText(
		String host,
		String from,
		String to,
		String subject,
		String message,
		String link) throws Exception {
		StringBuilder content = new StringBuilder();
		content.append(message);
		content.append(link);

		// create mail session
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		Session mailSession = Session.getDefaultInstance(props, null);

		// create email message header information
		MimeMessage mimeMessage = new MimeMessage(mailSession);
		mimeMessage.setFrom(new InternetAddress(from));
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		mimeMessage.setSubject(subject);
		mimeMessage.setText(content.toString());

		// send the message
		Transport.send(mimeMessage);
	}

	/**
	 * send email as html message
	 * 
	 * @param host
	 * @param from
	 * @param to
	 * @param link
	 * @throws Exception
	 */
	public static void sendEmailAsHtml(
		String host,
		String from,
		String to,
		String subject,
		String message,
		String link) throws Exception {
		// message buffer
		StringBuilder content = new StringBuilder();
		content.append(message); // can add html tags like <h1> if needed
		content.append("&nbsp;&nbsp;&nbsp;");
		String url = "<a href=\'" + link + "\'>" + "View Result</a>";
		content.append(url);

		// create mail session
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		Session mailSession = Session.getDefaultInstance(props, null);

		// create email message header information
		MimeMessage mimeMessage = new MimeMessage(mailSession);
		mimeMessage.setFrom(new InternetAddress(from));
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		mimeMessage.setSubject(subject);
		mimeMessage.setText(content.toString());

		// create multipart
		Multipart multipart = new MimeMultipart();
		// create email message body part
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(content.toString(), "text/html");
		multipart.addBodyPart(messageBodyPart);

		// put parts in message
		mimeMessage.setContent(multipart);

		// send the message
		Transport.send(mimeMessage);
	}

	/**
	 * check to see if the filename or diectory exists
	 * 
	 * @param filename
	 * @return
	 */
	public static boolean fileExists(String filename) {
		File file = new File(filename);
		boolean b = true;

		if (!file.exists()) {
			LOGGER.error("File doesn't exits : " + filename);
			b = false;
		}
		return b;
	}

	/**
	 * 
	 * @param response
	 * @param dirPath path of the download directory
	 * @param filename name of the file that was generated dynamically
	 * @return null for download purpose
	 * @throws Exception
	 */

	public static String downloadFile(HttpServletResponse response, String dirPath, String filename)
		throws Exception {
		return downloadFile(response,dirPath,filename,null);
	}
	
	public static String downloadFile(HttpServletResponse response, String dirPath, String filename, String contentType)
		throws Exception {
		// to download the file outside of the webapp, we need to call a servlet
		// that opens and read the file and display the output to the response object
	
		/**
		 * allow a popup dialog to downlaod the result file
		 */
		byte[] bytes = WebUtil.getBytesFromFile(dirPath, filename);
	
		response.reset();
		
		if ( null == contentType ) {
			// response.setContentType("application/octet-stream"); // this forces to be downloaded as a
			// binary file
			response.setContentType("application/x-unknown"); // this forces to be downloaded as type
			// of the extension (e.g NEX file for
			// *.nex)
			response.setHeader("Content-Disposition", "inline;filename=" + "\"" + filename + "\"");
			response.setContentType("text/plain"); // offer users option to view or save the file
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		}
		else {
			response.setContentType(contentType);
		}
		
		response.setContentLength(bytes.length);
	
		OutputStream outputStream = response.getOutputStream();
		outputStream.write(bytes, 0, bytes.length);
		outputStream.flush();
		outputStream.close();
	
		return null;
	}	

}
