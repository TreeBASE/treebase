package Bio::Phylo::TreeBASE::Result::Stateset;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Stateset

=cut

__PACKAGE__->table("stateset");

=head1 ACCESSORS

=head2 stateset_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'stateset_id_sequence'

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
  "stateset_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "stateset_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "matrix_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("stateset_id");

=head1 RELATIONS

=head2 discretecharstates

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Discretecharstate>

=cut

__PACKAGE__->has_many(
  "discretecharstates",
  "Bio::Phylo::TreeBASE::Result::Discretecharstate",
  { "foreign.stateset_id" => "self.stateset_id" },
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
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:PzUNYMSbz5wULmjxNJgLMw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
