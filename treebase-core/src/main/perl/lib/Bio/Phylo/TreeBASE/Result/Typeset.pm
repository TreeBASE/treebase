package Bio::Phylo::TreeBASE::Result::Typeset;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Typeset

=cut

__PACKAGE__->table("typeset");

=head1 ACCESSORS

=head2 typeset_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'typeset_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

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
  "typeset_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "typeset_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "matrix_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("typeset_id");

=head1 RELATIONS

=head2 matrixes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Matrix>

=cut

__PACKAGE__->has_many(
  "matrixes",
  "Bio::Phylo::TreeBASE::Result::Matrix",
  { "foreign.typeset_id" => "self.typeset_id" },
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

=head2 usertyperecords

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Usertyperecord>

=cut

__PACKAGE__->has_many(
  "usertyperecords",
  "Bio::Phylo::TreeBASE::Result::Usertyperecord",
  { "foreign.typeset_id" => "self.typeset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:7Tt6LEDXDlXWgWzlR5SKBg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
