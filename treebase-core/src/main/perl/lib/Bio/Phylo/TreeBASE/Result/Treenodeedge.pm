package Bio::Phylo::TreeBASE::Result::Treenodeedge;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Treenodeedge

=cut

__PACKAGE__->table("treenodeedge");

=head1 ACCESSORS

=head2 treenodeedge_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'treenodeedge_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 childnode_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 parentnode_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "treenodeedge_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "treenodeedge_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "childnode_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "parentnode_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("treenodeedge_id");

=head1 RELATIONS

=head2 childnode

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotreenode>

=cut

__PACKAGE__->belongs_to(
  "childnode",
  "Bio::Phylo::TreeBASE::Result::Phylotreenode",
  { phylotreenode_id => "childnode_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 parentnode

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotreenode>

=cut

__PACKAGE__->belongs_to(
  "parentnode",
  "Bio::Phylo::TreeBASE::Result::Phylotreenode",
  { phylotreenode_id => "parentnode_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:Oin+MLlpnrFZ9B6BRbYJGA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
