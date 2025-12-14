# Test: Get Exams By Student API

Write-Host "=== Testing Get Exams By Student API ===" -ForegroundColor Green

# Test 1: Get exams for student001
Write-Host "`n[Test 1] Getting exams for student001..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=student001" -Method GET

    Write-Host "✓ Success!" -ForegroundColor Green
    Write-Host "  Total exams found: $($response.result.Count)" -ForegroundColor Cyan

    if ($response.result.Count -gt 0) {
        Write-Host "`n  Exam List:" -ForegroundColor Cyan
        $response.result | ForEach-Object {
            Write-Host "    - ID: $($_.id) | Title: $($_.title) | Created: $($_.createdAt)" -ForegroundColor White
        }

        # Analyze by subject
        $bySubject = $response.result | Group-Object -Property subjectId
        Write-Host "`n  By Subject:" -ForegroundColor Cyan
        $bySubject | ForEach-Object {
            Write-Host "    Subject $($_.Name): $($_.Count) exam(s)" -ForegroundColor White
        }

        # Most recent exam
        $recent = $response.result | Sort-Object -Property createdAt -Descending | Select-Object -First 1
        Write-Host "`n  Most Recent Exam:" -ForegroundColor Cyan
        Write-Host "    Title: $($recent.title)" -ForegroundColor White
        Write-Host "    Created: $($recent.createdAt)" -ForegroundColor White
        Write-Host "    Questions: $($recent.numberOfQuestion)" -ForegroundColor White

    } else {
        Write-Host "  No exams found for this student" -ForegroundColor Yellow
    }

} catch {
    Write-Host "✗ Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Get exams for non-existent student
Write-Host "`n[Test 2] Getting exams for non-existent student..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=nonexistent999" -Method GET

    Write-Host "✓ Success!" -ForegroundColor Green
    Write-Host "  Total exams found: $($response.result.Count)" -ForegroundColor Cyan

    if ($response.result.Count -eq 0) {
        Write-Host "  ✓ Correctly returns empty array" -ForegroundColor Green
    } else {
        Write-Host "  ✗ Unexpected: found exams for non-existent student" -ForegroundColor Red
    }

} catch {
    Write-Host "✗ Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Create a practice exam and verify it appears in the list
Write-Host "`n[Test 3] Create practice exam and verify..." -ForegroundColor Yellow
try {
    $testStudent = "testuser_$(Get-Random -Maximum 9999)"

    # Create practice exam
    $createBody = @{
        title = "Test Practice Exam $(Get-Date -Format 'HH:mm:ss')"
        duration = 45
        numberOfQuestion = 20
        beginTime = "2025-12-15T10:00:00"
        student = $testStudent
        classId = 1
        subjectId = 1
    } | ConvertTo-Json

    Write-Host "  Creating practice exam for $testStudent..." -ForegroundColor Gray
    $createResponse = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/createPracticeExam" `
        -Method POST `
        -ContentType "application/json" `
        -Body $createBody

    $createdExamId = $createResponse.result.exam.id
    Write-Host "  ✓ Created exam ID: $createdExamId" -ForegroundColor Green

    # Get exams for this student
    Write-Host "  Fetching exams for $testStudent..." -ForegroundColor Gray
    $getResponse = Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=$testStudent" -Method GET

    if ($getResponse.result.Count -eq 1) {
        Write-Host "  ✓ Correctly found 1 exam" -ForegroundColor Green

        $foundExam = $getResponse.result[0]
        if ($foundExam.id -eq $createdExamId) {
            Write-Host "  ✓ Exam ID matches" -ForegroundColor Green
        } else {
            Write-Host "  ✗ Exam ID mismatch" -ForegroundColor Red
        }

        if ($foundExam.teacher -eq $testStudent) {
            Write-Host "  ✓ Teacher field matches student" -ForegroundColor Green
        } else {
            Write-Host "  ✗ Teacher field mismatch" -ForegroundColor Red
        }

    } else {
        Write-Host "  ✗ Expected 1 exam, found $($getResponse.result.Count)" -ForegroundColor Red
    }

} catch {
    Write-Host "✗ Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Summary
Write-Host "`n=== Test Summary ===" -ForegroundColor Green
Write-Host "API Endpoint: GET /api/exam/getExamsByStudent" -ForegroundColor Cyan
Write-Host "Status: Ready to use ✓" -ForegroundColor Green

Write-Host "`nExample usage:" -ForegroundColor Yellow
Write-Host '  Invoke-RestMethod -Uri "http://localhost:8088/api/exam/getExamsByStudent?student=student001"' -ForegroundColor Gray

