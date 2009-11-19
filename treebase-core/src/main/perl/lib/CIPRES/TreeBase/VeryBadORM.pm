
package CIPRES::TreeBase::VeryBadORM;
use Carp 'croak';
use strict 'vars';
use Devel::StackTrace;
use Data::Dumper;
our %dbh;
our $DBH;

sub set_db_connection { my $class = shift;  $DBH = $dbh{$class} = shift; }
sub get_db_connection { my $class = shift; return $dbh{$class}; }

sub prepare_cached {
    my ($self, $q) = @_;
    return $DBH->prepare_cached($q);
}

my %cache;
sub new {
    my ($class, $id) = @_;
    return $cache{$class}{$id} if exists $cache{$class}{$id};

    croak("Must connect to database with $class->set_db_connection() first")
	unless defined $DBH;

    unless (defined $id) {
		croak("$class\::new: missing ID argument");
    }
    my $obj = bless { 'id' => $id } => $class;
    $cache{$class}{$id} = $obj;
    return $obj;
}

# Maybe add some caching here at some point
sub AUTOLOAD {
    my $obj = shift;
    our $AUTOLOAD;
    my ($package, $method) = $AUTOLOAD =~ /(.*)::(.*)/;
    if ($package->has_attr($method)) {
		return $obj->get_no_check($method, @_);
    } elsif ($package->has_subobject($method)) {
		return $obj->get_subobject_no_check($method, @_);
    } elsif ($package->has_r_attr($method)) {
		return $obj->get_r_subobject_no_check($method, @_);
    } elsif ($package->has_r2_attr($method)) {
		return $obj->get_r2_subobject_no_check($method, @_);
    } else {
		my $trace = Devel::StackTrace->new;
		print $trace->as_string; # like carp
		croak("Unknown attribute '$method' in class '$package'");
    }
}

sub has_attr {
    my $base = shift;
    my $class = ref($base) || $base;
    return $class->attr_hash()->{shift()};
}

sub has_r_attr {
    my $base = shift;
    my $class = ref($base) || $base;
    return $class->r_class(shift());
}

sub has_r2_attr {
    my $base = shift;
    my $class = ref($base) || $base;
    return $class->r2_class(shift());
}

sub has_subobject {
    my $base = shift;
    my $subobj = shift;
    return $base->has_attr($base->foreign_key($subobj));
}

sub foreign_key {
    my $base = shift;
    my $subobj = lc(shift); # XXX
    return $subobj . "_id"; 
}

sub attr_hash {
    my $base = shift;
    my $class = ref($base) || $base;
    my $attr_hash = \%{"$class\::attr"};
    return $attr_hash if %$attr_hash;

    my $attr_list = $base->attr_list;
    if (@$attr_list) {
		%$attr_hash = map { $_ => 1 } @$attr_list;
		$attr_hash->{"$class\_id"} = 1;
		return $attr_hash;
    }
    return;
}

sub attr_list {
    my $base = shift;
    my $class = ref($base) || $base;
    my $attr_list = \@{"$class\::attr"};
    return $attr_list if @$attr_list;

    my $q = "select * from " . $base->table . " fetch first 1 rows only";
    my $sth = $DBH->prepare_cached($q);
    $sth->execute();
    while (my $row = $sth->fetchrow_hashref) {
	@$attr_list = keys %$row;
    }
    $sth->finish;
    return $attr_list;
}

sub r_attr_hash {
    my $base = shift;
    my $class = ref($base) || $base;
    return my $r_attr_hash = \%{"$class\::r_attr"};
}

sub r2_attr_hash {
    my $base = shift;
    my $class = ref($base) || $base;
    return my $r_attr_hash = \%{"$class\::r2_attr"};
}

sub reify {
    my $obj = shift;
    return $obj if $obj->reified;
    my $table =  $obj->table;
    my ($id_attr, $id_value) = $obj->get_id_pair;
    my $q = "select * from $table where $id_attr = ?";
    my $sth = $DBH->prepare_cached($q);
    $sth->execute($id_value);
    my $rows = 0;
    while (my $row = $sth->fetchrow_hashref()) {
	%$obj = %$row;
	$obj->{ID} = $obj->{$id_attr};
	$obj->set_reified();
	if (++$rows > 1) {
	    croak("Table '$table' has multiple entries for $id_attr = $id_value");
	}
    }
    return $obj;
}

sub reified { $_[0]{reified} }
sub set_reified { $_[0]{reified} = 1 }

sub get {
    my ($self, $attr) = @_;
    if ($self->has_attr($attr)) {
	return $self->get_no_check($attr);
    } elsif ($self->has_subobject($attr)) {
	return $self->get_subobject_no_check($attr, @_);
    } elsif ($self->has_r_attr($attr)) {
	return $self->get_r_subobject_no_check($attr, @_);
    }
    my $trace = Devel::StackTrace->new;
	print $trace->as_string; # like carp
    croak($self->class . " has no attribute named '$attr'");
}

sub get_no_check {
    my ($self, $attr) = @_;
    return $self->id if $attr eq "id";
    return $self->{$attr} if exists $self->{$attr};
    return $self->{$attr} = $self->reify->{$attr};
}

sub get_subobject_no_check {
    my ($self, $attr) = @_;
    return $self->{$attr} if exists $self->{$attr};
    my $id = $self->get($self->foreign_key($attr));
    return unless defined $id;
    return $self->{$attr} = $self->subobject_class($attr)->new($id);
}

# Example: Studies have analyses as a subobject
# $study->get_r_subobject_no_check("analyses")
# should query
#  select analysis_id from analysis where study_id = ?
# and return a list of analysis objects
sub get_r_subobject_no_check {
    my ($self, $attr) = @_;
    $attr = $attr;
    my $target_class = $self->r_class($attr);
    my $target_table = $target_class->table;
    my $field = $target_class->id_attr;
    my $id_field = $self->id_attr;
    my $q = "select $field from $target_table where $id_field = ?";
    my $sth = $DBH->prepare_cached($q);
    $sth->execute($self->id);
    my @results;
    while (my ($target_id) = $sth->fetchrow) {
	push @results, $target_class->new($target_id);
    }
    return @results;
}

# Example: Treeblocks have submissions as subobjects
#  and vice versa
# $treeblock->get_r2_subobject_no_check("submission")
# should query
#  select submission_id from sub_treeblock where treeblock_id = ?
# and return a list of submission objects
sub get_r2_subobject_no_check {
    my ($self, $attr) = @_;
    $attr = uc $attr;
    my $q = $self->r2_subobject_query($attr);
    my $target_class = $self->r2_class($attr);
    my $sth = $self->prepare_cached($q);
    $sth->execute($self->id);
    my @results;
    while (my ($target_id) = $sth->fetchrow) {
	push @results, $target_class->new($target_id);
    }
    return @results;
}
sub r2_subobject_query {
    my ($self, $attr) = @_;

    my $target_class = $self->r2_class($attr);
    my $target_table = $self->r2_table($attr);
    my $field = $self->r2_id_attr($attr);
    my $id_field = $self->id_attr;
    my $q = "select $field from $target_table where $id_field = ?";
    return $q;
}

sub r2_id_attr {
    my ($self, $attr) = @_;
    $self->r2_attr_hash()->{uc $attr}->[2] || $self->r2_class($attr)->id_attr;
}

sub to_str { my $self = shift; 
	     my %attr = @_;
	     return $self->class  . " #" . $self->id; }

sub id { $_[0]{'id'} }
sub id_attr { return lc($_[0]->class . "_id") };
sub class { return ref($_[0]) || $_[0]; }

my %known_class;

sub known_class_hash { return \%known_class; }
sub register {
    my $my_class = shift;
    my @classes = @_;
    @classes = scalar(caller()) unless @classes;
    for my $class (@classes) {
		push @{"$class\::ISA"}, $my_class;
		$class->known_class_hash->{uc $class} = $class;
    }
}

sub alias { 
    my ($base, $class) = @_;
    return $base->known_class_hash->{$class};
}

sub subobject_class { 
    my ($self, $subobj) = @_;
    my $subobj_class = \%{$self->class . "::subobject"};
    return $subobj_class->{$subobj} if exists $subobj_class->{$subobj};
    return $self->alias($subobj) || $subobj;#ucfirst(lc($subobj));
}

sub get_id_pair {
    my $self = shift;
    return ($self->id_attr, $self->id);
}

sub table { return $_[0]->class; }
sub r_class {
    my ($self, $r_attr) = @_;
    return $self->r_attr_hash()->{$r_attr};
}

sub r2_table {
    my ($self, $r_attr) = @_;
    return $self->r2_attr_hash()->{$r_attr}->[0];
}

sub r2_class {
    my ($self, $r_attr) = @_;
    return $self->r2_attr_hash()->{$r_attr}->[1];
}

sub dump {
    my $self = shift();
    my %attr = @_;
    my ($class, $id) = ($self->class, $self->id);

    my $continue = 1;
    $continue = $attr{'action'}->($self, %attr) if $attr{'action'};
    return unless $continue;

    $attr{'depth'} += 1;
    return if defined($attr{'maxdepth'}) && $attr{'depth'} > $attr{'maxdepth'};
    $attr{$class} = $id;
    $self->recurse(%attr);
    delete $attr{$class};
}

sub recurse { }
sub consistent { 1; }
sub is_nested { 0; }

sub DESTROY { }

1;
