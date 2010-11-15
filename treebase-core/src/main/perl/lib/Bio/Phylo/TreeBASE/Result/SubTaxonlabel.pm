package Bio::Phylo::TreeBASE::Result::SubTaxonlabel;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::SubTaxonlabel

=cut

__PACKAGE__->table("sub_taxonlabel");

=head1 ACCESSORS

=head2 submission_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 taxonlabel_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "submission_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "taxonlabel_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("submission_id", "taxonlabel_id");
__PACKAGE__->add_unique_constraint("sub_taxonlabel_taxonlabel_id_key", ["taxonlabel_id"]);

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

=head2 submission

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Submission>

=cut

__PACKAGE__->belongs_to(
  "submission",
  "Bio::Phylo::TreeBASE::Result::Submission",
  { submission_id => "submission_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:oWms2l5JbA2U2rMv1fLseg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
