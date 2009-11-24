
use Test::More tests => 50;

use_ok('DBI');
use_ok('DBD::CSV');

ok(my $dbh = DBI->connect("DBI:CSV:f_dir=test_db;csv_eol=\n"));

check_table('study', qw(name study_id owner tree_id));
check_table('matrices', qw(matrix_id name n_rows study_id));
check_table('TREE', qw(TreeId name root_node_id));
check_table('node', qw(treenode_id tree_id left_child right_child data));
check_table('person', qw(person_id last first));
check_table('study_author', qw(study_id person_id role));

use_ok('CIPRES::TreeBase::TestObjects');

# one test per column, plus four
sub check_table {
  my $table = shift;
  my %expected_columns = map {$_ => 1} @_;
  ok(my $sth = $dbh->prepare("select * from $table"));
  ok($sth->execute());
  ok(my $row = $sth->fetchrow_hashref);
  for my $col (keys %$row) {
    ok($expected_columns{$col}, "found expected column '$col' in table '$table'");
    delete $expected_columns{$col};
  }
  is(keys(%expected_columns), 0, "all columns found in table '$table'");
}

