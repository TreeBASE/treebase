package Bio::Phylo::TreeBASE::Result::Usertype;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Usertype

=cut

__PACKAGE__->table("usertype");

=head1 ACCESSORS

=head2 type

  data_type: 'char'
  is_nullable: 0
  size: 1

=head2 usertype_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'usertype_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 title

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=cut

__PACKAGE__->add_columns(
  "type",
  { data_type => "char", is_nullable => 0, size => 1 },
  "usertype_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "usertype_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "title",
  { data_type => "varchar", is_nullable => 1, size => 255 },
);
__PACKAGE__->set_primary_key("usertype_id");

=head1 RELATIONS

=head2 algorithms

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Algorithm>

=cut

__PACKAGE__->has_many(
  "algorithms",
  "Bio::Phylo::TreeBASE::Result::Algorithm",
  { "foreign.usertype_id" => "self.usertype_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 cstreenodes

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Cstreenode>

=cut

__PACKAGE__->has_many(
  "cstreenodes",
  "Bio::Phylo::TreeBASE::Result::Cstreenode",
  { "foreign.cstree_id" => "self.usertype_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 stepmatrixelements

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Stepmatrixelement>

=cut

__PACKAGE__->has_many(
  "stepmatrixelements",
  "Bio::Phylo::TreeBASE::Result::Stepmatrixelement",
  { "foreign.stepmatrix_id" => "self.usertype_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 usertyperecords

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Usertyperecord>

=cut

__PACKAGE__->has_many(
  "usertyperecords",
  "Bio::Phylo::TreeBASE::Result::Usertyperecord",
  { "foreign.usertype_id" => "self.usertype_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:lwmsCNvadepYQ6DovGlKRQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
