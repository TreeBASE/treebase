#!/bin/bash -e 

source common.sh

echo "######################################################################"
echo "############ Step 1d: Loading Study info from the dump    ############"
echo "######################################################################"

CMD="$JAVA org.cipres.treebase.util.AuxiliaryDataImporter $DATA/dump.txt"
echo $CMD
$CMD
