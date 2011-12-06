#!/usr/bin/perl

use strict;
use DBI;

# This script is intended to delete orphaned taxonlabelsets. As of Dec 6 2012, orphans 
# were being generated as a result of a bug. When our delete-treeblock function inadvertently 
# failed to include a cleanup step to delete related, but now orphaned, taxonlabelset records. 
# The result are studies that generate a "yikes" error when you try to get a list of taxa. 


my $database = "treebasedev";
my $username = "treebase_app";
my $password = "";
my $host = "treebasedb-dev.nescent.org";


my $dbh = &ConnectToPg($database, $username, $password, $host);

# start by asking how many taxonlabelset records are orphaned

my $recs = "SELECT count(*) FROM 
taxonlabelset tls LEFT JOIN 
matrix mx ON (tls.taxonlabelset_id = mx.taxonlabelset_id) LEFT JOIN 
treeblock tb ON (tls.taxonlabelset_id = tb.taxonlabelset_id) JOIN 
study s ON (tls.study_id = s.study_id) JOIN 
studystatus ss USING (studystatus_id) 
WHERE mx.matrix_id IS NULL 
AND tb.treeblock_id IS NULL ";
my $totRec = $dbh->selectrow_array ($recs);

print "$totRec orphaned taxonlabelset records need to be deleted\n\n";

# get a list of all these taxonlabelset IDs

my $statement = "SELECT tls.taxonlabelset_id FROM 
taxonlabelset tls LEFT JOIN 
matrix mx ON (tls.taxonlabelset_id = mx.taxonlabelset_id) LEFT JOIN 
treeblock tb ON (tls.taxonlabelset_id = tb.taxonlabelset_id) JOIN 
study s ON (tls.study_id = s.study_id) JOIN 
studystatus ss USING (studystatus_id) 
WHERE mx.matrix_id IS NULL 
AND tb.treeblock_id IS NULL ";

my @orphaned;
my $orphanedlist = $dbh->prepare($statement) or die "Can't prepare $statement: $dbh->errstr\n";	
$orphanedlist->execute;
while(my @row = $orphanedlist->fetchrow_array) {
	push(@orphaned, $row[0]);
}

foreach my $orph ( @orphaned ) {
	print "preparing to delete $orph\n";
	
	# run the whole thing in a single transaction 
	eval {

		# First delete the records that reference this taxonlabelset
		$dbh->do( "DELETE FROM taxonlabelset_taxonlabel WHERE taxonlabelset_id = ?", undef, $orph );

		# Next delete the taxonlabelset
		$dbh->do( "DELETE FROM taxonlabelset WHERE taxonlabelset_id = ?", undef, $orph );

		# If no errors so far, let's commit
		$dbh->commit();
	};

	if ($@) {
	   warn "Failed to delete taxonlabelset_id $orph: $@\n";
	   $dbh->rollback();
	   print "rollback!!\n";
	}

}

$totRec = $dbh->selectrow_array ($recs);

print "\n$totRec orphaned taxonlabelset records need to be deleted\n";

exit;



# Connect to Postgres using DBI
#==============================================================
sub ConnectToPg {

	my ($cstr, $user, $pass, $host) = @_;
	
	$cstr = "DBI:Pg:dbname="."$cstr";
	$cstr .= ";host=$host" if ($host);
	
	my $dbh = DBI->connect($cstr, $user, $pass, {AutoCommit => 0, PrintError => 1, RaiseError => 1});
	$dbh || &error("DBI connect failed : ",$dbh->errstr);
	 
	return($dbh);
}
