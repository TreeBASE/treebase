How to re-compile the artifacts in this repository
==================================================

This document describes the steps taken to re-compile the binary classes needed to deploy the TreeBASE web application.
Specifically, this includes:

- The object-relational mappings implemented in `treebase-core` and the web application proper, i.e. `treebase-web`, both
  bundled into a WAR archive for the Tomcat servlet container.
- The `PhyloWidget` applet for tree viewing and editing.

In addition, the headless mesquite library packaged as `mesquite-2.01.tb.jar` is made available here, but it is not 
re-compiled because its maintenance doesn't fall under the TreeBASE project.

Setting up the building environment
-----------------------------------

Assuming you are on a Ubuntu 16.04LTS operating system, have the following Java installed:

    $ java -version
    openjdk version "1.8.0_131"
    OpenJDK Runtime Environment (build 1.8.0_131-8u131-b11-0ubuntu1.16.04.2-b11)
    OpenJDK 64-Bit Server VM (build 25.131-b11, mixed mode)

And the following maven:

    $ mvn -V
    Apache Maven 3.3.9
    Maven home: /usr/share/maven
    Java version: 1.8.0_131, vendor: Oracle Corporation
    Java home: /usr/lib/jvm/java-8-openjdk-amd64/jre
    Default locale: en_US, platform encoding: UTF-8
    OS name: "linux", version: "4.4.0-83-generic", arch: "amd64", family: "unix"

Then, check out the source tree:

    $ sudo su
    # cd /usr/local/src
    # git clone https://github.com/TreeBASE/treebase.git
    # cd treebase
