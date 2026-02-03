package com.yongde.validator.impl;

import com.yongde.blog.repository.UserRepository;
import com.yongde.validator.annotation.ExistingEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistingEmailValidator implements ConstraintValidator<ExistingEmail, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        boolean exists = userRepository.findByEmail(email).isPresent();

        if (exists) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addConstraintViolation();
        }

        return !exists;
    }
}
