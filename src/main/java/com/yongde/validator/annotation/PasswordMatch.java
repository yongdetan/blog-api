package com.yongde.validator.annotation;

import com.yongde.validator.impl.PasswordMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE) //TYPE is used to apply annotation on the entire class. need to use TYPE instead of FIELD because we require 2 fields
@Retention(RetentionPolicy.RUNTIME) //RUNTIME means that java would keep this annotation around throughout the execution of the program
@Documented //Ensure that this annotation appears in JavaDoc when generating API Documentation
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordMatch {

    String message() default "Passwords do not match.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String passwordField();
    String confirmPasswordField();

}
