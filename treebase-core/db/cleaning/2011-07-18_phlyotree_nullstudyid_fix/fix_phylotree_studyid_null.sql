begin work;

-- check to see how many trees lack a study_id
SELECT count(*) FROM phylotree WHERE study_id IS NULL;

-- for those trees that lack a study_id, 
-- update the study_id based on the study_id 
-- value found in the related taxonlabelset
UPDATE phylotree SET study_id = tls.study_id
FROM phylotree pt JOIN treeblock tb USING (treeblock_id)
JOIN taxonlabelset tls USING (taxonlabelset_id)
WHERE pt.study_id IS NULL
AND tls.study_id IS NOT NULL 
AND phylotree.phylotree_id = pt.phylotree_id;

-- check to see how many trees still lack a study_id
SELECT count(*) FROM phylotree WHERE study_id IS NULL;

-- for those trees that still lack a study_id,
-- update the study_id based on the legacy study_id found 
-- in the filename where the tree was pulled from

UPDATE phylotree SET study_id = sta.study_id
FROM phylotree pt JOIN study sta ON (sta.tb_studyid = substring(pt.nexusfilename from 1 for @(position('A' in pt.nexusfilename )- 1)) )
WHERE pt.study_id IS NULL
AND phylotree.phylotree_id = pt.phylotree_id;

-- check to see how many trees still lack a study_id
SELECT count(*) FROM phylotree WHERE study_id IS NULL;

-- if no more trees lack a study_id, we can now apply a new
-- constraint to ensure that trees always have a study_id

ALTER TABLE phylotree
ALTER COLUMN study_id SET NOT NULL;

commit;
