
package CIPRES::TreeBase::DBILogin;

my $user=$ENV{TREEBASE_DB_USER};
my $pass=$ENV{TREEBASE_DB_PASS};
my $dsn =$ENV{TREEBASE_DB_DSN};

unless ($user && $pass && $dsn) {
    die "You must define \$user, \$pass, and \$dsn";
}

sub user {
    return $user;
}

sub pass {
    return $pass;
}

sub dsn {
  my ($self, $user, $pass) = @_;
  sprintf $dsn, $user, $pass;
}

1;

