package com.example.springSecurity.repository;

import com.example.springSecurity.bean.entity.ApplicationUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ApplicationUserRepository {
    public Optional<ApplicationUser> getUserByUserName(String userName);
}
