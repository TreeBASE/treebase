package Bio::Phylo::TreeBASE::Result::Taxonlabelset;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Taxonlabelset

=cut

__PACKAGE__->table("taxonlabelset");

=head1 ACCESSORS

=head2 taxonlabelset_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'taxonlabelset_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 taxa

  data_type: 'boolean'
  is_nullable: 1

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 study_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "taxonlabelset_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "taxonlabelset_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "taxa",
  { data_type => "boolean", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "study_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("taxonlabelset_id");

=head1 RELATIONS

=head2 matrixes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Matrix>

=cut

__PACKAGE__->has_many(
  "matrixes",
  "Bio::Phylo::TreeBASE::Result::Matrix",
  { "foreign.taxonlabelset_id" => "self.taxonlabelset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
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

=head2 taxonlabelset_taxonlabels

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::TaxonlabelsetTaxonlabel>

=cut

__PACKAGE__->has_many(
  "taxonlabelset_taxonlabels",
  "Bio::Phylo::TreeBASE::Result::TaxonlabelsetTaxonlabel",
  { "foreign.taxonlabelset_id" => "self.taxonlabelset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 treeblocks

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Treeblock>

=cut

__PACKAGE__->has_many(
  "treeblocks",
  "Bio::Phylo::TreeBASE::Result::Treeblock",
  { "foreign.taxonlabelset_id" => "self.taxonlabelset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:Z2EPk0kYDtyAD8bTaZKu+Q


# You can replace this text with custom content, and it will be preserved on regeneration
use Bio::Phylo::Taxa;
push @Bio::Phylo::TreeBASE::Result::Taxonlabelset::ISA, 'Bio::Phylo::Taxa';

sub get_entities {
	my $self = shift;
	my @labels;
	for my $taxonlabelset_taxonlabels ( $self->taxonlabelset_taxonlabels ) {
		my $taxonlabel = $taxonlabelset_taxonlabels->taxonlabel;
		push @labels, $taxonlabel;
	}
	my @sorted = map { $_->[0] } sort { $a->[1] cmp $b->[1] } map { [ $_, $_->get_name ] } @labels;
	return \@sorted;
}

sub get_id {
	my $self = shift;
	my $id = $self->taxonlabelset_id;
	my $version = $self->version;
	return "${id}.${version}";
}

1;
