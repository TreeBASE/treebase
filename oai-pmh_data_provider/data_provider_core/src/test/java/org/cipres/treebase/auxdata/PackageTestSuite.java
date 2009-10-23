package org.cipres.treebase.auxdata;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PackageTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.cipres.treebase.auxdata");
		//$JUnit-BEGIN$
		suite.addTestSuite(HeadlineParserTest.class);
		suite.addTestSuite(SimpleSectionParserTest.class);
		suite.addTestSuite(RDParserResultTest.class);
		suite.addTestSuite(AlternationParserTest.class);
		suite.addTestSuite(AssignmentSequenceParserTest.class);
		suite.addTestSuite(FilterParserTest.class);
		suite.addTestSuite(ValueAssignmentTest.class);
		suite.addTestSuite(AssignmentParserTest.class);
		suite.addTestSuite(ValueAssignmentMapTest.class);
		suite.addTestSuite(ValueTokenTest.class);
		suite.addTestSuite(TokenTest.class);
		suite.addTestSuite(AuxDataTest.class);
		suite.addTestSuite(TreebaseStreamTokenizerTest.class);
		suite.addTestSuite(ValueStudyTest.class);
		suite.addTestSuite(LazyListTest.class);
		suite.addTestSuite(SpecificHeadlineParserTest.class);
		suite.addTestSuite(ValueSequenceTest.class);
		suite.addTestSuite(RDParserFailureTest.class);
		suite.addTestSuite(ActionParserTest.class);
		suite.addTestSuite(RepeatedParserTest.class);
		suite.addTestSuite(ActionTest.class);
		suite.addTestSuite(RDParserTest.class);
		suite.addTestSuite(SingleTokenParserTest.class);
		suite.addTestSuite(FilterTest.class);
		suite.addTestSuite(TokenSequenceParserTest.class);
		//$JUnit-END$
		return suite;
	}

}
