package Bio::Phylo::TreeBASE::Result::Citationstatus;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Citationstatus

=cut

__PACKAGE__->table("citationstatus");

=head1 ACCESSORS

=head2 citationstatus_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'citationstatus_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 description

  data_type: 'varchar'
  is_nullable: 1
  size: 50

=cut

__PACKAGE__->add_columns(
  "citationstatus_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "citationstatus_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "description",
  { data_type => "varchar", is_nullable => 1, size => 50 },
);
__PACKAGE__->set_primary_key("citationstatus_id");

=head1 RELATIONS

=head2 citations

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Citation>

=cut

__PACKAGE__->has_many(
  "citations",
  "Bio::Phylo::TreeBASE::Result::Citation",
  { "foreign.citationstatus_id" => "self.citationstatus_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:MAH1c/O+u5/ybzF/M1S0/w


# You can replace this text with custom content, and it will be preserved on regeneration
1;
