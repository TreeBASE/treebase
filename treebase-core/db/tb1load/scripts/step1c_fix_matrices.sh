#!/bin/bash -e 

source common.sh

echo "######################################################################"
echo "############ Step 1c: fix counts in matrices              ############"
echo "######################################################################"

CMD="$JAVA org.cipres.treebase.util.SetMatrixNChar"
echo $CMD
$CMD

