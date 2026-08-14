CREATE TABLE user_totp
(
    user_id UUID PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    secret VARCHAR(64),
    pending_secret VARCHAR(64),
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    confirmed_at TIMESTAMPTZ
);
