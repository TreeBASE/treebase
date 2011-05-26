#!/usr/bin/perl
use strict;
use warnings;
use DBIx::Class::Schema::Loader qw/make_schema_at/;

my $dbname = 'treebasedev';
my $host = 'treebasedb-dev.nescent.org';
my $user = 'treebase_app';
my $pass = 'tim5tema';
make_schema_at(
	'Bio::Phylo::TreeBASE',
	{ 
		'debug' => 1,
		'dump_directory' => './lib',
	},
	[ 
		"dbi:Pg:dbname=$dbname;host=$host", 
		$user, 
		$pass,
	],
);