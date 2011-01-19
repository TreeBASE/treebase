-- Studies that lack a study.releasedate value fail to return 
-- a <pubDate> in the PhyloWS RSS, and this causes Safari's 
-- RSS reader to subsitute this value with a last-checked timestamp. 
-- The result is that these records appear to be new additions
-- to the database, when in fact they are quite old. The NULL 
-- values in study.releasedate is an artefact of the data migration 
-- from TB1. Fix the releasedate and lastmodifieddate for studies 
-- that were migrated from TB1 by using the submission.createdate.

-- To see how the effect, use Safari to compare the "fixed" data in treebase-dev:
-- http://treebase-dev.nescent.org/treebase-web/phylows/study/find?query=dcterms.contributor=Huelsenbeck&format=rss1
-- with the results from the same request on production here:
-- http://purl.org/phylo/treebase/phylows/study/find?query=dcterms.contributor=Huelsenbeck&format=rss1

-- Test to count the number of records with a NULL value in study.releasedate
SELECT count(*) FROM study st LEFT JOIN submission sb USING (study_id) 
WHERE st.studystatus_id = 3 
AND st.releasedate IS NULL;

-- should result in a largish number like "2282"

begin work;

UPDATE study SET releasedate = sb.createdate, lastmodifieddate = sb.createdate 
FROM submission sb 
WHERE study.studystatus_id = 3 
AND study.releasedate IS NULL 
AND study.study_id = sb.study_id 

-- now let's check this again

SELECT count(*) FROM study st LEFT JOIN submission sb USING (study_id) 
WHERE st.studystatus_id = 3 
AND st.releasedate IS NULL;

-- result should be zero
-- if so:
commit;
