#!/bin/bash -e 
# -e  Exit immediately if a command exits with a non-zero status.


./step1a_load_matrices.sh

./step1b_load_trees.sh 

./step1c_fix_matrices.sh 

./step1d_fix_trees.sh 

./step1e_load_dump.sh 

./step2_taxon_intell.sh

./step3_load_citations.sh

