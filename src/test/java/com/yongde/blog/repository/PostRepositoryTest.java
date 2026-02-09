package com.yongde.blog.repository;

import com.yongde.blog.entity.Post;
import com.yongde.blog.entity.User;
import com.yongde.blog.enums.PostStatus;
import com.yongde.blog.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

// We are using DataJpaTest for testing for repo layer
// Technically, DataJpaTest already configures H2 db but we add AutoConfigureTestDatabase for clarity.
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    //helper methods
    private User persistUser(String firstName, String lastName, String email, String password, Role role) {
        User author = new User(firstName, lastName, email, password);
        author.setRole(role);
        return userRepository.save(author);
    }

    private Post persistPost(String title, String content, User author, PostStatus postStatus) {
        Post post = new Post(title, content, author);
        post.setPostStatus(postStatus);
        return postRepository.save(post);
    }

    @Test
    void findPostByIdAndAuthorId_validIdAndAuthorId_returnsPost() {

        //Arrange
        User author = persistUser(
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                "password",
                Role.USER
        );

        Post post = persistPost(
                "post",
                "content",
                author,
                PostStatus.PUBLIC
        );

        //Act
        Optional<Post> results = postRepository.findPostByIdAndAuthorId(post.getId(),author.getId());


        //Assertions
        Assertions.assertThat(results).isPresent();

        Post retrievedPost = results.get();

        Assertions.assertThat(retrievedPost.getId()).isEqualTo(post.getId());
        Assertions.assertThat(retrievedPost.getAuthor().getId()).isEqualTo(author.getId());

    }

    @Test
    public void findPostByIdAndAuthorId_nonExistentIdAndAuthorId_returnsEmpty() {
        //Arrange
        Long nonExistentPostId = 1234L;
        Long nonExistentAuthorId = 1234L;

        User author = persistUser(
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                "password",
                Role.USER
        );

        Post post = persistPost(
                "post",
                "content",
                author,
                PostStatus.PUBLIC
        );

        //Act
        Optional<Post> results =
                postRepository.findPostByIdAndAuthorId(nonExistentPostId, nonExistentAuthorId);

        //Assertions
        Assertions.assertThat(results).isEmpty();
    }

    @Test
    public void findAllByPostStatus_publicPostStatus_returnsListOfPost() {
        User author1 = persistUser(
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                "password",
                Role.USER
        );

        User author2 = persistUser(
                "De Yong",
                "Tan",
                "deyongtan@gmail.com",
                "password",
                Role.USER
        );

        Post post1 = persistPost(
                "post1",
                "content",
                author1,
                PostStatus.PUBLIC
        );

        Post post2 = persistPost(
                "post2",
                "content",
                author1,
                PostStatus.DRAFT
        );

        Post post3 = persistPost(
                "post3",
                "content",
                author2,
                PostStatus.PUBLIC
        );

        List<Post> retrievedPosts = postRepository.findAllByPostStatus(PostStatus.PUBLIC);

        Assertions.assertThat(retrievedPosts)
                .hasSize(2)
                .extracting(Post::getId)
                .containsExactlyInAnyOrder(
                        post1.getId(),
                        post3.getId()
                );
    }

    @Test
    public void findAllByPostStatus_publicPostStatus_returnsEmptyList() {
        User author1 = persistUser(
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                "password",
                Role.USER
        );

        User author2 = persistUser(
                "De Yong",
                "Tan",
                "deyongtan@gmail.com",
                "password",
                Role.USER
        );

        Post post1 = persistPost(
                "post1",
                "content",
                author1,
                PostStatus.DRAFT
        );

        Post post2 = persistPost(
                "post2",
                "content",
                author1,
                PostStatus.DRAFT
        );

        Post post3 = persistPost(
                "post3",
                "content",
                author2,
                PostStatus.PRIVATE
        );

        List<Post> retrievedPosts = postRepository.findAllByPostStatus(PostStatus.PUBLIC);

        Assertions.assertThat(retrievedPosts).isEmpty();

    }

    @Test
    public void findAllByAuthorId_validAuthorId_returnsListOfPost() {
        User author1 = persistUser(
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                "password",
                Role.USER
        );

        User author2 = persistUser(
                "De Yong",
                "Tan",
                "deyongtan@gmail.com",
                "password",
                Role.USER
        );

        Post post1 = persistPost(
                "post1",
                "content",
                author1,
                PostStatus.PUBLIC
        );

        Post post2 = persistPost(
                "post2",
                "content",
                author1,
                PostStatus.DRAFT
        );

        Post post3 = persistPost(
                "post3",
                "content",
                author2,
                PostStatus.PUBLIC
        );

        List<Post> retrievedPosts = postRepository.findAllByAuthorId(author1.getId());

        Assertions.assertThat(retrievedPosts)
                .hasSize(2)
                .extracting(Post::getId)
                .containsExactlyInAnyOrder(
                        post1.getId(),
                        post2.getId()
                );
    }

    @Test
    public void findAllByAuthorId_invalidAuthorId_returnsEmptyList() {
        Long nonExistentAuthorId = 1234L;
        User author1 = persistUser(
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                "password",
                Role.USER
        );

        Post post1 = persistPost(
                "post1",
                "content",
                author1,
                PostStatus.PUBLIC
        );

        Post post2 = persistPost(
                "post2",
                "content",
                author1,
                PostStatus.DRAFT
        );

        List<Post> retrievedPosts = postRepository.findAllByAuthorId(nonExistentAuthorId);

        Assertions.assertThat(retrievedPosts).isEmpty();
    }

}
