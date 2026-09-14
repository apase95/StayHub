DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM users
        GROUP BY LOWER(BTRIM(email))
        HAVING COUNT(*) > 1
    ) THEN
        RAISE EXCEPTION 'Cannot normalize users.email because canonical duplicates exist';
    END IF;
END $$;

UPDATE users
SET email = LOWER(BTRIM(email))
WHERE email <> LOWER(BTRIM(email));

ALTER TABLE users DROP CONSTRAINT IF EXISTS users_email_key;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uk_users_email'
          AND conrelid = 'users'::regclass
    ) THEN
        ALTER TABLE users DROP CONSTRAINT uk_users_email;
    ELSE
        DROP INDEX IF EXISTS uk_users_email;
    END IF;
END $$;

ALTER TABLE users
    ADD CONSTRAINT uk_users_email_canonical UNIQUE (email),
    ADD CONSTRAINT chk_users_email_normalized
    CHECK (email = LOWER(BTRIM(email)));
