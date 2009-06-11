package CIPRES::TreeBase::RecDumper;
use Carp 'croak';

# LOB fields should be removed from fieldlist and handled separately
sub new {
    my $class = shift;
    my %arg = @_;
    my $fn = $arg{'FIELDS'}   or croak("$class->new: FIELDS required");
    my $ct = $arg{'TYPES'}    or croak("$class->new: TYPES required");
    my $tn = uc $arg{'TABLE'} or croak("$class->new: TABLE required");
    my $X  = my @fieldnames = map uc, @$fn;    
    my $self = { 
    	'F' => \@fieldnames, 
    	'X' => $X, 
    	'N' => '"'.$tn.'"', 
    	'T' => $ct 
    };
    bless $self => $class;
    $self->_initialize();
    return $self;
}

sub set_output {
    my ($self, $fh) = @_;
    $self->{'OUT'} = $fh;
}

sub _initialize {
    my $self = shift;
    my $fieldlist = join ", ", map { "\"$_\"" } @{$self->{F}}; 
    # Need to escape certain field names here
    $self->{'PREFIX'} = qq{INSERT INTO $self->{N} ($fieldlist) VALUES (};
    $self->{'SUFFIX'} = qq{);\n}; # XXX added closing semicolon
    return;
}

# Format data into an insert statement and return (or write) the result
sub rec {
	my $self = shift;
	@_ > @{$self->{F}} 
		and croak("rec: too many items (expected $self->{X})");
	@_ < @{$self->{F}}
		and croak("rec: too few items (expected $self->{X})");
	
	@_ = $self->quote_data(@_);
	
	my $values = join ", ", @_;
	my $insert = $self->{'PREFIX'} . $values . $self->{'SUFFIX'};
	return print {$self->{'OUT'}} $insert if $self->{'OUT'};
	return $insert;
}

# Format metadata into a create statement and return (or write) the result
sub dump_create {
	my $create = 'CREATE TABLE ' . $self->{'N'} . ";\n";
	return print {$self->{'OUT'}} $create if $self->{'OUT'};
	return $create;		
}

# XXX UNFINISHED !!!!
sub quote_data {
    my $self = shift;
    my @d = @_;
	for my $i (0 .. $#{$self->{F}}) {
		my $t = $self->{T}[$i];
		local *_ = \$d[$i];
		$_ = "NULL", next unless defined;
	
		if ($t eq "CHAR" || $t eq "VARCHAR") {
		    s/'/''/g;
		    $_ = "'$_'";
		} 
		elsif ($t =~ /^(BIG|SMALL|)INT$/ || $t eq 'INTEGER' || $t eq 'DOUBLE') {
		    # do nothing
		} 
		else {
		    croak("Unknown field type '$t'; aborting");
		}
	}

    return @d;
}

1;
