package Bio::Phylo::TreeBASE::Result::Distancematrixelement;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Distancematrixelement

=cut

__PACKAGE__->table("distancematrixelement");

=head1 ACCESSORS

=head2 distancematrixelement_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'distancematrixelement_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 distance

  data_type: 'double precision'
  is_nullable: 1

=head2 columnlabel_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 matrix_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 rowlabel_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "distancematrixelement_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "distancematrixelement_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "distance",
  { data_type => "double precision", is_nullable => 1 },
  "columnlabel_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "matrix_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "rowlabel_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("distancematrixelement_id");

=head1 RELATIONS

=head2 rowlabel

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlabel>

=cut

__PACKAGE__->belongs_to(
  "rowlabel",
  "Bio::Phylo::TreeBASE::Result::Taxonlabel",
  { taxonlabel_id => "rowlabel_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 matrix

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Matrix>

=cut

__PACKAGE__->belongs_to(
  "matrix",
  "Bio::Phylo::TreeBASE::Result::Matrix",
  { matrix_id => "matrix_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 columnlabel

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlabel>

=cut

__PACKAGE__->belongs_to(
  "columnlabel",
  "Bio::Phylo::TreeBASE::Result::Taxonlabel",
  { taxonlabel_id => "columnlabel_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:qzugWPFyQT9dmAxxLdgCoA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
