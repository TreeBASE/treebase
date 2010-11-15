package Bio::Phylo::TreeBASE::Result::Treeset;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Treeset

=cut

__PACKAGE__->table("treeset");

=head1 ACCESSORS

=head2 treeset_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'treeset_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=cut

__PACKAGE__->add_columns(
  "treeset_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "treeset_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
);
__PACKAGE__->set_primary_key("treeset_id");

=head1 RELATIONS

=head2 treeset_phylotrees

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::TreesetPhylotree>

=cut

__PACKAGE__->has_many(
  "treeset_phylotrees",
  "Bio::Phylo::TreeBASE::Result::TreesetPhylotree",
  { "foreign.treeset_id" => "self.treeset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:+LNLpeTBPYoKA5SlY+ckbQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
