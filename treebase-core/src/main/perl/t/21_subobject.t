use Test::More tests => 3;

use_ok('CIPRES::TreeBase::TestObjects');
use DBI;
my $dbh = DBI->connect("DBI:CSV:f_dir=test_db;csv_eol=\n");
CIPRES::TreeBase::VeryBadORM->set_db_connection($dbh);

is(Study->new(4)->Tree->name, "tree xxiv");  # use correct subobject name
is(Study->new(4)->tree->name, "tree xxiv");  # use alternate capitalization


