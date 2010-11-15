package Bio::Phylo::TreeBASE::Result::Usertyperecord;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Usertyperecord

=cut

__PACKAGE__->table("usertyperecord");

=head1 ACCESSORS

=head2 usertyperecord_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'usertyperecord_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 usertype_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=head2 typeset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "usertyperecord_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "usertyperecord_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "usertype_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
  "typeset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 1 },
);
__PACKAGE__->set_primary_key("usertyperecord_id");

=head1 RELATIONS

=head2 typeset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Typeset>

=cut

__PACKAGE__->belongs_to(
  "typeset",
  "Bio::Phylo::TreeBASE::Result::Typeset",
  { typeset_id => "typeset_id" },
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

=head2 usertyperrd_colranges

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::UsertyperrdColrange>

=cut

__PACKAGE__->has_many(
  "usertyperrd_colranges",
  "Bio::Phylo::TreeBASE::Result::UsertyperrdColrange",
  { "foreign.usertyperecord_id" => "self.usertyperecord_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:F+gWJJT2YwRqLDDbU4SU8g


# You can replace this text with custom content, and it will be preserved on regeneration
1;
