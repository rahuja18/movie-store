package com.maersk.moviestore.validator;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class YearValidator implements ConstraintValidator<ValidateYear, Integer> {

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
        return year != null && year.toString().length() == 4 && year.intValue() > 0;
    }
}
