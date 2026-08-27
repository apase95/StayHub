# UI/UX DESIGN SYSTEM

## 1. Color Palette
| Tên Style | Alias | Hex Code | Ứng dụng |
| :--- | :--- | :--- | :--- |
| **Primary Coral** | `color-primary-500` | `#FF6B6B` | Logo StayHub, Nút bấm Primary (Reserve, Confirm & Pay), Sidebar Active State, Badge trạng thái Confirmed |
| **Primary Coral Dark** | `color-primary-600` | `#E85555` | Trạng thái Hover/Pressed của nút Primary |
| **Text Heading** | `color-text-900` | `#111827` | Tiêu đề chính (Tên property, Heading trang), luôn đen đậm để tương phản mạnh |
| **Text Muted** | `color-text-gray` | `#6B7280` | Mô tả phụ, địa chỉ, số đêm, sub-text dưới Heading |
| **Surface White** | `color-bg-white` | `#FFFFFF` | Nền Card property, Nền Form Booking, Nền Navbar |
| **Surface Light Gray** | `color-bg-light` | `#F9FAFB` | Nền tổng thể trang Body Background |
| **Border Subtle** | `color-border` | `#F1E4E4` / `#E5E7EB` | Viền Card property, viền input Search box |
| **Success Green** | `color-success-500` | `#16A34A` | Badge trạng thái Confirmed (bản phụ), Admin Dashboard (`+12%`) |
| **Warning Amber** | `color-warning-500` | `#D97706` | Badge trạng thái Pending |
| **Rating Gold** | `color-rating-500` | `#F59E0B` | Icon sao đánh giá (★) |

### 2. Typography
*   **Font Family:** `Inter` hoặc `Helvetica Neue`
*   **Scale (Hệ tỷ lệ):**
    *   `Display/H1` (Bold 800): **48px** → Dùng cho Tiêu đề Hero Homepage, Tên property trên trang Property Detail.
    *   `H2` (Bold 700): **32px** → Dùng cho Heading section ("Platform Overview", "Trips", "120 stays in Da Nang").
    *   `H3` (SemiBold 600): **20px** → Tên property trên Card danh sách, tên các mục trong Sidebar Dashboard.
    *   `Body Text` (Regular 400): **16px** → Mô tả, nội dung "About this place".
    *   `Caption/Meta` (Regular 400): **14px** → Địa chỉ, số phòng ngủ/giường/khách, ngày Check-in/Check-out.
    *   `Price Big Number` (Bold 700): **28px** → Giá booking trong Price Box, số liệu Dashboard (Total Users, Platform Revenue).

### 1.3. Spacing & Grid (Khoảng cách & Lưới)
*   **Grid System:** 12 Columns, `max-width: 1280px`, Gutter `24px`.
*   **Section Padding:** Main block `48px` (Homepage, Search Results), Admin/Host Dashboard block `32px`.
*   **Border Radius:** `Radius: 12px` cho Card property và Input; `Radius: 8px` cho Button; `Radius: 999px` cho Search Box và Badge trạng thái.
*   **Shadow:** (`border: 1px solid color-border`) cho Cards, Khối nổi (Price Box sticky, Modal): `Y: 8px`, `Blur: 24px`, `Color: rgba(0,0,0,0.08)`.

---

## 3. HỆ THỐNG LƯỚI RESPONSIVE (BREAKPOINTS & GRID SYSTEM)

### 3.1. Desktop & Large Screens (`lg`: 1024px trở lên)
*   **Grid:** Hệ thống lưới 12 cột. Container căn giữa `max-width: 1280px`.
*   **Layout Ứng Xử:**
    *   **Trang chủ (Homepage):** Search box dạng pill nằm ngang 4 ô (Where / Check-in / Check-out / Guests) chồng lên Hero Banner.
    *   **Search Results:** Layout 2 cột cố định — Filter Sidebar bên trái (`3/12` cột) + Property List bên phải (`9/12` cột).
    *   **Property Detail:** Layout 2 cột — Nội dung mô tả bên trái (`7/12`), Price Box **sticky** bên phải (`5/12`), luôn hiển thị khi cuộn trang.
    *   **Admin/Host Dashboard:** Layout cố định Sidebar trái (`240px`) + Content chính co giãn, khối Stats Card xếp ngang 4 cột.

### 3.2. Tablet & iPad (`md`: 768px - 1023px)
*   **Grid:** Hệ thống lưới 8 cột. Margin hai mép viền tối thiểu `24px`.
*   **Layout Ứng Xử:**
    *   Font Size ở `Display/H1`, `H2` thu nhỏ khoảng 25% để không tràn màn hình.
    *   **Search Results:** Filter Sidebar chuyển thành thanh Filter ngang dạng Dropdown/Chip nằm phía trên danh sách (thay vì cột trái cố định).
    *   **Property Detail:** Price Box không còn sticky bên cạnh mà đẩy xuống dưới phần mô tả, giữ nguyên dạng Card bo góc.
    *   **Admin/Host Dashboard:** Sidebar có thể thu gọn lại chỉ hiện Icon (Icon-only Sidebar), khối Stats Card xếp lưới 2x2.

### 3.3. Mobile Devices (`sm`: 320px - 767px)
*   **Grid:** Hệ thống lưới 4 cột. Padding-X ốp viền `16px`.
*   **Layout Ứng Xử:**
    *   **Navbar:** Thu gọn còn Logo + Icon Search + Icon Menu (Hamburger); menu đầy đủ (Explore/Trips/Wishlists/Host your home) trượt ra dạng Bottom Sheet hoặc Full-screen Drawer.
    *   **Search box Homepage:** 4 ô Where/Check-in/Check-out/Guests xếp chồng dọc thành 1 cột, mỗi ô cao `56px`, nút Search full-width ở cuối.
    *   **Property Card (Search Results):** Ảnh full-width phía trên, thông tin (tên, rating, giá) xếp dọc bên dưới thay vì nằm ngang cạnh ảnh.
    *   **Image Gallery (Property Detail):** Lưới ảnh 2x2+1 chuyển thành Carousel vuốt ngang (Swipeable), chấm chỉ số trang (dots indicator) phía dưới ảnh.
    *   **Price Box / Booking Summary:** Chuyển thành thanh cố định (Sticky Bottom Bar) hiển thị Giá + nút "Reserve"/"Confirm & Pay", bấm vào mới mở Bottom Sheet chi tiết giá.
    *   **Admin/Host Dashboard:** Sidebar ẩn hoàn toàn, thay bằng Bottom Navigation Bar hoặc Hamburger Menu; bảng dữ liệu (Recent Bookings, Booking Requests) chuyển từ Table sang danh sách Card xếp dọc, mỗi Card gồm Property + Guest + Amount + Status.

---

## 4. COMPONENT SPECS (CẤU TRÚC COMPONENT CỐT LÕI)

Vẽ các Component (Frame) sau trên Stitch/Figma để dễ dàng nhân bản và tái sử dụng cho các trang khác:

### CPN 01: The Search Navbar (Thanh điều hướng có ô tìm kiếm)
*   **Nền (Fill):** `#FFFFFF`, viền dưới `1px solid color-border`.
*   **Height:** `72px`.
*   **Phân bổ:** Trái là Logo StayHub (icon nhà + ghim định vị, màu `color-primary-500`). Giữa là Search Pill thu gọn (`Anywhere | Any week | Add guests` + icon kính lúp đỏ). Phải là nhóm: `Explore / Trips / Wishlists / Host your home` (Text Link) + Icon Menu (hamburger) + Avatar tròn trong khung viền bo tròn (pill).
*   **Trạng thái đã đăng nhập vs chưa:** Khi chưa login, cụm phải chỉ còn `Host your home` + Icon Globe + Icon Account mặc định (silhouette).

### CPN 02: Stats Big Number Card (Thẻ số liệu lớn — dùng cho Admin/Host Dashboard)
*   Tham chiếu: khối 4 Card ngang hàng trên Platform Overview / Host Dashboard.
*   **Cấu trúc (Auto Layout dọc trong 1 Card bo góc `12px`, nền trắng, viền `color-border`):**
    1. Icon tròn nền nhạt (`color-bg-light`) ở góc trái, kèm Badge tăng trưởng nhỏ góc phải (`+12%`, nền `color-success-500` nhạt, chữ xanh — hoặc nền `color-primary-500` nhạt nếu số liệu trung tính).
    2. Số liệu lớn (`Price Big Number`, 28–32px, Bold, màu `color-text-900`) — VD: `45.2k`, `$2.4M`, `1,245`.
    3. Label mô tả phía dưới (`Caption/Meta`, màu `color-text-gray`) — VD: "Total Users", "Platform Revenue".

### CPN 03: The Property Card (Thẻ chỗ ở — dùng cho Search Results & Featured Properties)
*   Tham chiếu: Card ảnh trong danh sách "120 stays in Da Nang".
*   Tạo Frame, `Radius 12px`, ảnh bo góc trên full-width tỷ lệ `4:3`.
*   **Cấu trúc (Vertical Auto Layout bên dưới ảnh):**
    1. Badge nổi trên góc trái ảnh nếu có (`Superhost` — nền trắng, chữ đen, bo pill).
    2. Icon trái tim (Wishlist/Save) nổi góc phải trên ảnh, nền trắng bo tròn.
    3. Dòng loại hình + khu vực (`Caption`, màu xám) — VD: "Entire villa in Son Tra".
    4. Heading tên property (`H3`, đen đậm) + Icon sao vàng + số rating + số lượng review nằm cùng hàng bên phải.
    5. Dòng tiện ích ngắn gọn (Caption, xám) — "8 guests · 4 bedrooms · 5 beds · 4.5 baths".
    6. Giá tiền (`Price Big Number` cỡ nhỏ 18px, Bold, đen) + đơn vị "/ night" (Caption xám) căn phải; dòng phụ tổng giá gạch chân "₫9,600,000 total".

### CPN 04: Availability Calendar Grid (Lưới lịch kiểm tra ngày trống)
*   Trên Component Booking Page (Check-in/Check-out) và Property Detail (Price Box).
*   Dùng công cụ vẽ Grid của Stitch tạo lưới ô ngày dạng `7 cột x n hàng` (theo tuần), mỗi ô hình vuông `Radius 8px`, kích thước `40x40px`.
*   **Trạng thái ô ngày (3 biến thể Variant):**
    1. `Available` — nền trắng, chữ đen, hover viền `color-primary-500`.
    2. `Selected Range` — nền `color-primary-500` nhạt (`#FFE5E5`), 2 đầu mút (check-in/check-out) tô đậm `color-primary-500` nền, chữ trắng.
    3. `Booked/Disabled` — chữ xám nhạt, có gạch chéo mờ (strike-through), không thể click, tooltip hiển thị "Booked" khi hover.
*   Đây là Component tái sử dụng cho cả luồng Guest chọn ngày và luồng Backend hiển thị trực quan kết quả availability check (theo `flow.md`).