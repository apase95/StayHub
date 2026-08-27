# Tech Stack

## 1. Backend

| Công nghệ | Phiên bản | Mục đích | Lý do chọn |
| :--- | :--- | :--- | :--- |
| **Java** | 21 (LTS) | Ngôn ngữ lập trình | LTS, hiệu suất cao, bảo mật, cộng đồng lớn. |
| **Spring Boot** | 3.3.2 | Framework chính | Hệ sinh thái phong phú, dễ dàng tích hợp (Security, Data JPA, Mail, Thymeleaf...), hỗ trợ Java 21. |
| **Spring Security** | 6.x | Xác thực & phân quyền | Tích hợp sẵn với Spring Boot, hỗ trợ session-based, BCrypt. |
| **Spring Data JPA** | 3.x | ORM | Giảm boilerplate code, dễ dàng tương tác với PostgreSQL. |
| **Hibernate** | 6.x | JPA Implementation | Mặc định với Spring Data JPA, hỗ trợ tốt tính năng lazy loading, caching. |
| **Flyway** | 10.x | Migration schema | Quản lý version cho database, dễ dàng rollback và đồng bộ giữa các môi trường. |
| **Lombok** | 1.18.x | Giảm boilerplate code (getter/setter, constructor) | Tăng năng suất lập trình, code ngắn gọn. |
| **MapStruct** | 1.5.x | Mapping giữa Entity và DTO | Hiệu suất cao (compile-time), dễ dàng bảo trì khi cấu trúc thay đổi. |
| **Java Mail Sender** | - | Gửi email | Tích hợp sẵn trong Spring Boot, hỗ trợ SMTP. |

## 2. Database

| Công nghệ | Phiên bản | Mục đích | Lý do chọn |
| :--- | :--- | :--- | :--- |
| **PostgreSQL** | 16 | Cơ sở dữ liệu quan hệ | Hỗ trợ tốt cho ứng dụng có giao dịch phức tạp, toàn vẹn dữ liệu, truy vấn nâng cao (range queries, full-text search...). Miễn phí. |

## 3. Frontend

| Công nghệ | Phiên bản | Mục đích | Lý do chọn |
| :--- | :--- | :--- | :--- |
| **Thymeleaf** | 3.x | Template engine | Tích hợp mượt mà với Spring Boot, render server-side, hỗ trợ tốt SEO. |
| **Tailwind CSS** | 3.x | CSS utility-first | Tạo giao diện nhanh, tùy biến, giảm thiểu CSS custom. Phù hợp với thiết kế hệ thống (design system). |
| **Alpine.js** | 3.x | JavaScript framework nhẹ | Tương tác UI đơn giản (dropdown, modal, toggle), không cần React/Angular. |
| **htmx** | 1.x | AJAX và dynamic content | Cho phép gửi request và cập nhật DOM mà không cần reload toàn trang, giảm độ phức tạp của JavaScript. |

## 4. Infrastructure & DevOps

| Công nghệ | Phiên bản | Mục đích | Lý do chọn |
| :--- | :--- | :--- | :--- |
| **Docker** | 24.x | Containerization | Đóng gói ứng dụng và dependencies, dễ dàng triển khai và đồng bộ môi trường giữa dev/staging/production. |
| **Docker Compose** | 2.x | Orchestration đơn giản | Quản lý multi-container (app + db), dễ dàng chạy toàn bộ stack với một lệnh. |
| **Git** | - | Version control | Quản lý source code, làm việc nhóm, tích hợp với GitHub. |
| **GitHub** | - | Remote repository | Lưu trữ, quản lý Pull Request, CI/CD (có thể tích hợp sau). |
| **Maven** | 3.9.x | Build tool | Quản lý dependencies, build, test. Sử dụng Maven wrapper (`mvnw`) để đồng bộ version. |
| **Cloudinary** (optional) | - | Lưu trữ ảnh | Service đám mây cho upload và quản lý ảnh, hỗ trợ resize, optimize. Dùng trong production. |

## 5. Testing

| Công nghệ | Mục đích | Lý do chọn |
| :--- | :--- | :--- |
| **JUnit 5** | Unit testing | Framework test mặc định trong Spring Boot. |
| **Mockito** | Mocking | Tạo mock objects, kiểm tra tương tác giữa các layer. |
| **Testcontainers** | Integration testing với database thật | Chạy container database trong test, đảm bảo test chạy đúng với PostgreSQL thật (không dùng H2). |
| **Spring Boot Test** | Testing web layer, controller, security | Hỗ trợ các annotation như `@WebMvcTest`, `@DataJpaTest`, `@SpringBootTest`. |

## 6. Lý do lựa chọn tổng quát

- **Java + Spring Boot**: Lựa chọn phổ biến cho các ứng dụng enterprise, cộng đồng lớn, tài liệu phong phú, dễ tuyển dụng.
- **PostgreSQL**: Mạnh mẽ, miễn phí, hỗ trợ tốt cho các thao tác phức tạp như kiểm tra trùng lịch booking.
- **Thymeleaf + Tailwind**: Phù hợp với ứng dụng monolith render server-side, tạo UI đẹp và responsive nhanh, hỗ trợ tốt cho SEO.
- **Docker**: Đơn giản hóa việc thiết lập môi trường, đảm bảo nhất quán giữa các thành viên và môi trường.
- **Flyway**: Đảm bảo database schema được quản lý một cách bài bản, tránh sai sót khi deploy.

## 7. Các công nghệ sẽ được cân nhắc

- **Thanh toán thật**: VNPay, MoMo, Stripe...
- **Cache**: Redis (cho rating average, search results).
- **WebSocket / STOMP**: Thông báo real-time.
- **Message Queue**: RabbitMQ hoặc Kafka (cho xử lý bất đồng bộ email, notification).
- **Monitoring**: Prometheus + Grafana.
- **CI/CD**: GitHub Actions.