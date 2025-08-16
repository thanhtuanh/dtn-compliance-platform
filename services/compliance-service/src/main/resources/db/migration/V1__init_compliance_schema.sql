-- V1__init_compliance_schema.sql
-- DTN Compliance Service - PostgreSQL Schema
-- DSGVO + EU AI Act konforme Datenbankstruktur

-- Extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Processing Activities (DSGVO Art. 30 VVT)
CREATE TABLE processing_activities (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    purpose TEXT NOT NULL,
    legal_basis VARCHAR(500) NOT NULL,
    data_categories TEXT[],
    data_subject_categories TEXT[],
    recipients TEXT[],
    third_country_transfer BOOLEAN DEFAULT FALSE,
    retention_period VARCHAR(255),
    technical_measures TEXT[],
    organizational_measures TEXT[],
    risk_level VARCHAR(50) DEFAULT 'niedrig',
    dsfa_required BOOLEAN DEFAULT FALSE,
    comments TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE INDEX idx_processing_activities_name ON processing_activities(name);
CREATE INDEX idx_processing_activities_risk ON processing_activities(risk_level);
CREATE INDEX idx_processing_activities_dsfa ON processing_activities(dsfa_required);

-- AI Systems (EU AI Act)
CREATE TABLE ai_systems (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    system_name VARCHAR(255) NOT NULL,
    system_type VARCHAR(255) NOT NULL,
    application_domain VARCHAR(255) NOT NULL,
    risk_level VARCHAR(50) NOT NULL,
    risk_score DECIMAL(3,2) DEFAULT 0.0,
    prohibited_practice BOOLEAN DEFAULT FALSE,
    ce_marking_required BOOLEAN DEFAULT FALSE,
    conformity_assessment_required BOOLEAN DEFAULT FALSE,
    transparency_obligations_required BOOLEAN DEFAULT FALSE,
    risk_factors TEXT[],
    compliance_measures TEXT[],
    estimated_affected_persons INTEGER DEFAULT 0,
    geographic_scope VARCHAR(50) DEFAULT 'NATIONAL',
    classified_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    validity_months INTEGER DEFAULT 12,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE INDEX idx_ai_systems_name ON ai_systems(system_name);
CREATE INDEX idx_ai_systems_risk ON ai_systems(risk_level);
CREATE INDEX idx_ai_systems_prohibited ON ai_systems(prohibited_practice);
CREATE INDEX idx_ai_systems_ce_marking ON ai_systems(ce_marking_required);

-- DSFA Assessments (DSGVO Art. 35)
CREATE TABLE dsfa_assessments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    processing_name VARCHAR(255) NOT NULL,
    processing_description TEXT,
    risk_score DECIMAL(3,2) NOT NULL,
    risk_level VARCHAR(50) NOT NULL,
    dsfa_required BOOLEAN NOT NULL,
    identified_risks TEXT[],
    recommended_measures TEXT[],
    compliance_status VARCHAR(255),
    ai_act_assessment_performed BOOLEAN DEFAULT FALSE,
    ai_risk_class VARCHAR(50),
    authority_consultation_required BOOLEAN DEFAULT FALSE,
    assessed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    next_review_months INTEGER DEFAULT 12,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE INDEX idx_dsfa_assessments_name ON dsfa_assessments(processing_name);
CREATE INDEX idx_dsfa_assessments_risk ON dsfa_assessments(risk_level);
CREATE INDEX idx_dsfa_assessments_required ON dsfa_assessments(dsfa_required);

-- Compliance Reports
CREATE TABLE compliance_reports (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    report_type VARCHAR(50) NOT NULL,
    format VARCHAR(20) NOT NULL,
    organization VARCHAR(255),
    overall_compliance_score DECIMAL(5,2),
    gdpr_status VARCHAR(50),
    ai_act_status VARCHAR(50),
    vvt_entries INTEGER DEFAULT 0,
    dsfa_assessments INTEGER DEFAULT 0,
    ai_systems INTEGER DEFAULT 0,
    critical_issues TEXT[],
    recommended_actions TEXT[],
    file_path VARCHAR(500),
    file_size_bytes BIGINT,
    generated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    valid_until TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255)
);

CREATE INDEX idx_compliance_reports_type ON compliance_reports(report_type);
CREATE INDEX idx_compliance_reports_org ON compliance_reports(organization);
CREATE INDEX idx_compliance_reports_generated ON compliance_reports(generated_at);

-- Audit Log (DSGVO-konform)
CREATE TABLE compliance_audit_log (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    entity_type VARCHAR(100) NOT NULL,
    entity_id UUID,
    action VARCHAR(50) NOT NULL,
    old_values JSONB,
    new_values JSONB,
    user_id VARCHAR(255),
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_log_entity ON compliance_audit_log(entity_type, entity_id);
CREATE INDEX idx_audit_log_action ON compliance_audit_log(action);
CREATE INDEX idx_audit_log_user ON compliance_audit_log(user_id);
CREATE INDEX idx_audit_log_created ON compliance_audit_log(created_at);

-- Business Metrics (für ROI-Berechnung)
CREATE TABLE business_metrics (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    metric_name VARCHAR(255) NOT NULL,
    metric_value DECIMAL(12,2),
    metric_unit VARCHAR(50),
    metric_description TEXT,
    calculation_date DATE DEFAULT CURRENT_DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_business_metrics_name ON business_metrics(metric_name);
CREATE INDEX idx_business_metrics_date ON business_metrics(calculation_date);

-- Demo Data für Bewerbungsgespräche
INSERT INTO processing_activities (
    name, purpose, legal_basis, data_categories, data_subject_categories, 
    recipients, third_country_transfer, retention_period, 
    technical_measures, organizational_measures, risk_level, dsfa_required, comments
) VALUES 
(
    'Mitarbeiterdatenverwaltung',
    'Personalverwaltung, Gehaltsabrechnung, Sozialversicherung',
    'Art. 6 Abs. 1 lit. b, c DSGVO (Vertrag, rechtliche Verpflichtung), § 26 BDSG',
    ARRAY['Stammdaten', 'Gehaltsdaten', 'Arbeitszeitdaten', 'Bewerbungsunterlagen'],
    ARRAY['Mitarbeiter', 'Bewerber', 'Praktikanten'],
    ARRAY['Lohnbuchhaltung', 'Sozialversicherungsträger', 'Finanzamt'],
    FALSE,
    '10 Jahre nach Beendigung des Arbeitsverhältnisses',
    ARRAY['AES-256 Verschlüsselung', 'Rollenbasierte Zugriffskontrolle', 'Audit-Logging'],
    ARRAY['Datenschutzschulung', 'Berechtigungskonzept', 'Incident Response Plan'],
    'niedrig',
    FALSE,
    'Standard HR-Verarbeitung nach deutschem Arbeitsrecht'
),
(
    'KI-basierte Kundensegmentierung',
    'Automatisierte Analyse von Kundendaten zur Segmentierung für personalisierte Marketing-Kampagnen',
    'Art. 6 Abs. 1 lit. a DSGVO (Einwilligung), Art. 6 Abs. 1 lit. f DSGVO (berechtigtes Interesse)',
    ARRAY['Kundendaten', 'Kaufverhalten', 'Demografische Daten', 'Präferenzen'],
    ARRAY['Kunden', 'Interessenten', 'Website-Besucher'],
    ARRAY['Lokale KI-Systeme (Ollama)', 'Marketing-Team', 'Analytics-Platform'],
    FALSE,
    '2 Jahre für ML-Trainingsdaten, 6 Monate für Inferenz-Logs',
    ARRAY['Lokale KI-Verarbeitung', 'Pseudonymisierung', 'Datenminimierung'],
    ARRAY['AI Ethics Guidelines', 'Algorithmic Bias Monitoring', 'Human-in-the-Loop'],
    'mittel',
    TRUE,
    'EU AI Act Compliance erforderlich - Risikoklassifizierung durchführen'
);

INSERT INTO ai_systems (
    system_name, system_type, application_domain, risk_level, risk_score,
    prohibited_practice, ce_marking_required, conformity_assessment_required,
    transparency_obligations_required, risk_factors, compliance_measures,
    estimated_affected_persons, geographic_scope
) VALUES 
(
    'E-Commerce Recommendation Engine',
    'Recommendation System',
    'E-Commerce',
    'LIMITED_RISK',
    0.3,
    FALSE,
    FALSE,
    FALSE,
    TRUE,
    ARRAY['Automatisierte Produktempfehlungen', 'Personalisierung basierend auf Nutzerverhalten'],
    ARRAY['Nutzer über KI-System informieren', 'Algorithmic Decision Making transparent machen'],
    50000,
    'EU'
);

INSERT INTO business_metrics (metric_name, metric_value, metric_unit, metric_description) VALUES
('VVT_Automation_Savings', 45000, 'EUR', 'Jährliche Kostenersparnis durch VVT-Automatisierung'),
('DSFA_Automation_Savings', 30720, 'EUR', 'Jährliche Kostenersparnis durch DSFA-Automatisierung'),
('AI_Act_Compliance_Savings', 21000, 'EUR', 'Jährliche Kostenersparnis durch AI Risk Assessment'),
('Time_Efficiency_Gain', 87.5, 'Percent', 'Durchschnittliche Zeitersparnis durch Automatisierung'),
('Overall_ROI', 340, 'Percent', 'Return on Investment im ersten Jahr');

-- Trigger für updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_processing_activities_updated_at BEFORE UPDATE ON processing_activities FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_ai_systems_updated_at BEFORE UPDATE ON ai_systems FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_dsfa_assessments_updated_at BEFORE UPDATE ON dsfa_assessments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();