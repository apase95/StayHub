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
