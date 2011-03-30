#!/usr/bin/perl

# Searches for the MRCA on a phylogeny based on a list of taxon names. 
# Returns all the descendants of the MRCA, including taxa that were 
# missing from the phylogeny but that descend from a classification rank that  
# maps to the subtree with the MRCA at its origin. Additionally, it returns
# taxa from the classification that map to the direct ancestor of the MRCA 

use strict;
use DBI;

# check that the right number of arguments are listed
die "Input error! Usage: perl query_vtol.pl tree_id <list of taxa>
e.g.: perl query_taxa.pl 9365 'Dipelta floribunda' 'Zabelia biflora' \n" if (@ARGV < 2);

my $tree_id = shift;
my @taxonlabels = @ARGV;

my $list_size = $#taxonlabels;
$list_size++;

my $list_string = join ("', '", @taxonlabels );
$list_string = "'$list_string'";

# Fill in the database name and access credentials
my $database = "";
my $username = "";
my $password = "";
my $dbh = &ConnectToPg($database, $username, $password);

print "\nFind all leaf names that descend from the MRCA of ($list_string) in tree $tree_id:\n\n";

my $statement = "SELECT nds.node_id, nds.node_label  
FROM nodes nds JOIN nodes inc ON (nds.left_id BETWEEN inc.left_id AND inc.right_id)
WHERE nds.tree_id = $tree_id 
AND inc.tree_id = nds.tree_id  
AND (nds.right_id - nds.left_id = 1) 
AND inc.node_id = (
   SELECT ndsP.node_id  
   FROM nodes ndsC JOIN node_path npth ON (ndsC.node_id = npth.child_node_id) 
   JOIN nodes ndsP ON (npth.parent_node_id = ndsP.node_id) 
   JOIN taxon_variants tv ON (ndsC.taxon_variant_id = tv.taxon_variant_id) 
   JOIN taxa tx ON (tv.taxon_id = tx.taxon_id) 
   WHERE tx.namestring IN ($list_string)
   AND ndsC.tree_id = $tree_id 
   GROUP BY ndsP.node_id, ndsP.right_id 
   HAVING COUNT(npth.child_node_id) >= $list_size
   ORDER BY ndsP.right_id 
   LIMIT 1
)";

my $query = $dbh->prepare ($statement);
$query->execute;

for my $row (@{$query->fetchall_arrayref}) {
	my ($node_id, $label) = @$row;
	printf ( "%45s\n", $label);
}
$query->finish;

print "\nFind all mapped names that descend from the MRCA of ($list_string) in tree $tree_id:\n\n";

$statement = "SELECT nds.node_id, p.taxon_name  
FROM nodes nds JOIN nodes inc ON (nds.left_id BETWEEN inc.left_id AND inc.right_id) 
JOIN pq_subtree pqs ON (nds.node_id = pqs.node_id) 
JOIN pq p USING (pq_id) 
WHERE nds.tree_id = $tree_id 
AND inc.tree_id = nds.tree_id   
AND inc.node_id = (
   SELECT ndsP.node_id  
   FROM nodes ndsC JOIN node_path npth ON (ndsC.node_id = npth.child_node_id) 
   JOIN nodes ndsP ON (npth.parent_node_id = ndsP.node_id) 
   JOIN taxon_variants tv ON (ndsC.taxon_variant_id = tv.taxon_variant_id) 
   JOIN taxa tx ON (tv.taxon_id = tx.taxon_id) 
   WHERE tx.namestring IN ($list_string)
   AND ndsC.tree_id = $tree_id 
   GROUP BY ndsP.node_id, ndsP.right_id 
   HAVING COUNT(npth.child_node_id) >= $list_size
   ORDER BY ndsP.right_id 
   LIMIT 1
)";
 
my $query = $dbh->prepare ($statement);
$query->execute;

for my $row (@{$query->fetchall_arrayref}) {
	my ($node_id, $label) = @$row;
	printf ( "%45s\n", $label);
}
$query->finish;

print "\nFind all mapped names that *might* descend from the MRCA of ($list_string) in tree $tree_id:\n\n";

$statement = "SELECT nds.node_id, p.taxon_name, (100.0 * (ndsize.right_id - ndsize.left_id) / (nds.right_id - nds.left_id) ) as clade_span 
FROM nodes nds JOIN pq_subtree pqs ON (nds.node_id = pqs.node_id) 
JOIN pq p USING (pq_id), nodes ndsize  
WHERE nds.tree_id = $tree_id 
AND nds.left_id < ndsize.left_id 
AND nds.right_id > ndsize.right_id 
AND ndsize.node_id = (
   SELECT ndsP.node_id  
   FROM nodes ndsC JOIN node_path npth ON (ndsC.node_id = npth.child_node_id) 
   JOIN nodes ndsP ON (npth.parent_node_id = ndsP.node_id) 
   JOIN taxon_variants tv ON (ndsC.taxon_variant_id = tv.taxon_variant_id) 
   JOIN taxa tx ON (tv.taxon_id = tx.taxon_id) 
   WHERE tx.namestring IN ($list_string)
   AND ndsC.tree_id = $tree_id 
   GROUP BY ndsP.node_id, ndsP.right_id 
   HAVING COUNT(npth.child_node_id) >= $list_size
   ORDER BY ndsP.right_id 
   LIMIT 1
)
ORDER BY (nds.right_id - nds.left_id) DESC;";

my $query = $dbh->prepare ($statement);
$query->execute;

for my $row (@{$query->fetchall_arrayref}) {
	my ($node_id, $label, $clade_span) = @$row;
	printf ( "%45s %5.1f\%\n", $label, $clade_span);
}
$query->finish;


my $rc = $dbh->disconnect;




# Connect to Postgres using DBI
#==============================================================
sub ConnectToPg {
 
    my ($cstr, $user, $pass) = @_;
   
    $cstr = "DBI:Pg:dbname="."$cstr";
    # $cstr .= ";host=10.9.1.1";
   
    my $dbh = DBI->connect($cstr, $user, $pass, {PrintError => 1, RaiseError => 1});
    $dbh || &error("DBI connect failed : ",$dbh->errstr);
 
    return($dbh);
}