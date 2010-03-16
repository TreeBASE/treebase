
begin transaction; 

\i upload_tmp.sql 
\i update_treetypes.sql
\i drop_tmp.sql 

end transaction; 