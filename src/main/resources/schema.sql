-- Runs after Hibernate DDL (see application.properties). IF NOT EXISTS: safe on every startup and
-- when you drop/recreate the database. VARCHAR lengths must match ClinicalBackground.MAX_TEXT_FIELD_LENGTH.
CREATE TABLE IF NOT EXISTS clinical_backgrounds (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    senior_citizen_id BIGINT NOT NULL,
    hypertension TINYINT(1) NOT NULL,
    diabetes TINYINT(1) NOT NULL,
    cardiovascular_disease TINYINT(1) NOT NULL,
    respiratory_disease TINYINT(1) NOT NULL,
    allergies VARCHAR(1000),
    medications VARCHAR(1000),
    mobility_notes VARCHAR(1000),
    cognitive_condition VARCHAR(1000),
    general_notes VARCHAR(1000),
    created_by_role VARCHAR(32) NOT NULL,
    created_by_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_clinical_backgrounds_senior (senior_citizen_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
