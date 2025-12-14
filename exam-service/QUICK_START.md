# Quick Start: Test API Tạo Bài Thi Luyện Tập

## Bước 1: Khởi Động Services (PowerShell)

```powershell
# Terminal 1: Khởi động Exam Service
cd C:\STUDY\SPRINGBOOT2025\Classroom-Backend\exam-service
.\mvnw.cmd spring-boot:run
```

## Bước 2: Kiểm Tra ML Prediction Service

```powershell
# Test ML API có hoạt động không
Invoke-RestMethod -Uri "http://localhost:8000/predict" `
    -Method POST `
    -ContentType "application/json" `
    -Body '{"avg_recent_score":7.5,"hard_correct_ratio":0.6,"medium_correct_ratio":0.75,"easy_correct_ratio":0.9,"hard_questions_attempted":20,"exam_trend":0.5,"avg_time_per_question":25,"consistency":0.8,"recent_streak":5}'
```

**Expected Output:**
```json
{
    "proficiency_pred": 7.2,
    "easy_ratio": 0.25,
    "medium_ratio": 0.45,
    "hard_ratio": 0.30
}
```

## Bước 3: Test API Tạo Bài Thi Luyện Tập

```powershell
# Tạo bài thi luyện tập cho student001
$body = @{
    title = "Bài luyện tập Toán học - Test"
    duration = 45
    numberOfQuestion = 20
    beginTime = "2025-12-15T10:00:00"
    student = "student001"
    classId = 1
    subjectId = 1
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/createPracticeExam" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body

# Hiển thị kết quả
Write-Host "Exam ID: $($response.result.exam.id)"
Write-Host "Number of Questions: $($response.result.questions.Count)"
Write-Host "Questions by Level:"

$levels = $response.result.questions | Group-Object -Property level | Select-Object Name, Count
$levels | Format-Table -AutoSize
```

## Bước 4: Kiểm Tra Kết Quả

### Xem logs của Exam Service
```
INFO: Calling ML Predict API with payload: ...
INFO: ML Predict API response: ...
INFO: Creating practice exam for student: student001 with ratios - Easy: X.XX, Medium: X.XX, Hard: X.XX
```

### Query database
```sql
-- Xem exam vừa tạo
SELECT * FROM exam ORDER BY id DESC LIMIT 1;

-- Đếm câu hỏi theo level
SELECT q.level, COUNT(*) as count
FROM exam_question eq
JOIN question_service.question q ON eq.question_id = q.id
WHERE eq.exam_id = (SELECT MAX(id) FROM exam)
GROUP BY q.level;
```

## Bước 5: So Sánh Với Bài Thi Thường

```powershell
# Tạo bài thi thường (giáo viên) để so sánh
$normalExam = @{
    title = "Bài kiểm tra Toán học - Giáo viên"
    duration = 45
    numberOfQuestion = 20
    beginTime = "2025-12-15T10:00:00"
    teacher = "teacher001"
    classId = 1
    subjectId = 1
} | ConvertTo-Json

$normalResponse = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/createExam" `
    -Method POST `
    -ContentType "application/json" `
    -Body $normalExam

Write-Host "`nNormal Exam (Teacher) - Question Distribution:"
$normalResponse.result.questions | Group-Object -Property level | Select-Object Name, Count | Format-Table
```

## Kết Quả Mong Đợi

### Bài Thi Thường (Fixed Ratio: 0.2/0.5/0.3)
```
Level  Count
-----  -----
EASY      6 (30%)
MEDIUM   10 (50%)
HARD     4 (20%)
```

### Bài Thi Luyện Tập student001 (ML-based)
Dựa trên năng lực yếu (avg_score: 2.5):
```
Level  Count
-----  -----
EASY     12 (60%)  ← Nhiều hơn để xây dựng nền tảng
MEDIUM    5 (25%)  ← Vừa phải
HARD      3 (15%)  ← Ít hơn để không áp lực
```

## Troubleshooting

### Lỗi: Connection refused (localhost:8000)
```powershell
# Kiểm tra ML service
Test-NetConnection -ComputerName localhost -Port 8000
```
**Solution**: Khởi động ML Prediction Service

### Lỗi: No such table 'exam'
```sql
-- Kiểm tra database schema
SHOW TABLES FROM exam_service;
```
**Solution**: Chạy exam-service để tự động tạo tables (ddl-auto: update)

### Lỗi: Kafka connection error
**Solution**: 
- Comment dòng `kafkaTemplate.send()` để test nhanh
- Hoặc khởi động Kafka broker

## Debug Tips

### 1. Xem payload gửi đến ML API
```powershell
Invoke-RestMethod -Uri "http://localhost:8088/api/predict/getPredictData?student=student001"
```

### 2. Test ML API trực tiếp
```powershell
$mlPayload = @{
    avg_recent_score = 2.5
    hard_correct_ratio = 0.33
    medium_correct_ratio = 0.12
    easy_correct_ratio = 0.57
    hard_questions_attempted = 6
    exam_trend = 1.0
    avg_time_per_question = 6.66
    consistency = 0.65
    recent_streak = -4
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8000/predict" `
    -Method POST `
    -ContentType "application/json" `
    -Body $mlPayload
```

### 3. Check service logs
```powershell
# Trong terminal chạy exam-service, tìm:
# - "Calling ML Predict API with payload"
# - "ML Predict API response"
# - "Creating practice exam for student"
```

## Success Criteria

✅ API return code 1000
✅ Exam được lưu vào database
✅ Số câu hỏi đúng với request (20 câu)
✅ Tỉ lệ câu hỏi khác với bài thi thường
✅ Audit log được gửi qua Kafka
✅ Logs hiển thị ML prediction ratios

## Full Test Script

```powershell
# Script hoàn chỉnh để test API
Write-Host "=== Testing Practice Exam API ===" -ForegroundColor Green

# 1. Test ML API
Write-Host "`n[1] Testing ML Prediction API..." -ForegroundColor Yellow
try {
    $mlTest = Invoke-RestMethod -Uri "http://localhost:8000/predict" `
        -Method POST `
        -ContentType "application/json" `
        -Body '{"avg_recent_score":7.5,"hard_correct_ratio":0.6,"medium_correct_ratio":0.75,"easy_correct_ratio":0.9,"hard_questions_attempted":20,"exam_trend":0.5,"avg_time_per_question":25,"consistency":0.8,"recent_streak":5}'
    Write-Host "✓ ML API is working" -ForegroundColor Green
    Write-Host "  Prediction: $($mlTest | ConvertTo-Json -Compress)"
} catch {
    Write-Host "✗ ML API failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 2. Create Practice Exam
Write-Host "`n[2] Creating Practice Exam for student001..." -ForegroundColor Yellow
$body = @{
    title = "Test Practice Exam $(Get-Date -Format 'yyyy-MM-dd HH:mm')"
    duration = 45
    numberOfQuestion = 20
    beginTime = "2025-12-15T10:00:00"
    student = "student001"
    classId = 1
    subjectId = 1
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/createPracticeExam" `
        -Method POST `
        -ContentType "application/json" `
        -Body $body
    
    Write-Host "✓ Practice Exam created successfully" -ForegroundColor Green
    Write-Host "  Exam ID: $($response.result.exam.id)"
    Write-Host "  Total Questions: $($response.result.questions.Count)"
    
    # 3. Analyze question distribution
    Write-Host "`n[3] Question Distribution:" -ForegroundColor Yellow
    $distribution = $response.result.questions | Group-Object -Property level | 
        Select-Object @{Name='Level';Expression={$_.Name}}, 
                      @{Name='Count';Expression={$_.Count}},
                      @{Name='Percentage';Expression={[math]::Round($_.Count / $response.result.questions.Count * 100, 1)}}
    
    $distribution | Format-Table -AutoSize
    
    Write-Host "`n✓ All tests passed!" -ForegroundColor Green
    
} catch {
    Write-Host "✗ Failed to create practice exam: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}
```

Save this as `test-practice-exam.ps1` and run:
```powershell
.\test-practice-exam.ps1
```

