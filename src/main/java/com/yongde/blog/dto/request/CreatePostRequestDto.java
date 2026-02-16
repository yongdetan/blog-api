package com.yongde.blog.dto.request;

import com.yongde.blog.enums.PostStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreatePostRequestDto(

        @NotBlank(message = ERROR_MESSAGE_TITLE_BLANK)
        @Size(max = 255, message = ERROR_MESSAGE_TITLE_LENGTH)
        String title,

        @NotBlank(message = ERROR_MESSAGE_CONTENT_BLANK)
        @Size(min=1)
        String content,

        @Nullable
        String category,

        @Nullable
        List<String> tags,

        @NotNull(message = ERROR_MESSAGE_POST_STATUS_BLANK)
        PostStatus status
) {

        private static final String ERROR_MESSAGE_TITLE_BLANK =
                "Title must not be blank.";

        private static final String ERROR_MESSAGE_TITLE_LENGTH =
                "Title must not exceed 255 characters.";

        private static final String ERROR_MESSAGE_CONTENT_BLANK =
                "Content must not be blank.";

        private static final String ERROR_MESSAGE_POST_STATUS_BLANK =
                "Post status must not be blank.";
}
