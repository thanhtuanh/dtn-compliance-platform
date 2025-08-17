-- DTN Compliance Service - Demo Data for Presentations
-- Seed data for demonstrating DSGVO + EU AI Act compliance features

-- Demo Processing Activities (VVT)
INSERT INTO processing_activities (
    name, purpose, data_subject_categories, data_categories, recipients,
    third_country_transfer, retention_period, legal_basis,
    technical_measures, organizational_measures, risk_level,
    dsfa_required, comments, company_name
) VALUES 
(
    'Mitarbeiterdatenverwaltung',
    'Personalverwaltung, Gehaltsabrechnung, Sozialversicherung',
    ARRAY['Mitarbeiter', 'Bewerber', 'Praktikanten'],
    ARRAY['Stammdaten', 'Gehaltsdaten', 'Arbeitszeitdaten'],
    ARRAY['Lohnbuchhaltung', 'Sozialversicherungsträger', 'Finanzamt'],
    false,
    '10 Jahre nach Beendigung des Arbeitsverhältnisses',
    'Art. 6 Abs. 1 lit. b, c DSGVO (Vertrag, rechtliche Verpflichtung)',
    ARRAY['AES-256 Verschlüsselung', 'Rollenbasierte Zugriffskontrolle', 'Audit-Logging'],
    ARRAY['Datenschutzschulung', 'Berechtigungskonzept', 'Incident Response Plan'],
    'niedrig',
    false,
    'Standard HR-Verarbeitung nach deutschem Arbeitsrecht',
    'Mustermann Software GmbH'
),
(
    'KI-basierte Kundensegmentierung',
    'Marketing-Optimierung, Personalisierung, Kundenanalyse',
    ARRAY['Kunden', 'Interessenten', 'Website-Besucher'],
    ARRAY['Nutzungsdaten', 'Verhaltensdaten', 'Präferenz-Daten'],
    ARRAY['Lokale KI-Systeme (Ollama)', 'Analytics-Plattform'],
    false,
    '2 Jahre für ML-Trainingsdaten, 6 Monate für Inferenz-Logs',
    'Art. 6 Abs. 1 lit. a DSGVO (Einwilligung), Art. 6 Abs. 1 lit. f DSGVO (berechtigtes Interesse)',
    ARRAY['Lokale KI-Verarbeitung (Privacy by Design)', 'Datenminimierung', 'Pseudonymisierung'],
    ARRAY['AI Ethics Guidelines', 'Algorithmic Bias Monitoring', 'Human-in-the-Loop Oversight'],
    'mittel',
    true,
    'EU AI Act Compliance erforderlich - Risikoklassifizierung durchführen',
    'Mustermann Software GmbH'
),
(
    'Website-Betrieb und Online-Marketing',
    'Unternehmensdarstellung, Lead-Generierung, Newsletter-Versand',
    ARRAY['Website-Besucher', 'Newsletter-Abonnenten', 'Interessenten'],
    ARRAY['IP-Adresse', 'Browser-Daten', 'E-Mail-Adresse', 'Nutzungsverhalten'],
    ARRAY['Hosting-Provider', 'Analytics-Provider', 'Newsletter-Service'],
    true,
    '2 Jahre für Analytics-Daten, bis Widerruf für Newsletter',
    'Art. 6 Abs. 1 lit. a DSGVO (Einwilligung), Art. 6 Abs. 1 lit. f DSGVO (berechtigtes Interesse)',
    ARRAY['Cookie-Banner mit Consent Management', 'IP-Anonymisierung', 'TLS-Verschlüsselung'],
    ARRAY['Datenschutzerklärung', 'Cookie-Policy', 'Einwilligungsmanagement'],
    'niedrig',
    false,
    'Drittlandübermittlung: Google Analytics - Angemessenheitsbeschluss prüfen',
    'Mustermann Software GmbH'
);

-- Demo DSFA Assessments
INSERT INTO dsfa_assessments (
    processing_name, processing_description, data_types, purposes, technologies,
    data_subjects, estimated_data_subjects, special_categories, automated_decision_making,
    large_scale, data_matching, innovative_technology, risk_score, risk_level,
    dsfa_required, compliance_status
) VALUES 
(
    'KI-basierte Kundensegmentierung',
    'Machine Learning basiertes Empfehlungssystem für Produktvorschläge',
    ARRAY['Kaufverhalten', 'Präferenzen', 'Demografische Daten'],
    ARRAY['Marketing-Optimierung', 'Personalisierung', 'Kundenanalyse'],
    ARRAY['Machine Learning', 'Datenanalyse', 'Analytics-Platform'],
    ARRAY['Kunden', 'Interessenten', 'Website-Besucher'],
    50000,
    false,
    true,
    true,
    true,
    true,
    0.65,
    'mittel',
    true,
    'Maßnahmen erforderlich - DSFA durchführen'
),
(
    'Standard-Newsletter-Versand',
    'E-Mail-Marketing für bestehende Kunden und Interessenten',
    ARRAY['E-Mail-Adresse', 'Name', 'Präferenzen'],
    ARRAY['Marketing-Kommunikation', 'Kundenbindung'],
    ARRAY['E-Mail-Marketing-Tool', 'Analytics'],
    ARRAY['Kunden', 'Newsletter-Abonnenten'],
    5000,
    false,
    false,
    false,
    false,
    false,
    0.25,
    'niedrig',
    false,
    'Akzeptabel - Standard-Maßnahmen ausreichend'
);

-- Demo AI Risk Classifications
INSERT INTO ai_risk_classifications (
    system_name, system_type, application_domain, system_description,
    data_types, estimated_affected_persons, user_interaction,
    automated_decision_making, large_scale, risk_level, risk_score,
    prohibited_practice, ce_marking_required, transparency_obligations_required
) VALUES 
(
    'E-Commerce Recommendation Engine',
    'Recommendation System',
    'E-Commerce',
    'Machine Learning basiertes Empfehlungssystem für Produktvorschläge',
    ARRAY['Kaufverhalten', 'Präferenzen', 'Demografische Daten'],
    50000,
    true,
    true,
    true,
    'LIMITED_RISK',
    0.30,
    false,
    false,
    true
),
(
    'Spam-Filter für E-Mails',
    'Content Filtering System',
    'IT Security',
    'Automatische Erkennung und Filterung von Spam-E-Mails',
    ARRAY['E-Mail-Inhalte', 'Absender-Informationen'],
    1000,
    false,
    false,
    false,
    'MINIMAL_RISK',
    0.10,
    false,
    false,
    false
),
(
    'Biometrische Mitarbeiter-Überwachung',
    'Biometric Identification System',
    'Human Resources',
    'Gesichtserkennung für Zugangskontrolle und Mitarbeiterüberwachung',
    ARRAY['Biometrische Daten', 'Gesichtsdaten', 'Standortdaten'],
    500,
    true,
    true,
    false,
    'HIGH_RISK',
    0.85,
    false,
    true,
    true
);

-- Demo VVT Generation Logs
INSERT INTO vvt_generations (
    company_name, industry, employee_count, total_activities,
    compliance_score, processing_time_ms, success
) VALUES 
(
    'Mustermann Software GmbH',
    'Software-Dienstleistung',
    120,
    6,
    92.5,
    1250,
    true
),
(
    'Demo AG',
    'E-Commerce',
    75,
    4,
    88.0,
    980,
    true
),
(
    'TechStartup GmbH',
    'Software-Entwicklung',
    25,
    3,
    85.5,
    750,
    true
);

-- Demo Compliance Reports
INSERT INTO compliance_reports (
    organization, report_type, format, overall_compliance_score,
    gdpr_status, ai_act_status, vvt_entries, dsfa_assessments,
    ai_systems, critical_issues, processing_time_ms
) VALUES 
(
    'Mustermann Software GmbH',
    'FULL',
    'PDF',
    87.5,
    'COMPLIANT',
    'NEEDS_ATTENTION',
    6,
    2,
    3,
    ARRAY['CE-Kennzeichnung für Hochrisiko-KI-System erforderlich'],
    2150
),
(
    'Demo AG',
    'EXECUTIVE',
    'PDF',
    90.0,
    'COMPLIANT',
    'COMPLIANT',
    4,
    1,
    2,
    ARRAY[]::text[],  -- Expliziter Cast zu text[]
    1800
);

-- Update timestamps for realistic demo data
UPDATE processing_activities 
SET created_at = CURRENT_TIMESTAMP - INTERVAL '30 days',
    updated_at = CURRENT_TIMESTAMP - INTERVAL '5 days'
WHERE company_name = 'Mustermann Software GmbH';

UPDATE dsfa_assessments 
SET created_at = CURRENT_TIMESTAMP - INTERVAL '15 days',
    updated_at = CURRENT_TIMESTAMP - INTERVAL '2 days';

UPDATE ai_risk_classifications 
SET created_at = CURRENT_TIMESTAMP - INTERVAL '10 days',
    updated_at = CURRENT_TIMESTAMP - INTERVAL '1 day';

UPDATE vvt_generations 
SET created_at = CURRENT_TIMESTAMP - INTERVAL '7 days';

UPDATE compliance_reports 
SET created_at = CURRENT_TIMESTAMP - INTERVAL '3 days';