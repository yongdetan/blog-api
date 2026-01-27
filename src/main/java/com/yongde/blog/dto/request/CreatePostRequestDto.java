package com.yongde.blog.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreatePostRequestDto(

        @NotBlank(message = ERROR_MESSAGE_TITLE_BLANK)
        @Size(min = 1, max = 255, message = ERROR_MESSAGE_TITLE_LENGTH)
        String title,

        @NotBlank(message = ERROR_MESSAGE_CONTENT_BLANK)
        @Size(max=5000, message = ERROR_MESSAGE_CONTENT_LENGTH)
        String content,

        @Nullable
        String category,

        @Nullable
        List<String> tags
) {

        private static final String ERROR_MESSAGE_TITLE_BLANK =
                "Title must not be blank.";

        private static final String ERROR_MESSAGE_TITLE_LENGTH =
                "Title must not exceed 255 characters.";

        private static final String ERROR_MESSAGE_CONTENT_BLANK =
                "Content must not be blank.";

        private static final String ERROR_MESSAGE_CONTENT_LENGTH =
                "Content must not exceed 5000 characters.";

}
