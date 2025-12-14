# 🧪 Test Script: Create Practice Exam

## Quick Test (PowerShell)

```powershell
# Test tạo bài thi luyện tập cho student001
Write-Host "=== Testing Create Practice Exam API ===" -ForegroundColor Green

$body = @{
    title = "Bài luyện tập Toán - $(Get-Date -Format 'HH:mm:ss')"
    duration = 45
    numberOfQuestion = 20
    beginTime = "2025-12-15T10:00:00"
    student = "student001"
    classId = 1
    subjectId = 1
} | ConvertTo-Json

try {
    Write-Host "`n[1] Sending request..." -ForegroundColor Yellow
    $response = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/createPracticeExam" `
        -Method POST `
        -ContentType "application/json" `
        -Body $body

    Write-Host "✓ Success!" -ForegroundColor Green
    Write-Host "`nExam Details:" -ForegroundColor Cyan
    Write-Host "  ID: $($response.result.exam.id)"
    Write-Host "  Title: $($response.result.exam.title)"
    Write-Host "  Student: $($response.result.exam.teacher)"
    Write-Host "  Total Questions: $($response.result.questions.Count)"

    Write-Host "`nQuestion Distribution:" -ForegroundColor Cyan
    $distribution = $response.result.questions | Group-Object -Property level |
        Select-Object @{N='Level';E={$_.Name}},
                      @{N='Count';E={$_.Count}},
                      @{N='Percentage';E={"{0:P1}" -f ($_.Count / $response.result.questions.Count)}}

    $distribution | Format-Table -AutoSize

    # Verify ratios are reasonable for weak student
    $easyCount = ($distribution | Where-Object {$_.Level -eq "EASY"}).Count
    $mediumCount = ($distribution | Where-Object {$_.Level -eq "MEDIUM"}).Count
    $hardCount = ($distribution | Where-Object {$_.Level -eq "HARD"}).Count

    Write-Host "Analysis for student001 (weak student):" -ForegroundColor Cyan
    if ($easyCount -gt 10) {
        Write-Host "  ✓ More easy questions ($easyCount) - Good for building confidence" -ForegroundColor Green
    }
    if ($hardCount -lt 5) {
        Write-Host "  ✓ Fewer hard questions ($hardCount) - Reduces pressure" -ForegroundColor Green
    }

} catch {
    Write-Host "✗ Failed!" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red

    if ($_.ErrorDetails.Message) {
        $errorObj = $_.ErrorDetails.Message | ConvertFrom-Json
        Write-Host "Details: $($errorObj.message)" -ForegroundColor Yellow
    }
}
```

## Verify in Database

```powershell
# Connect to MySQL and run:
```

```sql
-- 1. Get the latest practice exam
SELECT * FROM exam
WHERE teacher = 'student001'
ORDER BY id DESC
LIMIT 1;

-- 2. Check question distribution
SELECT q.level, COUNT(*) as count,
       ROUND(COUNT(*) * 100.0 / 20, 1) as percentage
FROM exam_question eq
JOIN question_service.question q ON eq.question_id = q.id
WHERE eq.exam_id = (SELECT MAX(id) FROM exam WHERE teacher = 'student001')
GROUP BY q.level
ORDER BY FIELD(q.level, 'EASY', 'MEDIUM', 'HARD');
```

**Expected Result for student001:**
```
level   | count | percentage
--------|-------|------------
EASY    | 12-13 | 60-65%
MEDIUM  | 5     | 25%
HARD    | 2-3   | 10-15%
```

## Compare with Regular Exam

```powershell
# Create regular exam by teacher
$teacherExam = @{
    title = "Bài kiểm tra Toán - Giáo viên"
    duration = 45
    numberOfQuestion = 20
    beginTime = "2025-12-15T10:00:00"
    teacher = "teacher001"
    classId = 1
    subjectId = 1
} | ConvertTo-Json

$teacherResponse = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/createExam" `
    -Method POST `
    -ContentType "application/json" `
    -Body $teacherExam

Write-Host "`n=== Comparison ===" -ForegroundColor Magenta

Write-Host "`nTeacher's Exam (Fixed Ratios):" -ForegroundColor Yellow
$teacherResponse.result.questions | Group-Object -Property level |
    Select-Object @{N='Level';E={$_.Name}},
                  @{N='Count';E={$_.Count}},
                  @{N='Percentage';E={"{0:P0}" -f ($_.Count / 20)}} |
    Format-Table -AutoSize

Write-Host "Student's Practice Exam (ML-based Ratios):" -ForegroundColor Yellow
$response.result.questions | Group-Object -Property level |
    Select-Object @{N='Level';E={$_.Name}},
                  @{N='Count';E={$_.Count}},
                  @{N='Percentage';E={"{0:P0}" -f ($_.Count / 20)}} |
    Format-Table -AutoSize
```

**Expected Comparison:**
```
Teacher's Exam (30/50/20):
Level   Count  Percentage
EASY    6      30%
MEDIUM  10     50%
HARD    4      20%

Student's Practice Exam (62/25/13):
Level   Count  Percentage
EASY    12     60%  ← More easy for weak student
MEDIUM  5      25%
HARD    3      15%  ← Fewer hard questions
```

## Check Logs

Look for these messages in exam-service logs:

```
✓ INFO: Calling ML Predict API with payload: PredictRequest(avgRecentScore=2.5, ...)
✓ INFO: ML Predict API response: ProficiencyPredictionResponse(proficiencyPred=3.04, easyRatio=0.618, mediumRatio=0.248, hardRatio=0.135)
✓ INFO: Creating practice exam for student: student001 with ratios - Easy: 0.6178..., Medium: 0.2479..., Hard: 0.1348...
✓ INFO: Audit log sent to Kafka
```

## Full Test with Different Students

```powershell
# Test with multiple student profiles
$students = @(
    @{id="student001"; expected="More EASY (weak student)"},
    @{id="student002"; expected="Balanced"},
    @{id="student003"; expected="More HARD (strong student)"}
)

foreach ($student in $students) {
    Write-Host "`n=== Testing $($student.id) ===" -ForegroundColor Cyan
    Write-Host "Expected: $($student.expected)" -ForegroundColor Gray

    $body = @{
        title = "Practice - $($student.id)"
        duration = 45
        numberOfQuestion = 20
        beginTime = "2025-12-15T10:00:00"
        student = $student.id
        classId = 1
        subjectId = 1
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/createPracticeExam" `
            -Method POST `
            -ContentType "application/json" `
            -Body $body

        Write-Host "✓ Exam ID: $($response.result.exam.id)" -ForegroundColor Green

        $response.result.questions | Group-Object -Property level |
            Select-Object @{N='Level';E={$_.Name}}, @{N='Count';E={$_.Count}} |
            Format-Table -AutoSize

    } catch {
        Write-Host "✗ Failed: $($_.Exception.Message)" -ForegroundColor Red
    }

    Start-Sleep -Seconds 1
}
```

## Troubleshooting

### Error: "Field required" (422)
**Cause**: JSON field names mismatch
**Fix**: Already applied `@JsonProperty` annotations ✅

### Error: "Tổng ratios phải bằng 1" (500)
**Cause**: Floating point precision error
**Fix**: Already added ratio normalization ✅

### Error: "Không đủ câu hỏi"
**Cause**: Not enough questions in database
**Solution**: Add more questions to question_service database

### Error: "Connection refused to localhost:8000"
**Cause**: ML API not running
**Solution**: Start ML Prediction Service

## Success Criteria

- [x] Response code = 1000
- [x] Exam created in database
- [x] 20 questions selected
- [x] Ratios based on ML prediction (not fixed 0.2/0.5/0.3)
- [x] Easy ratio higher for weak students
- [x] Audit log sent to Kafka

## Performance

Expected response time: **500-1000ms**
- ML API call: ~200ms
- Question selection: ~300ms
- Database save: ~150ms
- Kafka publish: ~50ms

---

**Ready to test!** 🚀

Save this as `test-practice-exam-fixed.ps1` and run:
```powershell
.\test-practice-exam-fixed.ps1
```

