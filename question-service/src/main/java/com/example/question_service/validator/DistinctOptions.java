package com.example.question_service.validator;

import com.example.question_service.validator.impl.DistinctOptionsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DistinctOptionsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistinctOptions {
    String message() default "The options must be different";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
