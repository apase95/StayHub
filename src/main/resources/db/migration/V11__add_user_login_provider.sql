ALTER TABLE users ADD COLUMN login_provider VARCHAR(20) NOT NULL DEFAULT 'LOCAL';
ALTER TABLE users ADD COLUMN provider_id VARCHAR(255);

ALTER TABLE users ADD CONSTRAINT chk_users_login_provider CHECK (login_provider IN ('LOCAL', 'GOOGLE'));
CREATE UNIQUE INDEX uk_users_login_provider_provider_id ON users(login_provider, provider_id) WHERE provider_id IS NOT NULL;
