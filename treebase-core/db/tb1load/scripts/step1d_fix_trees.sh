#!/bin/bash -e 

source common.sh

echo "######################################################################"
echo "############ Step 1c: fix counts in trees                 ############"
echo "######################################################################"

CMD="$JAVA org.cipres.treebase.util.SetTreeNChar"
echo $CMD
$CMD
