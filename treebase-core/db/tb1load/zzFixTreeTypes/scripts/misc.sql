-- Number of trees with the null treetype
select count(*)
from phylotree
where treetype_id is null; 

-- The trees with the null treetype
select phylotree_id, tb1_treeid, treetype_id 
from phylotree
where treetype_id is null; 


-- All treetypes from the DB "dictionary"
select * from treetype

-- Distinct treetypes from the uploaded dump
select treetypename, count(*) from phtrtype group by treetypename

-- See treetype data from PhyloTree and the dump, side-by-side

select p.phylotree_id, p.tb1_treeid, p.treetype_id,
       Y.tb1_treeid, y.treetypename, 
       t.treetype_id, t.description
from phylotree p, phtrtype y, treetype t 
where p.tb1_treeid = y.tb1_treeid
  and y.treetypename = lower(t.description)


