-- In the March migration, newly created treeblock records received NULL values
-- for taxonlabelset_id, causing the download of trees to fail and causing 
-- a selection of trees under the Trees tab to fail to show the related selection
-- of taxa after clicking on the Taxa tab. This query addresses the problem 
-- by updating the treeblock's taxonlabelset_id using a value from one of the matrices
-- related by way of an analysisstep


UPDATE treeblock SET taxonlabelset_id = newtlsid FROM 
(SELECT DISTINCT ON (tb.treeblock_id) tb.treeblock_id AS tblid, mx.taxonlabelset_id AS newtlsid
FROM matrix mx JOIN analyzeddata am ON (mx.matrix_id = am.matrix_id) 
JOIN analyzeddata at ON (am.analysisstep_id = at.analysisstep_id)
JOIN phylotree pt ON (at.phylotree_id = pt.phylotree_id)
JOIN treeblock tb ON (pt.treeblock_id = tb.treeblock_id)
WHERE tb.taxonlabelset_id IS NULL) AS nm
WHERE treeblock.treeblock_id = tblid


-- note Hilar's comment: "note that UPDATE ... FROM is non-standard SQL; normally 
-- one would alias the table being updated and have the update value be a subquery"

