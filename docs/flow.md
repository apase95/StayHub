### Flow
```
User truy cập website
        │
        ▼
     Homepage
        │
        ▼
Search:
- Destination
- Check-in
- Check-out
- Guests
        │
        ▼
Search Results
        │
        ├── Filter
        ├── Sort
        └── Pagination
        │
        ▼
Property Detail
        │
        ├── Images
        ├── Description
        ├── Amenities
        ├── Location
        ├── Reviews
        ├── Availability
        └── Price
        │
        ▼
Select room / booking
        │
        ▼
Booking Information
        │
        ├── Guest information
        ├── Check-in / Check-out
        ├── Number of guests
        └── Price summary
        │
        ▼
"Payment"
        │
        ▼
Create Booking
        │
        ▼
Admin / Host Confirmation
        │
        ▼
Booking Confirmed
        │
        ▼
My Bookings
        │
        ├── View detail
        ├── Cancel
        └── Review after stay
```

### User truy cập website
```
Guest -> Browse -> Search -> View Property
Book -> Login/Register
```

### Homepage
- Header:
    - Logo, Search destination, Login(register), Become a host
    - Nếu đã login: Profile, My Booking, Wishlist, Logout
- Body:
    - Search box:
    ```
    ┌───────────────────────────────────────────────────┐
    │ Where           Check-in    Check-out    Guests   │
    │ Ho Chi Minh     20/09       23/09        2        │
    │                                      [ Search ]   │
    └───────────────────────────────────────────────────┘
    ```
    - Popular destination:
        - Ho Chi Minh
        - Da Nang
        - Da Lat
        - Nha Trang
        - Ha Noi
    - Featured properties:
        - Property A
        - Property B
        - Property C
    - Popular categories:
        - Apartment
        - Villa
        - Hotel
        - Homestay
        - Resort

### Search results
- Layout:
    ```
    ┌───────────────┬───────────────────────────────────┐
    │               │                                   │
    │   FILTER      │       PROPERTY LIST               │
    │               │                                   │
    │ Price         │ ┌───────────────────────────────┐ │
    │               │ │ Image │ Property information  │ │
    │ Property type │ │       │ Rating                │ │
    │               │ │       │ Location              │ │
    │ Bedrooms      │ │       │ Price                 │ │
    │               │ └───────────────────────────────┘ │
    │ Amenities     │                                   │
    │               │ ┌───────────────────────────────┐ │
    │ Rating        │ │ Image │ Property information  │ │
    │               │ └───────────────────────────────┘ │
    └───────────────┴───────────────────────────────────┘
    ```
- Filter:
    - Price range
    - Property type
    - Bedrooms
    - Beds
    - Bathrooms
    - Guests
    - Amenities
    - Rating
- Amenities:
    - Wi-Fi
    - Swimming pool
    - Parking
    - Air conditioning
    - Kitchen
    - Washer
    - TV
- Sort:
    - Price: Low → High
    - Price: High → Low
    - Rating: High → Low
- Pagination:
    - < 1 2 3 4 5 >

### Property details
- Image gallery:
    ```
    ┌─────────────────────────────────────┐
    │                                     │
    │          Main image                 │
    │                                     │
    ├─────────┬─────────┬─────────┬───────┤
    │ Image 2 │ Image 3 │ Image 4 │ +10   │
    └─────────┴─────────┴─────────┴───────┘
    ```
- Property information:
    ```
    The River Apartment

    ⭐ 4.8 (126 reviews)

    📍 District 1, Ho Chi Minh City

    Entire apartment
    2 bedrooms · 2 beds · 2 bathrooms
    4 guests
    ```
- Description:
    ```
    About this place

    ...
    ```
- Amenities:
    ```
    ✓ Wi-Fi
    ✓ Swimming pool
    ✓ Air conditioning
    ✓ Kitchen
    ✓ Parking
    ✓ Washing machine
    ```
- Reviews:
    ```
    ⭐ 4.8 / 5

    ★★★★★
    "Very clean and convenient..."
    Nguyen Van A

    ★★★★☆
    "Great location..."
    Tran Van B
    ```
    
### Availability check
- Trước khi user booking:
    ```
    Check-in
    Check-out
    Guests
    ```
- Backend check:
    ```
    Is property available for this date range?
    ```
- Example:
    ```
    Property A

    20/09 ───── 23/09
         BOOKED

    User:
    22/09 ───── 25/09
    ```
    - Không cho đặt


### Booking page
- Flow: 
    ```
    Property Detail -> Reserve -> Booking page
    ```
- Booking page:
    ```
    ┌─────────────────────────────────────────────┐
    │ Your booking                                │
    ├─────────────────────────────────────────────┤
    │                                             │
    │ Property                                    │
    │ The River Apartment                         │
    │                                             │
    │ Check-in      20 Sep                        │
    │ Check-out     23 Sep                        │
    │ Guests        2                             │
    │                                             │
    │ ─────────────────────────────────────────── │
    │                                             │
    │ $50 × 3 nights                    $150      │
    │ Cleaning fee                       $20      │
    │ Service fee                        $10      │
    │                                             │
    │ Total                              $180     │
    │                                             │
    │              [ Confirm & Pay ]              │
    └─────────────────────────────────────────────┘
    ```
- Flow mock payment:
    - Payment:
    ```
    Confirm & Pay -> Payment Processing -> Payment Success -> Create Booking
    ```
    - Status payment:
    ```
    Payment status = SUCCESS
    ```
    - Database:
    ```
    payments

    id
    booking_id
    amount
    payment_method
    status
    paid_at
    ```
    - Example:
    ```
    payment_method = MOCK
    status = SUCCESS
    ```
    
- Status Booking:
    - Flow:
    ```
    User
     ↓
    Create Booking
     ↓
    Payment Success
     ↓
    Booking = PENDING
     ↓
    Host
     ↓
    Confirm
     ↓
    Booking = CONFIRMED
     ↓
    User receives notification
    ```
    - Booking status enum:
    ```
    PENDING
    CONFIRMED
    CANCELLED
    REJECTED
    COMPLETED
    ```
    - User confirm:
    ```
                  ┌───────────┐
                  │  PENDING  │
                  └─────┬─────┘
                    ┌───┴───┐
                    ↓       ↓
              CONFIRMED   REJECTED
                  │
                  ↓
              COMPLETED
                  │
                  ↓
               REVIEW
    ```
    - Nếu user cancel:
    ```
    PENDING ──────> CANCELLED
    CONFIRMED ────> CANCELLED
    ```
    - Host confirm:
    ```
    PENDING
       │
       ├── Host accepts
       │       ↓
       │   CONFIRMED
       │
       └── Host rejects
               ↓
           REJECTED
     ```
     - Booking SUCCESSFULLY:
    ```
    My Bookings

    ┌─────────────────────────────────────────────┐
    │ The River Apartment                         │
    │                                             │
    │ 20 Sep → 23 Sep                             │
    │ 2 guests                                    │
    │                                             │
    │ Status: CONFIRMED                           │
    │ Total: $180                                 │
    │                                             │
    │ [ View Details ] [ Cancel ]                 │
    └─────────────────────────────────────────────┘
    ```
    - Detail:
    ```
    Booking #BK20260001

    Property:
    The River Apartment

    Guest:
    Ho Duy

    Check-in:
    20/09/2026

    Check-out:
    23/09/2026

    Guests:
    2

    Total:
    $180

    Payment:
    SUCCESS

    Status:
    CONFIRMED
    ```
     
### LAST FLOW:
```
                       ┌───────────┐
                       │   GUEST   │
                       └─────┬─────┘
                             │
                             ↓
                         Homepage
                             │
                             ↓
                         Search
                             │
                             ↓
                    Search Results
                             │
                   ┌─────────┴─────────┐
                   ↓                   ↓
                Filter               Sort
                   │                   │
                   └─────────┬─────────┘
                             ↓
                     Property Detail
                             │
                             ↓
                     Check Availability
                             │
                             ↓
                         Booking
                             │
                             ↓
                    Mock Payment
                             │
                             ↓
                        PENDING
                             │
                             ↓
                          HOST
                       ┌─────┴─────┐
                       ↓           ↓
                    ACCEPT      REJECT
                       ↓           ↓
                   CONFIRMED    REJECTED
                       │
                       ↓
                   CHECK-IN
                       │
                       ↓
                  CHECK-OUT
                       │
                       ↓
                   COMPLETED
                       │
                       ↓
                    REVIEW
```