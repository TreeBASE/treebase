package Bio::Phylo::TreeBASE::Result::TreesetPhylotree;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::TreesetPhylotree

=cut

__PACKAGE__->table("treeset_phylotree");

=head1 ACCESSORS

=head2 treeset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 phylotree_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 tree_order

  data_type: 'integer'
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "treeset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "phylotree_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "tree_order",
  { data_type => "integer", is_nullable => 0 },
);
__PACKAGE__->set_primary_key("treeset_id", "tree_order");

=head1 RELATIONS

=head2 treeset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Treeset>

=cut

__PACKAGE__->belongs_to(
  "treeset",
  "Bio::Phylo::TreeBASE::Result::Treeset",
  { treeset_id => "treeset_id" },
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
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:XZiXhfSLm6bOfMOn2vwiQA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
