package Bio::Phylo::TreeBASE::Result::MatrixcolumnItemdefinition;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::MatrixcolumnItemdefinition

=cut

__PACKAGE__->table("matrixcolumn_itemdefinition");

=head1 ACCESSORS

=head2 matrixcolumn_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 itemdefinition_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "matrixcolumn_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "itemdefinition_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("matrixcolumn_id", "itemdefinition_id");

=head1 RELATIONS

=head2 matrixcolumn

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixcolumn>

=cut

__PACKAGE__->belongs_to(
  "matrixcolumn",
  "Bio::Phylo::TreeBASE::Result::Matrixcolumn",
  { matrixcolumn_id => "matrixcolumn_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 itemdefinition

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Itemdefinition>

=cut

__PACKAGE__->belongs_to(
  "itemdefinition",
  "Bio::Phylo::TreeBASE::Result::Itemdefinition",
  { itemdefinition_id => "itemdefinition_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:PZdFLW13RABxGy8GGIzjiw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
