use Test::More tests => 7;

use_ok('CIPRES::TreeBase::TestObjects');
use DBI;
my $dbh = DBI->connect("DBI:CSV:f_dir=test_db;csv_eol=\n");
CIPRES::TreeBase::VeryBadORM->set_db_connection($dbh);

ok(my $s1 = Study->new(1));
is($s1->name, 'study one');

ok(my $person = Person->new(8));
is($person->first, 'Otto');

is(Tree->new(120)->name, "tree cxx");
is(Matrix->new(25)->n_rows, 26);