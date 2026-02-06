package com.yongde.blog.repository;

import com.yongde.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findPostByIdAndAuthorId(Long id, Long authorId);
}
