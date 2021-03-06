package Bio::Phylo::TreeBASE::Result::SubTreeblock;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::SubTreeblock

=cut

__PACKAGE__->table("sub_treeblock");

=head1 ACCESSORS

=head2 submission_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 treeblock_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 collection_id

  data_type: 'bigint'
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "submission_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "treeblock_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "collection_id",
  { data_type => "bigint", is_nullable => 0 },
);
__PACKAGE__->set_primary_key("collection_id");
__PACKAGE__->add_unique_constraint("sub_treeblock_treeblock_id_key", ["treeblock_id"]);

=head1 RELATIONS

=head2 submission

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Submission>

=cut

__PACKAGE__->belongs_to(
  "submission",
  "Bio::Phylo::TreeBASE::Result::Submission",
  { submission_id => "submission_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 treeblock

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Treeblock>

=cut

__PACKAGE__->belongs_to(
  "treeblock",
  "Bio::Phylo::TreeBASE::Result::Treeblock",
  { treeblock_id => "treeblock_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:KxkM5BnR1jW05sZGdl0FBA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
