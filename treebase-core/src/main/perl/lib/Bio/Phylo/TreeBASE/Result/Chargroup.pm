package Bio::Phylo::TreeBASE::Result::Chargroup;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Chargroup

=cut

__PACKAGE__->table("chargroup");

=head1 ACCESSORS

=head2 chargroup_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'chargroup_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 charpartition_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "chargroup_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "chargroup_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "charpartition_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("chargroup_id");

=head1 RELATIONS

=head2 charpartition

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Charpartition>

=cut

__PACKAGE__->belongs_to(
  "charpartition",
  "Bio::Phylo::TreeBASE::Result::Charpartition",
  { charpartition_id => "charpartition_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 chargroup_colranges

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::ChargroupColrange>

=cut

__PACKAGE__->has_many(
  "chargroup_colranges",
  "Bio::Phylo::TreeBASE::Result::ChargroupColrange",
  { "foreign.chargroup_id" => "self.chargroup_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:K4U0nWHRs+OHlRRhkls9Hg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
