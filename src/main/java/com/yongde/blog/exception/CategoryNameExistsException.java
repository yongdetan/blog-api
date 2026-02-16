package com.yongde.blog.exception;

public class CategoryNameExistsException extends RuntimeException {

    private final String categoryName;

    public CategoryNameExistsException(String categoryName){
        super(String.format("Category name '%s' already exists.", categoryName));
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

}
