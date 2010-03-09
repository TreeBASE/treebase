#!/bin/bash

# must be executed from data/scripts

CMD="cp -R ../../../../treebase-web/target/treebase-web/WEB-INF/lib/ ../tb2jars/"
echo $CMD
$CMD

CMD="cp -R ../../../../treebase-core/target/classes/ ../tb2classes/"
echo $CMD
$CMD

