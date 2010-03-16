-- This is a fix to Bill's request to fill all remaining 
-- phylotree.treetype_ids with the code for 'single' 

update phylotree
   set treetype_id = 1 
where treetype_id is null
returning phylotree_id, tb1_treeid, treetype_id; 
