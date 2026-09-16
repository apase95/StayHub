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
│       ├── db/migration/    V1__create_users.sql, V2__normalize_user_emails.sql, V3__create_properties.sql, ...
│       ├── application.yml
│       ├── application-local.yml
│       └── application-docker.yml
├── .dockerignore / .gitignore / Dockerfile / docker-compose.yml / pom.xml / tailwind.config.js
```
---

# SPRINT 0 — NỀN TẢNG CHUNG

- [x] **TSK-001** `[PM/Setup]` Khởi tạo Git repo + branch `main`/`dev`, add Collaborator cho cả 3 người. *(Estimate: 0.5h · Priority: Urgent)*

- [x] **TSK-002** `[BE_Core]` Khởi tạo `pom.xml` (Spring Boot 3.3.2, Java 21) + `StayHubApplication.java`. *(Estimate: 1h · Priority: Urgent)*

- [x] **TSK-003** `[Infra]` `Dockerfile` (multi-stage Maven build → JRE runtime) + `docker-compose.yml` (Postgres 16). *(Estimate: 1.5h · Priority: Urgent · Blocking)*

- [x] **TSK-004** `[Infra]` `.gitignore`, `.dockerignore`, tách `application.yml` / `application-local.yml` / `application-docker.yml`. *(Estimate: 1h · Priority: Urgent · Blocking)*

- [x] **TSK-005** `[FE_Core]` Setup `tailwind.config.js` build pipeline (npm/CLI), output CSS vào `static/css/`. *(Estimate: 1h · Priority: High)*

- [x] **TSK-006** `[BE_Core]` Package `common/response`: tạo `ApiResponse<T>` chuẩn theo `rules.md` mục 4. *(Estimate: 0.5h · Priority: Urgent · Blocking)*

- [x] **TSK-007** `[BE_Core]` Package `common/exception`: tách `ApiExceptionHandler` (`@RestControllerAdvice`) và `MvcExceptionHandler` (`@ControllerAdvice`) + các exception dùng chung. *(Estimate: 1h · Priority: Urgent · Blocking)*

- [x] **TSK-008** `[BE_Core]` Package `common/validation` + `common/util`: custom validator (vd: check-in phải trước check-out), `DateUtil`, `PriceUtil`. *(Estimate: 1h · Priority: Medium)*

- [x] **TSK-009** `[BE_Core]` `common/entity/BaseEntity` (id, createdAt, updatedAt) — dùng chung cho mọi entity*(Estimate: 0.5h · Priority: Urgent · Blocking)*

- [x] **TSK-010** `[DB]` Migration `V1__create_users.sql`:  bảng users (id, email, password_hash, full_name, phone, role, status, created_at, updated_at) . *(Estimate: 1h · Priority: Urgent · Blocking)*

- [x] **TSK-011** `[FE_Core]` `templates/fragments/navbar.html`, `footer.html` (bản khung, chưa cần hoàn thiện logic login/logout). *(Estimate: 1h · Priority: Medium)*


---

# TRACK A — AUTH · USER · CORE CONFIG · ADMIN

- [x] **TSK-012** `[BE_Config]` `config/SecurityConfig.java`: session-based auth, `PasswordEncoder` (BCrypt), phân quyền theo path (`/host/**` → HOST, `/admin/**` → ADMIN). *(Estimate: 2.5h · Priority: Urgent · Blocking cho Dev B & Dev C)*

- [x] **TSK-013** `[BE_User]` `user/User.java` (entity extends BaseEntity), `user/dto/` (UserResponse, UpdateProfileRequest). *(Estimate: 1h · Priority: Urgent · Blocking)*

- [x] **TSK-014** `[BE_User]` `UserRepository`, `UserService` (đăng ký, tìm theo email, đổi mật khẩu). *(Estimate: 1.5h · Priority: Urgent)*

- [x] **TSK-015** `[BE_Auth]` `UserPrincipal` + `CustomUserDetailsService` tích hợp Spring Security; `AuthService` là boundary cho đăng ký tài khoản. *(Estimate: 1.5h · Priority: Urgent · Blocking)*

- [x] **TSK-016** `[BE_Auth]` `AuthController`: `GET/POST /register`, `GET /login`; Spring Security xử lý `POST /login`, `POST /logout`; có `RegisterRequest`. *(Estimate: 2h · Priority: Urgent)*

- [x] **TSK-017** `[FE_Auth]` `templates/auth/login.html`, `register.html`. *(Estimate: 2h · Priority: High)*

- [x] **TSK-018** `[FE_Core]` Hoàn thiện `fragments/navbar.html` với `sec:authorize` (hiện Profile/My Booking/Logout khi đã login, ẩn khi chưa). Hiện Account/Profile vẫn là placeholder. *(Estimate: 1h · Priority: Medium)*

- [x] **TSK-019** `[DB]` Xác nhận không cần migration quyền hạn riêng: role, status và constraints đã có trong `V1__create_users.sql`. *(Estimate: 0.5h · Priority: Low)*

- [x] **TSK-020** `[BE_Admin]` `admin/AdminController.java`, `AdminService.java`: dashboard tổng quan với dữ liệu thật (tổng users, active hosts, bookings, revenue). Hiện bookings và revenue vẫn là mock. *(Estimate: 3h · Priority: Medium)*

- [x] **TSK-021** `[FE_Admin]` `templates/admin/bookings.html`: Stats Cards + bảng booking toàn hệ thống với search, filter status và pagination. Hiện bảng booking vẫn là placeholder. *(Estimate: 2.5h · Priority: Medium)*

- [x] **TSK-022** `[BE_Admin]` Quản lý user/host từ admin (khoá tài khoản, đổi role). Lock/unlock đã có; role-change chưa triển khai. *(Estimate: 2h · Priority: Low)*

## FOUNDATION HARDENING

- [x] **TSK-053** `[Testing]` Thêm JUnit/Mockito/Spring Security Test/Testcontainers và PostgreSQL 16 integration-test foundation.
- [x] **TSK-054** `[Infra]` Chuẩn hóa config local/docker bằng environment variables, bỏ credential khỏi source và hoàn thiện full-stack Compose.
- [x] **TSK-055** `[Security]` Bật CSRF cho session API; chuẩn hóa JSON 401/403 và CSRF response cho `/api/**`.
- [x] **TSK-056** `[BE_User]` Chuẩn hóa email, xử lý duplicate race, thêm `V2__normalize_user_emails.sql` và DTO boundary cho Admin view.
- [x] **TSK-057** `[BE_Config]` Bootstrap Admin opt-in bằng environment variables, không tự nâng quyền account tồn tại.
- [x] **TSK-058** `[BE_Core]` Tách exception handling cho REST API và Thymeleaf MVC.

---

# TRACK B — PROPERTY · SEARCH · HOST · STORAGE

- [x] **TSK-023** `[DB]` Migration `V3__create_properties.sql`: bảng `properties` (bao gồm `cleaning_fee`, `rating_avg`) + `V4__create_property_images.sql` + `V5__create_amenities.sql` + `V6__create_property_amenities.sql`. `V2` đã dành cho chuẩn hóa email user. *(Estimate: 2h · Priority: Urgent)*

- [x] **TSK-024** `[BE_Property]` `Property`, `PropertyImage`, `Amenity`, enums và Property/Image/Amenity DTOs. *(Estimate: 2h · Priority: Urgent)*

- [x] **TSK-025** `[BE_Property]` `PropertyRepository`, `PropertyService`, `PropertyMapper` (entity ↔ dto, tránh trả Entity trực tiếp theo `rules.md`). *(Estimate: 2h · Priority: Urgent)*

- [x] **TSK-026** `[BE_Storage]` `storage/StorageService.java` (interface) + `LocalStorageService.java` (lưu đĩa cục bộ trước, dùng khi dev). *(Estimate: 2h · Priority: High)*

- [x] **TSK-027** `[BE_Storage]` `storage/CloudinaryStorageService.java` + `config/StorageConfig.java` (bean chọn implementation theo `app.upload.use-cloudinary`). *(Estimate: 2h · Priority: Medium)*

- [x] **TSK-028** `[BE_Property]` `HomeController` phục vụ `GET /`; `PropertyController` và REST controller phục vụ public detail, chỉ trả listing `ACTIVE`. *(Estimate: 1.5h · Priority: Urgent)*

- [x] **TSK-029** `[FE_Home]` `templates/home/index.html`: search box (Where/Check-in/Check-out/Guests theo UI Design System), popular destinations, featured properties, popular categories — responsive (search box chồng dọc trên mobile). *(Estimate: 2.5h · Priority: Urgent)*

- [x] **TSK-030** `[BE_Search]` `search/SearchController.java`, `SearchService.java`, `SearchRepository.java`, `search/dto/SearchCriteria`: `GET /properties?location=&checkIn=&checkOut=&guests=` + filter (price, type, bedrooms, amenities, rating) + sort + pagination. *(Estimate: 3.5h · Priority: Urgent)*

- [x] **TSK-031** `[FE_Property]` `templates/property/search-results.html` filter sidebar (Price Range slider, Property Type checkboxes, Bedrooms/Beds/Bathrooms, Amenities, Rating) + property list (Property Card component) + sort dropdown + pagination — responsive (filter chuyển thành dropdown trên mobile) *(Estimate: 3h · Priority: Urgent)*

- [x] **TSK-032** `[FE_Property]` `templates/property/property-detail.html`: gallery ảnh (main + thumbnail grid, responsive → carousel trên mobile), description, amenities, reviews (hiển thị rating + comment), availability calendar, price box (sticky trên desktop, bottom bar trên mobile) — theo UI Design System. *(Estimate: 3h · Priority: Urgent)*

- [x] **TSK-033** `[BE_Host]` Host MVC + REST CRUD có ownership, archive listing, quản lý ảnh (upload/delete/cover/order) qua `StorageService`; mutation ảnh được serialize và có rollback/after-commit cleanup. *(Estimate: 3h · Priority: High)*

- [x] **TSK-034** `[FE_Host]` `templates/host/dashboard.html`: phần danh sách property/Add/Edit/Archive đã hoàn thành; danh sách booking request và Stats Cards chờ dữ liệu từ Track C. *(Estimate: 3h · Priority: High · phụ thuộc TSK-041 của Dev C)*

---

# TRACK C — BOOKING · PAYMENT · REVIEW · NOTIFICATION

- [ ] **TSK-035** `[DB]` Migration `V7__create_bookings.sql`: bảng `bookings` (id, property_id, guest_id, check_in_date, check_out_date, guests, nightly_price, cleaning_fee, service_fee, total_price, status, created_at, updated_at, cancelled_at) — theo thiết kế database (có snapshot price). *(Estimate: 1.5h · Priority: Urgent · phụ thuộc TSK-023 của Dev B)*

- [ ] **TSK-036** `[BE_Booking]` `booking/Booking.java`, `booking/BookingStatus.java` (`PENDING/CONFIRMED/CANCELLED/REJECTED/COMPLETED`), `booking/dto/`. *(Estimate: 1.5h · Priority: Urgent)*

- [ ] **TSK-037** `[BE_Booking]` `BookingRepository` (custom methods: findConflictingBookings, findByGuestId, findByPropertyIdAndStatus, findBookingRequestsByHost), `BookingService` (create booking, check overlap ngày), BookingPriceService (tính `price × nights + cleaning_fee + service_fee`). *(Estimate: 3h · Priority: Urgent)*

- [ ] **TSK-038** `[BE_Booking]` AJAX `POST /api/v1/bookings/check-availability` — dùng `ApiResponse<T>`, `errorCode = "ERR_ROOM_NOT_AVAILABLE"` nếu trùng ngày. *(Estimate: 1.5h · Priority: Urgent)*

- [ ] **TSK-039** `[BE_Booking]` `BookingController.java`: `GET /properties/{id}/book` → `booking.html`, `POST /bookings` (tạo booking + gọi Payment). *(Estimate: 2h · Priority: Urgent)*

- [ ] **TSK-040** `[FE_Booking]` `templates/booking/booking.html` + `payment.html`: Your booking, Guest details, Price summary, nút "Confirm & Pay" theo `flow.md`. *(Estimate: 3h · Priority: Urgent)*

- [ ] **TSK-041** `[BE_Payment]` `payment/Payment.java`, `PaymentStatus.java`, `PaymentMethod.java` + Migration `V8__create_payments.sql`. *(Estimate: 1h · Priority: Urgent)*

- [ ] **TSK-042** `[BE_Payment]` `payment/MockPaymentService.java` (implements `PaymentService`): set `payment_method = MOCK`, `status = SUCCESS` ngay lập tức. *(Estimate: 1.5h · Priority: Urgent)*

- [ ] **TSK-043** `[BE_Booking]` Flow `Confirm & Pay → Payment Success → Create Booking (PENDING)` đúng sơ đồ `flow.md`; expose API cho Dev B lấy "booking requests theo host" (phục vụ TSK-034). *(Estimate: 2h · Priority: Urgent · Blocking cho Dev B)*

- [ ] **TSK-044** `[BE_Booking]` API accept/reject cho host (`PENDING → CONFIRMED/REJECTED`) + kiểm tra state transition hợp lệ, cancel cho guest (`PENDING/CONFIRMED → CANCELLED`). *(Estimate: 2h · Priority: Urgent)*

- [ ] **TSK-045** `[FE_Booking]` `templates/booking/booking-detail.html` + trang "My Bookings" (tabs Upcoming/Pending/Completed/Cancelled) theo `flow.md`. *(Estimate: 3h · Priority: High)*

- [ ] **TSK-046** `[DB]` Migration `V9__create_reviews.sql`: bảng `reviews` (id, booking_id UNIQUE, property_id, guest_id, rating SMALLINT CHECK 1-5, comment, created_at, updated_at) — theo thiết kế database. *(Estimate: 0.5h · Priority: Medium)*

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
