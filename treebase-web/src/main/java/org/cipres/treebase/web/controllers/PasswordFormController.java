
package org.cipres.treebase.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserService;

/**
 * PasswordFormController.java
 * 
 * Class to retrieve user password and mail it to the user
 * 
 * Created on June 15, 2006
 * 
 * @author lcchan
 * 
 */
public class PasswordFormController extends BaseFormController {
	private static final Logger LOGGER = Logger.getLogger(PasswordFormController.class);

	private UserService mUserService;
	private MailSender mailSender;
	private SimpleMailMessage message;

	/**
	 * Set the SimpleMailMessage field.
	 */
	public void setMessage(SimpleMailMessage pNewSimpleMailMessage) {
		this.message = pNewSimpleMailMessage;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Return the UserService field.
	 * 
	 * @return UserService mUserService
	 */
	public UserService getUserService() {
		return mUserService;
	}

	/**
	 * Set the UserService field.
	 */
	public void setUserService(UserService pNewUserService) {
		mUserService = pNewUserService;
	}

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		User user = (User) command;

		String username = user.getUsername().trim();
		String userEmail = user.getTmpEmailAddress().trim();
		if ( TreebaseUtil.isEmpty(username) && TreebaseUtil.isEmpty(userEmail) ) {
			request.setAttribute("errors", "Please provide either user name or email.");
			return showForm(request, response, errors);
		}
				
		// look up user information
		User u = null;
		if (!TreebaseUtil.isEmpty(username)) {
			u = mUserService.findUserByName(username);
		} 
		else {
			List<User> users = mUserService.findUserByEmail(userEmail);			
			if (users != null && !users.isEmpty()) {
				u = users.iterator().next();
			}
		}
		
		if ( u == null ) {
			request.setAttribute("errors", "User name: " + username
				+ ", email: " + userEmail
				+ ", you have provided does not exist.");
			return showForm(request, response, errors);
		}

		if ( LOGGER.isDebugEnabled() ) {
			LOGGER.debug("Server URL: " + generateServerURL(request));
		}

		//SimpleMailMessage msg = new SimpleMailMessage(this.message);
		// XXX start of new stuff
		
		// create mail session
		Properties props = new Properties();
		props.put("mail.smtp.host", TreebaseUtil.getSmtpHost()); 
		Session mailSession = Session.getDefaultInstance(props,null);
		
		// create email message header information
	    MimeMessage message = new MimeMessage(mailSession);
	    message.setRecipient(Message.RecipientType.TO, new InternetAddress(u.getEmailAddressString(),u.getLastName() + ", " + u.getFirstName()));
	    message.setFrom(new InternetAddress("admin@treebase.org", "TreeBase administrator"));
	    InternetAddress[] replyTo = { new InternetAddress("admin@treebase.org", "TreeBase administrator") };
	    message.setReplyTo(replyTo);
	    message.setRecipient(Message.RecipientType.BCC, new InternetAddress("passwords@treebase.org", "TreeBase passwords"));
	    message.setSubject("TreeBASE Password", "UTF8");	    
	    message.setText( 
	    	"To: " + u.getEmailAddressString() + "\n"
	    	+ "Subject: TreeBASE Password\n\n"
	    	+ "Thank you for requesting username/password from TreeBASE.\n\nHere is your username: " 
	    	+ u.getUsername() + "\nYour password: " 
	    	+ u.getPassword()
	    	+ "\n\nTreebase site: " 
			+ TreebaseUtil.getSiteUrl()); 
//	    	+ "\n\nTreebase login site: " 
//			+ generateServerURL(request) 
//			+ "/login.jsp");	    
	    
	    // send the message
	    Transport.send(message);
	    request.setAttribute("messages", getMessageSourceAccessor().getMessage(
		"user.password.sent.successful")
		+ " (" + u.getEmailAddressString() + ")");
	    // XXX end of new stuff
		
	    /*
	    // XXX old spring-style mail sending
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(u.getEmailAddressString());
		msg.setFrom("admin@treebase.org");
		msg.setReplyTo("admin@treebase.org");
		msg.setBcc("passwords@treebase.org");
		msg.setSubject("TreeBASE Password ");
		msg.setText("Thank you for requesting username/password from TreeBASE.\n\nHere is your username: " + u.getUsername() + "\nYour password: " + u.getPassword()
			+ "\n\nTreebase login site:\t " + generateServerURL(request) + "/login.jsp");

		try {
			mailSender.send(msg);
			request.setAttribute("messages", getMessageSourceAccessor().getMessage(
				"user.password.sent.successful")
				+ " (" + u.getEmailAddressString() + ")");
			LOGGER.debug(msg.toString());
		} catch (MailException ex) {
			ex.printStackTrace();
			LOGGER.info(ex.getMessage());
			request.setAttribute("messages", getMessageSourceAccessor().getMessage(
				"user.password.sent.failed")
				+ " (" + u.getEmailAddressString() + ")");
		}
		*/
		
		return showForm(request, response, errors);
	}

	private String generateServerURL(HttpServletRequest request) {
		String serverURL = null;

		StringBuilder bldrURL = new StringBuilder(request.getScheme());
		bldrURL.append("://").append(request.getServerName()).append(":").append(
			request.getServerPort());
		bldrURL.append(request.getContextPath());

		serverURL = bldrURL.toString();
		bldrURL = null;
		return serverURL;
	}

}
