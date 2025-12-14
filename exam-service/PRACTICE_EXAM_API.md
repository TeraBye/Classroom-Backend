# API Tạo Bài Thi Luyện Tập Dựa Trên Dự Đoán ML

## Tổng Quan

API này cho phép học sinh tự tạo bài thi luyện tập với câu hỏi được chọn thông minh dựa trên năng lực học tập của họ. Hệ thống sử dụng Machine Learning để phân tích hiệu suất học tập gần đây và đề xuất tỉ lệ câu hỏi (dễ/trung bình/khó) phù hợp.

## Endpoint

### Tạo Bài Thi Luyện Tập
**POST** `/api/exam/createPracticeExam`

## Request Body

```json
{
    "title": "Bài thi luyện tập Toán học",
    "duration": 60,
    "numberOfQuestion": 20,
    "beginTime": "2025-12-10T10:00:00",
    "student": "student001",
    "classId": 1,
    "subjectId": 1
}
```

### Mô Tả Các Trường

| Trường | Kiểu | Bắt buộc | Mô tả |
|--------|------|----------|-------|
| title | String | Có | Tiêu đề bài thi |
| duration | Integer | Có | Thời lượng làm bài (phút) |
| numberOfQuestion | Integer | Có | Số lượng câu hỏi trong bài thi |
| beginTime | LocalDateTime | Có | Thời gian bắt đầu làm bài (ISO 8601 format) |
| student | String | Có | Username của học sinh tạo bài thi |
| classId | Integer | Có | ID của lớp học |
| subjectId | Integer | Có | ID của môn học |

## Response

```json
{
    "code": 1000,
    "message": "Success",
    "result": {
        "exam": {
            "id": 123,
            "title": "Bài thi luyện tập Toán học",
            "createdAt": "2025-12-09T14:30:00",
            "duration": 60,
            "numberOfQuestion": 20,
            "beginTime": "2025-12-10T10:00:00",
            "teacher": "student001",
            "classId": 1,
            "subjectId": 1
        },
        "questions": [
            {
                "id": 1,
                "question": "2 + 2 = ?",
                "optionA": "3",
                "optionB": "4",
                "optionC": "5",
                "optionD": "6",
                "correctAnswer": "B",
                "level": "EASY",
                "subjectId": 1
            },
            // ... more questions
        ]
    }
}
```

## Quy Trình Hoạt Động

### 1. Thu Thập Dữ Liệu Học Sinh
Hệ thống tự động thu thập các chỉ số sau từ lịch sử làm bài của học sinh:

- **avg_recent_score**: Điểm trung bình các bài thi gần đây
- **hard_correct_ratio**: Tỉ lệ trả lời đúng câu khó (0.0 - 1.0)
- **medium_correct_ratio**: Tỉ lệ trả lời đúng câu trung bình
- **easy_correct_ratio**: Tỉ lệ trả lời đúng câu dễ
- **hard_questions_attempted**: Số lượng câu khó đã làm
- **exam_trend**: Xu hướng điểm số (dương = cải thiện, âm = giảm sút)
- **avg_time_per_question**: Thời gian trung bình mỗi câu (giây)
- **consistency**: Độ ổn định trong các bài thi (0.0 - 1.0)
- **recent_streak**: Chuỗi điểm tốt/xấu gần đây

### 2. Gọi ML Prediction API
Hệ thống gửi dữ liệu đến ML API:

**POST** `http://localhost:8000/predict`

```json
{
    "avg_recent_score": 7.8,
    "hard_correct_ratio": 0.55,
    "medium_correct_ratio": 0.72,
    "easy_correct_ratio": 0.92,
    "hard_questions_attempted": 18,
    "exam_trend": 0.12,
    "avg_time_per_question": 21.5,
    "consistency": 0.83,
    "recent_streak": 6
}
```

### 3. Nhận Tỉ Lệ Câu Hỏi Từ ML
ML API trả về:

```json
{
    "proficiency_pred": 6.29,
    "easy_ratio": 0.423,
    "medium_ratio": 0.497,
    "hard_ratio": 0.081
}
```

### 4. Tạo Bài Thi
Hệ thống sử dụng các tỉ lệ này để chọn câu hỏi:
- Với 20 câu hỏi:
  - Câu dễ: 20 × 0.423 ≈ 8-9 câu
  - Câu trung bình: 20 × 0.497 ≈ 10 câu
  - Câu khó: 20 × 0.081 ≈ 1-2 câu

## Ví Dụ Sử Dụng

### Ví Dụ 1: Học Sinh Khá Giỏi
```bash
curl -X POST http://localhost:8088/api/exam/createPracticeExam \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Luyện tập Vật lý nâng cao",
    "duration": 90,
    "numberOfQuestion": 30,
    "beginTime": "2025-12-11T14:00:00",
    "student": "student_good",
    "classId": 2,
    "subjectId": 3
  }'
```

Kết quả dự đoán có thể là:
- Easy: 20% (6 câu)
- Medium: 50% (15 câu)
- Hard: 30% (9 câu)

### Ví Dụ 2: Học Sinh Cần Cải Thiện
```bash
curl -X POST http://localhost:8088/api/exam/createPracticeExam \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Luyện tập Toán cơ bản",
    "duration": 45,
    "numberOfQuestion": 15,
    "beginTime": "2025-12-11T16:00:00",
    "student": "student_weak",
    "classId": 1,
    "subjectId": 1
  }'
```

Kết quả dự đoán có thể là:
- Easy: 60% (9 câu)
- Medium: 30% (4-5 câu)
- Hard: 10% (1-2 câu)

## Xử Lý Lỗi

### Lỗi 1: Không Có Dữ Liệu Học Sinh
Nếu học sinh chưa có lịch sử làm bài:
```json
{
    "code": 5000,
    "message": "Không thể dự đoán năng lực học sinh, vui lòng thử lại sau."
}
```

### Lỗi 2: ML API Không Khả Dụng
Nếu không kết nối được với ML service:
```json
{
    "code": 5000,
    "message": "Không thể tạo bài thi luyện tập, vui lòng thử lại sau."
}
```

### Lỗi 3: Không Đủ Câu Hỏi
Nếu không có đủ câu hỏi theo tỉ lệ yêu cầu:
```json
{
    "code": 5000,
    "message": "Không đủ câu hỏi phù hợp để tạo bài thi"
}
```

## So Sánh Với API Tạo Bài Thi Thường

| Đặc điểm | createExam (Giáo viên) | createPracticeExam (Học sinh) |
|----------|------------------------|-------------------------------|
| Người tạo | Giáo viên | Học sinh |
| Tỉ lệ câu hỏi | Cố định (0.2/0.5/0.3) | Động, dựa trên ML |
| Mục đích | Kiểm tra chính thức | Luyện tập cá nhân |
| Phân tích | Không | Có (phân tích năng lực) |

## Cấu Hình

Đảm bảo ML Prediction Service chạy trên port 8000:

```yaml
# application.yaml
# Không cần cấu hình thêm vì PredictClient đã hardcode URL
```

## Logs & Monitoring

Hệ thống ghi log chi tiết:

```
INFO: Calling ML Predict API with payload: {...}
INFO: ML Predict API response: {...}
INFO: Creating practice exam for student: student001 with ratios - Easy: 0.423, Medium: 0.497, Hard: 0.081
```

Audit log được gửi qua Kafka:
```json
{
    "username": "student001",
    "role": "STUDENT",
    "action": "CREATE PRACTICE EXAM",
    "details": "Created practice exam with ID: 123 (Easy: 0.42, Medium: 0.50, Hard: 0.08)"
}
```

## Yêu Cầu Hệ Thống

1. **ML Prediction Service** phải chạy trên `http://localhost:8000`
2. **Question Service** phải có đủ câu hỏi với các level khác nhau
3. Học sinh phải có **ít nhất 2-3 bài thi** trong lịch sử để có dự đoán chính xác
4. **Kafka** phải chạy để ghi audit log

## Testing

Xem file `TEST_PRACTICE_EXAM_API.md` để biết chi tiết về test cases và kết quả.

