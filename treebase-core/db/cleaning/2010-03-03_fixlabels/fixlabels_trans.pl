#!/usr/bin/perl

use strict;
use warnings;
use DBI;

# This script is intended to remove all taxonlabel records that are duplicates within the 
# same study. We start by getting a list of all studies. Then for each study_id, we get 
# a unique list of taxonlabels, picking from among the duplicates one taxonlabel_id that 
# will survive the culling; then we pick out a taxonvariant_id so that we can be sure that 
# the surviving taxonlabel has a mapping to taxonvariants. For each taxonlabel_id that
# is destined to be deleted, we first update all tables that use it as a foreign key 
# and modify them to use the taxonlabel_id of the surviving record. Note that we cannot
# just update the corresponding record in sub_taxonlabel because that would violate the 
# constraint that taxonlabel_id in sub_taxonlabel be unique. Instead, we delete any 
# sub_taxonlabel records that have a taxonlabel_id that is determined to be redundant. 

my $database = "";
my $username = "treebase_app";
my $password = "";


my $dbh = &ConnectToPg($database, $username, $password);

# Let's start by counting the number of redundant taxonlabel_ids:

my $count = q|SELECT COUNT(*) FROM |; 
$count .= q|(SELECT study_id, taxonlabel, COUNT(taxonlabel_id) |;
$count .= q|FROM taxonlabel WHERE study_id IS NOT NULL |;
$count .= q|GROUP BY study_id, taxonlabel HAVING COUNT(taxonlabel_id) > 1 |;
$count .= q|ORDER BY COUNT(taxonlabel_id) DESC) AS countrecs |;

my $totRec = $dbh->selectrow_array ($count);
print "The database starts with $totRec redundant records.\n";

# Assuming that the study_id column in taxonlabel is accurate, 
# lets get a distinct list of all study_ids

my $query = q|SELECT DISTINCT study_id FROM taxonlabel WHERE study_id IS NOT NULL |;

my $sth = $dbh->prepare($query)
or die "Can't prepare $query: $dbh->errstr\n";	
my $rv = $sth->execute
or die "can't execute the query: $sth->errstr\n";

my @studyids; #unique list of all study_ids, as taken from the taxonlabel table

while(my @row = $sth->fetchrow_array) {
	push(@studyids, $row[0]);
}
my $rd = $sth->finish;

$query = qq|SELECT taxonlabel, MIN(taxonlabel_id), MAX(taxonvariant_id)  FROM taxonlabel |;
$query .= qq|WHERE study_id = ? GROUP BY taxonlabel HAVING COUNT(taxonlabel_id) > 1 |;
my $sth_getuniques = $dbh->prepare($query) or die "Can't prepare $query: $dbh->errstr\n";	

my $file ="output.txt";
open(OUTPUT,">$file") || die "Content-type: text/html\n\nCannot open $file!";

foreach my $stid ( @studyids ) {
	my %taxonlabels;

	# get a unique list of taxonlabel values and store the lowest taxonlabel_id,
	# and the highest taxonvariant_id

	my $rv = $sth_getuniques->execute( $stid );
	while(my @row = $sth_getuniques->fetchrow_array) {
		$taxonlabels{ $row[0] } = [ $row[1], $row[2] ];
	}
	my $rd = $sth_getuniques->finish;
	
	# get a list of taxonlabel_ids that need to be deleted (ie they are redundant)
	$query = qq|SELECT taxonlabel_id FROM taxonlabel WHERE taxonlabel = ? AND study_id = ? AND taxonlabel_id <> ? |;
	my $sth_getdups = $dbh->prepare($query) or die "Can't prepare $query: $dbh->errstr\n";	
	
	foreach my $uniquetaxonlabel ( keys(%taxonlabels) ) {
		
		eval {
			my $dup_taxlabid;
			my $rv = $sth_getdups->execute( $uniquetaxonlabel, $stid, $taxonlabels{$uniquetaxonlabel}[0] );
			while(my @row = $sth_getdups->fetchrow_array) {
				$dup_taxlabid = $row[0];
				
				# first check that the sub_taxonlabel table has a record for the surviving taxonlabel_id
				
				my $sub_taxonlabel_count = "SELECT COUNT(*) FROM sub_taxonlabel WHERE taxonlabel_id = " . $taxonlabels{$uniquetaxonlabel}[0] ; 
				my $sub_totRec = $dbh->selectrow_array ($sub_taxonlabel_count);
				
				if ( $sub_totRec == 1) {
	 
					# now that we have a taxonlabel_id that can be deleted, let's update all records in 
					# other tables that use this id so that they use the taxonlabel_id of the surviving taxonlabel record

					# we are ready to delete the sub_taxonlabel record that has our $dup_taxlabid
					$dbh->do( "DELETE FROM sub_taxonlabel WHERE taxonlabel_id = ? ", undef, $dup_taxlabid );

					# let's update the tables that have a FK with $dup_taxlabid so that they point to the surviving record
					foreach my $tble ( 'phylotreenode', 'rowsegment', 'taxonlabelgroup_taxonlabel', 'taxonlabelset_taxonlabel', 'matrixrow' ) {
						$dbh->do( "UPDATE $tble SET taxonlabel_id = ? WHERE taxonlabel_id = ? ", undef, $taxonlabels{$uniquetaxonlabel}[0], $dup_taxlabid );
					}

					# now we are ready to delete this redundant taxonlabel record
					$dbh->do( "DELETE FROM taxonlabel WHERE taxonlabel_id = ? ", undef, $dup_taxlabid );
					
					# optionally: print out a list of all deleted taxonlabel records
					print OUTPUT "study $stid: taxonlabel $uniquetaxonlabel: $dup_taxlabid deleted in favor of $taxonlabels{$uniquetaxonlabel}[0]\n";
				} elsif ($sub_totRec == 0) {
					print "Whoops... sub_taxonlabel lacks a record for the surviving taxonlabel_id $taxonlabels{$uniquetaxonlabel}[0] \n";
				} else {
					print "Whoops... I didn't expect sub_taxonlabel to have $sub_totRec records with taxonlabel_id $taxonlabels{$uniquetaxonlabel}[0] \n";
					print "     > $sub_taxonlabel_count\n";
				}
				
			}
			my $rd = $sth_getdups->finish;
			
			# Make sure that the surviving taxonlabel has a taxonvariant_id
			$dbh->do( "UPDATE taxonlabel SET taxonvariant_id = ? WHERE taxonlabel_id = ? ", undef, $taxonlabels{$uniquetaxonlabel}[1], $taxonlabels{$uniquetaxonlabel}[0] );
		
			# If no errors so far, let's commit this set of changes
			$dbh->commit();
		};

		if ($@) {
		   warn "Failed to fix $uniquetaxonlabel: $@\n";
		   $dbh->rollback();
		}

	}
}

close(OUTPUT);

$totRec = $dbh->selectrow_array ($count);
print "The database ends with $totRec redundant records.\n";


my $rc = $dbh->disconnect;



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


