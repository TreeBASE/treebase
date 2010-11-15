package Bio::Phylo::TreeBASE::Result::TaxonsetTaxon;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::TaxonsetTaxon

=cut

__PACKAGE__->table("taxonset_taxon");

=head1 ACCESSORS

=head2 taxonset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 taxon_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 taxon_order

  data_type: 'integer'
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "taxonset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "taxon_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "taxon_order",
  { data_type => "integer", is_nullable => 0 },
);
__PACKAGE__->set_primary_key("taxonset_id", "taxon_order");

=head1 RELATIONS

=head2 taxonset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonset>

=cut

__PACKAGE__->belongs_to(
  "taxonset",
  "Bio::Phylo::TreeBASE::Result::Taxonset",
  { taxonset_id => "taxonset_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

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


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:RxBJCixNK5XqEpgrihw0Ww


# You can replace this text with custom content, and it will be preserved on regeneration
1;
