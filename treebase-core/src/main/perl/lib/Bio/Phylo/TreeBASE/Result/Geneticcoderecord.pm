package Bio::Phylo::TreeBASE::Result::Geneticcoderecord;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Geneticcoderecord

=cut

__PACKAGE__->table("geneticcoderecord");

=head1 ACCESSORS

=head2 geneticcoderecord_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'geneticcoderecord_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 geneticcode_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 geneticcodeset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "geneticcoderecord_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "geneticcoderecord_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "geneticcode_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "geneticcodeset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("geneticcoderecord_id");

=head1 RELATIONS

=head2 coderecord_colranges

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::CoderecordColrange>

=cut

__PACKAGE__->has_many(
  "coderecord_colranges",
  "Bio::Phylo::TreeBASE::Result::CoderecordColrange",
  { "foreign.geneticcoderecord_id" => "self.geneticcoderecord_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 geneticcode

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Geneticcode>

=cut

__PACKAGE__->belongs_to(
  "geneticcode",
  "Bio::Phylo::TreeBASE::Result::Geneticcode",
  { geneticcode_id => "geneticcode_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 geneticcodeset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Geneticcodeset>

=cut

__PACKAGE__->belongs_to(
  "geneticcodeset",
  "Bio::Phylo::TreeBASE::Result::Geneticcodeset",
  { geneticcodeset_id => "geneticcodeset_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:CizMI+sDwpt8VYiWjiIdXQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
