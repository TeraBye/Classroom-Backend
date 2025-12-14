# 🎯 QUICK REFERENCE - Create Practice Exam API

## 🚀 One-Line Test

```powershell
Invoke-RestMethod -Uri "http://localhost:8088/api/exam/createPracticeExam" -Method POST -ContentType "application/json" -Body '{"title":"Test","duration":45,"numberOfQuestion":20,"beginTime":"2025-12-15T10:00:00","student":"student001","classId":1,"subjectId":1}'
```

---

## 📝 API Summary

| Property | Value |
|----------|-------|
| **Endpoint** | `POST /api/exam/createPracticeExam` |
| **Port** | 8088 |
| **Context Path** | `/api/exam` |
| **Full URL** | `http://localhost:8088/api/exam/createPracticeExam` |

---

## 📥 Request

```json
{
    "title": "Bài luyện tập Toán học",
    "duration": 45,
    "numberOfQuestion": 20,
    "beginTime": "2025-12-15T10:00:00",
    "student": "student001",
    "classId": 1,
    "subjectId": 1
}
```

---

## 📤 Response (Success)

```json
{
    "code": 1000,
    "message": "Success",
    "result": {
        "exam": {
            "id": 123,
            "title": "Bài luyện tập Toán học",
            "teacher": "student001",
            "numberOfQuestion": 20,
            "duration": 45,
            ...
        },
        "questions": [ /* 20 questions */ ]
    }
}
```

---

## 🔧 Bugs Fixed

| # | Issue | Status |
|---|-------|--------|
| 1 | JSON field mapping (422) | ✅ Fixed |
| 2 | Ratio sum validation (500) | ✅ Fixed |

---

## 📊 Expected Results (student001)

```
EASY:   12 câu (60%)  ← More for weak student
MEDIUM: 5 câu (25%)
HARD:   3 câu (15%)   ← Less for confidence
```

---

## ⚡ Performance

- **Response Time:** 500-1000ms
- **ML API Call:** ~200ms
- **Question Fetch:** ~300ms
- **DB Save:** ~150ms

---

## 🐛 Troubleshooting

| Error | Solution |
|-------|----------|
| Connection refused (8000) | Start ML service |
| Connection refused (8086) | Start Question service |
| Field required | Already fixed with @JsonProperty |
| Ratio sum != 1.0 | Already fixed with normalization |

---

## 📚 Documentation

- `PRACTICE_EXAM_API.md` - Full API docs
- `BUG_FIXES_SUMMARY.md` - Bug fixes
- `test-practice-exam-fixed.ps1` - Test script

---

## ✅ Quick Verify

```sql
-- Latest exam
SELECT * FROM exam WHERE teacher = 'student001' ORDER BY id DESC LIMIT 1;

-- Question count by level
SELECT q.level, COUNT(*) FROM exam_question eq
JOIN question_service.question q ON eq.question_id = q.id
WHERE eq.exam_id = (SELECT MAX(id) FROM exam)
GROUP BY q.level;
```

---

**Ready to use!** 🎉

