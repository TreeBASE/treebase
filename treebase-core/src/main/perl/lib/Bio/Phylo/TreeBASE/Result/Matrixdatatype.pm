package Bio::Phylo::TreeBASE::Result::Matrixdatatype;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Matrixdatatype

=cut

__PACKAGE__->table("matrixdatatype");

=head1 ACCESSORS

=head2 matrixdatatype_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'matrixdatatype_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 description

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 phylochar_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "matrixdatatype_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "matrixdatatype_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "description",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "phylochar_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("matrixdatatype_id");

=head1 RELATIONS

=head2 matrixes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Matrix>

=cut

__PACKAGE__->has_many(
  "matrixes",
  "Bio::Phylo::TreeBASE::Result::Matrix",
  { "foreign.matrixdatatype_id" => "self.matrixdatatype_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 phylochar

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Phylochar>

=cut

__PACKAGE__->belongs_to(
  "phylochar",
  "Bio::Phylo::TreeBASE::Result::Phylochar",
  { phylochar_id => "phylochar_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:xjZQAZ/Upvcm7T5zPrVSAw


# You can replace this text with custom content, and it will be preserved on regeneration
use Bio::Phylo::Matrices::Datatype;
use Bio::Phylo::Util::Exceptions 'throw';
push @Bio::Phylo::TreeBASE::Result::Matrixdatatype::ISA, 'Bio::Phylo::Matrices::Datatype';

my %typemap = (
	'DNA'      => 'Dna',
	'Standard' => 'Standard',
	'Protein'  => 'Protein',
);

sub get_type {
	my $self = shift;
	my $description = $self->description;
	my $type = $typemap{ $description };
	if ( $type ) {
		return $type;
	}
	else {
		throw 'API' => "No mapping for TreeBASE type $description";
	}
}

sub get_id {
	my $self = shift;
	my $id = $self->matrixdatatype_id;
	my $type = $self->version;
	return "${id}.${type}";
}

1;
