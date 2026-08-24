# StayHub MVP — Database Design & Diagrams

> **Purpose:** Database and domain design proposal for the StayHub MVP, based on the project's planned modules: `user`, `property`, `booking`, `payment`, `review`, `host`, `admin`, `storage`, and `notification`.
>
> **Database:** PostgreSQL  
> **Migration:** Flyway  
> **ORM:** Spring Data JPA / Hibernate  
> **Primary key strategy:** `BIGINT` generated IDs for the MVP

---

# 1. Domain Overview

The MVP can be divided into the following main domains:

```mermaid
flowchart TB
    U[User & Authentication]
    P[Property Management]
    B[Booking]
    PAY[Payment]
    R[Review]
    S[Search & Availability]
    N[Notification]

    U --> P
    U --> B
    P --> S
    P --> B
    B --> PAY
    B --> R
    U --> R
    B --> N
```

## Main relationships

- A **User** can act as `GUEST`, `HOST`, or `ADMIN`.
- A **Host** owns multiple **Properties**.
- A **Property** contains multiple **PropertyImages**.
- A **Property** has many **Amenities** through `property_amenities`.
- A **Guest** can create multiple **Bookings**.
- A **Property** can receive multiple **Bookings** over time.
- A **Booking** has one payment record in the MVP.
- A completed **Booking** can have at most one **Review**.

---

# 2. Core ERD

```mermaid
erDiagram

    USERS {
        bigint id PK
        varchar email UK
        varchar password_hash
        varchar full_name
        varchar phone
        varchar role
        varchar status
        timestamptz created_at
        timestamptz updated_at
    }

    PROPERTIES {
        bigint id PK
        bigint host_id FK
        varchar title
        text description
        varchar address
        varchar city
        numeric price_per_night
        int max_guests
        int bedrooms
        int beds
        int bathrooms
        varchar property_type
        varchar status
        timestamptz created_at
        timestamptz updated_at
    }

    PROPERTY_IMAGES {
        bigint id PK
        bigint property_id FK
        varchar image_url
        varchar public_id
        int display_order
        boolean is_cover
        timestamptz created_at
        timestamptz updated_at
    }

    AMENITIES {
        bigint id PK
        varchar name UK
        varchar icon
        timestamptz created_at
        timestamptz updated_at
    }

    PROPERTY_AMENITIES {
        bigint property_id PK, FK
        bigint amenity_id PK, FK
    }

    BOOKINGS {
        bigint id PK
        bigint property_id FK
        bigint guest_id FK
        date check_in_date
        date check_out_date
        int guests
        numeric nightly_price
        numeric cleaning_fee
        numeric service_fee
        numeric total_price
        varchar status
        timestamptz created_at
        timestamptz updated_at
    }

    PAYMENTS {
        bigint id PK
        bigint booking_id FK
        varchar payment_method
        varchar status
        numeric amount
        varchar transaction_id UK
        timestamptz paid_at
        timestamptz created_at
        timestamptz updated_at
    }

    REVIEWS {
        bigint id PK
        bigint booking_id FK
        bigint property_id FK
        bigint guest_id FK
        smallint rating
        text comment
        timestamptz created_at
        timestamptz updated_at
    }

    USERS ||--o{ PROPERTIES : "hosts"
    USERS ||--o{ BOOKINGS : "creates"
    USERS ||--o{ REVIEWS : "writes"

    PROPERTIES ||--o{ PROPERTY_IMAGES : "contains"
    PROPERTIES ||--o{ BOOKINGS : "receives"
    PROPERTIES ||--o{ REVIEWS : "has"

    PROPERTIES ||--o{ PROPERTY_AMENITIES : "has"
    AMENITIES ||--o{ PROPERTY_AMENITIES : "assigned to"

    BOOKINGS ||--|| PAYMENTS : "has"
    BOOKINGS ||--o| REVIEWS : "can produce"
```

---

# 3. Relational Model

## 3.1 `users`

| Column | Type | Constraint | Description |
|---|---|---|---|
| `id` | BIGINT | PK | User identifier |
| `email` | VARCHAR(255) | NOT NULL, UNIQUE | Login email |
| `password_hash` | VARCHAR(255) | NOT NULL | BCrypt password hash |
| `full_name` | VARCHAR(150) | NOT NULL | User full name |
| `phone` | VARCHAR(30) | NULL | Contact phone |
| `role` | VARCHAR(20) | NOT NULL | `GUEST`, `HOST`, `ADMIN` |
| `status` | VARCHAR(20) | NOT NULL | `ACTIVE`, `LOCKED`, `INACTIVE` |
| `created_at` | TIMESTAMPTZ | NOT NULL | Creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL | Last update timestamp |

### Recommended constraints

```sql
CONSTRAINT chk_users_role
CHECK (role IN ('GUEST', 'HOST', 'ADMIN'));

CONSTRAINT chk_users_status
CHECK (status IN ('ACTIVE', 'LOCKED', 'INACTIVE'));
```

### Relationship

```text
users
 ├── 1 : N properties
 ├── 1 : N bookings
 └── 1 : N reviews
```

---

## 3.2 `properties`

| Column | Type | Constraint | Description |
|---|---|---|---|
| `id` | BIGINT | PK | Property identifier |
| `host_id` | BIGINT | FK → users.id | Owner/host |
| `title` | VARCHAR(255) | NOT NULL | Property title |
| `description` | TEXT | NOT NULL | Description |
| `address` | VARCHAR(500) | NOT NULL | Address |
| `city` | VARCHAR(100) | NOT NULL | Searchable city |
| `price_per_night` | NUMERIC(12,2) | NOT NULL | Base nightly price |
| `max_guests` | INT | NOT NULL | Maximum guests |
| `bedrooms` | INT | NOT NULL | Bedroom count |
| `beds` | INT | NOT NULL | Bed count |
| `bathrooms` | INT | NOT NULL | Bathroom count |
| `property_type` | VARCHAR(30) | NOT NULL | Property category |
| `status` | VARCHAR(20) | NOT NULL | Listing state |
| `created_at` | TIMESTAMPTZ | NOT NULL | Creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL | Last update timestamp |

### Suggested values

```text
property_type:
- APARTMENT
- HOUSE
- VILLA
- HOTEL_ROOM
- HOMESTAY

status:
- ACTIVE
- INACTIVE
- DRAFT
```

### Relationship

```text
users (HOST)
       1
       │
       │ owns
       ▼
properties
  ├── 1 : N property_images
  ├── N : M amenities
  ├── 1 : N bookings
  └── 1 : N reviews
```

---

## 3.3 `property_images`

| Column | Type | Constraint | Description |
|---|---|---|---|
| `id` | BIGINT | PK | Image identifier |
| `property_id` | BIGINT | FK | Related property |
| `image_url` | VARCHAR(1000) | NOT NULL | Image URL |
| `public_id` | VARCHAR(255) | NULL | Cloudinary/local storage identifier |
| `display_order` | INT | NOT NULL | Gallery order |
| `is_cover` | BOOLEAN | NOT NULL | Cover image flag |
| `created_at` | TIMESTAMPTZ | NOT NULL | Creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL | Last update timestamp |

### Important rule

At most one image should be marked as the cover image for each property.

Possible application-level invariant:

```text
For each property:
COUNT(property_images WHERE is_cover = true) <= 1
```

---

## 3.4 `amenities`

| Column | Type | Constraint | Description |
|---|---|---|---|
| `id` | BIGINT | PK | Amenity identifier |
| `name` | VARCHAR(100) | UNIQUE | Amenity name |
| `icon` | VARCHAR(100) | NULL | Icon identifier |
| `created_at` | TIMESTAMPTZ | NOT NULL | Creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL | Last update timestamp |

Examples:

```text
WiFi
Air Conditioning
Kitchen
Pool
Parking
Washer
TV
Workspace
```

---

## 3.5 `property_amenities`

This table implements the many-to-many relationship.

| Column | Type | Constraint |
|---|---|---|
| `property_id` | BIGINT | PK, FK → properties.id |
| `amenity_id` | BIGINT | PK, FK → amenities.id |

```mermaid
flowchart LR
    P[Property] -->|1..N| PA[property_amenities]
    A[Amenity] -->|1..N| PA
```

---

# 4. Booking Data Model

## 4.1 `bookings`

| Column | Type | Constraint | Description |
|---|---|---|---|
| `id` | BIGINT | PK | Booking identifier |
| `property_id` | BIGINT | FK → properties.id | Booked property |
| `guest_id` | BIGINT | FK → users.id | Guest |
| `check_in_date` | DATE | NOT NULL | Check-in |
| `check_out_date` | DATE | NOT NULL | Check-out |
| `guests` | INT | NOT NULL | Guest count |
| `nightly_price` | NUMERIC(12,2) | NOT NULL | Snapshot of nightly price |
| `cleaning_fee` | NUMERIC(12,2) | NOT NULL | Snapshot fee |
| `service_fee` | NUMERIC(12,2) | NOT NULL | Snapshot fee |
| `total_price` | NUMERIC(12,2) | NOT NULL | Final total |
| `status` | VARCHAR(20) | NOT NULL | Booking status |
| `created_at` | TIMESTAMPTZ | NOT NULL | Creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL | Last update timestamp |

## Why store price snapshots?

Do not calculate historical booking totals from the current `properties.price_per_night`.

Example:

```text
January booking:
price_per_night = 1,000,000 VND

March:
Host changes property price to 1,500,000 VND
```

The January booking must still preserve:

```text
nightly_price = 1,000,000
```

Therefore:

```text
properties.price_per_night
        ≠
bookings.nightly_price
```

`bookings` stores the financial snapshot at booking time.

---

# 5. Booking State Diagram

```mermaid
stateDiagram-v2

    [*] --> PENDING : Payment success

    PENDING --> CONFIRMED : Host accepts
    PENDING --> REJECTED : Host rejects
    PENDING --> CANCELLED : Guest cancels

    CONFIRMED --> CANCELLED : Guest cancels
    CONFIRMED --> COMPLETED : Stay finished

    REJECTED --> [*]
    CANCELLED --> [*]
    COMPLETED --> [*]
```

## State transition matrix

| Current | Action | Next |
|---|---|---|
| `PENDING` | Host accepts | `CONFIRMED` |
| `PENDING` | Host rejects | `REJECTED` |
| `PENDING` | Guest cancels | `CANCELLED` |
| `CONFIRMED` | Guest cancels | `CANCELLED` |
| `CONFIRMED` | Stay completed | `COMPLETED` |

Invalid transitions should raise a business exception.

For example:

```text
REJECTED → CONFIRMED   ❌
COMPLETED → CANCELLED  ❌
CANCELLED → PENDING    ❌
```

---

# 6. Availability / Date Overlap Model

A new booking request conflicts with an existing active booking when:

```text
requested.check_in < existing.check_out
AND
requested.check_out > existing.check_in
```

Equivalent SQL concept:

```sql
WHERE property_id = :propertyId
  AND status IN ('PENDING', 'CONFIRMED')
  AND check_in_date < :requestedCheckOut
  AND check_out_date > :requestedCheckIn
```

## Visual example

```text
Existing:
|--------- Occupied ---------|
10        11        12        13        14

Request A:
      |----- overlap -----|
      11        12        13

=> NOT AVAILABLE
```

Adjacent bookings should normally be allowed:

```text
Existing:
[ Check-in 10 ] -------- [ Check-out 12 ]

New:
                            [ Check-in 12 ] ----- [ Check-out 15 ]

=> AVAILABLE
```

This is why the overlap condition uses:

```text
new_check_in < existing_check_out
AND
new_check_out > existing_check_in
```

rather than inclusive comparisons.

---

# 7. Payment Model

## `payments`

| Column | Type | Constraint | Description |
|---|---|---|---|
| `id` | BIGINT | PK | Payment identifier |
| `booking_id` | BIGINT | FK, UNIQUE | Related booking |
| `payment_method` | VARCHAR(20) | NOT NULL | `MOCK`, `VNPAY`, `MOMO` |
| `status` | VARCHAR(20) | NOT NULL | Payment state |
| `amount` | NUMERIC(12,2) | NOT NULL | Payment amount |
| `transaction_id` | VARCHAR(255) | UNIQUE | Gateway transaction reference |
| `paid_at` | TIMESTAMPTZ | NULL | Success time |
| `created_at` | TIMESTAMPTZ | NOT NULL | Creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL | Last update timestamp |

## Payment states

```text
PENDING
SUCCESS
FAILED
REFUNDED
```

## MVP relationship

```mermaid
flowchart LR
    G[Guest] --> CP[Confirm & Pay]
    CP --> MP[MockPaymentService]
    MP --> PS[Payment SUCCESS]
    PS --> CB[Create Booking]
    CB --> B[Booking PENDING]
```

### Recommended business sequence

```text
1. Validate request
2. Check property availability
3. Calculate price
4. Create/perform payment
5. If payment SUCCESS:
      create booking with PENDING
6. Return booking confirmation
```

For real payment gateways later, this flow can evolve into a more transactional/webhook-based design.

---

# 8. Review Data Model

## `reviews`

| Column | Type | Constraint | Description |
|---|---|---|---|
| `id` | BIGINT | PK | Review identifier |
| `booking_id` | BIGINT | FK, UNIQUE | Source booking |
| `property_id` | BIGINT | FK | Reviewed property |
| `guest_id` | BIGINT | FK | Review author |
| `rating` | SMALLINT | NOT NULL | Rating from 1 to 5 |
| `comment` | TEXT | NULL | Review content |
| `created_at` | TIMESTAMPTZ | NOT NULL | Creation timestamp |
| `updated_at` | TIMESTAMPTZ | NOT NULL | Last update timestamp |

### Review rule

```text
Booking.status must equal COMPLETED
```

before a review can be created.

```mermaid
flowchart LR
    B[Booking] --> C{Status = COMPLETED?}
    C -->|No| X[Reject Review]
    C -->|Yes| R[Create Review]
    R --> P[Property]
```

### Recommended constraints

```sql
CONSTRAINT chk_review_rating
CHECK (rating BETWEEN 1 AND 5);
```

`booking_id UNIQUE` ensures:

```text
One booking → at most one review
```

---

# 9. Class Diagram

This is a conceptual JPA/domain class diagram.

```mermaid
classDiagram

    class BaseEntity {
        <<abstract>>
        +Long id
        +Instant createdAt
        +Instant updatedAt
    }

    class User {
        +String email
        +String passwordHash
        +String fullName
        +String phone
        +UserRole role
        +UserStatus status
    }

    class Property {
        +Long hostId
        +String title
        +String description
        +String address
        +String city
        +BigDecimal pricePerNight
        +Integer maxGuests
        +Integer bedrooms
        +Integer beds
        +Integer bathrooms
        +PropertyType propertyType
        +PropertyStatus status
    }

    class PropertyImage {
        +String imageUrl
        +String publicId
        +Integer displayOrder
        +Boolean isCover
    }

    class Amenity {
        +String name
        +String icon
    }

    class Booking {
        +Long propertyId
        +Long guestId
        +LocalDate checkInDate
        +LocalDate checkOutDate
        +Integer guests
        +BigDecimal nightlyPrice
        +BigDecimal cleaningFee
        +BigDecimal serviceFee
        +BigDecimal totalPrice
        +BookingStatus status
    }

    class Payment {
        +Long bookingId
        +PaymentMethod paymentMethod
        +PaymentStatus status
        +BigDecimal amount
        +String transactionId
        +Instant paidAt
    }

    class Review {
        +Long bookingId
        +Long propertyId
        +Long guestId
        +Integer rating
        +String comment
    }

    class UserRole {
        <<enumeration>>
        GUEST
        HOST
        ADMIN
    }

    class BookingStatus {
        <<enumeration>>
        PENDING
        CONFIRMED
        CANCELLED
        REJECTED
        COMPLETED
    }

    class PaymentMethod {
        <<enumeration>>
        MOCK
        VNPAY
        MOMO
    }

    class PaymentStatus {
        <<enumeration>>
        PENDING
        SUCCESS
        FAILED
        REFUNDED
    }

    BaseEntity <|-- User
    BaseEntity <|-- Property
    BaseEntity <|-- PropertyImage
    BaseEntity <|-- Amenity
    BaseEntity <|-- Booking
    BaseEntity <|-- Payment
    BaseEntity <|-- Review

    User --> UserRole
    Booking --> BookingStatus
    Payment --> PaymentMethod
    Payment --> PaymentStatus

    User "1" --> "0..*" Property : hosts
    User "1" --> "0..*" Booking : guest
    Property "1" --> "0..*" PropertyImage
    Property "1" --> "0..*" Booking
    Booking "1" --> "1" Payment
    Booking "1" --> "0..1" Review
```

---

# 10. Java Entity Relationship Mapping

Suggested mapping:

```text
User
  @OneToMany(mappedBy = "host")
      → properties

Property
  @ManyToOne
      → host

Property
  @OneToMany(mappedBy = "property")
      → images

Property
  @ManyToMany
      → amenities

Booking
  @ManyToOne
      → property

Booking
  @ManyToOne
      → guest

Payment
  @OneToOne
      → booking

Review
  @OneToOne
      → booking
```

## Recommended ownership

```mermaid
flowchart TB
    U[User]
    P[Property]
    PI[PropertyImage]
    A[Amenity]
    B[Booking]
    PAY[Payment]
    R[Review]

    U -->|host_id| P
    P -->|property_id| PI
    P <-->|property_amenities| A
    P -->|property_id| B
    U -->|guest_id| B
    B -->|booking_id| PAY
    B -->|booking_id| R
```

---

# 11. Repository / Relational Table Summary

| Table | Primary Key | Main Foreign Keys |
|---|---|---|
| `users` | `id` | — |
| `properties` | `id` | `host_id → users.id` |
| `property_images` | `id` | `property_id → properties.id` |
| `amenities` | `id` | — |
| `property_amenities` | `(property_id, amenity_id)` | property + amenity |
| `bookings` | `id` | `property_id`, `guest_id` |
| `payments` | `id` | `booking_id` |
| `reviews` | `id` | `booking_id`, `property_id`, `guest_id` |

---

# 12. Full Relational Diagram

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

---

# 13. Suggested Indexes

## `users`

```sql
CREATE UNIQUE INDEX uk_users_email
ON users(email);
```

## `properties`

```sql
CREATE INDEX idx_properties_host_id
ON properties(host_id);

CREATE INDEX idx_properties_city
ON properties(city);

CREATE INDEX idx_properties_price
ON properties(price_per_night);

CREATE INDEX idx_properties_status
ON properties(status);
```

## `bookings`

Availability checking will be a frequent query.

```sql
CREATE INDEX idx_bookings_property_dates
ON bookings(property_id, check_in_date, check_out_date);

CREATE INDEX idx_bookings_guest_id
ON bookings(guest_id);

CREATE INDEX idx_bookings_status
ON bookings(status);
```

## `property_images`

```sql
CREATE INDEX idx_property_images_property_id
ON property_images(property_id);
```

## `reviews`

```sql
CREATE INDEX idx_reviews_property_id
ON reviews(property_id);
```

---

# 14. Search Data Relationships

Search uses property data plus booking data.

```mermaid
flowchart LR

    C[SearchCriteria]
    P[properties]
    B[bookings]
    A[amenities]
    PA[property_amenities]
    R[reviews]

    C -->|city| P
    C -->|guests <= max_guests| P
    C -->|price filter| P
    C -->|property type| P

    C -->|amenity filter| PA
    PA --> A

    C -->|date availability| B
    P --> B

    R -->|rating filter| P
```

### Example search criteria

```text
location = "Da Nang"
checkIn = 2026-09-10
checkOut = 2026-09-13
guests = 2

filters:
- minPrice
- maxPrice
- propertyType
- bedrooms
- amenities
- rating
```

Search should return properties where:

```text
city matches
AND max_guests >= requestedGuests
AND price matches filters
AND no conflicting active booking exists
```

---

# 15. Database Migration Plan

Recommended Flyway migration sequence:

```text
V1__create_users.sql
V2__create_properties.sql
V3__create_bookings.sql
V4__create_payments.sql
V5__create_reviews.sql
```

A more granular production-friendly sequence:

```text
V1__create_users.sql
V2__create_properties.sql
V3__create_property_images.sql
V4__create_amenities.sql
V5__create_property_amenities.sql
V6__create_bookings.sql
V7__create_payments.sql
V8__create_reviews.sql
V9__add_database_indexes.sql
```

## Recommended rule

Do not edit an already-applied migration.

Instead:

```text
Wrong:
V2__create_properties.sql
  → modify after other developers have run it
```

Use:

```text
Correct:
V10__add_property_status.sql
```

---

# 16. BaseEntity Design

All main entities can inherit common audit fields.

```java
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
```

Inheritance:

```text
BaseEntity
    │
    ├── User
    ├── Property
    ├── PropertyImage
    ├── Amenity
    ├── Booking
    ├── Payment
    └── Review
```

---

# 17. Important Business Rules

## User

```text
email must be unique
role ∈ GUEST, HOST, ADMIN
```

## Property

```text
price_per_night > 0
max_guests > 0
bedrooms >= 0
beds >= 0
bathrooms >= 0
```

## Booking

```text
check_in_date < check_out_date
guests > 0
guests <= property.max_guests
no date overlap with active bookings
```

## Payment

```text
amount >= 0
one payment record per MVP booking
booking is created only after required payment success
```

## Review

```text
1 <= rating <= 5
booking must belong to the reviewing guest
booking.status = COMPLETED
one booking can create at most one review
```

---

# 18. Recommended Package ↔ Table Mapping

```text
com.stayhub.user
    └── users

com.stayhub.property
    ├── properties
    ├── property_images
    ├── amenities
    └── property_amenities

com.stayhub.booking
    └── bookings

com.stayhub.payment
    └── payments

com.stayhub.review
    └── reviews
```

This keeps the Java code feature-based while maintaining a clear mapping to relational tables.

---

# 19. Future Extensions

These should not necessarily be included in the first MVP schema.

## Wishlist

```text
wishlists
- id
- user_id
- property_id
- created_at
```

Relationship:

```text
User N : M Property
```

## Notification

```text
notifications
- id
- user_id
- type
- title
- content
- is_read
- created_at
```

## Real payment

Possible additions:

```text
payment_attempts
payment_webhook_events
refunds
```

## Availability optimization

If the application grows significantly, consider:

```text
property_availability
```

or PostgreSQL range/exclusion constraints to strengthen date-overlap guarantees.

---

# 20. Final MVP Database Summary

```text
                    ┌───────────────┐
                    │     USERS     │
                    └───────┬───────┘
                       HOST │ GUEST
                            │
          ┌─────────────────┴─────────────────┐
          ▼                                   ▼
   ┌───────────────┐                   ┌───────────────┐
   │  PROPERTIES   │◄──────────────────│   BOOKINGS    │
   └───────┬───────┘                   └───────┬───────┘
           │                                   │
     ┌─────┼─────────┐                   ┌─────┴─────┐
     ▼     ▼         ▼                   ▼           ▼
  IMAGES AMENITIES BOOKINGS          PAYMENTS     REVIEWS
           │
           ▼
  PROPERTY_AMENITIES
```

## MVP tables

```text
1. users
2. properties
3. property_images
4. amenities
5. property_amenities
6. bookings
7. payments
8. reviews
```

This design supports the complete MVP flow:

```text
Register/Login
    ↓
Search Property
    ↓
Property Detail
    ↓
Check Availability
    ↓
Create Payment
    ↓
Payment Success
    ↓
Booking PENDING
    ↓
Host Accept / Reject
    ↓
CONFIRMED / REJECTED
    ↓
COMPLETED
    ↓
Review
```
