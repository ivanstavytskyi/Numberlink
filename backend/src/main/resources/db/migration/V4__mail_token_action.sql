ALTER TABLE email_verification_tokens
    ADD COLUMN action VARCHAR(32) NOT NULL DEFAULT 'EMAIL_VERIFY';

UPDATE email_verification_tokens
SET action = 'EMAIL_VERIFY'
WHERE action IS NULL OR action = '';

CREATE INDEX idx_email_tokens_user_action_unused
    ON email_verification_tokens (user_id, action)
    WHERE used_at IS NULL;
