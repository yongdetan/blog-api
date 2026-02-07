package com.yongde.blog.repository;

import com.yongde.blog.entity.Post;
import com.yongde.blog.enums.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findPostByIdAndAuthorId(Long id, Long authorId);

    List<Post> findAllByPostStatus(PostStatus postStatus);

    List<Post> findAllByAuthorId(Long authorId);

}
