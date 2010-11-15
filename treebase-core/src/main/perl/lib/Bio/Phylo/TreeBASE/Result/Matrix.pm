package Bio::Phylo::TreeBASE::Result::Matrix;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Matrix

=cut

__PACKAGE__->table("matrix");

=head1 ACCESSORS

=head2 matrixtype

  data_type: 'char'
  is_nullable: 0
  size: 1

=head2 matrix_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'matrix_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 tb_matrixid

  data_type: 'varchar'
  is_nullable: 1
  size: 30

=head2 description

  data_type: 'varchar'
  is_nullable: 1
  size: 2000

=head2 gapsymbol

  data_type: 'char'
  is_nullable: 1
  size: 1

=head2 missingsymbol

  data_type: 'char'
  is_nullable: 1
  size: 1

=head2 nexusfilename

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 published

  data_type: 'boolean'
  is_nullable: 1

=head2 symbols

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 nchar

  data_type: 'integer'
  is_nullable: 1

=head2 ntax

  data_type: 'integer'
  is_nullable: 1

=head2 aligned

  data_type: 'boolean'
  is_nullable: 1

=head2 diagonal

  data_type: 'boolean'
  is_nullable: 1

=head2 triangle

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 casesensitive

  data_type: 'boolean'
  is_nullable: 1

=head2 matrixdatatype_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 matrixkind_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 study_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 taxonlabelset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 ancstateset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 codonpositionset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 charset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 typeset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 charweightset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "matrixtype",
  { data_type => "char", is_nullable => 0, size => 1 },
  "matrix_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "matrix_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "tb_matrixid",
  { data_type => "varchar", is_nullable => 1, size => 30 },
  "description",
  { data_type => "varchar", is_nullable => 1, size => 2000 },
  "gapsymbol",
  { data_type => "char", is_nullable => 1, size => 1 },
  "missingsymbol",
  { data_type => "char", is_nullable => 1, size => 1 },
  "nexusfilename",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "published",
  { data_type => "boolean", is_nullable => 1 },
  "symbols",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "nchar",
  { data_type => "integer", is_nullable => 1 },
  "ntax",
  { data_type => "integer", is_nullable => 1 },
  "aligned",
  { data_type => "boolean", is_nullable => 1 },
  "diagonal",
  { data_type => "boolean", is_nullable => 1 },
  "triangle",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "casesensitive",
  { data_type => "boolean", is_nullable => 1 },
  "matrixdatatype_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "matrixkind_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "study_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "taxonlabelset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "ancstateset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "codonpositionset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "charset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "typeset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "charweightset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("matrix_id");

=head1 RELATIONS

=head2 analyzeddatas

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Analyzeddata>

=cut

__PACKAGE__->has_many(
  "analyzeddatas",
  "Bio::Phylo::TreeBASE::Result::Analyzeddata",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 ancstatesets

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Ancstateset>

=cut

__PACKAGE__->has_many(
  "ancstatesets",
  "Bio::Phylo::TreeBASE::Result::Ancstateset",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 charpartitions

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Charpartition>

=cut

__PACKAGE__->has_many(
  "charpartitions",
  "Bio::Phylo::TreeBASE::Result::Charpartition",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 charsets

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Charset>

=cut

__PACKAGE__->has_many(
  "charsets",
  "Bio::Phylo::TreeBASE::Result::Charset",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 charweightsets

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Charweightset>

=cut

__PACKAGE__->has_many(
  "charweightsets",
  "Bio::Phylo::TreeBASE::Result::Charweightset",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 codonpositionsets

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Codonpositionset>

=cut

__PACKAGE__->has_many(
  "codonpositionsets",
  "Bio::Phylo::TreeBASE::Result::Codonpositionset",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 distancematrixelements

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Distancematrixelement>

=cut

__PACKAGE__->has_many(
  "distancematrixelements",
  "Bio::Phylo::TreeBASE::Result::Distancematrixelement",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 geneticcodesets

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Geneticcodeset>

=cut

__PACKAGE__->has_many(
  "geneticcodesets",
  "Bio::Phylo::TreeBASE::Result::Geneticcodeset",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 codonpositionset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Codonpositionset>

=cut

__PACKAGE__->belongs_to(
  "codonpositionset",
  "Bio::Phylo::TreeBASE::Result::Codonpositionset",
  { codonpositionset_id => "codonpositionset_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 charweightset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Charweightset>

=cut

__PACKAGE__->belongs_to(
  "charweightset",
  "Bio::Phylo::TreeBASE::Result::Charweightset",
  { charweightset_id => "charweightset_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 matrixkind

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixkind>

=cut

__PACKAGE__->belongs_to(
  "matrixkind",
  "Bio::Phylo::TreeBASE::Result::Matrixkind",
  { matrixkind_id => "matrixkind_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 charset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Charset>

=cut

__PACKAGE__->belongs_to(
  "charset",
  "Bio::Phylo::TreeBASE::Result::Charset",
  { charset_id => "charset_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 matrixdatatype

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixdatatype>

=cut

__PACKAGE__->belongs_to(
  "matrixdatatype",
  "Bio::Phylo::TreeBASE::Result::Matrixdatatype",
  { matrixdatatype_id => "matrixdatatype_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 typeset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Typeset>

=cut

__PACKAGE__->belongs_to(
  "typeset",
  "Bio::Phylo::TreeBASE::Result::Typeset",
  { typeset_id => "typeset_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 ancstateset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Ancstateset>

=cut

__PACKAGE__->belongs_to(
  "ancstateset",
  "Bio::Phylo::TreeBASE::Result::Ancstateset",
  { ancstateset_id => "ancstateset_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 taxonlabelset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlabelset>

=cut

__PACKAGE__->belongs_to(
  "taxonlabelset",
  "Bio::Phylo::TreeBASE::Result::Taxonlabelset",
  { taxonlabelset_id => "taxonlabelset_id" },
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
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 matrixcolumns

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixcolumn>

=cut

__PACKAGE__->has_many(
  "matrixcolumns",
  "Bio::Phylo::TreeBASE::Result::Matrixcolumn",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 matrix_itemdefinitions

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::MatrixItemdefinition>

=cut

__PACKAGE__->has_many(
  "matrix_itemdefinitions",
  "Bio::Phylo::TreeBASE::Result::MatrixItemdefinition",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 matrixrows

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixrow>

=cut

__PACKAGE__->has_many(
  "matrixrows",
  "Bio::Phylo::TreeBASE::Result::Matrixrow",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 statesets

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Stateset>

=cut

__PACKAGE__->has_many(
  "statesets",
  "Bio::Phylo::TreeBASE::Result::Stateset",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 sub_matrix

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::SubMatrix>

=cut

__PACKAGE__->might_have(
  "sub_matrix",
  "Bio::Phylo::TreeBASE::Result::SubMatrix",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 typesets

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Typeset>

=cut

__PACKAGE__->has_many(
  "typesets",
  "Bio::Phylo::TreeBASE::Result::Typeset",
  { "foreign.matrix_id" => "self.matrix_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:rsqCvOAdNVUdpoHMGz7z0w


# You can replace this text with custom content, and it will be preserved on regeneration
use Bio::Phylo::Matrices::Matrix;
push @Bio::Phylo::TreeBASE::Result::Matrix::ISA, 'Bio::Phylo::Matrices::Matrix';

sub get_id {
	shift->matrix_id;
}

sub get_taxa {
	shift->taxonlabelset;
}

sub get_type_object {
	shift->matrixdatatype;
}

sub get_nchar { shift->nchar }

sub get_ntax { shift->ntax }

sub get_entities {
	my $self = shift;
	my @rows = $self->matrixrows;
	my @sorted = map { $_->[0] } sort { $a->[1] cmp $b->[1] } map { [ $_, $_->get_name ] } @rows;
	return \@sorted;
}

sub to_nexus {
	my $self = shift;
	my $template = "BEGIN CHARACTERS;\n[! Matrix URI http://purl.org/phylo/treebase/phylows/matrix/TB2:M%s ]\n";
	my $url = sprintf($template, $self->matrix_id);
	my $nexus = $self->SUPER::to_nexus(@_);
	$nexus =~ s/^BEGIN CHARACTERS;/$url/;
	return $nexus;
}

1;
