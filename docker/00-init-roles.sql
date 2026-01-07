-- Create postgres role if it doesn't exist
-- This is needed because the schema SQL files reference 'postgres' as the owner
-- but our Docker setup uses 'treebase' as the main user

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'postgres') THEN
    CREATE ROLE postgres WITH LOGIN SUPERUSER;
  END IF;
END
$$;

-- Create treebase_app role if it doesn't exist
-- This is needed because the schema SQL files grant privileges to this role

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'treebase_app') THEN
    CREATE ROLE treebase_app WITH LOGIN;
  END IF;
END
$$;

-- Grant necessary privileges to ensure treebase user can work with postgres-owned objects
GRANT postgres TO treebase;
GRANT treebase_app TO treebase;
