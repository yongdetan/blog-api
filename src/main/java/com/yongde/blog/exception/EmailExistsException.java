package com.yongde.blog.exception;

public class EmailExistsException extends RuntimeException {

    private final String email;

    public EmailExistsException(String email){
        super(String.format("Post with email '%s' already exists.",email));
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
