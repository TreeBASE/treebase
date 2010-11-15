package Bio::Phylo::TreeBASE::Result::Phylotreenode;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Phylotreenode

=cut

__PACKAGE__->table("phylotreenode");

=head1 ACCESSORS

=head2 phylotreenode_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'phylotreenode_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 branchlength

  data_type: 'double precision'
  is_nullable: 1

=head2 leftnode

  data_type: 'bigint'
  is_nullable: 1

=head2 name

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 nodedepth

  data_type: 'integer'
  is_nullable: 1

=head2 rightnode

  data_type: 'bigint'
  is_nullable: 1

=head2 child_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 nodeattribute_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 parent_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 sibling_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 taxonlabel_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 phylotree_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "phylotreenode_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "phylotreenode_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "branchlength",
  { data_type => "double precision", is_nullable => 1 },
  "leftnode",
  { data_type => "bigint", is_nullable => 1 },
  "name",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "nodedepth",
  { data_type => "integer", is_nullable => 1 },
  "rightnode",
  { data_type => "bigint", is_nullable => 1 },
  "child_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "nodeattribute_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "parent_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "sibling_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "taxonlabel_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "phylotree_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("phylotreenode_id");

=head1 RELATIONS

=head2 phylotrees

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotree>

=cut

__PACKAGE__->has_many(
  "phylotrees",
  "Bio::Phylo::TreeBASE::Result::Phylotree",
  { "foreign.rootnode_id" => "self.phylotreenode_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 child

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotreenode>

=cut

__PACKAGE__->belongs_to(
  "child",
  "Bio::Phylo::TreeBASE::Result::Phylotreenode",
  { phylotreenode_id => "child_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 phylotreenode_children

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotreenode>

=cut

__PACKAGE__->has_many(
  "phylotreenode_children",
  "Bio::Phylo::TreeBASE::Result::Phylotreenode",
  { "foreign.child_id" => "self.phylotreenode_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 parent

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotreenode>

=cut

__PACKAGE__->belongs_to(
  "parent",
  "Bio::Phylo::TreeBASE::Result::Phylotreenode",
  { phylotreenode_id => "parent_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 phylotreenode_parents

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotreenode>

=cut

__PACKAGE__->has_many(
  "phylotreenode_parents",
  "Bio::Phylo::TreeBASE::Result::Phylotreenode",
  { "foreign.parent_id" => "self.phylotreenode_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 phylotree

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotree>

=cut

__PACKAGE__->belongs_to(
  "phylotree",
  "Bio::Phylo::TreeBASE::Result::Phylotree",
  { phylotree_id => "phylotree_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 nodeattribute

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Nodeattribute>

=cut

__PACKAGE__->belongs_to(
  "nodeattribute",
  "Bio::Phylo::TreeBASE::Result::Nodeattribute",
  { nodeattribute_id => "nodeattribute_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 sibling

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotreenode>

=cut

__PACKAGE__->belongs_to(
  "sibling",
  "Bio::Phylo::TreeBASE::Result::Phylotreenode",
  { phylotreenode_id => "sibling_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 phylotreenodes_sibling

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotreenode>

=cut

__PACKAGE__->has_many(
  "phylotreenodes_sibling",
  "Bio::Phylo::TreeBASE::Result::Phylotreenode",
  { "foreign.sibling_id" => "self.phylotreenode_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 taxonlabel

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlabel>

=cut

__PACKAGE__->belongs_to(
  "taxonlabel",
  "Bio::Phylo::TreeBASE::Result::Taxonlabel",
  { taxonlabel_id => "taxonlabel_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 treenodeedge_childnodes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Treenodeedge>

=cut

__PACKAGE__->has_many(
  "treenodeedge_childnodes",
  "Bio::Phylo::TreeBASE::Result::Treenodeedge",
  { "foreign.childnode_id" => "self.phylotreenode_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 treenodeedge_parentnodes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Treenodeedge>

=cut

__PACKAGE__->has_many(
  "treenodeedge_parentnodes",
  "Bio::Phylo::TreeBASE::Result::Treenodeedge",
  { "foreign.parentnode_id" => "self.phylotreenode_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:6ct7LxnayzTeM6WTvsYqqg


# You can replace this text with custom content, and it will be preserved on regeneration
use Bio::Phylo::Forest::Node;
push @Bio::Phylo::TreeBASE::Result::Phylotreenode::ISA, 'Bio::Phylo::Forest::Node';

sub get_id {
	my $self = shift;
	my $id = $self->phylotreenode_id;
	my $version = $self->version;
	return "${id}.${version}";
}

sub get_children {
	my $self = shift;
	my @children;
	if ( my $child = $self->child ) {
		push @children, $child;
		while( $child = $child->sibling ) {
			push @children, $child;
		}
	}
	return \@children;
}

sub get_parent {
	shift->parent;
}

sub get_next_sister {
	shift->sibling;
}

sub get_branch_length {
	shift->branchlength;
}

sub get_taxon {
	shift->taxonlabel;
}

sub get_name {
	shift->name;
}

1;
