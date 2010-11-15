package Bio::Phylo::TreeBASE::Result::Taxonlink;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Taxonlink

=cut

__PACKAGE__->table("taxonlink");

=head1 ACCESSORS

=head2 linktype

  data_type: 'char'
  is_nullable: 0
  size: 1

=head2 taxonlink_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'taxonlink_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 foreigntaxonid

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 taxonauthority_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 taxon_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "linktype",
  { data_type => "char", is_nullable => 0, size => 1 },
  "taxonlink_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "taxonlink_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "foreigntaxonid",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "taxonauthority_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "taxon_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("taxonlink_id");

=head1 RELATIONS

=head2 taxon

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Taxon>

=cut

__PACKAGE__->belongs_to(
  "taxon",
  "Bio::Phylo::TreeBASE::Result::Taxon",
  { taxon_id => "taxon_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 taxonauthority

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonauthority>

=cut

__PACKAGE__->belongs_to(
  "taxonauthority",
  "Bio::Phylo::TreeBASE::Result::Taxonauthority",
  { taxonauthority_id => "taxonauthority_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:R2jBcSGtvGDHCR6C24KU5Q


# You can replace this text with custom content, and it will be preserved on regeneration
1;
