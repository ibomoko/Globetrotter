package com.me.globetrotter.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileValidator implements ConstraintValidator<File, MultipartFile> {
    private String[] contentTypes;

    @Override
    public void initialize(File constraintAnnotation) {
        contentTypes = constraintAnnotation.contentTypes();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        if (ArrayUtils.isNotEmpty(contentTypes)) {
            return Arrays.stream(contentTypes)
                    .anyMatch(contentType -> contentType.equals(multipartFile.getContentType()));
        }

        return true;
    }
}
