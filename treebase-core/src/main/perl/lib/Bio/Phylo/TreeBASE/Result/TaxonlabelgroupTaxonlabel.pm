package Bio::Phylo::TreeBASE::Result::TaxonlabelgroupTaxonlabel;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::TaxonlabelgroupTaxonlabel

=cut

__PACKAGE__->table("taxonlabelgroup_taxonlabel");

=head1 ACCESSORS

=head2 taxonlabelgroup_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 taxonlabel_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "taxonlabelgroup_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "taxonlabel_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);

=head1 RELATIONS

=head2 taxonlabel

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlabel>

=cut

__PACKAGE__->belongs_to(
  "taxonlabel",
  "Bio::Phylo::TreeBASE::Result::Taxonlabel",
  { taxonlabel_id => "taxonlabel_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 taxonlabelgroup

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlabelgroup>

=cut

__PACKAGE__->belongs_to(
  "taxonlabelgroup",
  "Bio::Phylo::TreeBASE::Result::Taxonlabelgroup",
  { taxonlabelgroup_id => "taxonlabelgroup_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:U+4x2GDsHg+vP7rKgJVMlw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
