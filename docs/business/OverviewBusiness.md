# TỔNG QUAN NGHIỆP VỤ DỰ ÁN STAYHUB

## 1. StayHub là gì?

StayHub là một **nền tảng trung gian (marketplace) hai chiều**, kết nối:

- **Người có chỗ ở cho thuê** (gọi là **Host**) — có thể là chủ căn hộ, villa, homestay, khách sạn nhỏ...
- **Người cần thuê chỗ ở** (gọi là **Guest**) — khách du lịch, người đi công tác, cần đặt phòng theo ngày.

StayHub **không sở hữu** bất kỳ bất động sản nào. Vai trò của nền tảng là:
1. Cho Host đăng tin chỗ ở của mình lên hệ thống.
2. Cho Guest tìm kiếm, so sánh, và đặt chỗ ở phù hợp.
3. Đứng giữa xử lý thanh toán, xác nhận đặt phòng, và thu phí dịch vụ trên mỗi giao dịch thành công.

## 2. Ba vai trò chính trong hệ thống

### 2.1. Guest (Khách thuê)
Là người dùng phổ thông nhất, có thể:
- Duyệt trang chủ, tìm kiếm chỗ ở theo địa điểm, ngày nhận/trả phòng, số khách.
- Lọc và sắp xếp kết quả tìm kiếm (theo giá, loại hình, số phòng ngủ, tiện nghi, đánh giá).
- Xem chi tiết một chỗ ở (ảnh, mô tả, tiện nghi, vị trí, đánh giá của khách trước, giá).
- Đặt phòng: chọn ngày, số khách, xem tổng tiền, xác nhận và thanh toán.
- Theo dõi các chuyến đi của mình: đang chờ, đã xác nhận, đã hoàn thành, đã huỷ.
- Huỷ một đặt phòng (nếu còn ở trạng thái cho phép huỷ).
- Viết đánh giá sau khi chuyến đi hoàn thành.

### 2.2. Host (Chủ nhà)
Là Guest đã đăng ký thêm vai trò cho thuê, có thể:
- Đăng tin chỗ ở mới: tiêu đề, mô tả, địa chỉ, giá/đêm, số khách tối đa, số phòng ngủ/giường/phòng tắm, loại hình (căn hộ/villa/khách sạn/homestay/resort), hình ảnh, tiện nghi.
- Quản lý danh sách chỗ ở của mình.
- Theo dõi doanh thu, booking đã thanh toán/đã xác nhận.
- Xử lý booking request thủ công là hướng mở rộng nếu sau này cần Host duyệt trước hoặc sau thanh toán.

### 2.3. Admin (Quản trị hệ thống)
Là đội ngũ vận hành nền tảng, có thể:
- Xem tổng quan toàn hệ thống: tổng số người dùng, số Host đang hoạt động, tổng doanh thu nền tảng, số booking phát sinh mỗi ngày.
- Xem và quản lý toàn bộ booking trên hệ thống (tìm kiếm theo mã booking/tên khách, lọc theo trạng thái).
- Quản lý người dùng: khoá tài khoản vi phạm, đổi vai trò nếu cần.
- (Mở rộng) xuất báo cáo, theo dõi các chỉ số tăng trưởng.

---

## 3. Vòng đời nghiệp vụ tổng thể (Guest POV)

Đây là hành trình chính mà một Guest trải qua khi sử dụng StayHub, từ lúc vào web đến lúc để lại đánh giá:

1. **Truy cập trang chủ** — thấy ô tìm kiếm, các điểm đến phổ biến (Hồ Chí Minh, Đà Nẵng, Đà Lạt, Nha Trang, Hà Nội), các chỗ ở nổi bật, các loại hình phổ biến.
2. **Tìm kiếm** — nhập địa điểm, ngày nhận phòng, ngày trả phòng, số khách.
3. **Xem kết quả tìm kiếm** — danh sách các chỗ ở phù hợp, có thể lọc (giá, loại hình, số phòng ngủ, tiện nghi, đánh giá) và sắp xếp (giá tăng/giảm, đánh giá cao nhất), có phân trang.
4. **Xem chi tiết chỗ ở** — ảnh gallery, thông tin cơ bản, mô tả, danh sách tiện nghi, đánh giá từ khách trước, và **kiểm tra tình trạng trống theo ngày** (hệ thống sẽ từ chối nếu khoảng ngày Guest chọn bị trùng với một booking đã được xác nhận trước đó).
5. **Bấm "Đặt phòng"** → chuyển sang trang đặt phòng, điền/xác nhận thông tin khách, nhập mã giảm giá nếu có, xem bảng tổng hợp giá.
6. **Xác nhận & thanh toán qua VNPay** — backend tạo booking `PENDING_PAYMENT`, payment `PENDING`, rồi redirect Guest sang VNPay checkout.
7. **VNPay xác nhận thanh toán qua IPN** — backend verify chữ ký và số tiền trước khi cập nhật dữ liệu.
8. **Booking chuyển sang CONFIRMED** nếu payment success hợp lệ; Guest nhận email xác nhận và trang kết quả tự chuyển về My Bookings.
9. **Guest có thể huỷ booking** ở trạng thái CONFIRMED theo rule hệ thống → booking chuyển sang **CANCELLED**.
10. Sau ngày trả phòng thực tế, booking đã CONFIRMED sẽ được đánh dấu **COMPLETED**.
11. Khi booking đã COMPLETED, Guest có thể **viết đánh giá (Review)** cho chỗ ở đó — đánh giá này sẽ hiển thị công khai ở trang chi tiết chỗ ở, góp phần vào điểm rating trung bình.

Toàn bộ vòng đời trạng thái của một booking có thể tóm tắt như sau:

```
PENDING_PAYMENT ──(VNPay Success + verified IPN)──► CONFIRMED ──(đến ngày trả phòng)──► COMPLETED ──► REVIEW
       │                                                 │
       └──(VNPay failed/cancelled/expired)──► CANCELLED  │
                                                         └──(Guest Cancel)──► CANCELLED
```

## 4. Mô hình kinh doanh & dòng tiền

StayHub vận hành theo mô hình **thu phí hoa hồng trên mỗi giao dịch (transaction-based commission)**, cụ thể trong bảng giá hiển thị cho Guest tại bước thanh toán bao gồm 3 thành phần:

| Thành phần | Ý nghĩa | Ai hưởng |
|---|---|---|
| **Giá phòng × số đêm** | Doanh thu chính của Host | Host |
| **Phí dọn dẹp (Cleaning fee)** | Chi phí cố định Host đặt ra cho mỗi lượt khách | Host |
| **Phí dịch vụ StayHub (Service fee)** | Phí nền tảng thu trên mỗi giao dịch thành công | StayHub |
| **Mã giảm giá (Discount)** | Khoản giảm trừ theo campaign/promo code | StayHub/marketing budget |

→ **Tổng tiền Guest trả = (Giá phòng × số đêm) + Phí dọn dẹp + Phí dịch vụ - Giảm giá.**

## 5. Ba mảng nghiệp vụ cốt lõi

### 5.1. Property & Search (Chỗ ở & Tìm kiếm)
Đây là "kho hàng" của nền tảng. Mỗi **Property** (chỗ ở) thuộc về một Host, có các thông tin: loại hình, địa chỉ/thành phố, giá/đêm, sức chứa, số phòng ngủ/giường/phòng tắm, mô tả, danh sách ảnh, danh sách tiện nghi (Wi-Fi, hồ bơi, bãi đỗ xe, điều hoà, bếp, máy giặt, TV...).

Nghiệp vụ tìm kiếm cho phép Guest lọc theo nhiều tiêu chí cùng lúc (khoảng giá, loại hình, số phòng ngủ, tiện nghi, đánh giá tối thiểu) và sắp xếp kết quả — đây là phần phức tạp nhất về mặt truy vấn dữ liệu vì phải kết hợp điều kiện lọc **với** điều kiện chỗ ở phải **còn trống** trong khoảng ngày Guest chọn (không được trùng với booking nào đã CONFIRMED hoặc đang PENDING_PAYMENT chờ thanh toán/xác minh).

### 5.2. Booking & Payment (Đặt phòng & Thanh toán)
Hai quy tắc quan trọng nhất:

- **Kiểm tra trùng lịch:** Trước khi cho phép tạo booking, hệ thống bắt buộc phải kiểm tra khoảng ngày [check-in, check-out] mà Guest chọn không được giao nhau với bất kỳ booking nào của cùng property đang ở trạng thái PENDING_PAYMENT hoặc CONFIRMED. Nếu trùng, hệ thống từ chối và báo lỗi rõ ràng cho Guest biết (mã lỗi `ERR_ROOM_NOT_AVAILABLE`).
- **Tính giá:** Tổng tiền booking = (giá/đêm × số đêm) + phí dọn dẹp + phí dịch vụ - discount. Logic này được tách riêng thành service chuyên trách để dễ kiểm thử và thay đổi công thức tính giá sau này.
- **Discount:** Mã giảm giá chỉ được dùng để preview trên FE. Khi Guest submit booking, backend phải tính lại subtotal, validate lại code, snapshot `discount_amount`, và chỉ tăng `used_count` sau khi VNPay xác nhận payment success.

Thanh toán (Payment) là một domain con nằm cạnh Booking, ghi nhận: số tiền, phương thức thanh toán, trạng thái, thời điểm thanh toán và mã giao dịch từ VNPay. Với VNPay, `returnUrl` chỉ phục vụ trải nghiệm người dùng; IPN/webhook có verify checksum và amount mới là nguồn xác nhận chính để chuyển payment sang `SUCCESS` và booking sang `CONFIRMED`.

### 5.3. Review & Notification (Đánh giá & Thông báo)
- **Review** chỉ được phép tạo khi booking đã ở trạng thái COMPLETED — đảm bảo chỉ khách đã thực sự lưu trú mới có quyền đánh giá, tránh đánh giá ảo. Review gắn với booking cụ thể, có điểm số (rating) và bình luận, hiển thị lại trên trang chi tiết property.
- **Notification** được gửi tự động mỗi khi trạng thái booking thay đổi quan trọng: thanh toán thành công và booking được xác nhận (CONFIRMED), hoặc booking bị huỷ (CANCELLED) — giúp Guest nắm được tình trạng đặt phòng mà không cần chủ động vào lại hệ thống kiểm tra.


## 6. Vai trò của Host trong vận hành

Trong VNPay MVP, Host chủ yếu vận hành listing và theo dõi booking đã xác nhận. Việc Host duyệt thủ công có thể được bật lại sau nếu nghiệp vụ yêu cầu kiểm soát chặt hơn.

- Booking mới bắt đầu ở trạng thái **PENDING_PAYMENT** trong lúc chờ VNPay xác nhận.
- Sau IPN success hợp lệ, booking chuyển thẳng sang **CONFIRMED**.
- Host Dashboard vẫn hiển thị booking theo property để Host theo dõi lịch đặt và doanh thu.
- Nếu sau này khôi phục Host approval, trạng thái đề xuất là `PENDING_HOST_CONFIRMATION` sau payment success.

## 7. Vai trò của Admin trong vận hành

Admin đóng vai trò giám sát toàn hệ thống chứ không tham gia trực tiếp vào từng giao dịch. Admin Dashboard cho thấy bức tranh toàn cảnh: tổng người dùng, tổng Host đang hoạt động, tổng doanh thu nền tảng, số booking phát sinh trong ngày, và một bảng theo dõi các booking gần nhất kèm trạng thái (Confirmed/Pending/Cancelled) để phát hiện sớm bất thường (ví dụ tỷ lệ huỷ tăng cao ở một khu vực). Admin cũng có quyền quản lý tài khoản người dùng vi phạm chính sách nền tảng.

## 8. Phân quyền truy cập (business rule tổng quát)


| Khu vực | Ai được truy cập |
|:-:|:-:|
| Trang chủ, tìm kiếm, xem chi tiết property | Công khai (không cần đăng nhập) |
| Đặt phòng, xem "My Bookings", viết review | Bắt buộc đăng nhập, vai trò Guest (hoặc bất kỳ user nào đã đăng nhập) |
| Đăng property, xem booking request, dashboard host | Bắt buộc đăng nhập, vai trò Host |
| Dashboard quản trị, quản lý user/booking toàn hệ thống | Bắt buộc đăng nhập, vai trò Admin |


## 9. Vì sao thiết kế theo hướng này? (Lý do nghiệp vụ)

- **VNPay trước cho thanh toán VND:** giúp flow demo sát thực tế người dùng Việt Nam hơn, hỗ trợ bank/card/QR và có IPN để xác minh giao dịch.
- **Tự xác nhận sau payment success:** giảm friction cho Guest; booking chỉ được CONFIRMED khi backend đã verify checksum và amount từ VNPay.
- **Discount snapshot:** giúp audit rõ ràng số tiền trước/sau giảm giá, đồng thời tránh lệ thuộc vào campaign có thể thay đổi sau này.
- **Service fee cố định hiển thị minh bạch tại bước thanh toán:** giúp Guest hiểu rõ tiền của mình đi đâu, đồng thời đây là cách đơn giản nhất để mô hình hoá doanh thu nền tảng mà không cần hệ thống tính hoa hồng phức tạp theo tỷ lệ % biến động.
- **Review gắn chặt với Booking đã COMPLETED:** đảm bảo tính xác thực của đánh giá — quy tắc này là chuẩn ngành (Airbnb, Booking.com đều áp dụng) để tránh review giả mạo làm sai lệch uy tín Host.

## 10. Định hướng mở rộng

Sau khi project hoạt động ổn định, một số hướng phát triển thêm về mặt nghiệp vụ:

- **Refund/void payment:** hoàn tiền khi Guest huỷ hoặc khi phát sinh tranh chấp.
- **MoMo/ZaloPay:** thêm provider khác sau khi abstraction VNPay ổn định.
- **Wishlist:** cho Guest lưu lại chỗ ở yêu thích để quay lại đặt sau, tăng tỷ lệ chuyển đổi (conversion).
- **Tự động huỷ booking PENDING_PAYMENT quá hạn:** nếu Guest không thanh toán trong X phút hoặc VNPay không xác nhận, hệ thống tự huỷ để trả lại availability.
- **Rating trung bình được cache** thay vì tính lại mỗi lần load trang, phục vụ khi lượng review lớn.
- **Báo cáo xuất file (CSV/PDF) cho Admin:** phục vụ nhu cầu báo cáo định kỳ, đối soát doanh thu với Host.
- **Thông báo real-time (WebSocket):** thay vì chỉ gửi email, giúp Host/Guest nhận cập nhật trạng thái tức thời ngay trên giao diện.
- **Rate limiting cho đăng nhập:** chống tấn công dò mật khẩu bằng brute-force để bảo vệ tài khoản người dùng.
