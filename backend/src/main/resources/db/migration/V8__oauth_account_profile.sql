ALTER TABLE oauth_accounts
    ADD COLUMN display_name VARCHAR(255),
    ADD COLUMN email VARCHAR(255);
