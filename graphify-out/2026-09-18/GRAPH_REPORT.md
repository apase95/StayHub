# Graph Report - StayHub  (2026-09-18)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 1048 nodes · 3341 edges · 55 communities (36 shown, 19 thin omitted)
- Extraction: 93% EXTRACTED · 7% INFERRED · 0% AMBIGUOUS · INFERRED: 231 edges (avg confidence: 0.81)
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
- BookingResponse
- org.springframework.transaction.annotation.Transactional
- localdate
- properties
- org.springframework.stereotype.Service
- AuthServiceImplTest.java
- RegisterRequest
- package.json
- BookingServiceIntegrationTest
- StayHub Application Configuration
- UserRepository
- org.junit.jupiter.api.Test
- BookingWebIntegrationTest.java
- PropertyServiceIntegrationTest.java
- ReviewServiceIntegrationTest.java
- StayHub Marketplace Platform
- AdminBootstrapRunner.java
- AuthServiceImpl.java
- RegisterRequest.java
- mvnw
- PropertyWebIntegrationTest
- BookingServiceIntegrationTest.java
- Test Application Configuration
- Booking Domain
- EmailNotificationService
- PostgreSqlIntegrationTest
- EmailTemplateRenderer
- bookings Table
- org.junit.jupiter.api.AfterEach
- graphify.js
- StayhubApplication.java
- AdminService
- PriceUtil
- PostgreSQL 16 Database Service
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

## Communities (55 total, 19 thin omitted)

### Community 0 - "lombok.RequiredArgsConstructor"
Cohesion: 0.05
Nodes (44): authenticationprincipal, datetimeformat, list, lombok.RequiredArgsConstructor, modelattribute, org.springframework.security.core.Authentication, org.springframework.security.oauth2.core.user.OAuth2User, org.springframework.stereotype.Controller (+36 more)

### Community 1 - "User"
Cohesion: 0.06
Nodes (75): arraylist, bigdecimal, bufferedimage, cascadetype, chronounit, column, comparator, creationtimestamp (+67 more)

### Community 2 - "org.springframework.stereotype.Component"
Cohesion: 0.05
Nodes (58): accessdeniedhandlerimpl, antpathrequestmatcher, bcryptpasswordencoder, com.fasterxml.jackson.databind.ObjectMapper, csrfexception, httpmethod, ioexception, jakarta.servlet.http.HttpServletRequest (+50 more)

### Community 3 - "PropertyServiceImpl"
Cohesion: 0.06
Nodes (17): lombok.EqualsAndHashCode, org.springframework.data.jpa.repository.Modifying, org.springframework.web.multipart.MultipartFile, BusinessException, PropertyCreateRequest, PropertyImageOrderRequest, PropertyUpdateRequest, PropertyImageRepository (+9 more)

### Community 4 - "BookingResponse"
Cohesion: 0.06
Nodes (29): collection, lockmodetype, optional, org.springframework.data.jpa.repository.JpaRepository, org.springframework.data.jpa.repository.JpaSpecificationExecutor, org.springframework.data.jpa.repository.Lock, org.springframework.data.jpa.repository.Query, param (+21 more)

### Community 5 - "org.springframework.transaction.annotation.Transactional"
Cohesion: 0.09
Nodes (22): join, jointype, org.springframework.data.domain.Page, org.springframework.data.domain.Pageable, org.springframework.data.jpa.domain.Specification, org.springframework.stereotype.Repository, org.springframework.transaction.annotation.Transactional, pagerequest (+14 more)

### Community 6 - "localdate"
Cohesion: 0.09
Nodes (20): elementtype, futureorpresent, jakarta.validation.Constraint, jakarta.validation.ConstraintValidator, jakarta.validation.ConstraintValidatorContext, jakarta.validation.Payload, java.lang.annotation.Documented, java.lang.annotation.Retention (+12 more)

### Community 7 - "properties"
Cohesion: 0.09
Nodes (24): uk_users_username, uk_users_login_provider_provider_id, uk_users_email, users, idx_properties_host_id, idx_properties_public_listing, properties, idx_property_images_property_id (+16 more)

### Community 8 - "org.springframework.stereotype.Service"
Cohesion: 0.11
Nodes (18): collections, map, oauth2authenticationexception, objects, org.springframework.security.core.GrantedAuthority, org.springframework.security.core.userdetails.UserDetails, org.springframework.security.core.userdetails.UserDetailsService, org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService (+10 more)

### Community 9 - "AuthServiceImplTest.java"
Cohesion: 0.13
Nodes (19): any, argumentcaptor, defaultapplicationarguments, javamailsenderimpl, mock, never, org.hibernate.exception.ConstraintViolationException, org.junit.jupiter.api.extension.ExtendWith (+11 more)

### Community 10 - "RegisterRequest"
Cohesion: 0.14
Nodes (6): org.springframework.boot.ApplicationArguments, AuthService, Override, RegisterRequest, Override, StayhubApplicationTest

### Community 11 - "package.json"
Cohesion: 0.08
Nodes (23): author, bugs, url, description, devDependencies, tailwindcss, directories, doc (+15 more)

### Community 12 - "BookingServiceIntegrationTest"
Cohesion: 0.16
Nodes (3): PaymentRepository, BookingServiceIntegrationTest, ReviewServiceIntegrationTest

### Community 13 - "StayHub Application Configuration"
Cohesion: 0.10
Nodes (22): Property Card Component, Search Navbar Component, StayHub Application Configuration, Docker PostgreSQL Datasource, Local PostgreSQL Datasource, Mail Configuration, Google OAuth2 Client Configuration, Upload Configuration (+14 more)

### Community 14 - "UserRepository"
Cohesion: 0.21
Nodes (6): java.security.SecureRandom, AuthServiceImpl, Override, PendingRegistration, UserRepository, AuthServiceImplTest

### Community 15 - "org.junit.jupiter.api.Test"
Cohesion: 0.16
Nodes (5): org.junit.jupiter.api.Test, org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest, org.springframework.context.annotation.Import, BookingWebIntegrationTest, SecurityConfigTest

### Community 16 - "BookingWebIntegrationTest.java"
Cohesion: 0.22
Nodes (17): authentication, autowired, content, csrf, dothrow, get, jsonpath, mediatype (+9 more)

### Community 17 - "PropertyServiceIntegrationTest.java"
Cohesion: 0.13
Nodes (13): base64, com.stayhub.storage.StorageService, mockbean, org.springframework.mock.web.MockMultipartFile, UserRole, ADMIN, GUEST, HOST (+5 more)

### Community 18 - "ReviewServiceIntegrationTest.java"
Cohesion: 0.14
Nodes (10): assertthat, assertthatthrownby, org.junit.jupiter.api.BeforeEach, BookingStatus, CANCELLED, COMPLETED, CONFIRMED, PENDING (+2 more)

### Community 19 - "StayHub Marketplace Platform"
Cohesion: 0.14
Nodes (14): Admin Stakeholder, Guest Stakeholder, Host Stakeholder, StayHub Marketplace Platform, Cloudinary Local Storage Service, VNPay Payment Gateway, Spring Boot 3.3.2 Java 21 Stack, ApiResponse Standard Wrapper (+6 more)

### Community 20 - "AdminBootstrapRunner.java"
Cohesion: 0.23
Nodes (10): constraintviolation, dataintegrityviolationexception, jakarta.validation.Validator, org.springframework.boot.ApplicationRunner, org.springframework.boot.autoconfigure.condition.ConditionalOnProperty, org.springframework.security.crypto.password.PasswordEncoder, AdminBootstrapRunner, UserLoginProvider (+2 more)

### Community 21 - "AuthServiceImpl.java"
Cohesion: 0.29
Nodes (8): concurrenthashmap, datetimeformatter, lombok.extern.slf4j.Slf4j, messagingexception, mimemessage, mimemessagehelper, unsupportedencodingexception, value

### Community 22 - "RegisterRequest.java"
Cohesion: 0.29
Nodes (6): email, locale, notblank, pattern, size, EmailNormalizer

### Community 23 - "mvnw"
Cohesion: 0.38
Nodes (8): mvnw script, clean(), die(), exec_maven(), hash_string(), set_java_home(), trim(), verbose()

### Community 25 - "BookingServiceIntegrationTest.java"
Cohesion: 0.31
Nodes (3): BookingCreateRequest, InvalidStateTransitionException, ResourceNotFoundException

### Community 26 - "Test Application Configuration"
Cohesion: 0.20
Nodes (10): Bootstrap Admin Disabled, Flyway Baseline On Migrate Disabled, Flyway Test Configuration, Flyway Enabled, Hibernate DDL Validate, JPA Show SQL Disabled, Application Mail Disabled, Spring JPA Test Configuration (+2 more)

### Community 27 - "Booking Domain"
Cohesion: 0.22
Nodes (9): Discount Validation Flow, Booking Domain, Discount Domain, Payment Domain, Property Domain, Review Domain, User Domain, Booking Price Snapshot Rule (+1 more)

### Community 28 - "EmailNotificationService"
Cohesion: 0.36
Nodes (3): EmailNotificationService, Override, EmailNotificationServiceTest

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

### Community 37 - "PostgreSQL 16 Database Service"
Cohesion: 0.67
Nodes (3): StayHub Docker App Service, PostgreSQL 16 Database Service, Contributor First Step Setup Guide

### Community 38 - "Guest Booking Lifecycle"
Cohesion: 0.67
Nodes (3): Guest Booking Lifecycle, Transaction Based Commission Business Model, Booking State Machine

### Community 39 - "UI/UX Design System"
Cohesion: 0.67
Nodes (3): Color Palette, UI/UX Design System, Responsive Grid System

### Community 41 - "Admin User Management"
Cohesion: 0.67
Nodes (3): Admin Booking Dashboard, Admin User Management, User Profile Form

## Knowledge Gaps
- **101 isolated node(s):** `APARTMENT`, `HOMESTAY`, `HOTEL_ROOM`, `HOUSE`, `RESORT` (+96 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 196 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **19 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `User` connect `User` to `lombok.RequiredArgsConstructor`, `org.springframework.stereotype.Component`, `PropertyServiceImpl`, `org.springframework.transaction.annotation.Transactional`, `org.springframework.stereotype.Service`, `AuthServiceImplTest.java`, `RegisterRequest`, `BookingServiceIntegrationTest`, `UserRepository`, `org.junit.jupiter.api.Test`, `BookingWebIntegrationTest.java`, `PropertyServiceIntegrationTest.java`, `ReviewServiceIntegrationTest.java`, `AdminBootstrapRunner.java`, `AuthServiceImpl.java`, `PropertyWebIntegrationTest`, `BookingServiceIntegrationTest.java`?**
  _High betweenness centrality (0.057) - this node is a cross-community bridge._
- **Why does `UserPrincipal` connect `lombok.RequiredArgsConstructor` to `User`, `org.springframework.stereotype.Service`, `BookingWebIntegrationTest.java`, `PropertyServiceIntegrationTest.java`, `PropertyWebIntegrationTest`?**
  _High betweenness centrality (0.054) - this node is a cross-community bridge._
- **Why does `ApiResponse` connect `lombok.RequiredArgsConstructor` to `User`, `org.springframework.stereotype.Component`, `org.springframework.transaction.annotation.Transactional`?**
  _High betweenness centrality (0.041) - this node is a cross-community bridge._
- **What connects `APARTMENT`, `HOMESTAY`, `HOTEL_ROOM` to the rest of the system?**
  _101 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `lombok.RequiredArgsConstructor` be split into smaller, more focused modules?**
  _Cohesion score 0.05236432878451285 - nodes in this community are weakly interconnected._
- **Should `User` be split into smaller, more focused modules?**
  _Cohesion score 0.06241956241956242 - nodes in this community are weakly interconnected._
- **Should `org.springframework.stereotype.Component` be split into smaller, more focused modules?**
  _Cohesion score 0.05143638850889193 - nodes in this community are weakly interconnected._