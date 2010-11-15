package Bio::Phylo::TreeBASE::Result::Analysisstep;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Analysisstep

=cut

__PACKAGE__->table("analysisstep");

=head1 ACCESSORS

=head2 analysisstep_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'analysisstep_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 commands

  data_type: 'varchar'
  is_nullable: 1
  size: 2000

=head2 name

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 notes

  data_type: 'varchar'
  is_nullable: 1
  size: 2000

=head2 algorithm_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 analysis_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 software_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 step_order

  data_type: 'integer'
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "analysisstep_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "analysisstep_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "commands",
  { data_type => "varchar", is_nullable => 1, size => 2000 },
  "name",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "notes",
  { data_type => "varchar", is_nullable => 1, size => 2000 },
  "algorithm_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "analysis_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "software_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "step_order",
  { data_type => "integer", is_nullable => 1 },
);
__PACKAGE__->set_primary_key("analysisstep_id");

=head1 RELATIONS

=head2 algorithm

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Algorithm>

=cut

__PACKAGE__->belongs_to(
  "algorithm",
  "Bio::Phylo::TreeBASE::Result::Algorithm",
  { algorithm_id => "algorithm_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 analysis

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Analysis>

=cut

__PACKAGE__->belongs_to(
  "analysis",
  "Bio::Phylo::TreeBASE::Result::Analysis",
  { analysis_id => "analysis_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 software

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Software>

=cut

__PACKAGE__->belongs_to(
  "software",
  "Bio::Phylo::TreeBASE::Result::Software",
  { software_id => "software_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 analyzeddatas

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Analyzeddata>

=cut

__PACKAGE__->has_many(
  "analyzeddatas",
  "Bio::Phylo::TreeBASE::Result::Analyzeddata",
  { "foreign.analysisstep_id" => "self.analysisstep_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:5AheD5cN0TdR+3kkdXGNzA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
