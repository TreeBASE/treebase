-- In the March migration, newly imported records were parked under a study_id
-- with study_id 10215. The taxonlabelset table failed to have its study_id 
-- remapped to the newly created studies, so instead it retained the original 
-- temporary study_id. This query updates the study_id field in the taxonlabelset


UPDATE taxonlabelset SET study_id = mx.study_id
FROM matrix mx JOIN taxonlabelset tls USING (taxonlabelset_id)
WHERE tls.study_id = 10215
AND mx.study_id <> 10215
AND taxonlabelset.taxonlabelset_id = tls.taxonlabelset_id


