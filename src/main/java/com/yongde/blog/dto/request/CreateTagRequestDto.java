package com.yongde.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTagRequestDto(

        @NotBlank(message = ERROR_MESSAGE_NAME_BLANK)
        @Size(max=20, message = ERROR_MESSAGE_NAME_LENGTH)
        String name
) {

    private static final String ERROR_MESSAGE_NAME_BLANK =
            "Tag name must not be blank.";

    private static final String ERROR_MESSAGE_NAME_LENGTH =
            "Tag name must not exceed 20 characters.";
}
