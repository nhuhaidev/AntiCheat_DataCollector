# 🛡️ Anti-Cheat ML Pipeline: Behavioral Analytics in Game Server

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Python](https://img.shields.io/badge/Python-3.12-blue?style=flat-square&logo=python)
![SQL Server](https://img.shields.io/badge/SQL_Server-2022-red?style=flat-square&logo=microsoft-sql-server)
![Machine Learning](https://img.shields.io/badge/Machine_Learning-XGBoost%20%7C%20Random%20Forest-success?style=flat-square)

## 📌 1. Bối cảnh & Lý do chọn đề tài (Motivation)

Trong nền công nghiệp giải trí số hiện nay, gian lận (Cheat/Hack) gây ảnh hưởng nghiêm trọng đến trải nghiệm người dùng. Các phương pháp chống gian lận truyền thống chủ yếu dựa trên việc quét chữ ký phần mềm (Signature-based) tại máy khách (Client-side), dẫn đến việc dễ bị qua mặt và tiềm ẩn nguy cơ vi phạm quyền riêng tư.

Dự án này tiếp cận bài toán theo một hướng hiện đại và an toàn hơn: **Phân tích hành vi (Behavioral Analytics)** từ phía máy chủ (Server-side). Bằng việc thu thập log dữ liệu khổng lồ và ứng dụng Học máy (Machine Learning), hệ thống tự động nhận diện các mẫu hành vi bất thường của Bot hoặc người chơi gian lận một cách chủ động và bảo mật.

---

## 🎯 2. Mục tiêu & Phạm vi nghiên cứu (Scope & Objectives)

Dự án tập trung vào luồng xử lý Backend và Data Analytics, giới hạn mục tiêu phát hiện 3 loại gian lận vật lý phổ biến nhất:
*   ⛏️ **Auto-mine:** Bot đào tài nguyên, cày cuốc tự động / X-Ray.
*   ⚔️ **Killaura / Aimbot:** Tấn công tự động không cần góc nhìn chuẩn xác.
*   ⚡ **Speed Hack / Fly:** Di chuyển với gia tốc phi thực tế.

**Hệ thống được chia thành 3 phân hệ cốt lõi:**
1.  **Về hệ thống:** Tự triển khai máy chủ giả lập sinh tồn (Paper) và thiết lập Data Pipeline tự động.
2.  **Về dữ liệu (Feature Engineering):** Trích xuất các đặc trưng toán học từ tọa độ, sự kiện và thời gian thực.
3.  **Về phân tích (Machine Learning):** Xây dựng mô hình phân loại (Classification) xử lý tập dữ liệu mất cân bằng (Imbalanced Data) để phát hiện sớm các hành vi phi tự nhiên.

---

## 🛠️ 3. Công nghệ & Kiến trúc hệ thống (Tech Stack & Architecture)

### 🔹 Thu thập dữ liệu (Data Ingestion)
*   **Hạ tầng:** Lõi máy chủ Paper, thiết lập mạng cục bộ ảo bằng `Remote.It`.
*   **Thu thập (Logs):** Plugin Java ghi nhận các sự kiện: Di chuyển (Movement), Tương tác vật phẩm (Inventory), Đào khối (Mining), và Chiến đấu (Combat).
*   **Đầu ra:** Định dạng `JSONLines` thô.

### 🔹 Lưu trữ & Xử lý (Data Engineering / ETL)
*   **Cơ sở dữ liệu:** Microsoft SQL Server (Mô hình ERD tối ưu hóa đọc/ghi tốc độ cao).
*   **ETL Pipeline:** Sử dụng Python (`pandas`, `pyodbc`) và T-SQL để Trích xuất (Extract), Làm sạch (Transform), và Nạp (Load) dữ liệu tự động.

### 🔹 Học máy & Phân tích (Machine Learning)
*   **Xử lý Dữ liệu mất cân bằng:** Kỹ thuật **SMOTE** (Synthetic Minority Oversampling Technique).
*   **Mô hình Phân loại:** Cây quyết định **Random Forest** & **XGBoost**.
*   **Chỉ số đánh giá:** Tập trung tối ưu hóa **Recall** (giảm bỏ sót gian lận) trong khi duy trì **F1-score** ở mức cao.

---
## 🗓️ 4. Lộ trình phát triển

| Giai đoạn | Nội dung công việc | Kết quả đạt được (Deliverables) | Tiến độ |
| :--- | :--- | :--- | :---: |
| Tuần 1-2 | Cấu hình máy chủ Paper, tích hợp plugin ghi log | Thu được tập dữ liệu JSON thô | <input type="checkbox" checked disabled> Công việc đã hoàn thành|
| Tuần 3-4 | Thiết kế ERD, xây dựng ETL và nạp dữ liệu vào SQL | Cơ sở dữ liệu hoàn chỉnh | <input type="checkbox" checked disabled> Công việc đã hoàn thành|
| Tuần 5-6 | Phân tích gameplay và thực hiện Feature Engineering | Bộ dữ liệu đặc trưng hành vi | <input type="checkbox" disabled> |
| Tuần 7-8 | Áp dụng SMOTE, huấn luyện Random Forest và XGBoost | Mô hình học máy hoàn thiện | <input type="checkbox" disabled> |
| Tuần 9-10| Kiểm thử hệ thống với dữ liệu mới, hoàn thiện báo cáo | Báo cáo đồ án hoàn chỉnh | <input type="checkbox" disabled> |
---
## 📂 5. Cấu trúc Thư mục (Project Structure)

```text
DoAnNganh/
├── AntiCheat_DataCollector/      # Mã nguồn Java (Maven) của Plugin thu thập dữ liệu
│   ├── src/main/java/...         # Chứa các Listener (Movement, Combat, Mining, Inventory)
│   └── pom.xml                   # Cấu hình dependency của Paper API
│
├── PaperServer/                  # Môi trường chạy Game Server thực tế (Runtime)
│   └── plugins/DataCollector/    # Chứa file .jar đã Build và là nơi sinh file log JSONL thô
│
├── DataPipeline_Python/          # Phân hệ Data Engineering & Machine Learning
│   ├── etl_pipeline.py           # Script tự động xoay vòng log, làm sạch và nạp vào DB
│   └── ml_training.ipynb         # Jupyter Notebook trích xuất đặc trưng và huấn luyện mô hình
│
├── SQL_Scripts/                  # Chứa file T-SQL khởi tạo kiến trúc Database (Star-schema)
├── Start_System.bat              # Script One-Click khởi động đồng thời luồng ETL và Server
└── README.md                     # Tài liệu kỹ thuật tổng quan dự án
"Lưu ý không đẩy PaperServer lên"
```
---
## 📖 6. Cấu trúc Báo cáo Đồ án (Documentation)
*(Dành cho mục đích tham khảo học thuật)*
*   **Chương 1:** Tổng quan đề tài (Bối cảnh, Bài toán, Mục tiêu).
*   **Chương 2:** Cơ sở lý thuyết (Log Analytics, Data Pipeline, SMOTE, XGBoost).
*   **Chương 3:** Thiết kế hệ thống (Kiến trúc, ERD, Plugin Log, Anomaly Data Generation).
*   **Chương 4:** Trích xuất đặc trưng & Huấn luyện mô hình (Feature Engineering & Hyperparameter Tuning).
*   **Chương 5:** Đánh giá kết quả (So sánh hiệu năng) & Hướng phát triển Real-time Detection.
---
**Keywords:** `Anti-Cheat`, `Behavioral Analytics`, `Minecraft Server`, `Machine Learning`, `Random Forest`, `XGBoost`, `SMOTE`, `Data Pipeline`, `SQL Server`, `Feature Engineering`.
---
