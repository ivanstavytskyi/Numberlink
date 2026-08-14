CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version BIGINT DEFAULT 0 NOT NULL,
    email VARCHAR(255) UNIQUE,
    username VARCHAR(128) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE score
(
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    score_result INT NOT NULL,
    elapsed_seconds INT NOT NULL,
    field_width INT NOT NULL,
    field_height INT NOT NULL,
    played_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE rating
(
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    value INTEGER NOT NULL CHECK (value >= 1 AND value <= 5),
    rated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UNIQUE (user_id)
);

CREATE TABLE comments
(
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    commented_on TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE local_accounts
(
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    encoded_password VARCHAR(255) NOT NULL,
    UNIQUE (user_id)
);

CREATE TABLE oauth_accounts
(
    id BIGSERIAL PRIMARY KEY,
    provider VARCHAR(32) NOT NULL,
    sub VARCHAR(255) NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id),
    joined_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UNIQUE (provider, sub),
    UNIQUE (user_id, provider)
);

CREATE INDEX idx_score_leaderboard ON score
(
    score_result DESC,
    played_at DESC
);

CREATE INDEX idx_comments_commented_on ON comments
(
    commented_on DESC
);
