    package com.yongde.blog.repository;

    import com.yongde.blog.TestcontainersConfiguration;
    import com.yongde.blog.entity.User;
    import com.yongde.blog.enums.Role;
    import org.assertj.core.api.Assertions;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
    import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
    import org.springframework.context.annotation.Import;

    import java.util.Optional;

    @DataJpaTest
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @Import(TestcontainersConfiguration.class)
    public class UserRepositoryTest {

        @Autowired
        private UserRepository userRepository;

        //helper methods
        private User persistUser(String firstName, String lastName, String email, String password, Role role) {
            User user = new User(firstName, lastName, email, password);
            user.setRole(role);
            return userRepository.save(user);
        }

        @Test
        public void findByEmail_existingEmail_returnsUser() {
            String email = "yongdetan@gmail.com";

            User savedUser = persistUser(
                    "Yong De",
                    "Tan",
                    email,
                    "password",
                    Role.USER
            );

            Optional<User> result = userRepository.findByEmail(email);

            Assertions.assertThat(result).isPresent();

            User retrievedUser = result.get();
            Assertions.assertThat(retrievedUser)
                    .usingRecursiveAssertion()
                    .isEqualTo(savedUser);
        }

        @Test
        public void findByEmail_nonExistentEmail_returnsEmpty() {
            String email = "yongdetan@gmail.com";

            Optional<User> result = userRepository.findByEmail(email);

            Assertions.assertThat(result).isEmpty();
        }

        @Test
        public void findById_existingId_returnsUser() {

            User savedUser1 = persistUser(
                    "Yong De",
                    "Tan",
                    "yongdetan@gmail.com",
                    "password",
                    Role.USER
            );

            User savedUser2 = persistUser(
                    "De Yong",
                    "Tan",
                    "deyongtan@gmail.com",
                    "password",
                    Role.USER
            );

            Optional<User> result = userRepository.findById(savedUser2.getId());

            Assertions.assertThat(result).isPresent();

            User retrievedUser = result.get();
            Assertions.assertThat(retrievedUser)
                    .usingRecursiveAssertion()
                    .isEqualTo(savedUser2);
        }

        @Test
        public void findById_nonExistentId_returnsEmpty() {
            Long id = 999L;

            User savedUser = persistUser(
                    "Yong De",
                    "Tan",
                    "yongdetan@gmail.com",
                    "password",
                    Role.USER
            );

            Optional<User> result = userRepository.findById(id);

            Assertions.assertThat(result).isEmpty();
        }

    }
