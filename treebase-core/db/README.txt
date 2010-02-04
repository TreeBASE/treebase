[This will contain instructions on how to do and commit patches. 
For now, it's just a message from my email 2010-02-03  --VG]

"A setup/discipline for schema changes"

This brings up something that was on my back burner for a while:  
setting up a workflow and a file/directory structure to maintain  
schema patches. That would be something similar to migration scripts  
in Ruby or Python (but with scripts written in SQL and no automation).

The idea is that we store in SVN the initial snapshot of the DB schema  
and sequential parches that we have been developing w.r.t  
treebasedev.  These patches will be used to bring up-to-date  
treebasestage, and then treebaseprod.   After that we can erase the  
patches, refresh the snapshot, and continue for another cycle.   
Official TB2 code releases are to be declared at the start of a cycle,  
when there are no patches yet.  The snapshot is what should be used to  
create a fresh TB2 instance.

I suggest to create the directory treebase-code/db to store patches  
and snapshots.  (Another option would be a separate project, treebase- 
db, a a sibling of treebase-core and treebase-web.)

Patches are to be sequentially numbered (by hand).  To keep track  
which DB instance has already been brought up to which patch version,  
I suggest creating a special table with a number column.  Each patch  
will have an UPDATE setting the number in the table to the patch's  
number.

If this sounds ok, I hope to set this up by end of tomorrow.
