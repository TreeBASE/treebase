package Bio::Phylo::TreeBASE::Result::Analysis;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Analysis

=cut

__PACKAGE__->table("analysis");

=head1 ACCESSORS

=head2 analysis_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'analysis_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 name

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 notes

  data_type: 'varchar'
  is_nullable: 1
  size: 2000

=head2 validated

  data_type: 'boolean'
  is_nullable: 1

=head2 study_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 analysis_order

  data_type: 'integer'
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "analysis_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "analysis_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "name",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "notes",
  { data_type => "varchar", is_nullable => 1, size => 2000 },
  "validated",
  { data_type => "boolean", is_nullable => 1 },
  "study_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "analysis_order",
  { data_type => "integer", is_nullable => 1 },
);
__PACKAGE__->set_primary_key("analysis_id");

=head1 RELATIONS

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

=head2 analysissteps

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Analysisstep>

=cut

__PACKAGE__->has_many(
  "analysissteps",
  "Bio::Phylo::TreeBASE::Result::Analysisstep",
  { "foreign.analysis_id" => "self.analysis_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:tZ7CJ1RYuZPag7ntKKRKuA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
