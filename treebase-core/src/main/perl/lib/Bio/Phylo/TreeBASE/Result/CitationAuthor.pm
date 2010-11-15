package Bio::Phylo::TreeBASE::Result::CitationAuthor;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::CitationAuthor

=cut

__PACKAGE__->table("citation_author");

=head1 ACCESSORS

=head2 citation_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 authors_person_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 author_order

  data_type: 'integer'
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "citation_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "authors_person_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "author_order",
  { data_type => "integer", is_nullable => 0 },
);
__PACKAGE__->set_primary_key("citation_id", "author_order");

=head1 RELATIONS

=head2 citation

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Citation>

=cut

__PACKAGE__->belongs_to(
  "citation",
  "Bio::Phylo::TreeBASE::Result::Citation",
  { citation_id => "citation_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 authors_person

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Person>

=cut

__PACKAGE__->belongs_to(
  "authors_person",
  "Bio::Phylo::TreeBASE::Result::Person",
  { person_id => "authors_person_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:HFPvlcMUJ/i+CXW41Pqwrg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
