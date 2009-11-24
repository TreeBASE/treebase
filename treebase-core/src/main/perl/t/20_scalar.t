use Test::More tests => 7;

use_ok('CIPRES::TreeBase::TestObjects');
use DBI;
my $dbh = DBI->connect("DBI:CSV:f_dir=test_db;csv_eol=\n");
CIPRES::TreeBase::VeryBadORM->set_db_connection($dbh);

ok(my $s1 = Study->new(1));
is($s1->name, 'study one');

ok(my $s8 = Study->new(8));
is($s8->owner, 'Otto');

is(Tree->new(120)->name, "tree cxx");
is(Matrix->new(25)->n_rows, 26);