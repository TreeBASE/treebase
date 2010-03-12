
\echo  'Assigning the special user tb1import to all studies and submissions with null user_id...'
\i user_tb1import.sql

\echo 'Putting default values into phylotree.treekind, phylotree.treequality, and matrix.description...'
\i field_defaults.sql 

\echo 'Updating matrix.{ntax,nchar} to the actual counts of rows and columns...'
\i matrix_ntax_nchar.sql 
