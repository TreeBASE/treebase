package Bio::Phylo::TreeBASE::Result::Statechangeset;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Statechangeset

=cut

__PACKAGE__->table("statechangeset");

=head1 ACCESSORS

=head2 statechangeset_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'statechangeset_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 reversible

  data_type: 'boolean'
  is_nullable: 1

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=cut

__PACKAGE__->add_columns(
  "statechangeset_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "statechangeset_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "reversible",
  { data_type => "boolean", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
);
__PACKAGE__->set_primary_key("statechangeset_id");

=head1 RELATIONS

=head2 leftchangeset_charstates

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::LeftchangesetCharstate>

=cut

__PACKAGE__->has_many(
  "leftchangeset_charstates",
  "Bio::Phylo::TreeBASE::Result::LeftchangesetCharstate",
  { "foreign.statechangeset_id" => "self.statechangeset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 rightchangeset_charstates

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::RightchangesetCharstate>

=cut

__PACKAGE__->has_many(
  "rightchangeset_charstates",
  "Bio::Phylo::TreeBASE::Result::RightchangesetCharstate",
  { "foreign.statechangeset_id" => "self.statechangeset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:TfQKmB4Bpfe4Ou20VNrNqw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
