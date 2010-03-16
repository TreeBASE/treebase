-- This assumes that the original Bill's file was modified: 
--   "Simple Tree" --> "supertree"
--   T391c2x7x96c15c45c54 is annotated "simple"

update phylotree p 
   set treetype_id = t.treetype_id
from phtrtype y, treetype t 
where p.tb1_treeid = y.tb1_treeid
  and y.treetypename = lower(t.description)

