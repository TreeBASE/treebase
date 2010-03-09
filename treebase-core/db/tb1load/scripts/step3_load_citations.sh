#!/bin/bash -e 

source common.sh

CITATIONS=$DATA/citations.txt


echo "######################################################################"
echo "############ Step 3: load citations $CITATIONS            ############"
echo "######################################################################"

CMD="$JAVA org.cipres.treebase.util.CitationDataImporter $CITATIONS"
echo $CMD 
$CMD
