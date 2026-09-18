UPDATE properties
SET price_per_night = price_per_night * 10000,
    cleaning_fee = cleaning_fee * 10000
WHERE price_per_night < 100000;

INSERT INTO discount_codes (
    code,
    type,
    value,
    cap,
    minimum_amount,
    starts_at,
    ends_at,
    usage_limit,
    used_count,
    per_user_limit,
    active
) VALUES (
    'SAVE20',
    'PERCENT',
    20.00,
    50000.00,
    100000.00,
    CURRENT_TIMESTAMP - INTERVAL '1 day',
    CURRENT_TIMESTAMP + INTERVAL '1 year',
    1000,
    0,
    NULL,
    TRUE
)
ON CONFLICT (code) DO UPDATE SET
    type = EXCLUDED.type,
    value = EXCLUDED.value,
    cap = EXCLUDED.cap,
    minimum_amount = EXCLUDED.minimum_amount,
    starts_at = EXCLUDED.starts_at,
    ends_at = EXCLUDED.ends_at,
    usage_limit = EXCLUDED.usage_limit,
    per_user_limit = EXCLUDED.per_user_limit,
    active = EXCLUDED.active;
