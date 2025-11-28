package com.example.classroom_service.controller;

import com.example.classroom_service.dto.request.*;
import com.example.classroom_service.dto.response.*;
import com.example.classroom_service.service.ClassroomDetailService;
import com.example.classroom_service.service.ClassroomService;
import com.example.classroom_service.service.JoinRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Classroom Controller")
public class ClassroomController {
    ClassroomService classroomService;
    ClassroomDetailService classroomDetailService;
    JoinRequestService joinRequestService;

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

    @GetMapping("/student/{username}")
    public ApiResponse<Page<StudentResponse>> findStudentClasses(
            @PathVariable String username,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ApiResponse.<Page<StudentResponse>>builder()
                .result(classroomService.findStudentClasses(username, page, size))
                .build();
    }

    @GetMapping("/{classroomId}/students-of-class")
    public ApiResponse<Page<UserProfileResponse>> getStudentsOfClass(
            @PathVariable int classroomId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.<Page<UserProfileResponse>>builder()
                .result(classroomDetailService.getStudentsOfClass(classroomId, page, size))
                .build();
    }

    @GetMapping("/get-student-usernames/{classroomId}")
    public ApiResponse<List<String>> findStudentUsernamesByClassroomId(@PathVariable int classroomId) {
        return ApiResponse.<List<String>>builder()
                .result(classroomDetailService.findStudentUsernamesByClassroomId(classroomId))
                .build();
    }

    @PatchMapping("/{classroomId}/restore")
    public ApiResponse<Void> restore(@PathVariable int classroomId) {
        classroomService.restore(classroomId);
        return ApiResponse.<Void>builder()
                .message("Restore classroom successfully")
                .build();
    }

    @PostMapping("/get-list-subjects-by-classrooms")
    ApiResponse<List<SubjectWithClassroomResponse>> getListSubjectByClassrooms(
            @RequestBody List<Integer> listClassroomId
    ) {
        return ApiResponse.<List<SubjectWithClassroomResponse>>builder()
                .result(classroomService.getListSubjectsByClassrooms(listClassroomId))
                .build();
    }

    @PostMapping("/get-list-class")
    ApiResponse<List<Integer>> getListClass() {
        return ApiResponse.<List<Integer>>builder()
                .result(classroomService.getListClass())
                .build();
    }

    @GetMapping("/{classroomId}/join-requests")
    public ApiResponse<Page<JoinRequestResponse>> getJoinRequests(
            SearchJoinRequest request,
            Pageable pageable
    ) {
        return ApiResponse.<Page<JoinRequestResponse>>builder()
                .result(joinRequestService.getRequests(request, pageable))
                .build();
    }

    @PatchMapping("/{classroomId}/join-requests/{requestId}")
    public ApiResponse<JoinRequestResponse> updateStatus(
            @PathVariable("classroomId") Integer classroomId,
            @RequestBody UpdateJoinRequest updateJoinRequest,
            @PathVariable("requestId") Integer requestId
    ) {
        return ApiResponse.<JoinRequestResponse>builder()
                .result(joinRequestService.updateStatus(requestId, updateJoinRequest))
                .build();
    }
}
