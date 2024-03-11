-- ********************************************************************************
-- This script creates the database users and grants them the necessary permissions
-- ********************************************************************************

CREATE USER laterladder_owner
WITH PASSWORD 'laterladder';

GRANT ALL
ON ALL TABLES IN SCHEMA public
TO laterladder_owner;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO laterladder_owner;

CREATE USER laterladder_appuser
WITH PASSWORD 'laterladder';

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO laterladder_appuser;

GRANT USAGE, SELECT
ON ALL SEQUENCES IN SCHEMA public
TO laterladder_appuser;