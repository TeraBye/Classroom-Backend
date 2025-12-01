# Student Prediction Analytics API

## Overview
This module provides analytics for student performance prediction based on their exam history.

## API Endpoints

### Main Analytics Endpoint
**GET** `/predict/analytics?student={studentId}`

Returns all prediction analytics for a student in a single response:

```json
{
  "result": {
    "avgRecentScore": 8.5,
    "hardCorrectRatio": 0.65,
    "mediumCorrectRatio": 0.75,
    "easyCorrectRatio": 0.90,
    "hardQuestionsAttempted": 25,
    "examTrend": 1.2,
    "avgTimePerQuestion": 45.5,
    "consistency": 0.85,
    "recentStreak": 3
  }
}
```

### Individual Metric Endpoints

1. **GET** `/predict/avg-recent-score?student={studentId}` - Average score from recent exams
2. **GET** `/predict/hard-correct-ratio?student={studentId}` - Ratio of correct answers for HARD questions
3. **GET** `/predict/medium-correct-ratio?student={studentId}` - Ratio of correct answers for MEDIUM questions
4. **GET** `/predict/easy-correct-ratio?student={studentId}` - Ratio of correct answers for EASY questions
5. **GET** `/predict/hard-questions-attempted?student={studentId}` - Number of HARD questions attempted
6. **GET** `/predict/exam-trend?student={studentId}` - Performance trend (positive = improving, negative = declining)
7. **GET** `/predict/avg-time-per-question?student={studentId}` - Average time spent per question (seconds)
8. **GET** `/predict/consistency?student={studentId}` - Score consistency (higher = more consistent)
9. **GET** `/predict/recent-streak?student={studentId}` - Current streak (positive = passing streak, negative = failing streak)

## Analytics Explanation

### Metrics Description

- **avgRecentScore**: Average score from the 5 most recent exam submissions
- **hardCorrectRatio**: Percentage of HARD level questions answered correctly (0.0 - 1.0)
- **mediumCorrectRatio**: Percentage of MEDIUM level questions answered correctly (0.0 - 1.0)
- **easyCorrectRatio**: Percentage of EASY level questions answered correctly (0.0 - 1.0)
- **hardQuestionsAttempted**: Total number of HARD level questions the student has attempted
- **examTrend**: Difference between recent and older performance (positive means improvement)
- **avgTimePerQuestion**: Average time spent per question across recent exams (in seconds)
- **consistency**: Performance consistency measure (1.0 = perfectly consistent, closer to 0 = more variable)
- **recentStreak**: Current consecutive passing/failing streak (7.0+ is considered passing)

### Question Levels
The system recognizes three difficulty levels for questions:
- **EASY**: Basic level questions
- **MEDIUM**: Intermediate level questions  
- **HARD**: Advanced level questions

## Implementation Details

### Service Structure
- `PredictService`: Interface defining all analytics methods
- `PredictServiceImpl`: Implementation with actual calculation logic
- `PredictController`: REST controller exposing the endpoints
- `PredictResponse`: DTO for the combined analytics response

### Dependencies
- Uses `ExamSubmissionService` to get recent exam data
- Calls `QuestionClient` to retrieve question details and difficulty levels
- Queries `ExamAnswerRepository` for student answers

### Data Sources
- Recent exam submissions (last 5 submissions)
- Question metadata from question-service
- Student answer records from exam submissions
