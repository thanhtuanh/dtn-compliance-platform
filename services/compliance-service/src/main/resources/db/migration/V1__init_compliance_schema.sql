-- DTN Compliance Service - Initial Schema
-- DSGVO + EU AI Act Database Schema

-- Processing Activities Table (VVT - Verzeichnis der Verarbeitungstätigkeiten)
CREATE TABLE processing_activities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    purpose TEXT NOT NULL,
    data_subject_categories TEXT[], -- Array of categories
    data_categories TEXT[], -- Array of data types
    recipients TEXT[], -- Array of recipients
    third_country_transfer BOOLEAN DEFAULT false,
    retention_period VARCHAR(500),
    legal_basis VARCHAR(500) NOT NULL,
    technical_measures TEXT[],
    organizational_measures TEXT[],
    risk_level VARCHAR(20) DEFAULT 'niedrig',
    dsfa_required BOOLEAN DEFAULT false,
    comments TEXT,
    company_name VARCHAR(200),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- DSFA Assessments Table (Datenschutz-Folgenabschätzung)
CREATE TABLE dsfa_assessments (
    id BIGSERIAL PRIMARY KEY,
    processing_name VARCHAR(200) NOT NULL,
    processing_description TEXT,
    data_types TEXT[],
    purposes TEXT[],
    technologies TEXT[],
    data_subjects TEXT[],
    estimated_data_subjects INTEGER DEFAULT 1000,
    processing_duration_months INTEGER DEFAULT 12,
    special_categories BOOLEAN DEFAULT false,
    third_country_transfer BOOLEAN DEFAULT false,
    automated_decision_making BOOLEAN DEFAULT false,
    systematic_monitoring BOOLEAN DEFAULT false,
    vulnerable_groups BOOLEAN DEFAULT false,
    large_scale BOOLEAN DEFAULT false,
    data_matching BOOLEAN DEFAULT false,
    innovative_technology BOOLEAN DEFAULT false,
    prevents_rights_exercise BOOLEAN DEFAULT false,
    risk_score DECIMAL(3,2),
    risk_level VARCHAR(20),
    dsfa_required BOOLEAN,
    compliance_status VARCHAR(100),
    additional_info TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- AI Risk Classifications Table (EU AI Act)
CREATE TABLE ai_risk_classifications (
    id BIGSERIAL PRIMARY KEY,
    system_name VARCHAR(200) NOT NULL,
    system_type VARCHAR(100),
    application_domain VARCHAR(100),
    system_description TEXT,
    data_types TEXT[],
    estimated_affected_persons INTEGER DEFAULT 1000,
    geographic_scope VARCHAR(20) DEFAULT 'NATIONAL',
    user_interaction BOOLEAN DEFAULT false,
    automated_decision_making BOOLEAN DEFAULT false,
    biometric_data BOOLEAN DEFAULT false,
    emotion_recognition BOOLEAN DEFAULT false,
    critical_infrastructure BOOLEAN DEFAULT false,
    education_context BOOLEAN DEFAULT false,
    employment_context BOOLEAN DEFAULT false,
    essential_services BOOLEAN DEFAULT false,
    law_enforcement BOOLEAN DEFAULT false,
    migration_asylum_border BOOLEAN DEFAULT false,
    justice_and_democracy BOOLEAN DEFAULT false,
    credit_scoring BOOLEAN DEFAULT false,
    insurance_risk_assessment BOOLEAN DEFAULT false,
    emergency_services BOOLEAN DEFAULT false,
    safety_components BOOLEAN DEFAULT false,
    minors_data BOOLEAN DEFAULT false,
    public_spaces BOOLEAN DEFAULT false,
    large_scale BOOLEAN DEFAULT false,
    risk_level VARCHAR(30),
    risk_score DECIMAL(3,2),
    prohibited_practice BOOLEAN DEFAULT false,
    ce_marking_required BOOLEAN DEFAULT false,
    conformity_assessment_required BOOLEAN DEFAULT false,
    transparency_obligations_required BOOLEAN DEFAULT false,
    additional_info TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- VVT Generation Logs
CREATE TABLE vvt_generations (
    id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(200) NOT NULL,
    industry VARCHAR(100),
    employee_count INTEGER,
    total_activities INTEGER,
    compliance_score DECIMAL(5,2),
    processing_time_ms BIGINT,
    success BOOLEAN DEFAULT true,
    error_message TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Compliance Reports
CREATE TABLE compliance_reports (
    id BIGSERIAL PRIMARY KEY,
    organization VARCHAR(200),
    report_type VARCHAR(50),
    format VARCHAR(10),
    overall_compliance_score DECIMAL(5,2),
    gdpr_status VARCHAR(50),
    ai_act_status VARCHAR(50),
    vvt_entries INTEGER DEFAULT 0,
    dsfa_assessments INTEGER DEFAULT 0,
    ai_systems INTEGER DEFAULT 0,
    critical_issues TEXT[],
    file_path VARCHAR(500),
    file_size_bytes BIGINT,
    processing_time_ms BIGINT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    valid_until TIMESTAMP WITH TIME ZONE
);

-- Indexes for better performance
CREATE INDEX idx_processing_activities_company ON processing_activities(company_name);
CREATE INDEX idx_processing_activities_risk ON processing_activities(risk_level);
CREATE INDEX idx_dsfa_assessments_risk ON dsfa_assessments(risk_level);
CREATE INDEX idx_dsfa_assessments_required ON dsfa_assessments(dsfa_required);
CREATE INDEX idx_ai_risk_level ON ai_risk_classifications(risk_level);
CREATE INDEX idx_ai_prohibited ON ai_risk_classifications(prohibited_practice);
CREATE INDEX idx_vvt_generations_company ON vvt_generations(company_name);
CREATE INDEX idx_compliance_reports_org ON compliance_reports(organization);
CREATE INDEX idx_compliance_reports_type ON compliance_reports(report_type);

-- Comments for documentation
COMMENT ON TABLE processing_activities IS 'VVT (Verzeichnis der Verarbeitungstätigkeiten) nach DSGVO Art. 30';
COMMENT ON TABLE dsfa_assessments IS 'DSFA (Datenschutz-Folgenabschätzung) nach DSGVO Art. 35';
COMMENT ON TABLE ai_risk_classifications IS 'KI-Risikoklassifizierung nach EU AI Act';
COMMENT ON TABLE vvt_generations IS 'Log der VVT-Generierungen für Business Metrics';
COMMENT ON TABLE compliance_reports IS 'Generierte Compliance-Reports (DSGVO + EU AI Act)';