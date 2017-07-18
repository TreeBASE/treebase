Installation instructions for the web application
=================================================

The goal of these instructions is to guide you through the process of setting up the TreeBASE v.2 web application
on a Linux/Unix-like server and importing data into it. As such, these instructions DO NOT cover how to set up a
development environment (which would involve wrestling with Eclipse) or a continuous integration environment, where
something like Travis and JUnit would come into play.

Contributors to these instructions:

- Mark-Jason Dominus wrote an initial version (doc-mjd.txt) and Java data loading tools for the SDSC install
- Vladimir Gapeyev updated this in Feb-Mar 2010 for the NESCent install
- Rutger Vos ported the instructions to the install at Naturalis in July 2017

The goal of the instructions is to extract a subset of the TB2 development environment sufficient to run loading on the 
server. This circumvents the more complex task of re-creating a full-fledged development environment on the server. 

Set up working environment
--------------------------

You must have checked out from the code repository and configured a working TB2 build environment (for the [treebase](#), 
[treebase-core](treebase-core), and [treebase-web](treebase-web) projects). This will involve setting up a Java build 
environment, i.e. compilers and `maven`; installing and configuring PostgreSQL.

Target directory layout
-----------------------
For the remainder of the installation, it is assumed that the following directory layout exists:

- `tb2jars` - placeholder for pre-requisite JARs
- `tb2classes` - placeholder for compiled TreeBASE v.2 classes
- `mesquite` - placeholder for headless Mesquite code 
- `apple` - placeholder for MacOSX-specific JARs, needed by Mesquite, even on other platforms
- `data` - placeholder for the data files to be migrated 
- `scripts` - shell and SQL scripts to run for loading; the scripts invoke code from tb2classes, which relies on jars 
  in `tb2jars`, `mesquite`, and `apple`.

Copy compiled TreeBASE classes and JARs to target directories
-------------------------------------------------------------

Assuming TreeBASE has been built, the script [copy_tb2code.sh](treebase-core/db/tb1load/scripts/copy_tb2code.sh)
copies pre-requisite JARS from `treebase-web/target/treebase-web/WEB-INF/lib/` to `tb2jars` and the compiled contents 
of `treebase-core/target/classes/` to `tb2classes`

Copy headless Mesquite classes to target directories
----------------------------------------------------

Copy contents of your Mesquite installation to `mesquite`. If you have successfully built TB2 with Maven, you will have 
Mesquite in `~/.m2/repository/mesquite/mesquite/2.01.tb/mesquite-2.01.tb.jar`. Unzip this archive and place its *contents* 
into `mesquite`. Alternatively, if you already have a working TB2 instance running under tomcat, it should point to a 
Mesquite installation from its config file `$CATALINA_HOME/conf/Catalina/localhost/treebase-web.xml`. Copy the contents of 
that directory into `mesquite`.  

Copy Apple JARs to target directories
-------------------------------------

Regardless the OS on which it runs, Mesquite requires two MacOSX-specific Java jars: `MRJToolkit.jar` and `ui.jar`. Since Mesquite 
runs in "headless" mode, code from these JARs probably is not executed, so they are harmless but they have to be present in `apple`.
On a Mac OSX machine, these could be at: 

- `/System/Library/Java/Extensions/MRJToolkit.jar`
- `/System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Classes/ui.jar`

Specify DB credentials and the Mesquite location
------------------------------------------------

Edit `tb2classes/jdbc.properties` file, providing appropriate values for the properties: 

- `jdbc.url`
- `jdbc.username`
- `jdbc.password`
- `mesquite.folder_dir`

If the file does not exist, create it from `tb2classes/jdbc.properties.example`. 

`mesquite.folder_dir` should contain a path to the `mesquite` directory populated above (a relative path might work). 
