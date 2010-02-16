-- Initial Treebase schema, just prior to rolling out schema patching. 
-- Modified by VG from this 2010-02-15 dump: 
-- pg_dump -h treebase-dev.nescent.org -U treebase_app --format=p --no-owner --no-privileges --schema-only  treebasestage > 0000_SCHEMA_before_patches_start.sql
--  - Removed CREATE PROCEDURAL LANGUAGE plpgsql;
--  - Removed functions: pg_grant, pg_owner, pg_revoke

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;


--
-- Name: algorithm_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE algorithm_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: algorithm; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE algorithm (
    type character(1) NOT NULL,
    algorithm_id bigint DEFAULT nextval('algorithm_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(2000),
    propertyname character varying(255),
    propertyvalue character varying(255),
    usertype_id bigint,
    gapmode_id bigint,
    polytcount_id bigint
);


--
-- Name: analysis_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE analysis_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: analysis; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE analysis (
    analysis_id bigint DEFAULT nextval('analysis_id_sequence'::regclass) NOT NULL,
    version integer,
    name character varying(255),
    notes character varying(2000),
    validated boolean,
    study_id bigint,
    analysis_order integer
);


--
-- Name: analysisstep_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE analysisstep_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: analysisstep; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE analysisstep (
    analysisstep_id bigint DEFAULT nextval('analysisstep_id_sequence'::regclass) NOT NULL,
    version integer,
    commands character varying(2000),
    name character varying(255),
    notes character varying(2000),
    algorithm_id bigint,
    analysis_id bigint,
    software_id bigint,
    step_order integer
);


--
-- Name: analyzeddata_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE analyzeddata_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: analyzeddata; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE analyzeddata (
    type character(1) NOT NULL,
    analyzeddata_id bigint DEFAULT nextval('analyzeddata_id_sequence'::regclass) NOT NULL,
    version integer,
    input boolean,
    notes character varying(2000),
    treelength integer,
    analysisstep_id bigint NOT NULL,
    matrix_id bigint,
    phylotree_id bigint
);


--
-- Name: ancestralstate_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE ancestralstate_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: ancestralstate; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ancestralstate (
    type character(1) NOT NULL,
    ancestralstate_id bigint DEFAULT nextval('ancestralstate_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255),
    ancvalue character varying(255),
    discretecharstate_id bigint,
    ancstateset_id bigint
);


--
-- Name: ancstateset_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE ancstateset_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: ancstateset; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ancstateset (
    ancstateset_id bigint DEFAULT nextval('ancstateset_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255),
    matrix_id bigint
);


--
-- Name: chargroup_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE chargroup_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: chargroup; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE chargroup (
    chargroup_id bigint DEFAULT nextval('chargroup_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255),
    charpartition_id bigint
);


--
-- Name: chargroup_colrange; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE chargroup_colrange (
    chargroup_id bigint NOT NULL,
    columnrange_id bigint NOT NULL
);


--
-- Name: charpartition_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE charpartition_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: charpartition; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE charpartition (
    charpartition_id bigint DEFAULT nextval('charpartition_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255),
    matrix_id bigint
);


--
-- Name: charset_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE charset_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: charset; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE charset (
    type character(1) NOT NULL,
    charset_id bigint DEFAULT nextval('charset_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255),
    matrix_id bigint
);


--
-- Name: charset_colrange; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE charset_colrange (
    charset_id bigint NOT NULL,
    columnrange_id bigint NOT NULL
);


--
-- Name: charweight_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE charweight_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: charweight; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE charweight (
    type character(1) NOT NULL,
    charweight_id bigint DEFAULT nextval('charweight_id_sequence'::regclass) NOT NULL,
    version integer,
    weight integer,
    realweight double precision,
    charweightset_id bigint
);


--
-- Name: charweight_colrange; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE charweight_colrange (
    charweight_id bigint NOT NULL,
    columnrange_id bigint NOT NULL
);


--
-- Name: charweightset_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE charweightset_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: charweightset; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE charweightset (
    charweightset_id bigint DEFAULT nextval('charweightset_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255),
    matrix_id bigint
);


--
-- Name: citation_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE citation_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: citation; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE citation (
    type character(1) NOT NULL,
    citation_id bigint DEFAULT nextval('citation_id_sequence'::regclass) NOT NULL,
    version integer,
    pmid character varying(255),
    url character varying(255),
    abstract character varying(10000),
    doi character varying(255),
    keywords character varying(255),
    pages character varying(255),
    publishyear integer,
    published boolean,
    title character varying(500),
    issue character varying(255),
    journal character varying(255),
    volume character varying(255),
    isbn character varying(255),
    booktitle character varying(255),
    city character varying(255),
    publisher character varying(255),
    citationstatus_id bigint
);


--
-- Name: citation_author; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE citation_author (
    citation_id bigint NOT NULL,
    authors_person_id bigint NOT NULL,
    author_order integer NOT NULL
);


--
-- Name: citation_editor; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE citation_editor (
    citation_id bigint NOT NULL,
    editors_person_id bigint NOT NULL,
    editor_order integer NOT NULL
);


--
-- Name: citationstatus_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE citationstatus_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: citationstatus; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE citationstatus (
    citationstatus_id bigint DEFAULT nextval('citationstatus_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(50)
);


--
-- Name: coderecord_colrange; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE coderecord_colrange (
    geneticcoderecord_id bigint NOT NULL,
    columnrange_id bigint NOT NULL
);


--
-- Name: codonchar1_colrange; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE codonchar1_colrange (
    codonpositionset_id bigint NOT NULL,
    columnrange_id bigint NOT NULL
);


--
-- Name: codonchar2_colrange; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE codonchar2_colrange (
    codonpositionset_id bigint NOT NULL,
    columnrange_id bigint NOT NULL
);


--
-- Name: codonchar3_colrange; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE codonchar3_colrange (
    codonpositionset_id bigint NOT NULL,
    columnrange_id bigint NOT NULL
);


--
-- Name: codonnoncoding_colrange; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE codonnoncoding_colrange (
    codonpositionset_id bigint NOT NULL,
    columnrange_id bigint NOT NULL
);


--
-- Name: codonpositionset_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE codonpositionset_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: codonpositionset; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE codonpositionset (
    codonpositionset_id bigint DEFAULT nextval('codonpositionset_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255),
    matrix_id bigint
);


--
-- Name: columnrange_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE columnrange_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: columnrange; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE columnrange (
    columnrange_id bigint DEFAULT nextval('columnrange_id_sequence'::regclass) NOT NULL,
    version integer,
    endcolindex integer,
    repeatinterval integer,
    startcolindex integer
);


--
-- Name: compound_element; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE compound_element (
    compound_id bigint NOT NULL,
    element_id bigint NOT NULL
);


--
-- Name: contancstate_value; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE contancstate_value (
    ancstate_id bigint NOT NULL,
    element character varying(255)
);


--
-- Name: cstreenode_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cstreenode_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cstreenode; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE cstreenode (
    cstreenode_id bigint DEFAULT nextval('cstreenode_id_sequence'::regclass) NOT NULL,
    version integer,
    discretecharstate_id bigint,
    parentnode_id bigint,
    cstree_id bigint NOT NULL
);


--
-- Name: discretecharstate_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE discretecharstate_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: discretecharstate; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE discretecharstate (
    discretecharstate_id bigint DEFAULT nextval('discretecharstate_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(255),
    notes character varying(255),
    symbol character(1),
    phylochar_id bigint NOT NULL,
    stateset_id bigint,
    ancestralstate_id bigint
);


--
-- Name: distancematrixelement_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE distancematrixelement_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: distancematrixelement; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE distancematrixelement (
    distancematrixelement_id bigint DEFAULT nextval('distancematrixelement_id_sequence'::regclass) NOT NULL,
    version integer,
    distance double precision,
    columnlabel_id bigint,
    matrix_id bigint NOT NULL,
    rowlabel_id bigint NOT NULL
);


--
-- Name: gapmode_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE gapmode_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: gapmode; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE gapmode (
    gapmode_id bigint DEFAULT nextval('gapmode_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(255)
);


--
-- Name: geneticcode_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE geneticcode_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: geneticcode; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE geneticcode (
    geneticcode_id bigint DEFAULT nextval('geneticcode_id_sequence'::regclass) NOT NULL,
    version integer,
    codedescription character varying(1000),
    codeorder character varying(255),
    extensions character varying(255),
    nucorder character varying(255),
    predefined boolean,
    title character varying(255)
);


--
-- Name: geneticcoderecord_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE geneticcoderecord_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: geneticcoderecord; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE geneticcoderecord (
    geneticcoderecord_id bigint DEFAULT nextval('geneticcoderecord_id_sequence'::regclass) NOT NULL,
    version integer,
    geneticcode_id bigint,
    geneticcodeset_id bigint NOT NULL
);


--
-- Name: geneticcodeset_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE geneticcodeset_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: geneticcodeset; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE geneticcodeset (
    geneticcodeset_id bigint DEFAULT nextval('geneticcodeset_id_sequence'::regclass) NOT NULL,
    version integer,
    format character varying(255),
    title character varying(255),
    matrix_id bigint
);


--
-- Name: geospot_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE geospot_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: help_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE help_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: help; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE help (
    help_id bigint DEFAULT nextval('help_id_sequence'::regclass) NOT NULL,
    version integer,
    helptext text,
    tag character varying(255)
);


--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: itemdefinition_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE itemdefinition_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: itemdefinition; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE itemdefinition (
    itemdefinition_id bigint DEFAULT nextval('itemdefinition_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(255)
);


--
-- Name: itemvalue_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE itemvalue_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: itemvalue; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE itemvalue (
    itemvalue_id bigint DEFAULT nextval('itemvalue_id_sequence'::regclass) NOT NULL,
    version integer,
    value character varying(255),
    element_id bigint NOT NULL
);


--
-- Name: leftchangeset_charstate; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE leftchangeset_charstate (
    statechangeset_id bigint NOT NULL,
    discretecharstate_id bigint NOT NULL
);


--
-- Name: matrix_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE matrix_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: matrix; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE matrix (
    matrixtype character(1) NOT NULL,
    matrix_id bigint DEFAULT nextval('matrix_id_sequence'::regclass) NOT NULL,
    version integer,
    tb_matrixid character varying(30),
    description character varying(2000),
    gapsymbol character(1),
    missingsymbol character(1),
    nexusfilename character varying(255),
    published boolean,
    symbols character varying(255),
    title character varying(255),
    "nchar" integer,
    ntax integer,
    aligned boolean,
    diagonal boolean,
    triangle character varying(255),
    casesensitive boolean,
    matrixdatatype_id bigint,
    matrixkind_id bigint,
    study_id bigint,
    taxonlabelset_id bigint,
    ancstateset_id bigint,
    codonpositionset_id bigint,
    charset_id bigint,
    typeset_id bigint,
    charweightset_id bigint
);


--
-- Name: matrix_itemdefinition; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE matrix_itemdefinition (
    matrix_id bigint NOT NULL,
    itemdefinition_id bigint NOT NULL
);


--
-- Name: matrixcolumn_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE matrixcolumn_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: matrixcolumn; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE matrixcolumn (
    matrixcolumn_id bigint DEFAULT nextval('matrixcolumn_id_sequence'::regclass) NOT NULL,
    version integer,
    phylochar_id bigint,
    matrix_id bigint,
    stateformat_id bigint,
    column_order integer
);


--
-- Name: matrixcolumn_itemdefinition; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE matrixcolumn_itemdefinition (
    matrixcolumn_id bigint NOT NULL,
    itemdefinition_id bigint NOT NULL
);


--
-- Name: matrixdatatype_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE matrixdatatype_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: matrixdatatype; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE matrixdatatype (
    matrixdatatype_id bigint DEFAULT nextval('matrixdatatype_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(255),
    phylochar_id bigint
);


--
-- Name: matrixelement_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE matrixelement_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: matrixelement; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE matrixelement (
    type character(1) NOT NULL,
    matrixelement_id bigint DEFAULT nextval('matrixelement_id_sequence'::regclass) NOT NULL,
    version integer,
    andlogic boolean,
    compoundvalue character varying(1000),
    value double precision,
    gap boolean,
    matrixcolumn_id bigint,
    matrixrow_id bigint,
    itemdefinition_id bigint,
    discretecharstate_id bigint,
    element_order integer
);


--
-- Name: matrixkind_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE matrixkind_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: matrixkind; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE matrixkind (
    matrixkind_id bigint DEFAULT nextval('matrixkind_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(100)
);


--
-- Name: matrixrow_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE matrixrow_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: matrixrow; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE matrixrow (
    matrixrow_id bigint DEFAULT nextval('matrixrow_id_sequence'::regclass) NOT NULL,
    version integer,
    symbolstring text,
    matrix_id bigint,
    taxonlabel_id bigint NOT NULL,
    row_order integer
);


--
-- Name: nodeattribute_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE nodeattribute_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: nodeattribute; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE nodeattribute (
    nodeattribute_id bigint DEFAULT nextval('nodeattribute_id_sequence'::regclass) NOT NULL,
    version integer
);


--
-- Name: person_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE person_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: person; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE person (
    person_id bigint DEFAULT nextval('person_id_sequence'::regclass) NOT NULL,
    version integer,
    email character varying(255),
    firstname character varying(255),
    lastname character varying(255) NOT NULL,
    mname character varying(255),
    phone character varying(50)
);


--
-- Name: phylochar_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE phylochar_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: phylochar; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE phylochar (
    type character(1) NOT NULL,
    phylochar_id bigint DEFAULT nextval('phylochar_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(255),
    lowerlimit double precision,
    upperlimit double precision
);


--
-- Name: phylotree_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE phylotree_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: phylotree; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE phylotree (
    phylotree_id bigint DEFAULT nextval('phylotree_id_sequence'::regclass) NOT NULL,
    version integer,
    tb1_treeid character varying(30),
    bigtree boolean,
    label character varying(255),
    ntax integer,
    newickstring text,
    nexusfilename character varying(255),
    published boolean,
    rootedtree boolean,
    title character varying(255),
    rootnode_id bigint,
    study_id bigint,
    treeattribute_id bigint,
    treeblock_id bigint,
    treekind_id bigint,
    treequality_id bigint,
    treetype_id bigint
);


--
-- Name: phylotreenode_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE phylotreenode_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: phylotreenode; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE phylotreenode (
    phylotreenode_id bigint DEFAULT nextval('phylotreenode_id_sequence'::regclass) NOT NULL,
    version integer,
    branchlength double precision,
    leftnode bigint,
    name character varying(255),
    nodedepth integer,
    rightnode bigint,
    child_id bigint,
    nodeattribute_id bigint,
    parent_id bigint,
    sibling_id bigint,
    taxonlabel_id bigint,
    phylotree_id bigint NOT NULL
);


--
-- Name: polytcount_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE polytcount_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: polytcount; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE polytcount (
    polytcount_id bigint DEFAULT nextval('polytcount_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(255)
);


--
-- Name: rightchangeset_charstate; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE rightchangeset_charstate (
    statechangeset_id bigint NOT NULL,
    discretecharstate_id bigint NOT NULL
);


--
-- Name: rowsegment_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE rowsegment_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: rowsegment; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE rowsegment (
    rowsegment_id bigint DEFAULT nextval('rowsegment_id_sequence'::regclass) NOT NULL,
    version integer,
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
);


--
-- Name: software_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE software_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: software; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE software (
    software_id bigint DEFAULT nextval('software_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(2000),
    name character varying(255),
    softwareurl character varying(500),
    softwareversion character varying(255)
);


--
-- Name: statechangeset_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE statechangeset_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: statechangeset; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE statechangeset (
    statechangeset_id bigint DEFAULT nextval('statechangeset_id_sequence'::regclass) NOT NULL,
    version integer,
    reversible boolean,
    title character varying(255)
);


--
-- Name: stateformat_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE stateformat_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: stateformat; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE stateformat (
    stateformat_id bigint DEFAULT nextval('stateformat_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(255)
);


--
-- Name: statemodifier_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE statemodifier_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: statemodifier; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE statemodifier (
    statemodifier_id bigint DEFAULT nextval('statemodifier_id_sequence'::regclass) NOT NULL,
    version integer,
    count integer,
    frequency double precision,
    discretecharstate_id bigint,
    element_id bigint NOT NULL,
    stateformat_id bigint NOT NULL
);


--
-- Name: stateset_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE stateset_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: stateset; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE stateset (
    stateset_id bigint DEFAULT nextval('stateset_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255),
    matrix_id bigint
);


--
-- Name: stepmatrixelement; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE stepmatrixelement (
    discretecharstate_id bigint DEFAULT nextval('discretecharstate_id_sequence'::regclass) NOT NULL,
    version integer,
    transcost double precision,
    state1_id bigint,
    state2_id bigint,
    stepmatrix_id bigint NOT NULL
);


--
-- Name: study_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE study_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: study; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE study (
    study_id bigint DEFAULT nextval('study_id_sequence'::regclass) NOT NULL,
    version integer,
    tb_studyid character varying(30),
    accessionnumber character varying(255),
    lastmodifieddate date,
    name character varying(255),
    notes character varying(2000),
    releasedate date,
    citation_id bigint,
    user_id bigint,
    studystatus_id bigint
);


--
-- Name: study_nexusfile; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE study_nexusfile (
    study_id bigint NOT NULL,
    nexus text NOT NULL,
    filename character varying(255) NOT NULL
);


--
-- Name: studystatus_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE studystatus_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: studystatus; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE studystatus (
    studystatus_id bigint DEFAULT nextval('studystatus_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(255)
);


--
-- Name: sub_matrix; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE sub_matrix (
    submission_id bigint NOT NULL,
    matrix_id bigint NOT NULL,
    collection_id bigint NOT NULL
);


--
-- Name: sub_taxonlabel; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE sub_taxonlabel (
    submission_id bigint NOT NULL,
    taxonlabel_id bigint NOT NULL
);


--
-- Name: sub_treeblock; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE sub_treeblock (
    submission_id bigint NOT NULL,
    treeblock_id bigint NOT NULL,
    collection_id bigint NOT NULL
);


--
-- Name: submission_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE submission_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: submission; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE submission (
    submission_id bigint DEFAULT nextval('submission_id_sequence'::regclass) NOT NULL,
    version integer,
    createdate date,
    submissionnumber character varying(255),
    test integer,
    study_id bigint NOT NULL,
    user_id bigint
);


--
-- Name: taxon_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE taxon_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: taxon; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE taxon (
    taxon_id bigint DEFAULT nextval('taxon_id_sequence'::regclass) NOT NULL,
    version integer,
    tb1legacyid integer,
    ubionamebankid bigint,
    description character varying(2000),
    groupcode integer,
    name character varying(255),
    ncbitaxid integer
);


--
-- Name: taxonauthority_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE taxonauthority_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: taxonauthority; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE taxonauthority (
    taxonauthority_id bigint DEFAULT nextval('taxonauthority_id_sequence'::regclass) NOT NULL,
    version integer,
    connectionstr character varying(1000),
    description character varying(2000),
    name character varying(255)
);


--
-- Name: taxonlabel_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE taxonlabel_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: taxonlabel; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE taxonlabel (
    taxonlabel_id bigint DEFAULT nextval('taxonlabel_id_sequence'::regclass) NOT NULL,
    version integer,
    linked boolean,
    taxonlabel character varying(255),
    study_id bigint,
    taxonvariant_id bigint
);


--
-- Name: taxonlabelgroup_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE taxonlabelgroup_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: taxonlabelgroup; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE taxonlabelgroup (
    taxonlabelgroup_id bigint DEFAULT nextval('taxonlabelgroup_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255),
    taxonlabelpartition_id bigint
);


--
-- Name: taxonlabelgroup_taxonlabel; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE taxonlabelgroup_taxonlabel (
    taxonlabelgroup_id bigint NOT NULL,
    taxonlabel_id bigint NOT NULL
);


--
-- Name: taxonlabelpartition_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE taxonlabelpartition_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: taxonlabelpartition; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE taxonlabelpartition (
    taxonlabelpartition_id bigint DEFAULT nextval('taxonlabelpartition_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255)
);


--
-- Name: taxonlabelset_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE taxonlabelset_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: taxonlabelset; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE taxonlabelset (
    taxonlabelset_id bigint DEFAULT nextval('taxonlabelset_id_sequence'::regclass) NOT NULL,
    version integer,
    taxa boolean,
    title character varying(255),
    study_id bigint
);


--
-- Name: taxonlabelset_taxonlabel; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE taxonlabelset_taxonlabel (
    taxonlabelset_id bigint NOT NULL,
    taxonlabel_id bigint NOT NULL,
    taxonlabel_order integer NOT NULL
);


--
-- Name: taxonlink_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE taxonlink_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: taxonlink; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE taxonlink (
    linktype character(1) NOT NULL,
    taxonlink_id bigint DEFAULT nextval('taxonlink_id_sequence'::regclass) NOT NULL,
    version integer,
    foreigntaxonid character varying(255),
    taxonauthority_id bigint NOT NULL,
    taxon_id bigint NOT NULL
);


--
-- Name: taxonset_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE taxonset_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: taxonset; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE taxonset (
    taxonset_id bigint DEFAULT nextval('taxonset_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255)
);


--
-- Name: taxonset_taxon; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE taxonset_taxon (
    taxonset_id bigint NOT NULL,
    taxon_id bigint NOT NULL,
    taxon_order integer NOT NULL
);


--
-- Name: taxonvariant_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE taxonvariant_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: taxonvariant; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE taxonvariant (
    taxonvariant_id bigint DEFAULT nextval('taxonvariant_id_sequence'::regclass) NOT NULL,
    version integer,
    tb1legacyid integer,
    fullname character varying(255),
    lexicalqualifier character varying(50),
    name character varying(255),
    namebankid bigint,
    taxon_id bigint NOT NULL
);


--
-- Name: treeattribute_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE treeattribute_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: treeattribute; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE treeattribute (
    treeattribute_id bigint DEFAULT nextval('treeattribute_id_sequence'::regclass) NOT NULL,
    version integer
);


--
-- Name: treeblock_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE treeblock_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: treeblock; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE treeblock (
    treeblock_id bigint DEFAULT nextval('treeblock_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255),
    taxonlabelset_id bigint
);


--
-- Name: treegroup_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE treegroup_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: treegroup; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE treegroup (
    treegroup_id bigint DEFAULT nextval('treegroup_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255),
    treepartition_id bigint
);


--
-- Name: treegroup_phylotree; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE treegroup_phylotree (
    treegroup_id bigint NOT NULL,
    phylotree_id bigint NOT NULL
);


--
-- Name: treekind_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE treekind_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: treekind; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE treekind (
    treekind_id bigint DEFAULT nextval('treekind_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(100)
);


--
-- Name: treenodeedge_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE treenodeedge_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: treenodeedge; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE treenodeedge (
    treenodeedge_id bigint DEFAULT nextval('treenodeedge_id_sequence'::regclass) NOT NULL,
    version integer,
    childnode_id bigint NOT NULL,
    parentnode_id bigint NOT NULL
);


--
-- Name: treepartition_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE treepartition_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: treepartition; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE treepartition (
    treepartition_id bigint DEFAULT nextval('treepartition_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255)
);


--
-- Name: treequality_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE treequality_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: treequality; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE treequality (
    treequality_id bigint DEFAULT nextval('treequality_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(255)
);


--
-- Name: treeset_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE treeset_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: treeset; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE treeset (
    treeset_id bigint DEFAULT nextval('treeset_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255)
);


--
-- Name: treeset_phylotree; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE treeset_phylotree (
    treeset_id bigint NOT NULL,
    phylotree_id bigint NOT NULL,
    tree_order integer NOT NULL
);


--
-- Name: treetype_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE treetype_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: treetype; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE treetype (
    treetype_id bigint DEFAULT nextval('treetype_id_sequence'::regclass) NOT NULL,
    version integer,
    description character varying(255)
);


--
-- Name: typeset_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE typeset_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: typeset; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE typeset (
    typeset_id bigint DEFAULT nextval('typeset_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255),
    matrix_id bigint
);


--
-- Name: user_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE user_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: user; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE "user" (
    user_id bigint DEFAULT nextval('user_id_sequence'::regclass) NOT NULL,
    version integer,
    password character varying(100) NOT NULL,
    username character varying(100) NOT NULL,
    person_id bigint NOT NULL,
    userrole_id bigint NOT NULL
);


--
-- Name: userrole_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE userrole_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: userrole; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE userrole (
    userrole_id bigint DEFAULT nextval('userrole_id_sequence'::regclass) NOT NULL,
    version integer,
    authority character varying(255) NOT NULL
);


--
-- Name: usertype_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE usertype_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: usertype; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE usertype (
    type character(1) NOT NULL,
    usertype_id bigint DEFAULT nextval('usertype_id_sequence'::regclass) NOT NULL,
    version integer,
    title character varying(255)
);


--
-- Name: usertyperecord_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE usertyperecord_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: usertyperecord; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE usertyperecord (
    usertyperecord_id bigint DEFAULT nextval('usertyperecord_id_sequence'::regclass) NOT NULL,
    version integer,
    usertype_id bigint,
    typeset_id bigint
);


--
-- Name: usertyperrd_colrange; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE usertyperrd_colrange (
    usertyperecord_id bigint NOT NULL,
    columnrange_id bigint NOT NULL
);


--
-- Name: versionhistory; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE versionhistory (
    id integer NOT NULL,
    patchnumber integer,
    patchlabel character varying(63) NOT NULL,
    patchdescription character varying(1023),
    applied timestamp(0) without time zone DEFAULT now()
);


--
-- Name: TABLE versionhistory; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE versionhistory IS 'VersionHistory table is NOT a part of the TreeBase application. It is only used within the development and deployment process to keep track of schema patches applied to a DB instance. ';


--
-- Name: versionhistory_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE versionhistory_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: versionhistory_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE versionhistory_id_seq OWNED BY versionhistory.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE versionhistory ALTER COLUMN id SET DEFAULT nextval('versionhistory_id_seq'::regclass);


--
-- Name: algorithm_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY algorithm
    ADD CONSTRAINT algorithm_pkey PRIMARY KEY (algorithm_id);


--
-- Name: analysis_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY analysis
    ADD CONSTRAINT analysis_pkey PRIMARY KEY (analysis_id);


--
-- Name: analysisstep_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY analysisstep
    ADD CONSTRAINT analysisstep_pkey PRIMARY KEY (analysisstep_id);


--
-- Name: analyzeddata_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY analyzeddata
    ADD CONSTRAINT analyzeddata_pkey PRIMARY KEY (analyzeddata_id);


--
-- Name: ancestralstate_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ancestralstate
    ADD CONSTRAINT ancestralstate_pkey PRIMARY KEY (ancestralstate_id);


--
-- Name: ancstateset_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ancstateset
    ADD CONSTRAINT ancstateset_pkey PRIMARY KEY (ancstateset_id);


--
-- Name: chargroup_colrange_columnrange_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY chargroup_colrange
    ADD CONSTRAINT chargroup_colrange_columnrange_id_key UNIQUE (columnrange_id);


--
-- Name: chargroup_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY chargroup
    ADD CONSTRAINT chargroup_pkey PRIMARY KEY (chargroup_id);


--
-- Name: charpartition_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY charpartition
    ADD CONSTRAINT charpartition_pkey PRIMARY KEY (charpartition_id);


--
-- Name: charset_colrange_columnrange_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY charset_colrange
    ADD CONSTRAINT charset_colrange_columnrange_id_key UNIQUE (columnrange_id);


--
-- Name: charset_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY charset
    ADD CONSTRAINT charset_pkey PRIMARY KEY (charset_id);


--
-- Name: charweight_colrange_columnrange_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY charweight_colrange
    ADD CONSTRAINT charweight_colrange_columnrange_id_key UNIQUE (columnrange_id);


--
-- Name: charweight_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY charweight
    ADD CONSTRAINT charweight_pkey PRIMARY KEY (charweight_id);


--
-- Name: charweightset_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY charweightset
    ADD CONSTRAINT charweightset_pkey PRIMARY KEY (charweightset_id);


--
-- Name: citation_author_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY citation_author
    ADD CONSTRAINT citation_author_pkey PRIMARY KEY (citation_id, author_order);


--
-- Name: citation_editor_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY citation_editor
    ADD CONSTRAINT citation_editor_pkey PRIMARY KEY (citation_id, editor_order);


--
-- Name: citation_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY citation
    ADD CONSTRAINT citation_pkey PRIMARY KEY (citation_id);


--
-- Name: citationstatus_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY citationstatus
    ADD CONSTRAINT citationstatus_pkey PRIMARY KEY (citationstatus_id);


--
-- Name: coderecord_colrange_columnrange_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY coderecord_colrange
    ADD CONSTRAINT coderecord_colrange_columnrange_id_key UNIQUE (columnrange_id);


--
-- Name: codonchar1_colrange_columnrange_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY codonchar1_colrange
    ADD CONSTRAINT codonchar1_colrange_columnrange_id_key UNIQUE (columnrange_id);


--
-- Name: codonchar2_colrange_columnrange_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY codonchar2_colrange
    ADD CONSTRAINT codonchar2_colrange_columnrange_id_key UNIQUE (columnrange_id);


--
-- Name: codonchar3_colrange_columnrange_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY codonchar3_colrange
    ADD CONSTRAINT codonchar3_colrange_columnrange_id_key UNIQUE (columnrange_id);


--
-- Name: codonnoncoding_colrange_columnrange_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY codonnoncoding_colrange
    ADD CONSTRAINT codonnoncoding_colrange_columnrange_id_key UNIQUE (columnrange_id);


--
-- Name: codonpositionset_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY codonpositionset
    ADD CONSTRAINT codonpositionset_pkey PRIMARY KEY (codonpositionset_id);


--
-- Name: columnrange_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY columnrange
    ADD CONSTRAINT columnrange_pkey PRIMARY KEY (columnrange_id);


--
-- Name: compound_element_element_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY compound_element
    ADD CONSTRAINT compound_element_element_id_key UNIQUE (element_id);


--
-- Name: compound_element_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY compound_element
    ADD CONSTRAINT compound_element_pkey PRIMARY KEY (compound_id, element_id);


--
-- Name: cstreenode_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY cstreenode
    ADD CONSTRAINT cstreenode_pkey PRIMARY KEY (cstreenode_id);


--
-- Name: discretecharstate_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY discretecharstate
    ADD CONSTRAINT discretecharstate_pkey PRIMARY KEY (discretecharstate_id);


--
-- Name: distancematrixelement_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY distancematrixelement
    ADD CONSTRAINT distancematrixelement_pkey PRIMARY KEY (distancematrixelement_id);


--
-- Name: gapmode_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY gapmode
    ADD CONSTRAINT gapmode_pkey PRIMARY KEY (gapmode_id);


--
-- Name: geneticcode_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY geneticcode
    ADD CONSTRAINT geneticcode_pkey PRIMARY KEY (geneticcode_id);


--
-- Name: geneticcoderecord_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY geneticcoderecord
    ADD CONSTRAINT geneticcoderecord_pkey PRIMARY KEY (geneticcoderecord_id);


--
-- Name: geneticcodeset_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY geneticcodeset
    ADD CONSTRAINT geneticcodeset_pkey PRIMARY KEY (geneticcodeset_id);


--
-- Name: help_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY help
    ADD CONSTRAINT help_pkey PRIMARY KEY (help_id);


--
-- Name: itemdefinition_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY itemdefinition
    ADD CONSTRAINT itemdefinition_pkey PRIMARY KEY (itemdefinition_id);


--
-- Name: itemvalue_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY itemvalue
    ADD CONSTRAINT itemvalue_pkey PRIMARY KEY (itemvalue_id);


--
-- Name: matrix_itemdefinition_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY matrix_itemdefinition
    ADD CONSTRAINT matrix_itemdefinition_pkey PRIMARY KEY (matrix_id, itemdefinition_id);


--
-- Name: matrix_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY matrix
    ADD CONSTRAINT matrix_pkey PRIMARY KEY (matrix_id);


--
-- Name: matrixcolumn_itemdefinition_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY matrixcolumn_itemdefinition
    ADD CONSTRAINT matrixcolumn_itemdefinition_pkey PRIMARY KEY (matrixcolumn_id, itemdefinition_id);


--
-- Name: matrixcolumn_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY matrixcolumn
    ADD CONSTRAINT matrixcolumn_pkey PRIMARY KEY (matrixcolumn_id);


--
-- Name: matrixdatatype_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY matrixdatatype
    ADD CONSTRAINT matrixdatatype_pkey PRIMARY KEY (matrixdatatype_id);


--
-- Name: matrixelement_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY matrixelement
    ADD CONSTRAINT matrixelement_pkey PRIMARY KEY (matrixelement_id);


--
-- Name: matrixkind_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY matrixkind
    ADD CONSTRAINT matrixkind_pkey PRIMARY KEY (matrixkind_id);


--
-- Name: matrixrow_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY matrixrow
    ADD CONSTRAINT matrixrow_pkey PRIMARY KEY (matrixrow_id);


--
-- Name: nodeattribute_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY nodeattribute
    ADD CONSTRAINT nodeattribute_pkey PRIMARY KEY (nodeattribute_id);


--
-- Name: person_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (person_id);


--
-- Name: phylochar_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY phylochar
    ADD CONSTRAINT phylochar_pkey PRIMARY KEY (phylochar_id);


--
-- Name: phylotree_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY phylotree
    ADD CONSTRAINT phylotree_pkey PRIMARY KEY (phylotree_id);


--
-- Name: phylotreenode_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY phylotreenode
    ADD CONSTRAINT phylotreenode_pkey PRIMARY KEY (phylotreenode_id);


--
-- Name: polytcount_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY polytcount
    ADD CONSTRAINT polytcount_pkey PRIMARY KEY (polytcount_id);


--
-- Name: rowsegment_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rowsegment
    ADD CONSTRAINT rowsegment_pkey PRIMARY KEY (rowsegment_id);


--
-- Name: software_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY software
    ADD CONSTRAINT software_pkey PRIMARY KEY (software_id);


--
-- Name: statechangeset_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY statechangeset
    ADD CONSTRAINT statechangeset_pkey PRIMARY KEY (statechangeset_id);


--
-- Name: stateformat_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY stateformat
    ADD CONSTRAINT stateformat_pkey PRIMARY KEY (stateformat_id);


--
-- Name: statemodifier_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY statemodifier
    ADD CONSTRAINT statemodifier_pkey PRIMARY KEY (statemodifier_id);


--
-- Name: stateset_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY stateset
    ADD CONSTRAINT stateset_pkey PRIMARY KEY (stateset_id);


--
-- Name: stepmatrixelement_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY stepmatrixelement
    ADD CONSTRAINT stepmatrixelement_pkey PRIMARY KEY (discretecharstate_id);


--
-- Name: study_nexusfile_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY study_nexusfile
    ADD CONSTRAINT study_nexusfile_pkey PRIMARY KEY (study_id, filename);


--
-- Name: study_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY study
    ADD CONSTRAINT study_pkey PRIMARY KEY (study_id);


--
-- Name: studystatus_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY studystatus
    ADD CONSTRAINT studystatus_pkey PRIMARY KEY (studystatus_id);


--
-- Name: sub_matrix_matrix_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sub_matrix
    ADD CONSTRAINT sub_matrix_matrix_id_key UNIQUE (matrix_id);


--
-- Name: sub_matrix_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sub_matrix
    ADD CONSTRAINT sub_matrix_pkey PRIMARY KEY (collection_id);


--
-- Name: sub_taxonlabel_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sub_taxonlabel
    ADD CONSTRAINT sub_taxonlabel_pkey PRIMARY KEY (submission_id, taxonlabel_id);


--
-- Name: sub_taxonlabel_taxonlabel_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sub_taxonlabel
    ADD CONSTRAINT sub_taxonlabel_taxonlabel_id_key UNIQUE (taxonlabel_id);


--
-- Name: sub_treeblock_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sub_treeblock
    ADD CONSTRAINT sub_treeblock_pkey PRIMARY KEY (collection_id);


--
-- Name: sub_treeblock_treeblock_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sub_treeblock
    ADD CONSTRAINT sub_treeblock_treeblock_id_key UNIQUE (treeblock_id);


--
-- Name: submission_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY submission
    ADD CONSTRAINT submission_pkey PRIMARY KEY (submission_id);


--
-- Name: submission_study_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY submission
    ADD CONSTRAINT submission_study_id_key UNIQUE (study_id);


--
-- Name: taxon_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY taxon
    ADD CONSTRAINT taxon_pkey PRIMARY KEY (taxon_id);


--
-- Name: taxonauthority_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY taxonauthority
    ADD CONSTRAINT taxonauthority_pkey PRIMARY KEY (taxonauthority_id);


--
-- Name: taxonlabel_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY taxonlabel
    ADD CONSTRAINT taxonlabel_pkey PRIMARY KEY (taxonlabel_id);


--
-- Name: taxonlabelgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY taxonlabelgroup
    ADD CONSTRAINT taxonlabelgroup_pkey PRIMARY KEY (taxonlabelgroup_id);


--
-- Name: taxonlabelpartition_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY taxonlabelpartition
    ADD CONSTRAINT taxonlabelpartition_pkey PRIMARY KEY (taxonlabelpartition_id);


--
-- Name: taxonlabelset_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY taxonlabelset
    ADD CONSTRAINT taxonlabelset_pkey PRIMARY KEY (taxonlabelset_id);


--
-- Name: taxonlabelset_taxonlabel_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY taxonlabelset_taxonlabel
    ADD CONSTRAINT taxonlabelset_taxonlabel_pkey PRIMARY KEY (taxonlabelset_id, taxonlabel_order);


--
-- Name: taxonlink_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY taxonlink
    ADD CONSTRAINT taxonlink_pkey PRIMARY KEY (taxonlink_id);


--
-- Name: taxonset_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY taxonset
    ADD CONSTRAINT taxonset_pkey PRIMARY KEY (taxonset_id);


--
-- Name: taxonset_taxon_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY taxonset_taxon
    ADD CONSTRAINT taxonset_taxon_pkey PRIMARY KEY (taxonset_id, taxon_order);


--
-- Name: taxonvariant_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY taxonvariant
    ADD CONSTRAINT taxonvariant_pkey PRIMARY KEY (taxonvariant_id);


--
-- Name: treeattribute_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY treeattribute
    ADD CONSTRAINT treeattribute_pkey PRIMARY KEY (treeattribute_id);


--
-- Name: treeblock_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY treeblock
    ADD CONSTRAINT treeblock_pkey PRIMARY KEY (treeblock_id);


--
-- Name: treegroup_phylotree_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY treegroup_phylotree
    ADD CONSTRAINT treegroup_phylotree_pkey PRIMARY KEY (treegroup_id, phylotree_id);


--
-- Name: treegroup_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY treegroup
    ADD CONSTRAINT treegroup_pkey PRIMARY KEY (treegroup_id);


--
-- Name: treekind_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY treekind
    ADD CONSTRAINT treekind_pkey PRIMARY KEY (treekind_id);


--
-- Name: treenodeedge_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY treenodeedge
    ADD CONSTRAINT treenodeedge_pkey PRIMARY KEY (treenodeedge_id);


--
-- Name: treepartition_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY treepartition
    ADD CONSTRAINT treepartition_pkey PRIMARY KEY (treepartition_id);


--
-- Name: treequality_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY treequality
    ADD CONSTRAINT treequality_pkey PRIMARY KEY (treequality_id);


--
-- Name: treeset_phylotree_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY treeset_phylotree
    ADD CONSTRAINT treeset_phylotree_pkey PRIMARY KEY (treeset_id, tree_order);


--
-- Name: treeset_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY treeset
    ADD CONSTRAINT treeset_pkey PRIMARY KEY (treeset_id);


--
-- Name: treetype_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY treetype
    ADD CONSTRAINT treetype_pkey PRIMARY KEY (treetype_id);


--
-- Name: typeset_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY typeset
    ADD CONSTRAINT typeset_pkey PRIMARY KEY (typeset_id);


--
-- Name: user_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);


--
-- Name: user_username_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_username_key UNIQUE (username);


--
-- Name: userrole_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY userrole
    ADD CONSTRAINT userrole_pkey PRIMARY KEY (userrole_id);


--
-- Name: usertype_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY usertype
    ADD CONSTRAINT usertype_pkey PRIMARY KEY (usertype_id);


--
-- Name: usertyperecord_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY usertyperecord
    ADD CONSTRAINT usertyperecord_pkey PRIMARY KEY (usertyperecord_id);


--
-- Name: usertyperrd_colrange_columnrange_id_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY usertyperrd_colrange
    ADD CONSTRAINT usertyperrd_colrange_columnrange_id_key UNIQUE (columnrange_id);


--
-- Name: versionhistory_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY versionhistory
    ADD CONSTRAINT versionhistory_pkey PRIMARY KEY (id);


--
-- Name: versionhistory_unique_patchnumber; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY versionhistory
    ADD CONSTRAINT versionhistory_unique_patchnumber UNIQUE (patchnumber);


--
-- Name: analysis_study_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX analysis_study_id_idx ON analysis USING btree (study_id);


--
-- Name: matrixcolumn_phylochar_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX matrixcolumn_phylochar_id_idx ON matrixcolumn USING btree (phylochar_id);


--
-- Name: matrixdatatype_phylochar_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX matrixdatatype_phylochar_id_idx ON matrixdatatype USING btree (phylochar_id);


--
-- Name: matrixelement_discretecharstate_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX matrixelement_discretecharstate_id_idx ON matrixelement USING btree (discretecharstate_id);


--
-- Name: matrixelement_matrixcolumn_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX matrixelement_matrixcolumn_id_idx ON matrixelement USING btree (matrixcolumn_id);


--
-- Name: matrixelement_matrixrow_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX matrixelement_matrixrow_id_idx ON matrixelement USING btree (matrixrow_id);


--
-- Name: matrixrow_matrix_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX matrixrow_matrix_id_idx ON matrixrow USING btree (matrix_id);


--
-- Name: matrixrow_taxonlabel_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX matrixrow_taxonlabel_id_idx ON matrixrow USING btree (taxonlabel_id);


--
-- Name: rowsegment_matrixrow_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX rowsegment_matrixrow_id_idx ON rowsegment USING btree (matrixrow_id);


--
-- Name: study_citation_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX study_citation_id_idx ON study USING btree (citation_id);


--
-- Name: submission_study_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX submission_study_id_idx ON submission USING btree (study_id);


--
-- Name: fk136af6164d737e46; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY usertyperecord
    ADD CONSTRAINT fk136af6164d737e46 FOREIGN KEY (typeset_id) REFERENCES typeset(typeset_id);


--
-- Name: fk136af61692e6a38e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY usertyperecord
    ADD CONSTRAINT fk136af61692e6a38e FOREIGN KEY (usertype_id) REFERENCES usertype(usertype_id);


--
-- Name: fk21f853a848a2817c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY analysisstep
    ADD CONSTRAINT fk21f853a848a2817c FOREIGN KEY (algorithm_id) REFERENCES algorithm(algorithm_id);


--
-- Name: fk21f853a865edd5f8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY analysisstep
    ADD CONSTRAINT fk21f853a865edd5f8 FOREIGN KEY (software_id) REFERENCES software(software_id);


--
-- Name: fk21f853a8d1884dd8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY analysisstep
    ADD CONSTRAINT fk21f853a8d1884dd8 FOREIGN KEY (analysis_id) REFERENCES analysis(analysis_id);


--
-- Name: fk237932b76cb73fc6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geneticcoderecord
    ADD CONSTRAINT fk237932b76cb73fc6 FOREIGN KEY (geneticcode_id) REFERENCES geneticcode(geneticcode_id);


--
-- Name: fk237932b782d8ecce; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geneticcoderecord
    ADD CONSTRAINT fk237932b782d8ecce FOREIGN KEY (geneticcodeset_id) REFERENCES geneticcodeset(geneticcodeset_id);


--
-- Name: fk24aa55e36707573b; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY citation_author
    ADD CONSTRAINT fk24aa55e36707573b FOREIGN KEY (authors_person_id) REFERENCES person(person_id);


--
-- Name: fk24aa55e38e1e4df8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY citation_author
    ADD CONSTRAINT fk24aa55e38e1e4df8 FOREIGN KEY (citation_id) REFERENCES citation(citation_id);


--
-- Name: fk27b41a1e459091e5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY itemvalue
    ADD CONSTRAINT fk27b41a1e459091e5 FOREIGN KEY (element_id) REFERENCES matrixelement(matrixelement_id);


--
-- Name: fk28d3a5983c572c3c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonlabelset
    ADD CONSTRAINT fk28d3a5983c572c3c FOREIGN KEY (study_id) REFERENCES study(study_id);


--
-- Name: fk2a8955c568f6f619; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY citation_editor
    ADD CONSTRAINT fk2a8955c568f6f619 FOREIGN KEY (editors_person_id) REFERENCES person(person_id);


--
-- Name: fk2a8955c5ec93f501; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY citation_editor
    ADD CONSTRAINT fk2a8955c5ec93f501 FOREIGN KEY (citation_id) REFERENCES citation(citation_id);


--
-- Name: fk2cbd90416d5b50; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY charset_colrange
    ADD CONSTRAINT fk2cbd90416d5b50 FOREIGN KEY (charset_id) REFERENCES charset(charset_id);


--
-- Name: fk2cbd9079a523e6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY charset_colrange
    ADD CONSTRAINT fk2cbd9079a523e6 FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);


--
-- Name: fk2eb54b7163ab9fd7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonlabelset_taxonlabel
    ADD CONSTRAINT fk2eb54b7163ab9fd7 FOREIGN KEY (taxonlabel_id) REFERENCES taxonlabel(taxonlabel_id);


--
-- Name: fk2eb54b71e310471d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonlabelset_taxonlabel
    ADD CONSTRAINT fk2eb54b71e310471d FOREIGN KEY (taxonlabelset_id) REFERENCES taxonlabelset(taxonlabelset_id);


--
-- Name: fk2eef2802163c67ce; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY discretecharstate
    ADD CONSTRAINT fk2eef2802163c67ce FOREIGN KEY (stateset_id) REFERENCES stateset(stateset_id);


--
-- Name: fk2eef2802c7beaafe; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY discretecharstate
    ADD CONSTRAINT fk2eef2802c7beaafe FOREIGN KEY (ancestralstate_id) REFERENCES ancestralstate(ancestralstate_id);


--
-- Name: fk2eef2802fe41a723; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY discretecharstate
    ADD CONSTRAINT fk2eef2802fe41a723 FOREIGN KEY (phylochar_id) REFERENCES phylochar(phylochar_id);


--
-- Name: fk307897fc491db20e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY codonnoncoding_colrange
    ADD CONSTRAINT fk307897fc491db20e FOREIGN KEY (codonpositionset_id) REFERENCES codonpositionset(codonpositionset_id);


--
-- Name: fk307897fc79a523e6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY codonnoncoding_colrange
    ADD CONSTRAINT fk307897fc79a523e6 FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);


--
-- Name: fk31183048459091e5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compound_element
    ADD CONSTRAINT fk31183048459091e5 FOREIGN KEY (element_id) REFERENCES matrixelement(matrixelement_id);


--
-- Name: fk311830485f4f7ceb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compound_element
    ADD CONSTRAINT fk311830485f4f7ceb FOREIGN KEY (compound_id) REFERENCES matrixelement(matrixelement_id);


--
-- Name: fk31fc19a7b710cb23; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY treeset_phylotree
    ADD CONSTRAINT fk31fc19a7b710cb23 FOREIGN KEY (phylotree_id) REFERENCES phylotree(phylotree_id);


--
-- Name: fk31fc19a7ec59b1e3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY treeset_phylotree
    ADD CONSTRAINT fk31fc19a7ec59b1e3 FOREIGN KEY (treeset_id) REFERENCES treeset(treeset_id);


--
-- Name: fk36ebcbe3910672; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT fk36ebcbe3910672 FOREIGN KEY (person_id) REFERENCES person(person_id);


--
-- Name: fk36ebcbf59dd12; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT fk36ebcbf59dd12 FOREIGN KEY (userrole_id) REFERENCES userrole(userrole_id);


--
-- Name: fk3af18c91aa11dc6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY chargroup
    ADD CONSTRAINT fk3af18c91aa11dc6 FOREIGN KEY (charpartition_id) REFERENCES charpartition(charpartition_id);


--
-- Name: fk3ea79944271a5763; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotreenode
    ADD CONSTRAINT fk3ea79944271a5763 FOREIGN KEY (nodeattribute_id) REFERENCES nodeattribute(nodeattribute_id);


--
-- Name: fk3ea799443c348165; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotreenode
    ADD CONSTRAINT fk3ea799443c348165 FOREIGN KEY (sibling_id) REFERENCES phylotreenode(phylotreenode_id);


--
-- Name: fk3ea79944434fdccb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotreenode
    ADD CONSTRAINT fk3ea79944434fdccb FOREIGN KEY (child_id) REFERENCES phylotreenode(phylotreenode_id);


--
-- Name: fk3ea799445bcc767d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotreenode
    ADD CONSTRAINT fk3ea799445bcc767d FOREIGN KEY (parent_id) REFERENCES phylotreenode(phylotreenode_id);


--
-- Name: fk3ea7994463ab9fd7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotreenode
    ADD CONSTRAINT fk3ea7994463ab9fd7 FOREIGN KEY (taxonlabel_id) REFERENCES taxonlabel(taxonlabel_id);


--
-- Name: fk3ea79944b710cb23; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotreenode
    ADD CONSTRAINT fk3ea79944b710cb23 FOREIGN KEY (phylotree_id) REFERENCES phylotree(phylotree_id);


--
-- Name: fk3fa14284684f6406; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ancestralstate
    ADD CONSTRAINT fk3fa14284684f6406 FOREIGN KEY (ancstateset_id) REFERENCES ancstateset(ancstateset_id);


--
-- Name: fk3fa14284f4803ce6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ancestralstate
    ADD CONSTRAINT fk3fa14284f4803ce6 FOREIGN KEY (discretecharstate_id) REFERENCES discretecharstate(discretecharstate_id);


--
-- Name: fk405805dd79a523e6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY usertyperrd_colrange
    ADD CONSTRAINT fk405805dd79a523e6 FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);


--
-- Name: fk405805ddc009330e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY usertyperrd_colrange
    ADD CONSTRAINT fk405805ddc009330e FOREIGN KEY (usertyperecord_id) REFERENCES usertyperecord(usertyperecord_id);


--
-- Name: fk47fc1ee53c572c3c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY study_nexusfile
    ADD CONSTRAINT fk47fc1ee53c572c3c FOREIGN KEY (study_id) REFERENCES study(study_id);


--
-- Name: fk4ae2663513a813dd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonlabelgroup
    ADD CONSTRAINT fk4ae2663513a813dd FOREIGN KEY (taxonlabelpartition_id) REFERENCES taxonlabelpartition(taxonlabelpartition_id);


--
-- Name: fk4b915a9255a519c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY study
    ADD CONSTRAINT fk4b915a9255a519c FOREIGN KEY (studystatus_id) REFERENCES studystatus(studystatus_id);


--
-- Name: fk4b915a98e1e4df8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY study
    ADD CONSTRAINT fk4b915a98e1e4df8 FOREIGN KEY (citation_id) REFERENCES citation(citation_id);


--
-- Name: fk4b915a9bef300b2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY study
    ADD CONSTRAINT fk4b915a9bef300b2 FOREIGN KEY (user_id) REFERENCES "user"(user_id);


--
-- Name: fk4e501cc2a414944f; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY codonpositionset
    ADD CONSTRAINT fk4e501cc2a414944f FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fk54ca674e79a523e6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY charweight_colrange
    ADD CONSTRAINT fk54ca674e79a523e6 FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);


--
-- Name: fk54ca674ef9a75ee; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY charweight_colrange
    ADD CONSTRAINT fk54ca674ef9a75ee FOREIGN KEY (charweight_id) REFERENCES charweight(charweight_id);


--
-- Name: fk56d8ed2ca414944f; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY charset
    ADD CONSTRAINT fk56d8ed2ca414944f FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fk5717e0e4405a7cee; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrix_itemdefinition
    ADD CONSTRAINT fk5717e0e4405a7cee FOREIGN KEY (itemdefinition_id) REFERENCES itemdefinition(itemdefinition_id);


--
-- Name: fk5717e0e4ac5c19dd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrix_itemdefinition
    ADD CONSTRAINT fk5717e0e4ac5c19dd FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fk5f26a2c055961aee; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sub_matrix
    ADD CONSTRAINT fk5f26a2c055961aee FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fk5f26a2c08dfe4858; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sub_matrix
    ADD CONSTRAINT fk5f26a2c08dfe4858 FOREIGN KEY (submission_id) REFERENCES submission(submission_id);


--
-- Name: fk5f548a6a3c1b6f7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonlabel
    ADD CONSTRAINT fk5f548a6a3c1b6f7 FOREIGN KEY (taxonvariant_id) REFERENCES taxonvariant(taxonvariant_id);


--
-- Name: fk5f548a6a3c572c3c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonlabel
    ADD CONSTRAINT fk5f548a6a3c572c3c FOREIGN KEY (study_id) REFERENCES study(study_id);


--
-- Name: fk6a12f7dca414944f; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geneticcodeset
    ADD CONSTRAINT fk6a12f7dca414944f FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fk73bbf6b4294b993d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonlabelgroup_taxonlabel
    ADD CONSTRAINT fk73bbf6b4294b993d FOREIGN KEY (taxonlabelgroup_id) REFERENCES taxonlabelgroup(taxonlabelgroup_id);


--
-- Name: fk73bbf6b463ab9fd7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonlabelgroup_taxonlabel
    ADD CONSTRAINT fk73bbf6b463ab9fd7 FOREIGN KEY (taxonlabel_id) REFERENCES taxonlabel(taxonlabel_id);


--
-- Name: fk76f2e4423c572c3c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotree
    ADD CONSTRAINT fk76f2e4423c572c3c FOREIGN KEY (study_id) REFERENCES study(study_id);


--
-- Name: fk76f2e442a3015ce3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotree
    ADD CONSTRAINT fk76f2e442a3015ce3 FOREIGN KEY (treeattribute_id) REFERENCES treeattribute(treeattribute_id);


--
-- Name: fk76f2e442bfd107c3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotree
    ADD CONSTRAINT fk76f2e442bfd107c3 FOREIGN KEY (treeblock_id) REFERENCES treeblock(treeblock_id);


--
-- Name: fk76f2e442ea08b443; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotree
    ADD CONSTRAINT fk76f2e442ea08b443 FOREIGN KEY (treequality_id) REFERENCES treequality(treequality_id);


--
-- Name: fk76f2e442f5aea931; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotree
    ADD CONSTRAINT fk76f2e442f5aea931 FOREIGN KEY (treetype_id) REFERENCES treetype(treetype_id);


--
-- Name: fk76f2e442fe2812f1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotree
    ADD CONSTRAINT fk76f2e442fe2812f1 FOREIGN KEY (treekind_id) REFERENCES treekind(treekind_id);


--
-- Name: fk7767285d1851763b; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY treenodeedge
    ADD CONSTRAINT fk7767285d1851763b FOREIGN KEY (parentnode_id) REFERENCES phylotreenode(phylotreenode_id);


--
-- Name: fk7767285d27fd0589; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY treenodeedge
    ADD CONSTRAINT fk7767285d27fd0589 FOREIGN KEY (childnode_id) REFERENCES phylotreenode(phylotreenode_id);


--
-- Name: fk7d38523150cbab47; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY stateset
    ADD CONSTRAINT fk7d38523150cbab47 FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fk82b9ab10c6999fce; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cstreenode
    ADD CONSTRAINT fk82b9ab10c6999fce FOREIGN KEY (cstree_id) REFERENCES usertype(usertype_id);


--
-- Name: fk82b9ab10f4803ce6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cstreenode
    ADD CONSTRAINT fk82b9ab10f4803ce6 FOREIGN KEY (discretecharstate_id) REFERENCES discretecharstate(discretecharstate_id);


--
-- Name: fk82b9ab10f572a92; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cstreenode
    ADD CONSTRAINT fk82b9ab10f572a92 FOREIGN KEY (parentnode_id) REFERENCES cstreenode(cstreenode_id);


--
-- Name: fk86909e963ab9fd7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sub_taxonlabel
    ADD CONSTRAINT fk86909e963ab9fd7 FOREIGN KEY (taxonlabel_id) REFERENCES taxonlabel(taxonlabel_id);


--
-- Name: fk86909e98dfe4858; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sub_taxonlabel
    ADD CONSTRAINT fk86909e98dfe4858 FOREIGN KEY (submission_id) REFERENCES submission(submission_id);


--
-- Name: fk87208bc13c572c3c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrix
    ADD CONSTRAINT fk87208bc13c572c3c FOREIGN KEY (study_id) REFERENCES study(study_id);


--
-- Name: fk87208bc145534a9c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrix
    ADD CONSTRAINT fk87208bc145534a9c FOREIGN KEY (charset_id) REFERENCES charset(charset_id);


--
-- Name: fk87208bc1491db20e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrix
    ADD CONSTRAINT fk87208bc1491db20e FOREIGN KEY (codonpositionset_id) REFERENCES codonpositionset(codonpositionset_id);


--
-- Name: fk87208bc14ce484ae; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrix
    ADD CONSTRAINT fk87208bc14ce484ae FOREIGN KEY (matrixdatatype_id) REFERENCES matrixdatatype(matrixdatatype_id);


--
-- Name: fk87208bc14d737e46; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrix
    ADD CONSTRAINT fk87208bc14d737e46 FOREIGN KEY (typeset_id) REFERENCES typeset(typeset_id);


--
-- Name: fk87208bc1684f6406; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrix
    ADD CONSTRAINT fk87208bc1684f6406 FOREIGN KEY (ancstateset_id) REFERENCES ancstateset(ancstateset_id);


--
-- Name: fk87208bc191bd3c8e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrix
    ADD CONSTRAINT fk87208bc191bd3c8e FOREIGN KEY (matrixkind_id) REFERENCES matrixkind(matrixkind_id);


--
-- Name: fk87208bc1e310471d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrix
    ADD CONSTRAINT fk87208bc1e310471d FOREIGN KEY (taxonlabelset_id) REFERENCES taxonlabelset(taxonlabelset_id);


--
-- Name: fk87208bc1e9b425a6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrix
    ADD CONSTRAINT fk87208bc1e9b425a6 FOREIGN KEY (charweightset_id) REFERENCES charweightset(charweightset_id);


--
-- Name: fk879339e7a414944f; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ancstateset
    ADD CONSTRAINT fk879339e7a414944f FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fk891100af6e972a88; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY stepmatrixelement
    ADD CONSTRAINT fk891100af6e972a88 FOREIGN KEY (state1_id) REFERENCES discretecharstate(discretecharstate_id);


--
-- Name: fk891100af6e979ee7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY stepmatrixelement
    ADD CONSTRAINT fk891100af6e979ee7 FOREIGN KEY (state2_id) REFERENCES discretecharstate(discretecharstate_id);


--
-- Name: fk891100af9e8198e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY stepmatrixelement
    ADD CONSTRAINT fk891100af9e8198e FOREIGN KEY (stepmatrix_id) REFERENCES usertype(usertype_id);


--
-- Name: fk8c9618424ba97f78; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY analyzeddata
    ADD CONSTRAINT fk8c9618424ba97f78 FOREIGN KEY (analysisstep_id) REFERENCES analysisstep(analysisstep_id);


--
-- Name: fk8c96184255961aee; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY analyzeddata
    ADD CONSTRAINT fk8c96184255961aee FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fk8c961842b710cb23; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY analyzeddata
    ADD CONSTRAINT fk8c961842b710cb23 FOREIGN KEY (phylotree_id) REFERENCES phylotree(phylotree_id);


--
-- Name: fk8cc7694ee9b425a6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY charweight
    ADD CONSTRAINT fk8cc7694ee9b425a6 FOREIGN KEY (charweightset_id) REFERENCES charweightset(charweightset_id);


--
-- Name: fk92d3dde61f08bae7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY distancematrixelement
    ADD CONSTRAINT fk92d3dde61f08bae7 FOREIGN KEY (rowlabel_id) REFERENCES taxonlabel(taxonlabel_id);


--
-- Name: fk92d3dde64b8ef343; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY distancematrixelement
    ADD CONSTRAINT fk92d3dde64b8ef343 FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fk92d3dde6c1429863; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY distancematrixelement
    ADD CONSTRAINT fk92d3dde6c1429863 FOREIGN KEY (columnlabel_id) REFERENCES taxonlabel(taxonlabel_id);


--
-- Name: fk94d508308dfe4858; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sub_treeblock
    ADD CONSTRAINT fk94d508308dfe4858 FOREIGN KEY (submission_id) REFERENCES submission(submission_id);


--
-- Name: fk94d50830bfd107c3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sub_treeblock
    ADD CONSTRAINT fk94d50830bfd107c3 FOREIGN KEY (treeblock_id) REFERENCES treeblock(treeblock_id);


--
-- Name: fk99b8ac34a414944f; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY charweightset
    ADD CONSTRAINT fk99b8ac34a414944f FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fk9b0be57a414944f; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrixcolumn
    ADD CONSTRAINT fk9b0be57a414944f FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fk9b0be57bed40086; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrixcolumn
    ADD CONSTRAINT fk9b0be57bed40086 FOREIGN KEY (stateformat_id) REFERENCES stateformat(stateformat_id);


--
-- Name: fk9b0be57f5deca46; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrixcolumn
    ADD CONSTRAINT fk9b0be57f5deca46 FOREIGN KEY (phylochar_id) REFERENCES phylochar(phylochar_id);


--
-- Name: fka120274c3c572c3c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY submission
    ADD CONSTRAINT fka120274c3c572c3c FOREIGN KEY (study_id) REFERENCES study(study_id);


--
-- Name: fka120274cbef300b2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY submission
    ADD CONSTRAINT fka120274cbef300b2 FOREIGN KEY (user_id) REFERENCES "user"(user_id);


--
-- Name: fka3e6c61e79a523e6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY coderecord_colrange
    ADD CONSTRAINT fka3e6c61e79a523e6 FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);


--
-- Name: fka3e6c61edc51c546; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY coderecord_colrange
    ADD CONSTRAINT fka3e6c61edc51c546 FOREIGN KEY (geneticcoderecord_id) REFERENCES geneticcoderecord(geneticcoderecord_id);


--
-- Name: fka826f38fe310471d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY treeblock
    ADD CONSTRAINT fka826f38fe310471d FOREIGN KEY (taxonlabelset_id) REFERENCES taxonlabelset(taxonlabelset_id);


--
-- Name: fka84b8c3fd450d2bd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY contancstate_value
    ADD CONSTRAINT fka84b8c3fd450d2bd FOREIGN KEY (ancstate_id) REFERENCES ancestralstate(ancestralstate_id);


--
-- Name: fka870258180cbd223; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY treegroup
    ADD CONSTRAINT fka870258180cbd223 FOREIGN KEY (treepartition_id) REFERENCES treepartition(treepartition_id);


--
-- Name: fkb2d8c29379a523e6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY chargroup_colrange
    ADD CONSTRAINT fkb2d8c29379a523e6 FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);


--
-- Name: fkb2d8c293971fffa6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY chargroup_colrange
    ADD CONSTRAINT fkb2d8c293971fffa6 FOREIGN KEY (chargroup_id) REFERENCES chargroup(chargroup_id);


--
-- Name: fkb388c44f92e6a38e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY algorithm
    ADD CONSTRAINT fkb388c44f92e6a38e FOREIGN KEY (usertype_id) REFERENCES usertype(usertype_id);


--
-- Name: fkb388c44f9698d32e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY algorithm
    ADD CONSTRAINT fkb388c44f9698d32e FOREIGN KEY (polytcount_id) REFERENCES polytcount(polytcount_id);


--
-- Name: fkb388c44fc6e814e6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY algorithm
    ADD CONSTRAINT fkb388c44fc6e814e6 FOREIGN KEY (gapmode_id) REFERENCES gapmode(gapmode_id);


--
-- Name: fkb556f8e405a7cee; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrixcolumn_itemdefinition
    ADD CONSTRAINT fkb556f8e405a7cee FOREIGN KEY (itemdefinition_id) REFERENCES itemdefinition(itemdefinition_id);


--
-- Name: fkb556f8e8b2e884e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrixcolumn_itemdefinition
    ADD CONSTRAINT fkb556f8e8b2e884e FOREIGN KEY (matrixcolumn_id) REFERENCES matrixcolumn(matrixcolumn_id);


--
-- Name: fkc102d9a41de2fcdd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonlink
    ADD CONSTRAINT fkc102d9a41de2fcdd FOREIGN KEY (taxon_id) REFERENCES taxon(taxon_id);


--
-- Name: fkc102d9a43bf5f2f7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonlink
    ADD CONSTRAINT fkc102d9a43bf5f2f7 FOREIGN KEY (taxonauthority_id) REFERENCES taxonauthority(taxonauthority_id);


--
-- Name: fkc2edb736491db20e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY codonchar3_colrange
    ADD CONSTRAINT fkc2edb736491db20e FOREIGN KEY (codonpositionset_id) REFERENCES codonpositionset(codonpositionset_id);


--
-- Name: fkc2edb73679a523e6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY codonchar3_colrange
    ADD CONSTRAINT fkc2edb73679a523e6 FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);


--
-- Name: fkc656c90313c28457; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonset_taxon
    ADD CONSTRAINT fkc656c90313c28457 FOREIGN KEY (taxonset_id) REFERENCES taxonset(taxonset_id);


--
-- Name: fkc656c9031de2fcdd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonset_taxon
    ADD CONSTRAINT fkc656c9031de2fcdd FOREIGN KEY (taxon_id) REFERENCES taxon(taxon_id);


--
-- Name: fkc887720775763eae; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY leftchangeset_charstate
    ADD CONSTRAINT fkc887720775763eae FOREIGN KEY (statechangeset_id) REFERENCES statechangeset(statechangeset_id);


--
-- Name: fkc8877207f4803ce6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY leftchangeset_charstate
    ADD CONSTRAINT fkc8877207f4803ce6 FOREIGN KEY (discretecharstate_id) REFERENCES discretecharstate(discretecharstate_id);


--
-- Name: fkc8afc9f275763eae; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY rightchangeset_charstate
    ADD CONSTRAINT fkc8afc9f275763eae FOREIGN KEY (statechangeset_id) REFERENCES statechangeset(statechangeset_id);


--
-- Name: fkc8afc9f2f4803ce6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY rightchangeset_charstate
    ADD CONSTRAINT fkc8afc9f2f4803ce6 FOREIGN KEY (discretecharstate_id) REFERENCES discretecharstate(discretecharstate_id);


--
-- Name: fkcea44617491db20e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY codonchar2_colrange
    ADD CONSTRAINT fkcea44617491db20e FOREIGN KEY (codonpositionset_id) REFERENCES codonpositionset(codonpositionset_id);


--
-- Name: fkcea4461779a523e6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY codonchar2_colrange
    ADD CONSTRAINT fkcea4461779a523e6 FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);


--
-- Name: fkd157be48459091e5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY statemodifier
    ADD CONSTRAINT fkd157be48459091e5 FOREIGN KEY (element_id) REFERENCES matrixelement(matrixelement_id);


--
-- Name: fkd157be48bed40086; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY statemodifier
    ADD CONSTRAINT fkd157be48bed40086 FOREIGN KEY (stateformat_id) REFERENCES stateformat(stateformat_id);


--
-- Name: fkd157be48f4803ce6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY statemodifier
    ADD CONSTRAINT fkd157be48f4803ce6 FOREIGN KEY (discretecharstate_id) REFERENCES discretecharstate(discretecharstate_id);


--
-- Name: fkd8a7fae74c983658; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY citation
    ADD CONSTRAINT fkd8a7fae74c983658 FOREIGN KEY (citationstatus_id) REFERENCES citationstatus(citationstatus_id);


--
-- Name: fkda5ad4f8491db20e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY codonchar1_colrange
    ADD CONSTRAINT fkda5ad4f8491db20e FOREIGN KEY (codonpositionset_id) REFERENCES codonpositionset(codonpositionset_id);


--
-- Name: fkda5ad4f879a523e6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY codonchar1_colrange
    ADD CONSTRAINT fkda5ad4f879a523e6 FOREIGN KEY (columnrange_id) REFERENCES columnrange(columnrange_id);


--
-- Name: fkdea9f834a414944f; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY charpartition
    ADD CONSTRAINT fkdea9f834a414944f FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fke4688e5963ab9fd7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrixrow
    ADD CONSTRAINT fke4688e5963ab9fd7 FOREIGN KEY (taxonlabel_id) REFERENCES taxonlabel(taxonlabel_id);


--
-- Name: fke4688e59a414944f; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrixrow
    ADD CONSTRAINT fke4688e59a414944f FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fkee3d127b1de2fcdd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY taxonvariant
    ADD CONSTRAINT fkee3d127b1de2fcdd FOREIGN KEY (taxon_id) REFERENCES taxon(taxon_id);


--
-- Name: fkee9ae81963ab9fd7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY rowsegment
    ADD CONSTRAINT fkee9ae81963ab9fd7 FOREIGN KEY (taxonlabel_id) REFERENCES taxonlabel(taxonlabel_id);


--
-- Name: fkee9ae819e7b3cda6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY rowsegment
    ADD CONSTRAINT fkee9ae819e7b3cda6 FOREIGN KEY (matrixrow_id) REFERENCES matrixrow(matrixrow_id);


--
-- Name: fkf19622dc3c572c3c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY analysis
    ADD CONSTRAINT fkf19622dc3c572c3c FOREIGN KEY (study_id) REFERENCES study(study_id);


--
-- Name: fkf7f2b6c8a414944f; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY typeset
    ADD CONSTRAINT fkf7f2b6c8a414944f FOREIGN KEY (matrix_id) REFERENCES matrix(matrix_id);


--
-- Name: fkfb7d7045e56a83; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY treegroup_phylotree
    ADD CONSTRAINT fkfb7d7045e56a83 FOREIGN KEY (treegroup_id) REFERENCES treegroup(treegroup_id);


--
-- Name: fkfb7d704b710cb23; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY treegroup_phylotree
    ADD CONSTRAINT fkfb7d704b710cb23 FOREIGN KEY (phylotree_id) REFERENCES phylotree(phylotree_id);


--
-- Name: matrixdatatype_phylochar_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY matrixdatatype
    ADD CONSTRAINT matrixdatatype_phylochar_id_fkey FOREIGN KEY (phylochar_id) REFERENCES phylochar(phylochar_id);


--
-- Name: rootnode_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY phylotree
    ADD CONSTRAINT rootnode_id_fk FOREIGN KEY (rootnode_id) REFERENCES phylotreenode(phylotreenode_id);


--
-- PostgreSQL database dump complete
--

