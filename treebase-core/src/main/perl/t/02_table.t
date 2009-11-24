
use Test::More tests => 7;

use_ok('CIPRES::TreeBase::TestObjects');

is(Matrix->table, "matrices");
is(Study->table, "study");
is(Tree->table, "TREE");

is(Matrix->id_attr, "matrix_id");
is(Study->id_attr, "study_id");
is(Tree->id_attr, "TreeId");



