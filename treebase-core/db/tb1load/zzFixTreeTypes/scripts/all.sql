
begin transaction; 

\i upload_tmp.sql 
\i update_treetypes.sql
\i drop_tmp.sql 
\i fill_orphan_nulls.sql

end transaction; 