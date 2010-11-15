package Bio::Phylo::TreeBASE::Result::Matrixrow;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Matrixrow

=cut

__PACKAGE__->table("matrixrow");

=head1 ACCESSORS

=head2 matrixrow_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'matrixrow_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 symbolstring

  data_type: 'text'
  is_nullable: 1

=head2 matrix_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 taxonlabel_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 row_order

  data_type: 'integer'
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "matrixrow_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "matrixrow_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "symbolstring",
  { data_type => "text", is_nullable => 1 },
  "matrix_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "taxonlabel_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "row_order",
  { data_type => "integer", is_nullable => 1 },
);
__PACKAGE__->set_primary_key("matrixrow_id");

=head1 RELATIONS

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

=head2 taxonlabel

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlabel>

=cut

__PACKAGE__->belongs_to(
  "taxonlabel",
  "Bio::Phylo::TreeBASE::Result::Taxonlabel",
  { taxonlabel_id => "taxonlabel_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 rowsegments

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Rowsegment>

=cut

__PACKAGE__->has_many(
  "rowsegments",
  "Bio::Phylo::TreeBASE::Result::Rowsegment",
  { "foreign.matrixrow_id" => "self.matrixrow_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:ZLVQzC4FiPUPhN7kq+d7VQ


# You can replace this text with custom content, and it will be preserved on regeneration
use Bio::Phylo::Matrices::Datum;
use Bio::Phylo::Util::Exceptions 'throw';

push @Bio::Phylo::TreeBASE::Result::Matrixrow::ISA, 'Bio::Phylo::Matrices::Datum';

my $logger = __PACKAGE__->get_logger;

sub get_taxon {
	shift->taxonlabel;
}

sub get_id {
	my $self = shift;
	my $id = $self->matrixrow_id;
	my $version = $self->version;
	return "${id}.${version}";
}

sub get_type_object {
	my $self = shift;
	my $matrix = $self->matrix;
	if ( $matrix ) {
		return $matrix->get_type_object;
	}
	else {
		throw 'API' => "Row doesn't belong to a matrix!";
	}
}

sub get_entities {
	my $self = shift;
	my $string = $self->symbolstring;
	return $self->get_type_object->split($string);
}

sub get_name {
	my $self = shift;
	if ( my $tl = $self->taxonlabel ) {
		return $tl->taxonlabel;
	}
}

1;
