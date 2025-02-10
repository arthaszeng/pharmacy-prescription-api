-- Create audit_logs table with JSONB fields
CREATE TABLE audit_logs
(
    id              SERIAL PRIMARY KEY,
    prescription_id BIGINT                              NOT NULL,
    patient_id      BIGINT                              NOT NULL,
    pharmacy_id     BIGINT                              NOT NULL,
    status          VARCHAR(20)                         NOT NULL CHECK (status IN ('SUCCESS', 'FAILURE')),
    failure_reason  TEXT                                NULL,
    requested_drugs JSONB                               NOT NULL,
    dispensed_drugs JSONB                               NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Index for performance optimization
CREATE INDEX idx_audit_logs_patient ON audit_logs (patient_id);
CREATE INDEX idx_audit_logs_pharmacy ON audit_logs (pharmacy_id);
CREATE INDEX idx_audit_logs_status ON audit_logs (status);