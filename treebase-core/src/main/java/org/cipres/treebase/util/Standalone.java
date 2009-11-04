
package org.cipres.treebase.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


/**
 * @author mjd 20090223
 *
 */
public interface Standalone {
	public void bindSession();
	public void bindSession(Session sess);
	public void unbindSession();
	public SessionFactory getSessionFactory();
	public void setSessionFactory(SessionFactory sf);
	public void doIt(String[] args);
}
