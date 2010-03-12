Miscellaneous data fixes applied after all TB1 nexus data was imported. 
To run them all, use 
    all_postload_fixes.sql 
    
 Were applied to treebasedev and treebasestage (the reference instance at the time)
 on 2010-03-12 -- see out_*.txt files for output.    
 
    
Notes on the individual fixes: 

user_tb1import.sql
   Assigns the special user 'tb1import' to all studies and submissions with null user_id. 

field_defaults.sql 
   Puts default values into phylotree.treekind, phylotree.treequality, and matrix.description. 

matrix_ntax_nchar.sql 
   Updates matrix.{ntax,nchar} to the actual counts of rows and columns.  

   