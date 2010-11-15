package Bio::Phylo::TreeBASE::Result::Taxonvariant;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Taxonvariant

=cut

__PACKAGE__->table("taxonvariant");

=head1 ACCESSORS

=head2 taxonvariant_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'taxonvariant_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 tb1legacyid

  data_type: 'integer'
  is_nullable: 1

=head2 fullname

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 lexicalqualifier

  data_type: 'varchar'
  is_nullable: 1
  size: 50

=head2 name

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 namebankid

  data_type: 'bigint'
  is_nullable: 1

=head2 taxon_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "taxonvariant_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "taxonvariant_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "tb1legacyid",
  { data_type => "integer", is_nullable => 1 },
  "fullname",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "lexicalqualifier",
  { data_type => "varchar", is_nullable => 1, size => 50 },
  "name",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "namebankid",
  { data_type => "bigint", is_nullable => 1 },
  "taxon_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("taxonvariant_id");

=head1 RELATIONS

=head2 taxonlabels

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlabel>

=cut

__PACKAGE__->has_many(
  "taxonlabels",
  "Bio::Phylo::TreeBASE::Result::Taxonlabel",
  { "foreign.taxonvariant_id" => "self.taxonvariant_id" },
  { cascade_copy => 0, cascade_delete => 0 },
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
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:5wqb5r79DUJIo7uWR5ESgg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
