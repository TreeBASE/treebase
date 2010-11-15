package Bio::Phylo::TreeBASE::Result::TreegroupPhylotree;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::TreegroupPhylotree

=cut

__PACKAGE__->table("treegroup_phylotree");

=head1 ACCESSORS

=head2 treegroup_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 phylotree_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "treegroup_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "phylotree_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("treegroup_id", "phylotree_id");

=head1 RELATIONS

=head2 treegroup

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Treegroup>

=cut

__PACKAGE__->belongs_to(
  "treegroup",
  "Bio::Phylo::TreeBASE::Result::Treegroup",
  { treegroup_id => "treegroup_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
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


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:DCx5a8RQGtSCAdFCcXimNQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
