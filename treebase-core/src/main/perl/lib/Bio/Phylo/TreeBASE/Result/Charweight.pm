package Bio::Phylo::TreeBASE::Result::Charweight;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Charweight

=cut

__PACKAGE__->table("charweight");

=head1 ACCESSORS

=head2 type

  data_type: 'char'
  is_nullable: 0
  size: 1

=head2 charweight_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'charweight_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 weight

  data_type: 'integer'
  is_nullable: 1

=head2 realweight

  data_type: 'double precision'
  is_nullable: 1

=head2 charweightset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "type",
  { data_type => "char", is_nullable => 0, size => 1 },
  "charweight_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "charweight_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "weight",
  { data_type => "integer", is_nullable => 1 },
  "realweight",
  { data_type => "double precision", is_nullable => 1 },
  "charweightset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("charweight_id");

=head1 RELATIONS

=head2 charweightset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Charweightset>

=cut

__PACKAGE__->belongs_to(
  "charweightset",
  "Bio::Phylo::TreeBASE::Result::Charweightset",
  { charweightset_id => "charweightset_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 charweight_colranges

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::CharweightColrange>

=cut

__PACKAGE__->has_many(
  "charweight_colranges",
  "Bio::Phylo::TreeBASE::Result::CharweightColrange",
  { "foreign.charweight_id" => "self.charweight_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:zz3CjIbj/VAUeAqM9EKSQA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
