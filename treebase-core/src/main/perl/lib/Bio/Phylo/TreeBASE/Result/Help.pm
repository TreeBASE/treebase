package Bio::Phylo::TreeBASE::Result::Help;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Help

=cut

__PACKAGE__->table("help");

=head1 ACCESSORS

=head2 help_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'help_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 helptext

  data_type: 'text'
  is_nullable: 1

=head2 tag

  data_type: 'varchar'
  is_nullable: 1
  size: 255

=cut

__PACKAGE__->add_columns(
  "help_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "help_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "helptext",
  { data_type => "text", is_nullable => 1 },
  "tag",
  { data_type => "varchar", is_nullable => 1, size => 255 },
);
__PACKAGE__->set_primary_key("help_id");


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:iKY9Na16wIk5kRJN8ICSAQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
