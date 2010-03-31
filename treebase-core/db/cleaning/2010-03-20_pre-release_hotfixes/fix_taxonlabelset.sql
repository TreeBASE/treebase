BEGIN TRANSACTION; 

\echo These TaxonLabelSet.study_ids will be updated:

select s.study_id, s.tb_studyid, m.matrix_id, tls.*
from study s, taxonlabelset tls, matrix m 
where s.study_id = m.study_id and tls.taxonlabelset_id = m.taxonlabelset_id
  and s.study_id <> tls.study_id
order by s.study_id; 


\echo =================== Updating =================== 

update taxonlabelset tls
   set study_id = s.study_id
from study s, matrix m 
where tls.taxonlabelset_id = m.taxonlabelset_id and m.study_id = s.study_id  
  and s.study_id <> tls.study_id
returning s.study_id, s.tb_studyid, m.matrix_id, tls.*; 


COMMIT; 