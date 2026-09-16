CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT NOT NULL REFERENCES properties(id) ON DELETE RESTRICT,
    guest_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    guests INT NOT NULL,
    nightly_price NUMERIC(12,2) NOT NULL,
    cleaning_fee NUMERIC(12,2) NOT NULL,
    service_fee NUMERIC(12,2) NOT NULL,
    total_price NUMERIC(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    cancelled_at TIMESTAMPTZ NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_bookings_dates CHECK (check_in_date < check_out_date),
    CONSTRAINT chk_bookings_guests CHECK (guests > 0),
    CONSTRAINT chk_bookings_prices CHECK (
        nightly_price > 0
        AND cleaning_fee >= 0
        AND service_fee >= 0
        AND total_price > 0
    ),
    CONSTRAINT chk_bookings_status CHECK (
        status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'REJECTED', 'COMPLETED')
    ),
    CONSTRAINT chk_bookings_cancelled_at CHECK (
        (status = 'CANCELLED' AND cancelled_at IS NOT NULL)
        OR (status <> 'CANCELLED' AND cancelled_at IS NULL)
    ),
    CONSTRAINT uq_bookings_active_date_overlap EXCLUDE USING gist (
        property_id WITH =,
        daterange(check_in_date, check_out_date, '[)') WITH &&
    ) WHERE (status IN ('PENDING', 'CONFIRMED'))
);

CREATE INDEX idx_bookings_guest_status ON bookings(guest_id, status);
CREATE INDEX idx_bookings_property_status ON bookings(property_id, status);
CREATE INDEX idx_bookings_host_requests ON bookings(property_id, status, created_at DESC);
