CREATE TABLE property_amenities (
    property_id BIGINT NOT NULL REFERENCES properties(id) ON DELETE CASCADE,
    amenity_id BIGINT NOT NULL REFERENCES amenities(id) ON DELETE RESTRICT,
    PRIMARY KEY (property_id, amenity_id)
);

CREATE INDEX idx_property_amenities_amenity_id ON property_amenities(amenity_id);
