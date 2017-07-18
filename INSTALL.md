Installation instructions for the web application
=================================================

Instructions and scripts for installing the TreeBASE v.2 web application. Contributors to these instructions:

- Mark-Jason Dominus wrote an initial version (doc-mjd.txt) and Java data loading tools for the SDSC install
- Vladimir Gapeyev updated this in Feb-Mar 2010 for the NESCent install
- Rutger Vos ported the instructions to the install at Naturalis

The are two stages to the installation process: 

1. setting up working environment; and 
2. running the loading scripts 

The goal of the first stage is to extract a subset of the TB2 development environment sufficient to run loading on the server.
This circumvents the more complex task of re-creating a full-fledged development environment on the server. 

<!--
Contents of this directory
==========================

- [tb2jars](tb2jars) - placeholder for dependent JARs of Treebase2
- [tb2classes](tb2classes) - placeholder for Treebase2 classes
- [apple](apple) - placeholder for MacOSX-specific JARs 
- [mesquite](mesquite) - placeholder for Mesquite code 
- [data](data) - placeholder for the data files to be migrated 
- [scripts](scripts) - shell and SQL scripts to run for loading; the scripts invoke code from tb2classes, which relies on jars 
  in `tb2jars`, `mesquite`, and `apple`.
-->

Stage 1: Set up working environment
===================================

You must have checked out from the code repository and configured a working TB2 build environment (for the [treebase](../../../), 
[treebase-core](../../), and [treebase-web](../../treebase-web) projects). This directory (treebase-core/db/tb1load) is a part of 
this environment and some of the following instructions rely on its relative location.  

Bring in necessary Treebase2 code 
---------------------------------

    cd scripts; ./copy_tb2code.sh 

Assuming Treebase has been built, this script copies JARS from treebase-web/target/treebase-web/WEB-INF/lib/ to tb2jars/ copies contents 
of treebase-core/target/classes/ to tb2classes/

Bring in Mesquite code 
----------------------

Copy contents of your Mesquite installation to mesquite/

- If you already have a working TB2 instance running under tomcat, it should point to a Mesquite installation from its config 
  file $CATALINA_HOME/conf/Catalina/localhost/treebase-web.xml.  Copy the contents of that directory into mesquite/.  
- Alternatively, if you have successfully built TB2 with Maven, you will have Mesquite in 
  ~/.m2/repository/mesquite/mesquite/2.01.tb/mesquite-2.01.tb.jar. Unzip this archive and place its *contents* into mesquite/.

Bring in Apple JARs
-------------------

Only if the run location will not be a Mac OSX unix: JARs MRJToolkit.jar and ui.jar should be placed in tb2apple/

[These are Apple-specific JARs, apparently loaded by Mesquite.  Since Mesquite runs in "headless" mode, code from these JARs 
probably is not executed, so they are harmless.]

On a Mac OSX machine, these could be at: 

/System/Library/Java/Extensions/MRJToolkit.jar  /System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Classes/ui.jar 

Relocate to the run location 
----------------------------

*  Copy the whole tb1load/ directory (the current directory) to the run location, i.e. the location from which loading will be 
   performed.

   *** The rest of these instructions are for the run location. ***

[Tip: The run location should probably be at the server machine that runs the postgresl instance, to avoid sending large 
amounts of data over the network.]

Specify DB credentials and the Mesquite location
------------------------------------------------

*  Edit tb2classes/jdbc.properties file, providing appropriate values for the properties: 
  `jdbc.url`, `jdbc.username`, `jdbc.password` and `mesquite.folder_dir`

If the file does not exist, create it from tb2classes/jdbc.properties.example. 

mesquite.folder_dir should contain a path to the mesquite/ directory populated above (a relative path might work). 

*  Edit scripts/dbinfo.sh to provide the same DB credentials, to be used in Step 2. Note: The user that runs this step must have 
   full DDL rights, as it creates and removes several tables, constraints and functions. 

Stage 2: Prepare the data 
========================

Download the data to be migrated and place it in the data/ directory, under the exact file names as listed below. The scripts expect these locations and file names. 

  data/ 
    characters/            - contains Nexus matrix files  ***.nex 
    trees/                 - contains Nexus tree files ***.tre 
    dump.txt               - metadata about studies
    TI/                    - taxon intelligence, 3 tab-separated files
      taxa.tab 
      taxon_variants.tab 
      taxon_labels.tab 
    citations.txt           - tab-separated EndNote file

[Note: If you have several data sets, e.g. a testing set or several separate delta data sets, it could make sense to keep them in 
separate directories (with this structure) and link or move them to data/ when it is time to run the scripts.] 

Stage : Run the scripts
========================

There is a script, all_steps.sh that performs full loading. 

    cd scripts; ./all_steps.sh

However, each loading step can be run separately.  Consider doing this, as well as intermittent backups, if failures at each step 
are likely. 

On sizable data, the scripts can take hours (especially Steps 1a,b).  Consider running them via cron, by putting in crontab 
something like 
35 11 * * * (cd ..../tb1load/scripts; ./step3_load_citations.sh ) &> migr_log.log


Steps 1a,b: load matrices and trees
-----------------------------------
  ./step1a_load_matrices.sh
  ./step1b_load_trees.sh 

These load matrices and trees into a study with the name "UPLOAD", creating one if needed.  If you already have a "UPLOAD" study 
and do not want it to be affected, rename it first.

Code for Step1a appears to contain memory leak and may run out oh heap space midway.  The matrix being uploaded at that time will 
be left inconsistent.  It should be scraped out of the DB (use deletematrix.sql) and reloaded in a separate run of Step1a. 

Step 1c: fix counts in the uploaded data  
----------------------------------------

    ./step1c_fix_matrices.sh 
    ./step1d_fix_trees.sh 

Step 1e: load the dump containing metadata for studies 
------------------------------------------------------ 

    ./step1e_load_dump.sh 

Step 2: load taxon intelligence 
-------------------------------

    ./step2_taxon_intell.sh

or

   psql -f step2_taxon_intell.sql -h dbhost -U dbuser dbname 

This one requies the DB schema to be at least at Patch 0005.

Step 3: load citations
----------------------
 
     ./step3_load_citations.sh

This code does not work for "Book Section" enties.  Comment them out and enter manually later. 
