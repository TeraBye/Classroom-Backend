package com.example.classroom_service.mapper;

import com.example.classroom_service.dto.request.ClassroomCreateRequest;
import com.example.classroom_service.dto.request.ClassroomUpdateRequest;
import com.example.classroom_service.dto.response.ClassroomResponse;
import com.example.classroom_service.entity.Classroom;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
//- componentModel = "spring": yêu cầu MapStruct tạo ra Spring Bean,
// giúp bạn có thể inject mapper bằng @Autowired trong các service hoặc controller

public interface ClassroomMapper {
    Classroom toClassroom(ClassroomCreateRequest request);

    @Mapping(target = "studentNum", expression = "java(classroom.getStudentCount())")
    @Mapping(target = "postNum", ignore = true)
    @Mapping(target = "teacherUsername", source = "teacherSubject.teacherUsername")
    @Mapping(target = "subjectId", source = "teacherSubject.subject.id")
    @Mapping(target = "subjectName", source = "teacherSubject.subject.name")
    @Mapping(target = "teacherName", ignore = true)
    ClassroomResponse toClassroomResponse(Classroom classroom);

    List<ClassroomResponse> toClassroomResponses(List<Classroom> classrooms);

    void updateClassroom(@MappingTarget Classroom classroom, ClassroomUpdateRequest request);
}