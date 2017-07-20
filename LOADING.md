Loading PostgreSQL data dump
============================



Loading TreeBASE v.1 data files
===============================

In the below steps, importing data from Nexus files (and some tabular data files) is described. The steps described below 
follow on the [installation instructions](INSTALL.md) that bootstrap a data loading application.

Copy scripts
------------

Copy the whole tb1load directory to the run location, i.e. the location from which loading will be performed. The rest of 
these instructions are for the run location. Tip: The run location should probably be at the server machine that runs the 
postgresl instance, to avoid sending large amounts of data over the network.

Specify DB credentials and the Mesquite location
------------------------------------------------

Edit tb2classes/jdbc.properties file, providing appropriate values for the properties: 
`jdbc.url`, `jdbc.username`, `jdbc.password` and `mesquite.folder_dir`

If the file does not exist, create it from tb2classes/jdbc.properties.example. `mesquite.folder_dir` should contain a path 
to the mesquite/ directory populated above (a relative path might work). 

Edit scripts/dbinfo.sh to provide the same DB credentials, to be used in Step 2. Note: The user that runs this step must 
have full DDL rights, as it creates and removes several tables, constraints and functions. 

Prepare the data 
----------------

Download the data to be migrated and place it in the data/ directory, under the exact file names as listed below. The scripts 
expect these locations and file names. 

    data/
        characters/            - contains Nexus matrix files  ***.nex 
        trees/                 - contains Nexus tree files ***.tre 
        dump.txt               - metadata about studies
    TI/                        - taxon intelligence, 3 tab-separated files
        taxa.tab 
        taxon_variants.tab 
        taxon_labels.tab
    citations.txt              - tab-separated EndNote file

Note: If you have several data sets, e.g. a testing set or several separate delta data sets, it could make sense to keep them in 
separate directories (with this structure) and link or move them to data/ when it is time to run the scripts.

Stage: Run the scripts
========================

There is a script, all_steps.sh that performs full loading. 

    cd scripts; ./all_steps.sh

However, each loading step can be run separately. Consider doing this, as well as intermittent backups, if failures at each step 
are likely. 

On sizable data, the scripts can take hours (especially Steps 1a,b). Consider running them via cron, by putting in crontab 
something like 

    35 11 * * * (cd ..../tb1load/scripts; ./step3_load_citations.sh ) &> migr_log.log

Steps 1a,b: load matrices and trees
-----------------------------------

    ./step1a_load_matrices.sh
    ./step1b_load_trees.sh 

These load matrices and trees into a study with the name "UPLOAD", creating one if needed. If you already have a "UPLOAD" study 
and do not want it to be affected, rename it first.

Code for Step1a appears to contain memory leak and may run out oh heap space midway. The matrix being uploaded at that time will 
be left inconsistent. It should be scraped out of the DB (use deletematrix.sql) and reloaded in a separate run of Step1a. 

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

This code does not work for "Book Section" enties. Comment them out and enter manually later. 
