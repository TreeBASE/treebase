#!/usr/bin/perl


# This script takes a tree from a NEXUS file and creates a 
# table to store the tree as a classification, whereupon 
# you can build a vToL. So, for example, if you're unhappy with 
# NCBI's classification for a certain group, using the "classification_to_tree.pl" 
# script to dump the NCBI clade to a tree file, then use Mesquite 
# to edit the classification tree, then use this script to re-import it. 
# A new table is created to accomodate your edited classification 
# so as not to disturb the original NCBI classification.

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

my ($sth, $rv, $statement);

my $new_nodes_table = "my_edited_classification";
my $new_nodes_seq = "my_edited_classification_seq_taxid";
my $highest_ncbi = 1000000;
my $start_taxid = 4199; #ncbi taxid for the Dipsacales
my $division_id = 4;
my $inherited_div_flag = 1;
my $genetic_code_id = 1;
my $inherited_gc_flag = 1;
my $mitochondrial_genetic_code_id = 1;
my $inherited_mgc_flag = 1;
my $genbank_hidden_flag = 0;
my $hidden_subtree_root_flag = 0;

# highest tax_id = 869615
$dbh->do( "DELETE FROM NCBI_NAMES WHERE TAX_ID > 869615" );

$dbh->do( "DROP TABLE IF EXISTS $new_nodes_table CASCADE" );
$dbh->do( "DROP SEQUENCE IF EXISTS $new_nodes_seq " );

$statement = <<STATEMENT;
CREATE SEQUENCE $new_nodes_seq 
    START WITH $highest_ncbi
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1
STATEMENT
$dbh->do( "$statement" );

$statement = <<STATEMENT;
CREATE TABLE $new_nodes_table (
	tax_id integer DEFAULT nextval('$new_nodes_seq'::regclass) NOT NULL,
    parent_tax_id integer NOT NULL,
    rank character varying(32),
    embl_code character varying(16),
    division_id integer NOT NULL,
    inherited_div_flag integer NOT NULL,
    genetic_code_id integer NOT NULL,
    inherited_gc_flag integer NOT NULL,
    mitochondrial_genetic_code_id integer NOT NULL,
    inherited_mgc_flag integer NOT NULL,
    genbank_hidden_flag integer NOT NULL,
    hidden_subtree_root_flag integer NOT NULL,
    comments character varying(255) DEFAULT NULL::character varying,
    left_id integer,
    right_id integer
)
STATEMENT

$dbh->do( "$statement" );
$statement = "ALTER TABLE ONLY $new_nodes_table ADD CONSTRAINT $new_nodes_table" . "_pkey PRIMARY KEY (tax_id)";
$dbh->do( "$statement" );
$statement = "CREATE INDEX $new_nodes_table" . "_left_id ON $new_nodes_table USING btree (left_id)";
$dbh->do( "$statement" );
$statement = "CREATE INDEX $new_nodes_table" . "_right_id ON $new_nodes_table USING btree (right_id)";
$dbh->do( "$statement" );
$statement = "CREATE INDEX $new_nodes_table" . "_parent_tax_id ON $new_nodes_table USING btree (parent_tax_id)";
$dbh->do( "$statement" );

$dbh->do( "DROP TABLE IF EXISTS $new_nodes_table"."_path CASCADE" );
$statement = "CREATE TABLE $new_nodes_table"."_path (\n";
$statement .= <<STATEMENT;
    child_node_id bigint DEFAULT (0)::bigint NOT NULL,
    parent_node_id bigint DEFAULT (0)::bigint NOT NULL,
    distance integer DEFAULT 0 NOT NULL
)
STATEMENT
$dbh->do( "$statement" );
$statement = "CREATE INDEX $new_nodes_table" . "_path_parent_id ON $new_nodes_table"."_path USING btree (parent_node_id)";
$dbh->do( "$statement" );
$statement = "CREATE INDEX $new_nodes_table" . "_path_child_id ON $new_nodes_table"."_path USING btree (child_node_id)";
$dbh->do( "$statement" );
$statement = "CREATE INDEX $new_nodes_table" . "_path_distance ON $new_nodes_table"."_path USING btree (distance)";
$dbh->do( "$statement" );

# global statements for setting the left-right indexing
my $setleft  = $dbh->prepare("UPDATE $new_nodes_table SET left_id = ? WHERE tax_id = ?");
my $setright = $dbh->prepare("UPDATE $new_nodes_table SET right_id = ? WHERE tax_id = ?");
my $ctr;

# global statements for calculating the transitive closure
my $deletepaths = $dbh->prepare("DELETE FROM $new_nodes_table"."_path");

my $init_sql = "INSERT INTO $new_nodes_table"."_path (child_node_id, parent_node_id, distance) ";
  $init_sql .= "SELECT tax_id, parent_tax_id, 1 FROM $new_nodes_table ";
my $initialize_paths =  $dbh->prepare("$init_sql"); 

my $path_sql = "INSERT INTO $new_nodes_table"."_path (child_node_id, parent_node_id, distance) ";
  $path_sql .= "SELECT n.tax_id, p.parent_node_id, p.distance+1 ";
  $path_sql .= "FROM $new_nodes_table"."_path p, $new_nodes_table n ";
  $path_sql .= "WHERE p.child_node_id = n.parent_tax_id ";
  $path_sql .= "AND p.distance = ?";
my $calc_paths =  $dbh->prepare("$path_sql");

# get only the first tree
my $tree = shift ( @{ $forest->get_entities } );

print "###########################\n";
print $tree->get_name . "\n";
my $tree_name = $tree->get_name;

# create a new node record, which will be the root of this tree
my $tree_id = 1;
my $root_node_id = $highest_ncbi - 1;
$statement = "INSERT INTO $new_nodes_table ";
$statement .= "(tax_id, parent_tax_id, division_id, inherited_div_flag, genetic_code_id, inherited_gc_flag, mitochondrial_genetic_code_id, inherited_mgc_flag, genbank_hidden_flag, hidden_subtree_root_flag) VALUES ";
$statement .= "($root_node_id, 0, $division_id, $inherited_div_flag, $genetic_code_id, $inherited_gc_flag, $mitochondrial_genetic_code_id, $inherited_mgc_flag, $genbank_hidden_flag, $hidden_subtree_root_flag)";
$dbh->do( "$statement" );

# set a counter for the left-right indexing
$ctr = ($highest_ncbi * 100);
walktree( $tree->get_root , $root_node_id);

compute_tc();

my $rc = $dbh->disconnect;
 
exit;
 

#===================================
sub walktree {
	my $parent = shift;
	my $parent_id = shift;
	
	$setleft->execute($ctr++, $parent_id);
 
	for my $child ( @{ $parent->get_children } ) {
 
		my $branch_length;
		my $edge_support;
		my $child_id;

		if (($child->get_name) && !($child->get_name =~ m/^\d*\.?\d+$/)) {
			my $taxon_label = &detokenize( $child->get_name );
			
			my $statement = "SELECT COUNT(*) FROM ncbi_names WHERE name_txt = " . $dbh->quote( $taxon_label );
			my $totRec = $dbh->selectrow_array ($statement);

			if ($totRec > 0) {
				# there is a node name and this name already exists
				$statement = "SELECT tax_id FROM ncbi_names WHERE name_txt = " . $dbh->quote( $taxon_label ) . " LIMIT 1 ";
				$child_id = $dbh->selectrow_array ($statement);
								
				$statement = "INSERT INTO $new_nodes_table ";
				$statement .= "(tax_id, parent_tax_id, division_id, inherited_div_flag, genetic_code_id, inherited_gc_flag, ";
				$statement .= "mitochondrial_genetic_code_id, inherited_mgc_flag, genbank_hidden_flag, hidden_subtree_root_flag) ";
				$statement .= "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
				$dbh->do( "$statement", undef, $child_id, $parent_id, $division_id, $inherited_div_flag, $genetic_code_id, $inherited_gc_flag, $mitochondrial_genetic_code_id, $inherited_mgc_flag, $genbank_hidden_flag, $hidden_subtree_root_flag );

			} else {
				# there is a node name, but it doesn't exist
				$statement = "INSERT INTO $new_nodes_table ";
				$statement .= "(parent_tax_id, division_id, inherited_div_flag, genetic_code_id, inherited_gc_flag, ";
				$statement .= "mitochondrial_genetic_code_id, inherited_mgc_flag, genbank_hidden_flag, hidden_subtree_root_flag) ";
				$statement .= "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";
				$dbh->do( "$statement", undef, $parent_id, $division_id, $inherited_div_flag, $genetic_code_id, $inherited_gc_flag, $mitochondrial_genetic_code_id, $inherited_mgc_flag, $genbank_hidden_flag, $hidden_subtree_root_flag );

				$child_id = $dbh->last_insert_id(undef,undef,undef,undef,{sequence=>"$new_nodes_seq"});
				$dbh->do( "INSERT INTO ncbi_names (tax_id, name_txt, name_class) VALUES (?, ?, ?) ", undef, $child_id, $taxon_label, 'scientific name' );
			}

		} else {
			# there is no node name for this node
			$statement = "INSERT INTO $new_nodes_table ";
			$statement .= "(parent_tax_id, division_id, inherited_div_flag, genetic_code_id, inherited_gc_flag, ";
			$statement .= "mitochondrial_genetic_code_id, inherited_mgc_flag, genbank_hidden_flag, hidden_subtree_root_flag) ";
			$statement .= "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			$dbh->do( "$statement", undef, $parent_id, $division_id, $inherited_div_flag, $genetic_code_id, $inherited_gc_flag, $mitochondrial_genetic_code_id, $inherited_mgc_flag, $genbank_hidden_flag, $hidden_subtree_root_flag );

			$child_id = $dbh->last_insert_id(undef,undef,undef,undef,{sequence=>"$new_nodes_seq"});
			$dbh->do( "INSERT INTO ncbi_names (tax_id, name_txt, name_class) VALUES (?, ?, ?) ", undef, $child_id, "Unnamed Rank $child_id", 'scientific name' );
		}
   
		walktree( $child, $child_id );
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
 
   $deletepaths->execute();
   $initialize_paths->execute();
 
   my $dist = 1;
   my $rv = 1;
   while ($rv > 0) {
        $rv = $calc_paths->execute($dist);
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
