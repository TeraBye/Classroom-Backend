package com.example.classroom_service.controller;

import com.example.classroom_service.dto.request.StudentAddRequest;
import com.example.classroom_service.dto.request.ClassroomCreateRequest;
import com.example.classroom_service.dto.request.ClassroomUpdateRequest;
import com.example.classroom_service.dto.request.StudentRemoveRequest;
import com.example.classroom_service.dto.response.StudentResponse;
import com.example.classroom_service.dto.response.ApiResponse;
import com.example.classroom_service.dto.response.ClassroomResponse;
import com.example.classroom_service.service.ClassroomDetailService;
import com.example.classroom_service.service.ClassroomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Classroom Controller")
public class ClassroomController {
    ClassroomService classroomService;
    ClassroomDetailService classroomDetailService;

    @PostMapping("/create")
    public ApiResponse<ClassroomResponse> createClassroom(@RequestBody ClassroomCreateRequest request) {
        return ApiResponse.<ClassroomResponse>builder()
                .result(classroomService.createClassroom(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ClassroomResponse>> getAllClassrooms() {
        return ApiResponse.<List<ClassroomResponse>>builder()
                .result(classroomService.getAllClassrooms())
                .build();
    }

    @GetMapping("/{classroomId}")
    public ApiResponse<ClassroomResponse> getClassroomById(@PathVariable int classroomId) {
        return ApiResponse.<ClassroomResponse>builder()
                .result(classroomService.getClassroomById(classroomId))
                .build();
    }

    @PutMapping("/{classroomId}")
    public ApiResponse<ClassroomResponse> updateClassroom(@PathVariable int classroomId, @RequestBody ClassroomUpdateRequest request) {
        return ApiResponse.<ClassroomResponse>builder()
                .result(classroomService.updateClassroom(classroomId, request))
                .build();
    }

    @DeleteMapping("/{classroomId}")
    public ApiResponse<Void> deleteClassroom(@PathVariable int classroomId) {
        classroomService.deleteClassroom(classroomId);
        return ApiResponse.<Void>builder()
                .message("Delete classroom successfully")
                .build();
    }

    @PostMapping("/add-student")
    public ApiResponse<StudentResponse> addStudent(@RequestBody StudentAddRequest request) {
        return ApiResponse.<StudentResponse>builder()
                .result(classroomDetailService.addStudent(request))
                .build();
    }

    @DeleteMapping("/delete-student")
    public ApiResponse<Void> deleteStudent(@RequestBody StudentRemoveRequest request) {
        classroomDetailService.deleteStudent(request);
        return ApiResponse.<Void>builder()
                .message("Delete student successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<ClassroomResponse>> searchUsers(
            @RequestParam("q") String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.<Page<ClassroomResponse>>builder()
                .result(classroomService.searchClassrooms(q, page, size))
                .build();
    }

    @GetMapping("/teacher/{username}")
    public ApiResponse<Page<ClassroomResponse>> findClassroomsByTeacherUsername(
            @PathVariable String username,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.<Page<ClassroomResponse>>builder()
                .result(classroomService.findClassroomsByTeacherUsername(username, page, size))
                .build();
    }
}
