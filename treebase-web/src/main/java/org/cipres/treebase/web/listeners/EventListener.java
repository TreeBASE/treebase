/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */

package org.cipres.treebase.web.listeners;

import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.acegisecurity.context.HttpSessionContextIntegrationFilter;
import org.acegisecurity.context.SecurityContext;

import org.cipres.treebase.domain.admin.User;

/**
 * EventListener.java
 * 
 * Created on May 2, 2006
 * @author lcchan
 *
 */
public class EventListener implements ServletContextListener, HttpSessionAttributeListener {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(EventListener.class);

	private static final String USER_KEY = "user";
	private static final String EVENT_KEY = HttpSessionContextIntegrationFilter.ACEGI_SECURITY_CONTEXT_KEY;
	private transient ServletContext servletContext;
	
	/** 
	 * 
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		servletContext = sce.getServletContext();
	}

	/** 
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		servletContext = null;
	}

	/** 
	 * 
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void attributeAdded(HttpSessionBindingEvent event) {
		
		String eventName = event.getName();
		if (eventName.equals(EVENT_KEY)) {
			SecurityContext securityContext = (SecurityContext) event.getValue();
			User user = (User) securityContext.getAuthentication().getPrincipal();
			/* do something here */
		}
	}

	/** 
	 * 
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
	 * 
	 * There is probably no need to do anything.  When the user logs out, the session is invalidate
	 * which unbinds any objects bounds to it.
	 */
	public void attributeRemoved(HttpSessionBindingEvent event) {
		/* 
		String eventName = event.getName();
		if (eventName.equals(EVENT_KEY)) {
			SecurityContext securityContext = (SecurityContext) event.getValue();
			User use = (User) securityContext.getAuthentication().getPrincipal();
			HttpSession session = (HttpSession) event.getSession();
			event.getSession().removeAttribute(USER_KEY);
		}*/
	}

	/** 
	 * 
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void attributeReplaced(HttpSessionBindingEvent se) {
		//FIXME: attributeReplaced

	}

}
