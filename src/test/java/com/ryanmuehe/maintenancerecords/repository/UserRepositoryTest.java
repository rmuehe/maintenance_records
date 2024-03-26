package com.ryanmuehe.maintenancerecords.repository;

import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DisplayName("emailExists QUERY returns true for existing email")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenEmailExists_thenReturnTrue() {

        LocalDateTime localDateTime = LocalDateTime.now();
        // given a new user saved to an empty test database
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("USER");
        user.setPassword("password");
        user.setDateCreated(localDateTime);
        user.setLastUpdated(localDateTime);
        userRepository.save(user);

        // Expect true from UserRepository Query.EMAIL_EXISTS
        // "SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.email) = LOWER(?1)"
        assertEquals(true,
                userRepository.emailExists("test@example.com"),
                "emailExists() should return true"
        );
    }
}