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
- Nhận và xử lý **yêu cầu đặt phòng** từ Guest: chấp nhận hoặc từ chối.
- Theo dõi doanh thu, số lượng booking đang chờ/đã xác nhận.

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
5. **Bấm "Đặt phòng"** → chuyển sang trang đặt phòng, điền/xác nhận thông tin khách, xem bảng tổng hợp giá (giá phòng × số đêm + phí dọn dẹp + phí dịch vụ nền tảng = Tổng cộng).
6. **Xác nhận & thanh toán** — hệ thống xử lý thanh toán (ở bản MVP là mô phỏng, trạng thái luôn trả về SUCCESS ngay lập tức).
7. **Booking được tạo với trạng thái PENDING** — nghĩa là đã thanh toán thành công nhưng đang **chờ Host xác nhận**.
8. **Host xem yêu cầu và quyết định:**
   - Nếu **Host chấp nhận (Accept)** → booking chuyển sang **CONFIRMED**, Guest nhận thông báo qua email.
   - Nếu **Host từ chối (Reject)** → booking chuyển sang **REJECTED**.
9. **Guest có thể huỷ booking** ở trạng thái PENDING hoặc CONFIRMED → booking chuyển sang **CANCELLED**.
10. Sau ngày trả phòng thực tế, booking đã CONFIRMED sẽ được đánh dấu **COMPLETED**.
11. Khi booking đã COMPLETED, Guest có thể **viết đánh giá (Review)** cho chỗ ở đó — đánh giá này sẽ hiển thị công khai ở trang chi tiết chỗ ở, góp phần vào điểm rating trung bình.

Toàn bộ vòng đời trạng thái của một booking có thể tóm tắt như sau:

```
PENDING ──(Host Accept)──► CONFIRMED ──(đến ngày trả phòng)──► COMPLETED ──► có thể REVIEW
   │                            │
   └──(Host Reject)──► REJECTED │
   │                            │
   └────────(Guest Cancel)──────┘──► CANCELLED
```

## 4. Mô hình kinh doanh & dòng tiền

StayHub vận hành theo mô hình **thu phí hoa hồng trên mỗi giao dịch (transaction-based commission)**, cụ thể trong bảng giá hiển thị cho Guest tại bước thanh toán bao gồm 3 thành phần:

| Thành phần | Ý nghĩa | Ai hưởng |
|---|---|---|
| **Giá phòng × số đêm** | Doanh thu chính của Host | Host |
| **Phí dọn dẹp (Cleaning fee)** | Chi phí cố định Host đặt ra cho mỗi lượt khách | Host |
| **Phí dịch vụ StayHub (Service fee)** | Phí nền tảng thu trên mỗi giao dịch thành công | StayHub |

→ **Tổng tiền Guest trả = (Giá phòng × số đêm) + Phí dọn dẹp + Phí dịch vụ.**

## 5. Ba mảng nghiệp vụ cốt lõi

### 5.1. Property & Search (Chỗ ở & Tìm kiếm)
Đây là "kho hàng" của nền tảng. Mỗi **Property** (chỗ ở) thuộc về một Host, có các thông tin: loại hình, địa chỉ/thành phố, giá/đêm, sức chứa, số phòng ngủ/giường/phòng tắm, mô tả, danh sách ảnh, danh sách tiện nghi (Wi-Fi, hồ bơi, bãi đỗ xe, điều hoà, bếp, máy giặt, TV...).

Nghiệp vụ tìm kiếm cho phép Guest lọc theo nhiều tiêu chí cùng lúc (khoảng giá, loại hình, số phòng ngủ, tiện nghi, đánh giá tối thiểu) và sắp xếp kết quả — đây là phần phức tạp nhất về mặt truy vấn dữ liệu vì phải kết hợp điều kiện lọc **với** điều kiện chỗ ở phải **còn trống** trong khoảng ngày Guest chọn (không được trùng với booking nào đã CONFIRMED hoặc đang PENDING chờ xử lý).

### 5.2. Booking & Payment (Đặt phòng & Thanh toán)
Hai quy tắc quan trọng nhất:

- **Kiểm tra trùng lịch:** Trước khi cho phép tạo booking, hệ thống bắt buộc phải kiểm tra khoảng ngày [check-in, check-out] mà Guest chọn không được giao nhau với bất kỳ booking nào của cùng property đang ở trạng thái PENDING hoặc CONFIRMED. Nếu trùng, hệ thống từ chối và báo lỗi rõ ràng cho Guest biết (mã lỗi `ERR_ROOM_NOT_AVAILABLE`).
- **Tính giá:** Tổng tiền booking = (giá/đêm × số đêm) + phí dọn dẹp + phí dịch vụ. Logic này được tách riêng thành một service chuyên trách để dễ kiểm thử và thay đổi công thức tính giá sau này (ví dụ thêm giảm giá theo số đêm dài hạn, phụ phí cuối tuần...).

Thanh toán (Payment) là một domain con nằm cạnh Booking, ghi nhận: số tiền, phương thức thanh toán, trạng thái, thời điểm thanh toán. Ở bản MVP dùng "Mock Payment" — tự động trả kết quả thành công — nhưng thiết kế theo interface để sau này có thể thay bằng cổng thanh toán thật (VNPay/Momo) mà không phải sửa lại toàn bộ luồng Booking.

### 5.3. Review & Notification (Đánh giá & Thông báo)
- **Review** chỉ được phép tạo khi booking đã ở trạng thái COMPLETED — đảm bảo chỉ khách đã thực sự lưu trú mới có quyền đánh giá, tránh đánh giá ảo. Review gắn với booking cụ thể, có điểm số (rating) và bình luận, hiển thị lại trên trang chi tiết property.
- **Notification** được gửi tự động mỗi khi trạng thái booking thay đổi quan trọng: được Host xác nhận (CONFIRMED), bị từ chối (REJECTED), hoặc bị huỷ (CANCELLED) — giúp Guest và Host luôn nắm được tình trạng đặt phòng mà không cần chủ động vào lại hệ thống kiểm tra.


## 6. Vai trò của Host trong vận hành

Host không chỉ là người đăng tin mà còn là **người ra quyết định cuối cùng** cho mỗi booking. Điều này có nghĩa:

- Mỗi booking mới luôn bắt đầu ở trạng thái **PENDING**, dù Guest đã thanh toán thành công.
- Host có trách nhiệm xem và phản hồi các yêu cầu này trong Host Dashboard — nơi hiển thị danh sách property của Host và danh sách booking request kèm theo tên khách, ngày ở, số tiền.
- Nếu Host không phản hồi, booking vẫn ở trạng thái PENDING.

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

- **Host duyệt thủ công (Manual Accept/Reject)** thay vì tự động xác nhận: giúp mô hình MVP đơn giản hơn về mặt kỹ thuật (không cần xử lý real-time lock chỗ trống phức tạp), đồng thời phản ánh đúng hành vi thực tế của nhiều nền tảng lưu trú nhỏ tại Việt Nam, nơi Host thường muốn xem qua thông tin khách trước khi nhận.
- **Mock Payment trước, thanh toán thật sau:** cho phép đội dev tập trung hoàn thiện toàn bộ luồng nghiệp vụ (search, booking, trạng thái, review, notification) trước, tách rủi ro tích hợp cổng thanh toán thật (vốn tốn thời gian xử lý webhook, đối soát, bảo mật) sang giai đoạn sau — đúng tinh thần MVP.
- **Service fee cố định hiển thị minh bạch tại bước thanh toán:** giúp Guest hiểu rõ tiền của mình đi đâu, đồng thời đây là cách đơn giản nhất để mô hình hoá doanh thu nền tảng mà không cần hệ thống tính hoa hồng phức tạp theo tỷ lệ % biến động.
- **Review gắn chặt với Booking đã COMPLETED:** đảm bảo tính xác thực của đánh giá — quy tắc này là chuẩn ngành (Airbnb, Booking.com đều áp dụng) để tránh review giả mạo làm sai lệch uy tín Host.

## 10. Định hướng mở rộng

Sau khi project hoạt động ổn định, một số hướng phát triển thêm về mặt nghiệp vụ:

- **Thanh toán thật (VNPay/Momo):** thay thế Mock Payment, mở khoá khả năng thu tiền thật, cần thêm nghiệp vụ đối soát và hoàn tiền (refund) khi Host reject hoặc Guest huỷ.
- **Wishlist:** cho Guest lưu lại chỗ ở yêu thích để quay lại đặt sau, tăng tỷ lệ chuyển đổi (conversion).
- **Tự động huỷ booking PENDING quá hạn:** nếu Host không phản hồi trong X giờ, hệ thống tự huỷ và hoàn tiền, tránh Guest chờ đợi vô thời hạn.
- **Rating trung bình được cache** thay vì tính lại mỗi lần load trang, phục vụ khi lượng review lớn.
- **Báo cáo xuất file (CSV/PDF) cho Admin:** phục vụ nhu cầu báo cáo định kỳ, đối soát doanh thu với Host.
- **Thông báo real-time (WebSocket):** thay vì chỉ gửi email, giúp Host/Guest nhận cập nhật trạng thái tức thời ngay trên giao diện.
- **Rate limiting cho đăng nhập:** chống tấn công dò mật khẩu bằng brute-force để bảo vệ tài khoản người dùng.
