To apply, run taxon_fixes_4-15-10.sql

--------

These statements help clean up the mapping between taxonlabel, taxonvariant, and 
taxon records. They include INSERTS in which the primary key is being supplied: 
please verify that this is okay, or will it confuse Hibernate's autoincrement? 

New taxon_id starts at 600001
New taxonvariant_id starts at 800000

Please verify that these numbers are beyond the current highest numbers. 

---------

Apply changes as soon as convenient, because it is not impossible that new submissions 
in the interim will map to taxonvariants that this script wants to delete. 