package Bio::Phylo::TreeBASE::Result::Submission;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Submission

=cut

__PACKAGE__->table("submission");

=head1 ACCESSORS

=head2 submission_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'submission_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 createdate

  data_type: 'date'
  is_nullable: 1

=head2 submissionnumber

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 test

  data_type: 'integer'
  is_nullable: 1

=head2 study_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 user_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "submission_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "submission_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "createdate",
  { data_type => "date", is_nullable => 1 },
  "submissionnumber",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "test",
  { data_type => "integer", is_nullable => 1 },
  "study_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "user_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("submission_id");
__PACKAGE__->add_unique_constraint("submission_study_id_key", ["study_id"]);

=head1 RELATIONS

=head2 sub_matrixes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::SubMatrix>

=cut

__PACKAGE__->has_many(
  "sub_matrixes",
  "Bio::Phylo::TreeBASE::Result::SubMatrix",
  { "foreign.submission_id" => "self.submission_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 user

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::User>

=cut

__PACKAGE__->belongs_to(
  "user",
  "Bio::Phylo::TreeBASE::Result::User",
  { user_id => "user_id" },
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
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 sub_taxonlabels

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::SubTaxonlabel>

=cut

__PACKAGE__->has_many(
  "sub_taxonlabels",
  "Bio::Phylo::TreeBASE::Result::SubTaxonlabel",
  { "foreign.submission_id" => "self.submission_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 sub_treeblocks

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::SubTreeblock>

=cut

__PACKAGE__->has_many(
  "sub_treeblocks",
  "Bio::Phylo::TreeBASE::Result::SubTreeblock",
  { "foreign.submission_id" => "self.submission_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:3B1YNfBLTUXko5GsHHDGGw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
