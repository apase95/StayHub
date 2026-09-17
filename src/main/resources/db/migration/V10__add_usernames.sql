ALTER TABLE users ADD COLUMN username VARCHAR(50);

CREATE UNIQUE INDEX uk_users_username ON users(username) WHERE username IS NOT NULL;
