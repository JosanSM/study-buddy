ALTER TABLE topic
    ADD COLUMN review_count     INT  NOT NULL DEFAULT 0,
    ADD COLUMN last_reviewed_at DATE NULL,
    ADD COLUMN next_review_at   DATE NULL;
