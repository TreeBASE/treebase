package Bio::Phylo::TreeBASE::Result::Matrixelement;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Matrixelement

=cut

__PACKAGE__->table("matrixelement");

=head1 ACCESSORS

=head2 type

  data_type: 'char'
  is_nullable: 0
  size: 1

=head2 matrixelement_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'matrixelement_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 andlogic

  data_type: 'boolean'
  is_nullable: 1

=head2 compoundvalue

  data_type: 'varchar'
  is_nullable: 1
  size: 1000

=head2 value

  data_type: 'double precision'
  is_nullable: 1

=head2 gap

  data_type: 'boolean'
  is_nullable: 1

=head2 matrixcolumn_id

  data_type: 'bigint'
  is_nullable: 1

=head2 matrixrow_id

  data_type: 'bigint'
  is_nullable: 1

=head2 itemdefinition_id

  data_type: 'bigint'
  is_nullable: 1

=head2 discretecharstate_id

  data_type: 'bigint'
  is_nullable: 1

=head2 element_order

  data_type: 'integer'
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "type",
  { data_type => "char", is_nullable => 0, size => 1 },
  "matrixelement_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "matrixelement_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "andlogic",
  { data_type => "boolean", is_nullable => 1 },
  "compoundvalue",
  { data_type => "varchar", is_nullable => 1, size => 1000 },
  "value",
  { data_type => "double precision", is_nullable => 1 },
  "gap",
  { data_type => "boolean", is_nullable => 1 },
  "matrixcolumn_id",
  { data_type => "bigint", is_nullable => 1 },
  "matrixrow_id",
  { data_type => "bigint", is_nullable => 1 },
  "itemdefinition_id",
  { data_type => "bigint", is_nullable => 1 },
  "discretecharstate_id",
  { data_type => "bigint", is_nullable => 1 },
  "element_order",
  { data_type => "integer", is_nullable => 1 },
);
__PACKAGE__->set_primary_key("matrixelement_id");

=head1 RELATIONS

=head2 compound_element_element

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::CompoundElement>

=cut

__PACKAGE__->might_have(
  "compound_element_element",
  "Bio::Phylo::TreeBASE::Result::CompoundElement",
  { "foreign.element_id" => "self.matrixelement_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 compound_element_compounds

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::CompoundElement>

=cut

__PACKAGE__->has_many(
  "compound_element_compounds",
  "Bio::Phylo::TreeBASE::Result::CompoundElement",
  { "foreign.compound_id" => "self.matrixelement_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 itemvalues

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Itemvalue>

=cut

__PACKAGE__->has_many(
  "itemvalues",
  "Bio::Phylo::TreeBASE::Result::Itemvalue",
  { "foreign.element_id" => "self.matrixelement_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 statemodifiers

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Statemodifier>

=cut

__PACKAGE__->has_many(
  "statemodifiers",
  "Bio::Phylo::TreeBASE::Result::Statemodifier",
  { "foreign.element_id" => "self.matrixelement_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:ixr7RMvCnQ8Iie3q/WgCCA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
