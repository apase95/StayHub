CREATE TABLE property_images (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT NOT NULL REFERENCES properties(id) ON DELETE CASCADE,
    image_url VARCHAR(1000) NOT NULL,
    public_id VARCHAR(255),
    display_order INT NOT NULL DEFAULT 0,
    is_cover BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_property_images_order CHECK (display_order >= 0),
    CONSTRAINT uk_property_images_order UNIQUE (property_id, display_order)
);

CREATE UNIQUE INDEX uk_property_images_cover
    ON property_images(property_id)
    WHERE is_cover = TRUE;

CREATE INDEX idx_property_images_property_id ON property_images(property_id);
