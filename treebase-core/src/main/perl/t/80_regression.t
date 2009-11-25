
use Test::More tests => 1;

BEGIN {
  require CIPRES::TreeBase::VeryBadORM;

  package TestObject;
  CIPRES::TreeBase::VeryBadORM->register;
  sub table { "study" }
  sub foreign_key { return undef; }
}

# Regression test for bug in has_subobject:  if ->foreign_key indicates that the attribute is
# unknown, don't try to call has_attr on the failed result value
# 20091125 MJD
use DBI;
my $dbh = DBI->connect("DBI:CSV:f_dir=test_db;csv_eol=\n");
CIPRES::TreeBase::VeryBadORM->set_db_connection($dbh);

ok(! TestObject->has_subobject("poo"), "foreign_key method failure");

