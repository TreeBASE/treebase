package org.treebase.oai;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	org.treebase.oai.web.PackageTestSuite.class
})
public class PackageTestSuite {
	// JUnit 4 suite - test classes are specified in @SuiteClasses annotation
}
