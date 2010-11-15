package Bio::Phylo::TreeBASE::Result::Discretecharstate;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Discretecharstate

=cut

__PACKAGE__->table("discretecharstate");

=head1 ACCESSORS

=head2 discretecharstate_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'discretecharstate_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 description

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 notes

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 symbol

  data_type: 'char'
  is_nullable: 1
  size: 1

=head2 phylochar_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 stateset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 ancestralstate_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "discretecharstate_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "discretecharstate_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "description",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "notes",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "symbol",
  { data_type => "char", is_nullable => 1, size => 1 },
  "phylochar_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "stateset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "ancestralstate_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("discretecharstate_id");

=head1 RELATIONS

=head2 ancestralstates

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Ancestralstate>

=cut

__PACKAGE__->has_many(
  "ancestralstates",
  "Bio::Phylo::TreeBASE::Result::Ancestralstate",
  { "foreign.discretecharstate_id" => "self.discretecharstate_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 cstreenodes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Cstreenode>

=cut

__PACKAGE__->has_many(
  "cstreenodes",
  "Bio::Phylo::TreeBASE::Result::Cstreenode",
  { "foreign.discretecharstate_id" => "self.discretecharstate_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 stateset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Stateset>

=cut

__PACKAGE__->belongs_to(
  "stateset",
  "Bio::Phylo::TreeBASE::Result::Stateset",
  { stateset_id => "stateset_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 phylochar

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Phylochar>

=cut

__PACKAGE__->belongs_to(
  "phylochar",
  "Bio::Phylo::TreeBASE::Result::Phylochar",
  { phylochar_id => "phylochar_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 ancestralstate

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Ancestralstate>

=cut

__PACKAGE__->belongs_to(
  "ancestralstate",
  "Bio::Phylo::TreeBASE::Result::Ancestralstate",
  { ancestralstate_id => "ancestralstate_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 leftchangeset_charstates

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::LeftchangesetCharstate>

=cut

__PACKAGE__->has_many(
  "leftchangeset_charstates",
  "Bio::Phylo::TreeBASE::Result::LeftchangesetCharstate",
  { "foreign.discretecharstate_id" => "self.discretecharstate_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 rightchangeset_charstates

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::RightchangesetCharstate>

=cut

__PACKAGE__->has_many(
  "rightchangeset_charstates",
  "Bio::Phylo::TreeBASE::Result::RightchangesetCharstate",
  { "foreign.discretecharstate_id" => "self.discretecharstate_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 statemodifiers

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Statemodifier>

=cut

__PACKAGE__->has_many(
  "statemodifiers",
  "Bio::Phylo::TreeBASE::Result::Statemodifier",
  { "foreign.discretecharstate_id" => "self.discretecharstate_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 stepmatrixelement_state1s

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Stepmatrixelement>

=cut

__PACKAGE__->has_many(
  "stepmatrixelement_state1s",
  "Bio::Phylo::TreeBASE::Result::Stepmatrixelement",
  { "foreign.state1_id" => "self.discretecharstate_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 stepmatrixelement_state2s

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Stepmatrixelement>

=cut

__PACKAGE__->has_many(
  "stepmatrixelement_state2s",
  "Bio::Phylo::TreeBASE::Result::Stepmatrixelement",
  { "foreign.state2_id" => "self.discretecharstate_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:sePXuvHfzJzSxam060aBOg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
