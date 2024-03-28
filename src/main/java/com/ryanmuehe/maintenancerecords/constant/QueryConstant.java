package com.ryanmuehe.maintenancerecords.constant;

// Constants supplied for @Query annotated methods in repositories
public class QueryConstant {
    public static final String EMAIL_EXISTS = "SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.email) = LOWER(?1)";
    public static final String FIND_ROLES_BY_USER_ID = "SELECT r FROM RoleAssignment ra JOIN ra.role r " +
            "WHERE ra.user.id = :userId";

    public static final String FIND_GLOBAL_ROLES_BY_USER_ID =
    "SELECT ra FROM RoleAssignment ra WHERE ra.user.id = :userId AND ra.item " +
            "IS NULL AND ra.startTime IS NULL AND ra.endTime IS NULL";
}