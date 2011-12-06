begin work;

-- check to see how many matrices lack a study_id
SELECT count(*) FROM matrix WHERE study_id IS NULL;

-- for those matrices that lack a study_id, 
-- update the study_id based on the study_id 
-- value found in the related taxonlabelset

UPDATE matrix SET study_id = tls.study_id
FROM matrix mx JOIN taxonlabelset tls USING (taxonlabelset_id)
WHERE mx.study_id IS NULL
AND tls.study_id IS NOT NULL 
AND matrix.matrix_id = mx.matrix_id;

-- check to see how many trees still lack a study_id
SELECT count(*) FROM matrix WHERE study_id IS NULL;

-- if no more trees lack a study_id, we can now apply a new
-- constraint to ensure that trees always have a study_id

ALTER TABLE matrix
ALTER COLUMN study_id SET NOT NULL;

commit;
