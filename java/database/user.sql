-- ********************************************************************************
-- This script creates the database users and grants them the necessary permissions
-- ********************************************************************************

CREATE USER owner
WITH PASSWORD 'password';

GRANT ALL
ON ALL TABLES IN SCHEMA public
TO owner;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO owner;

CREATE USER appuser
WITH PASSWORD 'password';

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO appuser;

GRANT USAGE, SELECT
ON ALL SEQUENCES IN SCHEMA public
TO appuser;
