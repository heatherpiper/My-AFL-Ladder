-- **************************************************************
-- This script destroys the database and associated users
-- **************************************************************

-- The following line terminates any active connections to the database so that it can be destroyed
SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = 'my_footy_ladder';

DROP DATABASE my_footy_ladder;

DROP USER myfootyladder_owner;
DROP USER myfootyladder_appuser;
