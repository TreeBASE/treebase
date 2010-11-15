package Bio::Phylo::TreeBASE::Result::Ancestralstate;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Ancestralstate

=cut

__PACKAGE__->table("ancestralstate");

=head1 ACCESSORS

=head2 type

  data_type: 'char'
  is_nullable: 0
  size: 1

=head2 ancestralstate_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'ancestralstate_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 ancvalue

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 discretecharstate_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 ancstateset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "type",
  { data_type => "char", is_nullable => 0, size => 1 },
  "ancestralstate_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "ancestralstate_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "ancvalue",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "discretecharstate_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "ancstateset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("ancestralstate_id");

=head1 RELATIONS

=head2 discretecharstate

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Discretecharstate>

=cut

__PACKAGE__->belongs_to(
  "discretecharstate",
  "Bio::Phylo::TreeBASE::Result::Discretecharstate",
  { discretecharstate_id => "discretecharstate_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 ancstateset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Ancstateset>

=cut

__PACKAGE__->belongs_to(
  "ancstateset",
  "Bio::Phylo::TreeBASE::Result::Ancstateset",
  { ancstateset_id => "ancstateset_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 contancstate_values

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::ContancstateValue>

=cut

__PACKAGE__->has_many(
  "contancstate_values",
  "Bio::Phylo::TreeBASE::Result::ContancstateValue",
  { "foreign.ancstate_id" => "self.ancestralstate_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 discretecharstates

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Discretecharstate>

=cut

__PACKAGE__->has_many(
  "discretecharstates",
  "Bio::Phylo::TreeBASE::Result::Discretecharstate",
  { "foreign.ancestralstate_id" => "self.ancestralstate_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:0O8yGH1q0Ln7F26g24sJnA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
