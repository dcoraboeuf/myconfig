-- User email must be unique
ALTER TABLE USERS ADD CONSTRAINT UQ_USERS_EMAIL UNIQUE (EMAIL);

-- @rollback
ALTER TABLE USERS DROP CONSTRAINT IF EXISTS UQ_USERS_EMAIL;

-- @mysql-rollback
-- ALTER TABLE USERS DROP CONSTRAINT UQ_USERS_EMAIL;
