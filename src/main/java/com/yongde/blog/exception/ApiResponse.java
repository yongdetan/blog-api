package com.yongde.blog.exception;

import java.time.Instant;
import java.util.List;

public record ApiResponse(

        Instant timestamp,
        List<String> messages
) {
}
