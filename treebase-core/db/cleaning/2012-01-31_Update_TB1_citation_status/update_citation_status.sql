-- For old TreeBASE1 data (i.e. those with a value for tb_studyid) that have  
-- page numbers in their citation metadata, it is reasonable to assume
-- that these papers are published. This takes all such records that 
-- are still called "In Prep" and toggles them to "Published"

begin work;

UPDATE citation SET citationstatus_id = (SELECT citationstatus_id FROM citationstatus WHERE description = 'Published')
FROM study st JOIN citation ct USING (citation_id) 
JOIN citationstatus cs USING (citationstatus_id)
WHERE cs.description = 'In Prep'
AND length(st.tb_studyid) > 1
AND length(ct.pages) > 0 
AND citation.citation_id = ct.citation_id;

commit;
