package org.cipres.treebase.domain.admin;

import java.sql.SQLException;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.dao.admin.UserDAO;


public class HelpTest extends AbstractDAOTest {

	private HelpHome helpHome;

	/**
	 * Return an instance of the class being tested.
	 * 
	 * @return an instance of the class being tested
	 * @see UserDAO
	 * @generatedBy CodePro at 10/6/05 1:26 PM
	 */
	public HelpHome getHelpHome() {
		return helpHome;
	}

	/**
	 * Injected by the super class based on autowiring by type.
	 * 
	 * @param pHelpHome
	 */
	public void setHelpHome(HelpHome pHelpHome) {
		helpHome = pHelpHome;
	}

	
	public Help getTestHelp() {
		Help test = getHelpHome().findByTag("test");
		assertNotNull(test);
		return test;
	}
	
	public void testGetHelp() {
		Help h = getTestHelp();
		assertNotNull(h.getHelpText());
		assertFalse(h.getHelpText().equals(""));
		assertTrue(h.getHelpText().contains("Test"));
	}
	
	public void testChangeMessage() throws SQLException {
		String newMessage = "new";

		{
			Help h = getTestHelp();
			h.setHelpText(newMessage);
			getHelpHome().save(h);
			getHelpHome().flush();
		}

		hibernateTemplate.clear();
		
		{
			Help h = getTestHelp();

			assertEquals(newMessage, h.getHelpText());
		}
	}
}
