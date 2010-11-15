package Bio::Phylo::TreeBASE::Result::Gapmode;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Gapmode

=cut

__PACKAGE__->table("gapmode");

=head1 ACCESSORS

=head2 gapmode_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'gapmode_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 description

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=cut

__PACKAGE__->add_columns(
  "gapmode_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "gapmode_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "description",
  { data_type => "varchar", is_nullable => 1, size => 255 },
);
__PACKAGE__->set_primary_key("gapmode_id");

=head1 RELATIONS

=head2 algorithms

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Algorithm>

=cut

__PACKAGE__->has_many(
  "algorithms",
  "Bio::Phylo::TreeBASE::Result::Algorithm",
  { "foreign.gapmode_id" => "self.gapmode_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:2nNFFUkP7/ecq+jZdvrENQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
