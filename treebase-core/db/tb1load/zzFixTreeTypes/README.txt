Uploading in Step 1e somehow failed to set phylotree.treetype_id, at least for some of the records.  

This directory contains scripts to upload this data from a file dumped by Bill from TB1. 
The scripts assume that the dump is in data/tree_types.txt 

The original data file was downloaded from
http://www.treebase.org/treebase/migration/Mar-10/tree_types.txt
It had to be modified, with Bill's approval: 
  -   "Simple Tree" --> "single"
  -   T391c2x7x96c15c45c54 got annotated "simple" (it had a blank)
  
  
To run, in a single transaction,  

$ cd zzFixTreeTypes/scripts 
$ psql yourdatabase 
$ \i all.sql 


The file misc.sql contains a few useful diagnostic queries. 

