package Bio::Phylo::TreeBASE::Result::CharsetColrange;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::CharsetColrange

=cut

__PACKAGE__->table("charset_colrange");

=head1 ACCESSORS

=head2 charset_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 columnrange_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "charset_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "columnrange_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->add_unique_constraint("charset_colrange_columnrange_id_key", ["columnrange_id"]);

=head1 RELATIONS

=head2 columnrange

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Columnrange>

=cut

__PACKAGE__->belongs_to(
  "columnrange",
  "Bio::Phylo::TreeBASE::Result::Columnrange",
  { columnrange_id => "columnrange_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);

=head2 charset

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Charset>

=cut

__PACKAGE__->belongs_to(
  "charset",
  "Bio::Phylo::TreeBASE::Result::Charset",
  { charset_id => "charset_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:WqXcFABK83Yw6t8mWN5rig


# You can replace this text with custom content, and it will be preserved on regeneration
1;
