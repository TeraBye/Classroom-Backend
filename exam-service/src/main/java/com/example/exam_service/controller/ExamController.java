package com.example.exam_service.controller;

import com.example.exam_service.dto.request.ExamCreationRequest;
import com.example.exam_service.dto.request.ExamSubmissionRequest;
import com.example.exam_service.dto.request.UpdateAnswerRequest;
import com.example.exam_service.dto.response.*;
import com.example.exam_service.entity.ExamSubmission;
import com.example.exam_service.service.ExamService;
import com.example.exam_service.service.ExamSubmissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamController {
    ExamService examService;
    ExamSubmissionService examSubmissionService;

    @PostMapping("/createExam")
    public ApiResponse<ExamViewResponse> createExam(@RequestBody ExamCreationRequest request){
        return ApiResponse.<ExamViewResponse>builder()
                .result(examService.createExam(request))
                .build();
    }

    @PostMapping("/doExam")
    public ApiResponse<ExamSubmissionViewResponse> doExam(@RequestBody ExamSubmissionRequest request){
        return ApiResponse.<ExamSubmissionViewResponse>builder()
                .result(examSubmissionService.getExamForStudents(request))
                .build();
    }

    @PutMapping("/updateAnswer")
    public ApiResponse<String> updateAnswer(@RequestBody UpdateAnswerRequest request){
        examSubmissionService.updateSelectedOption(request);
        return ApiResponse.<String>builder()
                .result("updated!")
                .build();
    }

    @PutMapping("/updateExamSubmission")
    public ApiResponse<ExamSubmissionResponse> updateExamSubmission(
            @RequestParam String student,
            @RequestParam Long examId){
        return ApiResponse.<ExamSubmissionResponse>builder()
                .result(examSubmissionService.updateExamSubmission(student, examId))
                .build();

    }

    @GetMapping("/getExamSubmission")
    public ApiResponse<ExamSubmissionViewResponse> getExamSubmission(
            @RequestParam String student,
            @RequestParam Long examId
    ){
        return ApiResponse.<ExamSubmissionViewResponse>builder()
                .result(examSubmissionService.getExamSubmission(student, examId))
                .build();
    }

    @GetMapping("/getExamsByExamId")
    public ApiResponse<List<ExamSubmissionResponse>> getExamsByClass(
            @RequestParam Long examId
    ){
        return ApiResponse.<List<ExamSubmissionResponse>>builder()
                .result(examSubmissionService.getExamsByClass(examId))
                .build();
    }

    @GetMapping("/getExamsByClass")
    public ApiResponse<List<ExamResponse>> getExamsByClass(
            @RequestParam int classId
    ){
        return ApiResponse.<List<ExamResponse>>builder()
                .result(examService.getExamsByClass(classId))
                .build();
    }

    @GetMapping("/getStudentAnswer")
    public ApiResponse<FinalStudentExamViewResponse> getStudentAnswer(
            @RequestParam String student,
            @RequestParam Long examId
    ){
        return ApiResponse.<FinalStudentExamViewResponse>builder()
                .result(examSubmissionService.getStudentAnswer(student, examId))
                .build();
    }

    @GetMapping("/isProblemExam")
    public ApiResponse<ProblemExamCheck> isProblemExam(
            @RequestParam String student,
            @RequestParam Long examId
    ){
        return ApiResponse.<ProblemExamCheck>builder()
                .result(examSubmissionService.isProblemExam(student, examId))
                .build();
    }

    @GetMapping("/{questionId}/isQuestionInUnstartedExam")
    public ApiResponse<QuestionInUnstartedExamCheck> isQuestionInUnstartedExam(@PathVariable int questionId) {
        return ApiResponse.<QuestionInUnstartedExamCheck>builder()
                .result(examService.isQuestionInUnstartedExam(questionId))
                .build();
    }

}
