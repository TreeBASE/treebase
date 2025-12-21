insert into versionhistory(patchnumber, patchlabel, patchdescription) 
       values (6, 'add-algorithm-types', 
       'Add standard algorithm types reference data'); 

delete from algorithm;
insert into algorithm (type, algorithm_id, version, description) values
('L', 1, 1, 'maximum likelihood'),
('B', 2, 1, 'bayesian inference'),
('P', 3, 1, 'parsimony'),
('E', 4, 1, 'minimum evolution'),
('J', 5, 1, 'neighbor joining'),
('U', 6, 1, 'UPGMA');

