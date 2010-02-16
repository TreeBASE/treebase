insert into versionhistory(patchnumber, patchlabel, patchdescription) 
       values (4, 'create-indexes', 
       'Create several indexes to improve query performance.'); 


create index taxon_idx_name 
  on taxon(name); 
create index taxonvariant_id_fullname 
  on taxonvariant(fullname); 
create index taxonlabel_idx_taxonlabel 
  on taxonlabel(taxonlabel);
create index citation_idx_title 
  on citation(title);
create index person_idx_lastname 
  on person(lastname);

create index phylotree_idx_treeblock_id
  on phylotree(treeblock_id);

create index phylotreenode_idx_phylotree_id 
  on phylotreenode(phylotree_id);
create index phylotreenode_idx_taxonlabel_id 
  on phylotreenode(taxonlabel_id);

create index phylotreenode_idx_parent_id
  on phylotreenode(parent_id);
create index phylotreenode_idx_child_id
  on phylotreenode(child_id);
create index phylotreenode_idx_sibling_id
  on phylotreenode(sibling_id);


create index treeblock_idx_taxonlabelset_id 
  on treeblock(taxonlabelset_id); 

create index taxonlabelset_taxonlabel_idx_taxonlabelset_id 
  on taxonlabelset_taxonlabel(taxonlabelset_id); 
create index taxonlabelset_taxonlabel_idx_taxonlabel_id 
  on taxonlabelset_taxonlabel(taxonlabel_id); 

create index taxonlabel_idx_taxonvariant_id 
  on taxonlabel(taxonvariant_id); 
 
