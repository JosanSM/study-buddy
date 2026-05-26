package com.josan.study_buddy.Repository;

import com.josan.study_buddy.User.User;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import com.josan.study_buddy.User.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    public void UserRepository_saveUser_ReturnSavedUser() {
        // Arrange
        User user = User.builder()
                .id(1)
                .name("test user")
                .email("testemail@gmail.com")
                .user_tier("pro")
                .build();

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        Assertions.assertNotNull(savedUser);
    }
}
