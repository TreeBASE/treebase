package Bio::Phylo::TreeBASE::Result::CharweightColrange;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::CharweightColrange

=cut

__PACKAGE__->table("charweight_colrange");

=head1 ACCESSORS

=head2 charweight_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=head2 columnrange_id

  data_type: 'bigint'
  is_foreign_key: 1
  is_nullable: 0

=cut

__PACKAGE__->add_columns(
  "charweight_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
  "columnrange_id",
  { data_type => "bigint", is_foreign_key => 1, is_nullable => 0 },
);
__PACKAGE__->add_unique_constraint("charweight_colrange_columnrange_id_key", ["columnrange_id"]);

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

=head2 charweight

Type: belongs_to

Related object: L<Bio::Phylo::TreeBASE::Result::Charweight>

=cut

__PACKAGE__->belongs_to(
  "charweight",
  "Bio::Phylo::TreeBASE::Result::Charweight",
  { charweight_id => "charweight_id" },
  { is_deferrable => 1, on_delete => "CASCADE", on_update => "CASCADE" },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:r6rX8xb3XGDWbPe4JjCiYw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
