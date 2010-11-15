package Bio::Phylo::TreeBASE::Result::Taxon;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Taxon

=cut

__PACKAGE__->table("taxon");

=head1 ACCESSORS

=head2 taxon_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'taxon_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 tb1legacyid

  data_type: 'integer'
  is_nullable: 1

=head2 ubionamebankid

  data_type: 'bigint'
  is_nullable: 1

=head2 description

  data_type: 'varchar'
  is_nullable: 1
  size: 2000

=head2 groupcode

  data_type: 'integer'
  is_nullable: 1

=head2 name

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 ncbitaxid

  data_type: 'integer'
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "taxon_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "taxon_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "tb1legacyid",
  { data_type => "integer", is_nullable => 1 },
  "ubionamebankid",
  { data_type => "bigint", is_nullable => 1 },
  "description",
  { data_type => "varchar", is_nullable => 1, size => 2000 },
  "groupcode",
  { data_type => "integer", is_nullable => 1 },
  "name",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "ncbitaxid",
  { data_type => "integer", is_nullable => 1 },
);
__PACKAGE__->set_primary_key("taxon_id");

=head1 RELATIONS

=head2 taxonlinks

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlink>

=cut

__PACKAGE__->has_many(
  "taxonlinks",
  "Bio::Phylo::TreeBASE::Result::Taxonlink",
  { "foreign.taxon_id" => "self.taxon_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 taxonset_taxons

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::TaxonsetTaxon>

=cut

__PACKAGE__->has_many(
  "taxonset_taxons",
  "Bio::Phylo::TreeBASE::Result::TaxonsetTaxon",
  { "foreign.taxon_id" => "self.taxon_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 taxonvariants

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonvariant>

=cut

__PACKAGE__->has_many(
  "taxonvariants",
  "Bio::Phylo::TreeBASE::Result::Taxonvariant",
  { "foreign.taxon_id" => "self.taxon_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:Q+sevhayHPZmRLLr2CUIdw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
