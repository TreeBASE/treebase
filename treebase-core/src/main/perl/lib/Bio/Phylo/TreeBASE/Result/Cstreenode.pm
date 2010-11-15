package Bio::Phylo::TreeBASE::Result::Cstreenode;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Cstreenode

=cut

__PACKAGE__->table("cstreenode");

=head1 ACCESSORS

=head2 cstreenode_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'cstreenode_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 discretecharstate_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 parentnode_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 cstree_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "cstreenode_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "cstreenode_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "discretecharstate_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "parentnode_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "cstree_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("cstreenode_id");

=head1 RELATIONS

=head2 parentnode

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Cstreenode>

=cut

__PACKAGE__->belongs_to(
  "parentnode",
  "Bio::Phylo::TreeBASE::Result::Cstreenode",
  { cstreenode_id => "parentnode_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 cstreenodes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Cstreenode>

=cut

__PACKAGE__->has_many(
  "cstreenodes",
  "Bio::Phylo::TreeBASE::Result::Cstreenode",
  { "foreign.parentnode_id" => "self.cstreenode_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 cstree

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Usertype>

=cut

__PACKAGE__->belongs_to(
  "cstree",
  "Bio::Phylo::TreeBASE::Result::Usertype",
  { usertype_id => "cstree_id" },
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


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:dJTF2Kb/NGhib3eukMEdbw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
