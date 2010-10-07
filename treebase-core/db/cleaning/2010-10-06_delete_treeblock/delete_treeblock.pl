#!/usr/bin/perl

use strict;
use DBI;

# This script is intended to delete a treeblock, cascading to all dependent tables

my $database = "";
my $username = "treebase_app";
my $password = "";

my $treeblock_id = shift;

# Number of nodes to delete at a time
my $sets = 1000;

# check that the treeblock_id looks like a number:
if ( $treeblock_id =~ m/^\d+$/ ) {

	my $dbh = &ConnectToPg($database, $username, $password);
		
	my $count = "SELECT COUNT(*) FROM treeblock WHERE treeblock_id = $treeblock_id ";
	my $totRec = $dbh->selectrow_array ($count);

	# check to see that the treeblock in question exists
	if ($totRec == 1) {

		$count = "SELECT COUNT(*) FROM phylotree WHERE treeblock_id = $treeblock_id ";
		$totRec = $dbh->selectrow_array ($count);

		my $a;
		do {
			print "There are $totRec trees in treeblock $treeblock_id, does that sound right? [yn]\n";
			$a = <STDIN>;
			chop $a;
		} until (($a eq "y") || ($a eq "n"));
		
		# offer a chance to cancel
		if ($a eq "y") {

			# run the whole thing in a single transaction 
			eval {
				
				# phylotreenode and phylotree have mutual FK constraints 
				# first NULL the phylotree.rootnode_id to remove constraint
				$dbh->do( "UPDATE phylotree SET rootnode_id = DEFAULT WHERE treeblock_id = ?", undef, $treeblock_id );

				# since we've been getting a time-out when deleting phylonodes, lets delete them in 
				# batches of $sets

				my $statement = "SELECT COUNT(*) FROM phylotreenode WHERE phylotree_id IN (
					SELECT phylotree_id FROM phylotree WHERE treeblock_id = $treeblock_id ) ";				
				$totRec = $dbh->selectrow_array ($statement);
				
				print "There are $totRec phylotreenode records that we need to delete.\n";
				print "Lets attempt to do this ". int($totRec/$sets) ." times in sets of $sets records\n";
				
				$statement = "DELETE FROM phylotreenode WHERE phylotree_id IN (
				   SELECT phylotree_id FROM phylotree WHERE treeblock_id = ? LIMIT ? 
				)";
				
				my $delete_phylonodes =  $dbh->prepare("$statement");

				foreach my $cnt (1 .. int($totRec/$sets) ) {
					$delete_phylonodes->execute( $treeblock_id, $sets );
					print "Deletion $cnt for batch of $sets phylotreenode records \n";
				}
				# one more for good measure
				$delete_phylonodes->execute( $treeblock_id, $totRec );

				# taxonlabels are referenced by nodes, matrix rows, taxon blocks *and* submissions 
				# let's remove the connection to submissions. This assumes that there are no matrices 
				# or other treeblocks that will continue to reference these taxonlabels
				$statement = "DELETE FROM sub_taxonlabel WHERE taxonlabel_id IN (
				   SELECT DISTINCT taxonlabel_id 
				   FROM taxonlabelset_taxonlabel JOIN taxonlabelset USING (taxonlabelset_id) 
				   JOIN treeblock USING (taxonlabelset_id) 
				   WHERE treeblock_id = ? 
				)";
				$dbh->do( $statement, undef, $treeblock_id );

				# remove the many-to-many join between taxonlabelset and taxonlabel
				$statement = "DELETE FROM taxonlabelset_taxonlabel WHERE taxonlabelset_id IN (
				   SELECT taxonlabelset_id FROM treeblock WHERE treeblock_id = ? 
				)";
				$dbh->do( $statement, undef, $treeblock_id );

				# delete taxonlabel records after having deleted the taxonlabelset_taxonlabel records
				$statement = "DELETE FROM taxonlabel WHERE taxonlabel_id IN (
				   SELECT tl.taxonlabel_id 
				   FROM taxonlabel tl LEFT JOIN taxonlabelset_taxonlabel USING (taxonlabel_id) 
				   WHERE study_id = (SELECT DISTINCT study_id FROM phylotree WHERE treeblock_id = ?) 
				   AND taxonlabelset_id IS NULL 
				   AND NOT EXISTS (SELECT 1 FROM matrixrow mr WHERE mr.taxonlabel_id = tl.taxonlabel_id) 
				   AND NOT EXISTS (SELECT 1 FROM phylotreenode ptn WHERE ptn.taxonlabel_id = tl.taxonlabel_id) 
				)";
				$dbh->do( $statement, undef, $treeblock_id );
				
				# Before we delete the taxonlabelset, we need to remove the FK constraint
				# with treeblock
				$statement = "UPDATE treeblock SET taxonlabelset_id = DEFAULT WHERE treeblock_id = ?";
				$dbh->do( $statement, undef, $treeblock_id );

				# Now delete the taxonlabelset 
				$statement = "DELETE FROM taxonlabelset WHERE taxonlabelset_id IN (
				   SELECT DISTINCT taxonlabelset_id 
				   FROM taxonlabelset tls LEFT JOIN taxonlabelset_taxonlabel tltl USING (taxonlabelset_id) 
				   WHERE study_id = (
					  SELECT DISTINCT study_id FROM phylotree  
					  WHERE treeblock_id = ?
					  ) 
				   AND tltl.taxonlabel_id IS NULL 
				   AND NOT EXISTS (SELECT 1 FROM treeblock tb WHERE tb.taxonlabelset_id = tls.taxonlabelset_id AND tb.treeblock_id <> ?) 
				   AND NOT EXISTS (SELECT 1 FROM matrix mx WHERE mx.taxonlabelset_id = tls.taxonlabelset_id ) 
				)";
				$dbh->do( $statement, undef, $treeblock_id, $treeblock_id );

				$dbh->do( "DELETE FROM phylotree WHERE treeblock_id = ?", undef, $treeblock_id );
				$dbh->do( "DELETE FROM sub_treeblock WHERE treeblock_id = ?", undef, $treeblock_id );
				$dbh->do( "DELETE FROM treeblock WHERE treeblock_id = ?", undef, $treeblock_id );

				# If no errors so far, let's commit
				$dbh->commit();
				print "Congratulations: treeblock_id $treeblock_id has been deleted and the deletes committed\n";
			};
	
			if ($@) {
			   warn "Failed to delete treeblock_id $treeblock_id: $@\n";
			   $dbh->rollback();
			   print "rollback!!\n";
			}
			
		} else {
			print "Deletion cancelled on user request\n";
		}
	} else {
		print "$totRec record(s) found, there should be only one.\n";
	}
	
	my $rc = $dbh->disconnect;
} else {
	print "The treeblock_id ($treeblock_id) does not look like a number.\n";
}


# Connect to Postgres using DBI
#==============================================================
sub ConnectToPg {

	my ($cstr, $user, $pass) = @_;
	
	$cstr = "DBI:Pg:dbname="."$cstr";
	# uncomment this to run against the server at NESCent:
	$cstr .= ";host=treebasedb-dev.nescent.org";

	
	my $dbh = DBI->connect($cstr, $user, $pass, {AutoCommit => 0, PrintError => 1, RaiseError => 1});
	$dbh || &error("DBI connect failed : ",$dbh->errstr);
	 
	return($dbh);
}


