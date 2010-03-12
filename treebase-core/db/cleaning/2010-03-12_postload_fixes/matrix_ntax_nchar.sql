BEGIN transaction; 

update matrix as m 
   set nTax  = r.rows_counted,
       nChar = c.columns_counted
from (select matrix_id, count(*) as rows_counted 
      from matrixrow group by matrix_id) as r, 
     (select matrix_id, count(*) as columns_counted 
      from matrixcolumn group by matrix_id) as c  
where m.matrix_id = r.matrix_id and m.matrix_id = c.matrix_id 
  and ((m.ntax is null or m.ntax <> r.rows_counted) or
       (m.nchar is null or m.nchar <> c.columns_counted)) 
returning m.matrix_id, m.tb_matrixid, m.nexusfilename, m.published, 
          m.nTax  as new_ntax,
          m.nChar as new_nchar;  

COMMIT; 