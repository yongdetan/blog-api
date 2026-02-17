package com.yongde.blog.exception;

public class CategoryNotFoundException extends RuntimeException {

    private final Long categoryId;

    public CategoryNotFoundException(Long categoryId){
        super(String.format("Category with id '%s' does not exist.",categoryId));
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

}
