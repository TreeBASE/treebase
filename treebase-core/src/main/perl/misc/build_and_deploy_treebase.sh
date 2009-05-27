#!/bin/bash
# this should do the whole thing, run from inside ~
export JAVA_HOME=/usr/local/apps/java-1.5
export CATALINA_HOME=/fs/cipres/treebase/applications/apache-tomcat-5.5.26/
export WEBAPPS_DIR=$CATALINA_HOME/webapps/
export JDBC_HOME=$CATALINA_HOME
cd ~/treebase/trunk
svn up
mvn clean
mvn install -Dmaven.test.skip=true
rm -f $WEBAPPS_DIR/treebase-web.war
mv treebase-web/target/treebase-web.war $WEBAPPS_DIR/treebase-web.war
cp ~/PhyloWidget.jar $WEBAPPS_DIR/treebase-web/test/phylowidget/PhyloWidget.jar
pid=`ps -ax | grep 'treebase' | grep 'ClassLoaderLogManager' | sed -e 's/^ *//g' | cut -f1 -d' '`
kill -9 $pid
