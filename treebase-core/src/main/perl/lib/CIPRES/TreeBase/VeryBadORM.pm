package CIPRES::TreeBase::VeryBadORM;

$CIPRES::TreeBase::VeryBadORM::VERSION=0.1;

use Carp 'croak';
use strict 'vars';
#use Devel::StackTrace;
#use Data::Dumper;
our %dbh;
our $DBH;

=head1 NAME

CIPRES::TreeBase::VeryBadORM

=head1 DESCRIPTION

Superclass for TreeBASE objects. This class is subclassed by packages in C<TreeBaseObjects>.

This module maps relations in a relational database to objects in Perl.  It avoids all difficult
implementation problems by providing only read-only access.

=head1 PACKAGE VARIABLES

=over

=item %dbh

This hash holds cached database handles keyed on class names.  

=item $DBH

Holds a singleton database handle

=back

=head1 PACKAGE METHODS

=over

=item set_db_connection()

Sets the database handle for the invoking child class. Called as a package method.

=cut

sub set_db_connection { my $class = shift;  $DBH = $dbh{$class} = shift; }

=item get_db_connection()

Gets the database handle for the invoking child class. Called as a package method.

=cut

sub get_db_connection { my $class = shift; return $dbh{$class}; }

=back

=head1 INSTANCE METHODS

=over

=item prepare_cached()

Prepares a query on the singleton database handle, returns statement handler.

=cut

sub prepare_cached {
    my ($self, $q) = @_;
    return $DBH->prepare_cached($q);
}

=item new()

Instantiates an instance of one of the classes defined in TreeBaseObjects. This constructor
requires that the singleton database handle $CIPRES::TreeBase::VeryBadORM::DBH has been defined
and that a valid ID is supplied as argument. Instantiated objects are cached in the private
%cache hash as $cache{$class}{$id}. Returned objects are simply blessed hash references that 
contain the ID as { 'id' => $id }

=cut

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

=item AUTOLOAD

Provides the magical methods available in the child classes. It does this by checking which of
has_attr(), has_subobject(), has_r_attr() or has_r2_attr() applies and invokes one of
get_no_check(), get_subobject_no_check(), get_r_subobject_no_check() or get_r2_subobject_no_check()
respectively. Croaks otherwise.

=cut

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

=item has_attr()

Checks to see if the invocant's class defines the supplied attribute. It does this by calling
attr_hash() and doing a lookup for the supplied attribute in the returned hash.

=cut

sub has_attr {
    my $base = shift;
    my $class = ref($base) || $base;
    return $class->attr_hash()->{shift()};
}

=item has_r_attr()

Checks to see if the invocant's class defines the supplied "reverse attribute" (see 
L<TreeBaseObjects> for the description of the %r_attr hash). It does this by returning whatever
is returned by r_class() whilst passing it the supplied "reverse attribute"'s name.

=cut

sub has_r_attr {
    my $base = shift;
    my $class = ref($base) || $base;
    return $class->r_class(shift());
}

=item has_r2_attr()

Checks to see if the invocant's class defines the supplied "reverse attribute through 
intersection table" (see L<TreeBaseObjects> for the description of the %r2_attr hash). It does this 
by returning whatever is returned by r2_class() whilst passing it the supplied "reverse 
attribute through intersection table"'s name.

=cut

sub has_r2_attr {
    my $base = shift;
    my $class = ref($base) || $base;
    return $class->r2_class(shift());
}

=item has_subobject()

Checks to see if the invocant is associated with the supplied subobject. It does this by 
first turning the subobject's name into a foreign key column (by calling foreign_key()) and then 
checking whether that column is available as an attribute (by calling has_attr()).

=cut

sub has_subobject {
    my $base = shift;
    my $subobj = shift;
    return $base->has_attr($base->foreign_key($subobj));
}

=item foreign_key()

Turns the supplied argument into a foreign key column. It does this by lower casing the 
argument string and appending '_id'.

=cut

sub foreign_key {
    my $base = shift;
    my $subobj = lc(shift); # XXX
    return $subobj . "_id"; 
}

=item attr_hash()

Returns a hash reference of all available attributes for the invocant. It does this by first 
checking to see if there is an %attr hash defined in the invocant's class (and returns 
a reference to that if it's there). Otherwise it calls attr_list, uses its contents as keys
(values are 1) and adds the class name . '_id', i.e. a lookup of the primary key. On subsequent
calls the output is cached due to the autovivification of the package hash.

=cut

sub attr_hash {
    my $base = shift;
    my $class = ref($base) || $base;
    my $attr_hash = \%{"$class\::attr"};
    return $attr_hash if %$attr_hash;

    my $attr_list = $base->attr_list;
    if (@$attr_list) {
		%$attr_hash = map { $_ => 1 } @$attr_list;
		$attr_hash->{$class->id_attr} = 1;
		return $attr_hash;
    }
    return;
}

=item attr_list()

Returns an array reference of available attributes. It does this by checking if there is an
array ref $attr available in the invocant's class (and returns that). Otherwise it checks
the invocant's mapped database table and collects the returned column names and returns those.
On subsequent calls the output is cached due to the autovivification of the package array.

=cut

sub attr_list {
    my $base = shift;
    my $class = ref($base) || $base;
    my $attr_list = \@{"$class\::attr"};
    return $attr_list if @$attr_list;

# For DB2:
#    my $q = "select * from " . $base->table . " fetch first 1 rows only";
# For Postgres:
    my $q = "select * from " . $base->table . " limit 1";
    my $sth = $DBH->prepare_cached($q);
    $sth->execute();
    while (my $row = $sth->fetchrow_hashref) {
		@$attr_list = keys %$row;
    }
    $sth->finish;
    return $attr_list;
}

=item r_attr_hash()

Returns the %r_attr hash defined in the invocant's class (see TreeBaseObjects for a description
of what that hash is for).

=cut

sub r_attr_hash {
    my $base = shift;
    my $class = ref($base) || $base;
    return my $r_attr_hash = \%{"$class\::r_attr"};
}

=item r2_attr_hash()

Returns the %r2_attr hash defined in the invocant's class (see TreeBaseObjects for a description
of what that hash is for).

=cut

sub r2_attr_hash {
    my $base = shift;
    my $class = ref($base) || $base;
    return my $r_attr_hash = \%{"$class\::r2_attr"};
}

=item reify()

Populates the invocant object's attributes from the database.

=cut

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
		$obj->{'id'} = $obj->{$id_attr};
		$obj->set_reified();
		if (++$rows > 1) {
			croak("Table '$table' has multiple entries for $id_attr = $id_value");
		}
    }
    return $obj;
}

=item reified()

Returns whether the invocant has been reified (see reify()).

=cut

sub reified { $_[0]{'reified'} }

=item set_reified()

Flags that the invocant object has been reified.

=cut

sub set_reified { $_[0]{'reified'} = 1 }

=item get()

Given an invocant and a supplied attribute name, returns the attribute value. What the attribute
actually is, is decided by first checking has_attr(), has_subobject(), has_r_attr() and returns
the output of either get_no_check(), get_subobject_no_check() or get_r_subobject_no_check() 
respectively. B<This method is probably never used and therefore probably buggy.>

=cut

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

=item get_no_check()

Returns the value of the supplied attribute name as applies to the invocant object. This will
most likely just return scalar, non-reference values such as titles and labels.

=cut

sub get_no_check {
    my ($self, $attr) = @_;
    return $self->id if $attr eq 'id';
    return $self->{$attr} if exists $self->{$attr};
    return $self->{$attr} = $self->reify->{$attr};
}

=item get_subobject_no_check()

Treats the supplied attribute name as either a true attribute or name from which a subobject
(in one-to-one relation) is instantiated. See description of %subobject hash in TreeBaseObjects.

=cut

sub get_subobject_no_check {
    my ($self, $attr) = @_;
    return $self->{$attr} if exists $self->{$attr};
    my $id = $self->get($self->foreign_key($attr));
    return unless defined $id;
    return $self->{$attr} = $self->subobject_class($attr)->new($id);
}

=item get_r_subobject_no_check()

Treats the supplied attribute name as either a true attribute or name from which a subobject
(in many-to-one relation) is instantiated. See description of %r_attr hash in TreeBaseObjects.

=cut

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

=item get_r2_subobject_no_check()

Treats the supplied attribute name as either a true attribute or name from which a subobject
(in many-to-one relation) is instantiated. See description of %r2_attr hash in TreeBaseObjects.

=cut

# Example: Treeblocks have submissions as subobjects
#  and vice versa
# $treeblock->get_r2_subobject_no_check("submission")
# should query
#  select submission_id from sub_treeblock where treeblock_id = ?
# and return a list of submission objects
sub get_r2_subobject_no_check {
    my ($self, $attr) = @_;
#    $attr = uc $attr;
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

=item r2_subobject_query()

Creates a SQL statement to resolve the many-to-many relationship (through intersection table)
between the invocant object and the supplied attribute. It does this by looking up the class
name to instantiate from (by calling r2_class()), the intersection table to look up the relation
(by calling r2_table()) and the field name of the id column in the intersection table (by calling
r2_id_attr()).

See description of %r2_attr hash in TreeBaseObjects.

=cut

sub r2_subobject_query {
    my ($self, $attr) = @_;

    my $target_class = $self->r2_class($attr);
    my $target_table = $self->r2_table($attr);
    my $field = $self->r2_id_attr($attr);
    my $id_field = $self->id_attr;
    my $q = "select $field from $target_table where $id_field = ?";
    return $q;
}

=item r2_id_attr()

Returns name of the foreign key column in the intersection table of the referenced objects 
(as opposed to instances of the invocant column) in a many-to-many relation.  By default, it 
consults %r2_attr first. And if that doesn't work, it consults the foreign class's %r2 instead, 
to see if the relationship was defined in the other direction.

See description of %r2_attr hash in TreeBaseObjects. This method returns the 3rd element (index 2)
in the value array ref.

=cut

sub r2_id_attr {
    my ($self, $attr) = @_;
    $self->r2_attr_hash()->{$attr}->[2] || $self->r2_class($attr)->id_attr;
}

=item to_str()

Stringification method. Returns at least the invocant's class name and its ID number, possibly
augmented by other attributes (as implemented in child classes).

=cut

sub to_str { my $self = shift; 
	     my %attr = @_;
	     return $self->class  . " #" . $self->id; }

=item id()

Returns the invocant's identifier number.

=cut

sub id { $_[0]{'id'} }

=item id_attr()

Returns the name of the column that contains the primary key for instances of the invocant class.

=cut

sub id_attr { return lc($_[0]->class . "_id") };

=item class()

Returns the invocant class name.

=cut

sub class { return ref($_[0]) || $_[0]; }

my %known_class;

=item known_class_hash()

Returns a reference to the private %known_class hash, in which child classes register themselves.

=cut

sub known_class_hash { return \%known_class; }

=item register()

Called by child classes in the package body. Causes these classes to be registered in the 
%known_class hash. Can be called with arguments, in which case the arguments are considered
class names to register, or without any, in which case the class name is deduced by using
caller().

=cut

sub register {
    my $my_class = shift;
    my @classes = @_;
    @classes = scalar(caller()) unless @classes;
    for my $class (@classes) {
		push @{"$class\::ISA"}, $my_class;
		$class->known_class_hash->{uc $class} = $class; # XXX casing correct?
    }
}

=item alias()

Returns a registered alias for the supplied class name.

=cut

sub alias { 
    my ($base, $class) = @_;
    return $base->known_class_hash->{$class};
}

=item subobject_class()

Returns the class name for the supplied subobject name. This is either a value in the invocant 
class's %subobject hash (see TreeBaseObjects), an alias as returned by the alias() method or
the supplied subobject's name itself.

=cut

sub subobject_class { 
    my ($self, $subobj) = @_;
    my $subobj_class = \%{$self->class . "::subobject"};
    return $subobj_class->{$subobj} if exists $subobj_class->{$subobj};
    return $self->alias($subobj) || $subobj;#ucfirst(lc($subobj)); # XXX really?
}

=item get_id_pair()

Returns the name of the column that contains the primary key in the table onto which the invocant's
class is mapped, and the value of the id for the invocant instance.

=cut

sub get_id_pair {
    my $self = shift;
    return ($self->id_attr, $self->id);
}

=item table()

Returns the name of the table onto which the invocant's class is mapped.

=cut

sub table { return lc($_[0]->class); }

=item r_class()

Returns the class name for the supplied attribute. This is a value in the %r_attr hash.

=cut

sub r_class {
    my ($self, $r_attr) = @_;
    return $self->r_attr_hash()->{$r_attr};
}

=item r2_table()

Returns the name of the intersection table that connects instances of invocant's class to other
objects in a many-to-many relation. 

See a description of the %r2_attr hash in TreeBaseObjects. This method returns the first field
(index 0) in the value array reference.

=cut

sub r2_table {
    my ($self, $r_attr) = @_;
    return $self->r2_attr_hash()->{$r_attr}->[0];
}

=item r2_class()

Returns the class name for objects that are in a many-to-many relationship with the invocant
object through an intersection table.

See a description of the %r2_attr hash in TreeBaseObjects. This method returns the second field
(index 1) in the value array reference.

=cut

sub r2_class {
    my ($self, $r_attr) = @_;
    return $self->r2_attr_hash()->{$r_attr}->[1];
}

=item dump()

Traverses invocant, executes supplied handlers as defined by the named 'action' argument. The
'action' argument provides a subroutine reference whose first argument is the invocant, remaining
arguments are a pass-through of @_. The dump method recurses through the invocant, an operation
whose depth can be limited by providing a named 'maxdepth' argument.

=cut

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

=item recurse()

Empty placeholder method. Implemented by child classes in TreeBaseObjects.

=cut

sub recurse { }

=item consistent()

Empty placeholder method. Implemented by child classes in TreeBaseObjects. Returns true by default.

=cut

sub consistent { 1; }

=item is_nested()

Empty placeholder method. Implemented by child classes in TreeBaseObjects. Returns false by default.

=cut

sub is_nested { 0; }

=item DESTROY()

Empty destructor, needed here so that it's not dispatched to AUTOLOAD

=cut

sub DESTROY { }

=back

=head1 SEE ALSO

L<TreeBaseObjects>

=cut

1;
