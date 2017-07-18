This directory keeps track of schema patches applied to the DB as the application evolves, 
as well as occasional snapshots of the schema and pre-loaded dictionary-like data. 

Outline
======= 

The setup is inspired by database migrations in Ruby and Python, but is not nearly as comprehensive. 

The 'patches' directory stores SQL scripts (nnnn_descr_label.sql) that move the schema 
from one version to the next.  Occasionally, we put into the 'snaphots' subdirectory 
nnnn_SCHEMA...sql and nnnn_DATA...sql that capture the state of the DB after the 
patches/nnnn.sql patch has been applied.

Patches are sequentially numbered (by hand). If a snapshot nnnn is present, it reflects 
the state of the DB after patch nnnn was applied. 

The schema (staring with the very first 0000 snapshot) contains the 'versionhistory' table, 
which is used to keep track which patches have already been applied to a particular DB instance. 
This is particularly useful in multi-instance installations (e.g., when there are 
development, staging, production, etc., instances).  [See versionhistory.sql for the table's definition.]


Task-specific instructions
==========================

Creating a patch 
----------------

A patch is generally committed into SVN together with Java code for the application.  
It is expected that, for a given SVN version, the application code expects to work
w.r.t. the database containing all the patches from this version.  

Use the next sequential number and a short descriptive label to name a patch. 
To avoid numbering conflicts, developers must coordinate their patching activity. 

Each patch must start by an INSERT of a row describing the patch into the versionhistory table.  
(See existing patches for examples.)

Each patch will be executed as a transaction, so there is no need to insert 
transaction commands into the patch.  

Patches must not include any ownership or permissions commands: ownership and 
permissions are installation-specific, and therefore should be handled by the 
instance maintainer.

When an error is discovered in a recent patch, it may be ok to fix it and commit 
a new version under the same patch number, provided it is possible to adjust all DB 
instances to conform with the new patch version.  This can be doable when the patch
was only applied to the development DB instance.  When the patch with an error was already 
applied to production databases, it could be more prudent to develop a new, error-fixing patch. 

When adding a patch, add a line into init_db_uptodate.pg as well. 


Applying a patch
----------------

Apply each patch within a transaction, e.g.,  
psql -d yourdb -U yourusername
yourdb=> begin transaction;
yourdb=> \i nnnn_your_patch.sql
yourdb=> commit;  OR rollback;

After that, adjust ownership and permissions on newly created objects, if any, 
as required by the DB instance.  

Use your judgment on what to do in case the patch application generated errors 
and you had to roll back.  Most likely, someone has to work on improving the patch. 


Creating a current snapshot
---------------------------

Schema snapshots must not include any ownership or permissions commands, since these 
are installation-specific.  This command,  

pg_dump -h your.host.url -U your_username --format=p --no-owner --no-privileges --schema-only  yourdb >  nnnn_your_label.sql

creates a reasonably lean dump of the schema.  However, if the DB contained any objects 
not created by prior snapshots and patches, they should be removed by hand. 

Data snapshot is trickier.  The best bet right now is probably by hand-modifying the previous data snapshot. 

After creating a new snapshot, update init_db_uptodate.pg: change the names of the snapshot scripts
and remove all \i commands for the patch scripts. 


Creating a fresh DB
------------------

Use the init_db_uptodate.pg script, which should create the DB from the most recent snapshot 
and apply all the subsequent patches: 

Use the most recent snapshot: 

psql -d yourdb -U yourusername -f init_db_uptodate.pg

OR 

psql -d yourdb -U yourusername
yourdb=> begin transaction;
yourdb=> \i init_db_uptodate.pg
yourdb=> commit;  


Recommended development - production - staging workflow 
=======================================================

Local instances:  It is recommended that each developer has his own DB instance to use for writing 
and initially testing new versions of the application and schema patches. This ensures 
at least some basic compatibility between SQL and Java within a given SVN commit. 
These instances may contain only minimal data, as useful for the developer. 

Development instance:  The dev DB instance is frequently re-built to track 
the most recent SVN version of the application and DB patches. It is used to verify correctness 
of new versions and to communicate with non-programmer project participants. 
This instance should contain sizable and representative amount of data, 
but not necessarily as much or as good quality as the production instance. 

Staging instance:  The stage instance is a testbed for rolling out a new version on production.  
When development on dev reaches  release, a staging instance is created by cloning the 
current production instance.  This ensures that the possible new data quirks accumulated 
since the previous release are present.  Then patches spanning from the previous to the current 
release are applied, new version of the application is installed, and the whole system is tested
and verified as necessary.  If all works well, then the release roll-out may proceed on production. 
Otherwise, back to development.    

Production instance: This is the instance with the authoritative data that is constantly being 
accessed by external uses and can tolerate only brief periods of downtime.  Make a backup before
rolling out a new release and only roll out releases that were successfully verified on staging. 
After a new release roll-out, it makes sense to use the prod instance to create a new 
schema-and-dictionaries snapshot. 

  
