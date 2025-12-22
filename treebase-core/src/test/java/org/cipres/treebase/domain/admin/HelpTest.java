package org.cipres.treebase.domain.admin;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.dao.admin.UserDAO;
import static org.junit.Assert.*;
import org.junit.Assume;
import org.junit.Test;


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
	@Autowired
	public void setHelpHome(HelpHome pHelpHome) {
		helpHome = pHelpHome;
	}

	/**
	 * Set up test data before each test.
	 */
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		
		// Create test Help object if it doesn't exist
		Help existingHelp = getHelpHome().findByTag("test");
		if (existingHelp == null) {
			Help testHelp = new Help("test");
			testHelp.setHelpText("Test help text");
			getHelpHome().save(testHelp);
			getHelpHome().flush();
		}
	}
	
	public Help getTestHelp() {
		Help test = getHelpHome().findByTag("test");
		Assume.assumeNotNull("getTestHelp - empty database, test skipped", test);
		return test;
	}

	@Test
	public void testGetHelp() {
		Help h = getTestHelp();
		assertNotNull(h.getHelpText());
		assertFalse(h.getHelpText().equals(""));
		//assertTrue(h.getHelpText().contains("Test")); data need to be clean.
	}

	@Test
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
