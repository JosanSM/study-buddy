package com.josan.study_buddy.Repository;

import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_saveUser_ReturnSavedUser() {
        // Arrange
        User user = User.builder()
                .name("test user")
                .email("testemail@gmail.com")
                .user_tier("pro")
                .build();

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals("test user", user.getName());
        Assertions.assertEquals("testemail@gmail.com", user.getEmail());
        Assertions.assertEquals("pro", user.getUser_tier());
    }

    // generate unit tests for userRepository
    // follow the naming convention of
    // Do not mock repository layer
    // Use real H2 database
    // Use DataJpaTest only
    // Avoid service layer
    // Use JUnit 5
    // follow the AAA pattern
    // test both success and failure scenarios

    @Test
    public void UserRepository_findUserById_ReturnFoundUser() {
        // Arrange
        User user = User.builder()
                .name("test user")
                .email("testemail@gmail.com")
                .user_tier("pro")
                .build();
        userRepository.save(user);

        // Act
        User foundUser = userRepository.findById(user.getId()).orElse(null);

        // Assert
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals("test user", foundUser.getName());
        Assertions.assertEquals("testemail@gmail.com", foundUser.getEmail());
        Assertions.assertEquals("pro", foundUser.getUser_tier());
    }

    @Test
    public void UserRepository_deleteUserById_UserIsDeleted() {
        // Arrange
        User user = User.builder()
                .name("test user")
                .email("testemail@gmail.com")
                .user_tier("pro")
                .build();
        userRepository.save(user);

        // Act
        userRepository.deleteById(user.getId());

        // Assert
        Assertions.assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    public void UserRepository_existsById_ReturnsTrueIfUserExists() {
        // Arrange
        User user = User.builder()
                .name("test user")
                .email("testemail@gmail.com")
                .user_tier("pro")
                .build();
        userRepository.save(user);

        // Act
        boolean exists = userRepository.existsById(user.getId());

        // Assert
        Assertions.assertTrue(exists);
    }

    @Test
    public void UserRepository_existsById_ReturnsFalseIfUserDoesNotExist() {
        // Arrange
        Long nonExistentUserId = 999L;

        // Act
        boolean exists = userRepository.existsById(nonExistentUserId);

        // Assert
        Assertions.assertFalse(exists);
    }

    @Test
    public void UserRepository_findAll_ReturnsAllUsers() {
        // Arrange
        User user1 = User.builder()
                .name("test user 1")
                .email("testemail1@gmail.com")
                .user_tier("pro")
                .build();
        User user2 = User.builder()
                .name("test user 2")
                .email("testemail2@gmail.com")
                .user_tier("premium")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        List<User> allUsers = userRepository.findAll();

        // Assert
        Assertions.assertNotNull(allUsers);
        Assertions.assertEquals(2, allUsers.size());
        Assertions.assertEquals("test user 1", allUsers.get(0).getName());
        Assertions.assertEquals("test user 2", allUsers.get(1).getName());
    }

    @Test
    public void UserRepository_findById_ReturnsEmptyOptionalIfUserDoesNotExist() {
        // Arrange
        Long nonExistentUserId = 999L;

        // Act
        User foundUser = userRepository.findById(nonExistentUserId).orElse(null);

        // Assert
        Assertions.assertNull(foundUser);
    }

    @Test
    public void UserRepository_saveUser_UpdatesExistingUser() {
        // Arrange
        User user = User.builder()
                .name("original name")
                .email("original@gmail.com")
                .user_tier("pro")
                .build();
        userRepository.save(user);
        long savedId = user.getId();

        user.setName("updated name");
        user.setEmail("updated@gmail.com");
        user.setUser_tier("premium");

        // Act
        User updatedUser = userRepository.save(user);

        // Assert
        Assertions.assertEquals(savedId, updatedUser.getId());
        Assertions.assertEquals("updated name", updatedUser.getName());
        Assertions.assertEquals("updated@gmail.com", updatedUser.getEmail());
        Assertions.assertEquals("premium", updatedUser.getUser_tier());
        Assertions.assertEquals(1, userRepository.findAll().size());
    }

    @Test
    public void UserRepository_saveUser_SetsLastUpdatedOnCreate() {
        // Arrange
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        User user = User.builder()
                .name("test user")
                .email("testemail@gmail.com")
                .user_tier("pro")
                .build();

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        Assertions.assertNotNull(savedUser.getLast_updated());
        Assertions.assertTrue(savedUser.getLast_updated().isAfter(before));
    }
}