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
        +String username
        +String passwordHash
        +String fullName
        +String phone
        +UserRole role
        +UserStatus status
        +UserLoginProvider loginProvider
        +String providerId
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
        +BigDecimal subtotalPrice
        +Long discountCodeId
        +BigDecimal discountAmount
        +BigDecimal totalPrice
        +BookingStatus status
    }

    class DiscountCode {
        +String code
        +DiscountType type
        +BigDecimal value
        +BigDecimal maxDiscountAmount
        +BigDecimal minBookingAmount
        +Instant startsAt
        +Instant endsAt
        +Integer usageLimit
        +Integer usedCount
        +Integer perUserLimit
        +Boolean active
    }

    class Payment {
        +Long bookingId
        +PaymentMethod paymentMethod
        +PaymentProvider provider
        +PaymentStatus status
        +BigDecimal amount
        +String currency
        +String transactionId
        +String providerTxnRef
        +String providerTransactionNo
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

    class UserLoginProvider {
        <<enumeration>>
        LOCAL
        GOOGLE
    }

    class BookingStatus {
        <<enumeration>>
        PENDING_PAYMENT
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

    class PaymentProvider {
        <<enumeration>>
        MOCK
        VNPAY
    }

    class PaymentStatus {
        <<enumeration>>
        PENDING
        SUCCESS
        FAILED
        CANCELLED
        EXPIRED
        REFUNDED
    }

    class DiscountType {
        <<enumeration>>
        PERCENT
        FIXED
    }

    BaseEntity <|-- User
    BaseEntity <|-- Property
    BaseEntity <|-- PropertyImage
    BaseEntity <|-- Amenity
    BaseEntity <|-- Booking
    BaseEntity <|-- DiscountCode
    BaseEntity <|-- Payment
    BaseEntity <|-- Review

    User --> UserRole
    User --> UserLoginProvider
    Booking --> BookingStatus
    Payment --> PaymentMethod
    Payment --> PaymentProvider
    Payment --> PaymentStatus
    DiscountCode --> DiscountType

    User "1" --> "0..*" Property : hosts
    User "1" --> "0..*" Booking : guest
    Property "1" --> "0..*" PropertyImage
    Property "1" --> "0..*" Booking
    DiscountCode "1" --> "0..*" Booking : optional
    Booking "1" --> "1" Payment
    Booking "1" --> "0..1" Review
```
