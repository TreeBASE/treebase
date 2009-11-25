$CIPRES::TreeBase::TreeBaseObjects::VERSION=0.1;

require CIPRES::TreeBase::VeryBadORM;

=head1 NAME

CIPRES::TreeBase::TreeBaseObjects

=head1 DESCRIPTION

This module is a collection of classes that represent objects from the TreeBase database.
Access to the objects is through L<CIPRES::TreeBase::VeryBadORM> and is read-only.

In general, each kind of object is represented by a different class.  For example, studies are
represented by C<Study> objects, treenodes are represented by C<PhyloTreeNode> objects, and so
on.


=head1 PACKAGE METHODS

=over

=item set_db_connection()

Aliases to CIPRES::TreeBase::VeryBadORM::set_db_connection

=back

=cut

sub CIPRES::TreeBase::TreeBaseObjects::set_db_connection { 
    my $class = shift; 
    CIPRES::TreeBase::VeryBadORM->set_db_connection(@_); 
}

=head1 OBJECTS

=cut

####################################################################################################
package Analysis;
CIPRES::TreeBase::VeryBadORM->register();
our %r_attr = ( 'analysissteps' => 'AnalysisStep' );

=head2 Analysis

Object representation of a TreeBASE analysis, which is referred to by 
L<AnalysisStep> objects.

=over

=item to_str()

Stringification method for invocant analysis.

=cut

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return $self->name ? qq{$s "} . $self->name . qq{"} : $s;
}

=item recurse()

Traverses all associated analysis steps and dumps them.

=cut

sub recurse {
  my $self = shift();
  for my $as ($self->analysissteps) {
    $as->dump(@_);
  }
}

=item analysis_steps()

Convenience alias for analysisteps().

=cut

# Convenience alias
sub analysis_steps {
  return $_[0]->analysissteps;
}

=item analyzed_matrices()

Getter for retrieving all analyzed matrices (input and output)
from all analysis steps associated with the invocant analysis.

=cut

sub analyzed_matrices {
  my $self = shift;
  unless ($self->{'analyzed_matrices'}) {
    my @matrices;
    for my $as ($self->analysis_steps) {
      for my $ad ($as->analyzeddata) {
	    my $matrix = $ad->matrix;
	    push @matrices, $matrix if defined $matrix;
      }
    }
    $self->{'analyzed_matrices'} = \@matrices;
  }
  return @{$self->{'analyzed_matrices'}};
}

=item analyzed_trees()

Getter for retrieving all analyzed trees (input and output)
from all analysis steps associated with the invocant analysis.

=cut

sub analyzed_trees {
  my $self = shift;
  unless ($self->{'analyzed_trees'}) {
    my @trees;
    for my $as ($self->analysis_steps) {
      for my $ad ($as->analyzeddata) {
	    my $tree = $ad->tree;
	    push @trees, $tree if defined $tree;
      }
    }
    $self->{'analyzed_trees'} = \@trees;
  }
  return @{$self->{'analyzed_trees'}};
}

=item analyzed_data()

Getter for retrieving all analyzed data (matrices and trees, input and output)
from all analysis steps associated with the invocant analysis.

=cut

sub analyzed_data {
  my $self = shift;
  return $self->analyzed_matrices, $self->analyzed_trees;
}

=item consistent()

Consistency check on the invocant analysis. An analysis is considered consistent
if all analysis steps have one or more analyzed trees, one or more analyzed matrices and one
or more taxonlabel sets. 

=cut

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

=back

=cut

####################################################################################################
package AnalysisStep;
CIPRES::TreeBase::VeryBadORM->register();
our %r_attr = ('analyzeddata' => 'AnalyzedData');

=head2 AnalysisStep

Object representation of a TreeBASE analysis step, which is referred to by 
L<AnalyzedData> objects.

=over

=item to_str()

Stringification method for invocant analysis step.

=cut

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return $self->name ? qq{$s "} . $self->name . qq{"} : $s;
}

=item recurse()

Stringification method for invocant analysis step.

=cut

sub recurse {
  my $self = shift();
  for my $ad ($self->analyzeddata) {
    $ad->dump(@_);
  }
}

=back

=cut

####################################################################################################
package AnalyzedData;
CIPRES::TreeBase::VeryBadORM->register();
our %subobject = ('phylotree' => 'PhyloTree');

=head2 AnalyzedData

Object representation of a TreeBASE analyzed data object, which is 
essentially a wrapper around either a matrix or a tree (accessed 
internally in that order).

=over

=item recurse()

Traverses internal data object, which is either a matrix or a tree.

=cut

sub recurse {
  my $self = shift();
  if (my $matrix = $self->matrix) {
    $matrix->dump(@_);
  } elsif (my $tree = $self->phylotree) {
    $tree->dump(@_);
  }
}

=item tree()

Alias method for phylotree()

=cut

# Convenience alias
sub tree {
  return $_[0]->phylotree;
}

=back

=cut

####################################################################################################
package Citation;
our %r2_attr = (
	'authors' => ['citation_author', 'Person', 'authors_person_id']
);
CIPRES::TreeBase::VeryBadORM->register();

=head2 Citation

Object representation of a TreeBASE citation object, which are associated
with L<Person> objects through the citation_author intersection table. These
Person objects are retrieved by accessing the Citation::authors() method.

=over

=item recurse()

Traverses the associated authors (Person objects) and dumps them.

=cut

sub recurse {
  my $self = shift();
  for my $author ($self->authors) {
    $author->dump(@_);
  }
}

=item consistent()

A Citation is considered consistent if it contains one or more
authors with no duplicates.

=cut

sub consistent {
    my $self = shift();
    my %attr = @_;
    my %author_count;
    my $OK = 1;
    for my $author ($self->authors) {
		if (++$author_count{$author->id} == 2) {
			push @{$attr{'warnings'}},  
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

=back

=cut

####################################################################################################
package Matrix;
CIPRES::TreeBase::VeryBadORM->register();
our %r_attr = (
	'rows'    => 'MatrixRow',
	'columns' => 'MatrixColumn',
);
our %subobject = ( 
	'taxonlabelset' => 'TaxonLabelSet',
	'matrixkind'    => 'MatrixKind',
);

=head2 Matrix

Object representation of a TreeBASE character state matrix object.
This object is referenced by L<MatrixRow> and L<MatrixColumn> objects.

=over

=item to_str()

Stringification method for the invocant matrix. 

=cut

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return "$s (was " . $self->tb_matrixid . " " . $self->title . ")";
}

=item recurse()

Traverses associated matrix kind and matrix rows and dumps them.

=cut

sub recurse {
  my $self = shift();
  my $mk = $self->matrixkind;
  $mk->dump(@_) if $mk;
  for my $ad ($self->rows) {
    $ad->dump(@_);
  }
}

=item taxonlabels()

Returns taxon labels associated with the matrix rows.

=cut

sub taxonlabels {
  my $self = shift;
  unless ($self->{'taxonlabels'}) {
    $self->{'taxonlabels'} = [ map { $_->taxonlabel } $self->rows ];
  }
  return @{$self->{'taxonlabels'}};
}

=item nexusfile()

Returns NexusFile object from which the invocant matrix was parsed.

=cut

sub nexusfile {
    my $self = shift;
    return $self->{'nexusfile'} if $self->{'nexusfile'};
    my $nfn = $self->nexusfilename;
    return $self->{'nexusfile'} = NexusFile->new_by_name($nfn);
}

=item consistent()

Consistency check on the invocant matrix object. A Matrix is considered consistent
if: the ntax field matches the row count and the matrix has more than 0 rows, nchar
matches the column count and the matrix has more than 0 columns, and if the matrix
kind and matrix title are defined.

=cut

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

=back

=cut

####################################################################################################
package MatrixColumn;
CIPRES::TreeBase::VeryBadORM->register();

=head2 MatrixColumn

Object representation of a TreeBASE character state matrix column object.

=cut


####################################################################################################
package MatrixKind;
CIPRES::TreeBase::VeryBadORM->register();

=head2 MatrixKind

Object representation of a TreeBASE character state matrix kind object.

=over

=item to_str()

Stringification method for the invocant matrix kind. 

=cut

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return $self->description ? qq{$s "} . $self->description . qq{"} : $s;
}

=back

=cut

####################################################################################################
package MatrixRow;
CIPRES::TreeBase::VeryBadORM->register();
our %subobject = ('taxonlabel' => 'TaxonLabel');

=head2 MatrixRow

Object representation of a TreeBASE character state matrix row object.

=over

=item recurse()

Fetches the associated taxon label object and dumps it.

=cut

sub recurse {
  my $self = shift();
  my $tl = $self->taxonlabel;
  $tl->dump(@_) if $tl;
}

=item consistent()

Consistency check on a matrix row object. A row is considered consistent
if the matrix it belong to is consistent.

=cut

sub consistent {
    my $self = shift;
    my %attr = @_;
    return $attr{'attr_check'}->($self, 'Matrix', \%attr);
}

=back

=cut

####################################################################################################
package NexusFile;
CIPRES::TreeBase::VeryBadORM->register();
our %subobject = ('study' => 'Study');

=head2 NexusFile

Object representation of a stored TreeBASE nexus file. NexusFile 
objects refer to L<Study> objects.

=over

=item table()

Helper method to specify the table in which the nexus file data
is stored.

=cut

sub table { "Study_NexusFile" } # XXX check to see if this casing makes sense

=item new_by_name()

Constructor for nexus file objects. This is a special constructor that
exists because nexus data is stored in large blobs which we don't want
to retrieve by default.

=cut

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

=back

=cut

####################################################################################################
package Person;
CIPRES::TreeBase::VeryBadORM->register();

=head2 Person

Object representation of a TreeBASE person. TreeBASE persons include
authors and editors.

=over

=item to_str()

Stringification method. A string representation of a Person includes
first name, middle name, last name and email address.

=cut

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

=back

=cut

####################################################################################################
package PhyloTree;
CIPRES::TreeBase::VeryBadORM->register();
our %subobject = (
	'rootnode'  => 'PhyloTreeNode', 
	'treetype'  => 'TreeType',
	'treeblock' => 'TreeBlock',
);
our %r_attr = ('treeblock' => 'TreeBlock');

=head2 PhyloTree

Object representation of a TreeBASE phylotree. TreeBASE phylotrees contain
at least a reference to a L<PhyloTreeNode> root and a L<TreeType>. They also
have a back-reference to a L<TreeBlock> object.

=over

=item to_str()

Stringification method. A string representation of a PhyloTree includes
its TreeBASE1 identifier, its title and its label.

=cut

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

=item recurse()

Traverses the associated L<TreeType> and L<PhyloTreeNode> root objects
and dumps them.

=cut

sub recurse {
  my $self = shift();

  my $type = $self->treetype;
  $type->dump(@_) if $type;

  my $root = $self->rootnode;
  $root->dump(@_) if $root;
}

=item consistent()

Consistency check on invocant PhyloTree object. A PhyloTree is considered consistent if:
its quality, kind and type fields are defined, its label and title are defined, its
ntax field and tip count match and it has more than 0 tips.

=cut

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
		push @{$attr{'warnings'}},  "PhyloTree " . $self->id . " has null $a";
		$OK = 0;
      }
    }

    my $leaves = $self->leaves;
    unless ($leaves == $self->ntax) {
      push @{$attr{'warnings'}},  "PhyloTree " . $self->id . " has $leaves leaf nodes but ntax=" . $self->ntax;
      $OK = 0;
    }
    if ($leaves == 0) {
      push @{$attr{'warnings'}},  "PhyloTree " . $self->id . " has no nodes";
      $OK = 0;
    }

    # TODO: select count(*) from phylotreenode where phylotree_id = :id
    #       should return the same as $self->nodes
    return $OK;
}

=item nodes()

Returns all nodes (internal and terminal) associated with the invocant tree.

=cut

sub nodes {
  my $self = shift;
  return @{$self->{'nodes'}} if $self->{'nodes'};
  my @nodeset = ();
  my @queue = $self->rootnode;
  while (@queue) {
    my $cur = shift @queue;
    push @nodeset, $cur;
    push @queue, $cur->children;
  }
  $self->{'nodes'} = \@nodeset;
  return @nodeset;
}

=item leaves()

Returns terminal nodes associated with the invocant tree.

=cut

sub leaves {
  my $self = shift;
  return grep {$_->is_leaf} $self->nodes;
}

=item taxonlabels()

Returns the L<TaxonLabel> objects associated with the nodes in the invocant tree.

=cut

sub taxonlabels {
  my $self = shift;
  unless ($self->{'taxonlabels'}) {
    $self->{'taxonlabels'} = [ map { $_->taxonlabel } $self->nodes ];
  }
  return @{$self->{'taxonlabels'}};
}

=item nexusfile()

Returns the L<NexusFile> object from which the invocant PhyloTree object was parsed.

=cut

sub nexusfile {
    my $self = shift;
    return $self->{'nexusfile'} if $self->{'nexusfile'};
    my $nfn = $self->nexusfilename;
    my $sid = $self->study->id;
    return $self->{'nexusfile'} = NexusFile->new_by_name($nfn, $sid);
}

=back

=cut

####################################################################################################
package PhyloTreeNode;
CIPRES::TreeBase::VeryBadORM->register();
our %subobject = (
	'child'      => 'PhyloTreeNode',
	'sibling'    => 'PhyloTreeNode',
	'parent'     => 'PhyloTreeNode',
	'taxonlabel' => 'TaxonLabel',
	'phylotree'  => 'PhyloTree',
);

=head2 PhyloTreeNode

Object representation of a TreeBASE phylotreenode. TreeBASE phylotreenodes contain
references to child, sibling and parent nodes any of which can be undefined, depending
on where the invocant node is in the containing tree topology.

=over

=item to_str()

Stringification method. A string representation of a PhyloTreeNode includes
its name.

=cut

sub to_str {
    my $self = shift;
    my $s = $self->SUPER::to_str(@_);
    my $name = $self->name();
    $s .= qq{ ('$name')} if $name;
    return $s;
}

=item children()

Returns all immediate descendants of the invocant node. These are fetched
by following the reference to the first child (as per the database's foreign
key) and its siblings (also as per the databases foreign keys for those).

=cut

sub children {
  my $self = shift;
  my @children;
  for (my $child = $self->child; $child; $child = $child->sibling) {
    push @children, $child;
  }
  return @children;
}

=item is_leaf()

Returns whether the invocant node is a terminal node. It decides this
by checking whether it has a child reference and negating that.

=cut

sub is_leaf {
  my $self = shift;
  return not defined $self->child;
}

=item recurse()

Traverses the associated L<TaxonLabel> object and child objects and 
dumps them.

=cut

sub recurse {
    my $self = shift;
    my %attr = @_;
    my $tl = $self->taxonlabel;
    $tl->dump(%attr) if $tl;
    for (my $n = $self->child; $n; $n = $n->sibling) {
		$n->dump(%attr);
    }
}

=item consistent()

Consistency check on the invocant PhyloTreeNode object. The invocant is considered consistent if:
it is associated with a L<PhyloTree> object, if the associated child node refers to the correct
parent node and if the node, if terminal, has a valid L<TaxonLabel> reference.

=cut

sub consistent {
    my $self = shift;
    my %attr = @_;
    my $W = $attr{'warnings'};
    my $OK = 1;
    $OK &&= $attr{'attr_check'}->($self, 'PhyloTree', \%attr);
    for my $child ($self->children) {
		$OK &&= $attr{'attr_check'}->($child, 'parent', $self->id, $W);
    }
    if ($self->is_leaf && ! defined($self->taxonlabel)) {
      push @{$attr{'warnings'}},  "PhyloTreeNode " . $self->id . " is a leaf but has no TaxonLabel";
      $OK = 0;
    }
    return $OK;
}

=item is_nested()

Returns that the invocant is nested. Perhaps this means it refers to other instances of the same 
class?

=cut

sub is_nested { 1 }

=back

=cut

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

=head2 Study

Object representation of a TreeBASE study. Studies contain references to L<Analysis>, L<Matrix>,
L<PhyloTree>, L<Submission> and L<TaxonLabelSet> objects. L<NexusFile> objects contain
backreferences to study objects.

=over

=item to_str()

Stringification method. A string representation of a Study includes
its citation title (truncated to 30 characters) and its name and title.

=cut

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

=item recurse()

Traverse associated L<Citation>, L<Analysis>, L<Matrix>, L<PhyloTree>, L<Submission> and 
L<TaxonLabelSet> objects and dumps them all.

=cut

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

=item analysis_taxonlabels()

Returns all taxon labels associated with all analyzed data.

=cut

sub analysis_taxonlabels {
  my $self = shift;
  unless (defined $self->{'analysis_taxonlabels'}) {
    my %tl;
    for my $ad ($self->analyzed_data) {
      for my $tl ($ad->taxonlabels) {
	$tl{$tl->id} = $tl;
      }
    }
    $self->{'analysis_taxonlabels'} = [ values %tl ];
  }
  return @{$self->{'analysis_taxonlabels'}};
}

=item analysis_steps()

Returns all associated analysis steps.

=cut

sub analysis_steps {
  my $self = shift;
  my @as;
  for my $analysis ($self->analyses) {
    push @as, $analysis->analysis_steps;
  }
  return @as;
}

=item analyzed_matrices()

Returns all associated analyzed matrices.

=cut

sub analyzed_matrices {
  my $self = shift;
  unless ($self->{analyzed_matrices}) {
      $self->{analyzed_matrices} = 
	  [ map {$_->analyzed_matrices} $self->analyses ];
  }
  return @{$self->{analyzed_matrices}};
}

=item analyzed_trees()

Returns all associated analyzed trees.

=cut

sub analyzed_trees {
  my $self = shift;
  unless ($self->{analyzed_trees}) {
      $self->{analyzed_trees} = 
	  [ map {$_->analyzed_trees} $self->analyses ];
  }
  return @{$self->{analyzed_trees}};
}

=item analyzed_data()

Returns all associated analyzed data.

=cut

sub analyzed_data {
  my $self = shift;
  my @ad = ($self->analyzed_matrices, $self->analyzed_trees);
  return @ad;
}

=item tls_taxonlabels()

Returns all taxon labels from all associated taxon label sets.

=cut

sub tls_taxonlabels {
  my $self = shift;
  map { $_->taxonlabels } $self->taxonlabelsets;
}

=item consistent()

Runs consistency check on the invocant Study. A study is considered consistent if: there is 
exactly one associated submission and that submission refers back to the invocant study, the
associated trees and matrices are consistent, the associated taxon labels refer back to the
invocant study, there are no multiple taxon labels with the same name, and the study contains
a consistent citation.

=cut

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

=item get_r2_subobject_no_check()

Overrides SUPER::get_r2_subobject_no_check() to deal with nexus files.

=cut

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

=back

=cut

####################################################################################################
package Submission;
CIPRES::TreeBase::VeryBadORM->register();
our %r2_attr = (
	treeblocks  => ['sub_treeblock', 'TreeBlock'],
	taxonlabels => ['sub_taxonlabel', 'TaxonLabel'],
	matrices    => ['sub_matrix', 'Matrix'],
);

=head2 Submission

Object representation of a TreeBASE submission. Submissions contain references to L<TreeBlock>,
L<TaxonLabel> and L<Matrix> objects.

=over

=item recurse()

Traverses associated study object and tree blocks and dumps them.

=cut

sub recurse {
  my $self = shift();
  my $study = $self->study;
  $study->dump(@_) if $study;
  for my $tb ($self->treeblocks) {
      $tb->dump(@_);
  }
}

=item consistent()

Consistency check for invocant submission object. A submission is considered consistent if: 
associated matrices, trees and taxon labels refer to the right L<Study> object and refer back to the invocant
submission.

=cut

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

=back

=cut

####################################################################################################
package Taxon;
CIPRES::TreeBase::VeryBadORM->register();

=head2 Taxon

Object representation of a TreeBASE Taxon.

=over

=item to_str()

Stringification method. A string representation of a Taxon includes its name.

=cut

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return "$s " . $self->name;
}

=back

=cut

####################################################################################################
package TaxonLabel;
CIPRES::TreeBase::VeryBadORM->register();
our %subobject = ( 'taxonvariant' => 'TaxonVariant' );
our %r_attr = ('treenodes' => 'PhyloTreeNode', 'rows' => 'MatrixRow');
our %r2_attr = (
	'taxonlabelsets' => ['taxonlabelset_taxonlabel', 'TaxonLabelSet'],
);

=head2 TaxonLabel

Object representation of a TreeBASE TaxonLabel, which is referenced by L<PhyloTreeNode> and
L<MatrixRow> objects, and referred back to by the taxonlabelsets table.

=over

=item to_str()

Stringification method. A string representation of a TaxonLabel includes its taxonlabel field.

=cut

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return "$s " . $self->taxonlabel;
}

=item recurse()

Traverses and dumps associated L<TaxonVariant> object.

=cut

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

=item consistent()

Consistency check on invocant TaxonLabel object, which is considered consistent if the associated
L<Study> object is.

=cut

sub consistent {
    my $self = shift;
    my %attr = @_;
    return $attr{'attr_check'}->($self, 'Study', \%attr);
}

=back

=cut

####################################################################################################
package TaxonLabelSet;
CIPRES::TreeBase::VeryBadORM->register();
our %r_attr = (
	'treeblocks' => 'TreeBlock', 
	'matrices'   => 'Matrix'
);
our %r2_attr = ('taxonlabels' => ['taxonlabelset_taxonlabel', 'TaxonLabel']);

=head2 TaxonLabelSet

Object representation of a TreeBASE taxonlabelset, which is referenced by L<TreeBlock> and
L<Matrix> objects, and referred back to by the taxonlabel table.

=over

=item to_str()

Stringification method. A string representation of a TaxonLabelSet includes its title.

=cut

sub to_str {
    my $self = shift;
    my $s = $self->SUPER::to_str(@_);
    my $title = $self->title();
    $s .= qq{ ('$title')} if $title;
    return $s;
}

=item recurse()

Travers associated L<TaxonLabel> objects and dumps them.

=cut

sub recurse {
  my $self = shift();
  for my $tl ($self->taxonlabels) {
      $tl->dump(@_);
  }
}

=item consistent()

Consistency check on invocant TaxonLabelSet object, which is considered consistent if: the 
associated study, matrices and tree blocks are consistent.

=cut

sub consistent {
    my $self = shift;
    my %attr = @_;
    my $OK = 1;

    $OK &&= $attr{'attr_check'}->($self, 'Study', \%attr);
    my $sid = $self->study_id;

    my %tl_id;
    for my $tl ($self->taxonlabels) {
      $tl_id{$tl->id} = 1;
      $OK &&= $attr{'attr_check'}->($tl, 'Study', $sid, $attr{'warnings'});
    }

    for my $matrix ($self->matrices) {
      $OK &&= $attr{'attr_check'}->($matrix, 'Study', $sid, $attr{'warnings'});
      for my $row ($matrix->rows) {
	    my $tl = $row->taxonlabel;
	    next unless defined $tl;
	    next if exists $tl_id{$tl->id};
	    push @{$attr{'warnings'}},  "matrix " . $matrix->id . " references TLS " . $self->id . ", but its row " . $row->id . " contains TL " . $tl->id . " which is not in the set.\n";
	    $OK = 0;
	  }
    }

    for my $tb ($self->treeblocks) {
		for my $tree ($tb->trees) {
			$OK &&= $attr{'attr_check'}->($tree, 'Study', $sid, $attr{'warnings'});
			for my $node ($tree->nodes) {
			my $tl = $node->taxonlabel;
			next unless defined $tl;
			next if exists $tl_id{$tl->id};
			push @{$attr{'warnings'}},  "tree " . $tree->id . " references TLS " . $self->id .", but its node " . $node->id . " contains TL " . $tl->id . " which is not in the set.\n";
			$OK = 0;
	    }
	    }
    }

    return $OK;
}

=back

=cut

####################################################################################################
package TaxonVariant;
CIPRES::TreeBase::VeryBadORM->register();

=head2 TaxonVariant

Object representation of a TreeBASE taxonvariant.

=over

=item to_str()

Stringification method. A string representation of a TaxonVariant includes its name, full name and
lexical qualifier.

=cut

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

=item recurse()

Traverses associated L<Taxon> object and dumps it.

=cut

sub recurse {
  my $self = shift();
  my $t = $self->taxon;
  $t->dump(@_) if $t;
}

=back

=cut

####################################################################################################
package TreeBlock;
CIPRES::TreeBase::VeryBadORM->register();
our %r2_attr = ('submissions' => ['sub_treeblock', 'Submission']);
our %r_attr = ('trees' => 'PhyloTree');
our %subobject = ( 'taxonlabelset' => 'TaxonLabelSet' );

=head2 TreeBlock

Object representation of a TreeBASE treeblock. A tree block is associated with a L<Submission>
through the sub_treeblock intersection table. L<PhyloTree> objects refer back to treeblock
objects.

=over

=item to_str()

Stringification method. A string representation of a TaxonVariant includes its title.

=cut

sub to_str {
    my $self = shift;
    my $s = $self->SUPER::to_str(@_);
    my $title = $self->title();
    $s .= qq{ ('$title')} if $title;
    return $s;
}

=item recurse()

Traverses associated taxon label sets, submissions and trees and dumps them.

=cut

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

=item consistent()

Consistency check on the invocant tree block, which is considered consistent if its associated
submission and taxon label set are.

=cut

sub consistent {
    my $self = shift;
    my %attr = @_;
    my $OK = 1;
    $OK &&= $attr{'attr_check'}->($self, 'Submissions', \%attr);
    $OK &&= $attr{'attr_check'}->($self, 'TaxonLabelSet', \%attr);
    return $OK;
}

=back

=cut

####################################################################################################
package TreeType;
CIPRES::TreeBase::VeryBadORM->register();

=head2 TreeType

Object representation of a TreeBASE TreeType.

=over

=item to_str()

Stringification method. A string representation of a TreeType includes its description.

=cut

sub to_str {
    my $self = shift();
    my $s = $self->SUPER::to_str();
    return $self->description ? qq{$s "} . $self->description . qq{"} : $s;
}

=back

=head1 SEE ALSO

L<VeryBadORM>

=cut

1;
