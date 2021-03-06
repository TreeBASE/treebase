delete from citationStatus;
insert into citationStatus(citationstatus_id, version, description) values 
(1, 1, 'In Prep'),
(2, 1, 'In Review'),
(3, 1, 'Accepted with Minor Changes'),
(4, 1, 'In Press'),
(5, 1, 'Published');
--alter table citationstatus alter column citationstatus_id restart with 6;
commit;

delete from gapMode;
insert into gapMode(gapMode_id, VERSION, Description) values 
(1, 1, 'Missing'),
(2, 1, 'NewState');
--alter table gapMode alter column gapMode_id restart with 3;
commit;

delete from itemDefinition;
insert into itemDefinition(itemDefinition_id, VERSION, Description) values 
(1, 1, 'Min'),
(2, 1, 'Max'),
(3, 1, 'Median'),
(4, 1, 'Avg'),
(5, 1, 'Variance'),
(6, 1, 'StdError'),
(7, 1, 'SampleSize'),
(8, 1, 'States');

alter table itemDefinition alter column itemDefinition_id restart with 9;
commit;

delete from PolyTCount;
insert into PolyTCount(polyTCount_id, VERSION, Description) values 
(1, 1, 'MinSteps'),
(2, 1, 'MaxSteps');
--alter table polyTCount alter column polyTCount_id restart with 3;
commit;

delete from stateFormat;
insert into stateFormat(stateFormat_id, VERSION, Description) values 
(1, 1, 'Count'),
(2, 1, 'Frequency'), 
(3, 1, 'StatePresent'),
(4, 1, 'Individuals');
--alter table stateformat alter column stateformat_id restart with 5;
commit;

delete from treekind;
INSERT INTO TREEKIND(TREEKIND_ID, VERSION, DESCRIPTION) values 
(1, 1, 'Species Tree'),
(2, 1, 'Gene Tree'),
(3, 1, 'Language Tree'),
(4, 1, 'Area Tree'),
(5, 1, 'Barcode Tree'),
(6, 1, 'Object Classification Tree');
alter table treekind alter column treekind_id restart with 7;
commit;

delete from treetype;
INSERT INTO TREETYPE(TREETYPE_ID, VERSION, DESCRIPTION) values 
(1, 1, 'Single'),
(2, 1, 'Consensus'),
(3, 1, 'SuperTree');
alter table treetype alter column treetype_id restart with 4;
commit;

delete from treequality;
INSERT INTO TREEQUALITY(TREEQUALITY_ID, VERSION, DESCRIPTION) values 
(1, 1, 'Unrated'),
(2, 1, 'Preferred Tree'),
(3, 1, 'Alternative Tree'),
(4, 1, 'Suboptimal Tree');
alter table treequality alter column treequality_id restart with 5;
commit;

delete from matrixkind;
INSERT INTO MATRIXKIND(MATRIXKIND_ID, VERSION, DESCRIPTION) values 
(1, 1, 'Unspecified'),
(2, 1, 'Allozyme'),
(3, 1, 'Amino Acid'),
(4, 1, 'Behavior'),
(5, 1, 'Combination'),
(6, 1, 'Karyotype'),
(7, 1, 'Matrix Representation'),
(8, 1, 'Morphological'),
(9, 1, 'Nucleic Acid'),
(10, 1, 'Restriction Site'),
(11, 1, 'Secondary Chemistry');
alter table matrixkind alter column matrixkind_id restart with 12;
commit;

-- DiscreteCharState and MatrixDataType depend on PhyloChar, so we must handle them together
--
-- If you update the PHYLOCHAR section, be sure to adjust
-- org.cipres.treebase.Constants.MIN_INTERESTING_PHYLOCHAR_ID 
-- also.  20090317 MJD
--
-- BEGIN DiscreteCharState, MatrixDataTpe, and PhyloChar
delete from DiscreteCharState;
delete from MatrixDataType;
delete from phylochar;
insert into phylochar (TYPE, PHYLOCHAR_ID, VERSION, DESCRIPTION) values
('D', 1, 1, 'Standard'),
('D', 2, 1, 'DNA'),
('D', 3, 1, 'RNA'),
('D', 4, 1, 'Nucleotide'),
('D', 5, 1, 'Protein');
--('C', 6, 1, 'Continuous');
-- 1:Standard, 2:DNA, 3:RNA, 4:Nucleotide, 5:Protein, 6:Continuous


alter table phylochar alter column phylochar_id restart with 7;
commit;

insert into MatrixDataType (MatrixDataType_ID, Version, PhyloChar_ID, Description) values
(1, 1, null, 'Standard'),
(2, 1, 2, 'DNA'),
(3, 1, 3, 'RNA'),
(4, 1, 4, 'Nucleotide'),
(5, 1, 5, 'Protein'),
(6, 1, null, 'Continuous'),
(7, 1, null, 'Distance');
alter table MatrixDataType alter column MatrixDataType_id restart with 10;
commit;

-- DNA symbols
insert into DiscreteCharState (DiscreteCharState_ID, Version, Description, PhyloChar_ID, StateSet_ID, AncestralState_ID) values
(1, 1, 'A', 2, null, null),
(2, 1, 'C', 2, null, null),
(3, 1, 'G', 2, null, null),
(4, 1, 'T', 2, null, null),
(5, 1, '?', 2, null, null),
(6, 1, 'R', 2, null, null),
(7, 1, 'Y', 2, null, null),
(8, 1, 'M', 2, null, null),
(9, 1, 'K', 2, null, null),
(10, 1, 'S', 2, null, null),
(11, 1, 'W', 2, null, null),
(12, 1, 'H', 2, null, null),
(13, 1, 'B', 2, null, null),
(14, 1, 'V', 2, null, null),
(15, 1, 'D', 2, null, null),
(16, 1, 'N', 2, null, null);
commit;

-- RNA symbols
insert into DiscreteCharState (DiscreteCharState_ID, Version, Description, PhyloChar_ID, StateSet_ID, AncestralState_ID) values
(21, 1, 'A', 3, null, null),
(22, 1, 'C', 3, null, null),
(23, 1, 'G', 3, null, null),
(24, 1, 'U', 3, null, null),
(25, 1, '?', 3, null, null),
(26, 1, 'R', 3, null, null),
(27, 1, 'Y', 3, null, null),
(28, 1, 'M', 3, null, null),
(29, 1, 'K', 3, null, null),
(30, 1, 'S', 3, null, null),
(31, 1, 'W', 3, null, null),
(32, 1, 'H', 3, null, null),
(33, 1, 'B', 3, null, null),
(34, 1, 'V', 3, null, null),
(35, 1, 'D', 3, null, null),
(36, 1, 'N', 3, null, null);
commit;

-- Nucleotide symbols
insert into DiscreteCharState (DiscreteCharState_ID, Version, Description, PhyloChar_ID, StateSet_ID, AncestralState_ID) values
(41, 1, 'A', 4, null, null),
(42, 1, 'C', 4, null, null),
(43, 1, 'G', 4, null, null),
(44, 1, 'T', 4, null, null),
(45, 1, 'U', 4, null, null),
(46, 1, '?', 4, null, null),
(47, 1, 'R', 4, null, null),
(48, 1, 'Y', 4, null, null),
(49, 1, 'M', 4, null, null),
(50, 1, 'K', 4, null, null),
(51, 1, 'S', 4, null, null),
(52, 1, 'W', 4, null, null),
(53, 1, 'H', 4, null, null),
(54, 1, 'B', 4, null, null),
(55, 1, 'V', 4, null, null),
(56, 1, 'D', 4, null, null),
(57, 1, 'N', 4, null, null);
commit;

-- protein symbols
insert into DiscreteCharState (DiscreteCharState_ID, Version, Description, PhyloChar_ID, StateSet_ID, AncestralState_ID) values
(81, 1, 'A', 5, null, null),
(82, 1, 'C', 5, null, null),
(83, 1, 'D', 5, null, null),
(84, 1, 'E', 5, null, null),
(85, 1, 'F', 5, null, null),
(86, 1, 'G', 5, null, null),
(87, 1, 'H', 5, null, null),
(88, 1, 'I', 5, null, null),
(89, 1, 'K', 5, null, null),
(90, 1, 'L', 5, null, null),
(91, 1, 'M', 5, null, null),
(92, 1, 'N', 5, null, null),
(93, 1, 'P', 5, null, null),
(94, 1, 'Q', 5, null, null),
(95, 1, 'R', 5, null, null),
(96, 1, 'S', 5, null, null),
(97, 1, 'T', 5, null, null),
(98, 1, 'V', 5, null, null),
(99, 1, 'W', 5, null, null),
(100, 1, 'Y', 5, null, null),
(101, 1, '*', 5, null, null),
(102, 1, '?', 5, null, null),
(103, 1, 'X', 5, null, null),
(104, 1, 'B', 5, null, null),
(105, 1, 'Z', 5, null, null);

alter table discretecharstate alter column Discretecharstate_id restart with 120;
commit;

UPDATE DISCRETECHARSTATE SET SYMBOL=DESCRIPTION
    WHERE length(DESCRIPTION) = 1;
commit;
-- END DiscreteCharState, MatrixDataTpe, and PhyloChar

-- Submission depends on User, which depends on Person, which depends
-- on userRole, so we handle them together
-- BEGIN Person, Submission, User, UserRole
-- Add base users and people
delete from submission;
delete from user;
delete from person;
delete from userRole;
insert into userRole(userRole_id, VERSION, AUTHORITY) values 
	(1, 1, 'Admin'),
	(2, 1, 'User'),
	(3, 1, 'Associate Editor');

alter table userrole alter column userrole_id restart with 5;

INSERT INTO PERSON(PERSON_ID, VERSION, LASTNAME, FIRSTNAME, MNAME, PHONE, EMAIL, AUTHORID) 
    VALUES
    (0, 0, 'TESTUSER','TIMOTHY', 'T.',    '',                    'william.piel@yale.edu', ''),
    (1, 0, 'Piel',    'William', 'H.',    '+1 203 716 907 3538', 'william.piel@yale.edu', ''),
    (2, 0, 'Dominus', 'Mark',    'Jason', '+1 215 573 5387',     'mjd@genomics.upenn.edu', ''),
    (3, 0, 'Tannen',  'Val',     '',      '',                    'val@cis.upenn.edu', ''),
    (4, 0, 'Vos',     'Rutger',  'A.',    '',                    'rutgeraldo@gmail.com', '');
    (5, 0, 'TB 1 importation suite', '',  '',    '',  '', '');
alter table person alter column person_id restart with 6;

INSERT INTO USER(USER_ID, VERSION, USERNAME, PASSWORD, USERROLE_ID, PERSON_ID) 
    VALUES
    (1,  0, 'piel', '*', 2, 1),
    (2,  0, 'mjd',  '*',       1, 2),
    (3,  0, 'val',  '*',       2, 3),
    (4,  0, 'rvosa','*', 1, 4),
    (5,  0, 'tb1importer','*',   3, 5),
    (11, 0, 't1',  '*',         1, 0),
    (12, 0, 't2',  '*',         2, 0),
    (13, 0, 't3',  '*',         3, 0);
alter table user alter column user_id restart with 100;

commit;
-- END Person, User, UserRole


-- Study depends on StudyStatus, so we handle them together
-- BEGIN Study, StudyStatus, Submission
delete from study;
delete from studyStatus;
insert into studyStatus(studystatus_id, version, description) values 
(1, 1, 'In Progress'),
(2, 1, 'Ready'),
(3, 1, 'Published');
--alter table studystatus alter column studystatus_id restart with 4;
commit;

-- Create initial submission amd study
INSERT INTO STUDY(STUDY_ID, VERSION, NOTES, ACCESSIONNUMBER, RELEASEDATE, NAME, CITATION_ID, STUDYSTATUS_ID, TB_STUDYID) 
    VALUES(1, 0, 'Mesquite parser test only', '1', current date, 'MesquiteTestStudy', null, 1, null);
INSERT INTO SUBMISSION(SUBMISSION_ID, VERSION, SUBMISSIONNUMBER, CREATEDATE, STUDY_ID, USER_ID, TEST) 
    VALUES(1, 0, '1', current date, 1, 1, 1);

alter table submission alter column submission_id restart with 2;
alter table study alter column study_id restart with 2;
commit;
-- END Study, StudyStatus, Submission

-- Add indices
--CREATE INDEX "TREENODE_TREE_IDX"
--	ON "PHYLOTREENODE"("PHYLOTREE_ID")
--	CLUSTER 
--	PCTFREE 10
--	MINPCTUSED 10;
--commit;
