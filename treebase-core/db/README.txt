[This will contain instructions on how to do and commit patches. 
For now, it's just a message from my email 2010-02-03  --VG]

"A setup/discipline for schema changes"

This brings up something that was on my back burner for a while:  
setting up a workflow and a file/directory structure to maintain  
schema patches. That would be something similar to migration scripts  
in Ruby or Python (but with scripts written in SQL and no automation).

The idea is that we store in SVN the initial snapshot of the DB schema  
and sequential patches that we have been developing w.r.t  
treebasedev.  These patches will be used to bring up-to-date  
treebasestage, and then treebaseprod.   After that we can erase the  
patches, refresh the snapshot, and continue for another cycle.   
Official TB2 code releases are to be declared at the start of a cycle,  
when there are no patches yet.  The snapshot is what should be used to  
create a fresh TB2 instance by someone who wants to run their own. 

Patches are to be sequentially numbered (by hand).  To keep track  
which DB instance has already been brought up to which patch version,  
I suggest creating a special table with a number column.  Each patch  
will have an UPDATE setting the number in the table to the patch's  
number.

If this sounds ok, I hope to set this up by end of tomorrow.


Some more details: 

* Patch naming: 0001_short_name.sql
* Each patch starts by incrementing the number in the version table.
* Each patch will be executed as a transaction, but there is no need to insert transaction commands into the patch.  
* Patches and snapshots must not include any ownership or permissions commands.
* Version table schema: 
CREATE TABLE versionhistory
(
  id serial NOT NULL,
  patchnumber integer,
  patchlabel character varying(63) NOT NULL,
  patchdescription character varying(1023),
  applied timestamp (0) without time zone DEFAULT now(),
  CONSTRAINT versionhistory_pkey PRIMARY KEY (id),
  CONSTRAINT versionhistory_unique_patchnumber UNIQUE (patchnumber)
)
COMMENT ON TABLE versionhistory IS 'VersionHistory table is NOT a part of the TreeBase application. It is only used within the development and deployment process to keep track of schema patches applied to a DB instance. ';


To get a current schema snapshot
================================

pg_dump -h treebase-dev.nescent.org -U treebase_app --format=p --no-owner --no-privileges --schema-only  treebasestage > schema_snapshot_treebasestage_2010-02-01.sql


To restore the schema into a new DB: 
====================================

psql -d yourdb -U yourusername -f nnn_SCHEMA_xxx.sql
psql -d yourdb -U yourusername -f nnn_DATA_xxx.sql

OR 

psql -d yourdb -U yourusername
yourdb=> begin transaction;
yourdb=> \i nnn_SCHEMA_xxx.sql
yourdb=> \i nnn_DATA_xxx.sql
yourdb=> end transaction 
