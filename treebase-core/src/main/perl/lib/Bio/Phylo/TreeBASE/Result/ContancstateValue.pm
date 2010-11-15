package Bio::Phylo::TreeBASE::Result::ContancstateValue;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::ContancstateValue

=cut

__PACKAGE__->table("contancstate_value");

=head1 ACCESSORS

=head2 ancstate_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 element

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=cut

__PACKAGE__->add_columns(
  "ancstate_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "element",
  { data_type => "varchar", is_nullable => 1, size => 255 },
);

=head1 RELATIONS

=head2 ancstate

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Ancestralstate>

=cut

__PACKAGE__->belongs_to(
  "ancstate",
  "Bio::Phylo::TreeBASE::Result::Ancestralstate",
  { ancestralstate_id => "ancstate_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:GQGP7B5637gXlGvHz1gsHQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
