package com.example.springSecurity.bean.enums;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.springSecurity.bean.enums.ApplicationPermission.*;

@Getter
public enum ApplicationRole {
    MANAGER(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
    TEACHER(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ)),
    STUDENT(Sets.newHashSet(COURSE_READ));

    private final Set<ApplicationPermission> permissions;

    ApplicationRole(Set<ApplicationPermission> permissions) {
        this.permissions = permissions;
    }

    // İstersek permissionı implement ederek oluşturabiliriz. İstersekte simple diyerek oluşturabiliriz.
    public List<SimpleGrantedAuthority> getPermissions() {
        List<SimpleGrantedAuthority> auths = permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toList());
        auths.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return auths;
    }
}
