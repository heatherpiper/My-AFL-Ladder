-- ********************************************************************************
-- This script creates the database users and grants them the necessary permissions
-- ********************************************************************************

CREATE USER match_owner
WITH PASSWORD 'finalcapstone';

GRANT ALL
ON ALL TABLES IN SCHEMA public
TO match_owner;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO match_owner;

CREATE USER match_appuser
WITH PASSWORD 'match-piper';

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO match_appuser;

GRANT USAGE, SELECT
ON ALL SEQUENCES IN SCHEMA public
TO match_appuser;