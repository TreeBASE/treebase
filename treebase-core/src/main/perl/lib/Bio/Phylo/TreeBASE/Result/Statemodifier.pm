package Bio::Phylo::TreeBASE::Result::Statemodifier;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Statemodifier

=cut

__PACKAGE__->table("statemodifier");

=head1 ACCESSORS

=head2 statemodifier_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'statemodifier_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 count

  data_type: 'integer'
  is_nullable: 1

=head2 frequency

  data_type: 'double precision'
  is_nullable: 1

=head2 discretecharstate_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 element_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 stateformat_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "statemodifier_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "statemodifier_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "count",
  { data_type => "integer", is_nullable => 1 },
  "frequency",
  { data_type => "double precision", is_nullable => 1 },
  "discretecharstate_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "element_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "stateformat_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("statemodifier_id");

=head1 RELATIONS

=head2 element

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixelement>

=cut

__PACKAGE__->belongs_to(
  "element",
  "Bio::Phylo::TreeBASE::Result::Matrixelement",
  { matrixelement_id => "element_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

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

=head2 stateformat

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Stateformat>

=cut

__PACKAGE__->belongs_to(
  "stateformat",
  "Bio::Phylo::TreeBASE::Result::Stateformat",
  { stateformat_id => "stateformat_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:LJtt8AmNzcPZUW/7FOTTUA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
