-- Migration script to add hibernate_sequence if it doesn't exist
-- This sequence is required by Hibernate's @CollectionId annotation
-- for generating collection_id values in sub_matrix and sub_treeblock tables

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_catalog.pg_sequences WHERE sequencename = 'hibernate_sequence') THEN
    CREATE SEQUENCE hibernate_sequence;
    RAISE NOTICE 'Created hibernate_sequence';
  ELSE
    RAISE NOTICE 'hibernate_sequence already exists';
  END IF;
END
$$;
