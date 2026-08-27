```mermaid
flowchart LR

    USERS[(users)]

    PROPERTIES[(properties)]
    IMAGES[(property_images)]
    AMENITIES[(amenities)]
    PA[(property_amenities)]

    BOOKINGS[(bookings)]
    PAYMENTS[(payments)]
    REVIEWS[(reviews)]

    USERS -->|"1:N host_id"| PROPERTIES
    PROPERTIES -->|"1:N property_id"| IMAGES

    PROPERTIES -->|"1:N"| PA
    AMENITIES -->|"1:N"| PA

    USERS -->|"1:N guest_id"| BOOKINGS
    PROPERTIES -->|"1:N property_id"| BOOKINGS

    BOOKINGS -->|"1:1 booking_id"| PAYMENTS
    BOOKINGS -->|"1:0..1 booking_id"| REVIEWS

    USERS -->|"1:N guest_id"| REVIEWS
    PROPERTIES -->|"1:N property_id"| REVIEWS
```
