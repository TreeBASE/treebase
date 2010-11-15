package Bio::Phylo::TreeBASE::Result::CompoundElement;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::CompoundElement

=cut

__PACKAGE__->table("compound_element");

=head1 ACCESSORS

=head2 compound_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 element_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "compound_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "element_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("compound_id", "element_id");
__PACKAGE__->add_unique_constraint("compound_element_element_id_key", ["element_id"]);

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

=head2 compound

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Matrixelement>

=cut

__PACKAGE__->belongs_to(
  "compound",
  "Bio::Phylo::TreeBASE::Result::Matrixelement",
  { matrixelement_id => "compound_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:l1omaWHgL1OuQeMJjv7Rtg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
