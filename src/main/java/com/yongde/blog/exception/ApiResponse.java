package com.yongde.blog.exception;

import java.time.Instant;
import java.util.Map;

public record ApiResponse(

        Instant timestamp,
        Map<String, String> fieldErrors

) {
}
