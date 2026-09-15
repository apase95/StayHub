CREATE TABLE amenities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    icon VARCHAR(100),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uk_amenities_name_canonical ON amenities(LOWER(BTRIM(name)));

INSERT INTO amenities (name, icon) VALUES
    ('Wi-Fi', 'wifi'),
    ('Air conditioning', 'snowflake'),
    ('Kitchen', 'utensils'),
    ('Parking', 'car'),
    ('Pool', 'waves'),
    ('Washer', 'washing-machine'),
    ('Workspace', 'laptop'),
    ('TV', 'tv');
