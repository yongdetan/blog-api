package com.yongde.blog.exception;

public class PostNotFoundException extends RuntimeException {

    private final Long postId;

    public PostNotFoundException(Long postId){
        super(String.format("Post with id '%s' does not exist.",postId));
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

}
