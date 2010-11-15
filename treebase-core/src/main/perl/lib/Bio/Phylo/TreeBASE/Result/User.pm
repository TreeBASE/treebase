package Bio::Phylo::TreeBASE::Result::User;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::User

=cut

__PACKAGE__->table("user");

=head1 ACCESSORS

=head2 user_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'user_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 password

  data_type: 'varchar'
  is_nullable: 0
  size: 100

=head2 username

  data_type: 'varchar'
  is_nullable: 0
  size: 100

=head2 person_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 userrole_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "user_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "user_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "password",
  { data_type => "varchar", is_nullable => 0, size => 100 },
  "username",
  { data_type => "varchar", is_nullable => 0, size => 100 },
  "person_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "userrole_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->set_primary_key("user_id");
__PACKAGE__->add_unique_constraint("user_username_key", ["username"]);

=head1 RELATIONS

=head2 studies

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Study>

=cut

__PACKAGE__->has_many(
  "studies",
  "Bio::Phylo::TreeBASE::Result::Study",
  { "foreign.user_id" => "self.user_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 submissions

Type: has_many

Related object: L<Bio::Phylo::TreeBASE::Result::Submission>

=cut

__PACKAGE__->has_many(
  "submissions",
  "Bio::Phylo::TreeBASE::Result::Submission",
  { "foreign.user_id" => "self.user_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 userrole

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Userrole>

=cut

__PACKAGE__->belongs_to(
  "userrole",
  "Bio::Phylo::TreeBASE::Result::Userrole",
  { userrole_id => "userrole_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 person

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Person>

=cut

__PACKAGE__->belongs_to(
  "person",
  "Bio::Phylo::TreeBASE::Result::Person",
  { person_id => "person_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:ePyObCYOyCw3ZiABGlAOQw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
