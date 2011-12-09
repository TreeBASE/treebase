CREATE TABLE algorithm
(
  "type" character(1) NOT NULL,
  algorithm_id bigint NOT NULL,
  "version" integer,
  description character varying(2000),
  propertyname character varying(255),
  propertyvalue character varying(255),
  usertype_id bigint,
  gapmode_id bigint,
  polytcount_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE algorithm OWNER TO postgres;
CREATE SEQUENCE algorithm_id_sequence;
ALTER TABLE algorithm ALTER COLUMN algorithm_id SET DEFAULT nextval('algorithm_id_sequence');
-- alter sequence ancestralstate_id_sequence restart with 4885;

CREATE TABLE analysis
(
  analysis_id bigint NOT NULL,
  "version" integer,
  "name" character varying(255),
  notes character varying(2000),
  validated smallint,
  study_id bigint,
  analysis_order integer
)
WITH (OIDS=FALSE);
ALTER TABLE analysis OWNER TO postgres;
CREATE SEQUENCE analysis_id_sequence;
ALTER TABLE analysis ALTER COLUMN analysis_id SET DEFAULT nextval('analysis_id_sequence');
--  alter sequence analysis_id_sequence restart with 4887;

CREATE TABLE analysisstep
(
  analysisstep_id bigint NOT NULL,
  "version" integer,
  commands character varying(2000),
  "name" character varying(255),
  notes character varying(2000),
  algorithm_id bigint,
  analysis_id bigint,
  software_id bigint,
  step_order integer,
  tb_analysisid character varying(34)
)
WITH (OIDS=FALSE);
ALTER TABLE analysisstep OWNER TO postgres;
CREATE SEQUENCE analysisstep_id_sequence;
ALTER TABLE analysisstep ALTER COLUMN analysisstep_id SET DEFAULT nextval('analysisstep_id_sequence');
-- alter sequence analysisstep_id_sequence restart with 4883;

CREATE TABLE analyzeddata
(
  "type" character(1) NOT NULL,
  analyzeddata_id bigint NOT NULL,
  "version" integer,
  "input" smallint,
  notes character varying(2000),
  treelength integer,
  analysisstep_id bigint NOT NULL,
  matrix_id bigint,
  phylotree_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE analyzeddata OWNER TO postgres;
CREATE SEQUENCE analyzeddata_id_sequence;
ALTER TABLE analyzeddata ALTER COLUMN analyzeddata_id SET DEFAULT nextval('analyzeddata_id_sequence');
-- alter sequence analyzeddata_id_sequence restart with 11063;

CREATE TABLE ancestralstate
(
  "type" character(1) NOT NULL,
  ancestralstate_id bigint NOT NULL,
  "version" integer,
  title character varying(255),
  ancvalue character varying(255),
  discretecharstate_id bigint,
  ancstateset_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE ancestralstate OWNER TO postgres;
CREATE SEQUENCE ancestralstate_id_sequence;
ALTER TABLE ancestralstate ALTER COLUMN ancestralstate_id SET DEFAULT nextval('ancestralstate_id_sequence');
-- alter sequence ancestralstate_id_sequence restart with 1;

CREATE TABLE ancstateset
(
  ancstateset_id bigint NOT NULL,
  "version" integer,
  title character varying(255),
  matrix_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE ancstateset OWNER TO postgres;
CREATE SEQUENCE ancstateset_id_sequence;
ALTER TABLE ancstateset ALTER COLUMN ancstateset_id SET DEFAULT nextval('ancstateset_id_sequence');
-- alter sequence ancestralstate_id_sequence restart with 1;

CREATE TABLE chargroup_colrange
(
  chargroup_id bigint NOT NULL,
  columnrange_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE chargroup_colrange OWNER TO postgres;

CREATE TABLE chargroup
(
  chargroup_id bigint NOT NULL,
  "version" integer,
  title character varying(255),
  charpartition_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE chargroup OWNER TO postgres;
CREATE SEQUENCE chargroup_id_sequence;
ALTER TABLE chargroup ALTER COLUMN chargroup_id SET DEFAULT nextval('chargroup_id_sequence');
-- alter sequence ancestralstate_id_sequence restart with 1011;

CREATE TABLE charpartition
(
  charpartition_id bigint NOT NULL,
  "version" integer,
  title character varying(255),
  matrix_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE charpartition OWNER TO postgres;
CREATE SEQUENCE charpartition_id_sequence;
ALTER TABLE charpartition ALTER COLUMN charpartition_id SET DEFAULT nextval('charpartition_id_sequence');
-- alter sequence charpartition_id_sequence restart with 429;

CREATE TABLE charset_colrange
(
  charset_id bigint NOT NULL,
  columnrange_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE charset_colrange OWNER TO postgres;

CREATE TABLE charset
(
  "type" character(1) NOT NULL,
  charset_id bigint NOT NULL,
  "version" integer,
  title character varying(255),
  matrix_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE charset OWNER TO postgres;
CREATE SEQUENCE charset_id_sequence;
ALTER TABLE charset ALTER COLUMN charset_id SET DEFAULT nextval('charset_id_sequence');
-- alter sequence ancestralstate_id_sequence restart with 6335;

CREATE TABLE charweight_colrange
(
  charweight_id bigint NOT NULL,
  columnrange_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE charweight_colrange OWNER TO postgres;

CREATE TABLE charweight
(
  "type" character(1) NOT NULL,
  charweight_id bigint NOT NULL,
  "version" integer,
  weight integer,
  realweight double precision,
  charweightset_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE charweight OWNER TO postgres;
CREATE SEQUENCE charweight_id_sequence;
ALTER TABLE charweight ALTER COLUMN charweight_id SET DEFAULT nextval('charweight_id_sequence');
-- alter sequence charweight_id_sequence restart with 1;

CREATE TABLE charweightset
(
  charweightset_id bigint NOT NULL,
  "version" integer,
  title character varying(255),
  matrix_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE charweightset OWNER TO postgres;
CREATE SEQUENCE charweightset_id_sequence;
ALTER TABLE charweightset ALTER COLUMN charweightset_id SET DEFAULT nextval('charweightset_id_sequence');
-- alter sequence charweightset_id_sequence restart with 208;

CREATE TABLE citation_author
(
  citation_id bigint NOT NULL,
  authors_person_id bigint NOT NULL,
  author_order integer NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE citation_author OWNER TO postgres;

CREATE TABLE citation_editor
(
  citation_id bigint NOT NULL,
  editors_person_id bigint NOT NULL,
  editor_order integer NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE citation_editor OWNER TO postgres;

CREATE TABLE citation
(
  "type" character(1) NOT NULL,
  citation_id bigint NOT NULL,
  "version" integer,
  pmid character varying(255),
  url character varying(255),
  abstract character varying(10000),
  doi character varying(255),
  keywords character varying(255),
  pages character varying(255),
  publishyear integer,
  published smallint,
  title character varying(500),
  issue character varying(255),
  journal character varying(255),
  volume character varying(255),
  isbn character varying(255),
  booktitle character varying(255),
  city character varying(255),
  publisher character varying(255),
  citationstatus_id bigint,
  tb_studyid character varying(30)
)
WITH (OIDS=FALSE);
ALTER TABLE citation OWNER TO postgres;
CREATE SEQUENCE citation_id_sequence;
ALTER TABLE citation ALTER COLUMN citation_id SET DEFAULT nextval('citation_id_sequence');
-- alter sequence citation_id_sequence restart with 18422;

CREATE TABLE citationstatus
(
  citationstatus_id bigint NOT NULL,
  "version" integer,
  description character varying(50)
)
WITH (OIDS=FALSE);
ALTER TABLE citationstatus OWNER TO postgres;
CREATE SEQUENCE citationstatus_id_sequence;
ALTER TABLE citationstatus ALTER COLUMN citationstatus_id SET DEFAULT nextval('citationstatus_id_sequence');
-- alter sequence citationstatus_id_sequence restart with 6;

CREATE TABLE coderecord_colrange
(
  geneticcoderecord_id bigint NOT NULL,
  columnrange_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE coderecord_colrange OWNER TO postgres;

CREATE TABLE codonchar1_colrange
(
  codonpositionset_id bigint NOT NULL,
  columnrange_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE codonchar1_colrange OWNER TO postgres;

CREATE TABLE codonchar2_colrange
(
  codonpositionset_id bigint NOT NULL,
  columnrange_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE codonchar2_colrange OWNER TO postgres;

CREATE TABLE codonchar3_colrange
(
  codonpositionset_id bigint NOT NULL,
  columnrange_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE codonchar3_colrange OWNER TO postgres;

CREATE TABLE codonnoncoding_colrange
(
  codonpositionset_id bigint NOT NULL,
  columnrange_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE codonnoncoding_colrange OWNER TO postgres;

CREATE TABLE codonpositionset
(
  codonpositionset_id bigint NOT NULL,
  "version" integer,
  title character varying(255),
  matrix_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE codonpositionset OWNER TO postgres;
CREATE SEQUENCE codonpositionset_id_sequence;
ALTER TABLE codonpositionset ALTER COLUMN codonpositionset_id SET DEFAULT nextval('codonpositionset_id_sequence');
-- alter sequence codonpositionset_id_sequence restart with 4495;

CREATE TABLE columnrange
(
  columnrange_id bigint NOT NULL,
  "version" integer,
  endcolindex integer,
  repeatinterval integer,
  startcolindex integer
)
WITH (OIDS=FALSE);
ALTER TABLE columnrange OWNER TO postgres;
CREATE SEQUENCE columnrange_id_sequence;
ALTER TABLE columnrange ALTER COLUMN columnrange_id SET DEFAULT nextval('columnrange_id_sequence');
-- alter sequence columnrange_id_sequence restart with 255521;

CREATE TABLE compound_element
(
  compound_id bigint NOT NULL,
  element_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE compound_element OWNER TO postgres;

CREATE TABLE contancstate_value
(
  ancstate_id bigint NOT NULL,
  element character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE contancstate_value OWNER TO postgres;

CREATE TABLE cstreenode
(
  cstreenode_id bigint NOT NULL,
  "version" integer,
  discretecharstate_id bigint,
  parentnode_id bigint,
  cstree_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE cstreenode OWNER TO postgres;
CREATE SEQUENCE cstreenode_id_sequence;
ALTER TABLE cstreenode ALTER COLUMN cstreenode_id SET DEFAULT nextval('cstreenode_id_sequence');
-- alter sequence cstreenode_id_sequence restart with 1;

CREATE TABLE discretecharstate
(
  discretecharstate_id bigint NOT NULL,
  "version" integer,
  description character varying(255),
  notes character varying(255),
  symbol character(1),
  phylochar_id bigint NOT NULL,
  stateset_id bigint,
  ancestralstate_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE discretecharstate OWNER TO postgres;
CREATE SEQUENCE discretecharstate_id_sequence;
ALTER TABLE discretecharstate ALTER COLUMN discretecharstate_id SET DEFAULT nextval('discretecharstate_id_sequence');
-- alter sequence discretecharstate_id_sequence restart with 169787;

CREATE TABLE distancematrixelement
(
  distancematrixelement_id bigint NOT NULL,
  "version" integer,
  distance double precision,
  columnlabel_id bigint,
  matrix_id bigint NOT NULL,
  rowlabel_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE distancematrixelement OWNER TO postgres;
CREATE SEQUENCE distancematrixelement_id_sequence;
ALTER TABLE distancematrixelement ALTER COLUMN distancematrixelement_id SET DEFAULT nextval('distancematrixelement_id_sequence');
-- alter sequence distancematrixelement_id_sequence restart with 1;

CREATE TABLE gapmode
(
  gapmode_id bigint NOT NULL,
  "version" integer,
  description character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE gapmode OWNER TO postgres;
CREATE SEQUENCE gapmode_id_sequence;
ALTER TABLE gapmode ALTER COLUMN gapmode_id SET DEFAULT nextval('gapmode_id_sequence');
-- alter sequence gapmode_id_sequence restart with 3;

CREATE TABLE geneticcode
(
  geneticcode_id bigint NOT NULL,
  "version" integer,
  codedescription character varying(1000),
  codeorder character varying(255),
  extensions character varying(255),
  nucorder character varying(255),
  predefined smallint,
  title character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE geneticcode OWNER TO postgres;
CREATE SEQUENCE geneticcode_id_sequence;
ALTER TABLE geneticcode ALTER COLUMN geneticcode_id SET DEFAULT nextval('geneticcode_id_sequence');
-- alter sequence geneticcode_id_sequence restart with 1;

CREATE TABLE geneticcoderecord
(
  geneticcoderecord_id bigint NOT NULL,
  "version" integer,
  geneticcode_id bigint,
  geneticcodeset_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE geneticcoderecord OWNER TO postgres;
CREATE SEQUENCE geneticcoderecord_id_sequence;
ALTER TABLE geneticcoderecord ALTER COLUMN geneticcoderecord_id SET DEFAULT nextval('geneticcoderecord_id_sequence');
-- alter sequence geneticcoderecord_id_sequence restart with 1;

CREATE TABLE geneticcodeset
(
  geneticcodeset_id bigint NOT NULL,
  "version" integer,
  format character varying(255),
  title character varying(255),
  matrix_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE geneticcodeset OWNER TO postgres;
CREATE SEQUENCE geneticcodeset_id_sequence;
ALTER TABLE geneticcodeset ALTER COLUMN geneticcodeset_id SET DEFAULT nextval('geneticcodeset_id_sequence');
-- alter sequence geneticcodeset_id_sequence restart with 1;

CREATE TABLE geospot
(
  geospot_id bigint NOT NULL,
  "version" integer,
  elevation double precision,
  latitude double precision,
  longitude double precision,
  "name" character varying(255),
  notes character varying(2000)
)
WITH (OIDS=FALSE);
ALTER TABLE geospot OWNER TO postgres;
CREATE SEQUENCE geospot_id_sequence;
ALTER TABLE geospot ALTER COLUMN geospot_id SET DEFAULT nextval('geospot_id_sequence');
-- alter sequence geospot_id_sequence restart with 1;

CREATE TABLE help
(
  help_id bigint NOT NULL,
  "version" integer,
  helptext text,
  tag character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE help OWNER TO postgres;
CREATE SEQUENCE help_id_sequence;
ALTER TABLE help ALTER COLUMN help_id SET DEFAULT nextval('help_id_sequence');
-- alter sequence help_id_sequence restart with 183;

CREATE TABLE itemdefinition
(
  itemdefinition_id bigint NOT NULL,
  "version" integer,
  description character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE itemdefinition OWNER TO postgres;
CREATE SEQUENCE itemdefinition_id_sequence;
ALTER TABLE itemdefinition ALTER COLUMN itemdefinition_id SET DEFAULT nextval('itemdefinition_id_sequence');
-- alter sequence itemdefinition_id_sequence restart with 9;

CREATE TABLE itemvalue
(
  itemvalue_id bigint NOT NULL,
  "version" integer,
  "value" character varying(255),
  element_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE itemvalue OWNER TO postgres;
CREATE SEQUENCE itemvalue_id_sequence;
ALTER TABLE itemvalue ALTER COLUMN itemvalue_id SET DEFAULT nextval('itemvalue_id_sequence');
-- alter sequence itemvalue_id_sequence restart with 1;

CREATE TABLE leftchangeset_charstate
(
  statechangeset_id bigint NOT NULL,
  discretecharstate_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE leftchangeset_charstate OWNER TO postgres;

CREATE TABLE matrix_itemdefinition
(
  matrix_id bigint NOT NULL,
  itemdefinition_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE matrix_itemdefinition OWNER TO postgres;

CREATE TABLE matrix
(
  matrixtype character(1) NOT NULL,
  matrix_id bigint NOT NULL,
  "version" integer,
  tb_matrixid character varying(30),
  description character varying(2000),
  gapsymbol character(1),
  missingsymbol character(1),
  nexusfilename character varying(255),
  published smallint,
  symbols character varying(255),
  title character varying(255),
  "nchar" integer,
  ntax integer,
  aligned smallint,
  diagonal smallint,
  triangle character varying(255),
  casesensitive smallint,
  matrixdatatype_id bigint,
  matrixkind_id bigint,
  study_id bigint NOT NULL,
  taxonlabelset_id bigint,
  ancstateset_id bigint,
  codonpositionset_id bigint,
  charset_id bigint,
  typeset_id bigint,
  charweightset_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE matrix OWNER TO postgres;
CREATE SEQUENCE matrix_id_sequence;
ALTER TABLE matrix ALTER COLUMN matrix_id SET DEFAULT nextval('matrix_id_sequence');
-- alter sequence matrix_id_sequence restart with 4151;

CREATE TABLE matrixcolumn_itemdefinition
(
  matrixcolumn_id bigint NOT NULL,
  itemdefinition_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE matrixcolumn_itemdefinition OWNER TO postgres;

CREATE TABLE matrixcolumn
(
  matrixcolumn_id bigint NOT NULL,
  "version" integer,
  phylochar_id bigint,
  matrix_id bigint,
  stateformat_id bigint,
  column_order integer
)
WITH (OIDS=FALSE);
ALTER TABLE matrixcolumn OWNER TO postgres;
CREATE SEQUENCE matrixcolumn_id_sequence;
ALTER TABLE matrixcolumn ALTER COLUMN matrixcolumn_id SET DEFAULT nextval('matrixcolumn_id_sequence');
-- alter sequence matrixcolumn_id_sequence restart with 5682181;

CREATE TABLE matrixdatatype
(
  matrixdatatype_id bigint NOT NULL,
  "version" integer,
  description character varying(255),
  phylochar_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE matrixdatatype OWNER TO postgres;
CREATE SEQUENCE matrixdatatype_id_sequence;
ALTER TABLE matrixdatatype ALTER COLUMN matrixdatatype_id SET DEFAULT nextval('matrixdatatype_id_sequence');
-- alter sequence matrixdatatype_id_sequence restart with 8;

CREATE TABLE matrixelement
(
  "type" character(1) NOT NULL,
  matrixelement_id bigint NOT NULL,
  "version" integer,
  andlogic smallint,
  compoundvalue character varying(1000),
  "value" double precision,
  gap smallint,
  matrixcolumn_id bigint,
  matrixrow_id bigint,
  itemdefinition_id bigint,
  discretecharstate_id bigint,
  element_order integer
)
WITH (OIDS=FALSE);
ALTER TABLE matrixelement OWNER TO postgres;
CREATE SEQUENCE matrixelement_id_sequence;
ALTER TABLE matrixelement ALTER COLUMN matrixelement_id SET DEFAULT nextval('matrixelement_id_sequence');
-- alter sequence matrixelement_id_sequence restart with 305807206;

CREATE TABLE matrixkind
(
  matrixkind_id bigint NOT NULL,
  "version" integer,
  description character varying(100)
)
WITH (OIDS=FALSE);
ALTER TABLE matrixkind OWNER TO postgres;
CREATE SEQUENCE matrixkind_id_sequence;
ALTER TABLE matrixkind ALTER COLUMN matrixkind_id SET DEFAULT nextval('matrixkind_id_sequence');
-- alter sequence matrixkind_id_sequence restart with 12;

CREATE TABLE matrixrow
(
  matrixrow_id bigint NOT NULL,
  "version" integer,
  symbolstring text,
  matrix_id bigint,
  taxonlabel_id bigint NOT NULL,
  row_order integer
)
WITH (OIDS=FALSE);
ALTER TABLE matrixrow OWNER TO postgres;
CREATE SEQUENCE matrixrow_id_sequence;
ALTER TABLE matrixrow ALTER COLUMN matrixrow_id SET DEFAULT nextval('matrixrow_id_sequence');
-- alter sequence matrixrow_id_sequence restart with 183676;

CREATE TABLE nodeattribute
(
  nodeattribute_id bigint NOT NULL,
  "version" integer
)
WITH (OIDS=FALSE);
ALTER TABLE nodeattribute OWNER TO postgres;
CREATE SEQUENCE nodeattribute_id_sequence;
ALTER TABLE nodeattribute ALTER COLUMN nodeattribute_id SET DEFAULT nextval('nodeattribute_id_sequence');
-- alter sequence nodeattribute_id_sequence restart with 1;

CREATE TABLE person
(
  person_id bigint NOT NULL,
  "version" integer,
  email character varying(255),
  firstname character varying(255),
  lastname character varying(255) NOT NULL,
  mname character varying(255),
  phone character varying(50),
  authorid character varying(50)
)
WITH (OIDS=FALSE);
ALTER TABLE person OWNER TO postgres;
CREATE SEQUENCE person_id_sequence;
ALTER TABLE person ALTER COLUMN person_id SET DEFAULT nextval('person_id_sequence');
-- alter sequence person_id_sequence restart with 9961;

CREATE TABLE phylochar
(
  "type" character(1) NOT NULL,
  phylochar_id bigint NOT NULL,
  "version" integer,
  description character varying(255),
  lowerlimit double precision,
  upperlimit double precision
)
WITH (OIDS=FALSE);
ALTER TABLE phylochar OWNER TO postgres;
CREATE SEQUENCE phylochar_id_sequence;
ALTER TABLE phylochar ALTER COLUMN phylochar_id SET DEFAULT nextval('phylochar_id_sequence');
-- alter sequence phylochar_id_sequence restart with 32903;

CREATE TABLE phylotree
(
  phylotree_id bigint NOT NULL,
  "version" integer,
  tb1_treeid character varying(30),
  bigtree smallint,
  label character varying(255),
  ntax integer,
  newickstring text,
  nexusfilename character varying(255),
  published smallint,
  rootedtree smallint,
  title character varying(255),
  rootnode_id bigint,
  study_id bigint NOT NULL,
  treeattribute_id bigint,
  treeblock_id bigint,
  treekind_id bigint,
  treequality_id bigint,
  treetype_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE phylotree OWNER TO postgres;
CREATE SEQUENCE phylotree_id_sequence;
ALTER TABLE phylotree ALTER COLUMN phylotree_id SET DEFAULT nextval('phylotree_id_sequence');
-- alter sequence phylotree_id_sequence restart with 5982;

CREATE TABLE phylotreenode
(
  phylotreenode_id bigint NOT NULL,
  "version" integer,
  branchlength double precision,
  leftnode bigint,
  "name" character varying(255),
  nodedepth integer,
  rightnode bigint,
  child_id bigint,
  nodeattribute_id bigint,
  parent_id bigint,
  sibling_id bigint,
  taxonlabel_id bigint,
  phylotree_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE phylotreenode OWNER TO postgres;
CREATE SEQUENCE phylotreenode_id_sequence;
ALTER TABLE phylotreenode ALTER COLUMN phylotreenode_id SET DEFAULT nextval('phylotreenode_id_sequence');
-- alter sequence phylotreenode_id_sequence restart with 452286;
-- GRANT ALL ON SEQUENCE phylotreenode_id_sequence TO postgres;

CREATE TABLE polytcount
(
  polytcount_id bigint NOT NULL,
  "version" integer,
  description character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE polytcount OWNER TO postgres;
CREATE SEQUENCE polytcount_id_sequence;
ALTER TABLE polytcount ALTER COLUMN polytcount_id SET DEFAULT nextval('polytcount_id_sequence');
-- alter sequence polytcount_id_sequence restart with 3;

CREATE TABLE rightchangeset_charstate
(
  statechangeset_id bigint NOT NULL,
  discretecharstate_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE rightchangeset_charstate OWNER TO postgres;

CREATE TABLE rowsegment
(
  rowsegment_id bigint NOT NULL,
  "version" integer,
  endindex integer,
  catalognum character varying(50),
  collectioncode character varying(50),
  collector character varying(255),
  country character varying(50),
  elevation double precision,
  genbaccession character varying(30),
  instacronym character varying(50),
  latitude double precision,
  locality character varying(255),
  longitude double precision,
  notes character varying(2000),
  otheraccession character varying(50),
  sampledate date,
  state character varying(50),
  startindex integer,
  title character varying(255),
  matrixrow_id bigint NOT NULL,
  taxonlabel_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE rowsegment OWNER TO postgres;
CREATE SEQUENCE rowsegment_id_sequence;
ALTER TABLE rowsegment ALTER COLUMN rowsegment_id SET DEFAULT nextval('rowsegment_id_sequence');
-- alter sequence rowsegment_id_sequence restart with 1;

CREATE TABLE software
(
  software_id bigint NOT NULL,
  "version" integer,
  description character varying(2000),
  "name" character varying(255),
  softwareurl character varying(500),
  softwareversion character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE software OWNER TO postgres;
CREATE SEQUENCE software_id_sequence;
ALTER TABLE software ALTER COLUMN software_id SET DEFAULT nextval('software_id_sequence');
-- alter sequence software_id_sequence restart with 4834;

CREATE TABLE specimenlabel
(
  specimenlabel_id bigint NOT NULL,
  "version" integer,
  genbankid character varying(255),
  museumnumber character varying(255),
  tissue character varying(255),
  geospot_id bigint,
  study_id bigint NOT NULL,
  taxon_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE specimenlabel OWNER TO postgres;
CREATE SEQUENCE specimenlabel_id_sequence;
ALTER TABLE specimenlabel ALTER COLUMN specimenlabel_id SET DEFAULT nextval('specimenlabel_id_sequence');
-- alter sequence specimenlabel_id_sequence restart with 4499;

CREATE TABLE statechangeset
(
  statechangeset_id bigint NOT NULL,
  "version" integer,
  reversible smallint,
  title character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE statechangeset OWNER TO postgres;
CREATE SEQUENCE statechangeset_id_sequence;
ALTER TABLE statechangeset ALTER COLUMN statechangeset_id SET DEFAULT nextval('statechangeset_id_sequence');
-- alter sequence statechangeset_id_sequence restart with 1;

CREATE TABLE stateformat
(
  stateformat_id bigint NOT NULL,
  "version" integer,
  description character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE stateformat OWNER TO postgres;
CREATE SEQUENCE stateformat_id_sequence;
ALTER TABLE stateformat ALTER COLUMN stateformat_id SET DEFAULT nextval('stateformat_id_sequence');
-- alter sequence stateformat_id_sequence restart with 5;

CREATE TABLE statemodifier
(
  statemodifier_id bigint NOT NULL,
  "version" integer,
  count integer,
  frequency double precision,
  discretecharstate_id bigint,
  element_id bigint NOT NULL,
  stateformat_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE statemodifier OWNER TO postgres;
CREATE SEQUENCE statemodifier_id_sequence;
ALTER TABLE statemodifier ALTER COLUMN statemodifier_id SET DEFAULT nextval('statemodifier_id_sequence');
-- alter sequence statemodifier_id_sequence restart with 1;

CREATE TABLE stateset
(
  stateset_id bigint NOT NULL,
  "version" integer,
  title character varying(255),
  matrix_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE stateset OWNER TO postgres;
CREATE SEQUENCE stateset_id_sequence;
ALTER TABLE stateset ALTER COLUMN stateset_id SET DEFAULT nextval('stateset_id_sequence');
-- alter sequence stateset_id_sequence restart with 1;

CREATE TABLE stepmatrixelement
(
  discretecharstate_id bigint NOT NULL,
  "version" integer,
  transcost double precision,
  state1_id bigint,
  state2_id bigint,
  stepmatrix_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE stepmatrixelement OWNER TO postgres;

CREATE TABLE study_nexusfile
(
  study_id bigint NOT NULL,
  nexus text NOT NULL,
  filename character varying(255) NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE study_nexusfile OWNER TO postgres;

CREATE TABLE study
(
  study_id bigint NOT NULL,
  "version" integer,
  tb_studyid character varying(30),
  accessionnumber character varying(255),
  lastmodifieddate date,
  "name" character varying(255),
  notes character varying(2000),
  releasedate date,
  citation_id bigint,
  user_id bigint,
  studystatus_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE study OWNER TO postgres;
CREATE SEQUENCE study_id_sequence;
ALTER TABLE study ALTER COLUMN study_id SET DEFAULT nextval('study_id_sequence');
-- alter sequence study_id_sequence restart with 9931;

CREATE TABLE studystatus
(
  studystatus_id bigint NOT NULL,
  "version" integer,
  description character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE studystatus OWNER TO postgres;
CREATE SEQUENCE studystatus_id_sequence;
ALTER TABLE studystatus ALTER COLUMN studystatus_id SET DEFAULT nextval('studystatus_id_sequence');
-- alter sequence studystatus_id_sequence restart with 4;

CREATE TABLE sub_matrix
(
  submission_id bigint NOT NULL,
  matrix_id bigint NOT NULL,
  collection_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE sub_matrix OWNER TO postgres;

CREATE TABLE sub_taxonlabel
(
  submission_id bigint NOT NULL,
  taxonlabel_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE sub_taxonlabel OWNER TO postgres;

CREATE TABLE sub_treeblock
(
  submission_id bigint NOT NULL,
  treeblock_id bigint NOT NULL,
  collection_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE sub_treeblock OWNER TO postgres;

CREATE TABLE submission
(
  submission_id bigint NOT NULL,
  "version" integer,
  createdate date,
  submissionnumber character varying(255),
  test smallint NOT NULL,
  study_id bigint NOT NULL,
  user_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE submission OWNER TO postgres;
CREATE SEQUENCE submission_id_sequence;
ALTER TABLE submission ALTER COLUMN submission_id SET DEFAULT nextval('submission_id_sequence');
-- alter sequence submission_id_sequence restart with 9921;

CREATE TABLE taxon
(
  taxon_id bigint NOT NULL,
  "version" integer,
  ubionamebankid bigint,
  description character varying(2000),
  groupcode integer,
  "name" character varying(255),
  ncbitaxid integer,
  tb1legacyid integer
)
WITH (OIDS=FALSE);
ALTER TABLE taxon OWNER TO postgres;
CREATE SEQUENCE taxon_id_sequence;
ALTER TABLE taxon ALTER COLUMN taxon_id SET DEFAULT nextval('taxon_id_sequence');
-- alter sequence taxon_id_sequence restart with 427168;

CREATE TABLE taxonauthority
(
  taxonauthority_id bigint NOT NULL,
  "version" integer,
  connectionstr character varying(1000),
  description character varying(2000),
  "name" character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE taxonauthority OWNER TO postgres;
CREATE SEQUENCE taxonauthority_id_sequence;
ALTER TABLE taxonauthority ALTER COLUMN taxonauthority_id SET DEFAULT nextval('taxonauthority_id_sequence');
-- alter sequence taxonauthority_id_sequence restart with 1;

CREATE TABLE taxonlabel
(
  taxonlabel_id bigint NOT NULL,
  "version" integer,
  linked smallint,
  taxonlabel character varying(255),
  study_id bigint,
  taxonvariant_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE taxonlabel OWNER TO postgres;
CREATE SEQUENCE taxonlabel_id_sequence;
ALTER TABLE taxonlabel ALTER COLUMN taxonlabel_id SET DEFAULT nextval('taxonlabel_id_sequence');
-- alter sequence taxonlabel_id_sequence restart with 239936;
GRANT ALL ON SEQUENCE taxonlabel_id_sequence TO postgres;
GRANT ALL ON SEQUENCE taxonlabel_id_sequence TO treebase_app;

CREATE TABLE taxonlabelgroup
(
  taxonlabelgroup_id bigint NOT NULL,
  "version" integer,
  title character varying(255),
  taxonlabelpartition_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE taxonlabelgroup OWNER TO postgres;
CREATE SEQUENCE taxonlabelgroup_id_sequence;
ALTER TABLE taxonlabelgroup ALTER COLUMN taxonlabelgroup_id SET DEFAULT nextval('taxonlabelgroup_id_sequence');
-- alter sequence taxonlabelgroup_id_sequence restart with 1;

CREATE TABLE taxonlabelpartition
(
  taxonlabelpartition_id bigint NOT NULL,
  "version" integer,
  title character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE taxonlabelpartition OWNER TO postgres;
CREATE SEQUENCE taxonlabelpartition_id_sequence;
ALTER TABLE taxonlabelpartition ALTER COLUMN taxonlabelpartition_id SET DEFAULT nextval('taxonlabelpartition_id_sequence');
-- alter sequence taxonlabelpartition_id_sequence restart with 1;

CREATE TABLE taxonlabelroup_taxonlabel
(
  taxonlabelgroup_id bigint NOT NULL,
  taxonlabel_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE taxonlabelroup_taxonlabel OWNER TO postgres;

CREATE TABLE taxonlabelset_taxonlabel
(
  taxonlabelset_id bigint NOT NULL,
  taxonlabel_id bigint NOT NULL,
  taxonlabel_order integer NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE taxonlabelset_taxonlabel OWNER TO postgres;

CREATE TABLE taxonlabelset
(
  taxonlabelset_id bigint NOT NULL,
  "version" integer,
  taxa smallint,
  title character varying(255),
  study_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE taxonlabelset OWNER TO postgres;
CREATE SEQUENCE taxonlabelset_id_sequence;
ALTER TABLE taxonlabelset ALTER COLUMN taxonlabelset_id SET DEFAULT nextval('taxonlabelset_id_sequence');
-- alter sequence taxonlabelset_id_sequence restart with 14371;

CREATE TABLE taxonlink
(
  linktype character(1) NOT NULL,
  taxonlink_id bigint NOT NULL,
  "version" integer,
  foreigntaxonid character varying(255),
  taxonauthority_id bigint NOT NULL,
  taxon_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE taxonlink OWNER TO postgres;
CREATE SEQUENCE taxonlink_id_sequence;
ALTER TABLE taxonlink ALTER COLUMN taxonlink_id SET DEFAULT nextval('taxonlink_id_sequence');
-- alter sequence taxonlink_id_sequence restart with 1;

CREATE TABLE taxonset_taxon
(
  taxonset_id bigint NOT NULL,
  taxon_id bigint NOT NULL,
  taxon_order integer NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE taxonset_taxon OWNER TO postgres;

CREATE TABLE taxonset
(
  taxonset_id bigint NOT NULL,
  "version" integer,
  title character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE taxonset OWNER TO postgres;
CREATE SEQUENCE taxonset_id_sequence;
ALTER TABLE taxonset ALTER COLUMN taxonset_id SET DEFAULT nextval('taxonset_id_sequence');
-- alter sequence taxonset_id_sequence restart with 1;

CREATE TABLE taxonvariant
(
  taxonvariant_id bigint NOT NULL,
  "version" integer,
  fullname character varying(255),
  lexicalqualifier character varying(50),
  "name" character varying(255),
  namebankid bigint,
  taxon_id bigint NOT NULL,
  tb1legacyid integer
)
WITH (OIDS=FALSE);
ALTER TABLE taxonvariant OWNER TO postgres;
CREATE SEQUENCE taxonvariant_id_sequence;
ALTER TABLE taxonvariant ALTER COLUMN taxonvariant_id SET DEFAULT nextval('taxonvariant_id_sequence');
-- alter sequence taxonvariant_id_sequence restart with 564331;

CREATE TABLE treeattribute
(
  treeattribute_id bigint NOT NULL,
  "version" integer
)
WITH (OIDS=FALSE);
ALTER TABLE treeattribute OWNER TO postgres;
CREATE SEQUENCE treeattribute_id_sequence;
ALTER TABLE treeattribute ALTER COLUMN treeattribute_id SET DEFAULT nextval('treeattribute_id_sequence');
-- alter sequence treeattribute_id_sequence restart with 1;

CREATE TABLE treeblock
(
  treeblock_id bigint NOT NULL,
  "version" integer,
  title character varying(255),
  taxonlabelset_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE treeblock OWNER TO postgres;
CREATE SEQUENCE treeblock_id_sequence;
ALTER TABLE treeblock ALTER COLUMN treeblock_id SET DEFAULT nextval('treeblock_id_sequence');
-- alter sequence treeblock_id_sequence restart with 9662;

CREATE TABLE treegroup_phylotree
(
  treegroup_id bigint NOT NULL,
  phylotree_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE treegroup_phylotree OWNER TO postgres;

CREATE TABLE treegroup
(
  treegroup_id bigint NOT NULL,
  "version" integer,
  title character varying(255),
  treepartition_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE treegroup OWNER TO postgres;
CREATE SEQUENCE treegroup_id_sequence;
ALTER TABLE treegroup ALTER COLUMN treegroup_id SET DEFAULT nextval('treegroup_id_sequence');
-- alter sequence treegroup_id_sequence restart with 1;

CREATE TABLE treekind
(
  treekind_id bigint NOT NULL,
  "version" integer,
  description character varying(100)
)
WITH (OIDS=FALSE);
ALTER TABLE treekind OWNER TO postgres;
CREATE SEQUENCE treekind_id_sequence;
ALTER TABLE treekind ALTER COLUMN treekind_id SET DEFAULT nextval('treekind_id_sequence');
-- alter sequence treekind_id_sequence restart with 7;

CREATE TABLE treenodeedge
(
  treenodeedge_id bigint NOT NULL,
  "version" integer,
  childnode_id bigint NOT NULL,
  parentnode_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE treenodeedge OWNER TO postgres;
CREATE SEQUENCE treenodeedge_id_sequence;
ALTER TABLE treenodeedge ALTER COLUMN treenodeedge_id SET DEFAULT nextval('treenodeedge_id_sequence');
-- alter sequence treenodeedge_id_sequence restart with 1;

CREATE TABLE treepartition
(
  treepartition_id bigint NOT NULL,
  "version" integer,
  title character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE treepartition OWNER TO postgres;
CREATE SEQUENCE treepartition_id_sequence;
ALTER TABLE treepartition ALTER COLUMN treepartition_id SET DEFAULT nextval('treepartition_id_sequence');
-- alter sequence treepartition_id_sequence restart with 1;

CREATE TABLE treequality
(
  treequality_id bigint NOT NULL,
  "version" integer,
  description character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE treequality OWNER TO postgres;
CREATE SEQUENCE treequality_id_sequence;
ALTER TABLE treequality ALTER COLUMN treequality_id SET DEFAULT nextval('treequality_id_sequence');
-- alter sequence treequality_id_sequence restart with 5;

CREATE TABLE treeset_phylotree
(
  treeset_id bigint NOT NULL,
  phylotree_id bigint NOT NULL,
  tree_order integer NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE treeset_phylotree OWNER TO postgres;

CREATE TABLE treeset
(
  treeset_id bigint NOT NULL,
  "version" integer,
  title character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE treeset OWNER TO postgres;
CREATE SEQUENCE treeset_id_sequence;
ALTER TABLE treeset ALTER COLUMN treeset_id SET DEFAULT nextval('treeset_id_sequence');
-- alter sequence treeset_id_sequence restart with 1;

CREATE TABLE treetype
(
  treetype_id bigint NOT NULL,
  "version" integer,
  description character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE treetype OWNER TO postgres;
CREATE SEQUENCE treetype_id_sequence;
ALTER TABLE treetype ALTER COLUMN treetype_id SET DEFAULT nextval('treetype_id_sequence');
-- alter sequence treetype_id_sequence restart with 4;

CREATE TABLE typeset
(
  typeset_id bigint NOT NULL,
  "version" integer,
  title character varying(255),
  matrix_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE typeset OWNER TO postgres;
CREATE SEQUENCE typeset_id_sequence;
ALTER TABLE typeset ALTER COLUMN typeset_id SET DEFAULT nextval('typeset_id_sequence');
-- alter sequence typeset_id_sequence restart with 1;

CREATE TABLE "user"
(
  user_id bigint NOT NULL,
  "version" integer,
  "password" character varying(100) NOT NULL,
  username character varying(100) NOT NULL,
  person_id bigint NOT NULL,
  userrole_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "user" OWNER TO postgres;
CREATE SEQUENCE user_id_sequence;
ALTER TABLE "user" ALTER COLUMN user_id SET DEFAULT nextval('user_id_sequence');
-- alter sequence user_id_sequence restart with 9951;

CREATE TABLE userrole
(
  userrole_id bigint NOT NULL,
  "version" integer,
  authority character varying(255) NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE userrole OWNER TO postgres;
CREATE SEQUENCE userrole_id_sequence;
ALTER TABLE "userrole" ALTER COLUMN userrole_id SET DEFAULT nextval('userrole_id_sequence');
-- alter sequence userrole_id_sequence restart with 4;

CREATE TABLE usertype
(
  "type" character(1) NOT NULL,
  usertype_id bigint NOT NULL,
  "version" integer,
  title character varying(255)
)
WITH (OIDS=FALSE);
ALTER TABLE usertype OWNER TO postgres;
CREATE SEQUENCE usertype_id_sequence;
ALTER TABLE usertype ALTER COLUMN usertype_id SET DEFAULT nextval('usertype_id_sequence');
-- alter sequence usertype_id_sequence restart with 4;

CREATE TABLE usertyperecord
(
  usertyperecord_id bigint NOT NULL,
  "version" integer,
  usertype_id bigint,
  typeset_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE usertyperecord OWNER TO postgres;
CREATE SEQUENCE usertyperecord_id_sequence;
ALTER TABLE usertyperecord ALTER COLUMN usertyperecord_id SET DEFAULT nextval('usertyperecord_id_sequence');
-- alter sequence usertyperecord_id_sequence restart with 1;

CREATE TABLE usertyperrd_colrange
(
  usertyperecord_id bigint NOT NULL,
  columnrange_id bigint NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE usertyperrd_colrange OWNER TO postgres;

CREATE INDEX COLUMN_M_IDX
	ON MATRIXCOLUMN(MATRIX_ID);
CREATE INDEX ELEMENT_COL_IDX
	ON MATRIXELEMENT(MATRIXCOLUMN_ID);
CREATE INDEX ELEMENT_ROW_IDX
	ON MATRIXELEMENT(MATRIXROW_ID);
CREATE INDEX ROW_M_IDX
	ON MATRIXROW(MATRIX_ID);
CREATE INDEX PERSON_EMAIL_IDX
	ON PERSON(EMAIL);
CREATE INDEX PERSON_LNAME_IDX
	ON PERSON(LASTNAME);
CREATE INDEX TNODE_TREE_IDX
	ON PHYLOTREENODE(PHYLOTREE_ID);
CREATE INDEX TNODE_TAXLABEL_IDX
	ON PHYLOTREENODE(TAXONLABEL_ID);
CREATE INDEX SPECIM_INST_IDX
	ON ROWSEGMENT(INSTACRONYM);
CREATE INDEX STUDY_NAME_IDX
	ON STUDY(NAME);
CREATE INDEX TAXON_NAME_IDX
	ON TAXON(NAME);
CREATE INDEX TLABEL_STUDY_IDX
	ON TAXONLABEL(STUDY_ID);
CREATE INDEX TVARI_NBID_IDX
	ON TAXONVARIANT(NAMEBANKID);
CREATE INDEX TVARI_TAXON_IDX
	ON TAXONVARIANT(TAXON_ID);
CREATE INDEX TVARI_FNAME_IDX
	ON TAXONVARIANT(FULLNAME);

ALTER TABLE ALGORITHM
 	ADD CONSTRAINT SQL081219134217210
 	PRIMARY KEY (ALGORITHM_ID);
ALTER TABLE ANALYSIS
	ADD CONSTRAINT SQL081219134217310
	PRIMARY KEY (ANALYSIS_ID);
ALTER TABLE ANALYSISSTEP
	ADD CONSTRAINT SQL081219134217470
	PRIMARY KEY (ANALYSISSTEP_ID);
ALTER TABLE ANALYZEDDATA
	ADD CONSTRAINT SQL081219134217570
	PRIMARY KEY (ANALYZEDDATA_ID);
ALTER TABLE ANCESTRALSTATE
	ADD CONSTRAINT SQL081219134217670
	PRIMARY KEY (ANCESTRALSTATE_ID);
ALTER TABLE ANCSTATESET
	ADD CONSTRAINT SQL081219134217770
	PRIMARY KEY (ANCSTATESET_ID);
ALTER TABLE CHARGROUP
	ADD CONSTRAINT SQL081219134217890
	PRIMARY KEY (CHARGROUP_ID);
ALTER TABLE CHARPARTITION
	ADD CONSTRAINT SQL081219134217990
	PRIMARY KEY (CHARPARTITION_ID);
ALTER TABLE CHARSET
	ADD CONSTRAINT SQL081219134218100
	PRIMARY KEY (CHARSET_ID);
ALTER TABLE CHARWEIGHT
	ADD CONSTRAINT SQL081219134218200
	PRIMARY KEY (CHARWEIGHT_ID);
ALTER TABLE CHARWEIGHTSET
	ADD CONSTRAINT SQL081219134218310
	PRIMARY KEY (CHARWEIGHTSET_ID);
ALTER TABLE CITATION_AUTHOR
	ADD CONSTRAINT SQL081219134218670
	PRIMARY KEY (CITATION_ID, AUTHOR_ORDER);
ALTER TABLE CITATION_EDITOR
	ADD CONSTRAINT SQL081219134218780
	PRIMARY KEY (CITATION_ID, EDITOR_ORDER);
ALTER TABLE CITATION
	ADD CONSTRAINT SQL081219134218470
	PRIMARY KEY (CITATION_ID);
ALTER TABLE CITATIONSTATUS
	ADD CONSTRAINT SQL081219134218570
	PRIMARY KEY (CITATIONSTATUS_ID);
ALTER TABLE CODONPOSITIONSET
	ADD CONSTRAINT SQL081219134220130
	PRIMARY KEY (CODONPOSITIONSET_ID);
ALTER TABLE COLUMNRANGE
	ADD CONSTRAINT SQL081219134218890
	PRIMARY KEY (COLUMNRANGE_ID);
ALTER TABLE COMPOUND_ELEMENT
	ADD CONSTRAINT SQL081219134219000
	PRIMARY KEY (COMPOUND_ID, ELEMENT_ID);
ALTER TABLE CSTREENODE
	ADD CONSTRAINT SQL081219134219200
	PRIMARY KEY (CSTREENODE_ID);
ALTER TABLE DISCRETECHARSTATE
	ADD CONSTRAINT SQL081219134220230
	PRIMARY KEY (DISCRETECHARSTATE_ID);
ALTER TABLE DISTANCEMATRIXELEMENT
	ADD CONSTRAINT SQL081219134220330
	PRIMARY KEY (DISTANCEMATRIXELEMENT_ID);
ALTER TABLE GAPMODE
	ADD CONSTRAINT SQL081219134220440
	PRIMARY KEY (GAPMODE_ID);
ALTER TABLE GENETICCODE
	ADD CONSTRAINT SQL081219134220650
	PRIMARY KEY (GENETICCODE_ID);
ALTER TABLE GENETICCODERECORD
	ADD CONSTRAINT SQL081219134220760
	PRIMARY KEY (GENETICCODERECORD_ID);
ALTER TABLE GENETICCODESET
	ADD CONSTRAINT SQL081219134220550
	PRIMARY KEY (GENETICCODESET_ID);
ALTER TABLE GEOSPOT
	ADD CONSTRAINT SQL080428163906080
	PRIMARY KEY (GEOSPOT_ID);
ALTER TABLE HELP
	ADD CONSTRAINT SQL081219134220860
	PRIMARY KEY (HELP_ID);
ALTER TABLE ITEMDEFINITION
	ADD CONSTRAINT SQL081219134220970
	PRIMARY KEY (ITEMDEFINITION_ID);
ALTER TABLE ITEMVALUE
	ADD CONSTRAINT SQL081219134221080
	PRIMARY KEY (ITEMVALUE_ID);
ALTER TABLE MATRIX_ITEMDEFINITION
	ADD CONSTRAINT SQL081219134222010
	PRIMARY KEY (MATRIX_ID, ITEMDEFINITION_ID);
ALTER TABLE MATRIX
	ADD CONSTRAINT SQL081219134221270
	PRIMARY KEY (MATRIX_ID);
ALTER TABLE MATRIXCOLUMN_ITEMDEFINITION
	ADD CONSTRAINT SQL081219134221470
	PRIMARY KEY (MATRIXCOLUMN_ID, ITEMDEFINITION_ID);
ALTER TABLE MATRIXCOLUMN
	ADD CONSTRAINT SQL081219134221370
	PRIMARY KEY (MATRIXCOLUMN_ID);
ALTER TABLE MATRIXDATATYPE
	ADD CONSTRAINT SQL081219134221580
	PRIMARY KEY (MATRIXDATATYPE_ID);
ALTER TABLE MATRIXELEMENT
	ADD CONSTRAINT SQL081219134221690
	PRIMARY KEY (MATRIXELEMENT_ID);
ALTER TABLE MATRIXKIND
	ADD CONSTRAINT SQL081219134221790
	PRIMARY KEY (MATRIXKIND_ID);
ALTER TABLE MATRIXROW
	ADD CONSTRAINT SQL081219134221900
	PRIMARY KEY (MATRIXROW_ID);
ALTER TABLE NODEATTRIBUTE
	ADD CONSTRAINT SQL081219134222120
	PRIMARY KEY (NODEATTRIBUTE_ID);
ALTER TABLE PERSON
	ADD CONSTRAINT SQL081219134222230
	PRIMARY KEY (PERSON_ID);
ALTER TABLE PHYLOCHAR
	ADD CONSTRAINT SQL081219134222330
	PRIMARY KEY (PHYLOCHAR_ID);
ALTER TABLE PHYLOTREE
	ADD CONSTRAINT SQL081219134222440
	PRIMARY KEY (PHYLOTREE_ID);
ALTER TABLE PHYLOTREENODE
	ADD CONSTRAINT SQL081219134222560
	PRIMARY KEY (PHYLOTREENODE_ID);
ALTER TABLE POLYTCOUNT
	ADD CONSTRAINT SQL081219134222660
	PRIMARY KEY (POLYTCOUNT_ID);
ALTER TABLE ROWSEGMENT
	ADD CONSTRAINT SQL081219134222850
	PRIMARY KEY (ROWSEGMENT_ID);
ALTER TABLE SOFTWARE
	ADD CONSTRAINT SQL081219134222960
	PRIMARY KEY (SOFTWARE_ID);
ALTER TABLE SPECIMENLABEL
	ADD CONSTRAINT SQL080428163906480
	PRIMARY KEY (SPECIMENLABEL_ID);
ALTER TABLE STATECHANGESET
	ADD CONSTRAINT SQL081219134223070
	PRIMARY KEY (STATECHANGESET_ID);
ALTER TABLE STATEFORMAT
	ADD CONSTRAINT SQL081219134223190
	PRIMARY KEY (STATEFORMAT_ID);
ALTER TABLE STATEMODIFIER
	ADD CONSTRAINT SQL081219134223290
	PRIMARY KEY (STATEMODIFIER_ID);
ALTER TABLE STATESET
	ADD CONSTRAINT SQL081219134223390
	PRIMARY KEY (STATESET_ID);
ALTER TABLE STEPMATRIXELEMENT
	ADD CONSTRAINT SQL081219134223500
	PRIMARY KEY (DISCRETECHARSTATE_ID);
ALTER TABLE STUDY_NEXUSFILE
	ADD CONSTRAINT SQL081219134224240
	PRIMARY KEY (STUDY_ID, FILENAME);
ALTER TABLE STUDY
	ADD CONSTRAINT SQL081219134223600
	PRIMARY KEY (STUDY_ID);
ALTER TABLE STUDYSTATUS
	ADD CONSTRAINT SQL081219134223710
	PRIMARY KEY (STUDYSTATUS_ID);
ALTER TABLE SUB_MATRIX
	ADD CONSTRAINT SQL081219134223910
	PRIMARY KEY (COLLECTION_ID);
ALTER TABLE SUB_TAXONLABEL
	ADD CONSTRAINT SQL081219134224020
	PRIMARY KEY (SUBMISSION_ID, TAXONLABEL_ID);
ALTER TABLE SUB_TREEBLOCK
	ADD CONSTRAINT SQL081219134224120
	PRIMARY KEY (COLLECTION_ID);
ALTER TABLE SUBMISSION
	ADD CONSTRAINT SQL081219134223810
	PRIMARY KEY (SUBMISSION_ID);
ALTER TABLE TAXON
	ADD CONSTRAINT SQL081219134224350
	PRIMARY KEY (TAXON_ID);
ALTER TABLE TAXONAUTHORITY
	ADD CONSTRAINT SQL081219134224450
	PRIMARY KEY (TAXONAUTHORITY_ID);
ALTER TABLE TAXONLABEL
	ADD CONSTRAINT SQL081219134224550
	PRIMARY KEY (TAXONLABEL_ID);
ALTER TABLE TAXONLABELGROUP
	ADD CONSTRAINT SQL081219134224650
	PRIMARY KEY (TAXONLABELGROUP_ID);
ALTER TABLE TAXONLABELPARTITION
	ADD CONSTRAINT SQL081219134224760
	PRIMARY KEY (TAXONLABELPARTITION_ID);
ALTER TABLE TAXONLABELSET_TAXONLABEL
	ADD CONSTRAINT SQL081219134226750
	PRIMARY KEY (TAXONLABELSET_ID, TAXONLABEL_ORDER);
ALTER TABLE TAXONLABELSET
	ADD CONSTRAINT SQL081219134226650
	PRIMARY KEY (TAXONLABELSET_ID);
ALTER TABLE TAXONLINK
	ADD CONSTRAINT SQL081219134224950
	PRIMARY KEY (TAXONLINK_ID);
ALTER TABLE TAXONSET_TAXON
	ADD CONSTRAINT SQL081219134225150
	PRIMARY KEY (TAXONSET_ID, TAXON_ORDER);
ALTER TABLE TAXONSET
	ADD CONSTRAINT SQL081219134225050
	PRIMARY KEY (TAXONSET_ID);
ALTER TABLE TAXONVARIANT
	ADD CONSTRAINT SQL081219134225250
	PRIMARY KEY (TAXONVARIANT_ID);
ALTER TABLE TREEATTRIBUTE
	ADD CONSTRAINT SQL081219134225410
	PRIMARY KEY (TREEATTRIBUTE_ID);
ALTER TABLE TREEBLOCK
	ADD CONSTRAINT SQL081219134225510
	PRIMARY KEY (TREEBLOCK_ID);
ALTER TABLE TREEGROUP_PHYLOTREE
	ADD CONSTRAINT SQL081219134225720
	PRIMARY KEY (TREEGROUP_ID, PHYLOTREE_ID);
ALTER TABLE TREEGROUP
	ADD CONSTRAINT SQL081219134225620
	PRIMARY KEY (TREEGROUP_ID);
ALTER TABLE TREEKIND
	ADD CONSTRAINT SQL081219134225820
	PRIMARY KEY (TREEKIND_ID);
ALTER TABLE TREENODEEDGE
	ADD CONSTRAINT SQL081219134225930
	PRIMARY KEY (TREENODEEDGE_ID);
ALTER TABLE TREEPARTITION
	ADD CONSTRAINT SQL081219134226040
	PRIMARY KEY (TREEPARTITION_ID);
ALTER TABLE TREEQUALITY
	ADD CONSTRAINT SQL081219134226140
	PRIMARY KEY (TREEQUALITY_ID);
ALTER TABLE TREESET_PHYLOTREE
	ADD CONSTRAINT SQL081219134226340
	PRIMARY KEY (TREESET_ID, TREE_ORDER);
ALTER TABLE TREESET
	ADD CONSTRAINT SQL081219134226240
	PRIMARY KEY (TREESET_ID);
ALTER TABLE TREETYPE
	ADD CONSTRAINT SQL081219134226440
	PRIMARY KEY (TREETYPE_ID);
ALTER TABLE TYPESET
	ADD CONSTRAINT SQL081219134226550
	PRIMARY KEY (TYPESET_ID);
ALTER TABLE "user"
	ADD CONSTRAINT SQL081219134226850
	PRIMARY KEY (USER_ID);
ALTER TABLE USERROLE
	ADD CONSTRAINT SQL081219134226960
	PRIMARY KEY (USERROLE_ID);
ALTER TABLE USERTYPE
	ADD CONSTRAINT SQL081219134227060
	PRIMARY KEY (USERTYPE_ID);
ALTER TABLE USERTYPERECORD
	ADD CONSTRAINT SQL081219134227170
	PRIMARY KEY (USERTYPERECORD_ID);
ALTER TABLE CHARGROUP_COLRANGE
	ADD CONSTRAINT SQL081219134219300
	UNIQUE (COLUMNRANGE_ID);
ALTER TABLE CHARSET_COLRANGE
	ADD CONSTRAINT SQL081219134219400
	UNIQUE (COLUMNRANGE_ID);
ALTER TABLE CHARWEIGHT_COLRANGE
	ADD CONSTRAINT SQL081219134219510
	UNIQUE (COLUMNRANGE_ID);
ALTER TABLE CODERECORD_COLRANGE
	ADD CONSTRAINT SQL081219134219610
	UNIQUE (COLUMNRANGE_ID);
ALTER TABLE CODONCHAR1_COLRANGE
	ADD CONSTRAINT SQL081219134219710
	UNIQUE (COLUMNRANGE_ID);
ALTER TABLE CODONCHAR2_COLRANGE
	ADD CONSTRAINT SQL081219134219810
	UNIQUE (COLUMNRANGE_ID);
ALTER TABLE CODONCHAR3_COLRANGE
	ADD CONSTRAINT SQL081219134219910
	UNIQUE (COLUMNRANGE_ID);
ALTER TABLE CODONNONCODING_COLRANGE
	ADD CONSTRAINT SQL081219134220010
	UNIQUE (COLUMNRANGE_ID);
ALTER TABLE COMPOUND_ELEMENT
	ADD CONSTRAINT SQL081219134219010
	UNIQUE (ELEMENT_ID);
ALTER TABLE SUB_MATRIX
	ADD CONSTRAINT SQL081219134223920
	UNIQUE (MATRIX_ID);
ALTER TABLE SUB_TAXONLABEL
	ADD CONSTRAINT SQL081219134224030
	UNIQUE (TAXONLABEL_ID);
ALTER TABLE SUB_TREEBLOCK
	ADD CONSTRAINT SQL081219134224130
	UNIQUE (TREEBLOCK_ID);
ALTER TABLE SUBMISSION
	ADD CONSTRAINT SQL081219134223811
	UNIQUE (STUDY_ID);
ALTER TABLE "user"
	ADD CONSTRAINT SQL081219134226851
	UNIQUE (USERNAME);
ALTER TABLE USERTYPERRD_COLRANGE
	ADD CONSTRAINT SQL081219134227270
	UNIQUE (COLUMNRANGE_ID);
ALTER TABLE ALGORITHM
 	ADD CONSTRAINT FKB388C44FC6E814E6
 	FOREIGN KEY(GAPMODE_ID)
 	REFERENCES GAPMODE(GAPMODE_ID)
 	ON DELETE NO ACTION 
 	ON UPDATE NO ACTION;
ALTER TABLE ALGORITHM
	ADD CONSTRAINT FKB388C44F9698D32E
	FOREIGN KEY(POLYTCOUNT_ID)
	REFERENCES POLYTCOUNT(POLYTCOUNT_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION;
ALTER TABLE ALGORITHM
	ADD CONSTRAINT FKB388C44F92E6A38E
	FOREIGN KEY(USERTYPE_ID)
	REFERENCES USERTYPE(USERTYPE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION;
ALTER TABLE ANALYSIS
	ADD CONSTRAINT FKF19622DC3C572C3C
	FOREIGN KEY(STUDY_ID)
	REFERENCES STUDY(STUDY_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE ANALYSISSTEP
	ADD CONSTRAINT FK21F853A8D1884DD8
	FOREIGN KEY(ANALYSIS_ID)
	REFERENCES ANALYSIS(ANALYSIS_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE ANALYSISSTEP
	ADD CONSTRAINT FK21F853A865EDD5F8
	FOREIGN KEY(SOFTWARE_ID)
	REFERENCES SOFTWARE(SOFTWARE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE ANALYSISSTEP
	ADD CONSTRAINT FK21F853A848A2817C
	FOREIGN KEY(ALGORITHM_ID)
	REFERENCES ALGORITHM(ALGORITHM_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE ANALYZEDDATA
	ADD CONSTRAINT FK8C961842B710CB23
	FOREIGN KEY(PHYLOTREE_ID)
	REFERENCES PHYLOTREE(PHYLOTREE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE ANALYZEDDATA
	ADD CONSTRAINT FK8C96184255961AEE
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE ANALYZEDDATA
	ADD CONSTRAINT FK8C9618424BA97F78
	FOREIGN KEY(ANALYSISSTEP_ID)
	REFERENCES ANALYSISSTEP(ANALYSISSTEP_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE ANCESTRALSTATE
	ADD CONSTRAINT FK3FA14284F4803CE6
	FOREIGN KEY(DISCRETECHARSTATE_ID)
	REFERENCES DISCRETECHARSTATE(DISCRETECHARSTATE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE ANCESTRALSTATE
	ADD CONSTRAINT FK3FA14284684F6406
	FOREIGN KEY(ANCSTATESET_ID)
	REFERENCES ANCSTATESET(ANCSTATESET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE ANCSTATESET
	ADD CONSTRAINT FK879339E7A414944F
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CHARGROUP_COLRANGE
	ADD CONSTRAINT FKB2D8C293971FFFA6
	FOREIGN KEY(CHARGROUP_ID)
	REFERENCES CHARGROUP(CHARGROUP_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CHARGROUP_COLRANGE
	ADD CONSTRAINT FKB2D8C29379A523E6
	FOREIGN KEY(COLUMNRANGE_ID)
	REFERENCES COLUMNRANGE(COLUMNRANGE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CHARGROUP
	ADD CONSTRAINT FK3AF18C91AA11DC6
	FOREIGN KEY(CHARPARTITION_ID)
	REFERENCES CHARPARTITION(CHARPARTITION_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CHARPARTITION
	ADD CONSTRAINT FKDEA9F834A414944F
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CHARSET_COLRANGE
	ADD CONSTRAINT FK2CBD9079A523E6
	FOREIGN KEY(COLUMNRANGE_ID)
	REFERENCES COLUMNRANGE(COLUMNRANGE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CHARSET_COLRANGE
	ADD CONSTRAINT FK2CBD90416D5B50
	FOREIGN KEY(CHARSET_ID)
	REFERENCES CHARSET(CHARSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CHARSET
	ADD CONSTRAINT FK56D8ED2CA414944F
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CHARWEIGHT_COLRANGE
	ADD CONSTRAINT FK54CA674EF9A75EE
	FOREIGN KEY(CHARWEIGHT_ID)
	REFERENCES CHARWEIGHT(CHARWEIGHT_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CHARWEIGHT_COLRANGE
	ADD CONSTRAINT FK54CA674E79A523E6
	FOREIGN KEY(COLUMNRANGE_ID)
	REFERENCES COLUMNRANGE(COLUMNRANGE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CHARWEIGHT
	ADD CONSTRAINT FK8CC7694EE9B425A6
	FOREIGN KEY(CHARWEIGHTSET_ID)
	REFERENCES CHARWEIGHTSET(CHARWEIGHTSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CHARWEIGHTSET
	ADD CONSTRAINT FK99B8AC34A414944F
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CITATION_AUTHOR
	ADD CONSTRAINT FK24AA55E38E1E4DF8
	FOREIGN KEY(CITATION_ID)
	REFERENCES CITATION(CITATION_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CITATION_AUTHOR
	ADD CONSTRAINT FK24AA55E36707573B
	FOREIGN KEY(AUTHORS_PERSON_ID)
	REFERENCES PERSON(PERSON_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CITATION_EDITOR
	ADD CONSTRAINT FK2A8955C5EC93F501
	FOREIGN KEY(CITATION_ID)
	REFERENCES CITATION(CITATION_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CITATION_EDITOR
	ADD CONSTRAINT FK2A8955C568F6F619
	FOREIGN KEY(EDITORS_PERSON_ID)
	REFERENCES PERSON(PERSON_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CITATION
	ADD CONSTRAINT FKD8A7FAE74C983658
	FOREIGN KEY(CITATIONSTATUS_ID)
	REFERENCES CITATIONSTATUS(CITATIONSTATUS_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CODERECORD_COLRANGE
	ADD CONSTRAINT FKA3E6C61EDC51C546
	FOREIGN KEY(GENETICCODERECORD_ID)
	REFERENCES GENETICCODERECORD(GENETICCODERECORD_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CODERECORD_COLRANGE
	ADD CONSTRAINT FKA3E6C61E79A523E6
	FOREIGN KEY(COLUMNRANGE_ID)
	REFERENCES COLUMNRANGE(COLUMNRANGE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CODONCHAR1_COLRANGE
	ADD CONSTRAINT FKDA5AD4F879A523E6
	FOREIGN KEY(COLUMNRANGE_ID)
	REFERENCES COLUMNRANGE(COLUMNRANGE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CODONCHAR1_COLRANGE
	ADD CONSTRAINT FKDA5AD4F8491DB20E
	FOREIGN KEY(CODONPOSITIONSET_ID)
	REFERENCES CODONPOSITIONSET(CODONPOSITIONSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CODONCHAR2_COLRANGE
	ADD CONSTRAINT FKCEA4461779A523E6
	FOREIGN KEY(COLUMNRANGE_ID)
	REFERENCES COLUMNRANGE(COLUMNRANGE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CODONCHAR2_COLRANGE
	ADD CONSTRAINT FKCEA44617491DB20E
	FOREIGN KEY(CODONPOSITIONSET_ID)
	REFERENCES CODONPOSITIONSET(CODONPOSITIONSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CODONCHAR3_COLRANGE
	ADD CONSTRAINT FKC2EDB73679A523E6
	FOREIGN KEY(COLUMNRANGE_ID)
	REFERENCES COLUMNRANGE(COLUMNRANGE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CODONCHAR3_COLRANGE
	ADD CONSTRAINT FKC2EDB736491DB20E
	FOREIGN KEY(CODONPOSITIONSET_ID)
	REFERENCES CODONPOSITIONSET(CODONPOSITIONSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CODONNONCODING_COLRANGE
	ADD CONSTRAINT FK307897FC79A523E6
	FOREIGN KEY(COLUMNRANGE_ID)
	REFERENCES COLUMNRANGE(COLUMNRANGE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CODONNONCODING_COLRANGE
	ADD CONSTRAINT FK307897FC491DB20E
	FOREIGN KEY(CODONPOSITIONSET_ID)
	REFERENCES CODONPOSITIONSET(CODONPOSITIONSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CODONPOSITIONSET
	ADD CONSTRAINT FK4E501CC2A414944F
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE COMPOUND_ELEMENT
	ADD CONSTRAINT FK311830485F4F7CEB
	FOREIGN KEY(COMPOUND_ID)
	REFERENCES MATRIXELEMENT(MATRIXELEMENT_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE COMPOUND_ELEMENT
	ADD CONSTRAINT FK31183048459091E5
	FOREIGN KEY(ELEMENT_ID)
	REFERENCES MATRIXELEMENT(MATRIXELEMENT_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CONTANCSTATE_VALUE
	ADD CONSTRAINT FKA84B8C3FD450D2BD
	FOREIGN KEY(ANCSTATE_ID)
	REFERENCES ANCESTRALSTATE(ANCESTRALSTATE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CSTREENODE
	ADD CONSTRAINT FK82B9AB10F572A92
	FOREIGN KEY(PARENTNODE_ID)
	REFERENCES CSTREENODE(CSTREENODE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CSTREENODE
	ADD CONSTRAINT FK82B9AB10F4803CE6
	FOREIGN KEY(DISCRETECHARSTATE_ID)
	REFERENCES DISCRETECHARSTATE(DISCRETECHARSTATE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE CSTREENODE
	ADD CONSTRAINT FK82B9AB10C6999FCE
	FOREIGN KEY(CSTREE_ID)
	REFERENCES USERTYPE(USERTYPE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE DISCRETECHARSTATE
	ADD CONSTRAINT FK2EEF2802FE41A723
	FOREIGN KEY(PHYLOCHAR_ID)
	REFERENCES PHYLOCHAR(PHYLOCHAR_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE DISCRETECHARSTATE
	ADD CONSTRAINT FK2EEF2802C7BEAAFE
	FOREIGN KEY(ANCESTRALSTATE_ID)
	REFERENCES ANCESTRALSTATE(ANCESTRALSTATE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE DISCRETECHARSTATE
	ADD CONSTRAINT FK2EEF2802163C67CE
	FOREIGN KEY(STATESET_ID)
	REFERENCES STATESET(STATESET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE DISTANCEMATRIXELEMENT
	ADD CONSTRAINT FK92D3DDE6C1429863
	FOREIGN KEY(COLUMNLABEL_ID)
	REFERENCES TAXONLABEL(TAXONLABEL_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE DISTANCEMATRIXELEMENT
	ADD CONSTRAINT FK92D3DDE64B8EF343
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE DISTANCEMATRIXELEMENT
	ADD CONSTRAINT FK92D3DDE61F08BAE7
	FOREIGN KEY(ROWLABEL_ID)
	REFERENCES TAXONLABEL(TAXONLABEL_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 

ALTER TABLE GENETICCODERECORD
	ADD CONSTRAINT FK237932B782D8ECCE
	FOREIGN KEY(GENETICCODESET_ID)
	REFERENCES GENETICCODESET(GENETICCODESET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE GENETICCODERECORD
	ADD CONSTRAINT FK237932B76CB73FC6
	FOREIGN KEY(GENETICCODE_ID)
	REFERENCES GENETICCODE(GENETICCODE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE GENETICCODESET
	ADD CONSTRAINT FK6A12F7DCA414944F
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE ITEMVALUE
	ADD CONSTRAINT FK27B41A1E459091E5
	FOREIGN KEY(ELEMENT_ID)
	REFERENCES MATRIXELEMENT(MATRIXELEMENT_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE LEFTCHANGESET_CHARSTATE
	ADD CONSTRAINT FKC8877207F4803CE6
	FOREIGN KEY(DISCRETECHARSTATE_ID)
	REFERENCES DISCRETECHARSTATE(DISCRETECHARSTATE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE LEFTCHANGESET_CHARSTATE
	ADD CONSTRAINT FKC887720775763EAE
	FOREIGN KEY(STATECHANGESET_ID)
	REFERENCES STATECHANGESET(STATECHANGESET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIX_ITEMDEFINITION
	ADD CONSTRAINT FK5717E0E4AC5C19DD
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIX_ITEMDEFINITION
	ADD CONSTRAINT FK5717E0E4405A7CEE
	FOREIGN KEY(ITEMDEFINITION_ID)
	REFERENCES ITEMDEFINITION(ITEMDEFINITION_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIX
	ADD CONSTRAINT FK87208BC1E9B425A6
	FOREIGN KEY(CHARWEIGHTSET_ID)
	REFERENCES CHARWEIGHTSET(CHARWEIGHTSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIX
	ADD CONSTRAINT FK87208BC1E310471D
	FOREIGN KEY(TAXONLABELSET_ID)
	REFERENCES TAXONLABELSET(TAXONLABELSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIX
	ADD CONSTRAINT FK87208BC191BD3C8E
	FOREIGN KEY(MATRIXKIND_ID)
	REFERENCES MATRIXKIND(MATRIXKIND_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIX
	ADD CONSTRAINT FK87208BC1684F6406
	FOREIGN KEY(ANCSTATESET_ID)
	REFERENCES ANCSTATESET(ANCSTATESET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIX
	ADD CONSTRAINT FK87208BC14D737E46
	FOREIGN KEY(TYPESET_ID)
	REFERENCES TYPESET(TYPESET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIX
	ADD CONSTRAINT FK87208BC14CE484AE
	FOREIGN KEY(MATRIXDATATYPE_ID)
	REFERENCES MATRIXDATATYPE(MATRIXDATATYPE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIX
	ADD CONSTRAINT FK87208BC1491DB20E
	FOREIGN KEY(CODONPOSITIONSET_ID)
	REFERENCES CODONPOSITIONSET(CODONPOSITIONSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIX
	ADD CONSTRAINT FK87208BC145534A9C
	FOREIGN KEY(CHARSET_ID)
	REFERENCES CHARSET(CHARSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIX
	ADD CONSTRAINT FK87208BC13C572C3C
	FOREIGN KEY(STUDY_ID)
	REFERENCES STUDY(STUDY_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIXCOLUMN_ITEMDEFINITION
	ADD CONSTRAINT FKB556F8E8B2E884E
	FOREIGN KEY(MATRIXCOLUMN_ID)
	REFERENCES MATRIXCOLUMN(MATRIXCOLUMN_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIXCOLUMN_ITEMDEFINITION
	ADD CONSTRAINT FKB556F8E405A7CEE
	FOREIGN KEY(ITEMDEFINITION_ID)
	REFERENCES ITEMDEFINITION(ITEMDEFINITION_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIXCOLUMN
	ADD CONSTRAINT FK9B0BE57F5DECA46
	FOREIGN KEY(PHYLOCHAR_ID)
	REFERENCES PHYLOCHAR(PHYLOCHAR_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIXCOLUMN
	ADD CONSTRAINT FK9B0BE57BED40086
	FOREIGN KEY(STATEFORMAT_ID)
	REFERENCES STATEFORMAT(STATEFORMAT_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIXCOLUMN
	ADD CONSTRAINT FK9B0BE57A414944F
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIXDATATYPE
	ADD CONSTRAINT FKF054C4A5F5DECA46
	FOREIGN KEY(PHYLOCHAR_ID)
	REFERENCES PHYLOCHAR(PHYLOCHAR_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIXELEMENT
	ADD CONSTRAINT FK90AE93FBF4803CE6
	FOREIGN KEY(DISCRETECHARSTATE_ID)
	REFERENCES DISCRETECHARSTATE(DISCRETECHARSTATE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIXELEMENT
	ADD CONSTRAINT FK90AE93FBE7B3CDA6
	FOREIGN KEY(MATRIXROW_ID)
	REFERENCES MATRIXROW(MATRIXROW_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIXELEMENT
	ADD CONSTRAINT FK90AE93FB8B2E884E
	FOREIGN KEY(MATRIXCOLUMN_ID)
	REFERENCES MATRIXCOLUMN(MATRIXCOLUMN_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIXELEMENT
	ADD CONSTRAINT FK90AE93FB405A7CEE
	FOREIGN KEY(ITEMDEFINITION_ID)
	REFERENCES ITEMDEFINITION(ITEMDEFINITION_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIXROW
	ADD CONSTRAINT FKE4688E59A414944F
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE MATRIXROW
	ADD CONSTRAINT FKE4688E5963AB9FD7
	FOREIGN KEY(TAXONLABEL_ID)
	REFERENCES TAXONLABEL(TAXONLABEL_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREE
	ADD CONSTRAINT FK76F2E442FE2812F1
	FOREIGN KEY(TREEKIND_ID)
	REFERENCES TREEKIND(TREEKIND_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREE
	ADD CONSTRAINT FK76F2E442F5AEA931
	FOREIGN KEY(TREETYPE_ID)
	REFERENCES TREETYPE(TREETYPE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREE
	ADD CONSTRAINT FK76F2E442F3D1CF03
	FOREIGN KEY(ROOTNODE_ID)
	REFERENCES PHYLOTREENODE(PHYLOTREENODE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREE
	ADD CONSTRAINT FK76F2E442EA08B443
	FOREIGN KEY(TREEQUALITY_ID)
	REFERENCES TREEQUALITY(TREEQUALITY_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREE
	ADD CONSTRAINT FK76F2E442BFD107C3
	FOREIGN KEY(TREEBLOCK_ID)
	REFERENCES TREEBLOCK(TREEBLOCK_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREE
	ADD CONSTRAINT FK76F2E442A3015CE3
	FOREIGN KEY(TREEATTRIBUTE_ID)
	REFERENCES TREEATTRIBUTE(TREEATTRIBUTE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREE
	ADD CONSTRAINT FK76F2E4423C572C3C
	FOREIGN KEY(STUDY_ID)
	REFERENCES STUDY(STUDY_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREENODE
	ADD CONSTRAINT FK3EA79944B710CB23
	FOREIGN KEY(PHYLOTREE_ID)
	REFERENCES PHYLOTREE(PHYLOTREE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREENODE
	ADD CONSTRAINT FK3EA7994463AB9FD7
	FOREIGN KEY(TAXONLABEL_ID)
	REFERENCES TAXONLABEL(TAXONLABEL_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREENODE
	ADD CONSTRAINT FK3EA799445BCC767D
	FOREIGN KEY(PARENT_ID)
	REFERENCES PHYLOTREENODE(PHYLOTREENODE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREENODE
	ADD CONSTRAINT FK3EA79944434FDCCB
	FOREIGN KEY(CHILD_ID)
	REFERENCES PHYLOTREENODE(PHYLOTREENODE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREENODE
	ADD CONSTRAINT FK3EA799443C348165
	FOREIGN KEY(SIBLING_ID)
	REFERENCES PHYLOTREENODE(PHYLOTREENODE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE PHYLOTREENODE
	ADD CONSTRAINT FK3EA79944271A5763
	FOREIGN KEY(NODEATTRIBUTE_ID)
	REFERENCES NODEATTRIBUTE(NODEATTRIBUTE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE RIGHTCHANGESET_CHARSTATE
	ADD CONSTRAINT FKC8AFC9F2F4803CE6
	FOREIGN KEY(DISCRETECHARSTATE_ID)
	REFERENCES DISCRETECHARSTATE(DISCRETECHARSTATE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE RIGHTCHANGESET_CHARSTATE
	ADD CONSTRAINT FKC8AFC9F275763EAE
	FOREIGN KEY(STATECHANGESET_ID)
	REFERENCES STATECHANGESET(STATECHANGESET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE ROWSEGMENT
	ADD CONSTRAINT FKEE9AE819E7B3CDA6
	FOREIGN KEY(MATRIXROW_ID)
	REFERENCES MATRIXROW(MATRIXROW_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE ROWSEGMENT
	ADD CONSTRAINT FKEE9AE81963AB9FD7
	FOREIGN KEY(TAXONLABEL_ID)
	REFERENCES TAXONLABEL(TAXONLABEL_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE SPECIMENLABEL
	ADD CONSTRAINT FK1E8F38CCA2039B9D
	FOREIGN KEY(GEOSPOT_ID)
	REFERENCES GEOSPOT(GEOSPOT_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE STATEMODIFIER
	ADD CONSTRAINT FKD157BE48F4803CE6
	FOREIGN KEY(DISCRETECHARSTATE_ID)
	REFERENCES DISCRETECHARSTATE(DISCRETECHARSTATE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE STATEMODIFIER
	ADD CONSTRAINT FKD157BE48BED40086
	FOREIGN KEY(STATEFORMAT_ID)
	REFERENCES STATEFORMAT(STATEFORMAT_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE STATEMODIFIER
	ADD CONSTRAINT FKD157BE48459091E5
	FOREIGN KEY(ELEMENT_ID)
	REFERENCES MATRIXELEMENT(MATRIXELEMENT_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE STATESET
	ADD CONSTRAINT FK7D38523150CBAB47
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE STEPMATRIXELEMENT
	ADD CONSTRAINT FK891100AF9E8198E
	FOREIGN KEY(STEPMATRIX_ID)
	REFERENCES USERTYPE(USERTYPE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE STEPMATRIXELEMENT
	ADD CONSTRAINT FK891100AF6E979EE7
	FOREIGN KEY(STATE2_ID)
	REFERENCES DISCRETECHARSTATE(DISCRETECHARSTATE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE STEPMATRIXELEMENT
	ADD CONSTRAINT FK891100AF6E972A88
	FOREIGN KEY(STATE1_ID)
	REFERENCES DISCRETECHARSTATE(DISCRETECHARSTATE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE STUDY_NEXUSFILE
	ADD CONSTRAINT FK47FC1EE53C572C3C
	FOREIGN KEY(STUDY_ID)
	REFERENCES STUDY(STUDY_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE STUDY
	ADD CONSTRAINT FK4B915A9BEF300B2
	FOREIGN KEY(USER_ID)
	REFERENCES "user"(USER_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE STUDY
	ADD CONSTRAINT FK4B915A98E1E4DF8
	FOREIGN KEY(CITATION_ID)
	REFERENCES CITATION(CITATION_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE STUDY
	ADD CONSTRAINT FK4B915A9255A519C
	FOREIGN KEY(STUDYSTATUS_ID)
	REFERENCES STUDYSTATUS(STUDYSTATUS_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE SUB_MATRIX
	ADD CONSTRAINT FK5F26A2C08DFE4858
	FOREIGN KEY(SUBMISSION_ID)
	REFERENCES SUBMISSION(SUBMISSION_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE SUB_MATRIX
	ADD CONSTRAINT FK5F26A2C055961AEE
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE SUB_TAXONLABEL
	ADD CONSTRAINT FK86909E98DFE4858
	FOREIGN KEY(SUBMISSION_ID)
	REFERENCES SUBMISSION(SUBMISSION_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE SUB_TAXONLABEL
	ADD CONSTRAINT FK86909E963AB9FD7
	FOREIGN KEY(TAXONLABEL_ID)
	REFERENCES TAXONLABEL(TAXONLABEL_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE SUB_TREEBLOCK
	ADD CONSTRAINT FK94D50830BFD107C3
	FOREIGN KEY(TREEBLOCK_ID)
	REFERENCES TREEBLOCK(TREEBLOCK_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE SUB_TREEBLOCK
	ADD CONSTRAINT FK94D508308DFE4858
	FOREIGN KEY(SUBMISSION_ID)
	REFERENCES SUBMISSION(SUBMISSION_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE SUBMISSION
	ADD CONSTRAINT FKA120274CBEF300B2
	FOREIGN KEY(USER_ID)
	REFERENCES "user"(USER_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE SUBMISSION
	ADD CONSTRAINT FKA120274C3C572C3C
	FOREIGN KEY(STUDY_ID)
	REFERENCES STUDY(STUDY_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONLABEL
	ADD CONSTRAINT FK5F548A6A3C572C3C
	FOREIGN KEY(STUDY_ID)
	REFERENCES STUDY(STUDY_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONLABEL
	ADD CONSTRAINT FK5F548A6A3C1B6F7
	FOREIGN KEY(TAXONVARIANT_ID)
	REFERENCES TAXONVARIANT(TAXONVARIANT_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONLABELGROUP
	ADD CONSTRAINT FK4AE2663513A813DD
	FOREIGN KEY(TAXONLABELPARTITION_ID)
	REFERENCES TAXONLABELPARTITION(TAXONLABELPARTITION_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONLABELROUP_TAXONLABEL
	ADD CONSTRAINT FK4B1D0BC763AB9FD7
	FOREIGN KEY(TAXONLABEL_ID)
	REFERENCES TAXONLABEL(TAXONLABEL_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONLABELROUP_TAXONLABEL
	ADD CONSTRAINT FK4B1D0BC7294B993D
	FOREIGN KEY(TAXONLABELGROUP_ID)
	REFERENCES TAXONLABELGROUP(TAXONLABELGROUP_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONLABELSET_TAXONLABEL
	ADD CONSTRAINT FK2EB54B71E310471D
	FOREIGN KEY(TAXONLABELSET_ID)
	REFERENCES TAXONLABELSET(TAXONLABELSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONLABELSET_TAXONLABEL
	ADD CONSTRAINT FK2EB54B7163AB9FD7
	FOREIGN KEY(TAXONLABEL_ID)
	REFERENCES TAXONLABEL(TAXONLABEL_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONLABELSET
	ADD CONSTRAINT FK28D3A5983C572C3C
	FOREIGN KEY(STUDY_ID)
	REFERENCES STUDY(STUDY_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONLINK
	ADD CONSTRAINT FKC102D9A43BF5F2F7
	FOREIGN KEY(TAXONAUTHORITY_ID)
	REFERENCES TAXONAUTHORITY(TAXONAUTHORITY_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONLINK
	ADD CONSTRAINT FKC102D9A41DE2FCDD
	FOREIGN KEY(TAXON_ID)
	REFERENCES TAXON(TAXON_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONSET_TAXON
	ADD CONSTRAINT FKC656C9031DE2FCDD
	FOREIGN KEY(TAXON_ID)
	REFERENCES TAXON(TAXON_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONSET_TAXON
	ADD CONSTRAINT FKC656C90313C28457
	FOREIGN KEY(TAXONSET_ID)
	REFERENCES TAXONSET(TAXONSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TAXONVARIANT
	ADD CONSTRAINT FKEE3D127B1DE2FCDD
	FOREIGN KEY(TAXON_ID)
	REFERENCES TAXON(TAXON_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TREEBLOCK
	ADD CONSTRAINT FKA826F38FE310471D
	FOREIGN KEY(TAXONLABELSET_ID)
	REFERENCES TAXONLABELSET(TAXONLABELSET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TREEGROUP_PHYLOTREE
	ADD CONSTRAINT FKFB7D704B710CB23
	FOREIGN KEY(PHYLOTREE_ID)
	REFERENCES PHYLOTREE(PHYLOTREE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TREEGROUP_PHYLOTREE
	ADD CONSTRAINT FKFB7D7045E56A83
	FOREIGN KEY(TREEGROUP_ID)
	REFERENCES TREEGROUP(TREEGROUP_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TREEGROUP
	ADD CONSTRAINT FKA870258180CBD223
	FOREIGN KEY(TREEPARTITION_ID)
	REFERENCES TREEPARTITION(TREEPARTITION_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TREENODEEDGE
	ADD CONSTRAINT FK7767285D27FD0589
	FOREIGN KEY(CHILDNODE_ID)
	REFERENCES PHYLOTREENODE(PHYLOTREENODE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TREENODEEDGE
	ADD CONSTRAINT FK7767285D1851763B
	FOREIGN KEY(PARENTNODE_ID)
	REFERENCES PHYLOTREENODE(PHYLOTREENODE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TREESET_PHYLOTREE
	ADD CONSTRAINT FK31FC19A7EC59B1E3
	FOREIGN KEY(TREESET_ID)
	REFERENCES TREESET(TREESET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TREESET_PHYLOTREE
	ADD CONSTRAINT FK31FC19A7B710CB23
	FOREIGN KEY(PHYLOTREE_ID)
	REFERENCES PHYLOTREE(PHYLOTREE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE TYPESET
	ADD CONSTRAINT FKF7F2B6C8A414944F
	FOREIGN KEY(MATRIX_ID)
	REFERENCES MATRIX(MATRIX_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE "user"
	ADD CONSTRAINT FK27E3CBF59DD12
	FOREIGN KEY(USERROLE_ID)
	REFERENCES USERROLE(USERROLE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE "user"
	ADD CONSTRAINT FK27E3CBE3910672
	FOREIGN KEY(PERSON_ID)
	REFERENCES PERSON(PERSON_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE USERTYPERECORD
	ADD CONSTRAINT FK136AF61692E6A38E
	FOREIGN KEY(USERTYPE_ID)
	REFERENCES USERTYPE(USERTYPE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE USERTYPERECORD
	ADD CONSTRAINT FK136AF6164D737E46
	FOREIGN KEY(TYPESET_ID)
	REFERENCES TYPESET(TYPESET_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE USERTYPERRD_COLRANGE
	ADD CONSTRAINT FK405805DDC009330E
	FOREIGN KEY(USERTYPERECORD_ID)
	REFERENCES USERTYPERECORD(USERTYPERECORD_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION; 
ALTER TABLE USERTYPERRD_COLRANGE
	ADD CONSTRAINT FK405805DD79A523E6
	FOREIGN KEY(COLUMNRANGE_ID)
	REFERENCES COLUMNRANGE(COLUMNRANGE_ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION;

-- INDICES
CREATE INDEX matrixcolumn_phylochar_id_idx ON matrixcolumn(phylochar_id);
CREATE INDEX matrixrow_taxonlabel_id_idx ON matrixrow(taxonlabel_id);
CREATE INDEX matrixrow_matrix_id_idx on matrixrow(matrix_id);
CREATE INDEX matrixdatatype_phylochar_id_idx ON matrixdatatype(phylochar_id);
CREATE INDEX rowsegment_matrixrow_id_idx ON rowsegment(matrixrow_id);

CREATE INDEX matrixelement_matrixcolumn_id_idx ON matrixelement(matrixcolumn_id);
CREATE INDEX matrixelement_matrixrow_id_idx ON matrixelement(matrixrow_id);
CREATE INDEX matrixelement_discretecharstate_id_idx ON matrixelement(discretecharstate_id);

CREATE INDEX matrixelement_discretecharstate_id_idx ON matrixelement(discretecharstate_id);
CREATE INDEX discretecharstate_phylochar_id_idx ON discretecharstate(phylochar_id);

CREATE INDEX analysis_study_id_idx ON analysis(study_id);
CREATE INDEX submission_study_id_idx ON submission(study_id);
CREATE INDEX study_citation_id_idx ON study(citation_id);