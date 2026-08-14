ALTER TABLE users
    ADD COLUMN session_epoch INTEGER NOT NULL DEFAULT 0;

CREATE TABLE email_change_requests
(
    id                 BIGSERIAL PRIMARY KEY,
    user_id            UUID NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    current_email      VARCHAR(255),
    new_email          VARCHAR(255) NOT NULL,
    confirm_code_hash  VARCHAR(64) NOT NULL,
    cancel_token_hash  VARCHAR(64) UNIQUE,
    attempts           INTEGER NOT NULL DEFAULT 0,
    expires_at         TIMESTAMPTZ NOT NULL,
    last_sent_at       TIMESTAMPTZ NOT NULL,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_email_change_new_email
    ON email_change_requests (new_email);

CREATE INDEX idx_email_change_expires
    ON email_change_requests (expires_at);
