package com.yongde.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        Map<String, String> errorMessages =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                error -> {  //basically a lambda expression to create a functional interface that will be executed when we are trying to obtain our value for key.
                                    String message = error.getDefaultMessage();
                                    return message != null ? message : "Invalid value";
                                },
                                (existing, replacement) -> existing + ", " + replacement //merge function. occurs when there are multiple errors happening on the same field
                        ));

        ApiResponse errorResponse = new ApiResponse(Instant.now(), errorMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse> handlePostNotFoundException(PostNotFoundException ex) {

        Map<String, String> errorMessages = Map.of("post", ex.getMessage());

        ApiResponse errorResponse = new ApiResponse(Instant.now(), errorMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<ApiResponse> handleEmailExistsException(EmailExistsException ex) {

        Map<String, String> errorMessages = Map.of("email", ex.getMessage());

        ApiResponse errorResponse = new ApiResponse(Instant.now(), errorMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoryNameExistsException.class)
    public ResponseEntity<ApiResponse> handleCategoryNameExistsException(CategoryNameExistsException ex) {

        Map<String, String> errorMessages = Map.of("categoryName", ex.getMessage());

        ApiResponse errorResponse = new ApiResponse(Instant.now(), errorMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {

        Map<String, String> errorMessages = Map.of("category", ex.getMessage());

        ApiResponse errorResponse = new ApiResponse(Instant.now(), errorMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleBadJsonException(HttpMessageNotReadableException ex) {
        Map<String, String> errorMessages = Map.of("request", "Malformed JSON request body");

        ApiResponse errorResponse = new ApiResponse(Instant.now(), errorMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNameExistsException.class)
    public ResponseEntity<ApiResponse> handleTagNameExistsException(TagNameExistsException ex) {

        Map<String, String> errorMessages = Map.of("tagName", ex.getMessage());

        ApiResponse errorResponse = new ApiResponse(Instant.now(), errorMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
