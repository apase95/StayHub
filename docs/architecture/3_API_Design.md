# API Design

## 1. Nguyên tắc chung

StayHub sử dụng **RESTful API** cho các endpoint trả về dữ liệu dạng JSON (dùng cho AJAX, Alpine.js, htmx). Các trang chính sử dụng **Thymeleaf** và được phục vụ qua các controller trả về template.

- **Endpoint cho API**: Luôn có tiền tố `/api/v1/`.
- **Endpoint cho View**: Không có tiền tố `/api`, trả về tên template.

## 2. Chuẩn response

Tất cả API response đều được bọc trong đối tượng `ApiResponse<T>`:

```json
{
  "success": true,
  "message": "Thành công",
  "data": { ... },
  "errorCode": null
}
```

- `success`: `true` hoặc `false`.
- `message`: Thông báo dành cho người dùng (có thể hiển thị lên UI).
- `data`: Dữ liệu trả về (có thể là object, array, null).
- `errorCode`: Mã lỗi nội bộ (nếu có), ví dụ `"ERR_ROOM_NOT_AVAILABLE"`.

## 3. HTTP Status codes

| Status | Mô tả |
| :--- | :--- |
| `200 OK` | Thành công (GET, PUT, DELETE) |
| `201 Created` | Tạo mới thành công (POST) |
| `400 Bad Request` | Dữ liệu gửi lên không hợp lệ |
| `401 Unauthorized` | Chưa đăng nhập |
| `403 Forbidden` | Không có quyền truy cập |
| `404 Not Found` | Không tìm thấy tài nguyên |
| `409 Conflict` | Xung đột dữ liệu (ví dụ: phòng đã được đặt) |
| `500 Internal Server Error` | Lỗi server |

## 4. Danh sách API chính

### 4.1. Authentication & User

| Method | Endpoint | Mô tả | Yêu cầu |
| :--- | :--- | :--- | :--- |
| `GET` | `/login` | Trang đăng nhập | - |
| `POST` | `/login` | Xử lý đăng nhập | `username`, `password` |
| `GET` | `/register` | Trang đăng ký | - |
| `POST` | `/register` | Đăng ký tài khoản | `email`, `password`, `fullName`, ... |
| `POST` | `/logout` | Đăng xuất | - |

### 4.2. Property & Search

| Method | Endpoint | Mô tả | Yêu cầu |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Trang chủ | - |
| `GET` | `/properties` | Trang kết quả tìm kiếm | Query params: `location`, `checkIn`, `checkOut`, `guests`, `page`, `sort`... |
| `GET` | `/properties/{id}` | Trang chi tiết property | Path: `id` |
| `GET` | `/api/v1/properties` | API tìm kiếm trả về JSON | Tương tự query params + filter (price, type, amenities...) |
| `GET` | `/api/v1/properties/{id}` | API lấy chi tiết property (JSON) | Path: `id` |

### 4.3. Booking

| Method | Endpoint | Mô tả | Yêu cầu |
| :--- | :--- | :--- | :--- |
| `GET` | `/properties/{id}/book` | Trang đặt phòng | Path: `propertyId`, query: `checkIn`, `checkOut`, `guests` |
| `POST` | `/bookings` | Tạo booking mới | Body: `propertyId`, `checkIn`, `checkOut`, `guests`, `guestInfo`... |
| `GET` | `/my-bookings` | Trang danh sách booking của tôi | - |
| `GET` | `/bookings/{id}` | Trang chi tiết booking | Path: `id` |
| `POST` | `/api/v1/bookings/check-availability` | Kiểm tra khả dụng (AJAX) | Body: `propertyId`, `checkIn`, `checkOut` |
| `POST` | `/api/v1/bookings/{id}/cancel` | Hủy booking | Path: `id` |

### 4.4. Host

| Method | Endpoint | Mô tả | Yêu cầu |
| :--- | :--- | :--- | :--- |
| `GET` | `/host/dashboard` | Trang dashboard của Host | - |
| `GET` | `/host/properties` | Danh sách property của host | - |
| `GET` | `/host/properties/new` | Form tạo property mới | - |
| `POST` | `/host/properties` | Tạo property mới | Body form |
| `GET` | `/host/properties/{id}/edit` | Form chỉnh sửa property | Path: `id` |
| `PUT` | `/host/properties/{id}` | Cập nhật property | Path: `id`, Body |
| `DELETE` | `/host/properties/{id}` | Xóa property | Path: `id` |
| `POST` | `/api/v1/host/bookings/{id}/accept` | Host chấp nhận booking | Path: `id` |
| `POST` | `/api/v1/host/bookings/{id}/reject` | Host từ chối booking | Path: `id` |

### 4.5. Admin

| Method | Endpoint | Mô tả | Yêu cầu |
| :--- | :--- | :--- | :--- |
| `GET` | `/admin/dashboard` | Dashboard admin | - |
| `GET` | `/admin/bookings` | Quản lý booking toàn hệ thống | - |
| `GET` | `/api/v1/admin/stats` | Thống kê tổng quan | - |
| `POST` | `/api/v1/admin/users/{id}/lock` | Khóa tài khoản người dùng | Path: `id` |
| `POST` | `/api/v1/admin/users/{id}/unlock` | Mở khóa tài khoản | Path: `id` |

### 4.6. Review

| Method | Endpoint | Mô tả | Yêu cầu |
| :--- | :--- | :--- | :--- |
| `GET` | `/bookings/{id}/review` | Trang viết review | Path: `id` |
| `POST` | `/reviews` | Gửi review | Body: `bookingId`, `rating`, `comment` |

### 4.7. Payment (không có endpoint riêng cho thanh toán MVP, được xử lý trong booking)

## 5. Ví dụ request/response

### 5.1. Kiểm tra khả dụng

**Request:**
```
POST /api/v1/bookings/check-availability
Content-Type: application/json

{
  "propertyId": 1,
  "checkIn": "2026-09-22",
  "checkOut": "2026-09-25"
}
```

**Response thành công:**
```json
{
  "success": true,
  "message": "Available",
  "data": { "available": true },
  "errorCode": null
}
```

**Response thất bại (xung đột):**
```json
{
  "success": false,
  "message": "Property is not available for the selected dates.",
  "data": {
    "available": false,
    "conflictingDates": [
      { "checkIn": "2026-09-20", "checkOut": "2026-09-23" }
    ]
  },
  "errorCode": "ERR_ROOM_NOT_AVAILABLE"
}
```

### 5.2. Tạo booking

**Request:**
```
POST /bookings
Content-Type: application/x-www-form-urlencoded (hoặc JSON)

propertyId=1&checkIn=2026-09-22&checkOut=2026-09-25&guests=2
```

**Response (redirect đến trang booking detail hoặc my bookings):**
- Nếu thành công, redirect `302` đến `/my-bookings`.
- Nếu thất bại, hiển thị lỗi.

### 5.3. Host chấp nhận booking (AJAX)

**Request:**
```
POST /api/v1/host/bookings/5/accept
```

**Response thành công:**
```json
{
  "success": true,
  "message": "Booking accepted",
  "data": null,
  "errorCode": null
}
```

**Response thất bại (không hợp lệ state):**
```json
{
  "success": false,
  "message": "Booking is not in PENDING state",
  "data": null,
  "errorCode": "ERR_INVALID_STATE"
}
```

## 6. Authentication và Authorization

- Sử dụng **session-based authentication** (Spring Security).
- Các endpoint `/api/v1/**` yêu cầu xác thực trừ những trường hợp đặc biệt (ví dụ: check availability có thể mở nếu không yêu cầu login?).
- Phân quyền:
  - `/host/**` và `/api/v1/host/**` chỉ dành cho `HOST` hoặc `ADMIN`.
  - `/admin/**` và `/api/v1/admin/**` chỉ dành cho `ADMIN`.
  - `/my-bookings`, `/bookings/**` yêu cầu đăng nhập (bất kỳ role nào, nhưng sẽ kiểm tra ownership).
- Sử dụng `@PreAuthorize` hoặc cấu hình trong `SecurityConfig` để enforce.

## 7. Error handling

Tất cả lỗi (trừ lỗi validation cơ bản) được xử lý tập trung trong `GlobalExceptionHandler` (thuộc package `common/exception`).

Các custom exception:
- `ResourceNotFoundException`: 404
- `BusinessException`: 409 hoặc 400
- `InvalidStateTransitionException`: 409
- `AuthenticationException`: 401
- `AccessDeniedException`: 403

## 8. Tài liệu liên quan

- [Quy chuẩn API trong Rules.md](../contributors/Rules.md)
- [Luồng nghiệp vụ sơ bộ](0_DemoSystem.md)