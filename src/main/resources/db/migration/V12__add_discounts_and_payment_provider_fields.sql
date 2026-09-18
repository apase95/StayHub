CREATE TABLE discount_codes (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    value NUMERIC(12,2) NOT NULL,
    cap NUMERIC(12,2),
    minimum_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
    starts_at TIMESTAMPTZ NOT NULL,
    ends_at TIMESTAMPTZ NOT NULL,
    usage_limit INT,
    used_count INT NOT NULL DEFAULT 0,
    per_user_limit INT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_discount_codes_type CHECK (type IN ('PERCENT', 'FIXED')),
    CONSTRAINT chk_discount_codes_value CHECK (value > 0),
    CONSTRAINT chk_discount_codes_cap CHECK (cap IS NULL OR cap > 0),
    CONSTRAINT chk_discount_codes_minimum_amount CHECK (minimum_amount >= 0),
    CONSTRAINT chk_discount_codes_time_window CHECK (starts_at < ends_at),
    CONSTRAINT chk_discount_codes_usage_limit CHECK (usage_limit IS NULL OR usage_limit > 0),
    CONSTRAINT chk_discount_codes_used_count CHECK (used_count >= 0),
    CONSTRAINT chk_discount_codes_per_user_limit CHECK (per_user_limit IS NULL OR per_user_limit > 0),
    CONSTRAINT chk_discount_codes_usage_count CHECK (usage_limit IS NULL OR used_count <= usage_limit)
);

CREATE INDEX idx_discount_codes_active_window ON discount_codes(active, starts_at, ends_at);

ALTER TABLE bookings
    ADD COLUMN subtotal_price NUMERIC(12,2),
    ADD COLUMN discount_code_id BIGINT REFERENCES discount_codes(id) ON DELETE RESTRICT,
    ADD COLUMN discount_amount NUMERIC(12,2) NOT NULL DEFAULT 0;

UPDATE bookings
SET subtotal_price = total_price
WHERE subtotal_price IS NULL;

ALTER TABLE bookings
    ALTER COLUMN subtotal_price SET NOT NULL,
    DROP CONSTRAINT chk_bookings_prices,
    DROP CONSTRAINT chk_bookings_status,
    DROP CONSTRAINT uq_bookings_active_date_overlap,
    ADD CONSTRAINT chk_bookings_prices CHECK (
        nightly_price > 0
        AND cleaning_fee >= 0
        AND service_fee >= 0
        AND subtotal_price > 0
        AND discount_amount >= 0
        AND total_price = subtotal_price - discount_amount
        AND total_price > 0
    ),
    ADD CONSTRAINT chk_bookings_status CHECK (
        status IN ('PENDING', 'PENDING_PAYMENT', 'CONFIRMED', 'CANCELLED', 'REJECTED', 'COMPLETED')
    );

ALTER TABLE bookings
    ADD CONSTRAINT uq_bookings_active_date_overlap EXCLUDE USING gist (
        property_id WITH =,
        daterange(check_in_date, check_out_date, '[)') WITH &&
    ) WHERE (status IN ('PENDING', 'PENDING_PAYMENT', 'CONFIRMED'));

ALTER TABLE payments
    ADD COLUMN provider VARCHAR(20) NOT NULL DEFAULT 'MOCK',
    ADD COLUMN currency VARCHAR(3) NOT NULL DEFAULT 'VND',
    ADD COLUMN provider_txn_ref VARCHAR(255) UNIQUE,
    ADD COLUMN provider_transaction_no VARCHAR(255),
    ADD COLUMN raw_response JSONB,
    DROP CONSTRAINT chk_payments_status,
    ADD CONSTRAINT chk_payments_provider CHECK (provider IN ('MOCK', 'VNPAY', 'MOMO')),
    ADD CONSTRAINT chk_payments_currency CHECK (currency = 'VND'),
    ADD CONSTRAINT chk_payments_status CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED', 'CANCELLED', 'EXPIRED'));
