#!/usr/bin/perl

use strict;
use DBI;

# This script is intended to update citation information from a UTF8-encoded tab-separated 
# export of EndNote citation metadata. The records are identified by the legacy study_id. 
# Author names are unaffected. The order of the columns in the input file is as follows:
#
# legacyID, authors, year, title, journal, vol, num, pages, doi, url, keywords, abstract, citation_id
#
# To use, edit the database credentials, and then type:
#
#      perl update_citations.pl citationdata.txt
#
# ... where citationdata.txt is the citation metadata file. 


my $database = "";
my $username = "";
my $password = '';
my $host = "treebasedb-dev.nescent.org";

my $dbh = &ConnectToPg($database, $username, $password, $host);

my $file  = shift @ARGV;
die "Need file of citations in tab-separated text format with UTF8 encoding" if not -f $file;

my $statement = "SELECT st.study_id, cn.publishyear, cn.title, cn.journal, ";
$statement .= "cn.volume, cn.issue, cn.pages, cn.doi, cn.url, cn.keywords, ";
$statement .= "cn.abstract, cn.citation_id ";
$statement .= "FROM study st JOIN citation cn USING (citation_id) ";
$statement .= "WHERE st.tb_studyid = ? ";
my $existing = $dbh->prepare("$statement");

open(INPUT, "<:utf8", "$file") || die "Cannot open output.txt!: $!";

my (@field, @value);
while( my $line = <INPUT>) {
	chomp($line);
	my ($legacyID, $authors, $year, $title, $journal, $vol, $num, $pages, $doi, $url, $keywords, $abstract) = split(/\t/,$line);
	my $totRec = $dbh->selectrow_array ("SELECT count(*) FROM study s WHERE tb_studyid = ?", undef, $legacyID);
	if ($totRec == 1) {

		$existing->execute($legacyID) or die "can't execute the query: $existing->errstr\n";
		my($e_study_id, $e_year, $e_title, $e_journal, $e_vol, $e_num, $e_pages, $e_doi, $e_url, $e_keywords, $e_abstract, $citation_id) = $existing->fetchrow_array;
		$existing->finish;
		
		$title .= '.' if ($title =~ m/[a-z0-9]$/);

		@field = ();
		@value = ();
		if ( $e_pages eq "" && $pages ne "" ) {

			push (@field, 'publishyear = ?');
			push (@value, $year);
			
			push (@field, 'title = ?');
			push (@value, $title);

			push (@field, 'journal = ?');
			push (@value, $journal);

			if ($vol) {
				push (@field, 'volume = ?');
				push (@value, $vol);
			}

			if ($num) {
				push (@field, 'issue = ?');
				push (@value, $num);
			}

			push (@field, 'pages = ?');
			push (@value, $pages);

			if ($doi) {
				push (@field, 'doi = ?');
				push (@value, $doi);
			}

			if ($url) {
				push (@field, 'url = ?');
				push (@value, $url);
			}

			if ( ($e_keywords eq 'in press') || ($keywords ne "") ) {
				push (@field, 'keywords = ?');
				push (@value, $keywords);
			}

			if ($abstract) {
				push (@field, 'abstract = ?');
				push (@value, $abstract);
			}			
			
		} elsif ($e_doi eq "" && $doi ne "" ) {
			# existing metadata complete except for the DOI

			push (@field, 'doi = ?');
			push (@value, $doi);

			if ($url && $e_url eq "") {
				push (@field, 'url = ?');
				push (@value, $url);
			}

			if ( ($e_keywords eq 'in press') || ($keywords ne "") ) {
				push (@field, 'keywords = ?');
				push (@value, $keywords);
			}

		} elsif ($e_url eq "" && $url ne "" ) {
			# existing metadata complete except for the URL

			push (@field, 'url = ?');
			push (@value, $url);

			if ( ($e_keywords eq 'in press') || ($keywords ne "") ) {
				push (@field, 'keywords = ?');
				push (@value, $keywords);
			}

		} else {
			if ( ($e_keywords eq 'in press') || ($keywords ne "") ) {
				push (@field, 'keywords = ?');
				push (@value, $keywords);
			}
		}
		
		if ( $field[0] ) {
			$statement = "UPDATE citation SET ";
			$statement .= join (', ', @field);
			$statement .= " WHERE citation_id = ? ";

			eval {
				push(@value, $citation_id);
				my $rows_affected = $dbh->do( "$statement", undef, @value );
			
				if ($rows_affected == 1) {
					$dbh->commit();
					print "$legacyID, $e_study_id. $statement $citation_id\n";
				} elsif($rows_affected > 1) {
					$dbh->rollback();
			   		print "*************************** Rollback on $legacyID, $e_study_id. $statement $citation_id\n";
				} else {
					print "*************************** FAILED: $legacyID, $e_study_id. $statement $citation_id\n";
				}
			};
		
			if ($@) {
			   warn "Failed to update legacy_id $legacyID, study_id, $e_study_id, citation_id $citation_id: $@\n";
			   $dbh->rollback();
			   print "*************************** Rollback on $legacyID !!\n";
			}

		} else {
			print "$legacyID, $e_study_id. No changes: $e_journal $e_vol $e_pages $e_doi\n";
		}
		
	} else {
		print "*************************** For $legacyID number of records = $totRec \n";
	}
}

close(INPUT);

my $rc = $dbh->disconnect;

exit;


# Connect to Postgres using DBI
#==============================================================
sub ConnectToPg {

	my ($cstr, $user, $pass, $host) = @_;
	
	$cstr = "DBI:Pg:dbname="."$cstr";
	$cstr .= ";host=$host" if ($host);
	
	my $dbh = DBI->connect($cstr, $user, $pass, {pg_enable_utf8 => 1, AutoCommit => 0, PrintError => 1, RaiseError => 1});
	$dbh || &error("DBI connect failed : ",$dbh->errstr);
	 
	return($dbh);
}


