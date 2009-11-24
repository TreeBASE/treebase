use Test::More tests => 2;

#
# Tests for link attributes (%r2_attr)
#

use_ok('CIPRES::TreeBase::TestObjects');
use DBI;
my $dbh = DBI->connect("DBI:CSV:f_dir=test_db;csv_eol=\n");
CIPRES::TreeBase::VeryBadORM->set_db_connection($dbh);

my $FAIL = "";
my @s5_people = Study->new(5)->people;
my %x_people = map {$_ => 1} qw(Quincunx Sax Sargent);
for my $p (@s5_people) {
  my $ln = $p->last;
  if ($x_people{$ln}) {
    delete $x_people{$ln};
  } else {
    $FAIL = "unexpected person '$ln' associated with study 5";
  }
}
if (%x_people) {
  $FAIL = "missing persons " . join(", ", keys %x_people) . " not associated with study 5";
}
ok($FAIL eq "", $FAIL || "check study 5 people");

