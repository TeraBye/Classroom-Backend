# API: Get Exams By Student

## Mô Tả

API này trả về danh sách tất cả các bài thi (exams) được tạo bởi một học sinh cụ thể. Điều này bao gồm cả:
- **Practice Exams** (Bài thi luyện tập) - Học sinh tự tạo
- Các bài thi khác mà học sinh đã tạo

## Endpoint

**GET** `/api/exam/getExamsByStudent`

## Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| student | String | Yes | Username của học sinh |

## Request Example

### URL
```
GET http://localhost:8088/api/exam/getExamsByStudent?student=student001
```

### cURL
```bash
curl -X GET "http://localhost:8088/api/exam/getExamsByStudent?student=student001"
```

### PowerShell
```powershell
Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=student001" -Method GET
```

## Response

### Success Response (200 OK)

```json
{
    "code": 1000,
    "message": "Success",
    "result": [
        {
            "id": 123,
            "title": "Bài luyện tập Toán học",
            "createdAt": "2025-12-09T14:30:00",
            "duration": 45,
            "numberOfQuestion": 20,
            "beginTime": "2025-12-15T10:00:00",
            "teacher": "student001",
            "classId": 1,
            "subjectId": 1
        },
        {
            "id": 124,
            "title": "Bài luyện tập Vật lý",
            "createdAt": "2025-12-09T15:00:00",
            "duration": 60,
            "numberOfQuestion": 30,
            "beginTime": "2025-12-16T10:00:00",
            "teacher": "student001",
            "classId": 1,
            "subjectId": 2
        }
    ]
}
```

### Empty Result
```json
{
    "code": 1000,
    "message": "Success",
    "result": []
}
```

## Response Fields

| Field | Type | Description |
|-------|------|-------------|
| id | Long | ID của bài thi |
| title | String | Tiêu đề bài thi |
| createdAt | LocalDateTime | Thời gian tạo bài thi |
| duration | Integer | Thời lượng làm bài (phút) |
| numberOfQuestion | Integer | Số lượng câu hỏi |
| beginTime | LocalDateTime | Thời gian bắt đầu làm bài |
| teacher | String | Username của người tạo (student) |
| classId | Integer | ID lớp học |
| subjectId | Integer | ID môn học |

## Use Cases

### 1. Xem Lịch Sử Practice Exams
```powershell
# Lấy tất cả bài thi luyện tập của học sinh
$exams = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=student001"

Write-Host "Student001 has created $($exams.result.Count) exam(s):"
$exams.result | ForEach-Object {
    Write-Host "  - $($_.title) (ID: $($_.id))"
}
```

### 2. Phân Tích Thói Quen Luyện Tập
```powershell
$exams = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=student001"

# Đếm số bài thi theo môn học
$bySubject = $exams.result | Group-Object -Property subjectId
Write-Host "Practice frequency by subject:"
$bySubject | ForEach-Object {
    Write-Host "  Subject $($_.Name): $($_.Count) exams"
}
```

### 3. Timeline Luyện Tập
```powershell
$exams = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=student001"

# Sắp xếp theo thời gian tạo
$timeline = $exams.result | Sort-Object -Property createdAt -Descending
Write-Host "Practice timeline (recent first):"
$timeline | Select-Object -First 5 | ForEach-Object {
    Write-Host "  $($_.createdAt) - $($_.title)"
}
```

## Query Database

```sql
-- Lấy tất cả exams của student001
SELECT * FROM exam 
WHERE teacher = 'student001' 
ORDER BY created_at DESC;

-- Đếm số bài thi theo môn học
SELECT subject_id, COUNT(*) as count
FROM exam 
WHERE teacher = 'student001'
GROUP BY subject_id;

-- Lấy bài thi gần nhất
SELECT * FROM exam 
WHERE teacher = 'student001' 
ORDER BY created_at DESC 
LIMIT 1;
```

## Comparison with Other APIs

| API | Purpose | Filter By |
|-----|---------|-----------|
| `/getExamsByClass` | Lấy exams của lớp | classId |
| **`/getExamsByStudent`** | Lấy exams của học sinh | student (teacher field) |
| `/getExamsByExamId` | Lấy submissions của 1 exam | examId |

## Notes

1. **Field `teacher`**: Trong practice exams, field này chứa student username
2. **All Types**: API trả về cả regular exams và practice exams của student
3. **Order**: Kết quả không được sort, frontend cần sort nếu cần
4. **Performance**: Query theo indexed field `teacher` nên rất nhanh

## Error Handling

### No Exams Found
```json
{
    "code": 1000,
    "message": "Success",
    "result": []
}
```
→ Học sinh chưa tạo bài thi nào

### Invalid Student
API vẫn trả về empty array, không throw error

## Testing

### Test 1: Student With Practice Exams
```powershell
# Tạo 2 practice exams
1..2 | ForEach-Object {
    $body = @{
        title = "Practice Exam $_"
        duration = 45
        numberOfQuestion = 20
        beginTime = "2025-12-15T10:00:00"
        student = "student001"
        classId = 1
        subjectId = 1
    } | ConvertTo-Json
    
    Invoke-RestMethod -Uri "http://localhost:8088/api/exam/createPracticeExam" `
        -Method POST -ContentType "application/json" -Body $body
}

# Lấy tất cả exams
$exams = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=student001"
Write-Host "Total exams: $($exams.result.Count)"
```

### Test 2: New Student (No Exams)
```powershell
$exams = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=newstudent999"
Write-Host "Should be empty: $($exams.result.Count -eq 0)"
```

## Integration Example

### React/Angular Frontend
```typescript
async function getStudentExams(student: string) {
    const response = await fetch(
        `http://localhost:8088/api/exam/getExamsByStudent?student=${student}`
    );
    const data = await response.json();
    return data.result; // Array of ExamResponse
}

// Usage
const exams = await getStudentExams('student001');
console.log(`Found ${exams.length} practice exams`);
```

### Java Service-to-Service
```java
@FeignClient(name = "exam-service", url = "${app.services.exam}")
public interface ExamClient {
    @GetMapping("/getExamsByStudent")
    ApiResponse<List<ExamResponse>> getExamsByStudent(@RequestParam String student);
}
```

## Performance

- **Response Time**: < 100ms (simple query with index)
- **Scalability**: Excellent (indexed query on `teacher` field)
- **Caching**: Recommended for frequent requests

## Related APIs

- `POST /createPracticeExam` - Create practice exam
- `GET /getExamsByClass` - Get exams by class
- `GET /getExamSubmission` - Get student's submission for an exam

---

**Status:** ✅ Ready to use
**Version:** 1.0.0
**Last Updated:** December 10, 2025

