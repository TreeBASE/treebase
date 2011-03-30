#!/usr/bin/perl

# Script exports a clade from the a classification (NCBI's by  
# default) to a NEXUS style tree file. This is useful if you'd like to 
# use Mesquite to improve the classification -- e.g. you can 
# create unnamed ranks that are not present in NCBI, or can 
# add in missing species. After editing the classification tree, you
# can re-import it into the database to use with your vToL

use strict;
use DBI;

my $rootid = 999999;
my $new_nodes_table = "my_edited_classification";

my $file = "ncbi_tree_out.tre";
open (OUTPUT, ">$file") || die "Cannot open $file!: $!";

print OUTPUT "#NEXUS\n\nBEGIN TREES;\n\n";

# Fill in the database name and access credentials
my $database = "";
my $username = "";
my $password = "";
my $dbh = &ConnectToPg($database, $username, $password);

my $count = "SELECT COUNT(*) FROM ncbi_names NATURAL INNER JOIN $new_nodes_table ";
$count .= "WHERE parent_tax_id = ? AND name_class = 'scientific name'";
#$count .= "WHERE parent_tax_id = ? ";


my $select = "SELECT tax_id, name_txt FROM ncbi_names NATURAL INNER JOIN $new_nodes_table ";
$select .= "WHERE parent_tax_id = ? AND name_class = 'scientific name'";
#$select .= "WHERE parent_tax_id = ? ";

my $children = $dbh->prepare($select);

print OUTPUT "\tTREE mytree = ";

&walktree($dbh, $rootid);

print OUTPUT ";\n";

my $rc = $dbh->disconnect;

print OUTPUT "\nEND;\n";
exit;


# walktree
#==============================================================
sub walktree {
    my $dbh = shift;
    my $parent_id = shift;
    my $parent_name = shift;
    

    my $totRec = $dbh->selectrow_array ($count, undef, $parent_id);
    
    if ($totRec > 0) {
        # still more children -- print a new open parenthesis
        print OUTPUT "(";
    } else {
        # no more children -- print the OTU
        print OUTPUT tokenize("$parent_name");
    }
    
    # get some children
    $children->execute($parent_id);
        
    my $br = 0;
    for my $row (@{$children->fetchall_arrayref}) {
        $br++;
        my ($child_id, $child_name) = @$row;
        
        #treat each child as a parent and walk the tree some more
        walktree($dbh, $child_id, $child_name);
        print OUTPUT "," if ($br < $totRec);
    }
    
    if ($totRec > 0) {
        print OUTPUT ")";
        print OUTPUT tokenize("$parent_name");
    }
}

# tokenize
#==============================================================
sub tokenize {

    my $token = shift;
    
    $token =~ s/\'/\"/g;
    
    if ($token =~ m/[-\/\?\<\>\*\%\&\$\#\@\!\"\(\)]/) {
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
