
package CIPRES::TreeBase::DBIUtil;
use base 'Exporter';
use DBI;
@EXPORT = qw(print_aoh quick_select);

sub dbh {
    my ($class) = shift;
    my %opts = @_;

    my ($login_info_class) = $opts{login_info_class} || 'CIPRES::TreeBase::DBILogin';
    my $login_info_file = $login_info_class;
    $login_info_file =~ s{::}{/}g;
    require "$login_info_file.pm";
    my $user = $opts{user} || $login_info_class->user;
    my $pass = $opts{pass} || $login_info_class->pass;
    my $dsn  = $opts{dsn} ? sprintf $opts{dsn}, $user, $pass : $login_info_class->dsn($user, $pass);

    return DBI->connect($dsn);
}

sub max {
    my $max = shift;
    $max = $max > $_ ? $max : $_ for @_;
    $max;
}

sub print_aoa {
    my @aoa = @{shift()};
    return if @aoa == 0;
    my $null = shift;
    my @headers = @{shift @aoa};
    print_rows(\@headers, \@aoa, $null);
}

sub print_aoh {
    my $aoh = shift;
    return if @$aoh == 0;
    my $null = shift;
    my @headers = sort keys %{$aoh->[0]};
    my @rows = map [@{$_}{@headers}], @$aoh;
    print_rows(\@headers, \@rows, $null);
}

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

sub printcols {
    my ($vals, $w, $fill) = @_;
    for my $i (0 .. $#$vals) {
	my $v = $vals->[$i];
	print $v, " " x ($w->[$i] - length $v);
	print $fill unless $i == $#$vals;
    }
    print "\n";
}

sub printhyphens {
    my ($w, $x) = @_;
    my $t = 0;
    $t += $_ for @$w;
    $t += $x * (@$w - 1);
    print "-" x $t, "\n";
}

sub quick_select {
    my $dbh = shift;
    my $q = shift;
    my $sth = $dbh->prepare_cached($q);
    $sth->execute(@_) or return;
    my @rec = $sth->fetchrow_array;
    $sth->fetchrow_array && return;   # multiple records == error
    return wantarray ? @rec : $rec[0];
}

1;
