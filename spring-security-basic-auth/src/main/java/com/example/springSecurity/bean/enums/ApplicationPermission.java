package com.example.springSecurity.bean.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum ApplicationPermission implements GrantedAuthority {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_WRITE("course:write"),
    COURSE_READ("course:read");

    private final String name;

    ApplicationPermission(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
