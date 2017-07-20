Deployment
----------

The TreeBASE web application needs to be deployed in a 'servlet container', i.e. a web server 
that can invoke Java functionality. This document describes the steps and gotchas involved in
this process. It is assumed that you meet the following requirements:

- the database has been [loaded](LOADING.md)
- the WAR for the OAI-PMH web service interface
- the WAR web application has been [compiled and bundled](BUILDING.md) such that it contains 
  a `context.xml` with the credentials for the database and a plausible location for headless 
  mesquite
- the required Apple JARs (described [here](INSTALL.md)) that headless mesquite needs 
- headless mesquite itself

Except for the database loading, all of these requirements are met for the specific deployment
at Naturalis in the [treebase-artifacts](https://github.com/naturalis/treebase-artifact)
repository. The following steps are geared towards that specific deployment.

### Installing the requirements

    $ sudo su
    # cd /usr/local/src
    # git clone https://github.com/naturalis/treebase-artifact

By cloning in this location, the following is accomplished:

- the Apple JARs end up in `/usr/local/src/treebase-artifact/mesquite/apple`
- headless mesquite ends up in `/usr/local/src/treebase-artifact/mesquite/`, which
  is what the JNDI variable `tb2/MesquiteFolder` is set to in the context.xml
- the WAR files for treebase and for the OAI-PMH service are ready to be deployed

What needs to happen next is that the WAR files are copied into the webapps folder of the
servlet container. On Ubuntu 16.04LTS with tomcat v.7, it would go like this:

    # cp /usr/local/src/treebase-artifact/treebase-web.war /var/lib/tomcat7/webapps/
    # cp /usr/local/src/treebase-artifact/data_provider_web.war /var/lib/tomcat7/webapps/

In addition, it is necessary to place the right JDBC driver jar for PostgreSQL in the
library folder of Tomcat itself. Like so:

    # cd /usr/share/tomcat7/lib && wget https://jdbc.postgresql.org/download/postgresql-42.1.3.jar

### Launching the server

The invocation of the tomcat server has a couple of additional parameters that must not be
forgotten:

- The Apple JARs that mesquite needs (`MRJToolkit.jar` and `ui.jar`) must be added to the class 
  path, note the argument `-classpath` below.
- The web application uses certain JSP constructs that Tomcat v.7 has become more strict than
  previous versions. This strictness is turned off using the 
  `-Dorg.apache.el.parser.SKIP_IDENTIFIER_CHECK=true`.

In the invocation below, all other arguments are default:

```shell
TOM=/usr/share/tomcat7/bin
APPLE=/usr/local/src/treebase-artifact/mesquite/apple
/usr/lib/jvm/default-java/bin/java \
    -Djava.util.logging.config.file=/var/lib/tomcat7/conf/logging.properties \
    -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager \
    -Djava.awt.headless=true -Xmx128m -XX:+UseConcMarkSweepGC \
    -Djava.endorsed.dirs=/usr/share/tomcat7/endorsed \
    -classpath $TOM/bootstrap.jar:$TOM/tomcat-juli.jar:$APPLE/MRJToolkit.jar:$APPLE/ui.jar \
    -Dcatalina.base=/var/lib/tomcat7 \
    -Dcatalina.home=/usr/share/tomcat7 \
    -Djava.io.tmpdir=/tmp/tomcat7-tomcat7-tmp \
    -Dorg.apache.el.parser.SKIP_IDENTIFIER_CHECK=true \
    org.apache.catalina.startup.Bootstrap start
```

Successful deployment should result in a functioning website, 
e.g. at http://145.136.242.33/treebase-web
