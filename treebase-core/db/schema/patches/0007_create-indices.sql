insert into versionhistory(patchnumber, patchlabel, patchdescription) 
       values (7, 'create-indices', 
       'Create additional indices to improve query performance'); 

CREATE INDEX discretecharstate_phylochar_id_idx ON discretecharstate USING btree (phylochar_id);
CREATE INDEX matrixcolumn_matrix_id_idx ON matrixcolumn USING btree (matrix_id);

