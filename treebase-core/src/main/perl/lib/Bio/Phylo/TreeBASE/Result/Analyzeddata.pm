package Bio::Phylo::TreeBASE::Result::Analyzeddata;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Analyzeddata

=cut

__PACKAGE__->table("analyzeddata");

=head1 ACCESSORS

=head2 type

  data_type: 'char'
  is_nullable: 0
  size: 1

=head2 analyzeddata_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'analyzeddata_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 input

  data_type: 'boolean'
  is_nullable: 1

=head2 notes

  data_type: 'varchar'
  is_nullable: 1
  size: 2000

=head2 treelength

  data_type: 'integer'
  is_nullable: 1

=head2 analysisstep_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 matrix_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 phylotree_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "type",
  { data_type => "char", is_nullable => 0, size => 1 },
  "analyzeddata_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "analyzeddata_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "input",
  { data_type => "boolean", is_nullable => 1 },
  "notes",
  { data_type => "varchar", is_nullable => 1, size => 2000 },
  "treelength",
  { data_type => "integer", is_nullable => 1 },
  "analysisstep_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "matrix_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "phylotree_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("analyzeddata_id");

=head1 RELATIONS

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

=head2 phylotree

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotree>

=cut

__PACKAGE__->belongs_to(
  "phylotree",
  "Bio::Phylo::TreeBASE::Result::Phylotree",
  { phylotree_id => "phylotree_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 analysisstep

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Analysisstep>

=cut

__PACKAGE__->belongs_to(
  "analysisstep",
  "Bio::Phylo::TreeBASE::Result::Analysisstep",
  { analysisstep_id => "analysisstep_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:2JF1Q0rUjUer1dnLfetDRQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
