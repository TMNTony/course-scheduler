-- **************************************************************
-- This script destroys the database and associated users
-- **************************************************************

-- The following line terminates any active connections to the database so that it can be destroyed
SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = 'course_scheduler';

DROP DATABASE course_scheduler;

DROP USER course_scheduler_owner;
DROP USER course_scheduler_appuser;
