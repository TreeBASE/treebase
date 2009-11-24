use Test::More tests => 11;

use_ok('CIPRES::TreeBase::TestObjects');
use DBI;
my $dbh = DBI->connect("DBI:CSV:f_dir=test_db;csv_eol=\n");
CIPRES::TreeBase::VeryBadORM->set_db_connection($dbh);

is(Study->new(4)->Tree->name, "tree xxiv");  # use correct subobject name
is(Study->new(4)->tree->name, "tree xxiv");  # use alternate capitalization

# root_node's class is defined by %Tree::subobject, not by the default behavior
is(Tree->new(24)->root_node->id, Tree->new(24)->root_node_id);  

# trace attributes through subobject and back again: make a tree
# object, get is root_node object, then come back to the tree via
# root_node:tree_id, and see if we ended in the same place
for my $tid (1,2,24,119,120,5040,40320) {
    is(Tree->new($tid)->root_node->tree->id, $tid);
}

