package com.rocksolid.security.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    REVIEWER_READ("reviewer:read"),
    REVIEWER_UPDATE("reviewer:update"),
    REVIEWER_CREATE("reviewer:create"),
    REVIEWER_DELETE("reviewer:delete"),

    STUDENT_READ("student:read"),
    STUDENT_UPDATE("student:update"),
    STUDENT_CREATE("student:create"),
    STUDENT_DELETE("student:delete");

    @Getter
    private final String permission;
}
