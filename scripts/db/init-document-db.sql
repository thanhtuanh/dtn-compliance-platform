-- DTN Document Service Database
SET client_encoding = 'UTF8';
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE SCHEMA IF NOT EXISTS document;
GRANT USAGE ON SCHEMA document TO dtn_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA document TO dtn_user;
