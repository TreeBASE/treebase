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

=head1 OBJECT ATTRIBUTES

In general, if a database object, represented by C<$X>, has an attribute named C<foo>,
then C< $X->foo > or C< $x->get('foo') > retrieves the value of the attribute.  If the
attribute is a scalar, the value is returned as a Perl scalar; if the attribute is a
reference to another database object, a Perl object is returned.

Each object is assumed to correspond to a single table in the database.  If the object
class is C<ObjectClass>, the corresponding table name is assumed to be C<objectclass>.
This can be overridden by defining the C<ObjectClass::table> method, which should return
the correct table name.

Each object from class C<ObjectClass> is assumed to have a unique ID attribute which is stored
in the table in a field whose name is returned by C<ObjectClass::id_attr>.  This defaults to
C<objectclass_id> if the method is not overridden.

C<ObjectClass::new(C<$id>)> will create an object with the specified ID number.  Objects
are created lazily: the database is not consulted until some other attribute of the object
is read.

Attributes are of four types.  The call C< $Object->foo > will be resolved in the
following order:

=over 4

=item 1. Direct attributes. 

If the object's table has a column whose name is exactly C<foo>, the value from that
column will be returned directly.  SQL C<NULL> values are returned as Perl C<undef>.  

Technical details: Whether an attribute name is considered a direct attribute is
determined by the return value of the C<has_attr> method.  If C<has_attr> returns true,
C<get_no_check> is called to produce the attribute value.  See the descriptions of those
methods below for details.

=item 2. Subobjects.

If the object's table has a column whose name is C<foo_id>, its value is taken to be a
foreign key, joining to a table named C<foo>.  The corresponding record is looked up in
the joined table, and an object of class C<Foo> will be constructed and returned.

Technical details: Whether an attribute is considered to be a subobject name is determined
by the return value of the C<has_subobject> method.  The foreign key column name may be
overridden by redefining the C<foreign_key> method.  The name of the instantiated class
may be overridden by redefining the C<subobject_class> method.  The name of the joined
table may be overridden by redefining the C<table> method for the instantiated class.  See
the descriptions of those methods below for details.

=back

=item 3. Reverse subobjects.

If the object's package contains an C<%r_attr> hash with key C<foo>, the value is taken to
be a class that contains a foreign key linking to the invocant's table.  The tables are
joined and all foreign objects linking to the invocant are returned.

Technical details: An attribute name is considered to designate a reverse subobject when
the C<has_r_attr> method returns true.  If so, the C<r_class> method is called to
determine the class from which the subobjects will be instantiated, that class's C<table>
method will determine the table joined, and that class's C<foreign_key> method will be
called to determine the foreign key column for the join.  See the descriptions of those
methods below for details.

=item 4. Linked objects.

If the invocant's package contains an C<%r2_attr> hash with key C<foo>, the value should be
an array whose first element is the name of a link table that contains keys for both the
invocant's table and a foreign table.  The three tables are joined, and the objects from
the foreign table that link through to the invocant are returned.  The value
C<$r2_attr{foo}> has the following format:

	[ link table name,
          class in which foreign objects are instantiated,
          column of link table with foreign keys ]

The third of these is optional; if omitted, the name returned by the foreign class's
C<id_attr> method is used.

Technical details: An attribute name is considered to designate a linked object when the
C<has_r2_attr> method returns true.  The C<r2_table> method is called to determine the
name of the link table.  The invocant's ID is looked up in the column of the link table
named by the invocant's C<id_field> method, and the corresponding values of the column
named by the C<r2_id_attr> method are gathered.  An object is allocated for each resulting
foreign id, in the class named by the C<r2_class> method.  See the descriptions of those
methods below for details.

For example, consider the following tables:

    study             study_author		person

    study_id          study_id  person_id       person_id
    1                 1         100             100
    2                 2         101             101
    3                 2         102             102
    4                 2         103             103
                      3         102
                      4         101
                      4         103

Suppose that C<%study::r2_attr> contains:

    authors => [ 'study_author', 'Person', 'person_id' ]

The link table is C<study_author>, and the foreign table is C<person>.  Then 
C< Study->new(4)->authors > will return C<Person> objects 101 and 103.


=back

=head1 CLASS METHODS

=over

=item set_db_connection()

Sets the database handle for the invoking child class. Called as a package method.

=cut

sub set_db_connection { my $class = shift;  $DBH = $dbh{$class} = shift; }

=item get_db_connection()

Gets the database handle for the invoking child class. Called as a package method.

=cut

sub get_db_connection { my $class = shift; return $dbh{$class}; }

# Private; should remain undocumented
sub _prepare_cached {
    my ($self, $q) = @_;
    return $DBH->prepare_cached($q);
}

=head1 INSTANCE METHODS

=item new()

Instantiates an instance of one of the classes defined in TreeBaseObjects. This
constructor requires that the singleton database handle $CIPRES::TreeBase::VeryBadORM::DBH
has been defined and that a valid ID is supplied as argument.

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

# Maybe add some caching here at some point
#
# Do not document this; it's private
sub AUTOLOAD {
    our $AUTOLOAD;
    my ($package, $method) = $AUTOLOAD =~ /(.*)::(.*)/;
    @_ = ($_[0], $method);
    my $get = $package->can("get");
    goto &$get;
}

=item has_attr()

Given an attribute name, check to see if the invocant's class has a direct attribute with
that name, and return true or false.  

By default, it looks for the attribute name as a key in the hash returned by the
C<attr_hash()> method.

=cut

sub has_attr {
    my $base = shift;
    my $class = ref($base) || $base;
    return $class->attr_hash()->{shift()};
}

=item has_r_attr()

Given an attribute name, check to see if the invocant's class has a reverse attribute with
that name, and return true or false.

By default, this just passes the attribute name to the C<r_class> method to see if an
instantiating class is known for the reverse attribute.

=cut

sub has_r_attr {
    my $base = shift;
    my $class = ref($base) || $base;
    return $class->r_class(shift());
}

=item has_r2_attr()

Given an attribute name, check to see if the invocant's class has a link attribute with
that name, and return true or false.

By default, this just passes the attribute name to the C<r2_class> method to see if an
instantiating class is known for the link attribute.

=cut

sub has_r2_attr {
    my $base = shift;
    my $class = ref($base) || $base;
    return $class->r2_class(shift());
}

=item has_subobject()

Given an attribute name, check to see if the invocant's class has a subobject attribute with
that name, and return true or false.

By default, this method first obtains the name of the foreign key field used for this
attribute, and then checks to see if the invocant has a direct attribute with that name.

=cut

sub has_subobject {
    my $base = shift;
    my $subobj = shift;
    my $fk = $base->foreign_key($subobj) or return;
    return $base->has_attr($fk);
}

=item foreign_key()

Given an attribute name, return the name of the field that stores foreign keys for that
attribute.

=cut

sub foreign_key {
    my $base = shift;
    my $subobj = lc(shift());
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

Given an invocant and an attribute name, returns the attribute value. 

See the section L<> for details of how attribute names are resolved.

=cut

sub get {
    my ($self, $attr) = @_;
    if ($self->has_attr($attr)) {
		return $self->get_no_check($attr);
    } elsif ($self->has_subobject($attr)) {
		return $self->get_subobject_no_check($attr, @_);
    } elsif ($self->has_r_attr($attr)) {
		return $self->get_r_subobject_no_check($attr, @_);
    } elsif ($self->has_r2_attr($attr)) {
		return $self->get_r2_subobject_no_check($attr, @_);
    }
#    my $trace = Devel::StackTrace->new;
#	print $trace->as_string; # like carp
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
    my $target_class = $self->r_class($attr);
    my $target_table = $target_class->table;
    my $field = $target_class->id_attr;
    my $id_field = $target_class->foreign_key($self->class); 
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
    my $sth = $self->_prepare_cached($q);
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
(as opposed to instances of the invocant column) in a many-to-many relation.  

If C<%r2_attr> lists a target class for the referenced object,
C<r2_id_attr> uses that class's default C<id_attr>, unless that us
overriden by C<%r2_attr>.  For example, if C<Study> has:

    %Study::r2_attr = (nexusfiles => ['study_nexus', 'Nexus'])

then the C<nexus_id> column will be consulted, unless
C<Nexus->id_attr> returns something else.  But if the attribute is
given explicitly, like this:

    %Study::r2_attr = (nexusfiles => ['study_nexus', 'Nexus', 'nexusfileID'])

then the C<nexusfileID> column of the C<study_nexus> table will be consulted.

=cut

sub r2_id_attr {
    my ($self, $attr) = @_;
# Would it make more sense to use $self->foreign_key($attr) as the fallback here?
# 20091125 MJD
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

Given an attribute name, return the name of the class that represents that attribute.

The default version looks up the attribute name as a key in the hash C<%subobject>, and,
returns the associated value, if there is one.

If not, it converts the attribute name to all-lowercase, uppercases the first character,
and uses the result.   For example, the default class for the C<potato> attribute is C<Potato>.

For example, suppose there are C<Dessert> objects and C<Flavor> objects.  Suppose each
C<Dessert> has a C<flavor> and an C<alternate_flavor> attribute, which are C<Flavor>
objects.  One could represent this by defining:

    %Dessert::subobject = (flavor => 'Flavor',
                           alternate_flavor => 'Flavor',
                          );

which says that whenever a C<Dessert> object's C<flavor> or C<alternate_flavor> attributes
are accessed, C<VeryBadORM> should instantiate them as C<Flavor> objects.

But one could omit the first entry from the hash:

    %Dessert::subobject = (alternate_flavor => 'Flavor');

since the class for the C<flavor> attribute will be inferred to be C<Flavor> by default.

One may, of course, override this method to implement any mapping of attribute to class names that
is desired.

=cut

sub subobject_class { 
    my ($self, $subobj) = @_;
    my $subobj_class = \%{$self->class . "::subobject"};
    return $subobj_class->{lc $subobj} if exists $subobj_class->{lc $subobj};
    return $self->alias($subobj) || ucfirst(lc($subobj));
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
