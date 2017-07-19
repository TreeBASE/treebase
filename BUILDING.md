![Logo of Treebase](https://treebase.org/treebase-web/images/TreeBASE.png)

How to re-compile the artifacts in this repository
==================================================

This document describes the steps taken to re-compile the binary classes needed to deploy the 
TreeBASE web application. Specifically, this includes:

- The key subprojects from the [treebase](https://github.com/TreeBASE/treebase) code repository, 
  i.e. the object-relational mappings implemented in `treebase-core` and the web application proper, 
  i.e. `treebase-web`, both bundled into a WAR archive for the Tomcat servlet container.
- The `PhyloWidget` applet for tree viewing and editing.

In addition, the headless mesquite library packaged as `mesquite-2.01.tb.jar` is made available, 
but it is not re-compiled because its maintenance doesn't fall under the TreeBASE project. All 
these artifacts, if successfully rebuilt, should be committed to 
[treebase-artifact](https://github.com/naturalis/treebase-artifact) so that the server provisioning
process can make use of it.

Setting up the building environment
-----------------------------------

Assuming you are on a Ubuntu 16.04LTS operating system, have the following Java installed:

    $ java -version
    openjdk version "1.8.0_131"
    OpenJDK Runtime Environment (build 1.8.0_131-8u131-b11-0ubuntu1.16.04.2-b11)
    OpenJDK 64-Bit Server VM (build 25.131-b11, mixed mode)

Also, ensure you have the maven installed as tested below. **NOTE**: if you get a message about 
`JAVA_HOME` not being set, or you get downstream error messages about `javac` missing once you're 
trying to compile, it means that your JDK was not fully installed and you only have the JRE part.
This is addressed by re-installing the JDK, e.g. `sudo apt install openjdk-8-jdk`.

    $ mvn -V
    Apache Maven 3.3.9
    Maven home: /usr/share/maven
    Java version: 1.8.0_131, vendor: Oracle Corporation
    Java home: /usr/lib/jvm/java-8-openjdk-amd64/jre
    Default locale: en_US, platform encoding: UTF-8
    OS name: "linux", version: "4.4.0-83-generic", arch: "amd64", family: "unix"

Compiling
---------

Then, check out and compile the source tree:

    $ sudo su
    # cd /usr/local/src
    # git clone https://github.com/TreeBASE/treebase.git
    # cd treebase
    # mvn compiler:compile

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
