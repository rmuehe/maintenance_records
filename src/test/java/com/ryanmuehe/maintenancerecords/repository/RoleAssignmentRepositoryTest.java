package com.ryanmuehe.maintenancerecords.repository;

import com.ryanmuehe.maintenancerecords.model.Role;
import com.ryanmuehe.maintenancerecords.model.RoleAssignment;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.repository.RoleAssignmentRepository;
import com.ryanmuehe.maintenancerecords.model.repository.RoleRepository;
import com.ryanmuehe.maintenancerecords.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class RoleAssignmentRepositoryTest {

    @Autowired
    private RoleAssignmentRepository roleAssignmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private User user;
    private Role role;

    @BeforeEach
    public void setup() {
        // Setup user
        user = new User();
        user.setEmail("sample@sample.com");
        user.setUsername("TestUser");
        user.setPassword("password");
        user.setDateCreated(LocalDateTime.now());
        user.setLastUpdated(LocalDateTime.now());
        user = userRepository.save(user);

        // Setup role
        role = new Role();
        role.setName("ROLE_USER");
        role = roleRepository.save(role);

        // Setup role assignment
        RoleAssignment roleAssignment = new RoleAssignment();
        roleAssignment.setUser(user);
        roleAssignment.setRole(role);
        roleAssignment.setDateCreated(LocalDateTime.now());
        roleAssignment.setLastUpdated(LocalDateTime.now());

        roleAssignmentRepository.save(roleAssignment);
    }

    @Test
    @DisplayName("Role Retrieval by User ID")
    public void whenFindRolesByUserId_thenReturnRoles() {
        List<Role> foundRoles = roleAssignmentRepository.findRolesByUserId(user.getId());
        assertEquals(1, foundRoles.size(), "The size of found roles should be 1");
        assertEquals("ROLE_USER", foundRoles.get(0).getName(), "The name of the role should be 'ROLE_USER'");
    }


    /*
    TODO: update global roles
    * Global role for the time being are RoleAssignments without duration and not set to an item Id
    * In the future, a User may have several global Roles and time-bound Role Assignments
    * This tests the query to find the set of all global roles a User has
    * */
    @Test
    @DisplayName("Global Role Retrieval by User ID")
    public void whenFindGlobalRolesByUserId_thenReturnGlobalRoles() {
        List<RoleAssignment> foundGlobalRoles = roleAssignmentRepository.findGlobalRolesByUserId(user.getId());
        assertThat(foundGlobalRoles).hasSize(1);
        assertThat(foundGlobalRoles.get(0).getRole().getName()).isEqualTo("ROLE_USER");
        assertThat(foundGlobalRoles.get(0).getItem()).isNull();
        assertThat(foundGlobalRoles.get(0).getStartTime()).isNull();
        assertThat(foundGlobalRoles.get(0).getEndTime()).isNull();
    }
}



//package com.ryanmuehe.maintenancerecords.repository;
//
//import com.ryanmuehe.maintenancerecords.model.Role;
//import com.ryanmuehe.maintenancerecords.model.RoleAssignment;
//import com.ryanmuehe.maintenancerecords.model.User;
//import com.ryanmuehe.maintenancerecords.model.repository.RoleAssignmentRepository;
//import com.ryanmuehe.maintenancerecords.model.repository.RoleRepository;
//import com.ryanmuehe.maintenancerecords.model.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@DataJpaTest
//public class RoleAssignmentRepositoryTest {
//
//    @Autowired
//    private RoleAssignmentRepository roleAssignmentRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//
//    private User user;
//    private Role role;
//
//    @BeforeEach
//    public void setup() {
//        // Setup user
//        user = new User();
//        user.setEmail("test@example.com");
//        user.setUsername("TestUser");
//        user.setPassword("password");
//        user.setDateCreated(LocalDateTime.now());
//        user.setLastUpdated(LocalDateTime.now());
//        user = userRepository.save(user);
//
//        // Setup role
//        role = new Role();
//        role.setName("ROLE_USER");
//        role = roleRepository.save(role);
//
//        // Setup role assignment
//        RoleAssignment roleAssignment = new RoleAssignment();
//        roleAssignment.setUser(user);
//        roleAssignment.setRole(role);
//        roleAssignment.setDateCreated(LocalDateTime.now());
//        roleAssignment.setLastUpdated(LocalDateTime.now());
//        roleAssignmentRepository.save(roleAssignment);
//    }
//
//    @Test
//    @DisplayName("Role Retrieval by User ID ")
//    public void whenFindRolesByUserId_thenReturnRoles() {
//        // provided a list of roles from the RoleAssignmentRepository query
//        //    @Query("SELECT r FROM RoleAssignment ra JOIN ra.role r WHERE ra.user.id = :userId")
//        List<Role> foundRoles = roleAssignmentRepository.findRolesByUserId(user.getId());
//
//        // The list should have one role
//        assertEquals(1, foundRoles.size(), "The size of found roles should be 1");
//        // The role should be "ROLE_USER"
//        assertEquals("ROLE_USER", foundRoles.get(0).getName(), "The name of the role should be 'ROLE_USER'");    }
//}