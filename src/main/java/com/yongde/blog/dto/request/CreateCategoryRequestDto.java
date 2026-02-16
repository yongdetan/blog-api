package com.yongde.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(

        @NotBlank(message = ERROR_MESSAGE_NAME_BLANK)
        @Size(max=50, message = ERROR_MESSAGE_NAME_LENGTH)
        String name,

        @NotBlank(message = ERROR_MESSAGE_DESCRIPTION_BLANK)
        @Size(max=500, message = ERROR_MESSAGE_DESCRIPTION_LENGTH)
        String description
) {

    private static final String ERROR_MESSAGE_NAME_BLANK =
            "Category name must not be blank.";

    private static final String ERROR_MESSAGE_NAME_LENGTH =
            "Category name must not exceed 50 characters.";

    private static final String ERROR_MESSAGE_DESCRIPTION_BLANK =
            "Description must not be blank.";

    private static final String ERROR_MESSAGE_DESCRIPTION_LENGTH =
            "Category name must not exceed 500 characters.";

}
