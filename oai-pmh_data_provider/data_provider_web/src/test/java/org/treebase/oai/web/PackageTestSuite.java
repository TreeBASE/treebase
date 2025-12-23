package org.treebase.oai.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	org.treebase.oai.web.command.PackageTestSuite.class,
	org.treebase.oai.web.controller.PackageTestSuite.class,
	org.treebase.oai.web.util.PackageTestSuite.class
})
public class PackageTestSuite {
	// JUnit 4 suite - test classes are specified in @SuiteClasses annotation
}
