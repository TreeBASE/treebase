package Bio::Phylo::TreeBASE::Result::Itemdefinition;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Itemdefinition

=cut

__PACKAGE__->table("itemdefinition");

=head1 ACCESSORS

=head2 itemdefinition_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'itemdefinition_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 description

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=cut

__PACKAGE__->add_columns(
  "itemdefinition_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "itemdefinition_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "description",
  { data_type => "varchar", is_nullable => 1, size => 255 },
);
__PACKAGE__->set_primary_key("itemdefinition_id");

=head1 RELATIONS

=head2 matrixcolumn_itemdefinitions

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::MatrixcolumnItemdefinition>

=cut

__PACKAGE__->has_many(
  "matrixcolumn_itemdefinitions",
  "Bio::Phylo::TreeBASE::Result::MatrixcolumnItemdefinition",
  { "foreign.itemdefinition_id" => "self.itemdefinition_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 matrix_itemdefinitions

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::MatrixItemdefinition>

=cut

__PACKAGE__->has_many(
  "matrix_itemdefinitions",
  "Bio::Phylo::TreeBASE::Result::MatrixItemdefinition",
  { "foreign.itemdefinition_id" => "self.itemdefinition_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:a9P96J8KG6vucBUaPUAGzg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
