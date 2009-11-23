package CIPRES::TreeBase::DBILogin;

my $user=$ENV{'TREEBASE_DB_USER'};
my $pass=$ENV{'TREEBASE_DB_PASS'};
my $dsn =$ENV{'TREEBASE_DB_DSN'};

unless ($user && $pass && $dsn) {
    die "You must define \$user, \$pass, and \$dsn";
}

=head1 NAME

CIPRES::TreeBase::DBILogin

=head1 DESCRIPTION

Provides TreeBASE login credentials

=head1 PACKAGE VARIABLES

=over

=item $user

The database user name. This package copies the value from the $ENV{'TREEBASE_DB_USER'}
environment variable.

=item $pass

The database password. This package copies the value from the $ENV{'TREEBASE_DB_PASS'}
environment variable.

=item $dsn

A template for a L<DBI>-compliant dsn string. This package copies the value from 
the $ENV{'TREEBASE_DB_DSN'} environment variable. The template can contain placeholders
as used by sprintf (i.e. of the format C<%s>) within which the user name and password
can be interpolated (in that order).

=back

=head1 PACKAGE METHODS

=over

=item user()

Returns the value of the private $user package variable

=cut

sub user {
    return $user;
}

=item pass()

Returns the value of the private $pass package variable

=cut

sub pass {
    return $pass;
}

=item dsn()

Constructs a L<DBI>-compliant dsn string by using the private $dsn package
variable as a template within which it interpolates the $user and $pass private
variables using sprintf().

=cut

sub dsn {
  my ($self, $user, $pass) = @_;
  sprintf $dsn, $user, $pass;
}

=back

=head1 SEE ALSO

L<DBIUtils>

=cut

1;

