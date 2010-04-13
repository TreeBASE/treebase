begin work;

SELECT phylotree_id, study_id from phylotree where phylotree_id = 7238;

-- should result in a NULL value for study_id

UPDATE phylotree pt
   SET study_id = sub.study_id
FROM treeblock tb JOIN sub_treeblock stb USING (treeblock_id)
JOIN submission sub USING (submission_id)
WHERE pt.treeblock_id = tb.treeblock_id
AND pt.study_id IS NULL
AND sub.study_id > 10000;

-- should result in about 10 records updated

SELECT phylotree_id, study_id from phylotree where phylotree_id = 7238;

-- should result in "10349" for study_id

commit;
