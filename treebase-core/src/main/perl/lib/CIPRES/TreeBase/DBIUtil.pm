package CIPRES::TreeBase::DBIUtil;
use base 'Exporter';
use DBI;
@EXPORT = qw(print_aoh quick_select);
@EXPORT_OK = qw(get_colnames get_coltypes);

=head1 CIPRES::TreeBase::DBIUtil

Utility functions for database operations.

=head1 SPECIAL VARIABLES

=over

=item @EXPORT

Exports print_aoh and quick_select

=item @EXPORT_OK

Exports get_colnames and get_coltypes

=back

=head1 FUNCTIONS

=over

=item dbh()

Creates and returns a database handle. Optional named arguments:
 * user => database user name
 * pass => database password
 * dsn  => DBI-compliant dsn template string (with optional 
           sprintf placeholders for user name and password)
 * login_info_class => package name of a class that conforms 
           to the interface of CIPRES::TreeBase::DBILogin

=cut

sub dbh {
    my ($class) = shift;
    my %opts = @_;

    my ($login_info_class) = $opts{'login_info_class'} || 'CIPRES::TreeBase::DBILogin';
    my $login_info_file = $login_info_class;
    $login_info_file =~ s{::}{/}g;
    require "$login_info_file.pm";
    my $user = $opts{'user'} || $login_info_class->user;
    my $pass = $opts{'pass'} || $login_info_class->pass;
    my $dsn  = $opts{'dsn'} ? sprintf $opts{'dsn'}, $user, $pass : $login_info_class->dsn($user, $pass);
    my $h = DBI->connect($dsn);
    $h->{'private_cipres_treebase_dbiutil_username'} = $user if $h;
    return $h;
}

=item get_user()

Returns the TreeBASE database user name.

=cut

sub get_user {
    return $_[0]{'private_cipres_treebase_dbiutil_username'};
}

=item max()

Utility function, returns the highest numerical value in the argument list.

=cut

sub max {
    my $max = shift;
    $max = $max > $_ ? $max : $_ for @_;
    $max;
}

=item print_aoa()

Utility function, prints an array of arrays provided as an array reference where the
first row is a header row. Second argument is what is used to pad undefined values.
Calls print_rows() internally.

=cut

sub print_aoa {
    my @aoa = @{shift()};
    return if @aoa == 0;
    my $null = shift;
    my @headers = @{shift @aoa};
    print_rows(\@headers, \@aoa, $null);
}

=item print_aoh()

Utility function, prints an array of hashes provided as an array reference
containing hash references where the keys of the first hash are used as the
header row. Second argument is what is used to pad undefined values.
Calls print_rows() internally.

=cut

sub print_aoh {
    my $aoh = shift;
    return if @$aoh == 0;
    my $null = shift;
    my @headers = sort keys %{$aoh->[0]};
    my @rows = map [@{$_}{@headers}], @$aoh;
    print_rows(\@headers, \@rows, $null);
}

=item print_rows()

Internal function, called by print_aoa() and print_aoh().

=cut

sub print_rows {
    my ($headers, $rows, $null) = @_;
    my @width = map length, @$headers;
    for my $row (@$rows) {
		@$row = map defined() ? $_ : $null, @$row;
		s/ +$// for @$row;
		s/^ +// for @$row;
		s/([^[:print:]])/"\\x" . sprintf("%02x", ord($1))/ge for @$row;
		$width[$_] = max($width[$_], length $row->[$_]) for 0 .. $#$row;
    }
    printcols($headers, \@width, " | ");
    printhyphens(\@width, 3);
    printcols($_, \@width, " | ") for @$rows;
}

=item printcols()

Internal function, called by print_rows()

=cut

sub printcols {
    my ($vals, $w, $fill) = @_;
    for my $i (0 .. $#$vals) {
		my $v = $vals->[$i];
		print $v, " " x ($w->[$i] - length $v);
		print $fill unless $i == $#$vals;
    }
    print "\n";
}

=item printhyphens()

Internal function, called by print_rows()

=cut

sub printhyphens {
    my ($w, $x) = @_;
    my $t = 0;
    $t += $_ for @$w;
    $t += $x * (@$w - 1);
    print "-" x $t, "\n";
}

=item quick_select()

Utility function, runs the provided query statement (second argument)
on the provided database handle (first argument). Returns undef if 
multiple records are returned by the query (because that is considered
an error). Otherwise returns an array or the first value of the array
depending on context (array or scalar, respectively).

=cut

sub quick_select {
    my $dbh = shift;
    my $q = shift;
    my $sth = $dbh->prepare_cached($q);
    $sth->execute(@_) or return;
    my @rec = $sth->fetchrow_array;
    $sth->fetchrow_array && return;   # multiple records == error
    return wantarray ? @rec : $rec[0];
}

=item get_colnames()

Given a database handle and a table name, returns the names
of the columns in that table. Returns either a list or an
array reference, depending on the context (array or scalar,
respectively).

=cut

# Maybe use new $dbh->table_info method instead.
sub get_colnames {
    my $dbh = shift();
    my $table = uc(shift());
    my $q = qq{SELECT name from sysibm.syscolumns 
               where tbcreator = ? and tbname = ?
               order by colno};
    my $names = $dbh->selectcol_arrayref($q, {RaiseError => 1}, 
					 # Bind values: 
					 uc(get_user($dbh)), $table);
    return wantarray() ? @$names : $names;
}

=item get_coltypes()

Given a database handle and a table name, returns the
datatype names of the columns in the table. Returns
either a list or an array reference, depending on the
context (array or scalar, respectively).

=cut

sub get_coltypes {
    my $dbh = shift();
    my $table = uc(shift());
    my $q = qq{SELECT coltype from sysibm.syscolumns 
               where tbcreator = ? and tbname = ?
               order by colno};
    my $names = $dbh->selectcol_arrayref($q, {RaiseError => 1}, 
					 # Bind values: 
					 uc(get_user($dbh)), $table);
    s/\s+$// for @$names;
    return wantarray() ? @$names : $names;
}

=back

=head1 SEE ALSO

L<CIPRES::TreeBase::DBILogin>, L<DBI>

=cut

1;
