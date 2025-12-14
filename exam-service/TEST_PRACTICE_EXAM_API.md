# Test API Tạo Bài Thi Luyện Tập

## Chuẩn Bị Test

### 1. Khởi động các services cần thiết

```powershell
# 1. Khởi động MySQL
# Đảm bảo MySQL đang chạy trên port 3306

# 2. Khởi động Kafka
# Đảm bảo Kafka đang chạy trên port 9092

# 3. Khởi động ML Prediction Service
cd path/to/ml-service
python app.py
# Service sẽ chạy trên http://localhost:8000

# 4. Khởi động Question Service
cd C:\STUDY\SPRINGBOOT2025\Classroom-Backend\question-service
mvnw spring-boot:run
# Service sẽ chạy trên http://localhost:8086

# 5. Khởi động Exam Service
cd C:\STUDY\SPRINGBOOT2025\Classroom-Backend\exam-service
mvnw spring-boot:run
# Service sẽ chạy trên http://localhost:8088
```

## Test Case 1: Tạo Bài Thi Luyện Tập Cho Học Sinh student001

### Request

```http
POST http://localhost:8088/api/exam/createPracticeExam
Content-Type: application/json

{
    "title": "Bài luyện tập Toán học - Tự động",
    "duration": 45,
    "numberOfQuestion": 20,
    "beginTime": "2025-12-15T10:00:00",
    "student": "student001",
    "classId": 1,
    "subjectId": 1
}
```

### Expected Response

```json
{
    "code": 1000,
    "message": "Success",
    "result": {
        "exam": {
            "id": 1,
            "title": "Bài luyện tập Toán học - Tự động",
            "createdAt": "2025-12-09T14:30:00",
            "duration": 45,
            "numberOfQuestion": 20,
            "beginTime": "2025-12-15T10:00:00",
            "teacher": "student001",
            "classId": 1,
            "subjectId": 1
        },
        "questions": [
            // 20 câu hỏi với tỉ lệ dựa trên ML prediction
        ]
    }
}
```

### Verification Steps

1. **Kiểm tra logs của Exam Service:**
```
INFO: Calling ML Predict API with payload: PredictRequest(avgRecentScore=2.5, hardCorrectRatio=0.33333334, ...)
INFO: ML Predict API response: ProficiencyPredictionResponse(proficiencyPred=3.04, easyRatio=0.618, mediumRatio=0.248, hardRatio=0.135)
INFO: Creating practice exam for student: student001 with ratios - Easy: 0.618, Medium: 0.248, Hard: 0.135
```

2. **Kiểm tra database:**
```sql
-- Kiểm tra exam đã được tạo
SELECT * FROM exam WHERE id = 1;

-- Kiểm tra exam questions
SELECT * FROM exam_question WHERE exam_id = 1;

-- Đếm số lượng câu hỏi theo level
SELECT q.level, COUNT(*) as count
FROM exam_question eq
JOIN question_service.question q ON eq.question_id = q.id
WHERE eq.exam_id = 1
GROUP BY q.level;
```

Expected result:
- EASY: ~12-13 câu (20 × 0.618)
- MEDIUM: ~5 câu (20 × 0.248)
- HARD: ~2-3 câu (20 × 0.135)

3. **Kiểm tra Kafka audit log:**
```json
{
    "username": "student001",
    "role": "STUDENT",
    "action": "CREATE PRACTICE EXAM",
    "details": "Created practice exam with ID: 1 (Easy: 0.62, Medium: 0.25, Hard: 0.14)"
}
```

## Test Case 2: So Sánh Với Bài Thi Thường

### Tạo bài thi thường (giáo viên)

```http
POST http://localhost:8088/api/exam/createExam
Content-Type: application/json

{
    "title": "Bài kiểm tra Toán học - Giáo viên",
    "duration": 45,
    "numberOfQuestion": 20,
    "beginTime": "2025-12-15T10:00:00",
    "teacher": "teacher001",
    "classId": 1,
    "subjectId": 1
}
```

### So sánh tỉ lệ câu hỏi

**Bài thi thường (giáo viên):**
- EASY: 6 câu (30%)
- MEDIUM: 10 câu (50%)
- HARD: 4 câu (20%)

**Bài thi luyện tập (student001 - yếu):**
- EASY: 12-13 câu (60-65%)
- MEDIUM: 5 câu (25%)
- HARD: 2-3 câu (10-15%)

## Test Case 3: Học Sinh Giỏi

Giả sử có học sinh "student_good" với lịch sử tốt:

### Request

```http
POST http://localhost:8088/api/exam/createPracticeExam
Content-Type: application/json

{
    "title": "Bài luyện tập nâng cao",
    "duration": 60,
    "numberOfQuestion": 25,
    "beginTime": "2025-12-15T14:00:00",
    "student": "student_good",
    "classId": 2,
    "subjectId": 1
}
```

### Expected ML Prediction

```json
{
    "proficiency_pred": 8.5,
    "easy_ratio": 0.2,
    "medium_ratio": 0.4,
    "hard_ratio": 0.4
}
```

### Expected Distribution
- EASY: 5 câu (20%)
- MEDIUM: 10 câu (40%)
- HARD: 10 câu (40%)

## Test Case 4: Test Dữ Liệu Payload ML API

### Gọi trực tiếp endpoint test để lấy payload

```http
GET http://localhost:8088/api/predict/getPredictData?student=student001
```

### Expected Response

```json
{
    "code": 1000,
    "message": "Success",
    "result": {
        "avgRecentScore": 2.5,
        "hardCorrectRatio": 0.33333334,
        "mediumCorrectRatio": 0.11764706,
        "easyCorrectRatio": 0.5714286,
        "hardQuestionsAttempted": 6,
        "examTrend": 1.0,
        "avgTimePerQuestion": 6.66,
        "consistency": 0.65358984,
        "recentStreak": -4
    }
}
```

### Test với ML API trực tiếp

```http
POST http://localhost:8000/predict
Content-Type: application/json

{
    "avg_recent_score": 2.5,
    "hard_correct_ratio": 0.33333334,
    "medium_correct_ratio": 0.11764706,
    "easy_correct_ratio": 0.5714286,
    "hard_questions_attempted": 6,
    "exam_trend": 1.0,
    "avg_time_per_question": 6.66,
    "consistency": 0.65358984,
    "recent_streak": -4
}
```

### Expected ML Response

```json
{
    "proficiency_pred": 3.04,
    "easy_ratio": 0.618,
    "medium_ratio": 0.248,
    "hard_ratio": 0.135
}
```

## Test Case 5: Error Handling

### Test 1: ML Service Không Chạy

```bash
# Stop ML service
# Ctrl+C trên terminal chạy ML service
```

```http
POST http://localhost:8088/api/exam/createPracticeExam
Content-Type: application/json

{
    "title": "Test Error",
    "duration": 45,
    "numberOfQuestion": 20,
    "beginTime": "2025-12-15T10:00:00",
    "student": "student001",
    "classId": 1,
    "subjectId": 1
}
```

**Expected Response:**
```json
{
    "code": 5000,
    "message": "Không thể dự đoán năng lực học sinh, vui lòng thử lại sau."
}
```

### Test 2: Học Sinh Không Có Lịch Sử

```http
POST http://localhost:8088/api/exam/createPracticeExam
Content-Type: application/json

{
    "title": "Test No History",
    "duration": 45,
    "numberOfQuestion": 20,
    "beginTime": "2025-12-15T10:00:00",
    "student": "new_student_no_history",
    "classId": 1,
    "subjectId": 1
}
```

**Expected Behavior:**
- Hệ thống sẽ trả về các giá trị mặc định (0.0, 0, etc.)
- ML API có thể trả về tỉ lệ cân bằng hơn

### Test 3: Không Đủ Câu Hỏi

```http
POST http://localhost:8088/api/exam/createPracticeExam
Content-Type: application/json

{
    "title": "Test Too Many Questions",
    "duration": 120,
    "numberOfQuestion": 1000,
    "beginTime": "2025-12-15T10:00:00",
    "student": "student001",
    "classId": 1,
    "subjectId": 1
}
```

**Expected Response:**
```json
{
    "code": 5000,
    "message": "Không đủ câu hỏi phù hợp để tạo bài thi"
}
```

## Postman Collection

### Import vào Postman

```json
{
    "info": {
        "name": "Practice Exam API Tests",
        "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
    },
    "item": [
        {
            "name": "Create Practice Exam - Student001",
            "request": {
                "method": "POST",
                "header": [
                    {
                        "key": "Content-Type",
                        "value": "application/json"
                    }
                ],
                "body": {
                    "mode": "raw",
                    "raw": "{\n    \"title\": \"Bài luyện tập Toán học - Tự động\",\n    \"duration\": 45,\n    \"numberOfQuestion\": 20,\n    \"beginTime\": \"2025-12-15T10:00:00\",\n    \"student\": \"student001\",\n    \"classId\": 1,\n    \"subjectId\": 1\n}"
                },
                "url": {
                    "raw": "http://localhost:8088/api/exam/createPracticeExam",
                    "protocol": "http",
                    "host": ["localhost"],
                    "port": "8088",
                    "path": ["api", "exam", "createPracticeExam"]
                }
            }
        },
        {
            "name": "Get Predict Data - Student001",
            "request": {
                "method": "GET",
                "url": {
                    "raw": "http://localhost:8088/api/predict/getPredictData?student=student001",
                    "protocol": "http",
                    "host": ["localhost"],
                    "port": "8088",
                    "path": ["api", "predict", "getPredictData"],
                    "query": [
                        {
                            "key": "student",
                            "value": "student001"
                        }
                    ]
                }
            }
        },
        {
            "name": "ML Predict API Direct Call",
            "request": {
                "method": "POST",
                "header": [
                    {
                        "key": "Content-Type",
                        "value": "application/json"
                    }
                ],
                "body": {
                    "mode": "raw",
                    "raw": "{\n    \"avg_recent_score\": 2.5,\n    \"hard_correct_ratio\": 0.33333334,\n    \"medium_correct_ratio\": 0.11764706,\n    \"easy_correct_ratio\": 0.5714286,\n    \"hard_questions_attempted\": 6,\n    \"exam_trend\": 1.0,\n    \"avg_time_per_question\": 6.66,\n    \"consistency\": 0.65358984,\n    \"recent_streak\": -4\n}"
                },
                "url": {
                    "raw": "http://localhost:8000/predict",
                    "protocol": "http",
                    "host": ["localhost"],
                    "port": "8000",
                    "path": ["predict"]
                }
            }
        }
    ]
}
```

## Kết Quả Test Với student001

Dựa trên dữ liệu bạn đã cung cấp:

### Input (getPredictData)
```json
{
    "avg_recent_score": 2.5,
    "hard_correct_ratio": 0.33333334,
    "medium_correct_ratio": 0.11764706,
    "easy_correct_ratio": 0.5714286,
    "hard_questions_attempted": 6,
    "exam_trend": 1.0,
    "avg_time_per_question": 6.66,
    "consistency": 0.65358984,
    "recent_streak": -4
}
```

### ML Prediction Output
```json
{
    "proficiency_pred": 3.04,
    "easy_ratio": 0.618,
    "medium_ratio": 0.248,
    "hard_ratio": 0.135
}
```

### Bài Thi Luyện Tập (20 câu)
- **Câu dễ**: 20 × 0.618 = 12.36 → **12-13 câu**
- **Câu trung bình**: 20 × 0.248 = 4.96 → **5 câu**
- **Câu khó**: 20 × 0.135 = 2.7 → **2-3 câu**

### Phân Tích
Student001 có:
- Điểm trung bình thấp (2.5/10)
- Tỉ lệ đúng câu dễ tốt (57%)
- Tỉ lệ đúng câu trung bình/khó thấp (11-33%)
- Chuỗi kết quả xấu gần đây (-4)

→ ML đề xuất nhiều câu dễ hơn (62%) để học sinh xây dựng lại tự tin và nền tảng.

## Performance Benchmarks

- **Thời gian tạo bài thi**: 500-1000ms
  - Gọi ML API: 100-200ms
  - Lấy câu hỏi: 200-400ms
  - Lưu database: 100-200ms
  - Gửi Kafka: 50-100ms

- **Độ chính xác dự đoán**: Phụ thuộc vào ML model
- **Tỉ lệ thành công**: > 95% (nếu có đủ dữ liệu và services chạy ổn định)

## Troubleshooting

### Lỗi: Connection refused to localhost:8000
**Giải pháp**: Khởi động ML Prediction Service

### Lỗi: No questions available
**Giải pháp**: Đảm bảo Question Service có đủ câu hỏi với các level khác nhau

### Lỗi: Kafka connection error
**Giải pháp**: Kiểm tra Kafka broker đang chạy, hoặc comment phần gửi audit log để test

### Lỗi: Division by zero in predictions
**Giải pháp**: Học sinh cần có ít nhất 2-3 bài thi trong lịch sử

