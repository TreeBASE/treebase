require CIPRES::TreeBase::VeryBadORM;

sub CIPRES::TreeBase::TreeBaseObjects::set_db_connection { 
    my $class = shift; 
    CIPRES::TreeBase::VeryBadORM->set_db_connection(@_); 
}

####################################################################################################
package Analysis;
CIPRES::TreeBase::VeryBadORM->register();
our %r_attr = ( 'analysissteps' => 'AnalysisStep' );

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return $self->name ? qq{$s "} . $self->name . qq{"} : $s;
}

sub recurse {
  my $self = shift();
  for my $as ($self->analysissteps) {
    $as->dump(@_);
  }
}

# Convenience alias
sub analysis_steps {
  return $_[0]->analysissteps;
}

sub analyzed_matrices {
  my $self = shift;
  unless ($self->{analyzed_matrices}) {
    my @matrices;
    for my $as ($self->analysis_steps) {
      for my $ad ($as->analyzeddata) {
	    my $matrix = $ad->matrix;
	    push @matrices, $matrix if defined $matrix;
      }
    }
    $self->{analyzed_matrices} = \@matrices;
  }
  return @{$self->{analyzed_matrices}};
}

sub analyzed_trees {
  my $self = shift;
  unless ($self->{analyzed_trees}) {
    my @trees;
    for my $as ($self->analysis_steps) {
      for my $ad ($as->analyzeddata) {
	    my $tree = $ad->tree;
	    push @trees, $tree if defined $tree;
      }
    }
    $self->{analyzed_trees} = \@trees;
  }
  return @{$self->{analyzed_trees}};
}

sub analyzed_data {
  my $self = shift;
  return $self->analyzed_matrices, $self->analyzed_trees;
}

sub consistent {
    my $self = shift;
    my %attr = @_;
    my %tlset_ids;
    my $OK = 1;
    for my $tree ($self->analyzed_trees) {
		if ($tree->treeblock && $tree->treeblock->taxonlabelset) {
			$tlset_ids{$tree->treeblock->taxonlabelset->id} ++;
		}
    }
    for my $matrix ($self->analyzed_matrices) {
		if ($matrix->taxonlabelset) {
			$tlset_ids{$matrix->taxonlabelset->id} ++;
		}
    }
    my @ids = keys(%tlset_ids);
    if (@ids == 1) {
      # OK
    } elsif (@ids == 0) {
      push @{$attr{warnings}},  "Analysis " . $self->id . " contains no taxonlabelsets\n";
      $OK = 0;
    }

    if ($self->analyzed_matrices == 0) {
      push @{$attr{warnings}},  "Analysis " . $self->id . " contains no matrices\n";
	$OK = 0;
    }
    if ($self->analyzed_trees == 0) {
      push @{$attr{warnings}},  "Analysis " . $self->id . " contains no trees\n";
	$OK = 0;
    }

    # TODO: check to see if the tree's taxonlabelset is a subset of the matrix's

    return $OK;
}

####################################################################################################
package AnalysisStep;
CIPRES::TreeBase::VeryBadORM->register();
our %r_attr = qw(analyzeddata AnalyzedData);

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return $self->name ? qq{$s "} . $self->name . qq{"} : $s;
}

sub recurse {
  my $self = shift();
  for my $ad ($self->analyzeddata) {
    $ad->dump(@_);
  }
}

####################################################################################################
package AnalyzedData;
CIPRES::TreeBase::VeryBadORM->register();

sub recurse {
  my $self = shift();
  if (my $matrix = $self->matrix) {
    $matrix->dump(@_);
  } elsif (my $tree = $self->phylotree) {
    $tree->dump(@_);
  }
}

# Convenience alias
sub tree {
  return $_[0]->phylotree;
}

####################################################################################################
package Citation;
our %r2_attr = (
	'authors' => ['citation_author', 'Person', 'authors_person_id']
);
CIPRES::TreeBase::VeryBadORM->register();

sub recurse {
  my $self = shift();
  for my $author ($self->authors) {
    $author->dump(@_);
  }
}

sub consistent {
    my $self = shift();
    my %attr = @_;
    my %author_count;
    my $OK = 1;
    for my $author ($self->authors) {
		if (++$author_count{$author->id} == 2) {
			push @{$attr{warnings}},  
				"Citation " 
				. $self->id 
				. " contains author " 
				. $author->id 
				. " multiple times.\n";
			$OK = 0;
		}
    }
    return $OK;
}

####################################################################################################
package Matrix;
CIPRES::TreeBase::VeryBadORM->register();
our %r_attr = (
	'rows'    => 'MatrixRow',
	'columns' => 'MatrixColumn',
);

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return "$s (was " . $self->tb_matrixid . " " . $self->title . ")";
}

sub recurse {
  my $self = shift();
  my $mk = $self->matrixkind;
  $mk->dump(@_) if $mk;
  for my $ad ($self->rows) {
    $ad->dump(@_);
  }
}

sub taxonlabels {
  my $self = shift;
  unless ($self->{taxonlabels}) {
    $self->{taxonlabels} = [ map { $_->taxonlabel } $self->rows ];
  }
  return @{$self->{taxonlabels}};
}

sub nexusfile {
    my $self = shift;
    return $self->{nexusfile} if $self->{nexusfile};
    my $nfn = $self->nexusfilename;
    return $self->{nexusfile} = NexusFile->new_by_name($nfn);
}

sub consistent {
    my $self = shift;
    my %attr = @_;
    my $OK = 1;
    $OK &&= $attr{attr_check}->($self, 'Study', \%attr);
    $OK &&= $attr{attr_check}->($self, 'TaxonLabelSet', \%attr);

    my $rows = $self->rows;
    unless ($rows == $self->ntax) {
	push @{$attr{warnings}},  "Matrix " . $self->id . " has $rows rows but ntax=" . $self->ntax;
	$OK = 0;
    }
    if ($rows == 0) {
	push @{$attr{warnings}},  "Matrix " . $self->id . " has no rows\n";
	$OK = 0;
    }

    my $columns = $self->columns;
    unless ($columns == $self->nchar) {
	push @{$attr{warnings}},  "Matrix " . $self->id . " has $columns rows but nchar=" . $self->nchar;
	$OK = 0;
    }
    if ($columns == 0) {
	push @{$attr{warnings}},  "Matrix " . $self->id . " has no columns\n";
	$OK = 0;
    }

    unless (defined $self->matrixkind) {
      push @{$attr{warnings}},  "Matrix " . $self->id . " has null matrixkind";
      $OK = 0;
    }

    unless (defined $self->title) {
      push @{$attr{warnings}},  "Matrix " . $self->id . " has null title";
      $OK = 0;
    }

    return $OK;
}

####################################################################################################
package MatrixColumn;
CIPRES::TreeBase::VeryBadORM->register();

####################################################################################################
package MatrixKind;
CIPRES::TreeBase::VeryBadORM->register();

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return $self->description ? qq{$s "} . $self->description . qq{"} : $s;
}

####################################################################################################
package MatrixRow;
CIPRES::TreeBase::VeryBadORM->register();
sub recurse {
  my $self = shift();
  my $tl = $self->taxonlabel;
  $tl->dump(@_) if $tl;
}

sub consistent {
    my $self = shift;
    my %attr = @_;
    return $attr{attr_check}->($self, 'Matrix', \%attr);
}

####################################################################################################
package NexusFile;
CIPRES::TreeBase::VeryBadORM->register();
our %subobject = ('study' => 'Study');

sub table { "Study_NexusFile" } # XXX check to see if this casing makes sense

sub new_by_name {
    my ($class, $filename, $study_id) = @_;
    my $self = bless { 
    	'filename' => $filename, 
		'study_id' => $study_id,
		'id'       => "$study_id,$filename",
		'reified'  => 1, # don't try to retrieve the clob!
    } => $class;
    return $self;
}

####################################################################################################
package Person;
CIPRES::TreeBase::VeryBadORM->register();

sub to_str {
    my $self = shift;
    my $s = $self->SUPER::to_str();
    my ($f, $m, $l) = ($self->firstname, $self->mname, $self->lastname);
    my @names = grep defined($_) && $_, $f, $m, $l;
    my $name = join(" ", @names);
    $s .= " $name";
    $s .= " (" . $self->email . ")" if $self->email;
    return $s;
}

####################################################################################################
package PhyloTree;
CIPRES::TreeBase::VeryBadORM->register();
our %subobject = ('rootnode' => 'PhyloTreeNode', 'treetype' => 'TreeType');
our %r_attr = ('treeblock' => 'TreeBlock');

sub to_str {
    my $self = shift;
    my $s = $self->SUPER::to_str(@_);
    my $tb1id = $self->tb1_treeid();
    $s .= qq{ (was $tb1id)} if $tb1id;
    my $title = $self->title;
    $s .= qq{ "$title"} if $title;
    my $label = $self->label;
    $s .= qq{ ($label)} if $label;
    return $s;
}

sub recurse {
  my $self = shift();

  my $type = $self->treetype;
  $type->dump(@_) if $type;

  my $root = $self->rootnode;
  $root->dump(@_) if $root;
}

sub consistent {
    my $self = shift;
    my %attr = @_;
    my $OK = 1;
    $OK &&= $attr{'attr_check'}->($self, 'Study', \%attr);
    $OK &&= $attr{'attr_check'}->($self, 'TreeBlock', \%attr);
    $OK &&= $attr{'attr_check'}->($self->rootnode, 'parent', undef, $attr{'warnings'});

    for my $a (qw(quality kind type)) {
      my $meth = "tree$a\_id";
      unless (defined $self->$meth) {
		push @{$attr{'warnings'}},  "PhyloTree " . $self->id . " has null tree_$a";
		$OK = 0;
      }
    }

    for my $a (qw(label title)) {
      unless (defined $self->$a) {
		push @{$attr{warnings}},  "PhyloTree " . $self->id . " has null $a";
		$OK = 0;
      }
    }

    my $leaves = $self->leaves;
    unless ($leaves == $self->ntax) {
      push @{$attr{warnings}},  "PhyloTree " . $self->id . " has $leaves leaf nodes but ntax=" . $self->ntax;
      $OK = 0;
    }
    if ($leaves == 0) {
      push @{$attr{warnings}},  "PhyloTree " . $self->id . " has no nodes";
      $OK = 0;
    }

    # TODO: select count(*) from phylotreenode where phylotree_id = :id
    #       should return the same as $self->nodes
    return $OK;
}

sub nodes {
  my $self = shift;
  return @{$self->{nodes}} if $self->{nodes};
  my @nodeset = ();
  my @queue = $self->rootnode;
  while (@queue) {
    my $cur = shift @queue;
    push @nodeset, $cur;
    push @queue, $cur->children;
  }
  $self->{nodes} = \@nodeset;
  return @nodeset;
}

sub leaves {
  my $self = shift;
  return grep {$_->is_leaf} $self->nodes;
}


sub taxonlabels {
  my $self = shift;
  unless ($self->{taxonlabels}) {
    $self->{taxonlabels} = [ map { $_->taxonlabel } $self->nodes ];
  }
  return @{$self->{taxonlabels}};
}

sub nexusfile {
    my $self = shift;
    return $self->{nexusfile} if $self->{nexusfile};
    my $nfn = $self->nexusfilename;
    my $sid = $self->study->id;
    return $self->{nexusfile} = NexusFile->new_by_name($nfn, $sid);
}

####################################################################################################
package PhyloTreeNode;
CIPRES::TreeBase::VeryBadORM->register();
our %subobject = (
	'child'     => 'PhyloTreeNode',
	'sibling'   => 'PhyloTreeNode',
	'parent'    => 'PhyloTreeNode',
);

sub to_str {
    my $self = shift;
    my $s = $self->SUPER::to_str(@_);
    my $name = $self->name();
    $s .= qq{ ('$name')} if $name;
    return $s;
}

sub children {
  my $self = shift;
  my @children;
  for (my $child = $self->child; $child; $child = $child->sibling) {
    push @children, $child;
  }
  return @children;
}

sub is_leaf {
  my $self = shift;
  return not defined $self->child;
}

sub recurse {
    my $self = shift;
    my %attr = @_;
    my $tl = $self->taxonlabel;
    $tl->dump(%attr) if $tl;
    for (my $n = $self->child; $n; $n = $n->sibling) {
		$n->dump(%attr);
    }
}

sub consistent {
    my $self = shift;
    my %attr = @_;
    my $W = $attr{warnings};
    my $OK = 1;
    $OK &&= $attr{attr_check}->($self, 'PhyloTree', \%attr);
    for my $child ($self->children) {
	$OK &&= $attr{attr_check}->($child, 'parent', $self->id, $W);
    }
    if ($self->is_leaf && ! defined($self->taxonlabel)) {
      push @{$attr{warnings}},  "PhyloTreeNode " . $self->id . " is a leaf but has no TaxonLabel";
      $OK = 0;
    }

    return $OK;
}

sub is_nested { 1 }

####################################################################################################
package Study;
CIPRES::TreeBase::VeryBadORM->register();
our %r_attr = (
	'analyses'       => 'Analysis',
	'matrices'       => 'Matrix',
	'trees'          => 'PhyloTree',
	'submissions'    => 'Submission',
	'taxonlabelsets' => 'TaxonLabelSet',
);

our %r2_attr = ('nexusfiles' => ['study_nexusfile', 'NexusFile']);

sub to_str {
    my $self = shift();
    my $name = $self->name;
    my $citation = $self->citation;
    my $title = $citation && $citation->title;
    $title = substr($title, 0, 30) . "..." if length($title) > 33;
    my @items = ($self->SUPER::to_str(),
		 $name ? "($name)" : (),
		 $title ? qq{"$title"} : (),
		 );
    return join " ", @items;
}

sub recurse {
  my $self = shift();

  my $citation = $self->citation;
  $citation->dump(@_) if $citation;

  for my $analysis ($self->analyses) {
    $analysis->dump(@_);
  }
  for my $matrix ($self->matrices) {
    $matrix->dump(@_);
  }
  for my $tree ($self->trees) {
    $tree->dump(@_);
  }
  for my $submission ($self->submissions) {
      $submission->dump(@_);
  }
  for my $tls ($self->taxonlabelsets) {
    $tls->dump(@_);
  }
}

sub analysis_taxonlabels {
  my $self = shift;
  unless (defined $self->{analysis_taxonlabels}) {
    my %tl;
    for my $ad ($self->analyzed_data) {
      for my $tl ($ad->taxonlabels) {
	$tl{$tl->id} = $tl;
      }
    }
    $self->{analysis_taxonlabels} = [ values %tl ];
  }
  return @{$self->{analysis_taxonlabels}};
}

sub analysis_steps {
  my $self = shift;
  my @as;
  for my $analysis ($self->analyses) {
    push @as, $analysis->analysis_steps;
  }
  return @as;
}

sub analyzed_matrices {
  my $self = shift;
  unless ($self->{analyzed_matrices}) {
      $self->{analyzed_matrices} = 
	  [ map {$_->analyzed_matrices} $self->analyses ];
  }
  return @{$self->{analyzed_matrices}};
}

sub analyzed_trees {
  my $self = shift;
  unless ($self->{analyzed_trees}) {
      $self->{analyzed_trees} = 
	  [ map {$_->analyzed_trees} $self->analyses ];
  }
  return @{$self->{analyzed_trees}};
}

sub analyzed_data {
  my $self = shift;
  my @ad = ($self->analyzed_matrices, $self->analyzed_trees);
  return @ad;
}

sub tls_taxonlabels {
  my $self = shift;
  map { $_->taxonlabels } $self->taxonlabelsets;
}

sub consistent {
  my $self = shift;
  my %attr = @_;
  my $OK = 1;

  my %nexusfile_by_name;
  for my $nexusfile ($self->nexusfiles) {
      $nexusfile_by_name{$nexusfile->filename} = $nexusfile;
  }

  {
      my @submissions = $self->submissions;
      if (@submissions != 1) {
	  push @{$attr{warnings}},  "Study " . $self->id . " in " . @submissions . " submissions.\n";
	  $OK = 0;
      } 

      if (@submissions) {
	  my $submission = $submissions[0];
	  unless ($submission->study->id == $self->id) {
	      push @{$attr{warnings}},  "Study " . $self->id . " refers to submission " . $submission->id . " which doesn't refer back.\n";
	      $OK = 0;
	  }
      }
  }

  for my $tree ($self->analyzed_trees) {
    $OK &&= $attr{attr_check}->($tree, 'Study', $self->id, $attr{warnings});
  }
  for my $matrix ($self->analyzed_matrices) {
    $OK &&= $attr{attr_check}->($matrix, 'Study', $self->id, $attr{warnings});
  }

  my %tl_id;
  my %tv_tls;
  for my $tl ($self->analysis_taxonlabels, $self->tls_taxonlabels) {
      my $name = $tl->taxonlabel;
      $tl_id{$name}{$tl->id} = 1;
      if (my $tv = $tl->taxonvariant) {
	  push @{$tv_tls{$tv->id}}, $tl->id;
      }

    unless ($tl->study && $tl->study->id == $self->id) {
      push @{$attr{warnings}},  "Study " . $self->id . " has analyzed data with taxon label " . $tl->id . " which doesn't point back.\n";
      $OK = 0;
    }
  }

  for my $name (keys %tl_id) {
      my @ids = keys %{$tl_id{$name}};
      next if @ids == 1;
      push @{$attr{warnings}},  "Study " . $self->id . " contains multiple instances of taxon label '$name': ids @ids\n";
      $OK = 0;
  }

  # Rutger points out that this test is erroneous; there might be multiple
  # TL that all refer to the same TV.  Example is T4105.  There many
  # TLs of the form "Andricus quercustozae haplotype ##" all refer to Tv568114
  # 20090527
  if (0) { # TODO
  for my $tv_id (keys %tv_tls) {
      my @ids = @{$tv_tls{$tv_id}};
      next if @ids == 1;
      push @{$attr{warnings}},  "Study " . $self->id . " contains several taxonlabels that all refer to taxonvariant " . $tv_id . ": ids @ids\n";
      $OK = 0;
  }

  unless ($self->citation) {
      push @{$attr{warnings}},  "Study " . $self->id . " has no citation\n";
      $OK = 0;
  }}

  return $OK;
}

# Override this for nexusfiles, which are a little odd
sub get_r2_subobject_no_check {
    my ($self, $attr) = @_;
    if (uc($attr) eq 'NEXUSFILES') {
	# Special case for nexusfiles
	my $target_table = $self->r2_table($attr);
	my $target_class = $self->r2_class($attr);
	my $q = "select filename from $target_table where study_id = ?";
	my $sth = $self->prepare_cached($q);
	$sth->execute($self->id);
	while (my ($filename) = $sth->fetchrow) {
	    push @results, $target_class->new_by_name($filename, $self->id);
	}
	return @results;
    } else {
	# Business as usual
	return $self->SUPER::get_r2_subobject_no_check($attr);
    }
}

####################################################################################################
package Submission;
CIPRES::TreeBase::VeryBadORM->register();
our %r2_attr = (
	treeblocks  => ['sub_treeblock', 'TreeBlock'],
	taxonlabels => ['sub_taxonlabel', 'TaxonLabel'],
	matrices    => ['sub_matrix', 'Matrix'],
);

sub recurse {
  my $self = shift();
  my $study = $self->study;
  $study->dump(@_) if $study;
  for my $tb ($self->treeblocks) {
      $tb->dump(@_);
  }
}

sub consistent {
  my $OK = 1;
  my $self = shift;
  my %attr = @_;

  my $study_id = $self->study->id;

  for my $matrix ($self->matrices) {
    unless (defined($matrix->study)) {
      push @{$attr{warnings}},  "Submission " . $self->id . " refers to matrix " . $matrix->id . " which has no study.\n";
      $OK = 0;
      next;
    }
    unless ($matrix->study->id == $study_id) {
      push @{$attr{warnings}},  "Submission " . $self->id . " refers to matrix " . $matrix->id . " which doesn't refer back.\n";
      $OK = 0;
    }
  }

  for my $tb ($self->treeblocks) {
    for my $tree ($tb->trees) {
      unless (defined($tree->study)) {
	push @{$attr{warnings}},  "Submission " . $self->id . " refers to tree " . $tree->id . " which has no study.\n";
	$OK = 0;
	next;
      }
      unless ($tree->study->id == $study_id) {
	push @{$attr{warnings}},  "Submission " . $self->id . " refers to tree " . $tree->id . " which doesn't refer back.\n";
	$OK = 0;
      }
    }
  }

  for my $tl ($self->taxonlabels) {
    unless (defined($tl->study)) {
      push @{$attr{warnings}},  "Submission " . $self->id . " refers to taxonlabel " . $tl->id . " which has no study.\n";
      $OK = 0;
      next;
    }
    unless ($tl->study->id == $study_id) {
      push @{$attr{warnings}},  "Submission " . $self->id . " refers to taxonlabel " . $tl->id . " which doesn't refer back.\n";
      $OK = 0;
    }
  }

  return $OK;
}

####################################################################################################
package Taxon;
CIPRES::TreeBase::VeryBadORM->register();

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return "$s " . $self->name;
}

####################################################################################################
package TaxonLabel;
CIPRES::TreeBase::VeryBadORM->register();
our %r_attr = ('treenodes' => 'PhyloTreeNode', 'rows' => 'MatrixRow');
our %r2_attr = ('taxonlabelsets' => ['taxonlabelset_taxonlabel', 'TaxonLabelSet']);

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return "$s " . $self->taxonlabel;
}

sub recurse {
  my $self = shift();
  my $tv = $self->taxonvariant;
  $tv->dump(@_) if $tv;
#  for my $tree ($self->treenodes) {
#      $tree->dump(@_);
#  }
#  for my $row ($self->rows) {
#      $row->dump(@_);
#  }
#  for my $tls ($self->taxonlabelsets) {
#      $tls->dump(@_);
#  }
}

sub consistent {
    my $self = shift;
    my %attr = @_;
    return $attr{attr_check}->($self, 'Study', \%attr);
}

####################################################################################################
package TaxonLabelSet;
CIPRES::TreeBase::VeryBadORM->register();
our %r_attr = ('treeblocks' => 'TreeBlock', 'matrices' => 'Matrix');
our %r2_attr = ('taxonlabels' => ['taxonlabelset_taxonlabel', 'TaxonLabel']);

sub to_str {
    my $self = shift;
    my $s = $self->SUPER::to_str(@_);
    my $title = $self->title();
    $s .= qq{ ('$title')} if $title;
    return $s;
}

sub recurse {
  my $self = shift();
  for my $tl ($self->taxonlabels) {
      $tl->dump(@_);
  }
}

sub consistent {
    my $self = shift;
    my %attr = @_;
    my $OK = 1;

    $OK &&= $attr{attr_check}->($self, 'Study', \%attr);
    my $sid = $self->study_id;

    my %tl_id;
    for my $tl ($self->taxonlabels) {
      $tl_id{$tl->id} = 1;
      $OK &&= $attr{attr_check}->($tl, 'Study', $sid, $attr{warnings});
    }

    for my $matrix ($self->matrices) {
      $OK &&= $attr{attr_check}->($matrix, 'Study', $sid, $attr{warnings});
      for my $row ($matrix->rows) {
	    my $tl = $row->taxonlabel;
	    next unless defined $tl;
	    next if exists $tl_id{$tl->id};
	    push @{$attr{warnings}},  "matrix " . $matrix->id . " references TLS " . $self->id . ", but its row " . $row->id . " contains TL " . $tl->id . " which is not in the set.\n";
	    $OK = 0;
	}
    }

    for my $tb ($self->treeblocks) {
	for my $tree ($tb->trees) {
	    $OK &&= $attr{attr_check}->($tree, 'Study', $sid, $attr{warnings});
	    for my $node ($tree->nodes) {
		my $tl = $node->taxonlabel;
		next unless defined $tl;
		next if exists $tl_id{$tl->id};
		push @{$attr{warnings}},  "tree " . $tree->id . " references TLS " . $self->id .", but its node " . $node->id . " contains TL " . $tl->id . " which is not in the set.\n";
		$OK = 0;
	    }
	}
    }

    return $OK;
}

####################################################################################################
package TaxonVariant;
CIPRES::TreeBase::VeryBadORM->register();

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    my $name = $self->name;
    my $fullname = $self->fullname;
    my $lq = $self->lexicalqualifier;
    my $str = "$s " . $self->name;
    $str .= " ($fullname)" if $fullname && $fullname ne $name;
    $str .= " [$lq]" if $lq;
    $str;
}

sub recurse {
  my $self = shift();
  my $t = $self->taxon;
  $t->dump(@_) if $t;
}

####################################################################################################
package TreeBlock;
CIPRES::TreeBase::VeryBadORM->register();
our %r2_attr = ('submissions' => ['sub_treeblock', 'Submission']);
our %r_attr = ('trees' => 'PhyloTree');


sub to_str {
    my $self = shift;
    my $s = $self->SUPER::to_str(@_);
    my $title = $self->title();
    $s .= qq{ ('$title')} if $title;
    return $s;
}

sub recurse {
  my $self = shift();
  my %attr = @_;
#  $attr{prune}{TaxonLabelSet} = 1;
#  $attr{prune}{Study} = 1;
#  $attr{prune}{Submission} = 1;
  my $tls = $self->taxonlabelset;
  $tls->dump(%attr) if $tls;
  for my $sub ($self->submissions) {
      $sub->dump(%attr);
  }
  for my $t ($self->trees) {
      $t->dump(%attr);
  }
}

sub consistent {
    my $self = shift;
    my %attr = @_;
    my $OK = 1;
    $OK &&= $attr{attr_check}->($self, 'Submissions', \%attr);
    $OK &&= $attr{attr_check}->($self, 'TaxonLabelSet', \%attr);
    return $OK;
}

####################################################################################################
package TreeType;
CIPRES::TreeBase::VeryBadORM->register();

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return $self->description ? qq{$s "} . $self->description . qq{"} : $s;
}

1;
