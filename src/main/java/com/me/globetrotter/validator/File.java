package com.me.globetrotter.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FileValidator.class})
public @interface File {
    String message() default "Content type is incorrect";
    String[] contentTypes() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
