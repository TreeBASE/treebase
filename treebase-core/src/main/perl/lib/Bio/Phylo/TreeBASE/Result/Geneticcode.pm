package Bio::Phylo::TreeBASE::Result::Geneticcode;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Geneticcode

=cut

__PACKAGE__->table("geneticcode");

=head1 ACCESSORS

=head2 geneticcode_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'geneticcode_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 codedescription

  data_type: 'varchar'
  is_nullable: 1
  size: 1000

=head2 codeorder

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 extensions

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 nucorder

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 predefined

  data_type: 'boolean'
  is_nullable: 1

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=cut

__PACKAGE__->add_columns(
  "geneticcode_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "geneticcode_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "codedescription",
  { data_type => "varchar", is_nullable => 1, size => 1000 },
  "codeorder",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "extensions",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "nucorder",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "predefined",
  { data_type => "boolean", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
);
__PACKAGE__->set_primary_key("geneticcode_id");

=head1 RELATIONS

=head2 geneticcoderecords

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Geneticcoderecord>

=cut

__PACKAGE__->has_many(
  "geneticcoderecords",
  "Bio::Phylo::TreeBASE::Result::Geneticcoderecord",
  { "foreign.geneticcode_id" => "self.geneticcode_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:RBE8ya++AdslbqPUNO91tw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
