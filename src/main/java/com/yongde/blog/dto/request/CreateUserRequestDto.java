package com.yongde.blog.dto.request;

import com.yongde.validator.annotation.PasswordMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@PasswordMatch(passwordField = "password", confirmPasswordField = "confirmPassword")
public record CreateUserRequestDto(

        @NotBlank(message = ERROR_MESSAGE_FIRSTNAME_BLANK)
        String firstName,

        @NotBlank(message = ERROR_MESSAGE_LASTNAME_BLANK)
        String lastName,

        @Email(message = ERROR_MESSAGE_EMAIL_INVALID)
        @NotBlank(message = ERROR_MESSAGE_EMAIL_BLANK)
        String email,

        @NotBlank(message = ERROR_MESSAGE_PASSWORD_BLANK)
        String password,

        @NotBlank(message = ERROR_MESSAGE_PASSWORD_BLANK)
        String confirmPassword

) {

    private static final String ERROR_MESSAGE_FIRSTNAME_BLANK =
            "First name must not be blank.";

    private static final String ERROR_MESSAGE_LASTNAME_BLANK =
            "Last name must not be blank.";

    private static final String ERROR_MESSAGE_EMAIL_BLANK =
            "Email must not be blank.";

    private static final String ERROR_MESSAGE_EMAIL_INVALID =
            "Email must not be invalid.";

    private static final String ERROR_MESSAGE_PASSWORD_BLANK =
            "Password must not be blank.";
    
}
