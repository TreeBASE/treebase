
-- begin transaction; 

create table phtrtype (
  tb1_treeid character varying (30), 
  treetypename character varying (255)
); 

\copy phtrtype (tb1_treeid, treetypename) from '../data/tree_types.txt'

