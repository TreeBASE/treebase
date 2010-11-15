package Bio::Phylo::TreeBASE::Result::Algorithm;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Algorithm

=cut

__PACKAGE__->table("algorithm");

=head1 ACCESSORS

=head2 type

  data_type: 'char'
  is_nullable: 0
  size: 1

=head2 algorithm_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'algorithm_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 description

  data_type: 'varchar'
  is_nullable: 1
  size: 2000

=head2 propertyname

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 propertyvalue

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=head2 usertype_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 gapmode_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 polytcount_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "type",
  { data_type => "char", is_nullable => 0, size => 1 },
  "algorithm_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "algorithm_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "description",
  { data_type => "varchar", is_nullable => 1, size => 2000 },
  "propertyname",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "propertyvalue",
  { data_type => "varchar", is_nullable => 1, size => 255 },
  "usertype_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "gapmode_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "polytcount_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("algorithm_id");

=head1 RELATIONS

=head2 gapmode

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Gapmode>

=cut

__PACKAGE__->belongs_to(
  "gapmode",
  "Bio::Phylo::TreeBASE::Result::Gapmode",
  { gapmode_id => "gapmode_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 polytcount

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Polytcount>

=cut

__PACKAGE__->belongs_to(
  "polytcount",
  "Bio::Phylo::TreeBASE::Result::Polytcount",
  { polytcount_id => "polytcount_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 usertype

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Usertype>

=cut

__PACKAGE__->belongs_to(
  "usertype",
  "Bio::Phylo::TreeBASE::Result::Usertype",
  { usertype_id => "usertype_id" },
  {
    is_deferrable => 1,
    join_type     => "LEFT",
    on_delete     => "CASCADE",
    on_update     => "CASCADE",
  },
);

=head2 analysissteps

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Analysisstep>

=cut

__PACKAGE__->has_many(
  "analysissteps",
  "Bio::Phylo::TreeBASE::Result::Analysisstep",
  { "foreign.algorithm_id" => "self.algorithm_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:wxKvAVkFQHMlQu7n/NcF/A


# You can replace this text with custom content, and it will be preserved on regeneration
1;
