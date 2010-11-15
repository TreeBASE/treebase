package Bio::Phylo::TreeBASE::Result::Matrixcolumn;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Matrixcolumn

=cut

__PACKAGE__->table("matrixcolumn");

=head1 ACCESSORS

=head2 matrixcolumn_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'matrixcolumn_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 phylochar_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 matrix_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 stateformat_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 column_order

  data_type: 'integer'
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "matrixcolumn_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "matrixcolumn_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "phylochar_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "matrix_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "stateformat_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "column_order",
  { data_type => "integer", is_nullable => 1 },
);
__PACKAGE__->set_primary_key("matrixcolumn_id");

=head1 RELATIONS

=head2 phylochar

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Phylochar>

=cut

__PACKAGE__->belongs_to(
  "phylochar",
  "Bio::Phylo::TreeBASE::Result::Phylochar",
  { phylochar_id => "phylochar_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 matrix

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Matrix>

=cut

__PACKAGE__->belongs_to(
  "matrix",
  "Bio::Phylo::TreeBASE::Result::Matrix",
  { matrix_id => "matrix_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 stateformat

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Stateformat>

=cut

__PACKAGE__->belongs_to(
  "stateformat",
  "Bio::Phylo::TreeBASE::Result::Stateformat",
  { stateformat_id => "stateformat_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 matrixcolumn_itemdefinitions

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::MatrixcolumnItemdefinition>

=cut

__PACKAGE__->has_many(
  "matrixcolumn_itemdefinitions",
  "Bio::Phylo::TreeBASE::Result::MatrixcolumnItemdefinition",
  { "foreign.matrixcolumn_id" => "self.matrixcolumn_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:AtWjE2ITEhUnfu57i/qGRg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
