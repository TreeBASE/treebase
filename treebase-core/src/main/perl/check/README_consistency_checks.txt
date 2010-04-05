	From: 	rutgeraldo@gmail.com
	Subject: 	Re: [Treebase-devel] Consistency tests...
	Date: 	March 18, 2010 7:30:58 AM EDT
	To: 	vladimir.gapeyev@duke.edu
	Cc: 	treebase-devel@lists.sourceforge.net
	
Hi all,

sorry about the late response. Here's how it works, (to the extent
that I've managed to understand MJD's code): there is a "check"
script. This script needs two arguments: a table name (out of which
MJD's code creates a perl ORM object) and an ID in that table. The
script then tries to construct the logically expected subtended object
hierarchy starting from the focal object. Anything unexpected is
written two STDERR. The most useful way to use this is to say "check
Study $studyID". What I've done in the past is to dump all study IDs
to a file "STUDIES", and then running the following shell script:

#!/bin/bash
studies=`cat STUDIES`
for study in $studies; do
	check Study $study 2> $study.err
	logfilesize=`wc -l $study.err | cut -f1 -d' '`
	if [[ $logfilesize > 0 ]]
	then
		gzip -9 $study.err
	else
		rm $study.err
	fi
done

This will create a $studyID.gz file for every inconsistent study. On
closer examination of these, most inconsistencies lead back to only a
handful of problems, mostly related to incomplete repatriation of
objects from dummy study 22 to their destination study. It's therefore
more informative to bin the inconsistencies by category as opposed to
by study. For this, MJD has written a "digester" script. Assuming you
have a directory full of gzipped study reports, you can then run the
following shell script to categorize the reports:

#!/bin/bash
zips=`ls *.gz`
for zip in $zips; do
	gunzip $zip
	base=`echo $zip | sed -e 's/\.gz//'`
	dir=`echo $base | sed -e 's/\.err//'`
	grep '\*' $base | digester -d $dir
	gzip -9 $base
	cd $dir
	logs=`ls *`
	for log in $logs; do
		cat $log >> ../$log
	done
	cd ../
done

This will create files such as "tree_references_tls_but_its_no", which
lists the PhyloTree objects that reference TaxonLabelSet X, whereas
some of its nodes reference a TaxonLabel that is in TaxonLabelSet Y.
In all these cases, X is still linked to Study 22 (so not repatriated
correctly) while the individual labels and their Y are in the right
place.

By the way, the "gc" script is to be ignored. The idea was that this
would be a garbage collector that could automatically figure out all
inconsistencies and fix them. MJD never quite completed it and/or
worked up the confidence and courage to let it loose on a live
database.

Hope this helps,

Rutger
	