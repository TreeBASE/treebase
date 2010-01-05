package org.cipres.treebase.service.admin;

import junit.framework.TestCase;
import org.cipres.treebase.dao.AbstractDAOTest;
import org.apache.log4j.Logger;
import org.cipres.treebase.domain.admin.Help;

/**
 * The class <code>HelpServiceImplTest</code> contains tests for the class
 * {@link <code>HelpServiceImpl</code>}.
 * 
 * @author mjd 20081117
 */
public class HelpServiceImplTest extends AbstractDAOTest {
	private static final Logger LOGGER = Logger.getLogger(HelpServiceImplTest.class);

	private HelpServiceImpl mFixture;

	/**
	 * Return an instance of the class being tested.
	 * 
	 * @return an instance of the class being tested
	 * 
	 * @see HelpServiceImpl
	 * 
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	public HelpServiceImpl getFixture() {
		
		return mFixture;
	}
	
	public void setFixture(HelpServiceImpl hsi) {
		
			mFixture = hsi;
		
	}
	/*@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
    */
	public void testNull() {
		assertNotNull(getFixture());
	}
	
	public void testDomainHome() {
		assertNotNull(getFixture().getDomainHome());
	}
	
	public void testFindByTag() {
		Help h;
		h = getFixture().findByTag("there is no such tag");
		assertNull(h);
	}
}
