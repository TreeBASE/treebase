package Bio::Phylo::TreeBASE::Result::UsertyperrdColrange;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::UsertyperrdColrange

=cut

__PACKAGE__->table("usertyperrd_colrange");

=head1 ACCESSORS

=head2 usertyperecord_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 columnrange_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "usertyperecord_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "columnrange_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->add_unique_constraint("usertyperrd_colrange_columnrange_id_key", ["columnrange_id"]);

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

=head2 usertyperecord

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Usertyperecord>

=cut

__PACKAGE__->belongs_to(
  "usertyperecord",
  "Bio::Phylo::TreeBASE::Result::Usertyperecord",
  { usertyperecord_id => "usertyperecord_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:M11kRlub8IeVlooL0kB22Q


# You can replace this text with custom content, and it will be preserved on regeneration
1;
