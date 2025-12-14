# 🎯 Quick Reference: getExamsByStudent API

## One-Liner Test
```powershell
Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=student001"
```

## API Details
- **Method:** GET
- **URL:** `/api/exam/getExamsByStudent`
- **Port:** 8088
- **Parameter:** `student` (String, required)

## Response Structure
```json
{
    "code": 1000,
    "message": "Success",
    "result": [
        {
            "id": Long,
            "title": String,
            "createdAt": LocalDateTime,
            "duration": Integer,
            "numberOfQuestion": Integer,
            "beginTime": LocalDateTime,
            "teacher": String,  // = student username
            "classId": Integer,
            "subjectId": Integer
        }
    ]
}
```

## Common Queries

### Get all exams
```powershell
$exams = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=student001"
$exams.result.Count
```

### Filter by subject
```powershell
$exams = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=student001"
$mathExams = $exams.result | Where-Object { $_.subjectId -eq 1 }
```

### Get recent 5
```powershell
$exams = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=student001"
$recent = $exams.result | Sort-Object createdAt -Descending | Select-Object -First 5
```

## Database Query
```sql
SELECT * FROM exam WHERE teacher = 'student001';
```

## Files Modified
- ✅ ExamRepository.java
- ✅ ExamService.java
- ✅ ExamServiceImpl.java
- ✅ ExamController.java

## Status
✅ **READY TO USE**

