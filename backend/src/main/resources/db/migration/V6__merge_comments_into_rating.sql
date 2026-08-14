ALTER TABLE rating
    ADD COLUMN content TEXT,
    ADD COLUMN commented_on TIMESTAMPTZ;

WITH latest_comment AS (
    SELECT DISTINCT ON (user_id)
        user_id,
        content,
        commented_on
    FROM comments
    WHERE content IS NOT NULL AND btrim(content) <> ''
    ORDER BY user_id, commented_on DESC
)
UPDATE rating r
SET
    content = lc.content,
    commented_on = lc.commented_on
FROM latest_comment lc
WHERE r.user_id = lc.user_id;

DROP INDEX IF EXISTS idx_comments_commented_on;
DROP TABLE comments;

CREATE INDEX idx_rating_commented_on ON rating (commented_on DESC)
    WHERE content IS NOT NULL;
