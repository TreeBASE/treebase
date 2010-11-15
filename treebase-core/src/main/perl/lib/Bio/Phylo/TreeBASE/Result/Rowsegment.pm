package Bio::Phylo::TreeBASE::Result::Rowsegment;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Rowsegment

=cut

__PACKAGE__->table("rowsegment");

=head1 ACCESSORS

=head2 rowsegment_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'rowsegment_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 endindex

  data_type: 'integer'
  is_nullable: 1

=head2 catalognum

  data_type: 'varchar'
  is_nullable: 1
  size: 50

=head2 collectioncode

  data_type: 'varchar'
  is_nullable: 1
  size: 50

=head2 collector

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 country

  data_type: 'varchar'
  is_nullable: 1
  size: 50

=head2 elevation

  data_type: 'double precision'
  is_nullable: 1

=head2 genbaccession

  data_type: 'varchar'
  is_nullable: 1
  size: 30

=head2 instacronym

  data_type: 'varchar'
  is_nullable: 1
  size: 50

=head2 latitude

  data_type: 'double precision'
  is_nullable: 1

=head2 locality

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 longitude

  data_type: 'double precision'
  is_nullable: 1

=head2 notes

  data_type: 'varchar'
  is_nullable: 1
  size: 2000

=head2 otheraccession

  data_type: 'varchar'
  is_nullable: 1
  size: 50

=head2 sampledate

  data_type: 'date'
  is_nullable: 1

=head2 state

  data_type: 'varchar'
  is_nullable: 1
  size: 50

=head2 startindex

  data_type: 'integer'
  is_nullable: 1

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 matrixrow_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 taxonlabel_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "rowsegment_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "rowsegment_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "endindex",
  { data_type => "integer", is_nullable => 1 },
  "catalognum",
  { data_type => "varchar", is_nullable => 1, size => 50 },
  "collectioncode",
  { data_type => "varchar", is_nullable => 1, size => 50 },
  "collector",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "country",
  { data_type => "varchar", is_nullable => 1, size => 50 },
  "elevation",
  { data_type => "double precision", is_nullable => 1 },
  "genbaccession",
  { data_type => "varchar", is_nullable => 1, size => 30 },
  "instacronym",
  { data_type => "varchar", is_nullable => 1, size => 50 },
  "latitude",
  { data_type => "double precision", is_nullable => 1 },
  "locality",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "longitude",
  { data_type => "double precision", is_nullable => 1 },
  "notes",
  { data_type => "varchar", is_nullable => 1, size => 2000 },
  "otheraccession",
  { data_type => "varchar", is_nullable => 1, size => 50 },
  "sampledate",
  { data_type => "date", is_nullable => 1 },
  "state",
  { data_type => "varchar", is_nullable => 1, size => 50 },
  "startindex",
  { data_type => "integer", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "matrixrow_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "taxonlabel_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("rowsegment_id");

=head1 RELATIONS

=head2 taxonlabel

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Taxonlabel>

=cut

__PACKAGE__->belongs_to(
  "taxonlabel",
  "Bio::Phylo::TreeBASE::Result::Taxonlabel",
  { taxonlabel_id => "taxonlabel_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 matrixrow

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixrow>

=cut

__PACKAGE__->belongs_to(
  "matrixrow",
  "Bio::Phylo::TreeBASE::Result::Matrixrow",
  { matrixrow_id => "matrixrow_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:ksur82SrxNqnwxTBzHYHTg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
