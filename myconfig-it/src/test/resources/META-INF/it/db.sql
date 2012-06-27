-- Clean-up
DELETE FROM APPLICATION;

-- Data
INSERT INTO APPLICATION (ID, NAME) VALUES (1, 'myapp');

INSERT INTO VERSION (APPLICATION, NAME) VALUES (1, '1.0');
INSERT INTO VERSION (APPLICATION, NAME) VALUES (1, '1.1');
INSERT INTO VERSION (APPLICATION, NAME) VALUES (1, '1.2');

INSERT INTO ENVIRONMENT (APPLICATION, NAME) VALUES (1, 'DEV');
INSERT INTO ENVIRONMENT (APPLICATION, NAME) VALUES (1, 'ACC');
INSERT INTO ENVIRONMENT (APPLICATION, NAME) VALUES (1, 'UAT');
INSERT INTO ENVIRONMENT (APPLICATION, NAME) VALUES (1, 'PROD');

INSERT INTO APPKEY (APPLICATION, NAME, DESCRIPTION) VALUES (1, 'jdbc.user', 'User used to connect to the database');
INSERT INTO APPKEY (APPLICATION, NAME, DESCRIPTION) VALUES (1, 'jdbc.password', 'Password used to connect to the database');

INSERT INTO VERSION_KEY (APPLICATION, VERSION, KEY) VALUES (1, '1.0', 'jdbc.user');
INSERT INTO VERSION_KEY (APPLICATION, VERSION, KEY) VALUES (1, '1.1', 'jdbc.user');
INSERT INTO VERSION_KEY (APPLICATION, VERSION, KEY) VALUES (1, '1.2', 'jdbc.user');
INSERT INTO VERSION_KEY (APPLICATION, VERSION, KEY) VALUES (1, '1.0', 'jdbc.password');
INSERT INTO VERSION_KEY (APPLICATION, VERSION, KEY) VALUES (1, '1.1', 'jdbc.password');
INSERT INTO VERSION_KEY (APPLICATION, VERSION, KEY) VALUES (1, '1.2', 'jdbc.password');

INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.0', 'jdbc.user', 'DEV', '1.0 DEV jdbc.user');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.0', 'jdbc.user', 'ACC', '1.0 ACC jdbc.user');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.0', 'jdbc.user', 'UAT', '1.0 UAT jdbc.user');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.0', 'jdbc.user', 'PROD', '1.0 PROD jdbc.user');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.1', 'jdbc.user', 'DEV', '1.1 DEV jdbc.user');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.1', 'jdbc.user', 'ACC', '1.1 ACC jdbc.user');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.1', 'jdbc.user', 'UAT', '1.1 UAT jdbc.user');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.1', 'jdbc.user', 'PROD', '1.1 PROD jdbc.user');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.2', 'jdbc.user', 'DEV', '1.2 DEV jdbc.user');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.2', 'jdbc.user', 'ACC', '1.2 ACC jdbc.user');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.2', 'jdbc.user', 'UAT', '1.2 UAT jdbc.user');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.2', 'jdbc.user', 'PROD', '1.2 PROD jdbc.user');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.0', 'jdbc.password', 'DEV', '1.0 DEV jdbc.password');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.0', 'jdbc.password', 'ACC', '1.0 ACC jdbc.password');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.0', 'jdbc.password', 'UAT', '1.0 UAT jdbc.password');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.0', 'jdbc.password', 'PROD', '1.0 PROD jdbc.password');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.1', 'jdbc.password', 'DEV', '1.1 DEV jdbc.password');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.1', 'jdbc.password', 'ACC', '1.1 ACC jdbc.password');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.1', 'jdbc.password', 'UAT', '1.1 UAT jdbc.password');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.1', 'jdbc.password', 'PROD', '1.1 PROD jdbc.password');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.2', 'jdbc.password', 'DEV', '1.2 DEV jdbc.password');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.2', 'jdbc.password', 'ACC', '1.2 ACC jdbc.password');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.2', 'jdbc.password', 'UAT', '1.2 UAT jdbc.password');
INSERT INTO CONFIG (APPLICATION, VERSION, KEY, ENVIRONMENT, VALUE) VALUES (1, '1.2', 'jdbc.password', 'PROD', '1.2 PROD jdbc.password');
