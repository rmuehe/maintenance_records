package com.ryanmuehe.maintenancerecords.model.repository;

import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.constant.QueryConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

/**
 * Manages operations for User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Checks if a user with the specified email exists, case-insensitively.
     * This method supports ensuring unique email addresses during registration.
     *
     * @param email check database for email uniqueness
     * @return true only if database contains email, otherwise false
     */
//    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.email) = LOWER(?1)")
    @Query(QueryConstant.EMAIL_EXISTS)
    Boolean emailExists(String email);

    // returns an Optional User provided with an email.
    // Optionals are hedges against NullPointerException
    Optional<User> findByEmail(String email);
}