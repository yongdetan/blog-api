package com.yongde.blog.repository;

import com.yongde.blog.entity.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

// We are using DataJpaTest for "unit testing" for repo layer
// Technically, DataJpaTest already configures H2 db but we add AutoConfigureTestDatabase for clarity.
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PostRepositoryTests {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void PostRepository_SavePost_ReturnsSavedPost() {
        //Using the Arrange-Act-Assert (AAA) pattern for unit testing.
        //Arrange
        Post post = Post.builder()
                .title("First Post")
                .content("Hello World!")
                .category("Test")
                .tags(List.of("First", "Test"))
                .build();

        //Act
        Post savedPost = postRepository.save(post);

        //Assert (using assertj assertion library)
        Assertions.assertThat(savedPost).isNotNull();
        Assertions.assertThat(savedPost.getId()).isGreaterThan(0);
        Assertions.assertThat(savedPost.getCreated()).isNotNull();
        Assertions.assertThat(savedPost.getUpdated()).isNotNull();
    }

}
