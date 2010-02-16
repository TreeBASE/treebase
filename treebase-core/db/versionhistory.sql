-- Table for tracking which schema patches have been applied to the current DB instance. 
-- This table is already in the snapshot 0000_SCHEMA_before_patches_start.sql 

CREATE TABLE versionhistory
(
  id serial NOT NULL,
  patchnumber integer,
  patchlabel character varying(63) NOT NULL,
  patchdescription character varying(1023),
  applied timestamp (0) without time zone DEFAULT now(),
  CONSTRAINT versionhistory_pkey PRIMARY KEY (id),
  CONSTRAINT versionhistory_unique_patchnumber UNIQUE (patchnumber)
); 

COMMENT ON TABLE versionhistory IS 'VersionHistory table is NOT a part of the TreeBase application. It is only used within the development and deployment process to keep track of schema patches applied to a DB instance. ';
