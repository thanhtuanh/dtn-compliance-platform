-- DTN Compliance Service Database
SET client_encoding = 'UTF8';
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE SCHEMA IF NOT EXISTS compliance;
GRANT USAGE ON SCHEMA compliance TO dtn_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA compliance TO dtn_user;
