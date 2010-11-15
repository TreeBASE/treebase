package Bio::Phylo::TreeBASE::Result::Geneticcodeset;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Geneticcodeset

=cut

__PACKAGE__->table("geneticcodeset");

=head1 ACCESSORS

=head2 geneticcodeset_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'geneticcodeset_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 format

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 matrix_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "geneticcodeset_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "geneticcodeset_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "format",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "matrix_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("geneticcodeset_id");

=head1 RELATIONS

=head2 geneticcoderecords

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Geneticcoderecord>

=cut

__PACKAGE__->has_many(
  "geneticcoderecords",
  "Bio::Phylo::TreeBASE::Result::Geneticcoderecord",
  { "foreign.geneticcodeset_id" => "self.geneticcodeset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
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


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:sMaInv9u8FlOEHJJAJ8U2w


# You can replace this text with custom content, and it will be preserved on regeneration
1;
