-- Typing of keys
ALTER TABLE APPKEY ADD COLUMN TYPEID VARCHAR(16);
ALTER TABLE APPKEY ADD COLUMN TYPEPARAM VARCHAR(500);