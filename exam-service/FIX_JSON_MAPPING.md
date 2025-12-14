# Test Fix: JSON Field Mapping & Ratio Normalization

## Problem 1: JSON Field Mapping ✅ FIXED
ML API expects **snake_case** field names:
```json
{
    "avg_recent_score": 2.5,
    "hard_correct_ratio": 0.33,
    ...
}
```

But Feign Client was sending **camelCase**:
```json
{
    "avgRecentScore": 2.5,
    "hardCorrectRatio": 0.33,
    ...
}
```

### Solution
Added `@JsonProperty` annotations to `PredictRequest.java`:

```java
@JsonProperty("avg_recent_score")
private double avgRecentScore;
```

## Problem 2: Ratio Sum Not Equal to 1.0 ✅ FIXED
Question Service validates: `hardRatio + mediumRatio + easyRatio = 1.0`

But ML API returned: `0.135 + 0.248 + 0.618 = 1.001` (floating point error)

### Solution
Added ratio normalization in `ExamServiceImpl.createPracticeExam()`:

```java
// Normalize ratios để tổng = 1.0
double totalRatio = prediction.getEasyRatio() + prediction.getMediumRatio() + prediction.getHardRatio();
double easyRatio = prediction.getEasyRatio() / totalRatio;
double mediumRatio = prediction.getMediumRatio() / totalRatio;
double hardRatio = prediction.getHardRatio() / totalRatio;
// Now: easyRatio + mediumRatio + hardRatio = 1.0 (exactly)
```

## Test

### 1. Restart Exam Service
```powershell
# Stop current service (Ctrl+C)
# Then restart
cd C:\STUDY\SPRINGBOOT2025\Classroom-Backend\exam-service
.\mvnw.cmd spring-boot:run
```

### 2. Test Create Practice Exam
```powershell
$body = @{
    title = "Bài luyện tập Test Fix"
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

# Show result
Write-Host "Success! Exam ID: $($response.result.exam.id)"
Write-Host "Questions count: $($response.result.questions.Count)"

# Show distribution
$response.result.questions | Group-Object -Property level | 
    Select-Object @{N='Level';E={$_.Name}}, @{N='Count';E={$_.Count}} | 
    Format-Table -AutoSize
```

### 3. Verify Logs
Look for:
```
INFO: Calling ML Predict API with payload: PredictRequest(...)
INFO: ML Predict API response: ProficiencyPredictionResponse(...)
INFO: Creating practice exam for student: student001 with ratios - Easy: X.XX, Medium: X.XX, Hard: X.XX
```

### 4. Check Database
```sql
-- Get latest exam
SELECT * FROM exam ORDER BY id DESC LIMIT 1;

-- Check question distribution
SELECT q.level, COUNT(*) as count
FROM exam_question eq
JOIN question_service.question q ON eq.question_id = q.id
WHERE eq.exam_id = (SELECT MAX(id) FROM exam)
GROUP BY q.level;
```

## Expected Result

### ML API Response
```json
{
    "proficiency_pred": 3.04,
    "easy_ratio": 0.618,
    "medium_ratio": 0.248,
    "hard_ratio": 0.135
}
```

### Practice Exam (20 questions)
```
Level   Count
-----   -----
EASY    12-13  (62%)
MEDIUM  5      (25%)
HARD    2-3    (13%)
```

## If Still Fails

### Debug Step 1: Check Payload Format
Add this to PredictServiceImpl before calling ML API:

```java
log.info("Raw payload JSON: {}", objectMapper.writeValueAsString(request));
```

### Debug Step 2: Test ML API Directly
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

This should return:
```json
{
    "proficiency_pred": 3.04,
    "easy_ratio": 0.618,
    "medium_ratio": 0.248,
    "hard_ratio": 0.135
}
```

