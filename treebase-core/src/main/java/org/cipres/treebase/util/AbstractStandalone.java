


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
