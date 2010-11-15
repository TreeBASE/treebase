package Bio::Phylo::TreeBASE::Result::Treeblock;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Treeblock

=cut

__PACKAGE__->table("treeblock");

=head1 ACCESSORS

=head2 treeblock_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'treeblock_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 taxonlabelset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "treeblock_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "treeblock_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "taxonlabelset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("treeblock_id");

=head1 RELATIONS

=head2 phylotrees

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotree>

=cut

__PACKAGE__->has_many(
  "phylotrees",
  "Bio::Phylo::TreeBASE::Result::Phylotree",
  { "foreign.treeblock_id" => "self.treeblock_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 sub_treeblock

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::SubTreeblock>

=cut

__PACKAGE__->might_have(
  "sub_treeblock",
  "Bio::Phylo::TreeBASE::Result::SubTreeblock",
  { "foreign.treeblock_id" => "self.treeblock_id" },
  { cascade_copy => 0, cascade_delete => 0 },
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


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:/o3dEPN6dtARVT+qqyZQPQ


# You can replace this text with custom content, and it will be preserved on regeneration
use Bio::Phylo::Forest;
push @Bio::Phylo::TreeBASE::Result::Treeblock::ISA, 'Bio::Phylo::Forest';

sub get_id {
	my $self = shift;
	my $id = $self->treeblock_id;
	my $version = $self->version;
	return "${id}.${version}";
}

sub get_taxa {
	shift->taxonlabelset;
}

sub get_entities {
	my $self = shift;
	if ( my $tls = $self->taxonlabelset ) {
		if ( my $study = $tls->study ) {
			my @trees = $study->phylotrees;
			return \@trees;
		}
	}
	return [];
}

1;
