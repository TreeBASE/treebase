package Bio::Phylo::TreeBASE::Result::Citation;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Citation

=cut

__PACKAGE__->table("citation");

=head1 ACCESSORS

=head2 type

  data_type: 'char'
  is_nullable: 0
  size: 1

=head2 citation_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'citation_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 pmid

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 url

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 abstract

  data_type: 'varchar'
  is_nullable: 1
  size: 10000

=head2 doi

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 keywords

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 pages

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 publishyear

  data_type: 'integer'
  is_nullable: 1

=head2 published

  data_type: 'boolean'
  is_nullable: 1

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 500

=head2 issue

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 journal

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 volume

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 isbn

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 booktitle

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 city

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 publisher

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 citationstatus_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "type",
  { data_type => "char", is_nullable => 0, size => 1 },
  "citation_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "citation_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "pmid",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "url",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "abstract",
  { data_type => "varchar", is_nullable => 1, size => 10000 },
  "doi",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "keywords",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "pages",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "publishyear",
  { data_type => "integer", is_nullable => 1 },
  "published",
  { data_type => "boolean", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 500 },
  "issue",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "journal",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "volume",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "isbn",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "booktitle",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "city",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "publisher",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "citationstatus_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("citation_id");

=head1 RELATIONS

=head2 citationstatus

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Citationstatus>

=cut

__PACKAGE__->belongs_to(
  "citationstatus",
  "Bio::Phylo::TreeBASE::Result::Citationstatus",
  { citationstatus_id => "citationstatus_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 citation_authors

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::CitationAuthor>

=cut

__PACKAGE__->has_many(
  "citation_authors",
  "Bio::Phylo::TreeBASE::Result::CitationAuthor",
  { "foreign.citation_id" => "self.citation_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 citation_editors

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::CitationEditor>

=cut

__PACKAGE__->has_many(
  "citation_editors",
  "Bio::Phylo::TreeBASE::Result::CitationEditor",
  { "foreign.citation_id" => "self.citation_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 studies

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Study>

=cut

__PACKAGE__->has_many(
  "studies",
  "Bio::Phylo::TreeBASE::Result::Study",
  { "foreign.citation_id" => "self.citation_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:qDcoG/xxDv4e0sgMznZgWw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
