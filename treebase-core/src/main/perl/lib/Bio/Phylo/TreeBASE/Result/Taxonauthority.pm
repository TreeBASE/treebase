package Bio::Phylo::TreeBASE::Result::Taxonauthority;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Taxonauthority

=cut

__PACKAGE__->table("taxonauthority");

=head1 ACCESSORS

=head2 taxonauthority_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'taxonauthority_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 connectionstr

  data_type: 'varchar'
  is_nullable: 1
  size: 1000

=head2 description

  data_type: 'varchar'
  is_nullable: 1
  size: 2000

=head2 name

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=cut

__PACKAGE__->add_columns(
  "taxonauthority_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "taxonauthority_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "connectionstr",
  { data_type => "varchar", is_nullable => 1, size => 1000 },
  "description",
  { data_type => "varchar", is_nullable => 1, size => 2000 },
  "name",
  { data_type => "varchar", is_nullable => 1, size => 255 },
);
__PACKAGE__->set_primary_key("taxonauthority_id");

=head1 RELATIONS

=head2 taxonlinks

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlink>

=cut

__PACKAGE__->has_many(
  "taxonlinks",
  "Bio::Phylo::TreeBASE::Result::Taxonlink",
  { "foreign.taxonauthority_id" => "self.taxonauthority_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:vVNM6hzUcUeJDl9CeIT21w


# You can replace this text with custom content, and it will be preserved on regeneration
1;
