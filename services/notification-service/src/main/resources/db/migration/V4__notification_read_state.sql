ALTER TABLE notification ADD COLUMN read_at TIMESTAMP;

-- Existing rows were delivery history before inbox read-state existed.
-- Backfill them as read so deployment does not create a large false unread badge.
UPDATE notification
SET read_at = COALESCE(sent_at, created_at);

CREATE INDEX idx_notif_user_unread
    ON notification(user_id, created_at DESC)
    WHERE read_at IS NULL;
