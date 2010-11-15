package Bio::Phylo::TreeBASE::Result::Stepmatrixelement;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Stepmatrixelement

=cut

__PACKAGE__->table("stepmatrixelement");

=head1 ACCESSORS

=head2 stepmatrixelement_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'stepmatrixelement_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 transcost

  data_type: 'double precision'
  is_nullable: 1

=head2 state1_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 state2_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 stepmatrix_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "stepmatrixelement_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "stepmatrixelement_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "transcost",
  { data_type => "double precision", is_nullable => 1 },
  "state1_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "state2_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "stepmatrix_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("stepmatrixelement_id");

=head1 RELATIONS

=head2 state1

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Discretecharstate>

=cut

__PACKAGE__->belongs_to(
  "state1",
  "Bio::Phylo::TreeBASE::Result::Discretecharstate",
  { discretecharstate_id => "state1_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 stepmatrix

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Usertype>

=cut

__PACKAGE__->belongs_to(
  "stepmatrix",
  "Bio::Phylo::TreeBASE::Result::Usertype",
  { usertype_id => "stepmatrix_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 state2

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Discretecharstate>

=cut

__PACKAGE__->belongs_to(
  "state2",
  "Bio::Phylo::TreeBASE::Result::Discretecharstate",
  { discretecharstate_id => "state2_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:k/KSiBx6TMksbRt+443wsQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
