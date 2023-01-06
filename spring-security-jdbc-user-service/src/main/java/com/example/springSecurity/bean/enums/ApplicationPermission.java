package com.example.springSecurity.bean.enums;

import lombok.Getter;

@Getter
public enum ApplicationPermission {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_WRITE("course:write"),
    COURSE_READ("course:read");

    private final String permission;

    ApplicationPermission(String permission) {
        this.permission = permission;
    }
}
