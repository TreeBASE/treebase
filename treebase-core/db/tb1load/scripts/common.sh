
TB2JARS=../tb2jars
TB2CLASSES=../tb2classes
APPLE=../apple
DATA=../data 

CP=$TB2JARS/spring-2.0.7.jar:$TB2JARS/hibernate-3.2.6.ga.jar:$TB2JARS/log4j-1.2.13.jar:$TB2JARS/commons-logging-1.1.jar:$TB2JARS/c3p0-0.9.1.2.jar:$TB2JARS/dom4j-1.6.1.jar:$TB2JARS/hibernate-annotations-3.3.1.GA.jar:$TB2JARS/jta-1.1.jar:$TB2JARS/hibernate-commons-annotations-3.0.0.ga.jar:$TB2JARS/acegi-security-1.0.1.jar:$TB2JARS/ejb3-persistence-1.0.1.GA.jar:$TB2JARS/commons-collections-3.1.jar:$TB2JARS/jdom-1.1.jar:$TB2JARS/postgresql-8.3-603.jdbc3.jar:$TB2JARS/ehcache-1.2.3.jar:$TB2JARS/cglib-nodep-2.1_3.jar:$TB2JARS/mesquite-2.01.tb.jar:$TB2JARS/antlr-2.7.2.jar:$TB2JARS/tolbaseclasses-mesquite.jar:$TB2JARS/nexml-1.5-SNAPSHOT.jar

CP=$CP:$APPLE/MRJToolkit.jar:$APPLE/ui.jar

CP=$CP:$TB2CLASSES

JAVA="java -Xmx2048M -cp $CP"
