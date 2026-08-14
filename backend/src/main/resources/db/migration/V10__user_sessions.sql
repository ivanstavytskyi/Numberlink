CREATE TABLE user_sessions
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    http_session_hash VARCHAR(64) NOT NULL UNIQUE,
    device VARCHAR(128) NOT NULL,
    os VARCHAR(64) NOT NULL,
    browser VARCHAR(64) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_seen_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    revoked_at TIMESTAMPTZ
);

CREATE INDEX idx_user_sessions_user_active
    ON user_sessions (user_id, revoked_at);
