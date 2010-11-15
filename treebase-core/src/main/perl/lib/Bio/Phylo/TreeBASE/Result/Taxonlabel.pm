package Bio::Phylo::TreeBASE::Result::Taxonlabel;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Taxonlabel

=cut

__PACKAGE__->table("taxonlabel");

=head1 ACCESSORS

=head2 taxonlabel_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'taxonlabel_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 linked

  data_type: 'boolean'
  is_nullable: 1

=head2 taxonlabel

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 study_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 taxonvariant_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 tb1legacyid

  data_type: 'varchar'
  is_nullable: 1
  size: 20

=cut

__PACKAGE__->add_columns(
  "taxonlabel_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "taxonlabel_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "linked",
  { data_type => "boolean", is_nullable => 1 },
  "taxonlabel",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "study_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "taxonvariant_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "tb1legacyid",
  { data_type => "varchar", is_nullable => 1, size => 20 },
);
__PACKAGE__->set_primary_key("taxonlabel_id");

=head1 RELATIONS

=head2 distancematrixelement_rowlabels

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Distancematrixelement>

=cut

__PACKAGE__->has_many(
  "distancematrixelement_rowlabels",
  "Bio::Phylo::TreeBASE::Result::Distancematrixelement",
  { "foreign.rowlabel_id" => "self.taxonlabel_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 distancematrixelement_columnlabels

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Distancematrixelement>

=cut

__PACKAGE__->has_many(
  "distancematrixelement_columnlabels",
  "Bio::Phylo::TreeBASE::Result::Distancematrixelement",
  { "foreign.columnlabel_id" => "self.taxonlabel_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 matrixrows

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixrow>

=cut

__PACKAGE__->has_many(
  "matrixrows",
  "Bio::Phylo::TreeBASE::Result::Matrixrow",
  { "foreign.taxonlabel_id" => "self.taxonlabel_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 phylotreenodes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotreenode>

=cut

__PACKAGE__->has_many(
  "phylotreenodes",
  "Bio::Phylo::TreeBASE::Result::Phylotreenode",
  { "foreign.taxonlabel_id" => "self.taxonlabel_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 rowsegments

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Rowsegment>

=cut

__PACKAGE__->has_many(
  "rowsegments",
  "Bio::Phylo::TreeBASE::Result::Rowsegment",
  { "foreign.taxonlabel_id" => "self.taxonlabel_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 sub_taxonlabel

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::SubTaxonlabel>

=cut

__PACKAGE__->might_have(
  "sub_taxonlabel",
  "Bio::Phylo::TreeBASE::Result::SubTaxonlabel",
  { "foreign.taxonlabel_id" => "self.taxonlabel_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 taxonvariant

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonvariant>

=cut

__PACKAGE__->belongs_to(
  "taxonvariant",
  "Bio::Phylo::TreeBASE::Result::Taxonvariant",
  { taxonvariant_id => "taxonvariant_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 study

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Study>

=cut

__PACKAGE__->belongs_to(
  "study",
  "Bio::Phylo::TreeBASE::Result::Study",
  { study_id => "study_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 taxonlabelgroup_taxonlabels

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::TaxonlabelgroupTaxonlabel>

=cut

__PACKAGE__->has_many(
  "taxonlabelgroup_taxonlabels",
  "Bio::Phylo::TreeBASE::Result::TaxonlabelgroupTaxonlabel",
  { "foreign.taxonlabel_id" => "self.taxonlabel_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 taxonlabelset_taxonlabels

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::TaxonlabelsetTaxonlabel>

=cut

__PACKAGE__->has_many(
  "taxonlabelset_taxonlabels",
  "Bio::Phylo::TreeBASE::Result::TaxonlabelsetTaxonlabel",
  { "foreign.taxonlabel_id" => "self.taxonlabel_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:jFc3Tp0mQI3FJxmzdOAW7w


# You can replace this text with custom content, and it will be preserved on regeneration
use Bio::Phylo::Taxa::Taxon;
push @Bio::Phylo::TreeBASE::Result::Taxonlabel::ISA, 'Bio::Phylo::Taxa::Taxon';

sub get_name {
	shift->taxonlabel;
}

sub get_id {
	my $self = shift;
	my $id = $self->taxonlabel_id;
	my $version = $self->version;
	return "${id}.${version}";
}

1;
