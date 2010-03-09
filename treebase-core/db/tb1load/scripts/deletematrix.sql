-- Use this script to delete a matrix with id NNNN.
-- (Useful for matrices in which Step1a broke.)
-- Replace NNNN by your matrix_id

begin transaction;

delete from codonnoncoding_colrange r
  using codonpositionset p, matrix m
  where r.codonpositionset_id = p.codonpositionset_id
    and p.matrix_id = m.matrix_id
    and m.matrix_id = NNNN
returning *;

update matrix
  set codonpositionset_id = null
where matrix_id = NNNN
returning *;

delete from codonpositionset where matrix_id = NNNN
returning *;

delete from sub_matrix where matrix_id = NNNN
returning *;

delete from matrixelement e
using  matrixrow r, matrixcolumn c
where e.matrixrow_id = r.matrixrow_id and r.matrix_id = NNNN
  and e.matrixcolumn_id = c.matrixcolumn_id and c.matrix_id = NNNN
returning *;


delete from matrixcolumn where matrix_id = NNNN
returning *;

delete from matrixrow where matrix_id = NNNN
returning *;


delete from matrix where matrix_id = NNNN
returning *;


commit transaction; 

