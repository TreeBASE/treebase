package Bio::Phylo::TreeBASE::Result::Software;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Software

=cut

__PACKAGE__->table("software");

=head1 ACCESSORS

=head2 software_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'software_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 description

  data_type: 'varchar'
  is_nullable: 1
  size: 2000

=head2 name

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 softwareurl

  data_type: 'varchar'
  is_nullable: 1
  size: 500

=head2 softwareversion

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=cut

__PACKAGE__->add_columns(
  "software_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "software_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "description",
  { data_type => "varchar", is_nullable => 1, size => 2000 },
  "name",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "softwareurl",
  { data_type => "varchar", is_nullable => 1, size => 500 },
  "softwareversion",
  { data_type => "varchar", is_nullable => 1, size => 255 },
);
__PACKAGE__->set_primary_key("software_id");

=head1 RELATIONS

=head2 analysissteps

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Analysisstep>

=cut

__PACKAGE__->has_many(
  "analysissteps",
  "Bio::Phylo::TreeBASE::Result::Analysisstep",
  { "foreign.software_id" => "self.software_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:SDQAfMInU8j59GqqBvOiNg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
