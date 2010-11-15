package Bio::Phylo::TreeBASE::Result::Stateformat;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Stateformat

=cut

__PACKAGE__->table("stateformat");

=head1 ACCESSORS

=head2 stateformat_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'stateformat_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 description

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=cut

__PACKAGE__->add_columns(
  "stateformat_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "stateformat_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "description",
  { data_type => "varchar", is_nullable => 1, size => 255 },
);
__PACKAGE__->set_primary_key("stateformat_id");

=head1 RELATIONS

=head2 matrixcolumns

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixcolumn>

=cut

__PACKAGE__->has_many(
  "matrixcolumns",
  "Bio::Phylo::TreeBASE::Result::Matrixcolumn",
  { "foreign.stateformat_id" => "self.stateformat_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 statemodifiers

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Statemodifier>

=cut

__PACKAGE__->has_many(
  "statemodifiers",
  "Bio::Phylo::TreeBASE::Result::Statemodifier",
  { "foreign.stateformat_id" => "self.stateformat_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:4kyCGwjzTYYtUs8V3hG+qQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
