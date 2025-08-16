-- DTN Compliance Platform - Gateway Database Initialization
-- DSGVO + EU AI Act konforme KI-Lösung für deutsche Unternehmen

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

-- Extensions für erweiterte Funktionalität
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Schema für Compliance-konforme Datenorganisation
CREATE SCHEMA IF NOT EXISTS gateway;
CREATE SCHEMA IF NOT EXISTS audit;

-- User Management Tables (DSGVO-konform)
CREATE TABLE IF NOT EXISTS gateway.users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    company_name VARCHAR(255),
    role VARCHAR(50) DEFAULT 'USER',
    is_active BOOLEAN DEFAULT true,
    gdpr_consent BOOLEAN DEFAULT false,
    gdpr_consent_date TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP WITH TIME ZONE
);

-- Indexes für Performance
CREATE INDEX IF NOT EXISTS idx_users_username ON gateway.users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON gateway.users(email);
CREATE INDEX IF NOT EXISTS idx_users_active ON gateway.users(is_active);

-- Demo-User für Bewerbungsgespräche
INSERT INTO gateway.users (
    username, 
    email, 
    password_hash, 
    first_name, 
    last_name, 
    company_name, 
    role,
    gdpr_consent,
    gdpr_consent_date
) VALUES 
(
    'demo-user',
    'demo@dtn-compliance.de',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LlVGlHj0QG9qJ9ZpG',
    'Demo',
    'User',
    'DTN Software GmbH',
    'DEMO_USER',
    true,
    CURRENT_TIMESTAMP
)
ON CONFLICT (username) DO NOTHING;

-- Grants für dtn_user
GRANT USAGE ON SCHEMA gateway TO dtn_user;
GRANT USAGE ON SCHEMA audit TO dtn_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA gateway TO dtn_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA gateway TO dtn_user;
