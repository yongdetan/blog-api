package com.yongde.blog.exception;

public class TagNameExistsException extends RuntimeException {

    private final String tagName;

    public TagNameExistsException(String tagName){
        super(String.format("Tag name '%s' already exists.", tagName));
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

}
