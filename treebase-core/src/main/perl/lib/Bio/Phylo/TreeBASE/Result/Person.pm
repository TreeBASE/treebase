package Bio::Phylo::TreeBASE::Result::Person;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Person

=cut

__PACKAGE__->table("person");

=head1 ACCESSORS

=head2 person_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'person_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 email

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 firstname

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 lastname

  data_type: 'varchar'
  is_nullable: 0
  size: 255

=head2 mname

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 phone

  data_type: 'varchar'
  is_nullable: 1
  size: 50

=cut

__PACKAGE__->add_columns(
  "person_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "person_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "email",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "firstname",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "lastname",
  { data_type => "varchar", is_nullable => 0, size => 255 },
  "mname",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "phone",
  { data_type => "varchar", is_nullable => 1, size => 50 },
);
__PACKAGE__->set_primary_key("person_id");

=head1 RELATIONS

=head2 citation_authors

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::CitationAuthor>

=cut

__PACKAGE__->has_many(
  "citation_authors",
  "Bio::Phylo::TreeBASE::Result::CitationAuthor",
  { "foreign.authors_person_id" => "self.person_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 citation_editors

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::CitationEditor>

=cut

__PACKAGE__->has_many(
  "citation_editors",
  "Bio::Phylo::TreeBASE::Result::CitationEditor",
  { "foreign.editors_person_id" => "self.person_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 users

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::User>

=cut

__PACKAGE__->has_many(
  "users",
  "Bio::Phylo::TreeBASE::Result::User",
  { "foreign.person_id" => "self.person_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:XC+AVY2yFwmjsmkGpwmLGA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
