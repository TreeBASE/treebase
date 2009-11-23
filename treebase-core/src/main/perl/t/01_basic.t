
use Test::More tests => 10;

warn(`pwd`);
use_ok('DBI');
use_ok('DBD::CSV');

ok(my $dbh = DBI->connect("DBI:CSV:f_dir=test_db;csv_eol=\n"));
ok(my $sth = $dbh->prepare("select id from study where interesting > 0"));
ok($sth->execute());

%x = (4 => 1, 6 => 1, 8 => 1, 9 => 1);
while (my ($id) = $sth->fetchrow) {
  ok($x{$id}, "found item $id");
  delete $x{$id};
}
is(keys(%x), 0, "all items found");

