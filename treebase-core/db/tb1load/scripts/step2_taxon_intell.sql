-- Upload Taxon Intelligence dumps into DB. 
-- Input data comes from several locations hard-wired into the 3 \copy commands. 

-- This script uses DDL to create temporary tables and functions, but it takes care 
-- to clean after itself, so there is no net effect on the schema. 

BEGIN TRANSACTION; 

----------------------------------------------------------------------
\echo 'Clean old TI data'
update taxonlabel 
  set taxonvariant_id = NULL;

-- Drop the constraints temporarily. 
-- Their presence slows down immencely deletions from TaxonVariant Taxon. 

alter table taxonlabel drop constraint taxonlabel_fkto_taxonvariant; 

delete from taxonvariant;

alter table taxonlabel
  add constraint taxonlabel_fkto_taxonvariant
  FOREIGN KEY (taxonvariant_id) REFERENCES taxonvariant (taxonvariant_id); 


alter table taxonset_taxon drop constraint taxonset_taxon_fkto_taxon;
alter table taxonlink drop constraint taxonlink_fkto_taxon;
alter table taxonvariant drop constraint taxonvariant_fkto_taxon; 

delete from taxon;

alter table taxonset_taxon 
  add constraint taxonset_taxon_fkto_taxon
  FOREIGN KEY (taxon_id) REFERENCES taxon (taxon_id); 
alter table taxonlink
  add constraint taxonlink_fkto_taxon
  FOREIGN KEY (taxon_id) REFERENCES taxon (taxon_id); 
alter table taxonvariant 
  add constraint taxonvariant_fkto_taxon 
   FOREIGN KEY (taxon_id) REFERENCES taxon (taxon_id); 


----------------------------------------------------------------------
\echo 'Upload Taxa and Taxon Variants into their tables'

\copy taxon (taxon_id, ubionamebankid, name, ncbitaxid) from '../data/TI/taxa.tab'

\copy taxonvariant (taxonvariant_id, taxon_id, namebankid, name, fullname, lexicalqualifier) from '../data/TI/taxon_variants.tab'

update taxon set 
   version = 1,
   tb1legacyid = taxon_id; 
update taxonvariant set 
   version = 1,
   tb1legacyid = taxonvariant_id; 


----------------------------------------------------------------------
\echo 'Upload TaxonLabels into a temporary table tb1taxonlabel'
CREATE TABLE tb1taxonlabel
(
  tb1taxonlabel_id bigint NOT NULL, 
  taxonvariant_id bigint,
  legacy_id character varying(20), 
  taxonlabel character varying(255),
  CONSTRAINT tb1taxonlabel_pkey PRIMARY KEY (tb1taxonlabel_id),
  CONSTRAINT tb1taxonlabel_fkto_taxonvariant FOREIGN KEY (taxonvariant_id)
      REFERENCES taxonvariant (taxonvariant_id) 
);

\copy tb1taxonlabel (tb1taxonlabel_id, taxonvariant_id, legacy_id, taxonlabel) from '../data/TI/taxon_labels.tab' 


----------------------------------------------------------------------
\echo 'Update fields in TaxonLabel with values from  tb1taxonlabel'
update taxonlabel as tb2  
  set taxonvariant_id = tb1.taxonvariant_id, 
      tb1legacyid = tb1.legacy_id, 
      version = 1
from tb1taxonlabel as tb1
where tb2.taxonlabel = tb1.taxonlabel; 


----------------------------------------------------------------------
\echo 'Drop the temporary table tb1taxonlabel'
drop table tb1taxonlabel;


----------------------------------------------------------------------
\echo 'Recompute taxon_id_sequence and taxonvariant_id_sequence'
create or replace function recompute_sequence (tablename varchar) returns void as $$
BEGIN
  execute 'create table tmp_newstart(val bigint)'; 
  execute 'insert into tmp_newstart '||
          'select (div(max('||tablename||'_id), 10000)+1)*10000 from '||tablename;  
  DECLARE 
    start_cursor   cursor for  select * from tmp_newstart;  
    start_value bigint; 
  BEGIN
    open start_cursor; 
    fetch start_cursor into start_value; 
    close start_cursor; 
    execute 'alter sequence '||tablename||'_id_sequence restart with '||start_value;
    execute 'drop table tmp_newstart'; 
    raise notice 'New start value for %_id_sequence: %', tablename, start_value;  
  END; 
END; 
$$ language 'plpgsql'; 

select recompute_sequence ('taxon'); 
select recompute_sequence ('taxonvariant');

drop function recompute_sequence(varchar);

----------------------------------------------------------------------

COMMIT TRANSACTION; 
