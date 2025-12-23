
package org.cipres.treebase.service;

import org.cipres.treebase.core.CoreServiceLauncher;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for service tests using Spring test framework.
 * Migrated from AbstractDependencyInjectionSpringContextTests to Spring 4.x compatible testing.
 * 
 * Created on Oct 7, 2005
 * 
 * @author Jin Ruan
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:applicationContext-db-standalone.xml",
	"classpath:applicationContext-dao.xml",
	"classpath:applicationContext-service.xml"
})
@Transactional
public abstract class AbstractServiceTest {

	protected org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(getClass());

	/**
	 * constructor.
	 */
	public AbstractServiceTest() {
		super();
	}

}
