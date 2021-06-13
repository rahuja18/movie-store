package com.maersk.moviestore.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({FIELD, ANNOTATION_TYPE, PARAMETER})
@Constraint(validatedBy = YearValidator.class)
public @interface ValidateYear {
    String message() default "Year must be 4 digits positive number";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
