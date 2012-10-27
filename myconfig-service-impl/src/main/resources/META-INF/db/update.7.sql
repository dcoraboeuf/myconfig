INSERT INTO ENVGRANTS (USER, APPLICATION, ENVIRONMENT, GRANTEDFUNCTION)
SELECT USER, APPLICATION, ENVIRONMENT, 'env_users' FROM ENVGRANTS WHERE GRANTEDFUNCTION = 'env_config';

-- @rollback
DELETE FROM ENVGRANTS WHERE GRANTEDFUNCTION = 'env_users';
