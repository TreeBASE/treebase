BEGIN transaction; 

update phylotree 
   set treekind_id = 1 
where treekind_id is null; 

update phylotree 
   set treequality_id = 1 
where treequality_id is null; 

update matrix
  set description = 'Legacy TreeBASE Matrix ID = ' || tb_matrixid
where description is null 
  and tb_matrixid is not null; 

COMMIT; 