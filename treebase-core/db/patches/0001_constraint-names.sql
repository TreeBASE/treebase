insert into versionhistory(patchnumber, patchlabel, patchdescription) 
  values (1, 'constraint-names', 
          'Replace system-generated FK constraint names by explicit ones'); 


ALTER TABLE ONLY usertyperecord 
  DROP CONSTRAINT fk136af6164d737e46, 
  ADD CONSTRAINT usertyperecord_fkto_typeset 
    FOREIGN KEY (typeset_id)  REFERENCES typeset(typeset_id);

ALTER TABLE ONLY usertyperecord 
  DROP CONSTRAINT fk136af61692e6a38e, 
  ADD CONSTRAINT usertyperecord_fkto_usertype 
    FOREIGN KEY (usertype_id)  REFERENCES usertype(usertype_id);

ALTER TABLE ONLY analysisstep 
  DROP CONSTRAINT fk21f853a848a2817c, 
  ADD CONSTRAINT analysisstep_fkto_algorithm 
    FOREIGN KEY (algorithm_id)  REFERENCES algorithm(algorithm_id);

ALTER TABLE ONLY analysisstep 
  DROP CONSTRAINT fk21f853a865edd5f8, 
  ADD CONSTRAINT analysisstep_fkto_software 
    FOREIGN KEY (software_id)  REFERENCES software(software_id);

ALTER TABLE ONLY analysisstep 
  DROP CONSTRAINT fk21f853a8d1884dd8, 
  ADD CONSTRAINT analysisstep_fkto_analysis 
    FOREIGN KEY (analysis_id)  REFERENCES analysis(analysis_id);

ALTER TABLE ONLY geneticcoderecord 
  DROP CONSTRAINT fk237932b76cb73fc6, 
  ADD CONSTRAINT geneticcoderecord_fkto_geneticcode 
    FOREIGN KEY (geneticcode_id)  REFERENCES geneticcode(geneticcode_id);

ALTER TABLE ONLY geneticcoderecord 
  DROP CONSTRAINT fk237932b782d8ecce, 
  ADD CONSTRAINT geneticcoderecord_fkto_geneticcodeset 
    FOREIGN KEY (geneticcodeset_id)  REFERENCES geneticcodeset(geneticcodeset_id);

ALTER TABLE ONLY citation_author 
  DROP CONSTRAINT fk24aa55e36707573b, 
  ADD CONSTRAINT citation_author_fkto_authors_person 
    FOREIGN KEY (authors_person_id) REFERENCES person(person_id);

ALTER TABLE ONLY citation_author 
  DROP CONSTRAINT fk24aa55e38e1e4df8, 
  ADD CONSTRAINT citation_author_fkto_citation 
    FOREIGN KEY (citation_id) REFERENCES citation(citation_id);

ALTER TABLE ONLY itemvalue 
  DROP CONSTRAINT fk27b41a1e459091e5, 
  ADD CONSTRAINT itemvalue_fkto_element 
    FOREIGN KEY (element_id)  REFERENCES matrixelement(matrixelement_id);

ALTER TABLE ONLY taxonlabelset 
  DROP CONSTRAINT Fk28d3a5983c572c3c, 
  ADD CONSTRAINT taxonlabelset_fkto_study 
    FOREIGN KEY (study_id)  REFERENCES study(study_id);

ALTER TABLE ONLY citation_editor 
  DROP CONSTRAINT fk2a8955c568f6f619, 
  ADD CONSTRAINT citation_editor_fkto_editors_person 
    FOREIGN KEY (editors_person_id) REFERENCES person(person_id);

ALTER TABLE ONLY citation_editor 
  DROP CONSTRAINT fk2a8955c5ec93f501, 
  ADD CONSTRAINT citation_editor_fkto_citation 
    FOREIGN KEY (citation_id) REFERENCES citation(citation_id);

ALTER TABLE ONLY charset_colrange 
  DROP CONSTRAINT fk2cbd90416d5b50, 
  ADD CONSTRAINT charset_colrange_fkto_charset 
    FOREIGN KEY (charset_id) REFERENCES charset(charset_id);

ALTER TABLE ONLY charset_colrange 
  DROP CONSTRAINT fk2cbd9079a523e6, 
  ADD CONSTRAINT charset_colrange_fkto_columnrange 
    FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);

ALTER TABLE ONLY taxonlabelset_taxonlabel 
  DROP CONSTRAINT fk2eb54b7163ab9fd7, 
  ADD CONSTRAINT taxonlabelset_taxonlabel_fkto_taxonlabel 
    FOREIGN KEY (taxonlabel_id) REFERENCES taxonlabel(taxonlabel_id);

ALTER TABLE ONLY taxonlabelset_taxonlabel 
  DROP CONSTRAINT fk2eb54b71e310471d, 
  ADD CONSTRAINT taxonlabelset_taxonlabel_fkto_taxonlabelset 
    FOREIGN KEY (taxonlabelset_id) REFERENCES taxonlabelset(taxonlabelset_id);

ALTER TABLE ONLY discretecharstate 
  DROP CONSTRAINT fk2eef2802163c67ce, 
  ADD CONSTRAINT discretecharstate_fkto_stateset 
    FOREIGN KEY (stateset_id)  REFERENCES stateset(stateset_id);

ALTER TABLE ONLY discretecharstate 
  DROP CONSTRAINT fk2eef2802c7beaafe, 
  ADD CONSTRAINT discretecharstate_fkto_ancestralstate 
    FOREIGN KEY (ancestralstate_id)  REFERENCES ancestralstate(ancestralstate_id);

ALTER TABLE ONLY discretecharstate 
  DROP CONSTRAINT fk2eef2802fe41a723, 
  ADD CONSTRAINT discretecharstate_fkto_phylochar 
    FOREIGN KEY (phylochar_id)  REFERENCES phylochar(phylochar_id);

ALTER TABLE ONLY codonnoncoding_colrange 
  DROP CONSTRAINT fk307897fc491db20e, 
  ADD CONSTRAINT codonnoncoding_colrange_fkto_codonpositionset 
    FOREIGN KEY (codonpositionset_id) REFERENCES codonpositionset(codonpositionset_id);

ALTER TABLE ONLY codonnoncoding_colrange 
  DROP CONSTRAINT fk307897fc79a523e6, 
  ADD CONSTRAINT codonnoncoding_colrange_fkto_columnrange 
    FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);

ALTER TABLE ONLY compound_element 
  DROP CONSTRAINT fk31183048459091e5,
  ADD CONSTRAINT compound_element_fkto_element 
    FOREIGN KEY (element_id) REFERENCES matrixelement(matrixelement_id);

ALTER TABLE ONLY compound_element 
  DROP CONSTRAINT fk311830485f4f7ceb, 
  ADD CONSTRAINT compound_element_fkto_compound 
    FOREIGN KEY (compound_id) REFERENCES matrixelement(matrixelement_id);

ALTER TABLE ONLY treeset_phylotree 
  DROP CONSTRAINT fk31fc19a7b710cb23, 
  ADD CONSTRAINT treeset_phylotree_fkto_phylotree 
    FOREIGN KEY (phylotree_id) REFERENCES phylotree(phylotree_id);

ALTER TABLE ONLY treeset_phylotree 
  DROP CONSTRAINT fk31fc19a7ec59b1e3, 
  ADD CONSTRAINT treeset_phylotree_fkto_treeset 
    FOREIGN KEY (treeset_id) REFERENCES treeset(treeset_id);

ALTER TABLE ONLY "user"
  DROP CONSTRAINT fk36ebcbe3910672,   
  ADD CONSTRAINT  user_fkto_person
    FOREIGN KEY (person_id) REFERENCES person(person_id);

ALTER TABLE ONLY "user"
  DROP CONSTRAINT fk36ebcbf59dd12,   
  ADD CONSTRAINT user_fkto_userrole
    FOREIGN KEY (userrole_id) REFERENCES userrole(userrole_id);

ALTER TABLE ONLY chargroup 
  DROP CONSTRAINT fk3af18c91aa11dc6, 
  ADD CONSTRAINT chargroup_fkto_charpartition 
    FOREIGN KEY (charpartition_id)  REFERENCES charpartition(charpartition_id);

ALTER TABLE ONLY phylotreenode 
  DROP CONSTRAINT fk3ea79944271a5763, 
  ADD CONSTRAINT phylotreenode_fkto_nodeattribute 
    FOREIGN KEY (nodeattribute_id)  REFERENCES nodeattribute(nodeattribute_id);

ALTER TABLE ONLY phylotreenode 
  DROP CONSTRAINT fk3ea799443c348165, 
  ADD CONSTRAINT phylotreenode_fkto_sibling 
    FOREIGN KEY (sibling_id)  REFERENCES phylotreenode(phylotreenode_id);

ALTER TABLE ONLY phylotreenode 
  DROP CONSTRAINT fk3ea79944434fdccb, 
  ADD CONSTRAINT phylotreenode_fkto_child 
    FOREIGN KEY (child_id)  REFERENCES phylotreenode(phylotreenode_id);

ALTER TABLE ONLY phylotreenode 
  DROP CONSTRAINT fk3ea799445bcc767d, 
  ADD CONSTRAINT phylotreenode_fkto_parent 
    FOREIGN KEY (parent_id)  REFERENCES phylotreenode(phylotreenode_id);

ALTER TABLE ONLY phylotreenode 
  DROP CONSTRAINT fk3ea7994463ab9fd7, 
  ADD CONSTRAINT phylotreenode_fkto_taxonlabel 
    FOREIGN KEY (taxonlabel_id)  REFERENCES taxonlabel(taxonlabel_id);

ALTER TABLE ONLY phylotreenode 
  DROP CONSTRAINT fk3ea79944b710cb23, 
  ADD CONSTRAINT phylotreenode_fkto_phylotree 
    FOREIGN KEY (phylotree_id)  REFERENCES phylotree(phylotree_id);

ALTER TABLE ONLY ancestralstate 
  DROP CONSTRAINT fk3fa14284684f6406, 
  ADD CONSTRAINT ancestralstate_fkto_ancstateset 
    FOREIGN KEY (ancstateset_id)  REFERENCES ancstateset(ancstateset_id);

ALTER TABLE ONLY ancestralstate 
  DROP CONSTRAINT fk3fa14284f4803ce6, 
  ADD CONSTRAINT ancestralstate_fkto_discretecharstate 
    FOREIGN KEY (discretecharstate_id)  REFERENCES discretecharstate(discretecharstate_id);

ALTER TABLE ONLY usertyperrd_colrange 
  DROP CONSTRAINT fk405805dd79a523e6, 
  ADD CONSTRAINT usertyperrd_colrange_fkto_columnrange 
    FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);

ALTER TABLE ONLY usertyperrd_colrange 
  DROP CONSTRAINT fk405805ddc009330e, 
  ADD CONSTRAINT usertyperrd_colrange_fkto_usertyperecord 
    FOREIGN KEY (usertyperecord_id) REFERENCES usertyperecord(usertyperecord_id);

ALTER TABLE ONLY study_nexusfile 
  DROP CONSTRAINT fk47fc1ee53c572c3c, 
  ADD CONSTRAINT study_nexusfile_fkto_study 
    FOREIGN KEY (study_id) REFERENCES study(study_id);

ALTER TABLE ONLY taxonlabelgroup 
  DROP CONSTRAINT fk4ae2663513a813dd, 
  ADD CONSTRAINT taxonlabelgroup_fkto_taxonlabelpartition 
    FOREIGN KEY (taxonlabelpartition_id)  REFERENCES taxonlabelpartition(taxonlabelpartition_id);

ALTER TABLE ONLY study 
  DROP CONSTRAINT fk4b915a9255a519c, 
  ADD CONSTRAINT study_fkto_studystatus 
    FOREIGN KEY (studystatus_id)  REFERENCES studystatus(studystatus_id);

ALTER TABLE ONLY study 
  DROP CONSTRAINT fk4b915a98e1e4df8, 
  ADD CONSTRAINT study_fkto_citation 
    FOREIGN KEY (citation_id)  REFERENCES citation(citation_id);

ALTER TABLE ONLY study 
  DROP CONSTRAINT fk4b915a9bef300b2, 
  ADD CONSTRAINT study_fkto_user 
    FOREIGN KEY (user_id)  REFERENCES "user"(user_id);

ALTER TABLE ONLY codonpositionset 
  DROP CONSTRAINT fk4e501cc2a414944f, 
  ADD CONSTRAINT codonpositionset_fkto_matrix 
    FOREIGN KEY (matrix_id)  REFERENCES matrix(matrix_id);

ALTER TABLE ONLY charweight_colrange 
  DROP CONSTRAINT fk54ca674e79a523e6, 
  ADD CONSTRAINT charweight_colrange_fkto_columnrange 
    FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);

ALTER TABLE ONLY charweight_colrange 
  DROP CONSTRAINT fk54ca674ef9a75ee, 
  ADD CONSTRAINT charweight_colrange_fkto_charweight 
    FOREIGN KEY (charweight_id) REFERENCES charweight(charweight_id);

ALTER TABLE ONLY charset 
  DROP CONSTRAINT fk56d8ed2ca414944f, 
  ADD CONSTRAINT charset_fkto_matrix 
    FOREIGN KEY (matrix_id)  REFERENCES matrix(matrix_id);

ALTER TABLE ONLY matrix_itemdefinition 
  DROP CONSTRAINT fk5717e0e4405a7cee, 
  ADD CONSTRAINT matrix_itemdefinition_fkto_itemdefinition 
    FOREIGN KEY (itemdefinition_id) REFERENCES itemdefinition(itemdefinition_id);

ALTER TABLE ONLY matrix_itemdefinition 
  DROP CONSTRAINT fk5717e0e4ac5c19dd, 
  ADD CONSTRAINT matrix_itemdefinition_fkto_matrix 
    FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);

ALTER TABLE ONLY sub_matrix 
  DROP CONSTRAINT fk5f26a2c055961aee, 
  ADD CONSTRAINT sub_matrix_fkto_matrix 
    FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);

ALTER TABLE ONLY sub_matrix 
  DROP CONSTRAINT fk5f26a2c08dfe4858, 
  ADD CONSTRAINT sub_matrix_fkto_submission 
    FOREIGN KEY (submission_id) REFERENCES submission(submission_id);

ALTER TABLE ONLY taxonlabel 
  DROP CONSTRAINT fk5f548a6a3c1b6f7, 
  ADD CONSTRAINT taxonlabel_fkto_taxonvariant 
    FOREIGN KEY (taxonvariant_id)  REFERENCES taxonvariant(taxonvariant_id);

ALTER TABLE ONLY taxonlabel 
  DROP CONSTRAINT fk5f548a6a3c572c3c, 
  ADD CONSTRAINT taxonlabel_fkto_study 
    FOREIGN KEY (study_id)  REFERENCES study(study_id);

ALTER TABLE ONLY geneticcodeset 
  DROP CONSTRAINT fk6a12f7dca414944f, 
  ADD CONSTRAINT geneticcodeset_fkto_matrix 
    FOREIGN KEY (matrix_id)  REFERENCES matrix(matrix_id);

ALTER TABLE ONLY taxonlabelgroup_taxonlabel 
  DROP CONSTRAINT fk73bbf6b4294b993d, 
  ADD CONSTRAINT taxonlabelgroup_taxonlabel_fkto_taxonlabelgroup 
    FOREIGN KEY (taxonlabelgroup_id) REFERENCES taxonlabelgroup(taxonlabelgroup_id);



ALTER TABLE ONLY taxonlabelgroup_taxonlabel 
  DROP CONSTRAINT fk73bbf6b463ab9fd7, 
  ADD CONSTRAINT taxonlabelgroup_taxonlabel_fkto_taxonlabel 
    FOREIGN KEY (taxonlabel_id) REFERENCES taxonlabel(taxonlabel_id);



ALTER TABLE ONLY phylotree 
  DROP CONSTRAINT fk76f2e4423c572c3c, 
  ADD CONSTRAINT phylotree_fkto_study 
    FOREIGN KEY (study_id)  REFERENCES study(study_id);



ALTER TABLE ONLY phylotree 
  DROP CONSTRAINT fk76f2e442a3015ce3, 
  ADD CONSTRAINT phylotree_fkto_treeattribute 
    FOREIGN KEY (treeattribute_id)  REFERENCES treeattribute(treeattribute_id);



ALTER TABLE ONLY phylotree 
  DROP CONSTRAINT fk76f2e442bfd107c3, 
  ADD CONSTRAINT phylotree_fkto_treeblock 
    FOREIGN KEY (treeblock_id)  REFERENCES treeblock(treeblock_id);



ALTER TABLE ONLY phylotree 
  DROP CONSTRAINT fk76f2e442ea08b443, 
  ADD CONSTRAINT phylotree_fkto_treequality 
    FOREIGN KEY (treequality_id)  REFERENCES treequality(treequality_id);



ALTER TABLE ONLY phylotree 
  DROP CONSTRAINT fk76f2e442f5aea931, 
  ADD CONSTRAINT phylotree_fkto_treetype 
    FOREIGN KEY (treetype_id)  REFERENCES treetype(treetype_id);



ALTER TABLE ONLY phylotree 
  DROP CONSTRAINT fk76f2e442fe2812f1, 
  ADD CONSTRAINT phylotree_fkto_treekind 
    FOREIGN KEY (treekind_id)  REFERENCES treekind(treekind_id);



ALTER TABLE ONLY treenodeedge 
  DROP CONSTRAINT fk7767285d1851763b, 
  ADD CONSTRAINT treenodeedge_fkto_parentnode 
    FOREIGN KEY (parentnode_id)  REFERENCES phylotreenode(phylotreenode_id);



ALTER TABLE ONLY treenodeedge 
  DROP CONSTRAINT fk7767285d27fd0589, 
  ADD CONSTRAINT treenodeedge_fkto_childnode 
    FOREIGN KEY (childnode_id)  REFERENCES phylotreenode(phylotreenode_id);



ALTER TABLE ONLY stateset 
  DROP CONSTRAINT fk7d38523150cbab47, 
  ADD CONSTRAINT stateset_fkto_matrix 
    FOREIGN KEY (matrix_id)  REFERENCES matrix(matrix_id);



ALTER TABLE ONLY cstreenode 
  DROP CONSTRAINT fk82b9ab10c6999fce, 
  ADD CONSTRAINT cstreenode_fkto_cstree 
    FOREIGN KEY (cstree_id)  REFERENCES usertype(usertype_id);



ALTER TABLE ONLY cstreenode 
  DROP CONSTRAINT fk82b9ab10f4803ce6, 
  ADD CONSTRAINT cstreenode_fkto_discretecharstate 
    FOREIGN KEY (discretecharstate_id)  REFERENCES discretecharstate(discretecharstate_id);



ALTER TABLE ONLY cstreenode 
  DROP CONSTRAINT fk82b9ab10f572a92, 
  ADD CONSTRAINT cstreenode_fkto_parentnode 
    FOREIGN KEY (parentnode_id)  REFERENCES cstreenode(cstreenode_id);



ALTER TABLE ONLY sub_taxonlabel 
  DROP CONSTRAINT fk86909e963ab9fd7, 
  ADD CONSTRAINT sub_taxonlabel_fkto_taxonlabel 
    FOREIGN KEY (taxonlabel_id) REFERENCES taxonlabel(taxonlabel_id);



ALTER TABLE ONLY sub_taxonlabel 
  DROP CONSTRAINT fk86909e98dfe4858, 
  ADD CONSTRAINT sub_taxonlabel_fkto_submission 
    FOREIGN KEY (submission_id) REFERENCES submission(submission_id);



ALTER TABLE ONLY matrix 
  DROP CONSTRAINT fk87208bc13c572c3c, 
  ADD CONSTRAINT matrix_fkto_study 
    FOREIGN KEY (study_id)  REFERENCES study(study_id);



ALTER TABLE ONLY matrix 
  DROP CONSTRAINT fk87208bc145534a9c, 
  ADD CONSTRAINT matrix_fkto_charset 
    FOREIGN KEY (charset_id)  REFERENCES charset(charset_id);



ALTER TABLE ONLY matrix 
  DROP CONSTRAINT fk87208bc1491db20e, 
  ADD CONSTRAINT matrix_fkto_codonpositionset 
    FOREIGN KEY (codonpositionset_id)  REFERENCES codonpositionset(codonpositionset_id);



ALTER TABLE ONLY matrix 
  DROP CONSTRAINT fk87208bc14ce484ae, 
  ADD CONSTRAINT matrix_fkto_matrixdatatype 
    FOREIGN KEY (matrixdatatype_id)  REFERENCES matrixdatatype(matrixdatatype_id);



ALTER TABLE ONLY matrix 
  DROP CONSTRAINT fk87208bc14d737e46, 
  ADD CONSTRAINT matrix_fkto_typeset 
    FOREIGN KEY (typeset_id)  REFERENCES typeset(typeset_id);



ALTER TABLE ONLY matrix 
  DROP CONSTRAINT fk87208bc1684f6406, 
  ADD CONSTRAINT matrix_fkto_ancstateset 
    FOREIGN KEY (ancstateset_id)  REFERENCES ancstateset(ancstateset_id);



ALTER TABLE ONLY matrix 
  DROP CONSTRAINT fk87208bc191bd3c8e, 
  ADD CONSTRAINT matrix_fkto_matrixkind 
    FOREIGN KEY (matrixkind_id)  REFERENCES matrixkind(matrixkind_id);



ALTER TABLE ONLY matrix 
  DROP CONSTRAINT fk87208bc1e310471d, 
  ADD CONSTRAINT matrix_fkto_taxonlabelset 
    FOREIGN KEY (taxonlabelset_id)  REFERENCES taxonlabelset(taxonlabelset_id);



ALTER TABLE ONLY matrix 
  DROP CONSTRAINT fk87208bc1e9b425a6, 
  ADD CONSTRAINT matrix_fkto_charweightset 
    FOREIGN KEY (charweightset_id)  REFERENCES charweightset(charweightset_id);



ALTER TABLE ONLY ancstateset 
  DROP CONSTRAINT fk879339e7a414944f, 
  ADD CONSTRAINT ancstateset_fkto_matrix 
    FOREIGN KEY (matrix_id)  REFERENCES matrix(matrix_id);



ALTER TABLE ONLY stepmatrixelement 
  DROP CONSTRAINT fk891100af6e972a88, 
  ADD CONSTRAINT stepmatrixelement_fkto_state1 
    FOREIGN KEY (state1_id)  REFERENCES discretecharstate(discretecharstate_id);



ALTER TABLE ONLY stepmatrixelement 
  DROP CONSTRAINT fk891100af6e979ee7, 
  ADD CONSTRAINT stepmatrixelement_fkto_state2 
    FOREIGN KEY (state2_id)  REFERENCES discretecharstate(discretecharstate_id);



ALTER TABLE ONLY stepmatrixelement 
  DROP CONSTRAINT fk891100af9e8198e, 
  ADD CONSTRAINT stepmatrixelement_fkto_stepmatrix 
    FOREIGN KEY (stepmatrix_id)  REFERENCES usertype(usertype_id);



ALTER TABLE ONLY analyzeddata 
  DROP CONSTRAINT fk8c9618424ba97f78, 
  ADD CONSTRAINT analyzeddata_fkto_analysisstep 
    FOREIGN KEY (analysisstep_id)  REFERENCES analysisstep(analysisstep_id);



ALTER TABLE ONLY analyzeddata 
  DROP CONSTRAINT fk8c96184255961aee, 
  ADD CONSTRAINT analyzeddata_fkto_matrix 
    FOREIGN KEY (matrix_id)  REFERENCES matrix(matrix_id);



ALTER TABLE ONLY analyzeddata 
  DROP CONSTRAINT fk8c961842b710cb23, 
  ADD CONSTRAINT analyzeddata_fkto_phylotree 
    FOREIGN KEY (phylotree_id)  REFERENCES phylotree(phylotree_id);



ALTER TABLE ONLY charweight 
  DROP CONSTRAINT fk8cc7694ee9b425a6, 
  ADD CONSTRAINT charweight_fkto_charweightset 
    FOREIGN KEY (charweightset_id)  REFERENCES charweightset(charweightset_id);



ALTER TABLE ONLY distancematrixelement 
  DROP CONSTRAINT fk92d3dde61f08bae7, 
  ADD CONSTRAINT distancematrixelement_fkto_rowlabel 
    FOREIGN KEY (rowlabel_id)  REFERENCES taxonlabel(taxonlabel_id);



ALTER TABLE ONLY distancematrixelement 
  DROP CONSTRAINT fk92d3dde64b8ef343, 
  ADD CONSTRAINT distancematrixelement_fkto_matrix 
    FOREIGN KEY (matrix_id)  REFERENCES matrix(matrix_id);



ALTER TABLE ONLY distancematrixelement 
  DROP CONSTRAINT fk92d3dde6c1429863, 
  ADD CONSTRAINT distancematrixelement_fkto_columnlabel 
    FOREIGN KEY (columnlabel_id)  REFERENCES taxonlabel(taxonlabel_id);



ALTER TABLE ONLY sub_treeblock
  DROP CONSTRAINT fk94d508308dfe4858, 
  ADD CONSTRAINT  sub_treeblock_fkto_submission
    FOREIGN KEY (submission_id) REFERENCES submission(submission_id);



ALTER TABLE ONLY sub_treeblock 
  DROP CONSTRAINT fk94d50830bfd107c3, 
  ADD CONSTRAINT sub_treeblock_fkto_treeblock 
    FOREIGN KEY (treeblock_id) REFERENCES treeblock(treeblock_id);



ALTER TABLE ONLY charweightset 
  DROP CONSTRAINT fk99b8ac34a414944f, 
  ADD CONSTRAINT charweightset_fkto_matrix 
    FOREIGN KEY (matrix_id)  REFERENCES matrix(matrix_id);



ALTER TABLE ONLY matrixcolumn 
  DROP CONSTRAINT fk9b0be57a414944f, 
  ADD CONSTRAINT matrixcolumn_fkto_matrix 
    FOREIGN KEY (matrix_id)  REFERENCES matrix(matrix_id);



ALTER TABLE ONLY matrixcolumn 
  DROP CONSTRAINT fk9b0be57bed40086, 
  ADD CONSTRAINT matrixcolumn_fkto_stateformat 
    FOREIGN KEY (stateformat_id)  REFERENCES stateformat(stateformat_id);



ALTER TABLE ONLY matrixcolumn 
  DROP CONSTRAINT fk9b0be57f5deca46, 
  ADD CONSTRAINT matrixcolumn_fkto_phylochar 
    FOREIGN KEY (phylochar_id)  REFERENCES phylochar(phylochar_id);



ALTER TABLE ONLY submission 
  DROP CONSTRAINT fka120274c3c572c3c, 
  ADD CONSTRAINT submission_fkto_study 
    FOREIGN KEY (study_id)  REFERENCES study(study_id);



ALTER TABLE ONLY submission 
  DROP CONSTRAINT fka120274cbef300b2, 
  ADD CONSTRAINT submission_fkto_user 
    FOREIGN KEY (user_id)  REFERENCES "user"(user_id);



ALTER TABLE ONLY coderecord_colrange 
  DROP CONSTRAINT fka3e6c61e79a523e6, 
  ADD CONSTRAINT coderecord_colrange_fkto_columnrange 
    FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);



ALTER TABLE ONLY coderecord_colrange 
  DROP CONSTRAINT fka3e6c61edc51c546, 
  ADD CONSTRAINT coderecord_colrange_fkto_geneticcoderecord 
    FOREIGN KEY (geneticcoderecord_id) REFERENCES geneticcoderecord(geneticcoderecord_id);



ALTER TABLE ONLY treeblock 
  DROP CONSTRAINT fka826f38fe310471d, 
  ADD CONSTRAINT treeblock_fkto_taxonlabelset 
    FOREIGN KEY (taxonlabelset_id)  REFERENCES taxonlabelset(taxonlabelset_id);



ALTER TABLE ONLY contancstate_value 
  DROP CONSTRAINT fka84b8c3fd450d2bd, 
  ADD CONSTRAINT contancstate_value_fkto_ancstate 
    FOREIGN KEY (ancstate_id) REFERENCES ancestralstate(ancestralstate_id);



ALTER TABLE ONLY treegroup 
  DROP CONSTRAINT fka870258180cbd223, 
  ADD CONSTRAINT treegroup_fkto_treepartition 
    FOREIGN KEY (treepartition_id)  REFERENCES treepartition(treepartition_id);



ALTER TABLE ONLY chargroup_colrange 
  DROP CONSTRAINT fkb2d8c29379a523e6, 
  ADD CONSTRAINT chargroup_colrange_fkto_columnrange 
    FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);



ALTER TABLE ONLY chargroup_colrange 
  DROP CONSTRAINT fkb2d8c293971fffa6, 
  ADD CONSTRAINT chargroup_colrange_fkto_chargroup 
    FOREIGN KEY (chargroup_id) REFERENCES chargroup(chargroup_id);



ALTER TABLE ONLY algorithm 
  DROP CONSTRAINT fkb388c44f92e6a38e, 
  ADD CONSTRAINT algorithm_fkto_usertype 
    FOREIGN KEY (usertype_id)  REFERENCES usertype(usertype_id);



ALTER TABLE ONLY algorithm 
  DROP CONSTRAINT fkb388c44f9698d32e, 
  ADD CONSTRAINT algorithm_fkto_polytcount 
    FOREIGN KEY (polytcount_id)  REFERENCES polytcount(polytcount_id);



ALTER TABLE ONLY algorithm 
  DROP CONSTRAINT fkb388c44fc6e814e6, 
  ADD CONSTRAINT algorithm_fkto_gapmode 
    FOREIGN KEY (gapmode_id)  REFERENCES gapmode(gapmode_id);



ALTER TABLE ONLY matrixcolumn_itemdefinition 
  DROP CONSTRAINT fkb556f8e405a7cee, 
  ADD CONSTRAINT matrixcolumn_itemdefinition_fkto_itemdefinition 
    FOREIGN KEY (itemdefinition_id) REFERENCES itemdefinition(itemdefinition_id);



ALTER TABLE ONLY matrixcolumn_itemdefinition 
  DROP CONSTRAINT fkb556f8e8b2e884e, 
  ADD CONSTRAINT matrixcolumn_itemdefinition_fkto_matrixcolumn 
    FOREIGN KEY (matrixcolumn_id) REFERENCES matrixcolumn(matrixcolumn_id);



ALTER TABLE ONLY taxonlink 
  DROP CONSTRAINT fkc102d9a41de2fcdd, 
  ADD CONSTRAINT taxonlink_fkto_taxon 
    FOREIGN KEY (taxon_id)  REFERENCES taxon(taxon_id);



ALTER TABLE ONLY taxonlink 
  DROP CONSTRAINT fkc102d9a43bf5f2f7, 
  ADD CONSTRAINT taxonlink_fkto_taxonauthority 
    FOREIGN KEY (taxonauthority_id)  REFERENCES taxonauthority(taxonauthority_id);



ALTER TABLE ONLY codonchar3_colrange 
  DROP CONSTRAINT fkc2edb736491db20e, 
  ADD CONSTRAINT codonchar3_colrange_fkto_codonpositionset 
    FOREIGN KEY (codonpositionset_id) REFERENCES codonpositionset(codonpositionset_id);



ALTER TABLE ONLY codonchar3_colrange 
  DROP CONSTRAINT fkc2edb73679a523e6, 
  ADD CONSTRAINT codonchar3_colrange_fkto_columnrange 
    FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);



ALTER TABLE ONLY taxonset_taxon 
  DROP CONSTRAINT fkc656c90313c28457, 
  ADD CONSTRAINT taxonset_taxon_fkto_taxonset 
    FOREIGN KEY (taxonset_id) REFERENCES taxonset(taxonset_id);



ALTER TABLE ONLY taxonset_taxon 
  DROP CONSTRAINT fkc656c9031de2fcdd, 
  ADD CONSTRAINT taxonset_taxon_fkto_taxon 
    FOREIGN KEY (taxon_id) REFERENCES taxon(taxon_id);



ALTER TABLE ONLY leftchangeset_charstate 
  DROP CONSTRAINT fkc887720775763eae, 
  ADD CONSTRAINT leftchangeset_charstate_fkto_statechangeset 
    FOREIGN KEY (statechangeset_id) REFERENCES statechangeset(statechangeset_id);



ALTER TABLE ONLY leftchangeset_charstate 
  DROP CONSTRAINT fkc8877207f4803ce6, 
  ADD CONSTRAINT leftchangeset_charstate_fkto_discretecharstate 
    FOREIGN KEY (discretecharstate_id) REFERENCES discretecharstate(discretecharstate_id);



ALTER TABLE ONLY rightchangeset_charstate 
  DROP CONSTRAINT fkc8afc9f275763eae, 
  ADD CONSTRAINT rightchangeset_charstate_fkto_statechangeset 
    FOREIGN KEY (statechangeset_id) REFERENCES statechangeset(statechangeset_id);



ALTER TABLE ONLY rightchangeset_charstate 
  DROP CONSTRAINT fkc8afc9f2f4803ce6, 
  ADD CONSTRAINT rightchangeset_charstate_fkto_discretecharstate 
    FOREIGN KEY (discretecharstate_id) REFERENCES discretecharstate(discretecharstate_id);



ALTER TABLE ONLY codonchar2_colrange 
  DROP CONSTRAINT fkcea44617491db20e, 
  ADD CONSTRAINT codonchar2_colrange_fkto_codonpositionset 
    FOREIGN KEY (codonpositionset_id) REFERENCES codonpositionset(codonpositionset_id);



ALTER TABLE ONLY codonchar2_colrange 
  DROP CONSTRAINT fkcea4461779a523e6, 
  ADD CONSTRAINT codonchar2_colrange_fkto_columnrange 
    FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);



ALTER TABLE ONLY statemodifier 
  DROP CONSTRAINT fkd157be48459091e5, 
  ADD CONSTRAINT statemodifier_fkto_element 
    FOREIGN KEY (element_id)  REFERENCES matrixelement(matrixelement_id);



ALTER TABLE ONLY statemodifier 
  DROP CONSTRAINT fkd157be48bed40086, 
  ADD CONSTRAINT statemodifier_fkto_stateformat 
    FOREIGN KEY (stateformat_id)  REFERENCES stateformat(stateformat_id);



ALTER TABLE ONLY statemodifier 
  DROP CONSTRAINT fkd157be48f4803ce6, 
  ADD CONSTRAINT statemodifier_fkto_discretecharstate 
    FOREIGN KEY (discretecharstate_id)  REFERENCES discretecharstate(discretecharstate_id);



ALTER TABLE ONLY citation 
  DROP CONSTRAINT fkd8a7fae74c983658, 
  ADD CONSTRAINT citation_fkto_citationstatus 
    FOREIGN KEY (citationstatus_id)  REFERENCES citationstatus(citationstatus_id);



ALTER TABLE ONLY codonchar1_colrange 
  DROP CONSTRAINT fkda5ad4f8491db20e, 
  ADD CONSTRAINT codonchar1_colrange_fkto_codonpositionset 
    FOREIGN KEY (codonpositionset_id) REFERENCES codonpositionset(codonpositionset_id);



ALTER TABLE ONLY codonchar1_colrange 
  DROP CONSTRAINT fkda5ad4f879a523e6, 
  ADD CONSTRAINT codonchar1_colrange_fkto_columnrange 
    FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);



ALTER TABLE ONLY charpartition 
  DROP CONSTRAINT fkdea9f834a414944f, 
  ADD CONSTRAINT charpartition_fkto_matrix 
    FOREIGN KEY (matrix_id)  REFERENCES matrix(matrix_id);



ALTER TABLE ONLY matrixrow 
  DROP CONSTRAINT fke4688e5963ab9fd7, 
  ADD CONSTRAINT matrixrow_fkto_taxonlabel 
    FOREIGN KEY (taxonlabel_id) REFERENCES taxonlabel(taxonlabel_id);



ALTER TABLE ONLY matrixrow 
  DROP CONSTRAINT fke4688e59a414944f, 
  ADD CONSTRAINT matrixrow_fkto_matrix 
    FOREIGN KEY (matrix_id)  REFERENCES matrix(matrix_id);



ALTER TABLE ONLY taxonvariant 
  DROP CONSTRAINT fkee3d127b1de2fcdd, 
  ADD CONSTRAINT taxonvariant_fkto_taxon 
    FOREIGN KEY (taxon_id)  REFERENCES taxon(taxon_id);



ALTER TABLE ONLY rowsegment 
  DROP CONSTRAINT fkee9ae81963ab9fd7, 
  ADD CONSTRAINT rowsegment_fkto_taxonlabel 
    FOREIGN KEY (taxonlabel_id)  REFERENCES taxonlabel(taxonlabel_id);



ALTER TABLE ONLY rowsegment 
  DROP CONSTRAINT fkee9ae819e7b3cda6, 
  ADD CONSTRAINT rowsegment_fkto_matrixrow 
    FOREIGN KEY (matrixrow_id)  REFERENCES matrixrow(matrixrow_id);



ALTER TABLE ONLY analysis 
  DROP CONSTRAINT fkf19622dc3c572c3c, 
  ADD CONSTRAINT analysis_fkto_study 
    FOREIGN KEY (study_id)  REFERENCES study(study_id);



ALTER TABLE ONLY typeset 
  DROP CONSTRAINT fkf7f2b6c8a414944f, 
  ADD CONSTRAINT typeset_fkto_matrix 
    FOREIGN KEY (matrix_id)  REFERENCES matrix(matrix_id);



ALTER TABLE ONLY treegroup_phylotree
  DROP CONSTRAINT fkfb7d7045e56a83, 
  ADD CONSTRAINT  treegroup_phylotree_fkto_treegroup
    FOREIGN KEY (treegroup_id) REFERENCES treegroup(treegroup_id);



ALTER TABLE ONLY treegroup_phylotree
  DROP CONSTRAINT  fkfb7d704b710cb23, 
  ADD CONSTRAINT  treegroup_phylotree_fkto_phylotree
    FOREIGN KEY (phylotree_id) REFERENCES phylotree(phylotree_id);



ALTER TABLE ONLY matrixdatatype
  DROP CONSTRAINT  matrixdatatype_phylochar_id_fkey, 
  ADD CONSTRAINT  matrixdatatype_fkto_phylochar
    FOREIGN KEY (phylochar_id) REFERENCES phylochar(phylochar_id);


ALTER TABLE ONLY phylotree
  DROP CONSTRAINT rootnode_id_fk, 
  ADD CONSTRAINT phylotree_fk_rootnode
    FOREIGN KEY (rootnode_id) REFERENCES phylotreenode(phylotreenode_id);


