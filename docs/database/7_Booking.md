# Bookings

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