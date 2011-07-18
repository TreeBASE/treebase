SELECT 
pg_catalog.nextval('study_id_sequence'), pg_catalog.nextval('submission_id_sequence'), 
  CASE 
    WHEN ( pg_catalog.currval('study_id_sequence') - pg_catalog.currval('submission_id_sequence') ) > 0 
      THEN pg_catalog.setval('submission_id_sequence', ( pg_catalog.currval('study_id_sequence') ) ) 
    WHEN ( pg_catalog.currval('study_id_sequence') - pg_catalog.currval('submission_id_sequence') ) < 0 
      THEN pg_catalog.setval('study_id_sequence', ( pg_catalog.currval('submission_id_sequence') ) )   
    WHEN ( pg_catalog.currval('study_id_sequence') - pg_catalog.currval('submission_id_sequence') ) = 0
      THEN 0
   END;