#!/bin/bash -e

source common.sh

MATRICES=$DATA/characters

echo "######################################################################"
echo "############ Step 1a: load character matrices from $MATRICES #########"
echo "######################################################################"

CMD="$JAVA org.cipres.treebase.util.BulkUpload $MATRICES"
echo $CMD
$CMD
