package org.cipres.treebase.util;

import org.cipres.treebase.ContextManager;
import org.springframework.orm.hibernate4.HibernateTransactionManager;

/**
 * Useful for forcing Hibernate to rebuild the database structure
 * 
 * <P>Set <tt>hibernate.hbm2ddl.auto=create</tt> in <tt>jdbc.properties</tt> and then run this.</p>
 * 
 * @author mjd 20081211
 *
 */
public class Trivial extends AbstractStandalone implements Standalone {

	HibernateTransactionManager htm;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setupContext();
		Standalone me =  (Standalone) ContextManager.getBean("trivial");
		warn("Hello, world");
	}
}
