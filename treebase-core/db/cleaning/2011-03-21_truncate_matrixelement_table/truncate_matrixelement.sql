-- make a backup of the matrixelement table just in 
-- case, edit the -h and database name as needed:
-- pg_dump -U treebase_app -h treebase.nescent.org -t matrixelement -a -O treebaseprod > matrixelement_bkup.sql

-- remove foreign key constraints
ALTER TABLE ONLY compound_element DROP CONSTRAINT compound_element_fkto_compound;
ALTER TABLE ONLY compound_element DROP CONSTRAINT compound_element_fkto_element;
ALTER TABLE ONLY itemvalue DROP CONSTRAINT itemvalue_fkto_element;
ALTER TABLE ONLY statemodifier DROP CONSTRAINT statemodifier_fkto_element;

-- delete all records in matrixelement. Be sure to use TRUNCATE instead 
-- of DELETE FROM because this makes it infinitely faster
TRUNCATE matrixelement;

-- reapply all foreign key constraints
ALTER TABLE ONLY compound_element ADD CONSTRAINT compound_element_fkto_compound FOREIGN KEY (compound_id) REFERENCES matrixelement(matrixelement_id);
ALTER TABLE ONLY compound_element ADD CONSTRAINT compound_element_fkto_element FOREIGN KEY (element_id) REFERENCES matrixelement(matrixelement_id);
ALTER TABLE ONLY itemvalue ADD CONSTRAINT itemvalue_fkto_element FOREIGN KEY (element_id) REFERENCES matrixelement(matrixelement_id);
ALTER TABLE ONLY statemodifier ADD CONSTRAINT statemodifier_fkto_element FOREIGN KEY (element_id) REFERENCES matrixelement(matrixelement_id);

