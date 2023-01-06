package com.example.springSecurity.repository;

import com.example.springSecurity.bean.entity.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import static com.example.springSecurity.bean.enums.ApplicationRole.*;

import java.util.*;

@Repository("fake")
public class ApplicationUserRepositoryImpl implements ApplicationUserRepository {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<ApplicationUser> getUserByUserName(String userName) {
        return getUsers().stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(userName))
                .findFirst();
    }

    private List<ApplicationUser> getUsers() {
        return Arrays.asList(
                ApplicationUser.builder()
                        .password(passwordEncoder.encode("password"))
                        .username("James_Bond")
                        .grantedAuthorities(MANAGER.getPermissions())
                        .isAccountNonExpired(false)
                        .isEnabled(true)
                        .isAccountNonLocked(false)
                        .isCredentialsNonExpired(false)
                        .build(),
                ApplicationUser.builder()
                        .password(passwordEncoder.encode("password"))
                        .username("George_Bonds")
                        .grantedAuthorities(TEACHER.getPermissions())
                        .isAccountNonExpired(false)
                        .isEnabled(true)
                        .isAccountNonLocked(false)
                        .isCredentialsNonExpired(false)
                        .build(),
                ApplicationUser.builder()
                        .password(passwordEncoder.encode("password"))
                        .username("Anna_Smith")
                        .grantedAuthorities(STUDENT.getPermissions())
                        .isAccountNonExpired(false)
                        .isEnabled(true)
                        .isAccountNonLocked(false)
                        .isCredentialsNonExpired(false)
                        .build()
        );
    }
}
