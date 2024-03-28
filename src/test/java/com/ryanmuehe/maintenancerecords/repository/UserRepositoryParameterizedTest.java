package com.ryanmuehe.maintenancerecords.repository;

import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("emailExists QUERY returns true for existing email")
// given an existing email in the database,
// method should report back that the email is taken already
public class UserRepositoryParameterizedTest {

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @ValueSource(strings = {"test@test.com", "void@void.com"})
    @DisplayName("Test emailExists method with different emails")
    public void whenEmailExists_thenReturnTrue_elseFalse() {
        LocalDateTime localDateTime = LocalDateTime.now();
        // given a new user saved to an empty test database
        User user = new User();
        user.setEmail("test@test.com");
        user.setUsername("TEST");
        user.setPassword("password");
        user.setDateCreated(localDateTime);
        user.setLastUpdated(localDateTime);
        userRepository.save(user);

        // Expect true from UserRepository Query.EMAIL_EXISTS
        // "SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.email) = LOWER(?1)"
        if ("test@test.com".equals(user.getEmail())) {
            assertTrue(userRepository.emailExists(user.getEmail()));
        } else {
            assertFalse(userRepository.emailExists(user.getEmail()));
        }

    }
}