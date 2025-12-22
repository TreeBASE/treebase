Building the artifacts in this repository
=========================================

This document describes the steps taken to compile the classes of the TreeBASE project proper.
Specifically, this includes:

- the object-relational mappings implemented in [treebase-core](treebase-core), and 
- the web application, i.e. [treebase-web](treebase-web)

which are both bundled into a WAR archive for the Tomcat servlet container. These artifacts, once 
successfully rebuilt, are intended to be committed to 
[treebase-artifact](https://github.com/naturalis/treebase-artifact) so that the server provisioning
process can make use of it.

Setting up the building environment
-----------------------------------

Assuming you are on a modern Linux operating system (Ubuntu 20.04 LTS or later), you need to have Java 17 LTS installed. You can verify your Java installation with:

    $ java -version
    openjdk version "17.0.9" 2023-10-17
    OpenJDK Runtime Environment (build 17.0.9+9-Ubuntu-120.04)
    OpenJDK 64-Bit Server VM (build 17.0.9+9-Ubuntu-120.04, mixed mode, sharing)

Any Java 17 LTS version should work. If you don't have Java 17 installed, you can install it with:

    sudo apt-get update
    sudo apt-get install openjdk-17-jdk

Also, ensure you have the maven installed as shown below (don't worry if there are any additional
lines where maven tries to compile a project in the current location, e.g. `Scanning for projects...`
and so on). 

**NOTE**: if you get a message about `JAVA_HOME` not being set, or you get downstream error messages 
about `javac` missing once you're trying to compile, it means that your JDK was not fully installed 
and you only have the JRE part. This is addressed by (re-)installing the JDK, e.g. 
`sudo apt install openjdk-17-jdk`.

    $ mvn -V
    Apache Maven 3.8.7
    Maven home: /usr/share/maven
    Java version: 17.0.9, vendor: Ubuntu
    Java home: /usr/lib/jvm/java-17-openjdk-amd64
    Default locale: en_US, platform encoding: UTF-8
    OS name: "linux", version: "5.15.0-83-generic", arch: "amd64", family: "unix"

If there is no maven on your system, install it with `sudo apt install maven`.

Finally, you will also need `git`, which you install with `sudo apt install git`, resulting in:

    $ git --version
    git version 2.7.4

Any recent version of git is probably fine.

Compiling
---------

Then, check out the source tree:

    $ git clone https://github.com/TreeBASE/treebase.git

...and compile it using the `compiler:compile` maven goal executed in the top level directory:

    $ cd treebase
    $ mvn compiler:compile

This should result in a successful build:

    ...previous omitted...
    [INFO] ------------------------------------------------------------------------
    [INFO] Reactor Summary:
    [INFO] 
    [INFO] Treebase ........................................... SUCCESS [  0.396 s]
    [INFO] treebase-core ...................................... SUCCESS [  3.234 s]
    [INFO] treebase-web ....................................... SUCCESS [ 11.620 s]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 15.445 s
    [INFO] Finished at: 2017-07-19T11:58:57+00:00
    [INFO] Final Memory: 34M/390M
    [INFO] ------------------------------------------------------------------------

Bundling
--------

Assuming we have compiled successfully, this means that all the dependencies were found
and there were no errors in the code. What needs to happen next is to bundle the compiled
code into artifacts that can be deployed. This bundle will also include the configuration
files to connect to the database. Specifically, you need to make neighbouring copies of 
the files [context.xml.example](treebase-web/src/main/webapp/META-INF/context.xml.example) 
and [jdbc.properties.example](treebase-core/src/main/resources/jdbc.properties.example),
remove the `.example` suffix, and edit them to insert the right values for the 
variables, e.g. database passwords. The currently used configuration values are logged in 
the private repo TreeBASE/treebase-config. Once that is done, the bundling is accomplished 
with the `package` goal, i.e.

    $ mvn package -Dmaven.test.skip=true

This should produce an output like so:

    ...previous omitted...
    [INFO] Packaging webapp
    [INFO] Assembling webapp [treebase-web] in [/usr/local/src/treebase/treebase-web/target/treebase-web]
    [INFO] Processing war project
    [INFO] Copying webapp resources [/usr/local/src/treebase/treebase-web/src/main/webapp]
    [INFO] Webapp assembled in [512 msecs]
    [INFO] Building war: /usr/local/src/treebase/treebase-web/target/treebase-web.war
    [INFO] WEB-INF/web.xml already added, skipping
    [INFO] ------------------------------------------------------------------------
    [INFO] Reactor Summary:
    [INFO] 
    [INFO] Treebase ........................................... SUCCESS [  0.001 s]
    [INFO] treebase-core ...................................... SUCCESS [  3.725 s]
    [INFO] treebase-web ....................................... SUCCESS [  4.654 s]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 8.512 s
    [INFO] Finished at: 2017-07-19T15:11:21+00:00
    [INFO] Final Memory: 35M/569M
    [INFO] ------------------------------------------------------------------------

The salient result of this packaging process is the production of a web archive (WAR)
that contains all the components, i.e.: 

- the compiled classes, HTML/JSP documents, and any other assets such as CSS stylesheets
  and JavaScript libraries from [treebase-web](treebase-web)
- the compiled classes of [treebase-core](treebase-core), which should be bundled into
  the WAR as a JAR archive (i.e. in `WEB-INF/lib/treebase-core-1.0-SNAPSHOT.jar`), 
- all the pre-requisite JARs,
- all configuration files, appropriately edited.

This means that the total size of the WAR archive should be around 59Mb.
