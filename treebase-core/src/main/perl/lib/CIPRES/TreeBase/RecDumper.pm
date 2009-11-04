package CIPRES::TreeBase::RecDumper;
use Carp 'croak';
use File::Temp qw(tempfile);
use strict;

# XXX LOB fields should be removed from fieldlist and handled separately
sub new {
    my $class = shift;
    my %arg = @_;
    my $fn = $arg{'FIELDS'}   or croak("$class->new: FIELDS required");
    my $ct = $arg{'TYPES'}    or croak("$class->new: TYPES required");
    my $tn = uc $arg{'TABLE'} or croak("$class->new: TABLE required");
    my $X  = my @fieldnames = map uc, @$fn; 
    my $dir = $arg{'DIR'};
    mkdir $dir if not -d $dir;   
    my $self = { 
    	'F' => \@fieldnames, 
    	'X' => $X, 
    	'N' => $tn,
    	'T' => $ct,
    	'D' => $dir,
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
    my $fieldlist = join ", ", map qq{"$_"}, @{$self->{F}}; 
    $self->{'PREFIX'} = qq{INSERT INTO "$self->{N}" ($fieldlist) VALUES (};
    $self->{'SUFFIX'} = qq{);\n}; # XXX added closing semicolon
    return;
}

# Print some text literally
sub print {
    my $self = shift;
    return print {$self->{'OUT'}} @_;
}

# Format data into an insert statement and return (or write) the result
sub rec {
	my $self = shift;
	@_ > @{$self->{F}} 
		and croak("rec: too many items (expected $self->{X})");
	@_ < @{$self->{F}}
		and croak("rec: too few items (expected $self->{X})");
	my @values;
	if ( $self->{'N'} ne 'STUDY_NEXUSFILE' ) {
		@values = $self->quote_data(@_);
	}
	else {		
		my ( %record, $dir, $path );
		eval {
		my @fields = @{$self->{F}};
		%record = map { $fields[$_] => $_[$_] } ( 0 .. $#fields );
#		$dir = $self->{'D'} . '/' . $record{STUDY_ID};
#		mkdir $dir if not -d $dir;
#		$path = $dir . '/' . $record{FILENAME};
		my ( $fh, $filename ) = tempfile( DIR => $self->{'D'} );
		@values = ( $self->quote_data($record{STUDY_ID}), "lo_import('$filename')", $self->quote_data($record{FILENAME}) );
#		open my $nexfh, '>', $path or croak $!;		
		print $fh substr( $record{NEXUS}, 1, length($record{NEXUS}) - 2 );
		close $fh;
		system('gzip','-9',$filename);
		};
		warn 'dir: ', $dir, ' path: ', $path, ' file: ', $record{FILENAME}, ' id: ', $record{STUDY_ID}, ' msg: ', $@ if $@;
	}
	my $values = join ", ", @values;
	my $insert = $self->{'PREFIX'} . $values . $self->{'SUFFIX'};
	return print {$self->{'OUT'}} $insert if $self->{'OUT'};
	return $insert;
}

# Format metadata into a create statement and return (or write) the result
sub dump_create {
	my $self = shift;
	my $create = qq{CREATE TABLE "$self->{'N'}";\n};
	return print {$self->{'OUT'}} $create if $self->{'OUT'};
	return $create;		
}

sub quote_data {
    my $self = shift;
    my @d = @_;
	for my $i (0 .. $#{$self->{F}}) {
		my $t = $self->{T}[$i];
		local *_ = \$d[$i];
		$_ = "NULL", next unless defined;
	
		if ($t eq "CHAR" || $t eq "VARCHAR" || $t eq 'CLOB') {
		    s/'/''/g;
		    $_ = "'$_'";
		} 
		elsif ($t =~ /^(BIG|SMALL|)INT$/ || $t eq 'INTEGER' || $t eq 'DOUBLE') {
		    # do nothing
		} 
		elsif ($t eq 'TIMESTMP'){
		    if ( m|(\d{1,2})/(\d{1,2})/(\d{4})\s+(\d{2}):(\d{2}):(\d{2})| ) {
		        my ( $day, $month, $year, $hour, $minute, $second ) =
		           ( $1,   $2,     $3,    $4,    $5,      $6      );
		        $_ = "'${year}-${month}-${day} ${hour}:${minute}:${second}'";
		    }
		}
		elsif ($t eq 'DATE'){
            my ( $day, $month, $year );
		    if ( m|(\d{1,2})/(\d{1,2})/(\d{4})| ) {
		        ( $day, $month, $year ) = ( $1, $2, $3 );
		    }
            if ( m|(\d{4})-(\d{1,2})-(\d{1,2})| ) {
                ( $year, $month, $day ) = ( $1, $2, $3 );
            }
            $_ = "'${year}-${month}-${day}'";
		}
		else {
		    croak("Unknown field type '$t'; aborting");
		}
	}

    return @d;
}

1;
