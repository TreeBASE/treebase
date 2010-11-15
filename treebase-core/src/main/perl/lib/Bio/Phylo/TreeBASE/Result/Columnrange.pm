package Bio::Phylo::TreeBASE::Result::Columnrange;

# Created by DBIx::Class::Schema::Loader
# DO NOT MODIFY THE FIRST PART OF THIS FILE

use strict;
use warnings;

use base 'DBIx::Class::Core';


=head1 NAME

Bio::Phylo::TreeBASE::Result::Columnrange

=cut

__PACKAGE__->table("columnrange");

=head1 ACCESSORS

=head2 columnrange_id

  data_type: 'bigint'
  is_auto_increment: 1
  is_nullable: 0
  sequence: 'columnrange_id_sequence'

=head2 version

  data_type: 'integer'
  is_nullable: 1

=head2 endcolindex

  data_type: 'integer'
  is_nullable: 1

=head2 repeatinterval

  data_type: 'integer'
  is_nullable: 1

=head2 startcolindex

  data_type: 'integer'
  is_nullable: 1

=cut

__PACKAGE__->add_columns(
  "columnrange_id",
  {
    data_type         => "bigint",
    is_auto_increment => 1,
    is_nullable       => 0,
    sequence          => "columnrange_id_sequence",
  },
  "version",
  { data_type => "integer", is_nullable => 1 },
  "endcolindex",
  { data_type => "integer", is_nullable => 1 },
  "repeatinterval",
  { data_type => "integer", is_nullable => 1 },
  "startcolindex",
  { data_type => "integer", is_nullable => 1 },
);
__PACKAGE__->set_primary_key("columnrange_id");

=head1 RELATIONS

=head2 chargroup_colrange

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::ChargroupColrange>

=cut

__PACKAGE__->might_have(
  "chargroup_colrange",
  "Bio::Phylo::TreeBASE::Result::ChargroupColrange",
  { "foreign.columnrange_id" => "self.columnrange_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 charset_colrange

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::CharsetColrange>

=cut

__PACKAGE__->might_have(
  "charset_colrange",
  "Bio::Phylo::TreeBASE::Result::CharsetColrange",
  { "foreign.columnrange_id" => "self.columnrange_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 charweight_colrange

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::CharweightColrange>

=cut

__PACKAGE__->might_have(
  "charweight_colrange",
  "Bio::Phylo::TreeBASE::Result::CharweightColrange",
  { "foreign.columnrange_id" => "self.columnrange_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 coderecord_colrange

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::CoderecordColrange>

=cut

__PACKAGE__->might_have(
  "coderecord_colrange",
  "Bio::Phylo::TreeBASE::Result::CoderecordColrange",
  { "foreign.columnrange_id" => "self.columnrange_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 codonchar1_colrange

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::Codonchar1Colrange>

=cut

__PACKAGE__->might_have(
  "codonchar1_colrange",
  "Bio::Phylo::TreeBASE::Result::Codonchar1Colrange",
  { "foreign.columnrange_id" => "self.columnrange_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 codonchar2_colrange

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::Codonchar2Colrange>

=cut

__PACKAGE__->might_have(
  "codonchar2_colrange",
  "Bio::Phylo::TreeBASE::Result::Codonchar2Colrange",
  { "foreign.columnrange_id" => "self.columnrange_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 codonchar3_colrange

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::Codonchar3Colrange>

=cut

__PACKAGE__->might_have(
  "codonchar3_colrange",
  "Bio::Phylo::TreeBASE::Result::Codonchar3Colrange",
  { "foreign.columnrange_id" => "self.columnrange_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 codonnoncoding_colrange

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::CodonnoncodingColrange>

=cut

__PACKAGE__->might_have(
  "codonnoncoding_colrange",
  "Bio::Phylo::TreeBASE::Result::CodonnoncodingColrange",
  { "foreign.columnrange_id" => "self.columnrange_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);

=head2 usertyperrd_colrange

Type: might_have

Related object: L<Bio::Phylo::TreeBASE::Result::UsertyperrdColrange>

=cut

__PACKAGE__->might_have(
  "usertyperrd_colrange",
  "Bio::Phylo::TreeBASE::Result::UsertyperrdColrange",
  { "foreign.columnrange_id" => "self.columnrange_id" },
  { cascade_copy => 0, cascade_delete => 0 },
);


# Created by DBIx::Class::Schema::Loader v0.07002 @ 2010-11-13 19:19:22
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:AeMPsdric+juOmRopS57Cg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
