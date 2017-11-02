package com.miskevich.movieland.web.validator;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MovieValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(@Nullable Object target, Errors errors) {
        String methodForSorting = (String) target;
        if(!(methodForSorting.equals("acs") || methodForSorting.equals("desc"))){
            errors.reject("Incorrect method for sorting");
        }
    }
}