package com.yongde.validator.annotation;

import com.yongde.validator.impl.ExistingEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ExistingEmailValidator.class)
public @interface ExistingEmail {

    //error message
    String message() default "Email already exists";

    // groups and payload is required by jakarta.validation
    // groups is used to "limit" the validation execution on certain classes
    Class<?>[] groups() default {};
    // payload is used to add additional metadata to the validation.
    Class<? extends Payload>[] payload() default {};


}
