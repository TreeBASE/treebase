package Bio::Phylo::TreeBASE::Result::Codonpositionset;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Codonpositionset

=cut

__PACKAGE__->table("codonpositionset");

=head1 ACCESSORS

=head2 codonpositionset_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'codonpositionset_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 matrix_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "codonpositionset_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "codonpositionset_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "matrix_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("codonpositionset_id");

=head1 RELATIONS

=head2 codonchar1_colranges

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Codonchar1Colrange>

=cut

__PACKAGE__->has_many(
  "codonchar1_colranges",
  "Bio::Phylo::TreeBASE::Result::Codonchar1Colrange",
  { "foreign.codonpositionset_id" => "self.codonpositionset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 codonchar2_colranges

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Codonchar2Colrange>

=cut

__PACKAGE__->has_many(
  "codonchar2_colranges",
  "Bio::Phylo::TreeBASE::Result::Codonchar2Colrange",
  { "foreign.codonpositionset_id" => "self.codonpositionset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 codonchar3_colranges

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Codonchar3Colrange>

=cut

__PACKAGE__->has_many(
  "codonchar3_colranges",
  "Bio::Phylo::TreeBASE::Result::Codonchar3Colrange",
  { "foreign.codonpositionset_id" => "self.codonpositionset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 codonnoncoding_colranges

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::CodonnoncodingColrange>

=cut

__PACKAGE__->has_many(
  "codonnoncoding_colranges",
  "Bio::Phylo::TreeBASE::Result::CodonnoncodingColrange",
  { "foreign.codonpositionset_id" => "self.codonpositionset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 matrix

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Matrix>

=cut

__PACKAGE__->belongs_to(
  "matrix",
  "Bio::Phylo::TreeBASE::Result::Matrix",
  { matrix_id => "matrix_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 matrixes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Matrix>

=cut

__PACKAGE__->has_many(
  "matrixes",
  "Bio::Phylo::TreeBASE::Result::Matrix",
  { "foreign.codonpositionset_id" => "self.codonpositionset_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:aSf28yMvFraz/WeYvJ5+0Q


# You can replace this text with custom content, and it will be preserved on regeneration
1;
