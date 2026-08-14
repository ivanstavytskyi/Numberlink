ALTER TABLE email_verification_tokens RENAME TO email_tokens;

ALTER INDEX IF EXISTS email_verification_tokens_pkey
    RENAME TO email_tokens_pkey;

ALTER INDEX IF EXISTS email_verification_tokens_token_hash_key
    RENAME TO email_tokens_token_hash_key;

ALTER INDEX IF EXISTS idx_email_verification_tokens_user_id
    RENAME TO idx_email_tokens_user_id;
