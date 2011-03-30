#!/usr/bin/perl

# Script finds species present in the classification, yet missing
# from the phylogeny, and maps them to the most recent compatible node 
# on the phylogeny. The most recent compatible node is the smallest clade 
# on the phylogeny whose descendants intersect with the descendants of a 
# clade on the classification but do not contain any that intersect with
# names on the classification that are outside of the clade. 

use strict;
use DBI;
 
# check that the right number of arguments are listed
die "Input error! Usage: perl build_vtol.pl tree_id\n" if (@ARGV < 1);

# Fill in the database name and access credentials
my $database = "";
my $username = "";
my $password = "";
my $dbh = &ConnectToPg($database, $username, $password);
 
my $tree_id = shift @ARGV;
my $new_nodes_table = "my_edited_classification";


# STEP 1
# find MRCA in the classification for all leaf nodes in tree_id x

my $statement = "SELECT tax_id, name_txt, left_id, right_id  
FROM $new_nodes_table JOIN ncbi_names USING (tax_id) 
WHERE left_id <= ( SELECT MIN(left_id) FROM $new_nodes_table WHERE tax_id IN (
   SELECT tx.taxid   
   FROM nodes nds JOIN taxon_variants tv USING (taxon_variant_id) 
   JOIN taxa tx ON (tv.taxon_id = tx.taxon_id) 
   WHERE tx.taxid > 0 
   AND nds.tree_id = $tree_id 
   AND (nds.right_id - nds.left_id = 1) 
 ) )
AND right_id >= ( SELECT MAX(right_id) FROM $new_nodes_table WHERE tax_id IN (
   SELECT tx.taxid   
   FROM nodes nds JOIN taxon_variants tv USING (taxon_variant_id) 
   JOIN taxa tx ON (tv.taxon_id = tx.taxon_id) 
   WHERE tx.taxid > 0 
   AND nds.tree_id = $tree_id 
   AND (nds.right_id - nds.left_id = 1)  
 ) ) 
AND name_class = 'scientific name' 
ORDER BY right_id 
LIMIT 1";


my ($mrca_ncbi_taxid, $mrca_ncbi_name, $mrca_ncbi_left_id, $mrca_ncbi_right_id) = $dbh->selectrow_array ($statement);

print "\n\nTree tree_id = $tree_id\n\n";
print "taxid of the MRCA in NCBI = $mrca_ncbi_taxid\n";
print "name of the MRCA in NCBI = $mrca_ncbi_name\n\n";

if ($mrca_ncbi_taxid == 0) {
	my $sc = $dbh->disconnect;
	exit;
}


# STEP 2
# list of classification names that potentially could be mapped 
# to the tree with the given tree_id  

$statement = "SELECT nnds.tax_id 
FROM $new_nodes_table nnds JOIN $new_nodes_table inc 
    ON (nnds.left_id BETWEEN inc.left_id AND inc.right_id)
WHERE inc.tax_id = (
    SELECT tax_id 
    FROM $new_nodes_table  
    WHERE left_id <= ( SELECT MIN(left_id) FROM $new_nodes_table WHERE tax_id IN (
       SELECT tx.taxid   
       FROM nodes nds JOIN taxon_variants tv USING (taxon_variant_id) 
       JOIN taxa tx ON (tv.taxon_id = tx.taxon_id) 
       WHERE tx.taxid > 0 
       AND nds.tree_id = $tree_id 
       AND (nds.right_id - nds.left_id = 1) 
     ) )
    AND right_id >= ( SELECT MAX(right_id) FROM $new_nodes_table WHERE tax_id IN (
       SELECT tx.taxid   
       FROM nodes nds JOIN taxon_variants tv USING (taxon_variant_id) 
       JOIN taxa tx ON (tv.taxon_id = tx.taxon_id) 
       WHERE tx.taxid > 0 
       AND nds.tree_id = $tree_id 
       AND (nds.right_id - nds.left_id = 1)  
     ) ) 
    ORDER BY right_id 
    LIMIT 1
)
EXCEPT
SELECT tx.taxid 
FROM nodes nds JOIN taxon_variants tv USING (taxon_variant_id) 
JOIN taxa tx ON (tv.taxon_id = tx.taxon_id)
WHERE nds.tree_id = $tree_id 
AND tx.taxid > 0 
AND (nds.right_id - nds.left_id = 1)";

my $sth = $dbh->prepare($statement)
or die "Can't prepare $statement: $dbh->errstr\n";		
my $rv = $sth->execute
or die "can't execute the query: $sth->errstr\n";

my @ncbi_list;
my %ncbi_hash;
while(my @row = $sth->fetchrow_array) {
	push(@ncbi_list, $row[0]);
	$ncbi_hash{ $row[0] } = $row[0];
}
my $rd = $sth->finish;

print "Number of classification names that potentially could be mapped to tree $tree_id: $#ncbi_list \n";
print "i.e., they are all descendants from the MRCA of tree $tree_id (i.e. $mrca_ncbi_name), \n";
print "excluding names that are already in tree $tree_id.\n\n";

# STEP 3
# List all classification internal nodes bounded by the phylogeny. 
# Order by smallest clade to largest

print "List all ncbi internal nodes in the NCBI $mrca_ncbi_name \n";
print "subtree order by smallest clade to largest: \n";
print "      taxid      left_id      right_id    clade size\n";

$statement = "SELECT nnds.tax_id, nnds.left_id, nnds.right_id, (nnds.right_id - nnds.left_id) - 1 AS clade_size
FROM $new_nodes_table nnds 
WHERE nnds.left_id >= $mrca_ncbi_left_id 
AND nnds.right_id <= $mrca_ncbi_right_id 
AND (nnds.right_id - nnds.left_id) > 1 
ORDER BY (nnds.right_id - nnds.left_id) ";

my $sth = $dbh->prepare($statement)
or die "Can't prepare $statement: $dbh->errstr\n";		
my $rv = $sth->execute
or die "can't execute the query: $sth->errstr\n";

my @ncbi_internalnode_list;
my %ncbi_internalnode_left;
my %ncbi_internalnode_right;
while(my @row = $sth->fetchrow_array) {
	printf (" %10d   %10d   %10d %10d\n", @row );
	push(@ncbi_internalnode_list, $row[0]);
	$ncbi_internalnode_left{ $row[0] } = $row[1];
	$ncbi_internalnode_right{ $row[0] } = $row[2];
}
my $rd = $sth->finish;
print "\n";

# STEP 4
# For each classification clade, see if there is a MRCA equivalent in tree_id $tree_id 

print "For each ncbi clade, see if there is an equivalent clade in tree_id $tree_id:\n\n";
print "ncbi_taxid -> tree node_id\n";

$statement = "SELECT onds.node_id 
FROM nodes onds 
WHERE onds.tree_id = ? 
AND onds.left_id <= (
    SELECT MIN(nds.left_id) 
    FROM nodes nds 
    JOIN taxon_variants tv USING (taxon_variant_id) 
    JOIN taxa tx ON (tv.taxon_id = tx.taxon_id) 
    JOIN $new_nodes_table nnds ON (tx.taxid = nnds.tax_id) 
    WHERE nnds.left_id >= ? 
    AND nnds.left_id <= ? 
    AND nds.tree_id = ? 
)
AND onds.right_id >= (
    SELECT MAX(nds.right_id) 
    FROM nodes nds 
    JOIN taxon_variants tv USING (taxon_variant_id) 
    JOIN taxa tx ON (tv.taxon_id = tx.taxon_id) 
    JOIN $new_nodes_table nnds ON (tx.taxid = nnds.tax_id) 
    WHERE nnds.left_id >= ? 
    AND nnds.left_id <= ? 
    AND nds.tree_id = ? 
)
ORDER BY onds.right_id 
LIMIT 1";

my $sth = $dbh->prepare($statement)
or die "Can't prepare $statement: $dbh->errstr\n";

my %mapped_node;
my $node_in_tree;
for ( my $j=0; $j < @ncbi_internalnode_list; $j++ ) {
	my $rv = $sth->execute( $tree_id, $ncbi_internalnode_left{ $ncbi_internalnode_list[$j] }, $ncbi_internalnode_right{ $ncbi_internalnode_list[$j] }, $tree_id, $ncbi_internalnode_left{ $ncbi_internalnode_list[$j] }, $ncbi_internalnode_right{ $ncbi_internalnode_list[$j] }, $tree_id );
	($node_in_tree) = $sth->fetchrow_array;
	$mapped_node{ $ncbi_internalnode_list[$j] } = $node_in_tree if ($node_in_tree);
	print " $ncbi_internalnode_list[$j] -> $node_in_tree\n" if ($node_in_tree);
}
my $rd = $sth->finish;
print "\n";


# STEP 5
# For each classification clade, going from smallest to largest, see if there are 
# any descendants that can be mapped

print "For each ncbi clade going from smallest to largest, take all descendants and \n";
print "attach them to the equivalent clade in the tree:\n\n";
print "ncbi_taxid -> tree node_id\n";

$statement = "SELECT nnds.tax_id, nna.name_txt 
FROM ncbi_names nna JOIN $new_nodes_table nnds USING (tax_id) 
JOIN $new_nodes_table ninc ON (nnds.left_id BETWEEN ninc.left_id AND ninc.right_id)
WHERE (nnds.right_id - nnds.left_id = 1) 
AND nna.name_class = 'scientific name' 
AND ninc.tax_id = ? ";
my $sth = $dbh->prepare($statement)
or die "Can't prepare $statement: $dbh->errstr\n";


for ( my $j=0; $j < @ncbi_internalnode_list; $j++ ) {
	if ( defined ( $mapped_node{ $ncbi_internalnode_list[$j] } ) ) {
		my $rv = $sth->execute( $ncbi_internalnode_list[$j] );
		while(my @row = $sth->fetchrow_array) {
			if ( defined($ncbi_hash{ $row[0] }) ) {
				print "map $row[0] ($row[1]) to $mapped_node{ $ncbi_internalnode_list[$j] } \n";
				
				my $pq_id;
				
				$pq_id = $dbh->selectrow_array("SELECT pq_id FROM pq WHERE tax_id = $row[0] ");
				
				if ( !($pq_id) ) {
					$dbh->do( "INSERT INTO pq (taxon_name, tax_id) VALUES (?, ?) ", undef, $row[1], $row[0] );
					$pq_id = $dbh->last_insert_id(undef,undef,undef,undef,{sequence=>'pq_id_seq'});
				}
				
				$dbh->do( "DELETE FROM pq_subtree WHERE pq_id = ? AND tree_id = ? ", undef, $pq_id, $tree_id );
				$dbh->do( "INSERT INTO pq_subtree (pq_id, tree_id, node_id) VALUES (?, ?, ?) ", undef, $pq_id, $tree_id, $mapped_node{ $ncbi_internalnode_list[$j] } );
				
				delete($ncbi_hash{ $row[0] });
			}
		}
	}
}

print "\n";




my $sc = $dbh->disconnect;
exit;
 

 
# Connect to Postgres using DBI
#==============================================================
sub ConnectToPg {
 
    my ($cstr, $user, $pass) = @_;
  
    $cstr = "DBI:Pg:dbname="."$cstr";
    #$cstr .= ";host=dev.nescent.org";
  
    my $dbh = DBI->connect($cstr, $user, $pass, {PrintError => 0, RaiseError => 1});
    $dbh || &error("DBI connect failed : ",$dbh->errstr);
 
    return($dbh);
}
