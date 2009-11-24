use Test::More tests => 9;

#
# Tests for reverse attributes (%r_attr)
#

use_ok('CIPRES::TreeBase::TestObjects');
use DBI;
my $dbh = DBI->connect("DBI:CSV:f_dir=test_db;csv_eol=\n");
CIPRES::TreeBase::VeryBadORM->set_db_connection($dbh);

my %x_names = map {$_ => 1} ('R' .. 'W');

{
  my @nodes_5040 = Tree->new(5040)->nodes;
  is (scalar(@nodes_5040), 6);
  { my $RESULT = "";
    for my $node (@nodes_5040) {
      if ($node->tree_id != 5040) {
	my $nid = $node->id;  my $tid = $node->tree_id;
	$RESULT = "Node $nid has treeid = $tid; s/b 5040";
	last;
      }
      delete $x_names{$node->data};
    }
    if (%x_names) {
      my $missing = each %x_names;
      $RESULT = "Where is node '$missing'?";
    }
    ok($RESULT eq "", $RESULT || "Tested nodes of tree 5040");
  }
}


my %x_num_matrices = (1 => 1, 2 => 1, 4 => 2, 5 => 1, 7 => 1, 8 => 1);
for my $sid (1, 2, 4, 5, 7, 8) {
  my $num_matrices = my @matrices = Study->new($sid)->matrices;
  my $matrices = $x_num_matrices{$sid} == 1 ? "matrix" : "matrices";
  is ($num_matrices, $x_num_matrices{$sid}, "count study $sid matrices");
}

