package Bio::Phylo::TreeBASE::Result::Itemvalue;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Itemvalue

=cut

__PACKAGE__->table("itemvalue");

=head1 ACCESSORS

=head2 itemvalue_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'itemvalue_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 value

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 element_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "itemvalue_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "itemvalue_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "value",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "element_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("itemvalue_id");

=head1 RELATIONS

=head2 element

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixelement>

=cut

__PACKAGE__->belongs_to(
  "element",
  "Bio::Phylo::TreeBASE::Result::Matrixelement",
  { matrixelement_id => "element_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:xBIK18ipXEPhbrmE454dHg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
