package com.ryanmuehe.maintenancerecords.constant;

public class QueryConstant {
    public static final String EMAIL_EXISTS = "SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.email) = LOWER(?1)";
    public static final String FIND_ROLES_BY_USER_ID = "SELECT r FROM RoleAssignment ra JOIN ra.role r WHERE ra.user.id = :userId";
}