# Graph Report - StayHub  (2026-09-18)

## Corpus Check
- 160 files · ~45,205 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 11 file(s) not represented in the graph (top: (none) 6, .css 2, .example 1)

## Summary
- 1135 nodes · 3432 edges · 69 communities (44 shown, 25 thin omitted)
- Extraction: 93% EXTRACTED · 7% INFERRED · 0% AMBIGUOUS · INFERRED: 233 edges (avg confidence: 0.81)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `ffced9c9`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- lombok.RequiredArgsConstructor
- User
- org.springframework.stereotype.Component
- PropertyServiceImpl
- PropertyRepository
- org.springframework.transaction.annotation.Transactional
- localdate
- properties
- CustomUserDetailsService.java
- AuthServiceImplTest.java
- RegisterRequest
- package.json
- BookingResponse
- StayHub Application Configuration
- BusinessException
- org.junit.jupiter.api.Test
- BookingWebIntegrationTest.java
- UserRepository
- 0_DatabaseDesignDemo.md
- StayHub Marketplace Platform
- UserLoginProvider
- AuthServiceImpl.java
- AdminBootstrapRunner.java
- mvnw
- PropertyWebIntegrationTest
- PropertySummary
- Test Application Configuration
- 3. Relational Model
- .bookingStatusChanged
- PostgreSqlIntegrationTest
- EmailTemplateRenderer
- bookings Table
- org.junit.jupiter.api.AfterEach
- graphify.js
- StayhubApplication.java
- DashboardStatsResponse
- PriceUtil
- StorageConfig.java
- Guest Booking Lifecycle
- UI/UX Design System
- opencode.json
- Admin User Management
- Feature Based Monolith
- Availability Calendar Grid
- Graphify Project Knowledge Graph Rules
- Email Service
- Google OAuth2
- Flyway Migration Management
- Thymeleaf Tailwind Alpine htmx Frontend
- Property Amenities
- Property Images
- com.stayhub:stayhub-app
- Booking Form
- ReviewServiceIntegrationTest
- 13. Suggested Indexes
- 17. Important Business Rules
- PaymentStatus
- 19. Future Extensions
- 7. Payment Model
- User
- PaymentMethod
- `reviews`
- 4. Booking Data Model
- V12__add_discounts_and_payment_provider_fields.sql
- 14. Search Data Relationships

## God Nodes (most connected - your core abstractions)
1. `UserPrincipal` - 55 edges
2. `User` - 55 edges
3. `Property` - 50 edges
4. `ApiResponse` - 44 edges
5. `BookingResponse` - 40 edges
6. `UserRepository` - 39 edges
7. `Booking` - 38 edges
8. `PropertyServiceImpl` - 38 edges
9. `BusinessException` - 35 edges
10. `BookingServiceImpl` - 30 edges

## Surprising Connections (you probably didn't know these)
- `Property Card Component` --conceptually_related_to--> `Property Search Results`  [INFERRED]
  docs/ui/UXUI_DesignSystem.md → src/main/resources/templates/property/search-results.html
- `Search Navbar Component` --conceptually_related_to--> `Navbar Fragment`  [INFERRED]
  docs/ui/UXUI_DesignSystem.md → src/main/resources/templates/fragments/navbar.html
- `Availability Calendar Grid` --conceptually_related_to--> `Booking Availability Check API Call`  [INFERRED]
  docs/ui/UXUI_DesignSystem.md → src/main/resources/templates/booking/booking.html
- `Docker PostgreSQL Datasource` --conceptually_related_to--> `StayHub Application Configuration`  [INFERRED]
  src/main/resources/application-docker.yml → src/main/resources/application.yml
- `Local PostgreSQL Datasource` --conceptually_related_to--> `StayHub Application Configuration`  [INFERRED]
  src/main/resources/application-local.yml → src/main/resources/application.yml

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Shared Layout Fragments** — src_main_resources_templates_fragments_navbar_navbar_fragment, src_main_resources_templates_fragments_footer_footer_fragment, src_main_resources_templates_home_home_search_form, src_main_resources_templates_auth_login_login_form, src_main_resources_templates_property_property_detail_property_detail [EXTRACTED 1.00]
- **StayHub Core Domains** — docs_database_1_domainoverview_user, docs_database_1_domainoverview_property, docs_database_1_domainoverview_booking, docs_database_1_domainoverview_payment, docs_database_1_domainoverview_review [EXTRACTED 1.00]
- **StayHub User Roles** — docs_architecture_1_systemoverview_guest, docs_architecture_1_systemoverview_host, docs_architecture_1_systemoverview_admin [EXTRACTED 1.00]
- **VNPay Booking Payment Flow** — docs_database_1_domainoverview_booking, docs_database_1_domainoverview_payment, docs_architecture_1_systemoverview_vnpay, docs_architecture_3_api_design_vnpay_ipn [EXTRACTED 1.00]
- **Admin Management Templates** — src_main_resources_templates_admin_bookings_admin_booking_dashboard, src_main_resources_templates_admin_users_user_management, src_main_resources_templates_fragments_navbar_navbar_fragment [INFERRED 0.85]
- **Disabled Test Side Effects** — src_test_resources_application_test_jpa_show_sql_disabled, src_test_resources_application_test_thymeleaf_cache_disabled, src_test_resources_application_test_mail_disabled, src_test_resources_application_test_bootstrap_admin_disabled [INFERRED 0.85]
- **Guest Booking Flow Templates** — src_main_resources_templates_home_home_search_form, src_main_resources_templates_property_search_results_property_search, src_main_resources_templates_property_property_detail_property_detail, src_main_resources_templates_booking_payment_mock_payment_success, src_main_resources_templates_booking_booking_detail_booking_actions [INFERRED 0.95]

## Communities (69 total, 25 thin omitted)

### Community 0 - "lombok.RequiredArgsConstructor"
Cohesion: 0.06
Nodes (41): authenticationprincipal, datetimeformat, list, lombok.RequiredArgsConstructor, modelattribute, org.springframework.security.core.Authentication, org.springframework.security.oauth2.core.user.OAuth2User, org.springframework.stereotype.Controller (+33 more)

### Community 1 - "User"
Cohesion: 0.06
Nodes (60): arraylist, bigdecimal, cascadetype, chronounit, column, creationtimestamp, dataintegrityviolationexception, enumerated (+52 more)

### Community 2 - "org.springframework.stereotype.Component"
Cohesion: 0.05
Nodes (55): accessdeniedhandlerimpl, antpathrequestmatcher, bcryptpasswordencoder, com.fasterxml.jackson.databind.ObjectMapper, csrfexception, httpmethod, ioexception, jakarta.servlet.http.HttpServletRequest (+47 more)

### Community 3 - "PropertyServiceImpl"
Cohesion: 0.06
Nodes (27): bufferedimage, com.stayhub.storage.StorageService, comparator, StayHub MVP — Database Design & Diagrams, hashset, imageinputstream, imageio, imagereader (+19 more)

### Community 4 - "PropertyRepository"
Cohesion: 0.08
Nodes (21): collection, lockmodetype, optional, org.springframework.data.jpa.repository.JpaRepository, org.springframework.data.jpa.repository.JpaSpecificationExecutor, org.springframework.data.jpa.repository.Lock, org.springframework.data.jpa.repository.Query, param (+13 more)

### Community 5 - "org.springframework.transaction.annotation.Transactional"
Cohesion: 0.15
Nodes (9): org.springframework.transaction.annotation.Transactional, pagerequest, sort, ResourceNotFoundException, UpdateProfileRequest, UserResponse, UserMapper, Override (+1 more)

### Community 6 - "localdate"
Cohesion: 0.10
Nodes (19): elementtype, futureorpresent, jakarta.validation.Constraint, jakarta.validation.ConstraintValidator, jakarta.validation.ConstraintValidatorContext, jakarta.validation.Payload, java.lang.annotation.Documented, java.lang.annotation.Retention (+11 more)

### Community 7 - "properties"
Cohesion: 0.09
Nodes (24): uk_users_username, uk_users_login_provider_provider_id, uk_users_email, users, idx_properties_host_id, idx_properties_public_listing, properties, idx_property_images_property_id (+16 more)

### Community 8 - "CustomUserDetailsService.java"
Cohesion: 0.38
Nodes (5): org.springframework.security.core.userdetails.UserDetails, org.springframework.security.core.userdetails.UserDetailsService, CustomUserDetailsService, Override, usernamenotfoundexception

### Community 9 - "AuthServiceImplTest.java"
Cohesion: 0.14
Nodes (16): any, argumentcaptor, defaultapplicationarguments, mock, never, org.hibernate.exception.ConstraintViolationException, org.junit.jupiter.api.extension.ExtendWith, org.mockito.junit.jupiter.MockitoExtension (+8 more)

### Community 10 - "RegisterRequest"
Cohesion: 0.16
Nodes (5): org.springframework.boot.ApplicationArguments, AuthService, RegisterRequest, Override, StayhubApplicationTest

### Community 11 - "package.json"
Cohesion: 0.08
Nodes (23): author, bugs, url, description, devDependencies, tailwindcss, directories, doc (+15 more)

### Community 12 - "BookingResponse"
Cohesion: 0.13
Nodes (7): BookingService, AvailabilityRequest, BookingCreateRequest, BookingResponse, PaymentRepository, BookingServiceIntegrationTest, BookingWebIntegrationTest

### Community 13 - "StayHub Application Configuration"
Cohesion: 0.10
Nodes (22): Property Card Component, Search Navbar Component, StayHub Application Configuration, Docker PostgreSQL Datasource, Local PostgreSQL Datasource, Mail Configuration, Google OAuth2 Client Configuration, Upload Configuration (+14 more)

### Community 14 - "BusinessException"
Cohesion: 0.17
Nodes (6): java.security.SecureRandom, AuthServiceImpl, Override, PendingRegistration, BusinessException, AuthServiceImplTest

### Community 15 - "org.junit.jupiter.api.Test"
Cohesion: 0.22
Nodes (4): org.junit.jupiter.api.Test, org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest, org.springframework.context.annotation.Import, SecurityConfigTest

### Community 16 - "BookingWebIntegrationTest.java"
Cohesion: 0.15
Nodes (21): authentication, collections, content, csrf, dothrow, get, jsonpath, map (+13 more)

### Community 17 - "UserRepository"
Cohesion: 0.10
Nodes (26): assertthat, assertthatthrownby, autowired, base64, oauth2authenticationexception, objects, org.junit.jupiter.api.BeforeEach, AdminServiceImpl (+18 more)

### Community 18 - "0_DatabaseDesignDemo.md"
Cohesion: 0.11
Nodes (18): 10. Java Entity Relationship Mapping, 11. Repository / Relational Table Summary, 12. Full Relational Diagram, 15. Database Migration Plan, 16. BaseEntity Design, 18. Recommended Package ↔ Table Mapping, 1. Domain Overview, 20. Final MVP Database Summary (+10 more)

### Community 19 - "StayHub Marketplace Platform"
Cohesion: 0.06
Nodes (34): StayHub Docker App Service, PostgreSQL 16 Database Service, Availability check, Booking page, Flow, Homepage, LAST FLOW:, Property details (+26 more)

### Community 20 - "UserLoginProvider"
Cohesion: 0.50
Nodes (3): UserLoginProvider, GOOGLE, LOCAL

### Community 21 - "AuthServiceImpl.java"
Cohesion: 0.18
Nodes (14): concurrenthashmap, datetimeformatter, javamailsenderimpl, lombok.extern.slf4j.Slf4j, messagingexception, mimemessage, mimemessagehelper, org.springframework.mail.javamail.JavaMailSender (+6 more)

### Community 22 - "AdminBootstrapRunner.java"
Cohesion: 0.14
Nodes (15): constraintviolation, decimalmin, digits, email, jakarta.validation.Validator, linkedhashset, locale, notblank (+7 more)

### Community 23 - "mvnw"
Cohesion: 0.38
Nodes (8): mvnw script, clean(), die(), exec_maven(), hash_string(), set_java_home(), trim(), verbose()

### Community 25 - "PropertySummary"
Cohesion: 0.13
Nodes (20): join, jointype, org.springframework.data.domain.Page, org.springframework.data.domain.Pageable, org.springframework.data.jpa.domain.Specification, org.springframework.stereotype.Repository, pageabledefault, predicate (+12 more)

### Community 26 - "Test Application Configuration"
Cohesion: 0.20
Nodes (10): Bootstrap Admin Disabled, Flyway Baseline On Migrate Disabled, Flyway Test Configuration, Flyway Enabled, Hibernate DDL Validate, JPA Show SQL Disabled, Application Mail Disabled, Spring JPA Test Configuration (+2 more)

### Community 27 - "3. Relational Model"
Cohesion: 0.18
Nodes (11): 3.1 `users`, 3.2 `properties`, 3.3 `property_images`, 3.4 `amenities`, 3.5 `property_amenities`, 3. Relational Model, Important rule, Recommended constraints (+3 more)

### Community 29 - "PostgreSqlIntegrationTest"
Cohesion: 0.43
Nodes (6): org.springframework.boot.test.context.SpringBootTest, org.springframework.test.context.ActiveProfiles, org.springframework.test.context.DynamicPropertyRegistry, org.springframework.test.context.DynamicPropertySource, org.testcontainers.containers.PostgreSQLContainer, PostgreSqlIntegrationTest

### Community 31 - "bookings Table"
Cohesion: 0.40
Nodes (6): bookings Table, discount_codes Table, payments Table, properties Table, reviews Table, users Table

### Community 33 - "graphify.js"
Cohesion: 0.40
Nodes (3): IMPORTANT: keep the reminder string free of backticks and $(...) constructs., ref_fs, ref_path

### Community 34 - "StayhubApplication.java"
Cohesion: 0.50
Nodes (3): org.springframework.boot.autoconfigure.SpringBootApplication, springapplication, StayhubApplication

### Community 37 - "StorageConfig.java"
Cohesion: 0.33
Nodes (7): org.springframework.boot.context.properties.EnableConfigurationProperties, org.springframework.context.annotation.Configuration, org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry, org.springframework.web.servlet.config.annotation.WebMvcConfigurer, path, Override, StorageConfig

### Community 38 - "Guest Booking Lifecycle"
Cohesion: 0.67
Nodes (3): Guest Booking Lifecycle, Transaction Based Commission Business Model, Booking State Machine

### Community 39 - "UI/UX Design System"
Cohesion: 0.67
Nodes (3): Color Palette, UI/UX Design System, Responsive Grid System

### Community 41 - "Admin User Management"
Cohesion: 0.67
Nodes (3): Admin Booking Dashboard, Admin User Management, User Profile Form

### Community 56 - "13. Suggested Indexes"
Cohesion: 0.25
Nodes (8): 13. Suggested Indexes, `bookings`, `discount_codes`, `payments`, `properties`, `property_images`, `reviews`, `users`

### Community 57 - "17. Important Business Rules"
Cohesion: 0.29
Nodes (7): 17. Important Business Rules, Booking, Discount, Payment, Property, Review, User

### Community 58 - "PaymentStatus"
Cohesion: 0.29
Nodes (6): PaymentStatus, CANCELLED, EXPIRED, FAILED, PENDING, SUCCESS

### Community 59 - "19. Future Extensions"
Cohesion: 0.40
Nodes (5): 19. Future Extensions, Availability optimization, Notification, Payment extensions, Wishlist

### Community 60 - "7. Payment Model"
Cohesion: 0.40
Nodes (5): 7. Payment Model, Payment states, `payments`, Recommended business sequence, VNPay relationship

### Community 61 - "User"
Cohesion: 0.40
Nodes (4): Authentication notes, Recommended constraints, Relationship, User

### Community 62 - "PaymentMethod"
Cohesion: 0.40
Nodes (4): PaymentMethod, MOCK, MOMO, VNPAY

### Community 63 - "`reviews`"
Cohesion: 0.50
Nodes (4): 8. Review Data Model, Recommended constraints, Review rule, `reviews`

### Community 64 - "4. Booking Data Model"
Cohesion: 0.67
Nodes (3): 4.1 `bookings`, 4. Booking Data Model, Why store price snapshots?

## Knowledge Gaps
- **155 isolated node(s):** `$schema`, `plugin`, `name`, `version`, `description` (+150 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 254 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **25 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `StayHub MVP — Database Design & Diagrams` connect `PropertyServiceImpl` to `0_DatabaseDesignDemo.md`, `.bookingStatusChanged`?**
  _High betweenness centrality (0.114) - this node is a cross-community bridge._
- **Why does `User` connect `User` to `lombok.RequiredArgsConstructor`, `org.springframework.stereotype.Component`, `PropertyServiceImpl`, `org.springframework.transaction.annotation.Transactional`, `CustomUserDetailsService.java`, `AuthServiceImplTest.java`, `RegisterRequest`, `BookingResponse`, `BookingWebIntegrationTest.java`, `UserRepository`, `UserLoginProvider`, `AuthServiceImpl.java`, `AdminBootstrapRunner.java`, `ReviewServiceIntegrationTest`, `PropertyWebIntegrationTest`?**
  _High betweenness centrality (0.106) - this node is a cross-community bridge._
- **Why does `Booking` connect `User` to `AuthServiceImplTest.java`, `.bookingStatusChanged`, `PropertyRepository`, `AuthServiceImpl.java`?**
  _High betweenness centrality (0.044) - this node is a cross-community bridge._
- **What connects `$schema`, `plugin`, `name` to the rest of the system?**
  _155 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `lombok.RequiredArgsConstructor` be split into smaller, more focused modules?**
  _Cohesion score 0.05726984126984127 - nodes in this community are weakly interconnected._
- **Should `User` be split into smaller, more focused modules?**
  _Cohesion score 0.05895316804407714 - nodes in this community are weakly interconnected._
- **Should `org.springframework.stereotype.Component` be split into smaller, more focused modules?**
  _Cohesion score 0.054363796650014694 - nodes in this community are weakly interconnected._