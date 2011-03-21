Serious performance and stability issues are predicted to arise because the 
matrixelement table is highly normalized and requires and excessive footprint 
when storing data matrices. The c. 6000+ matrices in TreeBASE are taking up 
nearly 200GB of space in this table alone. Empirical testing determined that 
the contents of the matrixelement table are not used when downloading discrete 
character data (whether as NEXUS or as NeXML) because the information is 
obtained from the symbolstring field in the matrixrow table. This is not the case
for matrices of datatype continuous. Since there are no matrices of this type
currently in TreeBASE, we propose to delete all records in the matrixelement table 
and modify the code so as not to create new ones except in cases of continuous data
types. 