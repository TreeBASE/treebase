package Bio::Phylo::TreeBASE::Result::Study;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Study

=cut

__PACKAGE__->table("study");

=head1 ACCESSORS

=head2 study_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'study_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 tb_studyid

  data_type: 'varchar'
  is_nullable: 1
  size: 30

=head2 accessionnumber

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 lastmodifieddate

  data_type: 'date'
  is_nullable: 1

=head2 name

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 notes

  data_type: 'varchar'
  is_nullable: 1
  size: 2000

=head2 releasedate

  data_type: 'date'
  is_nullable: 1

=head2 citation_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 user_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 studystatus_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "study_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "study_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "tb_studyid",
  { data_type => "varchar", is_nullable => 1, size => 30 },
  "accessionnumber",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "lastmodifieddate",
  { data_type => "date", is_nullable => 1 },
  "name",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "notes",
  { data_type => "varchar", is_nullable => 1, size => 2000 },
  "releasedate",
  { data_type => "date", is_nullable => 1 },
  "citation_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "user_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "studystatus_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("study_id");

=head1 RELATIONS

=head2 analyses

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Analysis>

=cut

__PACKAGE__->has_many(
  "analyses",
  "Bio::Phylo::TreeBASE::Result::Analysis",
  { "foreign.study_id" => "self.study_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 matrixes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Matrix>

=cut

__PACKAGE__->has_many(
  "matrixes",
  "Bio::Phylo::TreeBASE::Result::Matrix",
  { "foreign.study_id" => "self.study_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 phylotrees

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Phylotree>

=cut

__PACKAGE__->has_many(
  "phylotrees",
  "Bio::Phylo::TreeBASE::Result::Phylotree",
  { "foreign.study_id" => "self.study_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 citation

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Citation>

=cut

__PACKAGE__->belongs_to(
  "citation",
  "Bio::Phylo::TreeBASE::Result::Citation",
  { citation_id => "citation_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 studystatus

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Studystatus>

=cut

__PACKAGE__->belongs_to(
  "studystatus",
  "Bio::Phylo::TreeBASE::Result::Studystatus",
  { studystatus_id => "studystatus_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
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

=head2 study_nexusfiles

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::StudyNexusfile>

=cut

__PACKAGE__->has_many(
  "study_nexusfiles",
  "Bio::Phylo::TreeBASE::Result::StudyNexusfile",
  { "foreign.study_id" => "self.study_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 submission

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::Submission>

=cut

__PACKAGE__->might_have(
  "submission",
  "Bio::Phylo::TreeBASE::Result::Submission",
  { "foreign.study_id" => "self.study_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 taxonlabels

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlabel>

=cut

__PACKAGE__->has_many(
  "taxonlabels",
  "Bio::Phylo::TreeBASE::Result::Taxonlabel",
  { "foreign.study_id" => "self.study_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 taxonlabelsets

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlabelset>

=cut

__PACKAGE__->has_many(
  "taxonlabelsets",
  "Bio::Phylo::TreeBASE::Result::Taxonlabelset",
  { "foreign.study_id" => "self.study_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:Qsr1WOYYRSRR3qbaoWw/Lw


# You can replace this text with custom content, and it will be preserved on regeneration
use Bio::Phylo::Project;
use Bio::Phylo::Factory;

push @Bio::Phylo::TreeBASE::Result::Study::ISA, 'Bio::Phylo::Project';
my $fac = Bio::Phylo::Factory->new;

sub get_taxa {
	my $self = shift;
	my @taxa = $self->taxonlabelsets;
	return \@taxa;
}

sub get_forests {
	my $self = shift;
	my %treeblock_for_id;
	for my $tree ( $self->phylotrees ) {
		my $treeblock = $tree->treeblock;
		$treeblock_for_id{ $treeblock->get_id } = $treeblock;
	}
	return [ values %treeblock_for_id ];
}

sub get_matrices {
	my $self = shift;
	my @matrices = $self->matrixes;
	return \@matrices;
}

sub get_id { shift->study_id }

sub get_desc { shift->notes }

sub to_nexus {
	my $self = shift;
	my $template = "#NEXUS\n[! Study URI http://purl.org/phylo/treebase/phylows/study/TB2:S%s ]\n";
	my $url = sprintf($template, $self->study_id);
	my $nexus = $self->SUPER::to_nexus(@_);
	$nexus =~ s/^#NEXUS/$url/;
	return $nexus;
}

1;
