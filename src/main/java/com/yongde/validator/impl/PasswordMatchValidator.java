package com.yongde.validator.impl;

import com.yongde.validator.annotation.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    public String passwordField;
    public String confirmPasswordField;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.passwordField = constraintAnnotation.passwordField();
        this.confirmPasswordField = constraintAnnotation.confirmPasswordField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Object password = new BeanWrapperImpl(value).getPropertyValue(passwordField);
        Object confirmPassword = new BeanWrapperImpl(value).getPropertyValue(confirmPasswordField);

        boolean valid = password != null && password.equals(confirmPassword);

        if (!valid) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(confirmPasswordField) // attach message to confirmPassword
                    .addConstraintViolation();
        }

        return valid;
    }

}
