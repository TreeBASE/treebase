#!/bin/bash -e 

source common.sh

TREES=$DATA/trees

echo "######################################################################"
echo "############ Step 1b: load trees from $TREES              ############"
echo "######################################################################"

CMD="$JAVA org.cipres.treebase.util.BulkUpload $TREES"
echo $CMD
$CMD
