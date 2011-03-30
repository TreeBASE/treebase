#!/usr/bin/perl

# This script outputs a vToL as a NEXUS tree file, attaching all
# missing species (from the classification) to an extra node that 
# is parent to the subtree where the missing species attaches. 
# Additionally, the added species are prefixed with "O" so that 
# they can be easily highlighted in a different color using PhyloWidget

use strict;
use DBI;

my @inputs = @ARGV;
my $map = shift(@inputs);

if (length($inputs[0]) < 2) {
  print "Input error! Usage: perl download_vToL.pl [mapping] [list of tree_ids] \n";
  exit;
}


# Fill in the database name and access credentials
my $database = "";
my $username = "";
my $password = "";
my $dbh = &ConnectToPg($database, $username, $password);

&getTrees ($dbh, $map, @inputs);

my $rc = $dbh->disconnect;

exit;




# get trees
#==============================================================
sub getTrees {

	my ($dbh, $map, @treeItems) = @_;

	if (@treeItems) {
		
		print "#NEXUS\n\nBEGIN TREES;\n\n";
		foreach my $treeid (@treeItems) {
		
			print "   [tr_id: $treeid]\n";
			
			my $statement = "SELECT root, tree_label FROM trees WHERE ( tree_id = ? )";
			my $sth = $dbh->prepare ($statement);
			$sth->execute( $treeid );
			
			$statement  = "SELECT child_id, node_label, edge_length, edge_support ";
			$statement .= "FROM edges INNER JOIN nodes ON edges.child_ID = nodes.node_id ";
			$statement .= "WHERE parent_id = ?";
			my $children = $dbh->prepare ($statement);

			# return a list of mapped children
			$statement = "SELECT taxon_name, tax_id FROM pq_subtree JOIN pq USING (pq_id) WHERE node_id = ? ";
			my $mapping = $dbh->prepare ($statement);

			while (my @row = $sth->fetchrow_array()) {
			
				print "\tTREE ".&tokenize($row[1])." = ";
				
				&walktree($dbh, $map, $children, $mapping, $row[0]);
				
				print ";\n";

			}
			$sth->finish();
		}
		print "\nEND;\n";
		
		
	} else {
		print "Error: No trees requested\n";
		exit;
	}	

}

# walktree
#==============================================================
sub walktree {
	my $dbh = shift;
	my $map = shift;
	my $children = shift;
	my $mapping = shift;
	my $id = shift;
	my $support = shift;
	my $length = shift;
	
	my $statement = "SELECT COUNT(*) FROM edges WHERE parent_id = $id";
	my $totRec = $dbh->selectrow_array ($statement);
	
	$statement = "SELECT COUNT(*) FROM pq_subtree WHERE node_id = $id";
	my $totMaps = $dbh->selectrow_array ($statement);
	
	if ($totRec) {
		print "(";
	}
	if (($totMaps) && ($map)) {
		print "(";
	}

		
	$children->execute($id);
	
	my $br = 0;
	for my $row (@{$children->fetchall_arrayref}) {
		$br++;
		my ($id, $label, $edge_length, $edge_support) = @$row;
		$label = "O $label" if (($map) && ($label));
		print &tokenize($label);
		walktree($dbh, $map, $children, $mapping, $id, $edge_support, $edge_length);
		print "," if ($br < $totRec);
	}
	
	if ($totRec) {

		print ")";
		print &tokenize($support) if ($support);
		print ":". &tokenize($length) if ($length);	
	} else {
		print ":". &tokenize($length) if ($length);
	}

	if (($totMaps) && ($map)) {
		$mapping->execute($id);
		for my $row (@{$mapping->fetchall_arrayref}) {
			my ($mapped_label, $tax_id) = @$row;
			$mapped_label = "$mapped_label";
			print ",". &tokenize($mapped_label);
		}	
		print ")";
	}

	
}

# tokenize -- encapsulate tokens according to nexus rules
# technically speaking, single quotes should be repeated
# e.g. change "It's nice" to "'It''s nice'", but I'd rather not
# mess with that, so I'm changing all single quotes to double
#==============================================================
sub tokenize {

    my $token = shift;
   
    $token =~ s/\'/\"/g;
   
    if ($token =~ m/[-\/\?\<\>\*\%\&\$\#\@\!\"\:]/) {
        $token = "\'$token\'";
    } else {
        $token =~ s/\s/_/g;
    }
   
    return ($token);

}

# Connect to Postgres using DBI
#==============================================================
sub ConnectToPg {

    my ($cstr, $user, $pass) = @_;
   
    $cstr = "DBI:Pg:dbname="."$cstr";
    #$cstr .= ";host=dev.nescent.org";
   
    my $dbh = DBI->connect($cstr, $user, $pass, {PrintError => 1, RaiseError => 1});
    $dbh || &error("DBI connect failed : ",$dbh->errstr);

    return($dbh);
}