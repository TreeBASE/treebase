package Bio::Phylo::TreeBASE::Result::Phylotree;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Phylotree

=cut

__PACKAGE__->table("phylotree");

=head1 ACCESSORS

=head2 phylotree_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'phylotree_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 tb1_treeid

  data_type: 'varchar'
  is_nullable: 1
  size: 30

=head2 bigtree

  data_type: 'boolean'
  is_nullable: 1

=head2 label

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 ntax

  data_type: 'integer'
  is_nullable: 1

=head2 newickstring

  data_type: 'text'
  is_nullable: 1

=head2 nexusfilename

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 published

  data_type: 'boolean'
  is_nullable: 1

=head2 rootedtree

  data_type: 'boolean'
  is_nullable: 1

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 rootnode_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 study_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 treeattribute_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 treeblock_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 treekind_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 treequality_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 treetype_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "phylotree_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "phylotree_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "tb1_treeid",
  { data_type => "varchar", is_nullable => 1, size => 30 },
  "bigtree",
  { data_type => "boolean", is_nullable => 1 },
  "label",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "ntax",
  { data_type => "integer", is_nullable => 1 },
  "newickstring",
  { data_type => "text", is_nullable => 1 },
  "nexusfilename",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "published",
  { data_type => "boolean", is_nullable => 1 },
  "rootedtree",
  { data_type => "boolean", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "rootnode_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "study_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "treeattribute_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "treeblock_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "treekind_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "treequality_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "treetype_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("phylotree_id");

=head1 RELATIONS

=head2 analyzeddatas

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Analyzeddata>

=cut

__PACKAGE__->has_many(
  "analyzeddatas",
  "Bio::Phylo::TreeBASE::Result::Analyzeddata",
  { "foreign.phylotree_id" => "self.phylotree_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 treeblock

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Treeblock>

=cut

__PACKAGE__->belongs_to(
  "treeblock",
  "Bio::Phylo::TreeBASE::Result::Treeblock",
  { treeblock_id => "treeblock_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 treetype

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Treetype>

=cut

__PACKAGE__->belongs_to(
  "treetype",
  "Bio::Phylo::TreeBASE::Result::Treetype",
  { treetype_id => "treetype_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 treekind

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Treekind>

=cut

__PACKAGE__->belongs_to(
  "treekind",
  "Bio::Phylo::TreeBASE::Result::Treekind",
  { treekind_id => "treekind_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 rootnode

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotreenode>

=cut

__PACKAGE__->belongs_to(
  "rootnode",
  "Bio::Phylo::TreeBASE::Result::Phylotreenode",
  { phylotreenode_id => "rootnode_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 treeattribute

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Treeattribute>

=cut

__PACKAGE__->belongs_to(
  "treeattribute",
  "Bio::Phylo::TreeBASE::Result::Treeattribute",
  { treeattribute_id => "treeattribute_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 treequality

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Treequality>

=cut

__PACKAGE__->belongs_to(
  "treequality",
  "Bio::Phylo::TreeBASE::Result::Treequality",
  { treequality_id => "treequality_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 study

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Study>

=cut

__PACKAGE__->belongs_to(
  "study",
  "Bio::Phylo::TreeBASE::Result::Study",
  { study_id => "study_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 phylotreenodes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotreenode>

=cut

__PACKAGE__->has_many(
  "phylotreenodes",
  "Bio::Phylo::TreeBASE::Result::Phylotreenode",
  { "foreign.phylotree_id" => "self.phylotree_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 treegroup_phylotrees

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::TreegroupPhylotree>

=cut

__PACKAGE__->has_many(
  "treegroup_phylotrees",
  "Bio::Phylo::TreeBASE::Result::TreegroupPhylotree",
  { "foreign.phylotree_id" => "self.phylotree_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 treeset_phylotrees

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::TreesetPhylotree>

=cut

__PACKAGE__->has_many(
  "treeset_phylotrees",
  "Bio::Phylo::TreeBASE::Result::TreesetPhylotree",
  { "foreign.phylotree_id" => "self.phylotree_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:o+hvUNcYOHpk3dMhhDT42g


# You can replace this text with custom content, and it will be preserved on regeneration
use Bio::Phylo::Forest::Tree;
push @Bio::Phylo::TreeBASE::Result::Phylotree::ISA, 'Bio::Phylo::Forest::Tree';

sub get_id {
	my $self = shift;
	my $id = $self->phylotree_id;
	my $version = $self->version;
	return "${id}.${version}";
}

sub get_root {
	shift->rootnode;
}

sub get_name {
	shift->label;
}

1;
