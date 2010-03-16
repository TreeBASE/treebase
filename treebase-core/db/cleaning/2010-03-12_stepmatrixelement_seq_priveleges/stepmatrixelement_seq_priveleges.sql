BEGIN transaction; 
select pg_owner('postgres', 'treebase_owner', 'stepmatrixelement_id_sequence', 'public'); 
select pg_grant('treebase_owner', 'ALL', 'stepmatrixelement_id_sequence', 'public'); 
select pg_grant('treebase_app',   'ALL', 'stepmatrixelement_id_sequence', 'public'); 
END transaction; 
