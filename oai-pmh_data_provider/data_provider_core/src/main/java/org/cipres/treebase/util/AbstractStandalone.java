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



package org.cipres.treebase.util;

import java.io.PrintStream;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.core.CoreServiceLauncher;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public abstract class AbstractStandalone extends HibernateDaoSupport implements Standalone {
//	SessionFactory sessionFactory;
	static PrintStream errorStream = System.err;
	static boolean setup = false;
	
	public static void setupContext() {
		if (! setup) {
			ContextManager.createSpringContext(CoreServiceLauncher.getSpringConfigurations());

			setup = true;
		}
	}
	
	public void bindSession() {
		if (sessionIsBound()) return;
		bindSession(getSession(true));
	}
	
	public void bindSession(Session sess) {
		if (sessionIsBound()) return;
		TransactionSynchronizationManager.bindResource(getSessionFactory(), new SessionHolder(sess)); 
	}
	
	public void unbindSession() {
		if (! sessionIsBound()) return;
		TransactionSynchronizationManager.unbindResource(getSessionFactory());
	}
	
	public boolean sessionIsBound() {
		return TransactionSynchronizationManager.getResource(getSessionFactory()) != null;
	}
	
	protected Object getBean(String pBeanClassName) {
		return ContextManager.getBean(pBeanClassName);
	}
	
	protected Class<ContextManager> getContextManager() {
		return ContextManager.class;
	}

	public Transaction beginTransaction() {
		setupContext();
		return getCurrentSession().beginTransaction();
	}

	public void commitTransaction() {
		getCurrentSession().getTransaction().commit();
	}
	
	public Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	public static void warn(String s) {
		getErrorStream().println(s);
	}
	
	public static void die(String s) {
		warn(s);
		System.exit(1);
	}

	public static PrintStream getErrorStream() {
		return errorStream;
	}

	public static void setErrorStream(PrintStream errorStream) {
		AbstractStandalone.errorStream = errorStream;
	}
	
	public void doIt(String [] args) { }
}
