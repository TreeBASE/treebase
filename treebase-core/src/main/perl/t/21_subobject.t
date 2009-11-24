use Test::More tests => 4;

use_ok('CIPRES::TreeBase::TestObjects');
use DBI;
my $dbh = DBI->connect("DBI:CSV:f_dir=test_db;csv_eol=\n");
CIPRES::TreeBase::VeryBadORM->set_db_connection($dbh);

is(Study->new(4)->Tree->name, "tree xxiv");  # use correct subobject name
is(Study->new(4)->tree->name, "tree xxiv");  # use alternate capitalization

# root_node's class is defined by %Tree::subobject, not by the default behavior
is(Tree->new(24)->root_node->id, Tree->new(24)->root_node_id);  
