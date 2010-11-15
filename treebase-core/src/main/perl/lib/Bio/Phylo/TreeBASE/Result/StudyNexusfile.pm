package Bio::Phylo::TreeBASE::Result::StudyNexusfile;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::StudyNexusfile

=cut

__PACKAGE__->table("study_nexusfile");

=head1 ACCESSORS

=head2 study_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 nexus

  data_type: 'text'
  is_nullable: 0

=head2 filename

  data_type: 'varchar'
  is_nullable: 0
  size: 255

=cut

__PACKAGE__->add_columns(
  "study_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "nexus",
  { data_type => "text", is_nullable => 0 },
  "filename",
  { data_type => "varchar", is_nullable => 0, size => 255 },
);
__PACKAGE__->set_primary_key("study_id", "filename");

=head1 RELATIONS

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


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:o8XBAZMqRyLaZkZ0w9dmjQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
