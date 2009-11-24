use Test::More tests => 3;

use lib '../lib';
use_ok('CIPRES::TreeBase::TestObjects');
use DBI;
my $dbh = DBI->connect("DBI:CSV:f_dir=test_db;csv_eol=\n");
CIPRES::TreeBase::VeryBadORM->set_db_connection($dbh);

my %x_names = map {$_ => 1} ('R' .. 'W');

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

