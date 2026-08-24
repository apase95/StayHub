# HƯỚNG DẪN BƯỚC ĐẦU (FIRST STEP) — DỰ ÁN STAYHUB

---

## BƯỚC 0: CHUẨN BỊ
1. Đảm bảo máy bạn đã cài đặt:
   - [Git](https://git-scm.com/)
   - **JDK 21** (hoặc bản LTS mới nhất project đang dùng)
   - **Maven** (hoặc dùng `./mvnw` có sẵn trong repo, không cần cài riêng)
   - **PostgreSQL** (hoặc Docker để chạy Postgres qua container — khuyến khích dùng Docker cho gọn)
2. **[QUAN TRỌNG]** Nhắn username Github của bạn để add quyền **Collaborator**.
3. Mở Terminal (hoặc Git Bash / VS Code Terminal / IntelliJ Terminal) lên để bắt đầu.

---

## BƯỚC 1: CLONE DỰ ÁN VỀ MÁY
Để lấy toàn bộ source code từ Github về máy tính của bạn, dùng lệnh `clone`.

```bash
# Clone dự án về máy
git clone https://github.com/apase95/StayHub.git

# Di chuyển vào thư mục dự án vừa tải về
cd StayHub
```

---

## BƯỚC 2: CHUYỂN SANG NHÁNH `dev` VÀ CẬP NHẬT CODE
Nhánh `dev` là nơi chứa code mới nhất của cả team. Mặc định khi clone về, bạn đang ở nhánh `main`. Hãy chuyển sang `dev` và cập nhật.

```bash
# Chuyển sang nhánh dev
git checkout dev

# Kéo (pull) code mới nhất từ kho lưu trữ (origin) nhánh dev về máy
git pull origin dev
```

---

## BƯỚC 3: TẠO NHÁNH LÀM VIỆC CÁ NHÂN
**⚠️ LUẬT CỦA TEAM:** Tuyệt đối KHÔNG code trực tiếp trên nhánh `main` hoặc `dev`. Bạn phải tạo một nhánh riêng từ `dev` để làm task của mình.

Giả sử bạn được giao task số 10 (TSK-010) — ví dụ "xây trang tìm kiếm property" — hãy tạo nhánh mới:

```bash
# Lệnh -b giúp tạo nhánh mới VÀ chuyển sang nhánh đó luôn
git checkout -b feature/TSK-010
```
*(Nếu bạn fix bug, hãy đặt tên là `fixbug/TSK-xxx`; nếu chỉ setup/cấu hình, đặt tên `chore/TSK-xxx`)*

---

## BƯỚC 4: CHẠY THỬ DỰ ÁN TRƯỚC KHI CODE
Trước khi bắt đầu sửa code, hãy đảm bảo project chạy được trên máy bạn:

```bash
# Nếu dùng Docker Compose để chạy Postgres (khuyến khích)
docker compose up -d db

# Copy file cấu hình mẫu và điền thông tin kết nối DB, mail... của bạn
cp src/main/resources/application-example.yml src/main/resources/application-local.yml

# Chạy ứng dụng Spring Boot (dùng Maven wrapper, không cần cài Maven riêng)
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Mặc định ứng dụng chạy tại `http://localhost:8080`. Thymeleaf sẽ tự reload lại view khi bạn sửa file `.html` (nếu bật devtools), nhưng nếu sửa code Java thì cần chạy lại app.

---

## BƯỚC 5: CODE, ADD VÀ COMMIT
Bây giờ bạn bắt đầu mở code lên và làm task của mình (tạo Controller, Service, template Thymeleaf...).
Sau khi code xong và chạy thử ngon lành:

```bash
# 1. Kiểm tra xem mình đã sửa những file nào
git status

# 2. Đưa TẤT CẢ các file đã sửa vào trạng thái chờ (Staging)
git add .
# Hoặc nếu chỉ muốn add từng file: git add src/main/java/com/stayhub/property/PropertyController.java

# 3. Đóng gói code (Commit) kèm theo lời nhắn theo chuẩn của team
git commit -m "feat: [TSK-010] tạo trang tìm kiếm property theo địa điểm"
```
*(Nhớ tuân thủ quy tắc ghi chú Commit: dùng `feat:`, `fix:`, `chore:`, `refactor:`, `docs:` kèm mã Task).*

---

## BƯỚC 6: PUSH CODE LÊN GITHUB (LẦN ĐẦU TIÊN)
Vì nhánh `feature/TSK-010` chỉ mới tồn tại trên máy tính của bạn, Github chưa hề biết đến nó. Lần đầu tiên đẩy code lên, bạn phải dùng cờ `-u` (upstream) để liên kết nhánh ở máy tính với nhánh trên Github.

```bash
git push -u origin feature/TSK-010
```
*Từ những lần push sau trên cùng nhánh này, bạn chỉ cần gõ ngắn gọn: `git push`.*

---

## BƯỚC 7: TẠO PULL REQUEST (PR) ĐỂ GỘP CODE
Code của bạn đã lên Github, nhưng nó vẫn nằm ở nhánh riêng của bạn. Để đưa code vào nhánh chung `dev`:

1. Lên trang Github của dự án.
2. Bạn sẽ thấy một nút màu xanh lá nổi bật: **"Compare & pull request"**. Bấm vào đó.
3. Đảm bảo nhánh gốc (base) là `dev`, nhánh so sánh (compare) là nhánh của bạn `feature/TSK-010`.
4. Viết mô tả ngắn gọn những gì bạn đã làm (kèm ảnh chụp màn hình UI nếu có sửa Thymeleaf).
5. Ở góc phải, mục **Reviewers**, hãy tag (chọn) tên một người bạn trong team để họ xem code giúp bạn.
6. Bấm **Create pull request**.

---

## MỘT SỐ QUY TẮC PHẢI NHỚ

*   **Luôn đồng bộ trước khi push:** Nếu task của bạn làm trong nhiều ngày, nhánh `dev` có thể đã được người khác cập nhật code mới (đặc biệt là các thay đổi schema DB qua Flyway). Thỉnh thoảng hãy chạy lệnh sau để kéo code mới từ `dev` vào nhánh của bạn, tránh bị conflict (xung đột):
    ```bash
    git pull origin dev
    ```
*   **Có migration mới (Flyway)?** Nếu sau khi pull `dev` thấy có file mới trong `src/main/resources/db/migration/`, hãy chạy lại app (Flyway tự apply migration khi start) trước khi tiếp tục code.
*   **Xoá nhánh sau khi xong việc:** Khi Pull Request của bạn đã được Merge, bạn có thể chuyển về nhánh `dev` và xoá nhánh cá nhân cũ trên máy tính cho đỡ rác:
    ```bash
    git checkout dev
    git pull origin dev
    git branch -d feature/TSK-010
    ```
*   **Gõ sai tên commit?** Đừng lo, lệnh này giúp bạn sửa lời nhắn commit cuối cùng (trước khi push):
    ```bash
    git commit --amend -m "lời-nhắn-mới-chính-xác-hơn"
    ```
---
