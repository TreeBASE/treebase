#!/usr/bin/perl
use strict;
use warnings;
use Bio::Phylo::TreeBASE;
use Bio::Phylo::Util::Logger ':levels';
use Bio::Phylo::Factory;
use Data::Dumper;

my $fac = Bio::Phylo::Factory->new;
my $logger = Bio::Phylo::Util::Logger->new;

my $dbname = 'treebasedev';
my $host = 'treebasedb-dev.nescent.org';
my $user = 'treebase_app';
my $pass = 'tim5tema';

my $tb = Bio::Phylo::TreeBASE->connect(
    "dbi:Pg:dbname=$dbname;host=$host",
    $user,
    $pass,
    { AutoCommit => 0 },
);

my @matrices = $tb->resultset('Matrix')->search_literal(
	'ntax > ? AND nchar > ?', qw/3 20/);

my ( @studies, %seen_tree );
MATRIX: for my $matrix ( @matrices ) {
	my $matrix_id = $matrix->matrix_id;
	my $type = $matrix->get_type;
	if ( $type ne 'Dna' ) {
		warn "Matrix $matrix_id is ${type}, not Dna";
		next MATRIX;
	}	
	if ( -e "nexus/${matrix_id}.nex" ) {
		warn "Matrix $matrix_id has already been written out";
		next MATRIX;		
	}
	for my $data ( $matrix->analyzeddatas ) {
		if ( $data->input ) {
			my $step = $data->analysisstep;
			for my $other_data ( $step->analyzeddatas ) {
				if ( not $other_data->input ) {
					if ( $other_data->type eq 'T' ) {
						my $tree = $other_data->phylotree;
						my $tree_id = $tree->phylotree_id;
						if ( not $seen_tree{ $tree_id } ) {
							$seen_tree{ $tree_id } = 1;
							write_study($matrix_id,$tree_id);							
							warn "found tree $tree_id";
						}
						else {
							warn "already seen tree $tree_id";
						}
					}
				}
			}
		}
	}
}

sub write_study {
	my ( $matrix_id, $tree_id ) = @_;
	my $template = 'http://purl.org/phylo/treebase/phylows/%s/TB2:%s?format=nexus';
	
	# download tree
	my $tree_file = "nexus/M${matrix_id}-Tr${tree_id}.tre";
	my $tree_url = sprintf($template,"tree","Tr${tree_id}");
	if ( system('wget','-O',$tree_file,$tree_url) == 0 ) {
		warn "downloaded $tree_file from $tree_url";
	}
	else {
		warn "problem: $?";
	}
	
	# download matrix
	my $matrix_file = "nexus/M${matrix_id}.nex";
	my $matrix_url = sprintf($template,"matrix","M${matrix_id}");
	if ( system('wget','-O',$matrix_file,$matrix_url) == 0 ) {
		warn "downloaded $matrix_file from $matrix_url";
	}
	else {
		warn "problem: $?";
	}	
}
