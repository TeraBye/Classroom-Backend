package com.example.question_service.validator.impl;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.validator.DistinctOptions;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.stream.Stream;

public class DistinctOptionsValidator implements ConstraintValidator<DistinctOptions, QuestionCreateRequest> {
    @Override
    public boolean isValid(QuestionCreateRequest request, ConstraintValidatorContext constraintValidatorContext) {
        long count = Stream.of(
                request.getOptionA(),
                request.getOptionB(),
                request.getOptionC(),
                request.getOptionD()
        ).distinct().count();
        return count == 4;
    }
}
