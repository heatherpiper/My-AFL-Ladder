-- ********************************************************************************
-- This script creates the database users and grants them the necessary permissions
-- ********************************************************************************

CREATE USER myfootyladder_owner
WITH PASSWORD 'myfootyladder';

GRANT ALL
ON ALL TABLES IN SCHEMA public
TO myfootyladder_owner;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO myfootyladder_owner;

CREATE USER myfootyladder_appuser
WITH PASSWORD 'myfootyladder';

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO myfootyladder_appuser;

GRANT USAGE, SELECT
ON ALL SEQUENCES IN SCHEMA public
TO myfootyladder_appuser;