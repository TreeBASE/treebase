package Bio::Phylo::TreeBASE::Result::Phylochar;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Phylochar

=cut

__PACKAGE__->table("phylochar");

=head1 ACCESSORS

=head2 type

  data_type: 'char'
  is_nullable: 0
  size: 1

=head2 phylochar_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'phylochar_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 description

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 lowerlimit

  data_type: 'double precision'
  is_nullable: 1

=head2 upperlimit

  data_type: 'double precision'
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "type",
  { data_type => "char", is_nullable => 0, size => 1 },
  "phylochar_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "phylochar_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "description",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "lowerlimit",
  { data_type => "double precision", is_nullable => 1 },
  "upperlimit",
  { data_type => "double precision", is_nullable => 1 },
);
__PACKAGE__->set_primary_key("phylochar_id");

=head1 RELATIONS

=head2 discretecharstates

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Discretecharstate>

=cut

__PACKAGE__->has_many(
  "discretecharstates",
  "Bio::Phylo::TreeBASE::Result::Discretecharstate",
  { "foreign.phylochar_id" => "self.phylochar_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 matrixcolumns

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixcolumn>

=cut

__PACKAGE__->has_many(
  "matrixcolumns",
  "Bio::Phylo::TreeBASE::Result::Matrixcolumn",
  { "foreign.phylochar_id" => "self.phylochar_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 matrixdatatypes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixdatatype>

=cut

__PACKAGE__->has_many(
  "matrixdatatypes",
  "Bio::Phylo::TreeBASE::Result::Matrixdatatype",
  { "foreign.phylochar_id" => "self.phylochar_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:lNWV9mGsUM1HRk6bVtlyiQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
