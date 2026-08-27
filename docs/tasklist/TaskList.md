# StayHub — Task List

## Target Project Structure

```txt
StayHub/
├── .github/
├── docs/                        
├── src/main/
│   ├── java/com/stayhub/
│   │   ├── StayHubApplication.java
│   │   ├── common/          {exception, response, validation, util}
│   │   ├── config/          SecurityConfig, MailConfig, StorageConfig
│   │   ├── auth/            AuthController, AuthService, UserPrincipal, dto/
│   │   ├── user/            User, UserRepository, UserService, dto/
│   │   ├── property/        Property, PropertyController/Service/Repository/Mapper, dto/
│   │   ├── search/          SearchController, SearchService, SearchRepository, dto/
│   │   ├── booking/         Booking, BookingStatus, BookingController/Service/Repository, BookingPriceService, dto/
│   │   ├── payment/         Payment, PaymentStatus, PaymentMethod, PaymentService, MockPaymentService, dto/
│   │   ├── review/          Review, ReviewController, ReviewService, ReviewRepository
│   │   ├── host/            HostController, HostService, dto/
│   │   ├── admin/           AdminController, AdminService
│   │   ├── notification/    NotificationService, EmailNotificationService
│   │   └── storage/         StorageService, LocalStorageService, CloudinaryStorageService
│   └── resources/
│       ├── templates/
│       │   ├── fragments/   navbar.html, footer.html
│       │   ├── home/        index.html
│       │   ├── property/    search-results.html, property-detail.html
│       │   ├── booking/     booking.html, payment.html, booking-detail.html
│       │   ├── auth/        login.html, register.html
│       │   ├── host/        dashboard.html
│       │   └── admin/       bookings.html
│       ├── static/{css,js,images,favicon.ico}
│       ├── db/migration/    V1__create_users.sql, V2__create_properties.sql, V3__create_bookings.sql, ...
│       ├── application.yml
│       ├── application-local.yml
│       └── application-docker.yml
├── .dockerignore / .gitignore / Dockerfile / docker-compose.yml / pom.xml / tailwind.config.js
```
---

# SPRINT 0 — NỀN TẢNG CHUNG

- [x] **TSK-001** `[PM/Setup]` Khởi tạo Git repo + branch `main`/`dev`, add Collaborator cho cả 3 người. *(Estimate: 0.5h · Priority: Urgent)*

- [x] **TSK-002** `[BE_Core]` Khởi tạo `pom.xml` (Spring Boot 3.3.2, Java 21) + `StayHubApplication.java`. *(Estimate: 1h · Priority: Urgent)*

- [ ] **TSK-003** `[Infra]` `Dockerfile` (multi-stage Maven build → JRE runtime) + `docker-compose.yml` (Postgres 16). *(Estimate: 1.5h · Priority: Urgent · Blocking)*

- [ ] **TSK-004** `[Infra]` `.gitignore`, `.dockerignore`, tách `application.yml` / `application-local.yml` / `application-docker.yml`. *(Estimate: 1h · Priority: Urgent · Blocking)*

- [ ] **TSK-005** `[FE_Core]` Setup `tailwind.config.js` build pipeline (npm/CLI), output CSS vào `static/css/`. *(Estimate: 1h · Priority: High)*

- [ ] **TSK-006** `[BE_Core]` Package `common/response`: tạo `ApiResponse<T>` chuẩn theo `rules.md` mục 4. *(Estimate: 0.5h · Priority: Urgent · Blocking)*

- [ ] **TSK-007** `[BE_Core]` Package `common/exception`: `GlobalExceptionHandler` (`@RestControllerAdvice`) + các exception dùng chung (`ResourceNotFoundException`, `BusinessException`, `InvalidStateTransitionException`...). *(Estimate: 1h · Priority: Urgent · Blocking)*

- [ ] **TSK-008** `[BE_Core]` Package `common/validation` + `common/util`: custom validator (vd: check-in phải trước check-out), `DateUtil`, `PriceUtil`. *(Estimate: 1h · Priority: Medium)*

- [ ] **TSK-009** `[BE_Core]` `common/entity/BaseEntity` (id, createdAt, updatedAt) — dùng chung cho mọi entity*(Estimate: 0.5h · Priority: Urgent · Blocking)*

- [ ] **TSK-010** `[DB]` Migration `V1__create_users.sql`:  bảng users (id, email, password_hash, full_name, phone, role, status, created_at, updated_at) . *(Estimate: 1h · Priority: Urgent · Blocking)*

- [ ] **TSK-011** `[FE_Core]` `templates/fragments/navbar.html`, `footer.html` (bản khung, chưa cần hoàn thiện logic login/logout). *(Estimate: 1h · Priority: Medium)*


---

# TRACK A — AUTH · USER · CORE CONFIG · ADMIN

- [ ] **TSK-012** `[BE_Config]` `config/SecurityConfig.java`: session-based auth, `PasswordEncoder` (BCrypt), phân quyền theo path (`/host/**` → HOST, `/admin/**` → ADMIN). *(Estimate: 2.5h · Priority: Urgent · Blocking cho Dev B & Dev C)*

- [ ] **TSK-013** `[BE_User]` `user/User.java` (entity extends BaseEntity), `user/dto/` (UserResponse, UpdateProfileRequest). *(Estimate: 1h · Priority: Urgent · Blocking)*

- [ ] **TSK-014** `[BE_User]` `UserRepository`, `UserService` (đăng ký, tìm theo email, đổi mật khẩu). *(Estimate: 1.5h · Priority: Urgent)*

- [ ] **TSK-015** `[BE_Auth]` `auth/UserPrincipal.java` (implements `UserDetails`) + `AuthService` (load user, xác thực). *(Estimate: 1.5h · Priority: Urgent · Blocking)*

- [ ] **TSK-016** `[BE_Auth]` `auth/AuthController.java`: `GET/POST /register`, `GET /login`, `POST /logout` + `auth/dto/RegisterRequest`. *(Estimate: 2h · Priority: Urgent)*

- [ ] **TSK-017** `[FE_Auth]` `templates/auth/login.html`, `register.html`. *(Estimate: 2h · Priority: High)*

- [ ] **TSK-018** `[FE_Core]` Hoàn thiện `fragments/navbar.html` với `sec:authorize` (hiện Profile/My Booking/Logout khi đã login, ẩn khi chưa). *(Estimate: 1h · Priority: Medium)*

- [ ] **TSK-019** `[DB]` Migration cho quyền hạn nếu cần (vd: thêm cột `status` cho user bị khoá) — tuỳ phát sinh. *(Estimate: 0.5h · Priority: Low)*

- [ ] **TSK-020** `[BE_Admin]` `admin/AdminController.java`, `AdminService.java`: dashboard tổng quan (tổng users, hosts, bookings, revenue). *(Estimate: 3h · Priority: Medium)*

- [ ] **TSK-021** `[FE_Admin]` `templates/admin/bookings.html`: hiển thị Stats Big Number Cards (4 cards: Users, Hosts, Bookings, Revenue) + `templates/admin/bookings.html`: bảng quản lý booking toàn hệ thống (search, filter theo status) *(Estimate: 2.5h · Priority: Medium)*

- [ ] **TSK-022** `[BE_Admin]` Quản lý user/host từ admin (khoá tài khoản, đổi role) — mở rộng `AdminController`. *(Estimate: 2h · Priority: Low)*

---

# TRACK B — PROPERTY · SEARCH · HOST · STORAGE

- [ ] **TSK-023** `[DB]` Migration `V2__create_properties.sql`: bảng `properties` (id, host_id, title, description, address, city, price_per_night, max_guests, bedrooms, beds, bathrooms, property_type, status, created_at, updated_at) + `V3__create_property_images.sql` + `V4__create_amenities.sql` + `V5__create_property_amenities.sql`. *(Estimate: 2h · Priority: Urgent)*

- [ ] **TSK-024** `[BE_Property]` `property/Property.java`, `property/PropertyImage.java`, `property/PropertyStatus.java`, `property/PropertyType.java` + `property/dto/` (PropertyResponse, PropertyCreateRequest, PropertyUpdateRequest, PropertySummary, PropertyImageRequest). *(Estimate: 2h · Priority: Urgent)*

- [ ] **TSK-025** `[BE_Property]` `PropertyRepository`, `PropertyService`, `PropertyMapper` (entity ↔ dto, tránh trả Entity trực tiếp theo `rules.md`). *(Estimate: 2h · Priority: Urgent)*

- [ ] **TSK-026** `[BE_Storage]` `storage/StorageService.java` (interface) + `LocalStorageService.java` (lưu đĩa cục bộ trước, dùng khi dev). *(Estimate: 2h · Priority: High)*

- [ ] **TSK-027** `[BE_Storage]` `storage/CloudinaryStorageService.java` + `config/StorageConfig.java` (bean chọn implementation theo `app.upload.use-cloudinary`). *(Estimate: 2h · Priority: Medium)*

- [ ] **TSK-028** `[BE_Property]` `PropertyController.java`: `GET /` (home), `GET /properties/{id}` (property detail). *(Estimate: 1.5h · Priority: Urgent)*

- [ ] **TSK-029** `[FE_Home]` `templates/home/index.html`: search box (Where/Check-in/Check-out/Guests theo UI Design System), popular destinations, featured properties, popular categories — responsive (search box chồng dọc trên mobile). *(Estimate: 2.5h · Priority: Urgent)*

- [ ] **TSK-030** `[BE_Search]` `search/SearchController.java`, `SearchService.java`, `SearchRepository.java`, `search/dto/SearchCriteria`: `GET /properties?location=&checkIn=&checkOut=&guests=` + filter (price, type, bedrooms, amenities, rating) + sort + pagination. *(Estimate: 3.5h · Priority: Urgent)*

- [ ] **TSK-031** `[FE_Property]` `templates/property/search-results.html` filter sidebar (Price Range slider, Property Type checkboxes, Bedrooms/Beds/Bathrooms, Amenities, Rating) + property list (Property Card component) + sort dropdown + pagination — responsive (filter chuyển thành dropdown trên mobile) *(Estimate: 3h · Priority: Urgent)*

- [ ] **TSK-032** `[FE_Property]` `templates/property/property-detail.html`: gallery ảnh (main + thumbnail grid, responsive → carousel trên mobile), description, amenities, reviews (hiển thị rating + comment), availability calendar, price box (sticky trên desktop, bottom bar trên mobile) — theo UI Design System. *(Estimate: 3h · Priority: Urgent)*

- [ ] **TSK-033** `[BE_Host]` `host/HostController.java`, `HostService.java`, `host/dto/`: CRUD property của host (`GET /host/properties`, `GET/POST /host/properties/new`, `GET/PUT /host/properties/{id}`, `DELETE /host/properties/{id}`), upload ảnh qua `StorageService`.  *(Estimate: 3h · Priority: High)*

- [ ] **TSK-034** `[FE_Host]` `templates/host/dashboard.html`: danh sách property của host (với nút Add Property, Edit, Delete) + danh sách booking request (dữ liệu từ Track C) — responsive với Stats Cards. *(Estimate: 3h · Priority: High · phụ thuộc TSK-041 của Dev C)*

---

# TRACK C — BOOKING · PAYMENT · REVIEW · NOTIFICATION

- [ ] **TSK-035** `[DB]` Migration `V3__create_bookings.sql`: bảng `bookings` (id, property_id, guest_id, check_in_date, check_out_date, guests, nightly_price, cleaning_fee, service_fee, total_price, status, created_at, updated_at, cancelled_at) — theo thiết kế database (có snapshot price). *(Estimate: 1.5h · Priority: Urgent · phụ thuộc TSK-023 của Dev B)*

- [ ] **TSK-036** `[BE_Booking]` `booking/Booking.java`, `booking/BookingStatus.java` (`PENDING/CONFIRMED/CANCELLED/REJECTED/COMPLETED`), `booking/dto/`. *(Estimate: 1.5h · Priority: Urgent)*

- [ ] **TSK-037** `[BE_Booking]` `BookingRepository` (custom methods: findConflictingBookings, findByGuestId, findByPropertyIdAndStatus, findBookingRequestsByHost), `BookingService` (create booking, check overlap ngày), BookingPriceService (tính `price × nights + cleaning_fee + service_fee`). *(Estimate: 3h · Priority: Urgent)*

- [ ] **TSK-038** `[BE_Booking]` AJAX `POST /api/v1/bookings/check-availability` — dùng `ApiResponse<T>`, `errorCode = "ERR_ROOM_NOT_AVAILABLE"` nếu trùng ngày. *(Estimate: 1.5h · Priority: Urgent)*

- [ ] **TSK-039** `[BE_Booking]` `BookingController.java`: `GET /properties/{id}/book` → `booking.html`, `POST /bookings` (tạo booking + gọi Payment). *(Estimate: 2h · Priority: Urgent)*

- [ ] **TSK-040** `[FE_Booking]` `templates/booking/booking.html` + `payment.html`: Your booking, Guest details, Price summary, nút "Confirm & Pay" theo `flow.md`. *(Estimate: 3h · Priority: Urgent)*

- [ ] **TSK-041** `[BE_Payment]` `payment/Payment.java`, `PaymentStatus.java`, `PaymentMethod.java` + Migration `V4__create_payments.sql`. *(Estimate: 1h · Priority: Urgent)*

- [ ] **TSK-042** `[BE_Payment]` `payment/MockPaymentService.java` (implements `PaymentService`): set `payment_method = MOCK`, `status = SUCCESS` ngay lập tức. *(Estimate: 1.5h · Priority: Urgent)*

- [ ] **TSK-043** `[BE_Booking]` Flow `Confirm & Pay → Payment Success → Create Booking (PENDING)` đúng sơ đồ `flow.md`; expose API cho Dev B lấy "booking requests theo host" (phục vụ TSK-034). *(Estimate: 2h · Priority: Urgent · Blocking cho Dev B)*

- [ ] **TSK-044** `[BE_Booking]` API accept/reject cho host (`PENDING → CONFIRMED/REJECTED`) + kiểm tra state transition hợp lệ, cancel cho guest (`PENDING/CONFIRMED → CANCELLED`). *(Estimate: 2h · Priority: Urgent)*

- [ ] **TSK-045** `[FE_Booking]` `templates/booking/booking-detail.html` + trang "My Bookings" (tabs Upcoming/Pending/Completed/Cancelled) theo `flow.md`. *(Estimate: 3h · Priority: High)*

- [ ] **TSK-046** `[DB]` Migration `V8__create_reviews.sql`: bảng `reviews` (id, booking_id UNIQUE, property_id, guest_id, rating SMALLINT CHECK 1-5, comment, created_at, updated_at) — theo thiết kế database.. *(Estimate: 0.5h · Priority: Medium)*

- [ ] **TSK-047** `[BE_Review]` `review/Review.java`, `ReviewController.java`, `ReviewService.java`, `ReviewRepository.java`: cho phép review khi booking `COMPLETED`, cập nhật `rating_avg` của property sau khi review mới. *(Estimate: 2.5h · Priority: Medium)*

- [ ] **TSK-048** `[BE_Notification]` `config/MailConfig.java` + `notification/NotificationService.java` (interface) + `EmailNotificationService.java`: gửi mail khi booking đổi trạng thái (CONFIRMED/REJECTED/CANCELLED) bao gồm booking info template. *(Estimate: 2.5h · Priority: Medium)*

---

# INTEGRATION & DEPLOY

- [ ] **TSK-049** `[Testing]` Test end-to-end theo `flow.md`: Search → Property Detail → Check Availability → Booking → Mock Payment → PENDING → Host Accept → CONFIRMED → My Bookings → Review. *(Estimate: 2h · Priority: Urgent)*

- [ ] **TSK-050** `[Infra]` Verify `docker compose up -d --build` chạy full stack (app + db) không lỗi, dùng `application-docker.yml`. *(Estimate: 1.5h · Priority: High)*

- [ ] **TSK-051** `[Testing]` Review chéo giữa 3 track: kiểm tra không có entity nào bị trả trực tiếp ra view/API (đúng `rules.md` mục 5), không có `catch (Exception e) {}` rỗng. *(Estimate: 1.5h · Priority: High)*

- [ ] **TSK-052** `[Docs]` Cập nhật README + screenshots, đánh dấu lại task đã hoàn thành trong `task-list.md`. *(Estimate: 1h · Priority: Medium)*

---

# Project Done Checklist

- [ ] Chạy được app bằng `./mvnw spring-boot:run -Dspring-boot.run.profiles=local` sau `docker compose up -d db`.
- [ ] Đăng ký / đăng nhập / phân quyền GUEST-HOST-ADMIN hoạt động (`SecurityConfig`).
- [ ] Search property theo địa điểm + ngày + số khách, có filter/sort/pagination.
- [ ] Xem property detail, check availability theo ngày trước khi đặt.
- [ ] Đặt phòng → mock payment SUCCESS → booking status PENDING.
- [ ] Host xem được booking request, accept/reject.
- [ ] User xem My Bookings, cancel được booking.
- [ ] Sau COMPLETED, user viết được review, hiển thị trên property detail.
- [ ] Admin xem được dashboard tổng quan + danh sách booking toàn hệ thống.
- [ ] Nhận được email khi booking đổi trạng thái.
- [ ] `docker compose up -d --build` chạy được toàn bộ stack.

---

# Post-MVP (Optional)

- [ ] Tích hợp thanh toán thật VNPay/Momo (thay `MockPaymentService`).
- [ ] Wishlist (lưu property yêu thích).
- [ ] Cache rating trung bình thay vì tính lại mỗi lần load property.
- [ ] Export báo cáo (CSV/PDF) cho Admin.
- [ ] Notification real-time (websocket) thay vì chỉ email.
- [ ] Rate limiting cho `auth` (chống brute-force login).

---