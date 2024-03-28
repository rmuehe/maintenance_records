package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.configuration.TestJpaAuditingConfig;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.repository.ItemRepository;
import com.ryanmuehe.maintenancerecords.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({UserServiceImpl.class, TestJpaAuditingConfig.class, ItemServiceImpl.class})

// Tests the User Service class
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Setup test data
        User user1 = new User();
        user1.setEmail("user1@user1.com");
        user1.setUsername("user1");
        user1.setPassword("password");
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("user2@user2.com");
        user2.setUsername("user2");
        user2.setPassword("password");
        userRepository.save(user2);
    }

    @Test
    @DisplayName("findAll should return all users")
    // provided sample users, the findAll() method should retrieve all of them
    void findAll_ShouldReturnAllUsers() {
        // Act
        List<User> users = userService.findAll();

        // Assert
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getUsername()).isEqualTo("user1");
        assertThat(users.get(1).getUsername()).isEqualTo("user2");
    }
}
