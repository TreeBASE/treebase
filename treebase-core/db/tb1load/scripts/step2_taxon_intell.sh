#!/bin/bash -e 

source common.sh
source dbinfo.sh
 
echo "######################################################################"
echo "############ Step 2: load taxon intelligence              ############"
echo "######################################################################"

psql -f step2_taxon_intell.sql -h $DBHOST -U $DBUSER $DBNAME 

