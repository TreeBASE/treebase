package Bio::Phylo::TreeBASE::Result::LeftchangesetCharstate;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::LeftchangesetCharstate

=cut

__PACKAGE__->table("leftchangeset_charstate");

=head1 ACCESSORS

=head2 statechangeset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 discretecharstate_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "statechangeset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "discretecharstate_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);

=head1 RELATIONS

=head2 statechangeset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Statechangeset>

=cut

__PACKAGE__->belongs_to(
  "statechangeset",
  "Bio::Phylo::TreeBASE::Result::Statechangeset",
  { statechangeset_id => "statechangeset_id" },
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
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:7AGOGUAlwhmEpe5lE+bDPg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
