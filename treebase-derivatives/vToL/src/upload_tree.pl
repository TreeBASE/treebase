#!/usr/bin/perl

# This script parsers a NEXUS file and uploads any phylogenies in it 
# to the database. When this is done, you'll want to make a mapping 
# between the new taxon labels brought in with your tree and the existing 
# taxonomy:
#
# UPDATE nodes SET taxon_variant_id = tv.taxon_variant_id 
# FROM nodes nds JOIN taxon_variants tv ON (nds.node_label = tv.fullnamestring)
# WHERE nds.tree_id = 9365 
# AND (nds.right_id - nds.left_id = 1)
# AND nodes.node_id = nds.node_id;

use strict;
use warnings;
use DBI;
use Bio::Phylo::IO 'parse';

my ( $taxa, $matrix, $forest );

my $file = shift @ARGV;
my $blocks = parse(
   '-format' => 'nexus',
   '-file'       => $file,
);
 
for my $block ( @{ $blocks } ) {
	$forest = $block if $block->isa('Bio::Phylo::Forest');
}
 
# Fill in the database name and access credentials
my $database = "";
my $username = "";
my $password = "";
my $dbh = &ConnectToPg($database, $username, $password);

my ($sth, $rv);
 
# global statements for setting the left-right indexing
my $setleft  = $dbh->prepare("UPDATE nodes SET left_id = ? WHERE node_id = ?");
my $setright = $dbh->prepare("UPDATE nodes SET right_id = ? WHERE node_id = ?");
my $ctr;
 
# global statements for calculating the transitive closure
my $deletepaths = $dbh->prepare("DELETE FROM node_path WHERE child_node_id IN (SELECT node_id FROM nodes WHERE tree_id = ?)");
 
my $init_sql = "INSERT INTO node_path (child_node_id, parent_node_id, distance) ";
  $init_sql .= "SELECT e.child_id, e.parent_id, 1 FROM edges e, nodes n ";
  $init_sql .= "WHERE e.child_id = n.node_id AND n.tree_id = ?";
my $initialize_paths =  $dbh->prepare("$init_sql");  
 
my $path_sql = "INSERT INTO node_path (child_node_id, parent_node_id, distance)";
  $path_sql .= "SELECT e.child_id, p.parent_node_id, p.distance+1 ";
  $path_sql .= "FROM node_path p, edges e, nodes n ";
  $path_sql .= "WHERE p.child_node_id = e.parent_id ";
  $path_sql .= "AND n.node_id = e.child_id AND n.tree_id = ? ";
  $path_sql .= "AND p.distance = ?";
my $calc_paths =  $dbh->prepare("$path_sql");
 
foreach my $tree ( @{ $forest->get_entities } ) {
    print "###########################\n";
    print $tree->get_name . "\n";
    my $tree_name = $tree->get_name;
 
    # create a new node record, which will be the root of this tree
    $dbh->do( "INSERT INTO nodes (node_label) VALUES (NULL) " );
    my $root_node_id = $dbh->last_insert_id(undef,undef,undef,undef,{sequence=>'nodes_node_id'});
    
    # create a new tree record, specifying the tree's name it its root node
    $dbh->do( "INSERT INTO trees (tree_label, root) VALUES ('$tree_name','$root_node_id')" );
    my $tree_id = $dbh->last_insert_id(undef,undef,undef,undef,{sequence=>'trees_tree_id'});
 
    # update the newly created node so that it knows what tree it belongs to
    $dbh->do( "UPDATE nodes SET tree_id = '$tree_id' WHERE node_id = $root_node_id " );
 
    # set a counter for the left-right indexing
    $ctr = 1;
    walktree( $tree->get_root , $tree_id, $root_node_id);
 
    compute_tc($tree_id);
}
my $rc = $dbh->disconnect;
 
exit;
 
 
#===================================
sub walktree {
	my $parent = shift;
	my $tree_id = shift;
	my $parent_id = shift;
	
	$setleft->execute($ctr++, $parent_id);
 
	for my $child ( @{ $parent->get_children } ) {
 
		my $branch_length;
		my $edge_support;
 
		# create a new child record, but only use the label if it doesn't look like a 
		# clade support value (i.e. a number)
		if (($child->get_name) && !($child->get_name =~ m/^\d*\.?\d+$/)) {
			my $taxon_label = $child->get_name;
			$dbh->do( "INSERT INTO nodes (node_label, tree_id) VALUES (?, ?) ", undef, &detokenize($taxon_label), $tree_id );
		} else {
			$dbh->do( "INSERT INTO nodes (node_label, tree_id) VALUES (NULL, $tree_id) " );
		}
		my $child_id = $dbh->last_insert_id(undef,undef,undef,undef,{sequence=>'nodes_node_id'});
 
		# capture the branch length if there is one
		if ($child->get_branch_length) {
			$branch_length = $child->get_branch_length;
		} else {
			$branch_length = undef;
		}
 
		# capture the edge support if internal node label looks like a number
		if (($child->is_internal) && ($child->get_internal_name =~ m/^\d*\.?\d+$/)) {
			$edge_support = $child->get_internal_name;
		} else {
			$edge_support = undef;
		}
 
		# create an edge record between parent and child
		my @values = ("$parent_id", "$child_id", "$branch_length", "$edge_support");
		$dbh->do( "INSERT INTO edges (parent_id, child_id, edge_length, edge_support) VALUES (?, ?, ?, ?) ", undef, @values);
 
		walktree( $child, $tree_id, $child_id );
	}
	
	$setright->execute($ctr++, $parent_id);
}
 
# Remove nexus tokenization
#==============================================================
sub detokenize {
	my $token = shift;
	
	$token =~ s/_/ /g;
	$token =~ s/^\'//g;
	$token =~ s/\'$//g;
	$token =~ s/''/'/g;
	
	return($token);
}
 
# Compute the transitive closure
#==============================================================
sub compute_tc {
   my $tree_id = shift;
 
   $deletepaths->execute($tree_id);
   $initialize_paths->execute($tree_id);
 
   my $dist = 1;
   my $rv = 1;
   while ($rv > 0) {
        $rv = $calc_paths->execute($tree_id, $dist);
        $dist++;
   }
}
 
# Connect to Postgres using DBI
#==============================================================
sub ConnectToPg {
 
	my ($cstr, $user, $pass) = @_;
 
	$cstr = "DBI:Pg:dbname="."$cstr";
	#$cstr .= ";host=10.9.1.1";
 
	my $dbh = DBI->connect($cstr, $user, $pass, {PrintError => 1, RaiseError => 1});
	$dbh || &error("DBI connect failed : ",$dbh->errstr);
 
	return($dbh);
}
 
# Get the auto-added id
#==============================================================
sub last_insert_id {
	my($dbh, $sequence_name) = @_;
	
	my $driver = $dbh->{Driver}->{Name};
	if (lc($driver) eq 'mysql') {
		return $dbh->{'mysql_insertid'};
	} else {
		return $dbh->last_insert_id(undef,undef,undef,undef,{sequence=>'$sequence_name'});
	}
}