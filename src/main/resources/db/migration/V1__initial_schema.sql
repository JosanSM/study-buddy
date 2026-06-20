CREATE TABLE users (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    email VARCHAR(60) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    user_tier VARCHAR(10),
    last_updated TIMESTAMP NOT NULL DEFAULT current_timestamp
);

CREATE TABLE subject (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    user_id BIGINT,
    CONSTRAINT fk_user_subject_table
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE TABLE topic (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    title VARCHAR(50),
    notes TEXT,
    topic_status VARCHAR(20),
    user_id BIGINT,
    subject_id BIGINT,
    CONSTRAINT fk_user_topic_table
        FOREIGN KEY (user_id)
        REFERENCES users(id),
    CONSTRAINT fk_subject
        FOREIGN KEY (subject_id)
        REFERENCES subject(id)
);
