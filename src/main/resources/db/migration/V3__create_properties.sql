CREATE TABLE properties (
    id BIGSERIAL PRIMARY KEY,
    host_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    address VARCHAR(500) NOT NULL,
    city VARCHAR(100) NOT NULL,
    price_per_night NUMERIC(12,2) NOT NULL,
    cleaning_fee NUMERIC(12,2) NOT NULL DEFAULT 0,
    max_guests INT NOT NULL,
    bedrooms INT NOT NULL,
    beds INT NOT NULL,
    bathrooms INT NOT NULL,
    property_type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    rating_avg NUMERIC(3,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_properties_price CHECK (price_per_night > 0),
    CONSTRAINT chk_properties_cleaning_fee CHECK (cleaning_fee >= 0),
    CONSTRAINT chk_properties_capacity CHECK (
        max_guests > 0 AND bedrooms >= 0 AND beds >= 0 AND bathrooms >= 0
    ),
    CONSTRAINT chk_properties_type CHECK (
        property_type IN ('APARTMENT', 'HOUSE', 'VILLA', 'HOTEL_ROOM', 'HOMESTAY', 'RESORT')
    ),
    CONSTRAINT chk_properties_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'DRAFT')),
    CONSTRAINT chk_properties_rating CHECK (rating_avg BETWEEN 0 AND 5)
);

CREATE INDEX idx_properties_host_id ON properties(host_id);
CREATE INDEX idx_properties_public_listing ON properties(status, city);
